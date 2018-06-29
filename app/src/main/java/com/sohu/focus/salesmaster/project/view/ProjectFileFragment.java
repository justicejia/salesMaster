package com.sohu.focus.salesmaster.project.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.CheckFileExistApi;
import com.sohu.focus.salesmaster.http.api.DeleteFileApi;
import com.sohu.focus.salesmaster.http.api.GetFileListApi;
import com.sohu.focus.salesmaster.http.api.UploadFileApi;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.project.adapter.ProjectFileHolder;
import com.sohu.focus.salesmaster.project.model.DeleteFileResultModel;
import com.sohu.focus.salesmaster.project.model.ProjectFileModel;
import com.sohu.focus.salesmaster.uiframe.CommonDialog;
import com.sohu.focus.salesmaster.uiframe.SelectFileTypeDialog;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.DividerDecoration;
import com.sohu.focus.salesmaster.utils.SalesPermissionUtil;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 楼盘文件列表
 * Created by yuanminjia on 2018/1/31.
 */

public class ProjectFileFragment extends BaseFragment implements SelectFileTypeDialog.OnDialogListener, RecyclerArrayAdapter.OnMoreListener
        , SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ProjectFileFragment";

    @BindView(R.id.project_file_list)
    EasyRecyclerView recyclerView;

    @OnClick(R.id.refresh)
    void errorRefresh() {
        loadData();
    }

    private RecyclerArrayAdapter<ProjectFileModel.DataBean.FileListBean> mAdapter;
    private RelativeLayout upload;
    private int mCurPage = 1;
    private String mProjectId;

    private static final int DEL_MSG_SUCCESS = 3;
    private static final int FILE_EXIST = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_file, container, false);
        ButterKnife.bind(this, view);
        mProjectId = getArguments().getString(SalesConstants.EXTRA_PROJECT_ID);
        recyclerView = (EasyRecyclerView) view.findViewById(R.id.project_file_list);
        upload = (RelativeLayout) view.findViewById(R.id.project_file_upload);
        //因为本页多个地方需要读写权限，在进入时就获取
        SalesPermissionUtil.requestStoragePermission(getActivity(), new SalesPermissionUtil.OnStoragePermissionListener() {
            @Override
            public void onStoragePermissionSuccess() {

            }
        });
        mCurPage = 1;
        initView();
        loadData();
        return view;
    }

    void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(getContext(), R.color.standard_line_light), 2));
        recyclerView.setRefreshListener(this);
        mAdapter = new RecyclerArrayAdapter<ProjectFileModel.DataBean.FileListBean>(getContext()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new ProjectFileHolder(parent, getFragmentManager());
            }
        };
        mAdapter.setMore(R.layout.recycer_view_more2, this);
        recyclerView.setAdapterWithProgress(mAdapter);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SalesPermissionUtil.hasStoragePermission(getContext())) {
                    ToastUtil.toast("请检查应用的读写权限");
                    return;
                }
                SelectFileTypeDialog dialog = new SelectFileTypeDialog();
                dialog.setDialogListener(ProjectFileFragment.this);
                dialog.show(getFragmentManager(), TAG);
            }
        });
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        mAdapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final int position) {
                CommonDialog dialog = new CommonDialog.CommonDialogBuilder(getContext())
                        .content("删除该文件")
                        .leftBtnText("取消")
                        .rightBtnText("确定")
                        .cancelable(true)
                        .rightBtnListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DeleteFileApi api = new DeleteFileApi(mAdapter.getAllData().get(position).getId());
                                HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<DeleteFileResultModel>() {
                                    @Override
                                    public void onSuccess(DeleteFileResultModel result, String method) {
                                        if (result != null && result.getData() != null) {
                                            if (result.getData().getResultCode() == DEL_MSG_SUCCESS) {
                                                mAdapter.remove(position);
                                            } else {
                                                ToastUtil.toast(result.getData().getResultMes());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        recyclerView.showError();
                                    }

                                    @Override
                                    public void onFailed(DeleteFileResultModel result, String method) {
                                        if (result != null)
                                            ToastUtil.toast(result.getMsg());

                                    }
                                });

                            }
                        }).create();
                dialog.show(getFragmentManager(), TAG);
                return true;
            }
        });

    }

    void loadData() {
        GetFileListApi api = new GetFileListApi(mProjectId, 20, mCurPage);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<ProjectFileModel>() {
            @Override
            public void onSuccess(ProjectFileModel result, String method) {
                if (result != null && result.getData() != null) {
                    if (mCurPage == 1) {
                        mAdapter.clear();
                    }
                    mAdapter.addAll(result.getData().getFileList());
                    mCurPage++;
                }
            }

            @Override
            public void onError(Throwable e) {
                recyclerView.showError();

            }

            @Override
            public void onFailed(ProjectFileModel result, String method) {
                if (result != null)
                    ToastUtil.toast(result.getMsg());
            }
        });
    }


    /**
     * 检查服务器端是否有文件，若没有则直接上传，有的话提示覆盖
     */
    void checkFileAlreadyUploaded(final String filePath) {
        File file = new File(filePath);
        CheckFileExistApi api = new CheckFileExistApi(mProjectId, file.getName());
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<DeleteFileResultModel>() {
            @Override
            public void onSuccess(DeleteFileResultModel result, String method) {
                if (result != null && result.getData() != null) {
                    if (result.getData().getResultCode() == FILE_EXIST) {
                        CommonDialog dialog = new CommonDialog.CommonDialogBuilder(getContext())
                                .cancelable(true)
                                .content("文件已存在，是否覆盖原文件？")
                                .leftBtnText("取消")
                                .rightBtnText("确定")
                                .rightBtnListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        uploadFile(filePath);
                                    }
                                }).create();
                        dialog.show(getFragmentManager(), TAG);
                    } else {
                        uploadFile(filePath);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.toast("上传失败");
            }

            @Override
            public void onFailed(DeleteFileResultModel result, String method) {
                if (result != null)
                    ToastUtil.toast(result.getMsg());
            }
        });
    }

    private void uploadFile(String filePath) {
        ToastUtil.toast("开始上传");
        File file = new File(filePath);
        UploadFileApi api = new UploadFileApi(file, mProjectId);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel result, String method) {
                ToastUtil.toast("上传成功");
                mCurPage = 1;
                loadData();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.toast("上传失败");
            }

            @Override
            public void onFailed(BaseModel result, String method) {
                if (result != null) {
                    ToastUtil.toast(result.getMsg());
                }
            }
        });
    }

    @Override
    public void onGetFilePath(final String filePath) {
        if (CommonUtils.notEmpty(filePath)) {
            //检查是否存在重复文件，并上传文件
            checkFileAlreadyUploaded(filePath);
        }
    }

    @Override
    public void onRefresh() {
        mCurPage = 1;
        loadData();
    }

    @Override
    public void onMoreShow() {
        loadData();
    }

    @Override
    public void onMoreClick() {

    }


}
