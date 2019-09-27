package com.kf5.sdk.im.mvp.usecase;

import com.kf5.sdk.im.mvp.model.IMModelManager;

/**
 * author:chosen
 * date:2016/10/27 18:25
 * email:812219713@qq.com
 */

public class IMCaseManager {

    public static IMCase provideIMCase() {
        return new IMCase(IMModelManager.provideIMModel());
    }
}
