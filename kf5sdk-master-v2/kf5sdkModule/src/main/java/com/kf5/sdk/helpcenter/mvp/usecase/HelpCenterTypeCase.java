package com.kf5.sdk.helpcenter.mvp.usecase;

import com.kf5.sdk.helpcenter.entity.HelpCenterRequestType;
import com.kf5.sdk.helpcenter.mvp.model.api.IHelpCenterTypeModel;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/19 14:48
 * email:812219713@qq.com
 */

public class HelpCenterTypeCase extends BaseUseCase<HelpCenterTypeCase.RequestCase, HelpCenterTypeCase.ResponseValue> {

    private IHelpCenterTypeModel mIHelpCenterTypeModel;

    public HelpCenterTypeCase(IHelpCenterTypeModel IHelpCenterTypeModel) {
        mIHelpCenterTypeModel = IHelpCenterTypeModel;
    }

    @Override
    public void executeUseCase(RequestCase requestValues) {
        switch (requestValues.mType) {
            case DEFAULT:
                mIHelpCenterTypeModel.getForumByID(requestValues.mMap, new HttpRequestCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        getUseCaseCallback().onSuccess(new HelpCenterTypeCase.ResponseValue(result));
                    }

                    @Override
                    public void onFailure(String result) {
                        getUseCaseCallback().onError(result);
                    }
                });
                break;
            case SEARCH:
                mIHelpCenterTypeModel.searchDocument(requestValues.mMap, new HttpRequestCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        getUseCaseCallback().onSuccess(new HelpCenterTypeCase.ResponseValue(result));
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

        public final Map<String, String> mMap;

        public RequestCase(HelpCenterRequestType type, Map<String, String> map) {
            mType = type;
            mMap = map;
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
