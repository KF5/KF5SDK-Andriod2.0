package com.kf5.sdk.system.entity;

/**
 * @author Chosen
 * @create 2019/4/4 14:02
 * @email 812219713@qq.com
 */
public final class TitleBarProperty {

    private String titleContent;
    private boolean rightViewVisible;
    private boolean rightViewClick;
    private String rightViewContent;

    private TitleBarProperty(String titleContent, boolean rightViewVisible, boolean rightViewClick, String rightViewContent) {
        this.titleContent = titleContent;
        this.rightViewVisible = rightViewVisible;
        this.rightViewClick = rightViewClick;
        this.rightViewContent = rightViewContent;
    }

    public String getTitleContent() {
        return titleContent;
    }

    public boolean isRightViewVisible() {
        return rightViewVisible;
    }

    public boolean isRightViewClick() {
        return rightViewClick;
    }

    public String getRightViewContent() {
        return rightViewContent;
    }


    /**
     * 当rightViewVisible为true时，才会绑定点击事件和设置内容，否则不做处理，直接设置为Gone。
     */
    public static class Builder {
        private String titleContent;
        private boolean rightViewVisible;
        private boolean rightViewClick;
        private String rightViewContent;

        public Builder setTitleContent(String titleContent) {
            this.titleContent = titleContent;
            return this;
        }

        public Builder setRightViewVisible(boolean rightViewVisible) {
            this.rightViewVisible = rightViewVisible;
            return this;
        }

        public Builder setRightViewClick(boolean rightViewClick) {
            this.rightViewClick = rightViewClick;
            return this;
        }

        public Builder setRightViewContent(String rightViewContent) {
            this.rightViewContent = rightViewContent;
            return this;
        }

        public TitleBarProperty build() {
            return new TitleBarProperty(titleContent, rightViewVisible, rightViewClick, rightViewContent);
        }
    }
}
