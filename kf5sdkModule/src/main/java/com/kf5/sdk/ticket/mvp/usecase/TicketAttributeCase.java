package com.kf5.sdk.ticket.mvp.usecase;

import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;
import com.kf5.sdk.ticket.mvp.model.api.ITicketAttributeModel;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/21 15:53
 * email:812219713@qq.com
 */

public class TicketAttributeCase extends BaseUseCase<TicketAttributeCase.RequestCase, TicketAttributeCase.ResponseValue> {


    private final ITicketAttributeModel mITicketAttributModel;

    public TicketAttributeCase(ITicketAttributeModel iTicketAttributeModel) {
        mITicketAttributModel = iTicketAttributeModel;
    }

    @Override
    public void executeUseCase(RequestCase requestValues) {
        mITicketAttributModel.getTicketAttribute(requestValues.map, new HttpRequestCallBack() {
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
