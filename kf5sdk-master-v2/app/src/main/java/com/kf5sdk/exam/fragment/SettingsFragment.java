package com.kf5sdk.exam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kf5.sdk.helpcenter.ui.HelpCenterActivity;
import com.kf5.sdk.im.api.IMAPI;
import com.kf5.sdk.im.db.IMSQLManager;
import com.kf5.sdk.im.ui.KF5ChatActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.ticket.ui.FeedBackActivity;
import com.kf5.sdk.ticket.ui.LookFeedBackActivity;
import com.kf5sdk.exam.LanguageActivity;
import com.kf5sdk.exam.R;

import java.util.Map;

/**
 * author:chosen
 * date:2016/11/18 20:08
 * email:812219713@qq.com
 */

public class SettingsFragment extends Fragment implements View.OnClickListener {


    private TextView layoutHelpCenter, layoutFeedBack, layoutLookFeedBack, layoutAboutUs, layoutChat, tvSetting;

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
        layoutAboutUs = (TextView) view.findViewById(R.id.tvAboutUS);
        layoutAboutUs.setOnClickListener(this);
        layoutFeedBack = (TextView) view.findViewById(R.id.tvFeedBack);
        layoutFeedBack.setOnClickListener(this);
        layoutHelpCenter = (TextView) view.findViewById(R.id.tvHelpCenter);
        layoutHelpCenter.setOnClickListener(this);
        layoutLookFeedBack = (TextView) view.findViewById(R.id.tvLookFeedBack);
        layoutLookFeedBack.setOnClickListener(this);
        layoutChat = (TextView) view.findViewById(R.id.tvIM);
        layoutChat.setOnClickListener(this);
        tvSetting = (TextView) view.findViewById(R.id.tvSetting);
        tvSetting.setOnClickListener(this);
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
            case R.id.tvAboutUS:

//                startActivity(new Intent(getActivity(), AboutUsActivity.class));

                Map<String, String> map = new ArrayMap<>();
                map.put(Field.MESSAGE_ID, IMSQLManager.getLastMessageId(getActivity()) + "");
                map.put(Field.USERTOKEN, SPUtils.getUserToken());
                IMAPI.getInstance().getUnReadMessageCount(map, new HttpRequestCallBack() {
                    @Override
                    public void onSuccess(String result) {

                    }

                    @Override
                    public void onFailure(String result) {

                    }
                });

                break;
            case R.id.tvFeedBack:

                startActivity(new Intent(getContext(), FeedBackActivity.class));
                break;

            case R.id.tvHelpCenter:
                startActivity(new Intent(getContext(), HelpCenterActivity.class));

                break;
            case R.id.tvLookFeedBack:

                startActivity(new Intent(getContext(), LookFeedBackActivity.class));

                break;
            case R.id.tvIM:
                startActivity(new Intent(getContext(), KF5ChatActivity.class));
                break;
            case R.id.tvSetting:
                startActivity(new Intent(getContext(), LanguageActivity.class));
                break;
            default:

                break;
        }
    }
}
