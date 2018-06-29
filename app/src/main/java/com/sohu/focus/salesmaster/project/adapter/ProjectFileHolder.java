package com.sohu.focus.salesmaster.project.adapter;

import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.kernal.http.HttpDownloadEngine;
import com.sohu.focus.salesmaster.kernal.http.download.DownInfo;
import com.sohu.focus.salesmaster.kernal.http.download.DownState;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpDownListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.NetUtil;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.project.model.ProjectFileModel;
import com.sohu.focus.salesmaster.uiframe.CommonDialog;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.utils.OpenFileUtil;
import com.sohu.focus.salesmaster.utils.SalesFileUtil;

import java.io.File;

/**
 * 文件holder
 * Created by yuanminjia on 2018/1/31.
 */

public class ProjectFileHolder extends BaseViewHolder<ProjectFileModel.DataBean.FileListBean> {

    private final String TAG = "ProjectFileHolder";
    private TextView fileName;
    private TextView fileDetail;
    private ImageView fileType;
    private RelativeLayout container;
    private FragmentManager mManager;
    private ProgressBar progressBar;

    public ProjectFileHolder(ViewGroup itemView, FragmentManager fragmentManager) {
        super(itemView, R.layout.holder_project_file);
        fileDetail = $(R.id.file_detail);
        fileName = $(R.id.file_name);
        fileType = $(R.id.file_type);
        container = $(R.id.file_holder_container);
        progressBar = $(R.id.download_progress);
        mManager = fragmentManager;
    }

    @Override
    public void setData(final ProjectFileModel.DataBean.FileListBean data) {
        fileName.setText(data.getFileName());
        fileDetail.setText(CommonUtils.convertToTime(data.getCreateTime(), "yyyy-MM-dd") + " " +
                data.getPersonName());
        if (data.getFileType().equals("WORD")) {
            fileType.setImageResource(R.drawable.file_word);
        } else if (data.getFileType().equals("PDF")) {
            fileType.setImageResource(R.drawable.file_pdf);
        } else if (data.getFileType().equals("EXCEL")) {
            fileType.setImageResource(R.drawable.file_excel);
        } else if (data.getFileType().equals("PPT")) {
            fileType.setImageResource(R.drawable.file_ppt);
        } else if (data.getFileType().equals("TXT")) {
            fileType.setImageResource(R.drawable.file_txt);
        } else {
            fileType.setImageResource(R.drawable.file_img);
        }
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrDownload(data.getFileName(), data.getUrl());
            }
        });


    }

    /**
     * 根据文件是否存在选择打开或者下载文件
     */

    private void openOrDownload(final String fileName, final String url) {
        final File file = checkFileExist(fileName);
        if (file == null) {
            CommonDialog dialog = new CommonDialog.CommonDialogBuilder(getContext())
                    .content("下载文件吗")
                    .leftBtnText("取消")
                    .rightBtnText("确定")
                    .cancelable(true)
                    .rightBtnListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downLoadFile(fileName, url);
                        }
                    }).create();
            dialog.show(mManager, TAG);
        } else {
            CommonDialog dialog = new CommonDialog.CommonDialogBuilder(getContext())
                    .content("打开文件?")
                    .leftBtnText("取消")
                    .rightBtnText("确定")
                    .cancelable(true)
                    .rightBtnListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OpenFileUtil.openFile(file.getAbsolutePath(), getContext());
                        }
                    }).create();
            dialog.show(mManager, TAG);
        }

    }

    /**
     * 下载文件
     */
    private void downLoadFile(String fileName, String url) {
        File file = new File(Environment.getExternalStorageDirectory(), SalesFileUtil.EXTERNAL_DOWNLOAD_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        File output = new File(file, fileName);
        DownInfo downInfo = new DownInfo(url);
        downInfo.setState(DownState.START);
        downInfo.setSavePath(output.getAbsolutePath());
        downInfo.setConnectonTime(NetUtil.TIMEOUT_TIME);
        downInfo.setRange(false);
        downInfo.setListener(new FileDownloadListener());
        HttpDownloadEngine httpDownloadEngine = HttpDownloadEngine.getInstance();
        httpDownloadEngine.startDown(downInfo);
    }

    /**
     * 判断文件是否存在
     */
    private File checkFileExist(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory(), SalesFileUtil.EXTERNAL_DOWNLOAD_DIR);
        if (!file.exists()) {
            file.mkdirs();
            return null;
        } else {
            File[] subFiles = file.listFiles();
            for (final File item : subFiles) {
                if (item.getName().equals(fileName)) {
                    return item;
                }
            }
            return null;
        }
    }


    private class FileDownloadListener extends HttpDownListener<DownInfo> {

        @Override
        public void onNext(final DownInfo downInfo) {
            CommonDialog dialog = new CommonDialog.CommonDialogBuilder(getContext())
                    .content("下载完成，打开文件？")
                    .leftBtnText("取消")
                    .rightBtnText("确定")
                    .cancelable(true)
                    .rightBtnListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OpenFileUtil.openFile(downInfo.getSavePath(), getContext());

                        }
                    }).create();
            dialog.show(mManager, TAG);
        }

        @Override
        public void onStart() {
            progressBar.setVisibility(View.VISIBLE);
            ToastUtil.toast("开始下载");
        }

        @Override
        public void onComplete() {
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void updateProgress(long readLength, long countLength) {
            progressBar.setProgress((int) (readLength * 100 / countLength));
        }
    }
}
