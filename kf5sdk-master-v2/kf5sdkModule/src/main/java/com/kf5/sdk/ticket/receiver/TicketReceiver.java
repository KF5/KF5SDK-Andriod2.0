package com.kf5.sdk.ticket.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.kf5.sdk.system.entity.Field;

/**
 * author:chosen
 * date:2016/10/17 16:40
 * email:812219713@qq.com
 */

public class TicketReceiver extends BroadcastReceiver {

    private RefreshTicketListener refreshTicketListener;

    public void setRefreshTicketListener(RefreshTicketListener refreshTicketListener) {
        this.refreshTicketListener = refreshTicketListener;
    }

    public static final String TICKET_FILTER = "com.kf5sdk.ticket.REFRESH";

    public static final String UPDATE_LIST_ITEM_DATA = "com.kf5sdk.ticket.UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (TextUtils.equals(TICKET_FILTER, action)) {
            refreshTicketListener.refreshTicket();
        } else if (TextUtils.equals(UPDATE_LIST_ITEM_DATA, action)) {
            int id = intent.getIntExtra(Field.ID, 0);
            int lastCommentId = intent.getIntExtra(Field.LAST_COMMENT_ID, 0);
            if (refreshTicketListener != null)
                refreshTicketListener.updateCommentId(id, lastCommentId);
        }
    }

    public interface RefreshTicketListener {

        void refreshTicket();

        void updateCommentId(int id, int last_comment_id);
    }
}