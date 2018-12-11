package com.expo.contract.presenter;

import android.text.TextUtils;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.expo.base.ExpoApp;
import com.expo.base.utils.FileUtils;
import com.expo.contract.FindPublishContract;
import com.expo.entity.Find;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.UploadRsp;
import com.expo.utils.Constants;

import java.io.File;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;

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
            if (filePath.endsWith("mp4")) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                Observable<UploadRsp> verifyCodeLoginObservable = Http.getServer().uploadFile(MultipartBody.Part.createFormData("file", "video.mp4", requestBody));
                Http.request(new ResponseCallback<UploadRsp>() {
                    @Override
                    protected void onResponse(UploadRsp rsp) {
                        find.setUrl(positon, rsp.shortUrl);
                        submitSociety(find);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoadingView();
                        super.onError(e);
                    }
                }, verifyCodeLoginObservable);
            } else {
                Luban.with(ExpoApp.getApplication())
                        .load(filePath)
                        .ignoreBy(200)
                        .setTargetDir(new File(filePath).getParentFile().getPath())
                        .filter(new CompressionPredicate() {
                            @Override
                            public boolean apply(String path) {
                                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                            }
                        })
                        .setRenameListener(new OnRenameListener() {
                            @Override
                            public String rename(String filePath) {
                                String newName = TimeUtils.getNowMills() + ".jpg";
                                filePath = new File(filePath).getParent() + File.separator + newName;
                                FileUtils.createFile(filePath);

                                return newName;
                            }
                        })
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                                // TODO 压缩开始前调用，可以在方法内启动 loading UI
                            }

                            @Override
                            public void onSuccess(File file) {
                                // TODO 压缩成功后调用，返回压缩后的图片文件
                                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                Observable<UploadRsp> verifyCodeLoginObservable = Http.getServer().uploadFile(MultipartBody.Part.createFormData("file", "image.png", requestBody));
                                Http.request(new ResponseCallback<UploadRsp>() {
                                    @Override
                                    protected void onResponse(UploadRsp rsp) {
                                        find.setUrl(positon, rsp.shortUrl);
                                        submitSociety(find);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mView.hideLoadingView();
                                        super.onError(e);
                                    }

                                    @Override
                                    public void onComplete() {
                                        if (!StringUtils.equals(file.getPath(), filePath))
                                            file.delete();
                                        super.onComplete();
                                    }
                                }, verifyCodeLoginObservable);
                            }

                            @Override
                            public void onError(Throwable e) {
                                // TODO 当压缩过程出现问题时调用
                                File file = new File(filePath);
                                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                Observable<UploadRsp> verifyCodeLoginObservable = Http.getServer().uploadFile(MultipartBody.Part.createFormData("file", "image.png", requestBody));
                                Http.request(new ResponseCallback<UploadRsp>() {
                                    @Override
                                    protected void onResponse(UploadRsp rsp) {
                                        find.setUrl(positon, rsp.shortUrl);
                                        submitSociety(find);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mView.hideLoadingView();
                                        super.onError(e);
                                    }
                                }, verifyCodeLoginObservable);
                            }
                        }).launch();
            }
        } else {
            submitSociety(find);
        }

//        if (!StringUtils.isEmpty(filePath)) {
//            File file = new File(filePath);
//            String fileType = "";
//            if(filePath.endsWith(".mp4"))
//                fileType="video.mp4";
//            else
//                fileType="image.png";
//            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            Observable<UploadRsp> verifyCodeLoginObservable = Http.getServer().uploadFile(MultipartBody.Part.createFormData("file", fileType, requestBody));
//            Http.request(new ResponseCallback<UploadRsp>() {
//                @Override
//                protected void onResponse(UploadRsp rsp) {
//                    find.setUrl(positon, rsp.shortUrl);
//                    submitSociety(find);
//                }
//            }, verifyCodeLoginObservable);
//        } else {
//            submitSociety(find);
//        }
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
                mView.complete();
            }

            @Override
            public void onComplete() {
                mView.hideLoadingView();
                super.onComplete();
            }
        }, addSociety);

    }

}
