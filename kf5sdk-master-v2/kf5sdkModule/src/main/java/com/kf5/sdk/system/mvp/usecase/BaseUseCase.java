package com.kf5.sdk.system.mvp.usecase;

/**
 * author:chosen
 * date:2016/10/13 15:06
 * email:812219713@qq.com
 */

public abstract class BaseUseCase<Q extends BaseUseCase.RequestValues, P extends BaseUseCase.ResponseValue> {

    private Q mRequestValues;

    private UseCaseCallBack<P> mUseCaseCallBack;

    public void setRequestValues(Q requestValues) {
        mRequestValues = requestValues;
    }

    public Q getRequestValues() {
        return mRequestValues;
    }

    public UseCaseCallBack<P> getUseCaseCallback() {
        return mUseCaseCallBack;
    }

    public void setUseCaseCallBack(UseCaseCallBack<P> useCaseCallBack) {
        mUseCaseCallBack = useCaseCallBack;
    }

    public void run() {
        if (mRequestValues != null) {
            executeUseCase(mRequestValues);
        }
    }

    /**
     * 执行该用例
     *
     * @param requestValues 用例输入
     */
    public abstract void executeUseCase(Q requestValues);

    /**
     * 用例的请求参数
     */
    public interface RequestValues {

    }

    /**
     * 用例的输出
     */
    public interface ResponseValue {

    }

    /**
     * 用例回调
     *
     * @param <R>
     */
    public interface UseCaseCallBack<R> {


        void onSuccess(R response);

        void onError(String msg);
    }

}
