package com.kf5.sdk.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kf5.sdk.R;
import com.kf5.sdk.im.db.IMSQLManager;
import com.kf5.sdk.im.entity.Agent;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.widget.CircleImageView;
import com.kf5.sdk.system.utils.Utils;

/**
 * @author Chosen
 * @create 2019/1/8 10:46
 * @email 812219713@qq.com
 */
class BaseHolder {

    private TextView tvDate;
    private CircleImageView headImg;
    protected View convertView;

    protected final Context context;
    protected final MessageAdapter messageAdapter;
    protected IMMessage message;
    protected int position;
    protected boolean isReceive;

    BaseHolder(MessageAdapter messageAdapter, View convertView) {
        this.messageAdapter = messageAdapter;
        this.context = convertView.getContext();
        this.convertView = convertView;
        tvDate = convertView.findViewById(R.id.kf5_tvDate);
        headImg = convertView.findViewById(R.id.kf5_message_head_img);
    }

    void initData(int position, boolean isReceive) {
        this.position = position;
        this.isReceive = isReceive;
        this.message = messageAdapter.getItem(position);
    }


    protected void setUpUI() {
        //显示头像
        if (isReceive) {
            String url = null;
            final int userID = message.getUserId();
            if (userID > 0) {
                if (messageAdapter.urlMap.containsKey(userID)) {
                    url = messageAdapter.urlMap.get(userID);
                } else {
                    Agent agent = IMSQLManager.getAgent(context, userID);
                    url = agent.getPhoto();
                    messageAdapter.urlMap.put(userID, url);
                }
            }
            Glide.with(context).load(!TextUtils.isEmpty(url) ? url : R.drawable.kf5_agent).into(headImg);
        } else {
            Glide.with(context).load(R.drawable.kf5_end_user).into(headImg);
        }
        //根据时间跨度动态加载时间控件
        toggleDateViewVisibility();
    }

    /**
     * 设置时间tip
     */
    private void toggleDateViewVisibility() {
        //处理时间提示tip
        if (position == 0) {
            tvDate.setText(Utils.getAllTime(message.getCreated()));
            tvDate.setVisibility(View.VISIBLE);
        } else {
            IMMessage previousMessage = messageAdapter.getItem(position - 1);
            if (previousMessage != null && (message.getCreated() - previousMessage.getCreated()) > 2 * 60) {
                tvDate.setText(Utils.getAllTime(message.getCreated()));
                tvDate.setVisibility(View.VISIBLE);
            } else {
                tvDate.setVisibility(View.GONE);
            }
        }
    }
}
