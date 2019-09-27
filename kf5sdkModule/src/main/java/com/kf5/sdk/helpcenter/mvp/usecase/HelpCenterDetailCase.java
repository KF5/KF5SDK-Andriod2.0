package com.kf5.sdk.helpcenter.mvp.usecase;

import com.kf5.sdk.helpcenter.mvp.model.api.IHelpCenterDetailModel;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/19 16:16
 * email:812219713@qq.com
 */

public class HelpCenterDetailCase extends BaseUseCase<HelpCenterDetailCase.RequestCase, HelpCenterDetailCase.ResponseValue> {

    private IHelpCenterDetailModel mIHelpCenterDetailModel;

    public HelpCenterDetailCase(IHelpCenterDetailModel IHelpCenterDetailModel) {
        mIHelpCenterDetailModel = IHelpCenterDetailModel;
    }

    @Override
    public void executeUseCase(RequestCase requestValues) {
        mIHelpCenterDetailModel.getPostDetail(requestValues.mMap, new HttpRequestCallBack() {
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

        public final Map<String, String> mMap;

        public RequestCase(Map<String, String> map) {
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
