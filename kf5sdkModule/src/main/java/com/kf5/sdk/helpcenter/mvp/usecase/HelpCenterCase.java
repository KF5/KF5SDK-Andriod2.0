package com.kf5.sdk.helpcenter.mvp.usecase;

import com.kf5.sdk.helpcenter.entity.HelpCenterRequestType;
import com.kf5.sdk.helpcenter.mvp.model.api.IHelpCenterModel;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/18 15:22
 * email:812219713@qq.com
 */

public class HelpCenterCase extends BaseUseCase<HelpCenterCase.RequestCase, HelpCenterCase.ResponseValue> {

    private IHelpCenterModel mHelpCenterModel;

    public HelpCenterCase(IHelpCenterModel helpCenterModel) {
        mHelpCenterModel = helpCenterModel;
    }

    @Override
    public void executeUseCase(RequestCase requestValues) {
        HelpCenterRequestType type = requestValues.mType;
        switch (type) {

            case SEARCH:

                mHelpCenterModel.searchDocument(requestValues.map, new HttpRequestCallBack() {

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

            case DEFAULT:

                mHelpCenterModel.getCategoriesList(requestValues.map, new HttpRequestCallBack() {

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

        private final Map<String, String> map;

        public RequestCase(HelpCenterRequestType type, Map<String, String> map) {
            mType = type;
            this.map = map;
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
