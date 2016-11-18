package com.kf5.sdk.ticket.mvp.usecase;

import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;
import com.kf5.sdk.ticket.mvp.model.api.ITicketFeedBackModel;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/21 16:32
 * email:812219713@qq.com
 */

public class TicketFeedBackCase extends BaseUseCase<TicketFeedBackCase.RequestCase, TicketFeedBackCase.ResponseValue> implements HttpRequestCallBack {

    private final ITicketFeedBackModel mTicketFeedBackModel;

    public TicketFeedBackCase(ITicketFeedBackModel ticketDetailModel) {
        mTicketFeedBackModel = ticketDetailModel;
    }

    @Override
    public void executeUseCase(RequestCase requestValues) {

        switch (requestValues.mRequestType) {
            case CREATE_TICKET:
                mTicketFeedBackModel.createTicket(requestValues.map, this);
                break;
            case UPLOAD_ATTACHMENT:
                mTicketFeedBackModel.uploadAttachment(requestValues.map, requestValues.mFile, this);
                break;
        }

    }

    @Override
    public void onSuccess(String result) {
        getUseCaseCallback().onSuccess(new ResponseValue(result));
    }

    @Override
    public void onFailure(String result) {
        getUseCaseCallback().onError(result);
    }

    public static class RequestCase implements BaseUseCase.RequestValues {

        private final Map<String, String> map;

        private final List<File> mFile;

        private final RequestType mRequestType;

        public RequestCase(Map<String, String> map, List<File> file, RequestType requestType) {
            this.map = map;
            mFile = file;
            mRequestType = requestType;
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

    public enum RequestType {
        CREATE_TICKET, UPLOAD_ATTACHMENT
    }

}
