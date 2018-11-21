package com.expo.contract.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.expo.contract.SeekHelpContract;
import com.expo.entity.VisitorService;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.UploadRsp;

import java.io.File;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SeekHelpPresenterImpl extends SeekHelpContract.Presenter {
    public SeekHelpPresenterImpl(SeekHelpContract.View view) {
        super(view);
    }

    @Override
    public void addVisitorService(VisitorService visitorService) {
        mView.showLoadingView();
        upLoadImgFile(visitorService, visitorService.img_url1, 0);
        upLoadImgFile(visitorService, visitorService.img_url2, 1);
        upLoadImgFile(visitorService, visitorService.img_url3, 2);
    }

    public void upLoadImgFile(VisitorService visitorService, String filePath, int positon) {
        if (!StringUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            String fileType = "";
            if (filePath.endsWith("mp4"))
                fileType = "video.mp4";
            else
                fileType = "image.png";
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            ;
            Observable<UploadRsp> verifyCodeLoginObservable = Http.getServer().uploadFile(MultipartBody.Part.createFormData("file", fileType, requestBody));
            Http.request(new ResponseCallback<UploadRsp>() {
                @Override
                protected void onResponse(UploadRsp rsp) {
                    if (positon == 0) visitorService.img_url1 = rsp.shortUrl;
                    else if (positon == 1) visitorService.img_url2 = rsp.shortUrl;
                    else if (positon == 2) visitorService.img_url3 = rsp.shortUrl;
                    submitVisitorService(visitorService);
                }
            }, verifyCodeLoginObservable);
        } else {
            submitVisitorService(visitorService);
        }
    }

    private synchronized void submitVisitorService(VisitorService visitorService) {
        visitorService.addTimes();
        if (visitorService.getTimes() < 3) return;
        Map<String, Object> params = Http.getBaseParams();
        params.put("Obj", visitorService.toJson());
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<BaseResponse> verifyCodeLoginObservable = Http.getServer().addVisitorService(requestBody);
        Http.request(new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
                mView.hideLoadingView();
                mView.complete();
            }
        }, verifyCodeLoginObservable);

    }

}
