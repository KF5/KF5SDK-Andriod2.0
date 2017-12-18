package com.kf5.sdk.ticket.adapter;

import android.content.Context;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.system.base.CommonAdapter;
import com.kf5.sdk.system.listener.CopyTextLongClickListener;
import com.kf5.sdk.system.utils.CustomTextView;
import com.kf5.sdk.system.utils.Utils;
import com.kf5.sdk.system.widget.NoScrollGridView;
import com.kf5.sdk.ticket.entity.Comment;
import com.kf5.sdk.ticket.listener.AttachmentItemClickListener;
import com.kf5.sdk.ticket.listener.AttachmentItemLongClickListener;

import java.util.List;

/**
 * author:chosen
 * date:2016/10/20 16:37
 * email:812219713@qq.com
 */

public class FeedBackDetailAdapter extends CommonAdapter<Comment> {

    public FeedBackDetailAdapter(Context context, List<Comment> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            view = inflateLayout(R.layout.kf5_feed_back_detail_item, viewGroup);
            holder = new ViewHolder();
            holder.tvContent = findViewById(view, R.id.kf5_feed_back_detail_content);
            holder.tvDate = findViewById(view, R.id.kf5_feed_back_detail_date);
            holder.tvName = findViewById(view, R.id.kf5_feed_back_detail_name);
            holder.mGridView = findViewById(view, R.id.kf5_feed_back_detai_grid_view);
            holder.mProgressBar = findViewById(view, R.id.kf5_progressBar);
            holder.failedImageView = findViewById(view, R.id.kf5_feed_back_detail_failed_image);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        holder.bindData(mContext, getItem(position));
        return view;
    }

    private class ViewHolder {

        TextView tvName, tvContent, tvDate;

        NoScrollGridView mGridView;

        ProgressBar mProgressBar;

        ImageView failedImageView;

        void bindData(Context context, Comment details) {
            CustomTextView.stripUnderlines(context, tvContent, details.getContent(), Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
            tvContent.setOnLongClickListener(new CopyTextLongClickListener(context, details.getContent()));
            tvDate.setText(Utils.getAllTime(details.getCreatedAt()));
            tvName.setText(details.getAuthorName());
            if (details.getAttachmentList() != null && details.getAttachmentList().size() > 0) {
                ImageAdapter adapter = new ImageAdapter(context, details.getAttachmentList());
                mGridView.setVisibility(View.VISIBLE);
                mGridView.setAdapter(adapter);
                mGridView.setOnItemClickListener(new AttachmentItemClickListener(details.getAttachmentList(), context));
                mGridView.setOnItemLongClickListener(new AttachmentItemLongClickListener(details.getAttachmentList(), context));
            } else {
                mGridView.setVisibility(View.GONE);
            }
            switch (details.getMessageStatus()) {
                case SUCCESS:
                    mProgressBar.setVisibility(View.INVISIBLE);
                    failedImageView.setVisibility(View.INVISIBLE);
                    break;
                case SENDING:
                    mProgressBar.setVisibility(View.VISIBLE);
                    failedImageView.setVisibility(View.INVISIBLE);
                    break;
                case FAILED:
                    mProgressBar.setVisibility(View.INVISIBLE);
                    failedImageView.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

    }

}
