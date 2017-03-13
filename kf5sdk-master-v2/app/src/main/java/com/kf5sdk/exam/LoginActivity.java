package com.kf5sdk.exam;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.init.UserInfoAPI;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.utils.SafeJson;
import com.kf5.sdk.system.widget.DialogBox;
import com.kf5sdk.exam.utils.Preference;
import com.kf5sdk.exam.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgBack;

    private EditText etEmail, etAddress, etAppid, etName;

    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initWidgets();
    }


    private void initWidgets() {
        imgBack = (ImageView) findViewById(R.id.back_img);
        imgBack.setOnClickListener(this);
        etEmail = (EditText) findViewById(R.id.et_email);
        etAddress = (EditText) findViewById(R.id.et_helpaddress);
        etAppid = (EditText) findViewById(R.id.et_appid);
        etName = (EditText) findViewById(R.id.et_name);
        btnLogin = (Button) findViewById(R.id.login);
        btnLogin.setOnClickListener(this);
        etEmail.setText("123456@qq.com");
        etAddress.setText("chosen.kf5.com");
        etAppid.setText("0015703278adb2883f1e71145ffa131ef6a8073e3ac7ec00");
        etName.setText("Android 用户");


        //第一个测试App
//        etEmail.setText("123@qq.com");
//        etAddress.setText("wuruo.kf5.com");
//        etAppid.setText("001570f2c8a0493961e0a5d927d3f8168dc1d3ec320d0b35");
//        etName.setText("Android 用户");


//        第二个测试App
//        etEmail.setText("123@qq.com");
//        etAddress.setText("wuruo.kf5.com");
//        etAppid.setText("001589a8b77a298cb35c8b8ef2376372fca61daa4799416a");
//        etName.setText("Android 用户");


//        etEmail.setText("123@qq.com");
//        etAddress.setText("tianxiang.kf5.com");
//        etAppid.setText("00155bee6f7945ea5aa21c6ffc35f7aa7ed0999d7c6b6029");
//        etName.setText("Android 用户");


    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.login:
                final Map<String, String> map = new ArrayMap<>();
                map.put(Field.EMAIL, etEmail.getText().toString());
//                map.put(Field.PHONE, "18715965784");
                SPUtils.saveAppID(etAppid.getText().toString());
                SPUtils.saveHelpAddress(etAddress.getText().toString());
                SPUtils.saveUserAgent(Utils.getAgent(new SoftReference<Context>(LoginActivity.this)));

                UserInfoAPI.getInstance().createUser(map, new HttpRequestCallBack() {
                    @Override
                    public void onSuccess(final String result) {
                        Log.i("kf5测试", "登录成功" + result);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final JSONObject jsonObject = SafeJson.parseObj(result);
                                    int resultCode = SafeJson.safeInt(jsonObject, "error");
                                    if (resultCode == 0) {
                                        Preference.saveBoolLogin(LoginActivity.this, true);
                                        final JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                                        JSONObject userObj = SafeJson.safeObject(dataObj, Field.USER);
                                        if (userObj != null) {
                                            String userToken = userObj.getString(Field.USERTOKEN);
                                            int id = userObj.getInt(Field.ID);
                                            SPUtils.saveUserToken(userToken);
                                            SPUtils.saveUserId(id);
                                            new DialogBox(LoginActivity.this)
                                                    .setMessage("登录成功")
                                                    .setLeftButton("取消", null)
                                                    .setRightButton("确定", new DialogBox.onClickListener() {
                                                        @Override
                                                        public void onClick(DialogBox dialog) {
                                                            dialog.dismiss();
                                                            LoginActivity.this.finish();
                                                        }
                                                    }).show();
                                            saveToken(map);
                                        }
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //                                            String message = jsonObject.getString("message");
                                                //                                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                                loginUser(map);
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(String result) {
                        Log.i("kf5测试", "登录失败" + result);
                    }
                });

                break;
        }
    }


    private void loginUser(final Map<String, String> map) {

        UserInfoAPI.getInstance().loginUser(map, new HttpRequestCallBack() {

            @Override
            public void onSuccess(final String result) {
                Log.i("kf5测试", "登录成功" + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject jsonObject = SafeJson.parseObj(result);
                            int resultCode = SafeJson.safeInt(jsonObject, "error");
                            if (resultCode == 0) {
                                Preference.saveBoolLogin(LoginActivity.this, true);
                                final JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                                JSONObject userObj = SafeJson.safeObject(dataObj, Field.USER);
                                if (userObj != null) {
                                    String userToken = userObj.getString(Field.USERTOKEN);
                                    int id = userObj.getInt(Field.ID);
                                    SPUtils.saveUserToken(userToken);
                                    SPUtils.saveUserId(id);
                                    new DialogBox(LoginActivity.this)
                                            .setMessage("登录成功")
                                            .setLeftButton("取消", null)
                                            .setRightButton("确定", new DialogBox.onClickListener() {
                                                @Override
                                                public void onClick(DialogBox dialog) {
                                                    dialog.dismiss();
                                                    LoginActivity.this.finish();
                                                }
                                            }).show();
                                    saveToken(map);
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String message = jsonObject.getString("message");
                                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String result) {
                Log.i("kf5测试", "登录失败" + result);
            }
        });


    }


    private void saveToken(Map<String, String> map) {
        map.put(Field.DEVICE_TOKEN, "123456");
        UserInfoAPI.getInstance().saveDeviceToken(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result) {
                Log.i("kf5测试", "保存设备Token成功" + result);
            }

            @Override
            public void onFailure(String result) {
                Log.i("kf5测试", "保存设备Token失败" + result);
            }
        });

        UserInfoAPI.getInstance().getUserInfo(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFailure(String result) {

            }
        });
    }


}
