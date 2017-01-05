package com.kf5sdk.exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.entity.KF5User;
import com.kf5.sdk.system.init.UserInfoAPI;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.MD5Utils;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5sdk.exam.fragment.HomeFragment;
import com.kf5sdk.exam.fragment.SettingsFragment;
import com.kf5sdk.exam.utils.Utils;

import java.lang.ref.SoftReference;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private LinearLayout layoutHome, layoutSetting;

    TextView tvHome, tvSetting;

    ImageView imgHome, imgSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initKF5SDK();
        initWidgets();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //			exitBy2Click();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;

        }
        return false;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.home_layout:

                changeFragment(HomeFragment.getInstance());
                layoutHome.setEnabled(false);
                layoutSetting.setEnabled(true);
                tvHome.setTextColor(getResources().getColor(R.color.bottom_text_selected_bg));
                imgHome.setImageResource(R.mipmap.home_home_img_light);
                tvSetting.setTextColor(getResources().getColor(R.color.bottom_text_bg));
                imgSetting.setImageResource(R.mipmap.home_setting);
                break;
            case R.id.setting_layout:
                layoutHome.setEnabled(true);
                layoutSetting.setEnabled(false);
                changeFragment(SettingsFragment.getInstance());
                tvHome.setTextColor(getResources().getColor(R.color.bottom_text_bg));
                imgHome.setImageResource(R.mipmap.home_home_img);
                tvSetting.setTextColor(getResources().getColor(R.color.bottom_text_selected_bg));
                imgSetting.setImageResource(R.mipmap.home_setting_light);
                break;
            default:
                break;
        }
    }

    private void initWidgets() {
        // TODO Auto-generated method stub
        layoutHome = (LinearLayout) findViewById(R.id.home_layout);
        layoutHome.setOnClickListener(this);
        layoutSetting = (LinearLayout) findViewById(R.id.setting_layout);
        layoutSetting.setOnClickListener(this);
        tvHome = (TextView) findViewById(R.id.main_home_tv);
        tvSetting = (TextView) findViewById(R.id.main_setting_tv);
        imgHome = (ImageView) findViewById(R.id.main_home_img);
        imgSetting = (ImageView) findViewById(R.id.main_setting_img);
        changeFragment(HomeFragment.getInstance());
    }


    private void changeFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.home_replace_layout, fragment).commit();
    }

    private void initKF5SDK() {
        SPUtils.clearSP();
        final KF5User kf5User = new KF5User();


        kf5User.appid = "001570f2c8a049396xxxxxxxxxxxxxxec320d0b35";
        kf5User.email = "2xxxxx@qq.com";
        kf5User.helpAddress = "xxxxx.kf5.com";
        kf5User.userAgent = Utils.getAgent(new SoftReference<Context>(MainActivity.this));
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.EMAIL, kf5User.email);
        SPUtils.saveAppID(kf5User.appid);
        SPUtils.saveHelpAddress(kf5User.helpAddress);
        SPUtils.saveUserAgent(Utils.getAgent(new SoftReference<Context>(MainActivity.this)));
        UserInfoAPI.getInstance().loginUser(map, new HttpRequestCallBack() {

            @Override
            public void onSuccess(String result) {
                Log.i("kf5测试", "登录成功" + result);

                final JSONObject jsonObject = JSONObject.parseObject(result);
                int resultCode = jsonObject.getIntValue("error");
                if (resultCode == 0) {
                    JSONObject dataObj = jsonObject.getJSONObject(Field.DATA);
                    JSONObject userObj = dataObj.getJSONObject(Field.USER);
                    String userToken = userObj.getString(Field.USERTOKEN);
                    int id = userObj.getIntValue(Field.ID);
                    SPUtils.saveUserToken(userToken);
                    SPUtils.saveUserId(id);
                    Log.i("kf5测试", MD5Utils.GetMD5Code("kf5_ticket_" + SPUtils.getUserId()));
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String message = jsonObject.getString("message");
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onFailure(String result) {
                Log.i("kf5测试", "登录失败" + result);
            }
        });


//        UserInfoAPI.getInstance().createUser(map, new HttpRequestCallBack() {
//            @Override
//            public void onSuccess(String result) {
//                Log.i("kf5测试", "创建用户成功" + result);
//            }
//
//            @Override
//            public void onFailure(String result) {
//                Log.i("kf5测试", "创建用户失败" + result);
//            }
//        });

//        UserInfoAPI.getInstance().getUserInfo(map, new HttpRequestCallBack() {
//            @Override
//            public void onSuccess(String result) {
//                Log.i("kf5测试", "获取用户信息成功" + result);
//            }
//
//            @Override
//            public void onFailure(String result) {
//                Log.i("kf5测试", "获取用户信息失败" + result);
//            }
//        });
//
//        UserInfoAPI.getInstance().updateUser(map, new HttpRequestCallBack() {
//            @Override
//            public void onSuccess(String result) {
//                Log.i("kf5测试", "更新用户信息成功" + result);
//            }
//
//            @Override
//            public void onFailure(String result) {
//                Log.i("kf5测试", "更新用户信息失败" + result);
//            }
//        });
//
//        UserInfoAPI.getInstance().saveDeviceToken(map, new HttpRequestCallBack() {
//            @Override
//            public void onSuccess(String result) {
//                Log.i("kf5测试", "保存设备Token成功" + result);
//            }
//
//            @Override
//            public void onFailure(String result) {
//                Log.i("kf5测试", "保存设备Token失败" + result);
//            }
//        });
//
//        UserInfoAPI.getInstance().deleteDeviceToken(map, new HttpRequestCallBack() {
//            @Override
//            public void onSuccess(String result) {
//                Log.i("kf5测试", "删除Token成功" + result);
//            }
//
//            @Override
//            public void onFailure(String result) {
//                Log.i("kf5测试", "删除Token失败" + result);
//            }
//        });
    }

}
