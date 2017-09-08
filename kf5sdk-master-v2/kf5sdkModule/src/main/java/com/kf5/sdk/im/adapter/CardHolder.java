package com.kf5.sdk.im.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.CardConstant;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.ui.KF5ChatActivity;
import com.kf5.sdk.system.utils.Utils;

import org.json.JSONObject;

/**
 * author:chosen
 * date:2017/9/6 15:19
 * email:812219713@qq.com
 */

class CardHolder extends AbstractHolder {

    private ImageView productImg;
    private TextView productTitle;
    private TextView productPrice;
    private Button productSend;
    private TextView tvDate;

    CardHolder(View view) {
        super(view.getContext());
        productImg = (ImageView) view.findViewById(R.id.kf5_message_item_product_img);
        productTitle = (TextView) view.findViewById(R.id.kf5_message_item_product_product_name);
        productPrice = (TextView) view.findViewById(R.id.kf5_message_item_product_product_money);
        productSend = (Button) view.findViewById(R.id.kf5_message_item_product_btn_send_info);
        tvDate = (TextView) view.findViewById(R.id.kf5_tvDate);
        view.setTag(this);
    }

    public void bindData(IMMessage message, int position, IMMessage previousMessage) {

        try {
            final String content = message.getMessage();
            JSONObject jsonObject = new JSONObject(content);
            String imgUrl = jsonObject.getString(CardConstant.IMG_URL);
            String title = jsonObject.getString(CardConstant.TITLE);
            String price = jsonObject.getString(CardConstant.PRICE);
            String linkTitle = jsonObject.getString(CardConstant.LINK_TITLE);
            final String url = jsonObject.getString(CardConstant.LINK_URL);
            loadImage(productImg, imgUrl);
            productTitle.setText(title);
            productPrice.setText(price);
            productSend.setText(linkTitle);
            productSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    if (context instanceof KF5ChatActivity) {
                        KF5ChatActivity activity = (KF5ChatActivity) context;
                        activity.sendCardMessage(url);
                    }
                }
            });
            if (position == 0) {
                if (message.getCreated() < 1) {
                    tvDate.setText(Utils.getAllTime(System.currentTimeMillis()));
                } else {
                    tvDate.setText(Utils.getAllTime(message.getCreated()));
                }
                tvDate.setVisibility(View.VISIBLE);
                tvDate.setBackgroundResource(R.drawable.kf5_message_item_with_date_bg);
            } else {
                if (previousMessage != null && (message.getCreated() - previousMessage.getCreated()) > 2 * 60) {
                    tvDate.setText(Utils.getAllTime(message.getCreated()));
                    tvDate.setVisibility(View.VISIBLE);
                    tvDate.setBackgroundResource(R.drawable.kf5_message_item_with_date_bg);
                } else {
                    tvDate.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
