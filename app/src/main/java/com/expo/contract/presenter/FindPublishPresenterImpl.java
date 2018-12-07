package com.expo.contract.presenter;

import android.location.Location;

import com.blankj.utilcode.util.StringUtils;
import com.expo.contract.FindPublishContract;
import com.expo.contract.SeekHelpContract;
import com.expo.entity.Find;
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

public class FindPublishPresenterImpl extends FindPublishContract.Presenter {
    public FindPublishPresenterImpl(FindPublishContract.View view) {
        super(view);
    }

    @Override
    public void addSociety(Find find) {
        mView.showLoadingView();
        for (int i = 0; i < 9; i++) {
            upLoadImgFile(find, find.getUrl(i), i);
        }
    }

    public void upLoadImgFile(Find find, String filePath, int positon) {
        if (!StringUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            String fileType = "";
            if (filePath.endsWith("mp4"))
                fileType = "video.mp4";
            else
                fileType = "image.png";
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            Observable<UploadRsp> verifyCodeLoginObservable = Http.getServer().uploadFile(MultipartBody.Part.createFormData("file", fileType, requestBody));
            Http.request(new ResponseCallback<UploadRsp>() {
                @Override
                protected void onResponse(UploadRsp rsp) {
                    find.setUrl(positon, rsp.shortUrl);
                    submitSociety(find);
                }
            }, verifyCodeLoginObservable);
        } else {
            submitSociety(find);
        }
    }

    private synchronized void submitSociety(Find find) {
        find.times++;
        if (find.times < 9) return;
        Map<String, Object> params = Http.getBaseParams();
        params.put("obj", find.toJson());
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<BaseResponse> addSociety = Http.getServer().addSociety(requestBody);
        Http.request(new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
                mView.hideLoadingView();
                mView.complete();
            }
        }, addSociety);

    }

}
