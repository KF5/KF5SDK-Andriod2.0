package com.kf5.sdk.ticket.mvp.usecase;

import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;
import com.kf5.sdk.ticket.mvp.model.api.ITicketDetailModel;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/20 15:30
 * email:812219713@qq.com
 */

public class TicketDetailCase extends BaseUseCase<TicketDetailCase.RequestCase, TicketDetailCase.ResponseValue> implements HttpRequestCallBack {

    private final ITicketDetailModel mITicketDetailModel;

    public TicketDetailCase(ITicketDetailModel iTicketDetailModel) {
        mITicketDetailModel = iTicketDetailModel;
    }


    @Override
    public void executeUseCase(RequestCase requestValues) {
        switch (requestValues.mRequestType) {
            case GET_TICKET_DETAIL:
                mITicketDetailModel.getTicketDetail(requestValues.map, this);
                break;
            case REPLY_TICKET:
                mITicketDetailModel.replyTicket(requestValues.map, this);
                break;
            case UPLOAD_ATTACHMENT:
                if (requestValues.mFiles != null)
                    mITicketDetailModel.uploadAttachment(requestValues.map, requestValues.mFiles, this);
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

        private final RequestType mRequestType;

        private List<File> mFiles;

        public RequestCase(Map<String, String> map, RequestType requestType) {
            this.map = map;
            mRequestType = requestType;
        }

        public RequestCase(Map<String, String> map, RequestType requestType, List<File> files) {
            this.map = map;
            mRequestType = requestType;
            mFiles = files;
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
        REPLY_TICKET, GET_TICKET_DETAIL, UPLOAD_ATTACHMENT
    }

}
