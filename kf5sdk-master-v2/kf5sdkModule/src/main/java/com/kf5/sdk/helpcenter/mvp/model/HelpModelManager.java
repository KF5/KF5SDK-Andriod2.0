package com.kf5.sdk.helpcenter.mvp.model;

/**
 * author:chosen
 * date:2016/10/18 15:06
 * email:812219713@qq.com
 */

public class HelpModelManager {

    public static HelpCenterModel provideHelpCenterModel() {
        return new HelpCenterModel();
    }

    public static HelpCenterTypeModel provideHelpCenterTypeModel() {
        return new HelpCenterTypeModel();
    }

    public static HelpCenterTypeChildModel provideHelpCenterTypeChildModel() {
        return new HelpCenterTypeChildModel();
    }

    public static HelpCenterDetailModel provideHelpCenterDetailModel() {
        return new HelpCenterDetailModel();
    }

}
