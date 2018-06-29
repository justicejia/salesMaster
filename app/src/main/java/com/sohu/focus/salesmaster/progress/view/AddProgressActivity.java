package com.sohu.focus.salesmaster.progress.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseActivity;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.client.view.AddClientActivity;
import com.sohu.focus.salesmaster.client.view.SelectClientsActivity;
import com.sohu.focus.salesmaster.http.api.PublishDynamicsApi;
import com.sohu.focus.salesmaster.dynamics.model.PublishDynamicsModel;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.invest.view.AddInvestActivity;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ScreenUtil;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.login.AccountManager;
import com.sohu.focus.salesmaster.progress.adapter.GallaryItemHolder;
import com.sohu.focus.salesmaster.http.api.UploadImgApi;
import com.sohu.focus.salesmaster.progress.model.GalleryModel;
import com.sohu.focus.salesmaster.progress.model.UploadPicModel;
import com.sohu.focus.salesmaster.uiframe.CommonDialog;
import com.sohu.focus.salesmaster.uiframe.LoadingDialog;
import com.sohu.focus.salesmaster.uiframe.SelectBottomDialog;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.SpaceDecoration;
import com.sohu.focus.salesmaster.uiframe.statusbar.StatusBarHelper;
import com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager;
import com.sohu.focus.salesmaster.utils.SalesFileUtil;
import com.sohu.focus.salesmaster.utils.SalesPermissionUtil;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.sohu.focus.salesmaster.kernal.utils.StorageUtil.getCameraUri;
import static com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager.STATUS_STRATEGY_ABOVE_KITKAT;

/**
 * 添加进度
 * Created by yuanminjia on 2017/10/30.
 */

public class AddProgressActivity extends BaseActivity implements SalesPermissionUtil.OnCameraPermissionListener, SelectBottomDialog.OnSelectImgListener
        , SalesPermissionUtil.OnStoragePermissionListener {

    private static final String TAG = "AddProgressActivity";
    @BindView(R.id.add_progress_note)
    EditText mAddnoteET;
    @BindView(R.id.add_gallery)
    EasyRecyclerView gallery;
    @BindView(R.id.add_progress_note_count)
    TextView count;
    @BindView(R.id.add_show_progress)
    TextView progress;
    @BindView(R.id.add_show_project)
    TextView project;
    @BindView(R.id.add_show_client)
    TextView showClient;
    @BindView(R.id.progress_publish)
    TextView publish;
    @BindView(R.id.add_progress_title)
    TextView title;
    @BindView(R.id.add_select_client)
    RelativeLayout clientLayout;
    @BindView(R.id.add_select_invest)
    RelativeLayout investLayout;
    @BindView(R.id.add_invest_show)
    TextView showInvest;
    @BindView(R.id.divider)
    View divider;


    private RecyclerArrayAdapter<GalleryModel> mAdapter;
    private ArrayList<String> mResults = new ArrayList<>();
    private int mStageId = -1;
    private int mEstateId = -1;
    private String mStageName; //进度的名称，动态名字（下单，回款...）
    private boolean mNeedCustomer;
    private File mCameraFile;
    private ArrayList<UploadPicModel.DataBean> mImgUrlList = new ArrayList<>();
    private LoadingDialog progressDialog;
    private List<String> mSelectedClientNames = new ArrayList<>();
    private ArrayList<String> mSelectedClientIds = new ArrayList<>();
    private int mSelectedProjectClientCount = 0;  //选中的楼盘对应的客户数量，如果该楼盘无客户，需要提示销售人员添加
    private String mInvestMoney; //投放金额


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_progress);
        Fresco.initialize(getApplicationContext());
        ButterKnife.bind(this);
        if (StatusBarHelper.statusBarLightMode(this) != StatusBarHelper.OTHER) {
            if (new SystemStatusManager(this).setTranslucentStatus(R.color.white) == STATUS_STRATEGY_ABOVE_KITKAT) {
                int statusHeight = ScreenUtil.getStatusBarHeight(this);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mAddnoteET.getLayoutParams();
                layoutParams.topMargin = statusHeight;
                mAddnoteET.setLayoutParams(layoutParams);
            }
        }
        initView();
    }

    public void initView() {
        mAddnoteET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int num = 300 - s.toString().length();
                count.setText(String.valueOf(num));
            }
        });
        gallery.setLayoutManager(new GridLayoutManager(this, 4));
        SpaceDecoration decoration = new SpaceDecoration(getResources().getDimensionPixelOffset(R.dimen.margin_medium_xxxxx));
        decoration.setPaddingHeaderFooter(false);
        decoration.setPaddingEdgeSide(false);
        decoration.setPaddingStart(false);
        mAdapter = new RecyclerArrayAdapter<GalleryModel>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new GallaryItemHolder(parent);
            }
        };
        gallery.setAdapter(mAdapter);
        GalleryModel model = new GalleryModel();
        model.setPhoto(false);
        mAdapter.add(model);
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!mAdapter.getAllData().get(position).isPhoto()) {
                    SelectBottomDialog dialog = new SelectBottomDialog(AddProgressActivity.this);
                    dialog.setImgListener(AddProgressActivity.this);
                    dialog.show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(SalesConstants.EXTRA_PHOTO_POSITION, position);
                    intent.putExtra(SalesConstants.EXTRA_GALLERY_OPTION, true);
                    intent.putStringArrayListExtra(SalesConstants.EXTRA_PHOTO_PATHS, mResults);
                    intent.setClass(AddProgressActivity.this, ViewPhotoActivity.class);
                    startActivityForResult(intent, SalesConstants.REQUEST_FOR_VIEW_PHOTO);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpEngine.getInstance().cancel(TAG);
    }

    @Override
    public void onBackPressed() {
        CommonDialog dialog = new CommonDialog.CommonDialogBuilder(this)
                .content("确定放弃发布动态?")
                .leftBtnText("取消")
                .rightBtnText("确定")
                .rightBtnListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).create();
        dialog.show(getSupportFragmentManager(), TAG);
    }

    @OnClick(R.id.add_progress_back)
    void close() {
        CommonDialog dialog = new CommonDialog.CommonDialogBuilder(this)
                .content("确定放弃发布动态?")
                .leftBtnText("取消")
                .rightBtnText("确定")
                .rightBtnListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).create();
        dialog.show(getSupportFragmentManager(), TAG);
    }

    @OnClick(R.id.add_select_project)
    void selectProject() {
        Intent intent = new Intent();
        intent.setClass(this, SelectProjectActivity.class);
        startActivityForResult(intent, SalesConstants.REQUEST_FOR_PROJECT);
    }

    @OnClick(R.id.add_select_progress)
    void selectProgress() {
        Intent intent = new Intent();
        intent.setClass(this, SelectProgressActivity.class);
        startActivityForResult(intent, SalesConstants.REQUEST_FOR_PROGRESS);
    }

    @OnClick(R.id.progress_publish)
    void publish() {
        if (mSelectedProjectClientCount == 0 && AccountManager.INSTANCE.getUserRole() == 1) {
            refreshPublishStatus();
            return;
        }
        if (mEstateId == -1) {
            ToastUtil.toast("请选择项目");
            return;
        } else if (mStageId == -1) {
            ToastUtil.toast("请选择动态类型");
            return;
        } else if (mNeedCustomer && mSelectedClientIds.size() == 0) {
            ToastUtil.toast("请添加客户");
            return;
        }
        mImgUrlList.clear();
        progressDialog = new LoadingDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if (mResults.size() > 0) {
            upload();
        } else {
            commit();
        }
    }

    @OnClick(R.id.add_select_client)
    void selectClient() {
        Intent intent = new Intent();
        intent.putExtra(SalesConstants.EXTRA_PROJECT_ID, mEstateId + "");
        intent.putStringArrayListExtra(SalesConstants.EXTRA_SELECT_CLIENT_IDS, mSelectedClientIds);
        intent.setClass(this, SelectClientsActivity.class);
        startActivityForResult(intent, SalesConstants.REQUEST_SELECT_CLIENT);
    }

    @OnClick(R.id.add_select_invest)
    void selectInvest() {
        Intent intent = new Intent(this, AddInvestActivity.class);
        intent.putExtra(SalesConstants.EXTRA_PROJECT_ID, mEstateId + "");
        startActivityForResult(intent, SalesConstants.REQUEST_ADD_INVEST);
    }

    /**
     * 上传图片并获取图片url
     */
    public void upload() {
        Observable.from(mResults).map(new Func1<String, File>() {
            @Override
            public File call(String s) {
                return SalesFileUtil.convertPNG2JPG(s);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        UploadImgApi api;
                        api = new UploadImgApi(file);
                        api.setTag(TAG);
                        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<UploadPicModel>() {
                            @Override
                            public void onSuccess(UploadPicModel result, String method) {
                                if (result != null && result.getData() != null) {
                                    if (mImgUrlList.size() < mResults.size() - 1) {
                                        mImgUrlList.add(result.getData());
                                    } else {
                                        mImgUrlList.add(result.getData());
                                        commit();
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onFailed(UploadPicModel result, String method) {
                                if (result != null) {
                                    ToastUtil.toast(result.getMsg());
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }
                });

    }

    /**
     * 提交数据
     */
    public void commit() {
        PublishDynamicsModel model = new PublishDynamicsModel();
        String convert = "";
        try {
            convert = URLEncoder.encode(mAddnoteET.getText().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (CommonUtils.notEmpty(mInvestMoney) && mStageName.equals("下单")) {
            model.setAdMoney(mInvestMoney);
        }
        model.setRemark(convert);
        model.setEstateId(mEstateId);
        model.setStageId(mStageId);
        model.setUserId(AccountManager.INSTANCE.getUserId());
        model.setCustomerList(mSelectedClientIds);
        List<PublishDynamicsModel.ImagesBean> imagesBeanList = new ArrayList<>();
        for (int i = 0; i < mImgUrlList.size(); i++) {
            PublishDynamicsModel.ImagesBean imagesBean = new PublishDynamicsModel.ImagesBean();
            imagesBean.setUrl(mImgUrlList.get(i).getUrl());
            imagesBean.setFileType(mImgUrlList.get(i).getFileType());
            imagesBean.setHeight(mImgUrlList.get(i).getHeight());
            imagesBean.setImgCloudPath(mImgUrlList.get(i).getImgCloudPath());
            imagesBean.setMd5(mImgUrlList.get(i).getMd5());
            imagesBean.setSize(mImgUrlList.get(i).getSize());
            imagesBean.setWidth(mImgUrlList.get(i).getWidth());
            imagesBeanList.add(imagesBean);
        }
        model.setImages(imagesBeanList);
        PublishDynamicsApi api = new PublishDynamicsApi(model);
        api.setTag(TAG);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel result, String method) {
                progressDialog.dismiss();
                ToastUtil.toast("发布成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
            }

            @Override
            public void onFailed(BaseModel result, String method) {
                if (result != null) {
                    ToastUtil.toast(result.getMsg());
                    progressDialog.dismiss();
                }
            }
        });

    }

    private void refreshPhotoList() {
        ArrayList<GalleryModel> list = new ArrayList<>();
        for (int i = 0; i < mResults.size(); i++) {
            GalleryModel model = new GalleryModel();
            model.setPhoto(true);
            model.setPhotoPath(mResults.get(i));
            list.add(model);
        }
        mAdapter.clear();
        if (mResults.size() < 9) {
            GalleryModel model = new GalleryModel();
            model.setPhoto(false);
            list.add(model);
        }
        mAdapter.addAll(list);
    }

    private void refreshPublishStatus() {
        if (mStageId == -1 || mEstateId == -1) {
            publish.setTextColor(ContextCompat.getColor(this, R.color.standard_text_light_gray));
            clientLayout.setVisibility(View.GONE);
            investLayout.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        } else {
            if (mNeedCustomer) {
                clientLayout.setVisibility(View.VISIBLE);
                if (mSelectedClientIds.size() > 0) {
                    publish.setTextColor(ContextCompat.getColor(this, R.color.home_icon_selected));
                } else {
                    publish.setTextColor(ContextCompat.getColor(this, R.color.standard_text_light_gray));
                }
            } else {
                clientLayout.setVisibility(View.GONE);
                investLayout.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
                publish.setTextColor(ContextCompat.getColor(this, R.color.home_icon_selected));
            }

            if (mStageName.equals("下单")) {
                investLayout.setVisibility(View.VISIBLE);
                divider.setVisibility(View.VISIBLE);
                if (CommonUtils.notEmpty(mInvestMoney)) {
                    publish.setTextColor(ContextCompat.getColor(this, R.color.home_icon_selected));
                } else {
                    publish.setTextColor(ContextCompat.getColor(this, R.color.standard_text_light_gray));
                }
            }
        }
        //postDelay的原因是onActivityResult发生时，界面还没完全展示，此时dialog无法展示出来
        publish.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mEstateId != -1 && mSelectedProjectClientCount == 0 && AccountManager.INSTANCE.getUserRole() == 1) {
                    CommonDialog dialog = new CommonDialog.CommonDialogBuilder(AddProgressActivity.this)
                            .content("添加客户后才能发布该项目动态哦～")
                            .leftBtnText("取消")
                            .rightBtnText("去添加")
                            .rightBtnListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setClass(AddProgressActivity.this, AddClientActivity.class);
                                    intent.putExtra(SalesConstants.EXTRA_PROJECT_ID, mEstateId + "");
                                    startActivityForResult(intent, SalesConstants.REQUEST_ADD_CLIENT);
                                }
                            }).leftBtnListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            }).create();
                    dialog.show(getSupportFragmentManager(), "CLIENT");
                }
            }
        }, 100);

    }


    private void takePhoto() {
        mCameraFile = new File(SalesFileUtil.getCameraFile(), System.currentTimeMillis()
                + ".jpg");
        mCameraFile.getParentFile().mkdirs();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getCameraUri(this, mCameraFile));
        startActivityForResult(intent, SalesConstants.REQUEST_FOR_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case SalesConstants.REQUEST_FOR_SELECT_PHOTO:
                        mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                        refreshPhotoList();
                        break;
                    case SalesConstants.REQUEST_FOR_VIEW_PHOTO:
                        mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                        refreshPhotoList();
                        break;
                    case SalesConstants.REQUEST_FOR_PROGRESS:
                        if (CommonUtils.notEmpty(data.getStringExtra(SalesConstants.EXTRA_PROGRESS))) {
                            mStageName = data.getStringExtra(SalesConstants.EXTRA_PROGRESS);
                            progress.setText(mStageName);
                            mStageId = data.getIntExtra(SalesConstants.EXTRA_PROGRESS_ID, -1);
                            mNeedCustomer = data.getBooleanExtra(SalesConstants.EXTRA_PROGRESS_NEED_CUSTOMER, false);
                            refreshPublishStatus();
                        }
                        break;
                    case SalesConstants.REQUEST_FOR_PROJECT:
                        if (CommonUtils.notEmpty(data.getStringExtra(SalesConstants.EXTRA_PROJECT))) {
                            project.setText(data.getStringExtra(SalesConstants.EXTRA_PROJECT));
                            mEstateId = data.getIntExtra(SalesConstants.EXTRA_PROJECT_ID, -1);
                            mSelectedProjectClientCount = data.getIntExtra(SalesConstants.EXTRA_PROJECT_CLIENT_NUM, 0);
                            refreshPublishStatus();
                        }
                        break;
                    case SalesConstants.REQUEST_SELECT_CLIENT:
                        mSelectedClientIds = data.getStringArrayListExtra(SalesConstants.EXTRA_SELECT_CLIENT_IDS);
                        mSelectedClientNames = data.getStringArrayListExtra(SalesConstants.EXTRA_SELECT_CLIENT_NAMES);
                        refreshPublishStatus();
                        if (CommonUtils.isEmpty(mSelectedClientIds)) {
                            showClient.setText("");
                            return;
                        }
                        StringBuilder sb = new StringBuilder();
                        for (String i : mSelectedClientNames) {
                            sb.append(i).append("，");
                        }
                        String result = sb.toString().substring(0, sb.length() - 1);
                        showClient.setText(result.length() > 16 ? result.substring(0, 15) + "..." : result);
                        break;
                    case SalesConstants.REQUEST_ADD_INVEST:
                        mInvestMoney = data.getStringExtra(SalesConstants.EXTRA_INVEST_MONEY);
                        showInvest.setText(mInvestMoney + "万");
                        refreshPublishStatus();
                        break;
                }
            }
            if (requestCode == SalesConstants.REQUEST_FOR_TAKE_PHOTO) {
                mResults.add(mCameraFile.getAbsolutePath());
                refreshPhotoList();
            } else if (requestCode == SalesConstants.REQUEST_ADD_CLIENT) {
                mSelectedProjectClientCount = 1;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SalesConstants.REQUEST_PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SalesPermissionUtil.requestStoragePermission(this, this);
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    ToastUtil.toast("权限已被禁止");
                }
            }
        } else if (requestCode == SalesConstants.REQUEST_PERMISSION_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    ToastUtil.toast("权限已被禁止");
                }
            }
        }
    }


    @Override
    public void onCameraPermissionSuccess() {
        SalesPermissionUtil.requestStoragePermission(this, this);
    }

    @Override
    public void onStoragePermissionSuccess() {
        takePhoto();
    }

    @Override
    public void onClickPhoto() {
        SalesPermissionUtil.requestCameraPermission(AddProgressActivity.this, AddProgressActivity.this);
    }

    @Override
    public void onClickAlbum() {
        Intent intent = new Intent(AddProgressActivity.this, ImagesSelectorActivity.class);
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 9);
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 20000);
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, false);
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
        startActivityForResult(intent, SalesConstants.REQUEST_FOR_SELECT_PHOTO);
    }


}
