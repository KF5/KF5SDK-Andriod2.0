package com.kf5.sdk.helpcenter.mvp.usecase;

import com.kf5.sdk.helpcenter.mvp.model.HelpModelManager;

/**
 * author:chosen
 * date:2016/10/18 16:08
 * email:812219713@qq.com
 */

public class HelpUseCaseManager {
    public static HelpCenterCase provideHelpCenterCase() {
        return new HelpCenterCase(HelpModelManager.provideHelpCenterModel());
    }

    public static HelpCenterTypeCase provideHelpCenterTypeCase() {
        return new HelpCenterTypeCase(HelpModelManager.provideHelpCenterTypeModel());
    }

    public static HelpCenterTypeChildCase provideHelpCenterTypeChildCase() {
        return new HelpCenterTypeChildCase(HelpModelManager.provideHelpCenterTypeChildModel());
    }

    public static HelpCenterDetailCase provideHelpCenterDetailCase() {
        return new HelpCenterDetailCase(HelpModelManager.provideHelpCenterDetailModel());
    }

}
