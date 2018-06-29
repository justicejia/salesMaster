package com.sohu.focus.salesmaster.http;


import com.sohu.focus.salesmaster.http.model.PostUserSheetsModel;
import com.sohu.focus.salesmaster.newFilter.model.FilterModel;
import com.sohu.focus.salesmaster.client.model.ClientsModel;
import com.sohu.focus.salesmaster.comment.model.DelCommentResultModel;
import com.sohu.focus.salesmaster.comment.model.UnreadCommentCountModel;
import com.sohu.focus.salesmaster.comment.model.UnreadCommentSetModel;
import com.sohu.focus.salesmaster.dynamics.model.DynamicDetailModel;
import com.sohu.focus.salesmaster.dynamics.model.DynamicSetModel;
import com.sohu.focus.salesmaster.dynamics.model.PublishDynamicsModel;
import com.sohu.focus.salesmaster.filter.model.FiltersDTO;
import com.sohu.focus.salesmaster.http.model.AddClient;
import com.sohu.focus.salesmaster.http.model.DelComment;
import com.sohu.focus.salesmaster.http.model.DelDynamics;
import com.sohu.focus.salesmaster.http.model.DeleteFile;
import com.sohu.focus.salesmaster.http.model.GetDynamicDetail;
import com.sohu.focus.salesmaster.http.model.GetDynamicsByProjId;
import com.sohu.focus.salesmaster.http.model.GetDynamicsByUid;
import com.sohu.focus.salesmaster.http.model.GetFiles;
import com.sohu.focus.salesmaster.http.model.GetProjectModel;
import com.sohu.focus.salesmaster.http.model.GetProjsByUid;
import com.sohu.focus.salesmaster.http.model.PostSuborInvestModel;
import com.sohu.focus.salesmaster.http.model.PostUserId;
import com.sohu.focus.salesmaster.http.model.GetUnreadComment;
import com.sohu.focus.salesmaster.http.model.GetUnreadCommentCount;
import com.sohu.focus.salesmaster.http.model.GetUserKpi;
import com.sohu.focus.salesmaster.http.model.GetUsrKpiHistory;
import com.sohu.focus.salesmaster.http.model.GetUsrKpiHistory2;
import com.sohu.focus.salesmaster.http.model.PostAddInvestModel;
import com.sohu.focus.salesmaster.http.model.PostCheckFileModel;
import com.sohu.focus.salesmaster.http.model.PostCheckInvestExistModel;
import com.sohu.focus.salesmaster.http.model.PostComment;
import com.sohu.focus.salesmaster.http.model.PostProjectIdModel;
import com.sohu.focus.salesmaster.http.model.RemoveClient;
import com.sohu.focus.salesmaster.http.model.UpdateCompetitorModel;
import com.sohu.focus.salesmaster.http.model.UpdateParams;
import com.sohu.focus.salesmaster.invest.model.SuborInvestModel;
import com.sohu.focus.salesmaster.invest.model.UserInvestInfoModel;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;
import com.sohu.focus.salesmaster.kernal.http.HttpResult;
import com.sohu.focus.salesmaster.goal.model.UsrKpiHistoryModel;
import com.sohu.focus.salesmaster.goal.model.UsrKpiHistoryModel2;
import com.sohu.focus.salesmaster.main.model.UpdateModel;
import com.sohu.focus.salesmaster.newFilter.model.HomeSheetFilterModel;
import com.sohu.focus.salesmaster.progress.model.UploadPicModel;
import com.sohu.focus.salesmaster.project.model.CheckInvestResultModel;
import com.sohu.focus.salesmaster.project.model.DeleteFileResultModel;
import com.sohu.focus.salesmaster.project.model.ProjKpiModel;
import com.sohu.focus.salesmaster.project.model.ProjectDataModel;
import com.sohu.focus.salesmaster.project.model.ProjectFileModel;
import com.sohu.focus.salesmaster.project.model.ProjectInfoModel;
import com.sohu.focus.salesmaster.project.model.ProjectInvestModel;
import com.sohu.focus.salesmaster.project.model.ProjectModelSet;
import com.sohu.focus.salesmaster.comment.model.PostCommentResultModel;
import com.sohu.focus.salesmaster.sheets.model.SheetModel;
import com.sohu.focus.salesmaster.sheets.model.SubscriptionModel;
import com.sohu.focus.salesmaster.subordinate.model.SuborModel;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by yuanminjia on 2017/11/1.
 */

public interface HttpService {

    //上传图片
    @Multipart
    @POST("api/salesFront/uploadImage")
    Observable<UploadPicModel> uploadImg(@Part MultipartBody.Part file);

    //发布楼盘项目动态
    @POST("api/salesFront/createProject")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BaseModel> publishDynamic(@Body PublishDynamicsModel dynamic);

    //获取指定楼盘项目的项目动态
    @POST("api/salesFront/getProjectByEstateId")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<DynamicSetModel> getDynamicsByProjId(@Body GetDynamicsByProjId projId);

    //获取楼盘项目详情信息
    @POST("api/salesFront/getEstateInfoByEstateId")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<ProjectInfoModel> getProjectInfo(@Body GetProjectModel model);

    //获取楼盘项目详情信息
    @POST("api/salesFront/getEstateInfoByEstateId")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<ProjectDataModel> getProjectData(@Body GetProjectModel model);

    //获取楼盘项目目标资料
    @POST("api/salesFront/getEstateInfoByEstateId")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<ProjKpiModel> getProjectKpiInfo(@Body GetProjectModel model);

    //    用户 kpi
    @POST("api/salesFront/getKPIBySalesUserId")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<ProjKpiModel> getUserKpiInfo(@Body GetUserKpi model);

    //    用户 kpi 列表（根据相对时间获得（本周上周本月上月等））
    @POST("api/salesFront/getSubordinateKPI")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<UsrKpiHistoryModel> getUserKpiHistory(@Body GetUsrKpiHistory model);

    //    用户 kpi 列表（根据具体时间获得（2017年12月））
    @POST("api/salesFront/getSubordinateKPIHistory")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<UsrKpiHistoryModel2> getUserKpiHistory2(@Body GetUsrKpiHistory2 model);

    //获取客户列表
    @POST("api/salesFront/getEstateInfoByEstateId")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<ClientsModel> getClients(@Body GetProjectModel model);

    //获取指定销售人员的项目动态
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/salesFront/getProjectByUserId")
    Observable<DynamicSetModel> getDynamicsByUid(@Body GetDynamicsByUid params);

    //获取楼盘项目
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/salesFront/getEstate")
    Observable<ProjectModelSet> getProjsByUid(@Body GetProjsByUid params);

    //获取下属列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/salesFront/getSubordinate")
    Observable<SuborModel> getSubordinates(@Body PostUserId param);

    //获取筛选项
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/salesFront/getParameters")
    Observable<FiltersDTO> getFilterData();

    //获取筛选项(新)
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/salesFront/getParameters")
    Observable<FilterModel> getFilterModel();


    //删除动态
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/salesFront/delete")
    Observable<HttpResult> delDynamic(@Body DelDynamics list);

    //检查更新
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/salesFront/getAppVersion")
    Observable<UpdateModel> checkUpdate(@Body UpdateParams params);

    //添加客户
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/salesFront/insertIfNotExistUpdateCustomerEstate")
    Observable<HttpResult> addClient(@Body AddClient addClient);

    //更新竞品
    //删除动态
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/salesFront/updateEstateInfoByEstateId")
    Observable<BaseModel> updateCompetitor(@Body UpdateCompetitorModel model);

    //删除客户
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/salesFront/deleteCustomerEstate")
    Observable<HttpResult> removeClient(@Body RemoveClient removeClient);

    //获取文件列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/salesFront/getFileList")
    Observable<ProjectFileModel> getProjectFileList(@Body GetFiles fileModel);

    //上传文件
    @Multipart
    @POST("api/salesFront/uploadFile")
    Observable<BaseModel> uploadFile(@Part MultipartBody.Part file);

    //删除文件
    @POST("api/salesFront/deleteFile")
    Observable<DeleteFileResultModel> deleteFile(@Body DeleteFile fileId);

    //检查文件是否存在
    @POST("api/salesFront/fileExists")
    Observable<DeleteFileResultModel> checkFile(@Body PostCheckFileModel model);

    //添加评论
    @POST("api/salesFront/createProjectComment")
    Observable<PostCommentResultModel> postComment(@Body PostComment comment);

    //未读消息计数
    @POST("api/salesFront/getUnreadCommentCount")
    Observable<UnreadCommentCountModel> getUnreadCommentCount(@Body GetUnreadCommentCount model);

    //未读计数列表
    @POST("api/salesFront/getUnreadCommentList")
    Observable<UnreadCommentSetModel> getUnreadComments(@Body GetUnreadComment model);

    //获取项目详情
    @POST("api/salesFront/getProjectDetail")
    Observable<DynamicDetailModel> getDynamicDetail(@Body GetDynamicDetail model);

    //删除评论
    @POST("api/salesFront/deleteComment ")
    Observable<DelCommentResultModel> delComment(@Body DelComment model);

    //获取项目投放
    @POST("api/admoney/front/getInfoByProject")
    Observable<ProjectInvestModel> getProjectInvest(@Body PostProjectIdModel model);

    @POST("api/admoney/front/checkAdmoney")
    Observable<CheckInvestResultModel> checkInvestExist(@Body PostCheckInvestExistModel model);

    @POST("api/admoney/front/createOrUpdate")
    Observable<BaseModel> addInvestRecord(@Body PostAddInvestModel model);

    //获取个人投放信息
    @POST("api/admoney/front/getInfoByPerson")
    Observable<UserInvestInfoModel> getUserInvestInfo(@Body PostUserId id);

    //获取下属投放信息
    @POST("api/admoney/front/getSubInfo")
    Observable<SuborInvestModel> getSuborInvest(@Body PostSuborInvestModel model);

    //获取用户已订阅列表
    @POST("api/reportReview/reviewListData")
    Observable<SheetModel> getUserSubscribedSheets(@Body PostUserSheetsModel model);

    //获取用户订阅列表（已订阅和未订阅）
    @POST("api/reportReview/reviewList")
    Observable<SubscriptionModel> getUserSubscriptionList(@Body PostUserId model);

    //首页报表筛选
    @POST("api/reportForm/commonAction/getParameters")
    Observable<HomeSheetFilterModel> getHomeSheetFilter(@Body PostUserId model);

}