package com.kf5.sdk.helpcenter.mvp.usecase;

import com.kf5.sdk.helpcenter.entity.HelpCenterRequestType;
import com.kf5.sdk.helpcenter.mvp.model.api.IHelpCenterTypeChildModel;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/19 15:42
 * email:812219713@qq.com
 */

public class HelpCenterTypeChildCase extends BaseUseCase<HelpCenterTypeChildCase.RequestCase, HelpCenterTypeChildCase.ResponseValue> {

    private IHelpCenterTypeChildModel mIHelpCenterTypeChildModel;

    public HelpCenterTypeChildCase(IHelpCenterTypeChildModel IHelpCenterTypeChildModel) {
        mIHelpCenterTypeChildModel = IHelpCenterTypeChildModel;
    }

    @Override
    public void executeUseCase(RequestCase requestValues) {
        switch (requestValues.mType) {
            case DEFAULT:
                mIHelpCenterTypeChildModel.getPostByID(requestValues.mMap, new HttpRequestCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        getUseCaseCallback().onSuccess(new ResponseValue(result));
                    }

                    @Override
                    public void onFailure(String result) {
                        getUseCaseCallback().onError(result);
                    }
                });
                break;
            case SEARCH:
                mIHelpCenterTypeChildModel.searchDocument(requestValues.mMap, new HttpRequestCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        getUseCaseCallback().onSuccess(new ResponseValue(result));
                    }

                    @Override
                    public void onFailure(String result) {
                        getUseCaseCallback().onError(result);
                    }
                });
                break;
        }
    }

    public static class RequestCase implements BaseUseCase.RequestValues {

        public final HelpCenterRequestType mType;

        public RequestCase(HelpCenterRequestType type, Map<String, String> map) {
            mMap = map;
            mType = type;
        }

        public final Map<String, String> mMap;

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
