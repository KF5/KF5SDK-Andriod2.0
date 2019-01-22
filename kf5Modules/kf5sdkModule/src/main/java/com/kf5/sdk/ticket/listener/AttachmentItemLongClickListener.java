package com.kf5.sdk.ticket.listener;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.kf5.sdk.helpcenter.entity.Attachment;
import com.kf5.sdk.system.base.BaseItemLongClickListener;
import com.kf5.sdk.system.widget.DialogBox;

import java.util.List;

/**
 * author:chosen
 * date:2016/10/20 17:11
 * email:812219713@qq.com
 */

public class AttachmentItemLongClickListener extends BaseItemLongClickListener {

    private List<Attachment> attachments;

    private DialogBox chatDialog;

    public AttachmentItemLongClickListener(List<Attachment> attachments, Context context) {
        super(context);
        this.attachments = attachments;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   final int position, long id) {

        return true;
    }
}