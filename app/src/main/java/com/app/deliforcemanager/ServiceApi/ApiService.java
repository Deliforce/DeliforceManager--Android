package com.app.deliforcemanager.ServiceApi;

import com.app.deliforcemanager.ModelClass.AllDriverData.FetchAllDriverResponse;
import com.app.deliforcemanager.ModelClass.AnalyticsApi.AnalyticTimeApi;
import com.app.deliforcemanager.ModelClass.AnalyticsApi.TaskComplete;
import com.app.deliforcemanager.ModelClass.AutoAllocationApi.AutoAllocationStatus;
import com.app.deliforcemanager.ModelClass.CreateTask.CreateNewTask;
import com.app.deliforcemanager.ModelClass.DeleteImageApi.DeleteImage;
import com.app.deliforcemanager.ModelClass.DriverStatusApi.DriverStatusUpdate;
import com.app.deliforcemanager.ModelClass.EarningsChart.EarningDetailsData;
import com.app.deliforcemanager.ModelClass.EarningsChart.EarningsChartData;
import com.app.deliforcemanager.ModelClass.EarningsChart.EarningsRequest;
import com.app.deliforcemanager.ModelClass.FetchTeamResponse;
import com.app.deliforcemanager.ModelClass.GetProfile.GetProfileApi;
import com.app.deliforcemanager.ModelClass.GetProfile.GetProfileImage;
import com.app.deliforcemanager.ModelClass.HoursApi.Hours;
import com.app.deliforcemanager.ModelClass.IdleDistance.AnalyticsApi;
import com.app.deliforcemanager.ModelClass.ManagerProfile.ProfileResponse;
import com.app.deliforcemanager.ModelClass.ManagerTaskList.ManagerTaskResponse;
import com.app.deliforcemanager.ModelClass.NotificationApi.NotificationDetails;
import com.app.deliforcemanager.ModelClass.ProfileUpdateRequest;
import com.app.deliforcemanager.ModelClass.SettingsApi.SettingsDetails;
import com.app.deliforcemanager.ModelClass.StatusApi.DefalutStatus;
import com.app.deliforcemanager.ModelClass.TaskBarCode.BarCodeAPi;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.ActualCustomerOutput;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.CrmData;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.CrmRequest;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.NotesDetails;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.NotesList;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.OTPTaskUpdate;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.ParticluarTaskDetails;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.TaskDetails;
import com.app.deliforcemanager.ModelClass.TaskRoutesApi.TaskRoutes;
import com.app.deliforcemanager.ModelClass.TokenRegisterApi.TokenRegister;
import com.app.deliforcemanager.ModelClass.UpdateImagesApi.DriverImage;
import com.app.deliforcemanager.ModelClass.updatepasswordtobackend.UpdatePassword;
import com.app.deliforcemanager.RawHeaders.Analytics.AnalyticTimeLog;
import com.app.deliforcemanager.RawHeaders.Analytics.AnalyticsData;
import com.app.deliforcemanager.RawHeaders.Analytics.HoursFilter;
import com.app.deliforcemanager.RawHeaders.AutoAllocationInfo.AutoAllocationData;
import com.app.deliforcemanager.RawHeaders.DriverStatusInfo.DriverStatus;
import com.app.deliforcemanager.RawHeaders.LocationInfo.LocationUpdate;
import com.app.deliforcemanager.RawHeaders.TaskInfo.AddActualCustomer;
import com.app.deliforcemanager.RawHeaders.TaskInfo.DeleteNote;
import com.app.deliforcemanager.RawHeaders.TaskInfo.DriverImages;
import com.app.deliforcemanager.RawHeaders.TaskInfo.FilterMain;
import com.app.deliforcemanager.RawHeaders.TaskInfo.SuperTask;
import com.app.deliforcemanager.RawHeaders.TaskInfo.TaskBarCode;
import com.app.deliforcemanager.RawHeaders.TaskInfo.UpdateTaskStatus;
import com.app.deliforcemanager.RawHeaders.UpdationPassword;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {


    @Headers({"Content-Type: application/json"})
    @POST("device-token-register")
    Call<TokenRegister> sendFcmtoken(@Header("authorization") String idToken,

                                     @Body LocationUpdate locationUpdate);


    @Headers({"Content-Type: application/json"})
    @POST("registertoken")
    Call<TokenRegister> sendManagerLogin(@Header("authorization") String idToken,
                                     @Body LocationUpdate locationUpdate);




    @GET("profile")
    Call<GetProfileApi> getDriverProfileDetails(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken);


    @Headers("Content-Type: application/json")
    @POST("profile_get")
    Call<ProfileResponse> getManagerProfileDetails(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken);



    @Headers("Content-Type: application/json")
    @POST("changestatus")
    Call<DriverStatusUpdate> updateDriverStatus(@Header("authorization") String idToken,
                                                @Header("devicetoken") String deviceToken,
                                                @Body DriverStatus driverStatus);

    @Headers("Content-Type: application/json")
    @POST("livetracking")
    Call<DriverStatusUpdate> updateDriverLivetrack(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Body DriverStatus driverStatus);

    @Headers("Content-Type: application/json")
    @POST("task/tasklist")
    Call<TaskDetails> getTaskList(@Header("authorization") String idToken,
                                  @Header("devicetoken") String deviceToken,
                                  @Body FilterMain filterMain);



    @Headers("Content-Type: application/json")
    @POST("task_listing")
    Call<ManagerTaskResponse> getNewTaskList(@Header("authorization") String idToken,
                                             @Header("devicetoken") String deviceToken,
                                             @Body FilterMain filterMain);


    @Headers("Content-Type: application/json")
    @POST("fetch_singletask")
    Call<ParticluarTaskDetails> getParticularDetails(@Header("authorization") String idToken,@Body SuperTask task);


    @Headers("Content-Type: application/json")
    @POST("crm")
    Call<CrmData> getCrmDetails(@Header("authorization") String authorization, @Header("devicetoken") String deviceToken, @Body CrmRequest crm);

    @Headers("Content-Type: application/json")
    @POST("task/taskupdate")
    Call<OTPTaskUpdate> updatetaskDetails(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Body UpdateTaskStatus updateTaskStatus);

    @Headers("Content-Type: application/json")
    @PUT("task/taskupdateotp")
    Call<OTPTaskUpdate> updatetaskWithOTPDetails(@Header("Authorization") String idToken, @Header("devicetoken") String deviceToken, @Body UpdateTaskStatus updateTaskStatus);


    @Multipart
    @POST("fileupload")
    Call<DriverImage> uploadtask_images(@Header("Authorization") String authorization,
                                        @Header("devicetoken") String deviceToken,
                                        @Part("_id") RequestBody id,
                                        @Part("type") RequestBody type,
                                        @Part List<MultipartBody.Part> file);





    @Headers("Content-Type: application/json")
    @POST("fileremove")
    Call<DeleteImage> deleteImage(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Body DriverImages driverImages);


    @Multipart
    @POST("fileupload")
    Call<DriverImage> uploadtask_signature_images(@Header("Authorization") String authorization,
                                                  @Header("devicetoken") String deviceToken,
                                                  @Part("_id") RequestBody id,
                                                  @Part MultipartBody.Part file);

    @Multipart
    @PUT("fileupload")
    Call<DriverImage> updateExistingSignatute(@Header("Authorization") String authorization,
                                              @Header("devicetoken") String deviceToken,
                                              @Part("_id") RequestBody id,
                                              @Part MultipartBody.Part file);


    @Headers("Content-Type: application/json")
    @PUT("profile_get")
    Call<ResponseBody> updateDriverProfileDetails(@Header("authorization") String idToken,
                                                   @Body ProfileUpdateRequest driverProfileDetails);


    @Headers("Content-Type: application/json")
    @POST("task_post")
    Call<CreateNewTask> createNewTask(@Header("authorization") String idToken,  @Body CreateNewTask createNewTask);

    @Headers("Content-Type: application/json")
    @GET("analytics/earning")
    Call<EarningDetailsData> requestEarningDetails(@Header("authorization") String idToken,
                                                   @Header("devicetoken") String deviceToken,
                                                   @Query("taskId") String taskID);

    @Headers("Content-Type: application/json")
    @POST("analytics/earning")
    Call<EarningsChartData> requestEarningsData(@Header("authorization") String idToken,
                                                @Header("devicetoken") String deviceToken,
                                                @Body EarningsRequest earningsRequest);

    @Headers("Content-Type: application/json")
    @GET("notifications")
    Call<NotificationDetails> getNotificationList(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken);

    @Headers("Content-Type: application/json")
    @DELETE("notifications")
    Call<TaskDetails> clearNotificationList(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken);

    @Headers("Content-Type: application/json")
    @POST("task/notes")
    Call<NotesList> postNewNote(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Body NotesList notesList);

    @Headers("Content-Type: application/json")
    @PUT("task/notes")
    Call<ResponseBody> editNote(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Body NotesList notesList);

    @Headers("Content-Type: application/json")
    @POST("notes")
    Call<NotesDetails> getAllNotesList(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Body NotesList notesList);

    @Headers("Content-Type: application/json")
    @DELETE("task/notes/{id}/{taskId}")
    Call<ResponseBody> deleteParticularNote(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Path("id") String noteid, @Path("taskId") String taskId);

    @Headers("Content-Type: application/json")
    @PUT("settings")
    Call<ResponseBody> updateSettings(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Body SettingsDetails settingsDetails);

    @Headers("Content-Type: application/json")
    @GET("settings")
    Call<SettingsDetails> getSettingsDetails(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken);

    @GET("statuslist")
    Call<DefalutStatus> getStatusList(@Header("authorization") String token, @Header("devicetoken") String deviceToken, @Query("language") String language);


    @Multipart
    @POST("profileimageupload")
    Call<GetProfileImage> uploadDriverProfilePic(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Part MultipartBody.Part driverProfileImage);

    @Headers("Content-Type: application/json")
    @POST("analytics/hourslist")
    Call<Hours> getHoursDetails(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Body HoursFilter hoursFilter);

    @Headers("Content-Type: application/json")
    @POST("analytics/taskcompletion")
    Call<TaskComplete> getTaskCompleteDetails(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Body HoursFilter hoursFilter);

    @Headers("Content-Type: application/json")
    @POST("analytics/driverlogput")
    Call<AnalyticsApi> sendIdleDetails(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Body AnalyticsData analyticsData);

    @Headers("Content-Type: application/json")
    @POST("task/autoallocationtaskupdate")
    Call<AutoAllocationStatus> updateAutoAllocationtask(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Body AutoAllocationData allocationData);

    @Headers("Content-Type: application/json")
    @GET("task/taskroutes")
    Call<TaskRoutes> getRoutes(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Query("timezone") String timezone);

    @Headers("Content-Type: application/json")
    @POST("analytics/driverlog")
    Call<AnalyticTimeApi> getAnalyticTImesCall(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Body AnalyticTimeLog analyticTimeLog);

    @Headers("Content-Type: application/json")
    @POST("task/barcode")
    Call<BarCodeAPi> sendBarCodeValue(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Body TaskBarCode taskBarCode);

    @Headers("Content-Type: application/json")
    @POST("task/notes/delete")
    Call<DeleteNote> deletenote(@Header("authorization") String idToken, @Header("devicetoken") String deviceToken, @Body DeleteNote deleteNote);


    /*Apr 5 added*/
    @Headers("Content-Type: application/json")
    @PUT("changepassword")
    Call<UpdatePassword> updatePasswordToServer(@Body UpdationPassword updationPassword);

    @Headers("Content-Type: application/json")
    @POST("fileupload/actualcustomer")
    Call<ActualCustomerOutput> insertActualCustomer(@Header("Authorization") String authorization,
                                                    @Header("devicetoken") String deviceToken,
                                                    @Body AddActualCustomer addActualCustomer);

    @Headers("Content-Type: application/json")
    @PUT("fileupload/actualcustomer")
    Call<ActualCustomerOutput> updateActualCustomer(@Header("Authorization") String authorization,
                                                    @Header("devicetoken") String deviceToken,
                                                    @Body AddActualCustomer addActualCustomer);



    @Headers("Content-Type: application/json")
    @GET("fetchteam")
    Call<ArrayList<FetchTeamResponse>> getTeam(@Header("authorization") String authorization);





    @Headers("Content-Type: application/json")
    @POST("driver_all_get")
    Call<ArrayList<FetchAllDriverResponse>> getAllDrivers(@Header("authorization") String authorization,@Body JSONObject encodedData);



}
