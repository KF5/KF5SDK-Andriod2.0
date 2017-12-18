package com.kf5sdk.exam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.widget.DialogBox;
import com.kf5sdk.exam.LoginActivity;
import com.kf5sdk.exam.R;
import com.kf5sdk.exam.utils.Preference;

/**
 * author:chosen
 * date:2016/11/18 20:06
 * email:812219713@qq.com
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    View view;

    TextView login;

    public static HomeFragment getInstance() {

        return new HomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        view = inflater.inflate(R.layout.fragment_home_layout, null, false);
        initWidgets();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Preference.getBoolLogin(getActivity())) {
            login.setText("注销");
        } else {
            login.setText("登录");
        }
    }


    private void initWidgets() {
        // TODO Auto-generated method stub
        login = (TextView) view.findViewById(R.id.login);
        login.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login:

                if (TextUtils.equals(login.getText().toString(), "登录")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    new DialogBox(getActivity())
                            .setTitle("温馨提示")
                            .setMessage("是否注销登录?")
                            .setLeftButton("取消", null)
                            .setRightButton("确定", new DialogBox.onClickListener() {

                                @Override
                                public void onClick(DialogBox dialog) {
                                    dialog.dismiss();
                                    login.setText("登录");
                                    SPUtils.clearSP();
                                    Preference.saveBoolLogin(getActivity(), false);
                                }
                            }).show();
                }

                break;

        }
    }

}
