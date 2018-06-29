package com.sohu.focus.salesmaster.kernal.imageloader.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.sohu.focus.salesmaster.kernal.log.Logger;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.StorageUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.sohu.focus.salesmaster.kernal.imageloader.compress.Preconditions.checkNotNull;
import static com.sohu.focus.salesmaster.kernal.utils.StorageUtil.DEFAULT_DISK_CACHE_DIR;

/**
 * 图片压缩类
 * 1.调用前先调用reset
 * 2.单个文件使用{@link OnCompressListener#onSuccess(CompressFileWrapper)}回调
 * 3.多个文件使用{@link OnCompressListener#onSuccess(List)}回调
 * 4.多种不同用途文件压缩时，使用包装类{@link CompressFileWrapper#tag}进行区分
 * Created by qiangzhao on 2016/12/27.
 */

public class CompressEngine {

    public static final int FIRST_GEAR = 1;
    public static final int THIRD_GEAR = 3;

    private static final String TAG = "CompressEngine";
    //缓存文件的后缀
    public static String COMPRESS_FILE_POSTFIX = "_compressed";

    private static volatile CompressEngine INSTANCE;

    private final File mCacheDir;

    private WeakReference<OnCompressListener> compressListenerRef;
    private int gear = THIRD_GEAR;
    private String filename;
    private long maxSize;

    private List<CompressFileWrapper> compressFiles = new CopyOnWriteArrayList<>();

    private AtomicInteger atomicInteger = new AtomicInteger();
    private int compressNum = -1;

    private CompressEngine(File cacheDir) {
        mCacheDir = cacheDir;
    }

    /**
     * Returns a directory with a default name in the private cache directory of the application to use to store
     * retrieved media and thumbnails.
     *
     * @param context A context.
     * @see #getPhotoCacheDir(android.content.Context, String)
     */
    public static File getPhotoCacheDir(Context context) {
        return getPhotoCacheDir(context, DEFAULT_DISK_CACHE_DIR);
    }

    /**
     * Returns a directory with the given name in the private cache directory of the application to use to store
     * retrieved media and thumbnails.
     *
     * @param context   A context.
     * @param cacheName The name of the subdirectory in which to store the cache.
     * @see #getPhotoCacheDir(android.content.Context)
     */
    public static File getPhotoCacheDir(Context context, String cacheName) {
        File cacheDir = StorageUtil.getCacheDir();
        if (cacheDir != null) {
            File result = new File(cacheDir, cacheName);
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
                // File wasn't able to create a directory, or the result exists but not a directory
                return null;
            }
            return result;
        }
        Logger.ZQ().e(TAG, "default disk cache dir is null");
        return null;
    }

    public static CompressEngine get(Context context) {
        if (INSTANCE == null)
            synchronized (CompressEngine.class) {
                if (INSTANCE == null)
                    INSTANCE = new CompressEngine(CompressEngine.getPhotoCacheDir(context));
            }
        return INSTANCE;
    }

    /**
     * 旋转图片
     * rotate the image with specified angle
     *
     * @param angle  the angle will be rotating 旋转的角度
     * @param bitmap target image               目标图片
     */
    private static Bitmap rotatingImage(int angle, Bitmap bitmap) {
        //rotate image
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        //create a new image
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 每次调用前要reset一次
     *
     * @return
     */
    public CompressEngine reset() {
        maxSize = 0;
        filename = "";
        gear = THIRD_GEAR;
        atomicInteger.set(0);
        compressNum = 0;
        compressFiles.clear();

        return this;
    }

    public void execute(CompressFileWrapper file) {
        if (compressNum != 0) {
            throw new IllegalAccessError("you must call rest first!");
        }
        Logger.ZQ().d(TAG, " begin compress!");
        compressNum = 1;
        launch(file);
    }

    public void execute(List<CompressFileWrapper> files) {
        if (compressNum != 0 || compressFiles.size() != 0) {
            throw new IllegalAccessError("you must call rest first!");
        }
        if (CommonUtils.isEmpty(files)) {
            Logger.ZQ().e(TAG, "files cannot be empty");
            return;
        }
        compressNum = files.size();
        Logger.ZQ().d(TAG, "files size is " + compressNum + " begin compress!");
        for (CompressFileWrapper file : files) {
            launch(file);
        }
    }

    private void launch(CompressFileWrapper mFile) {
        Preconditions.checkNotNull(mFile, "the image file cannot be null, please call .load() before this method!");
        if(compressListenerRef == null) {
            Logger.ZQ().e(TAG, "Compress Listener cant be null");
        }
        final OnCompressListener compressListener = compressListenerRef.get();
        if (compressListener != null) compressListener.onStart();

        if (gear == CompressEngine.FIRST_GEAR)
            Observable.just(mFile)
                    .map(new Func1<CompressFileWrapper, CompressFileWrapper>() {
                        @Override
                        public CompressFileWrapper call(CompressFileWrapper file) {
                            return firstCompress(file);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Logger.ZQ().d(TAG, "compress error msg is " + throwable.getMessage());
                            if (compressListener != null) compressListener.onError(throwable);
                        }
                    })
                    .onErrorResumeNext(Observable.<CompressFileWrapper>empty())
                    .filter(new Func1<CompressFileWrapper, Boolean>() {
                        @Override
                        public Boolean call(CompressFileWrapper file) {
                            return file.file != null && file.file.exists();
                        }
                    })
                    .subscribe(new Action1<CompressFileWrapper>() {
                        @Override
                        public void call(CompressFileWrapper file) {
                            atomicInteger.getAndIncrement();
                            Logger.ZQ().d(TAG, "compress done index is " + atomicInteger.get() + " tag is " + file.tag
                                    + "\n file path ===== " + file.file.getAbsolutePath());
                            if (compressListener != null) {
                                if (compressNum == 1) {
                                    compressListener.onSuccess(file);
                                } else {
                                    compressFiles.add(file);
                                    if (atomicInteger.getAndIncrement() == compressNum) {
                                        Logger.ZQ().d(TAG, "compress all done !!");
                                        compressListener.onSuccess(compressFiles);
                                    }
                                }
                            }
                        }
                    });
        else if (gear == CompressEngine.THIRD_GEAR)
            Observable.just(mFile)
                    .map(new Func1<CompressFileWrapper, CompressFileWrapper>() {
                        @Override
                        public CompressFileWrapper call(CompressFileWrapper file) {
                            return thirdCompress(file);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Logger.ZQ().d(TAG, "compress error msg is " + throwable.getMessage());
                            if (compressListener != null) compressListener.onError(throwable);
                        }
                    })
                    .onErrorResumeNext(Observable.<CompressFileWrapper>empty())
                    .filter(new Func1<CompressFileWrapper, Boolean>() {
                        @Override
                        public Boolean call(CompressFileWrapper file) {
                            return file.file != null && file.file.exists();
                        }
                    })
                    .subscribe(new Action1<CompressFileWrapper>() {
                        @Override
                        public void call(CompressFileWrapper file) {
                            atomicInteger.getAndIncrement();
                            Logger.ZQ().d(TAG, "compress done index is " + atomicInteger.get() + " tag is " + file.tag
                                    + " file name ===== " + file.file.getName());
                            if (compressListener != null) {
                                if (compressNum == 1) {
                                    Logger.ZQ().d(TAG, "compress all done !!");
                                    compressListener.onSuccess(file);
                                } else {
                                    compressFiles.add(file);
                                    if (atomicInteger.get() == compressNum) {
                                        Logger.ZQ().d(TAG, "compress all done !!");
                                        compressListener.onSuccess(compressFiles);
                                    }
                                }
                            }
                        }
                    });
    }

    public CompressEngine setCompressListener(OnCompressListener listener) {
        compressListenerRef = new WeakReference<>(listener);
        return this;
    }

    public CompressEngine putGear(int gear) {
        this.gear = gear;
        return this;
    }

    public CompressEngine setFilename(String filename) {
        if (CommonUtils.notEmpty(this.filename)) {
            throw new IllegalAccessError("you must call rest first!");
        }
        this.filename = filename;
        return this;
    }

    public CompressEngine maxKB(long maxKB) {
        if (maxSize != 0) {
            throw new IllegalAccessError("you must call rest first!");
        }
        this.maxSize = maxKB;
        return this;
    }

    public Observable<CompressFileWrapper> asObservable(CompressFileWrapper mFile) {
        if (gear == FIRST_GEAR)
            return Observable.just(mFile).map(new Func1<CompressFileWrapper, CompressFileWrapper>() {
                @Override
                public CompressFileWrapper call(CompressFileWrapper file) {
                    return firstCompress(file);
                }
            });
        else if (gear == THIRD_GEAR)
            return Observable.just(mFile).map(new Func1<CompressFileWrapper, CompressFileWrapper>() {
                @Override
                public CompressFileWrapper call(CompressFileWrapper file) {
                    return thirdCompress(file);
                }
            });
        else return Observable.empty();
    }

    private CompressFileWrapper thirdCompress(@NonNull CompressFileWrapper file) {
        String thumb = mCacheDir.getAbsolutePath() + File.separator + (TextUtils.isEmpty(filename) ? file.file.getName() + COMPRESS_FILE_POSTFIX : filename);

        double size;
        String filePath = file.file.getAbsolutePath();

        int angle = getImageSpinAngle(filePath);
        int width = getImageSize(filePath)[0];
        int height = getImageSize(filePath)[1];
        int thumbW = width % 2 == 1 ? width + 1 : width;
        int thumbH = height % 2 == 1 ? height + 1 : height;

        thumbW = getMaxSize((int) maxSize , thumbW);
        thumbH = getMaxSize((int) maxSize , thumbH);

        width = thumbW > thumbH ? thumbH : thumbW;
        height = thumbW > thumbH ? thumbW : thumbH;

        double scale = ((double) width / height);

        if (scale <= 1 && scale > 0.5625) {
            if (height < 1664) {
                if (file.file.length() / 1024 < 150) {
                    if (maxSize != 0 && file.file.length() / 1024 < maxSize - 1) {
                        Logger.ZQ().d(TAG, "file can be used directly, No need to compress tag is "
                                + file.tag + " size == " + file.file.length() / 1024 + "kb name ===" + file.file.getName());
                        return file;
                    } else if (maxSize == 0) {
                        Logger.ZQ().d(TAG, "file can be used directly, No need to compress tag is "
                                + file.tag + " size == " + file.file.length() / 1024 + "kb path ===" + file.file.getName());
                        return file;
                    }
                }

                size = (width * height) / Math.pow(1664, 2) * 150;
                size = size < 60 ? 60 : size;
            } else if (height >= 1664 && height < 4990) {
                thumbW = width / 2;
                thumbH = height / 2;
                size = (thumbW * thumbH) / Math.pow(2495, 2) * 300;
                size = size < 60 ? 60 : size;
            } else if (height >= 4990 && height < 10240) {
                thumbW = width / 4;
                thumbH = height / 4;
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 100 ? 100 : size;
            } else {
                int multiple = height / 1280 == 0 ? 1 : height / 1280;
                thumbW = width / multiple;
                thumbH = height / multiple;
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 100 ? 100 : size;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            if (height < 1280 && file.file.length() / 1024 < 200) {
                if (maxSize != 0 && file.file.length() / 1024 < maxSize) {
                    Logger.ZQ().d(TAG, "file can be used directly, No need to compress tag is "
                            + file.tag + " size == " + file.file.length() / 1024 + "kb name ===" + file.file.getName());
                    return file;
                } else if (maxSize == 0) {
                    Logger.ZQ().d(TAG, "file can be used directly, No need to compress tag is "
                            + file.tag + " size == " + file.file.length() / 1024 + "kb name ===" + file.file.getName());
                    return file;
                }
            }

            int multiple = height / 1280 == 0 ? 1 : height / 1280;
            thumbW = width / multiple;
            thumbH = height / multiple;
            size = (thumbW * thumbH) / (1440.0 * 2560.0) * 400;
            size = size < 100 ? 100 : size;
        } else {
            int multiple = (int) Math.ceil(height / (1280.0 / scale));
            thumbW = width / multiple;
            thumbH = height / multiple;
            size = ((thumbW * thumbH) / (1280.0 * (1280 / scale))) * 500;
            size = size < 100 ? 100 : size;
        }

        return compress(file, thumb, thumbW, thumbH, angle, (long) size);
    }

    private CompressFileWrapper firstCompress(@NonNull CompressFileWrapper file) {
        int minSize = 60;
        int longSide = 720;
        int shortSide = 1280;

        String filePath = file.file.getAbsolutePath();
        String thumbFilePath = mCacheDir.getAbsolutePath() + File.separator + (TextUtils.isEmpty(filename) ? file.file.getName() + "_compressed" : filename);

        long size = 0;
        long maxSize = file.file.length() / 5;

        int angle = getImageSpinAngle(filePath);
        int[] imgSize = getImageSize(filePath);
        int width = 0, height = 0;
        if (imgSize[0] <= imgSize[1]) {
            double scale = (double) imgSize[0] / (double) imgSize[1];
            if (scale <= 1.0 && scale > 0.5625) {
                width = imgSize[0] > shortSide ? shortSide : imgSize[0];
                height = width * imgSize[1] / imgSize[0];
                size = minSize;
            } else if (scale <= 0.5625) {
                height = imgSize[1] > longSide ? longSide : imgSize[1];
                width = height * imgSize[0] / imgSize[1];
                size = maxSize;
            }
        } else {
            double scale = (double) imgSize[1] / (double) imgSize[0];
            if (scale <= 1.0 && scale > 0.5625) {
                height = imgSize[1] > shortSide ? shortSide : imgSize[1];
                width = height * imgSize[0] / imgSize[1];
                size = minSize;
            } else if (scale <= 0.5625) {
                width = imgSize[0] > longSide ? longSide : imgSize[0];
                height = width * imgSize[1] / imgSize[0];
                size = maxSize;
            }
        }

        return compress(file, thumbFilePath, width, height, angle, size);
    }

    /**
     * obtain the image's width and height
     *
     * @param imagePath the path of image
     */
    private int[] getImageSize(String imagePath) {
        int[] res = new int[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;

        try {
            FileInputStream is = new FileInputStream(imagePath);
            BitmapFactory.decodeFileDescriptor(is.getFD(), null, options);
        } catch (FileNotFoundException e) {
            Logger.ZQ().e(TAG, new Throwable(e));
        } catch (IOException e) {
            Logger.ZQ().e(TAG, new Throwable(e));
        }


        res[0] = options.outWidth;
        res[1] = options.outHeight;

        return res;
    }

    /**
     * obtain the thumbnail that specify the size
     *
     * @param imagePath the target image path
     * @param width     the width of thumbnail
     * @param height    the height of thumbnail
     * @return {@link Bitmap}
     */
    private Bitmap compress(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        int outH = options.outHeight;
        int outW = options.outWidth;
        int inSampleSize = 1;

        if (outH > height || outW > width) {
            int halfH = outH / 2;
            int halfW = outW / 2;

            while ((halfH / inSampleSize) > height && (halfW / inSampleSize) > width) {
                inSampleSize *= 2;
            }
        }

        options.inSampleSize = inSampleSize;

        options.inJustDecodeBounds = false;

        int heightRatio = (int) Math.ceil(options.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(options.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                options.inSampleSize = heightRatio;
            } else {
                options.inSampleSize = widthRatio;
            }
        }
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(imagePath, options);
    }

    /**
     * obtain the image rotation angle
     *
     * @param path path of target image
     */
    private int getImageSpinAngle(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 指定参数压缩图片
     * create the thumbnail with the true rotate angle
     *
     * @param largeImageFile the big image file
     * @param thumbFilePath  the thumbnail path
     * @param width          width of thumbnail
     * @param height         height of thumbnail
     * @param angle          rotation angle of thumbnail
     * @param size           the file size of image
     */
    private CompressFileWrapper compress(CompressFileWrapper largeImageFile, String thumbFilePath, int width, int height, int angle, long size) {
        Bitmap thbBitmap = compress(largeImageFile.file.getAbsolutePath(), width, height);

        thbBitmap = rotatingImage(angle, thbBitmap);

        Logger.ZQ().d(TAG, "compress bitmap finish tag is " + largeImageFile.tag
                + " width is " + thbBitmap.getWidth() + " height is " + thbBitmap.getHeight());

        return saveImage(thumbFilePath, thbBitmap, size, largeImageFile);
    }

    /**
     * 保存图片到指定路径
     * Save image with specified size
     *
     * @param filePath the image file save path 储存路径
     * @param bitmap   the image what be save   目标图片
     * @param size     the file size of image   期望大小
     */
    private CompressFileWrapper saveImage(String filePath, Bitmap bitmap, long size, CompressFileWrapper originalFile) {
        Preconditions.checkNotNull(bitmap, TAG + "bitmap cannot be null");

        long finalSize;
        if (maxSize == 0)
            finalSize = size;
        else
            finalSize = size >= maxSize ? maxSize - 2 : size;

        File result = new File(filePath.substring(0, filePath.lastIndexOf("/")));

        if (!result.exists() && !result.mkdirs()) return null;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);

        while (stream.toByteArray().length / 1024 > finalSize && options > 6) {
            stream.reset();
            options -= 6;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);
        }
        //文件最终压缩的大小,单位kb
        int finalFileSize = 0;
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            finalFileSize = stream.toByteArray().length / 1024;
            fos.write(stream.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Logger.ZQ().e(TAG, new Throwable(e));
        }
        originalFile.file = new File(filePath);
        Logger.ZQ().d(TAG, "compress file finish tag is " + originalFile.tag
                + " size is " + finalFileSize + "KB name ==" + originalFile.file.getName());
        return originalFile;
    }

    public int getMaxSize(int maxFileSize, int size) {
        if(maxFileSize > 0 && maxFileSize <= 150) {
            return size > 512 ? 512 : size;
        } else if(maxFileSize > 150 && maxFileSize < 500) {
            return size > 1024 ? 1024 : size;
        } else {
            return size > 1280 ? 1280 : size;
        }
    }
}
