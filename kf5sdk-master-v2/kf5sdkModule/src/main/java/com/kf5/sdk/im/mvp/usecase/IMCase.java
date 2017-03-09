package com.kf5.sdk.im.mvp.usecase;

import android.support.v4.util.ArrayMap;

import com.kf5.sdk.im.mvp.model.api.IChatModel;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/27 18:20
 * email:812219713@qq.com
 */

public class IMCase extends BaseUseCase<IMCase.RequestCase, IMCase.ResponseValue> {

    private final IChatModel mIChatModel;

    public IMCase(IChatModel IChatModel) {
        mIChatModel = IChatModel;
    }

    /**
     * 执行该用例
     *
     * @param requestValues 用例输入
     */
    @Override
    public void executeUseCase(RequestCase requestValues) {
        Map<String, String> map = new ArrayMap<>();
        mIChatModel.uploadAttachment(map, requestValues.mFileList, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result) {
                getUseCaseCallback().onSuccess(new ResponseValue(result));
            }

            @Override
            public void onFailure(String result) {
                getUseCaseCallback().onError(result);
            }
        });
    }

    public static class RequestCase implements BaseUseCase.RequestValues {

        private final List<File> mFileList;

        public RequestCase(List<File> fileList) {
            this.mFileList = fileList;
        }
    }

    public static final class ResponseValue implements BaseUseCase.ResponseValue {

        public final String result;

        public ResponseValue(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }
    }

}
