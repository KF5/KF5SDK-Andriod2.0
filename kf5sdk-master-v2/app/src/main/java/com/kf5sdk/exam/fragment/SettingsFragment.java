package com.kf5sdk.exam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kf5.sdk.helpcenter.ui.HelpCenterActivity;
import com.kf5.sdk.im.ui.KF5ChatActivity;
import com.kf5.sdk.ticket.ui.FeedBackActivity;
import com.kf5.sdk.ticket.ui.LookFeedBackActivity;
import com.kf5sdk.exam.AboutUsActivity;
import com.kf5sdk.exam.R;

/**
 * author:chosen
 * date:2016/11/18 20:08
 * email:812219713@qq.com
 */

public class SettingsFragment extends Fragment implements View.OnClickListener {


    private RelativeLayout layoutHelpCenter, layoutFeedBack, layoutLookFeedBack, layoutAboutUs, layoutChat;

    private View view;

    public static SettingsFragment getInstance() {

        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_of_setting_layout, null, false);
        initWidgets();
        return view;
    }

    private void initWidgets() {
        layoutAboutUs = (RelativeLayout) view.findViewById(R.id.setting_about_us);
        layoutAboutUs.setOnClickListener(this);
        layoutFeedBack = (RelativeLayout) view.findViewById(R.id.setting_feed_back);
        layoutFeedBack.setOnClickListener(this);
        layoutHelpCenter = (RelativeLayout) view.findViewById(R.id.setting_help_center);
        layoutHelpCenter.setOnClickListener(this);
        layoutLookFeedBack = (RelativeLayout) view.findViewById(R.id.setting_look_feed_back);
        layoutLookFeedBack.setOnClickListener(this);
        layoutChat = (RelativeLayout) view.findViewById(R.id.setting_chat);
        layoutChat.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.setting_about_us:

                startActivity(new Intent(getActivity(), AboutUsActivity.class));

                break;
            case R.id.setting_feed_back:

                startActivity(new Intent(getContext(), FeedBackActivity.class));
                break;

            case R.id.setting_help_center:
                startActivity(new Intent(getContext(), HelpCenterActivity.class));

                break;
            case R.id.setting_look_feed_back:

                startActivity(new Intent(getContext(), LookFeedBackActivity.class));

                break;
            case R.id.setting_chat:
                startActivity(new Intent(getContext(), KF5ChatActivity.class));
                break;
            default:
                break;
        }
    }
}
