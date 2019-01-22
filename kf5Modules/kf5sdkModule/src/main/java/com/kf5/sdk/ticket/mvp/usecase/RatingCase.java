package com.kf5.sdk.ticket.mvp.usecase;

import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;
import com.kf5.sdk.ticket.mvp.model.api.IRatingModel;

import java.util.Map;

/**
 * author:chosen
 * date:2017/1/4 18:30
 * email:812219713@qq.com
 */

public class RatingCase extends BaseUseCase<RatingCase.RequestCase, RatingCase.ResponseValue> {

    private final IRatingModel mIRatingModel;

    public RatingCase(IRatingModel iRatingModel) {
        mIRatingModel = iRatingModel;
    }

    @Override
    public void executeUseCase(RequestCase requestValues) {
        mIRatingModel.onRating(requestValues.map, new HttpRequestCallBack() {
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

        private final Map<String, String> map;

        public RequestCase(Map<String, String> map) {
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
