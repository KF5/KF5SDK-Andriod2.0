package com.kf5.sdk.ticket.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.system.base.CommonAdapter;
import com.kf5.sdk.system.utils.Utils;
import com.kf5.sdk.ticket.db.KF5SDKtoHelper;
import com.kf5.sdk.ticket.entity.Message;
import com.kf5.sdk.ticket.entity.Requester;

import java.util.List;

/**
 * author:chosen
 * date:2016/10/20 14:03
 * email:812219713@qq.com
 */

public class FeedBackAdapter extends CommonAdapter<Requester> {

    private KF5SDKtoHelper mKF5SDKtoHelper;

    private String[] statusArray;

    public FeedBackAdapter(Context context, List<Requester> list, KF5SDKtoHelper helper) {
        super(context, list);
        this.mKF5SDKtoHelper = helper;
        mKF5SDKtoHelper.openDatabase();
        statusArray = context.getResources().getStringArray(R.array.kf5_ticket_status);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {
            view = inflateLayout(R.layout.kf5_look_feed_back_listview_item, viewGroup);
            viewHolder = new ViewHolder();
            viewHolder.mDate = findViewById(view, R.id.kf5_look_feed_back_listitem_date);
            viewHolder.mTitle = findViewById(view, R.id.kf5_look_feed_back_listitem_title);
            viewHolder.mStatus = findViewById(view, R.id.kf5_look_feed_back_listitem_statu);
            viewHolder.mImgStatus = findViewById(view, R.id.kf5_look_feed_back_listitem_update);
            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();
        viewHolder.bindData(getItem(position));
        return view;
    }

    private class ViewHolder {
        TextView mTitle, mDate, mStatus;
        ImageView mImgStatus;

        void bindData(Requester requester) {
            Message message = mKF5SDKtoHelper.queryOneData(String.valueOf(requester.getId()));
            if (message != null) {
                //如果本地时间与获取到的时间不一致，设置为可见
                if (TextUtils.equals(message.getLastCommentId(), String.valueOf(requester.getLast_comment_id()))) {
                    mImgStatus.setVisibility(View.INVISIBLE);
                } else {
                    mImgStatus.setVisibility(View.VISIBLE);
                }
            } else {
                Message message2 = new Message();
                message2.setId(String.valueOf(requester.getId()));
                message2.setLastCommentId(String.valueOf(requester.getLast_comment_id()));
                message2.setRead(false);
                mKF5SDKtoHelper.insert(message2);
                mImgStatus.setVisibility(View.INVISIBLE);
            }
            mDate.setText(Utils.getAllTime(requester.getCreated_at()));
            mTitle.setText(requester.getDescription());
            switch (requester.getStatus()) {
                case 0:
                    mStatus.setText(statusArray[0]);
                    break;
                case 1:
                    mStatus.setText(statusArray[1]);
                    break;
                case 2:
                    mStatus.setText(statusArray[2]);
                    break;
                case 3:
                    mStatus.setText(statusArray[3]);
                    break;
                case 4:
                    mStatus.setText(statusArray[4]);
                    break;
            }

        }

    }


}
