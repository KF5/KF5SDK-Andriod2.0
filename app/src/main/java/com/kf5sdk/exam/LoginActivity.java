package com.kf5sdk.exam;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kf5.sdk.im.utils.IMCacheUtils;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.entity.ParamsKey;
import com.kf5.sdk.system.init.UserInfoAPI;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.LogUtil;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.utils.SafeJson;
import com.kf5.sdk.system.widget.DialogBox;
import com.kf5sdk.exam.adapter.HelpAddressAdapter;
import com.kf5sdk.exam.entity.HelpAddress;
import com.kf5sdk.exam.utils.Preference;
import com.kf5sdk.exam.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgBack;

    private EditText etEmail, etAddress, etAppid, etName;

    private Button btnLogin;


    private TextView tvHostChange;

    private List<HelpAddress> mHelpAddresses = new ArrayList<>();

    private RadioGroup mRadioGroup;


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
        tvHostChange = (TextView) findViewById(R.id.tv_host_change);
        tvHostChange.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        etEmail.setText(year + "-" + (month + 1) + "-" + day + "@qq.com");
//        etAddress.setText("im2beta.kf5.com");
//        etAppid.setText("001589d1e48784ee06bf96e16d845ca177b73f48ec6a0b2d");
//        etName.setText("Android 用户");


        etEmail.setText(year + "-" + (month + 1) + "-" + day + "@qq.com");
//        etAddress.setText("aibox.kf5.com");
//        etAppid.setText("00159b67b412e7033f89a377fc248bc151455075de60761b");
//        etName.setText("Android 用户");

//        SPUtils.saveAppID("00159b67b412e7033f89a377fc248bc151455075de60761b");
//        SPUtils.saveHelpAddress("aibox.kf5.com");
//        etAddress.setText("chosen.kf5.com");
//        etAppid.setText("0015703278adb2883f1e71145ffa131ef6a8073e3ac7ec00");
//        etName.setText("Android 用户");

//        etAddress.setText("yyxx.kf5.com");
//        etAppid.setText("00159409e3aa8dd8b1582dc397ebd11bf330bbd3bbfa3437");
//        etName.setText("Android 用户");

//        etEmail.setText("cc454f6c-d7c7-4717-aa46-14b1ee4a8479@qq.com");
//        etAddress.setText("mifan365.kf5.com");
//        etAppid.setText("0015a444192a3dc846e7337f11d0743c84f4a6a8e6504546");
//        etName.setText("Android 用户");

//        etEmail.setText("123456@qq.com");
        etAddress.setText("tianxiang.kf5.com");
        etAppid.setText("00155bee6f7945ea5aa21c6ffc35f7aa7ed0999d7c6b6029");
        etName.setText("Android 用户");

//        etEmail.setText("18364263806");
//        etAddress.setText("rsdpay.kf5.com");
//        etAppid.setText("0015ae3f6e356cac3729747a22d8e0fd2de299d8fedea441");
//        etName.setText("Android 用户");

//        etEmail.setText("xinm@51eaju.com");
//        etAddress.setText("yianju001.kf5.com");
//        etAppid.setText("00158e5ce330c3585dd60fbac8fb5844f2e8878cb0402a3a");
//        etName.setText("Android 用户");

//        etEmail.setText("12345@qq.com");
//        etAddress.setText("twyunjiang.kf5.com");
//        etAppid.setText("00158ee40759d14a3b83cde37dc60888ef31161f938751c3");
//        etName.setText("Android 用户");
        mHelpAddresses.add(new HelpAddress("chosen", "0015703278adb2883f1e71145ffa131ef6a8073e3ac7ec00", "chosen.kf5.com"));
        mHelpAddresses.add(new HelpAddress("tianxiang", "00155bee6f7945ea5aa21c6ffc35f7aa7ed0999d7c6b6029", "tianxiang.kf5.com"));
        mHelpAddresses.add(new HelpAddress("im2beta", "001589d1e48784ee06bf96e16d845ca177b73f48ec6a0b2d", "im2beta.kf5.com"));
        mHelpAddresses.add(new HelpAddress("wuruo", "001589a8b77a298cb35c8b8ef2376372fca61daa4799416a", "wuruo.kf5.com"));
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        if (IMCacheUtils.temporaryMessageFirst(LoginActivity.this)) {
            RadioButton radioButton = (RadioButton) findViewById(R.id.rb_message_first);
            radioButton.setChecked(true);
            LogUtil.printf("先发消息再排队");
        } else {
            RadioButton radioButton = (RadioButton) findViewById(R.id.rb_queue_first);
            radioButton.setChecked(true);
            LogUtil.printf("先排队再发消息");
        }
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_message_first:
                        IMCacheUtils.setTemporaryMessageFirst(LoginActivity.this, true);
                        LogUtil.printf("设置先发消息再排队");
                        break;
                    case R.id.rb_queue_first:
                        IMCacheUtils.setTemporaryMessageFirst(LoginActivity.this, false);
                        LogUtil.printf("设置先排队再发消息");
                        break;
                }
            }
        });
//        etEmail.setText("123456@qq.com");
//        etAddress.setText("ielpm.kf5.com");
//        etAppid.setText("001590085a4d173b65ccd04246e5ef91127f0d08e7e3d3de");
//        etName.setText("Android 用户");

//        etEmail.setText("123456789231456@qq.com");
//        etAddress.setText("ximalaya.kf5.com");
//        etAppid.setText("00158298f97da6befccc2d53ae94fd0087fef30dc278b20b");
//        etName.setText("Android测试");


//        etEmail.setText("123456@qq.com");
//        etAddress.setText("xuzy.kf5.com");
//        etAppid.setText("00158fd7bc7ada31ef6de3bfa0ee1201cb5f18e789d05a7c");
//        etName.setText("Android 用户");


//        etEmail.setText("1234567@qq.com");
//        etAddress.setText("im2.kf5.com");
//        etAppid.setText("00158b3c29707edc00722d20870160827178d782697e97b7");
//        etName.setText("Android 用户");


        //第一个测试AppS
//        etEmail.setText("123@qq.com");
//        etAddress.setText("wuruo.kf5.com");
//        etAppid.setText("001570f2c8a0493961e0a5d927d3f8168dc1d3ec320d0b35");
//        etName.setText("Android 用户");


//        第二个测试App
//        etEmail.setText("123@qq.com");
//        etAddress.setText("wuruo.kf5.com");
//        etAppid.setText("001589a8b77a298cb35c8b8ef2376372fca61daa4799416a");
//        etName.setText("Android 用户");


//        etEmail.setText("123424@qq.com");
//        etAddress.setText("tianxiang.kf5.com");
//        etAppid.setText("00155bee6f7945ea5aa21c6ffc35f7aa7ed0999d7c6b6029");
//        etName.setText("Android 用户");

//
//        etEmail.setText("123456@qq.com");
//        etAddress.setText("im2beta.kf5.com");
//        etAppid.setText("001589d1e48784ee06bf96e16d845ca177b73f48ec6a0b2d");
//        etName.setText("Android 用户");


//        etEmail.setText("15110272604");
//        etAddress.setText("yianju001.kf5.com");
//        etAppid.setText("00158e5ce330c3585dd60fbac8fb5844f2e8878cb0402a3a");
//        etName.setText("Android 用户");

//        etEmail.setText("123456@qq.com");
//        etAddress.setText("laoliu.kf5.com");
//        etAppid.setText("00158eaef2fac9251e8019aa3fab1b9194750c368f1d5b60");
//        etName.setText("Android 用户");


//        etEmail.setText("123424@qq.com");
//        etAddress.setText("shixiseng.kf5.com");
//        etAppid.setText("00158abb1c7321d6c8e4092bba0eab412bc8fa9e32a12fc5");
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
//                map.put(ParamsKey.EMAIL, etEmail.getText().toString());
                map.put(ParamsKey.EMAIL, etEmail.getText().toString());
//                map.put(Field.PHONE, etEmail.getText().toString());
//                map.put(ParamsKey.NAME, "能不能设置昵称？");
                LogUtil.printf("测试");
                SPUtils.saveAppID(etAppid.getText().toString());
                SPUtils.saveHelpAddress(etAddress.getText().toString());
                //这里是传入用户自定义字段信息，同时更新用户信息接口也可以设置用户自定义字段。
                JSONArray jsonArray = new JSONArray();

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(ParamsKey.NAME, "field_1000572");
                    jsonObject.put(ParamsKey.VALUE, "这里是测试字段");
                    jsonArray.put(jsonObject);

                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put(ParamsKey.NAME, "field_1000573");
                    jsonObject1.put(ParamsKey.VALUE, "这里是文本区域");
                    jsonArray.put(jsonObject1);

                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put(ParamsKey.NAME, "field_1000574");
                    jsonObject2.put(ParamsKey.VALUE, "1");
                    //      jsonObject.put("field_1000572", "13233");
                    jsonArray.put(jsonObject2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.put(ParamsKey.USER_FIELDS, jsonArray.toString());
                LogUtil.printf(map.toString());
                SPUtils.saveUserAgent(Utils.getAgent(new SoftReference<Context>(LoginActivity.this)));
                UserInfoAPI.getInstance().createUser(map
                        , new HttpRequestCallBack() {
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
            case R.id.tv_host_change:
                createListAddress();
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
        map.put(ParamsKey.DEVICE_TOKEN, "123456");
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


    private void createListAddress() {
        final Dialog dialog = new Dialog(LoginActivity.this);
        ListView listView = (ListView) LayoutInflater.from(LoginActivity.this).inflate(R.layout.help_address_lisview, null);
        dialog.setTitle("平台选择");
        dialog.setContentView(listView);
        listView.setAdapter(new HelpAddressAdapter(LoginActivity.this, mHelpAddresses));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HelpAddress helpAddress = mHelpAddresses.get(position);
                etAddress.setText(helpAddress.helpAddress);
                etAppid.setText(helpAddress.appid);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
