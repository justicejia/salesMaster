package com.sohu.focus.salesmaster.main.adapter;

import android.app.Service;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseActivity;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.comment.AddCommentEvent;
import com.sohu.focus.salesmaster.comment.DynamicCommentManager;
import com.sohu.focus.salesmaster.comment.DynamicCommentAdapter;
import com.sohu.focus.salesmaster.comment.model.PostCommentResultModel;
import com.sohu.focus.salesmaster.comment.DelCommentEvent;
import com.sohu.focus.salesmaster.dynamics.adapter.AlbumAdapter;
import com.sohu.focus.salesmaster.dynamics.adapter.GridSpacingItemDecoration;
import com.sohu.focus.salesmaster.dynamics.model.DynamicSetModel;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.DelCommentApi;
import com.sohu.focus.salesmaster.http.api.DelDynamicApi;
import com.sohu.focus.salesmaster.http.model.DelComment;
import com.sohu.focus.salesmaster.http.model.DelDynamics;
import com.sohu.focus.salesmaster.http.model.PostComment;
import com.sohu.focus.salesmaster.kernal.bus.RxBus;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.imageloader.FocusImageLoader;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.login.AccountManager;
import com.sohu.focus.salesmaster.me.view.MeActivity;
import com.sohu.focus.salesmaster.progress.view.ViewPhotoActivity;
import com.sohu.focus.salesmaster.project.view.ProjectActivity;
import com.sohu.focus.salesmaster.subordinate.view.SuborActivity;
import com.sohu.focus.salesmaster.uiframe.CommonDialog;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.inputdialog.InputDialogFragment;
import com.sohu.focus.salesmaster.utils.DateUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态ViewHolder
 * Created by yuanminjia on 2017/10/27.
 */

public class DynamicHolder extends BaseViewHolder<DynamicSetModel.DataBean.ListBean>
        implements InputDialogFragment.SendListener, InputDialogFragment.CacheListener,
        AdapterView.OnItemClickListener {

    private DynamicSetModel.DataBean.ListBean mData;

    private static final String TAG = "DynamicHolder";

    private static final int ALBUM_PADDING = 6;

    RecyclerView.LayoutManager gridManager2;
    RecyclerView.ItemDecoration decoration2;

    RecyclerView.LayoutManager gridManager3;
    RecyclerView.ItemDecoration decoration3;

    //    整个 View 的宽度
    private static int width = 0;

    private AlbumAdapter adapter;
    private DynamicCommentAdapter commentAdapter;

    public interface DelCallBack {
        void del(int position);
    }

    public interface ShowProjectDetailCallBack {
        void showProjectDetail(Intent intent);
    }

    private DelCallBack callBack;
    private ShowProjectDetailCallBack showProjectDetailCallBack;
    private TextView name, buildName, time, note, state, showMore, roleTag, del, clients, hideMore, invest;
    private ImageView singlePhoto;
    private FrameLayout photoLayout;
    RecyclerView album;
    ListView reviews;
    View review;


    public DynamicHolder(ViewGroup parent) {
        super(parent, R.layout.holder_dynamics);
        name = $(R.id.home_dynamic_name);
        buildName = $(R.id.home_dynamic_build_name);
        time = $(R.id.home_dynamic_time);
        note = $(R.id.home_dynamic_note);
        album = $(R.id.home_dynamic_album);

        reviews = $(R.id.reviews);
        review = $(R.id.comment);
        state = $(R.id.home_dynamic_state);
        del = $(R.id.home_dynamic_del);
        roleTag = $(R.id.dynamic_role_tag);
        showMore = $(R.id.dynamic_show_more);
        clients = $(R.id.home_dynamic_clients);
        hideMore = $(R.id.dynamic_hide_more);
        singlePhoto = $(R.id.home_dynamic_single_photo);
        photoLayout = $(R.id.dynamic_photo);
        invest = $(R.id.home_dynamic_invest);

        if (gridManager2 == null)
            gridManager2 = new GridLayoutManager(getContext(), 2);
        if (decoration2 == null)
            decoration2 = new GridSpacingItemDecoration(2,
                    CommonUtils.dp2pxConvertInt(getContext(), ALBUM_PADDING));
        if (gridManager3 == null)
            gridManager3 = new GridLayoutManager(getContext(), 3);
        if (decoration3 == null)
            decoration3 = new GridSpacingItemDecoration(3,
                    CommonUtils.dp2pxConvertInt(getContext(), ALBUM_PADDING));
        if (adapter == null) {
            adapter = new AlbumAdapter();
        }

        if (width == 0) {
            DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
            width = metrics.widthPixels - 2 * CommonUtils.dp2pxConvertInt(getContext(), 20);
        }

        commentAdapter = new DynamicCommentAdapter();
    }

    public void setDelCallBack(DelCallBack callBack) {
        this.callBack = callBack;
    }

    public void setShowProjectDetailCallBack(ShowProjectDetailCallBack showProjectDetailCallBack) {
        this.showProjectDetailCallBack = showProjectDetailCallBack;
    }

    @Override
    public void setData(final DynamicSetModel.DataBean.ListBean data) {
        mData = data;

        name.setText(data.getPersonName());

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getPersonID().equals(AccountManager.INSTANCE.getUserId())) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), MeActivity.class);
                    getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(SalesConstants.EXTRA_ID, data.getPersonID());
                    intent.putExtra(SalesConstants.EXTRA_NAME, data.getPersonName());
                    intent.putExtra(SalesConstants.EXTRA_ROLE, data.getSalesRole());
                    intent.putExtra(SalesConstants.EXTRA_AREA,data.getAreaCode());
                    intent.setClass(getContext(), SuborActivity.class);
                    getContext().startActivity(intent);
                }
            }
        });

        buildName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ProjectActivity.class);
                intent.putExtra(SalesConstants.EXTRA_PROJECT_ID, data.getProjectID());
                intent.putExtra(SalesConstants.EXTRA_PROJECT, data.getProjectName());
                intent.putExtra(SalesConstants.EXTRA_DYNAMIC_ID, data.getSalesProjectID());
                if (showProjectDetailCallBack != null)
                    showProjectDetailCallBack.showProjectDetail(intent);
                else
                    ((BaseActivity) getContext()).startActivityForResult(intent, SalesConstants.REQUEST_FOR_PROJECT);
            }
        });
        //角色
        if (AccountManager.INSTANCE.getUserViewRole()) {
            roleTag.setVisibility(View.VISIBLE);
            StringBuilder tag = new StringBuilder();
            if (AccountManager.INSTANCE.isUserWholeCountry() && CommonUtils.notEmpty(data.getCityName())) {
                tag.append(data.getCityName());
            }
            if (data.getSalesRole() == SalesConstants.USER_ROLE_SALES) {
                tag.append("销售");
            } else {
                tag.append("运营");
            }
            roleTag.setText(tag.toString());
        } else {
            roleTag.setVisibility(View.GONE);
        }

        //投放
        if (CommonUtils.notEmpty(data.getAdMoney())) {
            invest.setVisibility(View.VISIBLE);
            invest.setText("投放：" + data.getAdMoney() + "万");
        } else {
            invest.setVisibility(View.GONE);
        }

        //客户
        StringBuilder clientString = new StringBuilder();
        clientString.append("客户：");
        if (CommonUtils.notEmpty(data.getCustomerList())) {
            clients.setVisibility(View.VISIBLE);
            for (DynamicSetModel.DataBean.ListBean.CustomerListBean i : data.getCustomerList()) {
                clientString.append(i.getTitle()).append(" ").append(i.getName()).append(",");
            }
            clients.setText(clientString.toString().substring(0, clientString.length() - 1));
        } else {
            clients.setVisibility(View.GONE);
        }
        buildName.setText(data.getProjectName());
        time.setText(DateUtils.getSimpleTime(data.getStampTime(),
                getContext()));
        String result = "";
        showMore.setVisibility(View.GONE);
        hideMore.setVisibility(View.GONE);
        note.setMaxLines(2);
        if (CommonUtils.isEmpty(data.getRemark()))
            note.setVisibility(View.GONE);
        else {
            try {
                result = URLDecoder.decode(data.getRemark(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            note.setVisibility(View.VISIBLE);
            note.setText("备注：" + result);
            if (note.getLineCount() > 2) {
                showMore.setVisibility(View.VISIBLE);
            }
        }
        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setMaxLines(100);
                showMore.setVisibility(View.GONE);
                hideMore.setVisibility(View.VISIBLE);
            }
        });

        hideMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setMaxLines(2);
                showMore.setVisibility(View.VISIBLE);
                hideMore.setVisibility(View.GONE);
            }
        });

        state.setText(data.getProjectStage());
        //图片
        List<String> imagesList = new ArrayList<>();
        for (DynamicSetModel.DataBean.ListBean.ImagesBean item : data.getImages()) {
            imagesList.add(item.getUrl());
        }
        adapter.setImgUrls(imagesList);

        switch (data.getImages().size()) {
            case 0:
                photoLayout.setVisibility(View.GONE);
                break;
            case 1:
                photoLayout.setVisibility(View.VISIBLE);
                showSingleAlbum(data.getImages().get(0).getUrl());
                break;
            case 4:
                photoLayout.setVisibility(View.VISIBLE);
                show4Album();
                break;
            default:
                photoLayout.setVisibility(View.VISIBLE);
                showAlbum();
                break;
        }

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment();
            }
        });

        commentAdapter.setComments(data.getCommentList());

        reviews.setAdapter(commentAdapter);
        reviews.setOnItemClickListener(this);

        if (!commentAdapter.isEmpty()) reviews.setVisibility(View.VISIBLE);
        else reviews.setVisibility(View.GONE);

        showDel(data);
    }

    private void showDel(final DynamicSetModel.DataBean.ListBean data) {
        del.setVisibility(View.GONE);
        if (!(data.getPersonID()).equals(AccountManager.INSTANCE.getUserId())) return;
        del.setVisibility(View.VISIBLE);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog dialog = new CommonDialog.CommonDialogBuilder(getContext())
                        .content("确认要删除吗?")
                        .leftBtnText("取消")
                        .rightBtnText("删除")
                        .rightBtnListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (callBack != null)
//                                    先将项目从视图中删掉，再调用网络接口
                                    callBack.del(getAdapterPosition());
                                delNetwork(data);
                            }
                        })
                        .onlyOne(true)
                        .cancelable(false)
                        .create();
                dialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "");
            }
        });

    }

    //    调用接口删除
    private void delNetwork(final DynamicSetModel.DataBean.ListBean data) {
        DelDynamics param = new DelDynamics();
        param.setDynamicId(data.getSalesProjectID());
        DelDynamicApi api = new DelDynamicApi();
        api.setDelDynamics(param);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel result, String method) {
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onFailed(BaseModel result, String method) {
                if (result != null)
                    ToastUtil.toast(result.getMsg());
            }
        });
    }

    //相册显示

    //    RecyclerView居然没有清空 temDecoration 的方法，只能这样处理了。
    private void clearItemDecoration() {
        if (album == null) return;
        album.removeItemDecoration(decoration2);
        album.removeItemDecoration(decoration3);
    }

    //   单张图片展示
    private void showSingleAlbum(final String url) {
        clearItemDecoration();
        album.setVisibility(View.GONE);
        singlePhoto.setVisibility(View.VISIBLE);
        FocusImageLoader.getLoader(getContext())
                .load(url)
                .into(singlePhoto)
                .fitXY()
                .placeholder(R.drawable.img_holder_gray)
                .display();

        singlePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(SalesConstants.EXTRA_PHOTO_POSITION, 0);
                intent.putExtra(SalesConstants.EXTRA_GALLERY_OPTION, false);
                ArrayList<String> urlList = new ArrayList<>();
                urlList.add(url);
                intent.putStringArrayListExtra(SalesConstants.EXTRA_PHOTO_PATHS, urlList);
                intent.setClass(getContext(), ViewPhotoActivity.class);
                getContext().startActivity(intent);
            }
        });

    }

    //    四张图片田字格显示
    private void show4Album() {
        clearItemDecoration();
        singlePhoto.setVisibility(View.GONE);
        album.setVisibility(View.VISIBLE);
        if (width > 0) {
            album.setPadding(
                    0,
                    album.getPaddingTop(),
                    (width + ALBUM_PADDING) / 3,
                    album.getPaddingBottom()
            );
        } else {

        }
        album.setLayoutManager(gridManager2);
        album.addItemDecoration(decoration2);
        album.setAdapter(adapter);
    }

    //    三列显示
    private void showAlbum() {
        singlePhoto.setVisibility(View.GONE);
        album.setVisibility(View.VISIBLE);
        clearItemDecoration();
        album.setPadding(
                0,
                album.getPaddingTop(),
                0,
                album.getPaddingBottom()
        );
        album.setLayoutManager(gridManager3);
        album.addItemDecoration(decoration3);
        album.setAdapter(adapter);
    }

//    与评论输入内容交互接口

    PostComment currentComment;

    private void comment() {
        currentComment = DynamicCommentManager.getINSTANCE().getCacheDynamicReview(mData.getSalesProjectID());

        inputComment(currentComment, "评论：");

    }

    private void replyComment(String replyToUserId, String hint) {

        currentComment = DynamicCommentManager.getINSTANCE().getCacheDynamicReview(
                mData.getSalesProjectID(), replyToUserId);

        inputComment(currentComment, hint);

    }

    private void inputComment(PostComment comment, String hint) {
        new InputDialogFragment.Builder()
                .setSendListener(DynamicHolder.this)
                .setCacheListener(DynamicHolder.this)
                .setHint(hint)
                .show(
                        ((FragmentActivity) getContext()).getSupportFragmentManager(), "");

        InputMethodManager imm;
        imm = (InputMethodManager) getContext().getSystemService(Service
                .INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void send(String content) {

        currentComment.setContent(content);

//        滑动过程可能会出现错误
        HttpRequestListener<PostCommentResultModel> listener = new HttpRequestListener<PostCommentResultModel>() {
            @Override
            public void onSuccess(PostCommentResultModel result, String method) {

//                通过 RxBus 发布事件
                AddCommentEvent event = new AddCommentEvent();
                event.setDynamicId(mData.getSalesProjectID());
                event.setComment(result.getData());
                RxBus.get().post(event);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onFailed(PostCommentResultModel result, String method) {

            }
        };

        DynamicCommentManager.getINSTANCE()
                .postDynamicComment(currentComment, listener);
        currentComment = null;
    }

    @Override
    public void cache(String content) {
//        暂时先不做缓存功能。
    }

    //    评论点击事件

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        commentClick(position);
    }

    public void commentClick(final int position) {
        if (commentAdapter.getItem(position).salesUserId.equals(AccountManager.INSTANCE
                .getUserId())) {
            CommonDialog dialog = new CommonDialog.CommonDialogBuilder(getContext())
                    .content("确定删除评论?")
                    .leftBtnText("取消")
                    .rightBtnText("确定")
                    .rightBtnListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DelComment model = new DelComment();
                            final String commentId = mData.getCommentList().get
                                    (position).getCommentId();
                            model.setCommentId(commentId);
                            DelCommentApi api = new DelCommentApi(model);

                            HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<BaseModel>() {
                                @Override
                                public void onSuccess(BaseModel result, String method) {
                                    DelCommentEvent event = new DelCommentEvent();
                                    event.setDynamicId(mData.getSalesProjectID());
                                    event.setCommentId(commentId);
                                    RxBus.get().post(event);
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onFailed(BaseModel result, String method) {

                                }
                            });
                        }
                    }).create();
            dialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), TAG);
        } else {
            replyComment(commentAdapter.getItem(position).salesUserId,
                    "回复 " + commentAdapter.getItem(position).getSalesUserName() + "：");
        }
    }
}
