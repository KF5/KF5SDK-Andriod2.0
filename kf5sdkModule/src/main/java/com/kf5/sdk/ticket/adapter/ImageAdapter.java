package com.kf5.sdk.ticket.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kf5.sdk.R;
import com.kf5.sdk.helpcenter.entity.Attachment;
import com.kf5.sdk.im.expression.utils.ImageBase;
import com.kf5.sdk.system.base.CommonAdapter;
import com.kf5.sdk.system.utils.ImageLoaderManager;
import com.kf5.sdk.system.utils.Utils;

import java.util.List;

/**
 * author:chosen
 * date:2016/10/20 16:55
 * email:812219713@qq.com
 */

public class ImageAdapter extends CommonAdapter<Attachment> {

    public ImageAdapter(Context context, List<Attachment> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {
            view = inflateLayout(R.layout.kf5_grid_view_item, viewGroup);
            viewHolder = new ViewHolder();
            viewHolder.imageView = findViewById(view, R.id.kf5_image_view);
            ViewGroup.LayoutParams layoutParams = viewHolder.imageView.getLayoutParams();
            layoutParams.width = (Utils.getDisplayWidth(mContext) - 32) / 3;
            layoutParams.height = (Utils.getDisplayWidth(mContext) - 32) / 3;
            viewHolder.imageView.setLayoutParams(layoutParams);
            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();
        viewHolder.bindData(getItem(position));
        return view;
    }

    private class ViewHolder {
        ImageView imageView;

        void bindData(Attachment attachment) {
            String name = attachment.getName();
//            String prefix = attachment.getName().substring(name.lastIndexOf('.') + 1, name.length());
//            LogUtil.printf("这里是后缀名" + prefix);
            if (Utils.isImage(Utils.getPrefix(name))) {
                if (attachment.getContent_url().startsWith("http"))
                    ImageLoaderManager.getInstance(mContext).displayImage(attachment.getContent_url(), imageView);
                else
                    ImageLoaderManager.getInstance(mContext).displayImage(ImageBase.getImagePath(ImageBase.Scheme.FILE, mContext, attachment.getContent_url()), imageView);
            } else
                ImageLoaderManager.getInstance(mContext).displayImage(ImageBase.getImagePath(ImageBase.Scheme.DRAWABLE, mContext, String.valueOf(R.drawable.kf5_document_img)), imageView);
        }
    }

}
