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
//        etAddress.setText("tianxiang.kf5.com");
//        etAppid.setText("0015c0f2c6bc70f42c90c1f8cc6c5bc94566eaf720bedd9a");
//        etName.setText("Android 用户");

        etEmail.setText(year + "-" + (month + 1) + "-" + day + "@qq.com");
        etEmail.setText("123@qq.com");
        etAddress.setText("im2beta.kf5.com");
        etAppid.setText("001589d1e48784ee06bf96e16d845ca177b73f48ec6a0b2d");
        etName.setText("Android 用户");

//        etEmail.setText(year + "-" + (month + 1) + "-" + day + "@qq.com");
//        etEmail.setText("123@qq.com");
//        etAddress.setText("fuqinjinrong.kf5.com");
//        etAppid.setText("0015acacf8362a218995f60ce861490cb2b063b3513a6069");
//        etName.setText("Android 用户");

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

    private void deleteToken(Map<String, String> map) {
        UserInfoAPI.getInstance().deleteDeviceToken(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result) {
                Log.i("kf5测试", "删除设备Token成功" + result);
            }

            @Override
            public void onFailure(String result) {
                Log.i("kf5测试", "删除设备Token失败" + result);
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


//    private void initUserInfo(Map<String, String> map) {
//        UserInfoAPI.getInstance().createUser(map
//                , new HttpRequestCallBack() {
//                    @Override
//                    public void onSuccess(final String result) {
//                        Log.i("kf5测试", "登录成功" + result);
//                        final JSONObject jsonObject = SafeJson.parseObj(result);
//                        int resultCode = SafeJson.safeInt(jsonObject, "error");
//                        if (resultCode == 0) {
//                            //我们先调用的是createUser接口，当code==0时，意味着注册用户成功，这里我们就可以缓存必要的信息，比如说userToken，userId；
//                            //如果我们需要用到推送功能，我们也可以在保存userToken之后调用saveToken接口。
//                            //这里省略了具体解析逻辑。
////                           SPUtils.saveUserToken(userToken);
////                           SPUtils.saveUserId(id);
//                        } else {
//                            //由于我们先调用的是createUser接口，当code不等于0时，我们就默认用户存在，则走登陆接口
//                            loginUser(map);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(String result) {
//                        //onSuccess回调接口是指http请求正常的回调接口，它的回调不代表用户信息初始化成功；
//                        //onFailure回调接口是在http请求失败时回调的接口。例如java.net.UnknownHostException: Unable to resolve host "sdkmobilev2":
//                        //No address associated with hostname
//                        Log.i("kf5测试", "登录失败" + result);
//                    }
//                });
//    }
//
//
//    private void loginUser(final Map<String, String> map) {
//        UserInfoAPI.getInstance().loginUser(map, new HttpRequestCallBack() {
//            @Override
//            public void onSuccess(final String result) {
//                Log.i("kf5测试", "登录成功" + result);
//                try {
//                    final JSONObject jsonObject = SafeJson.parseObj(result);
//                    int resultCode = SafeJson.safeInt(jsonObject, "error");
//                    if (resultCode == 0) {
//                        //由于先调用了createUser接口，当createUser接口的code不等于0时，且传入参数合法时，我们默认用户信息存在，这时我们就调用登陆接口。
//                        //当loginUser接口返回值等于0时，我们就认为用户登陆成功，这里我们就可以缓存必要的信息，比如说userToken，userId；
//                        //如果我们需要用到推送功能，我们也可以在保存userToken之后调用saveToken接口。
//                        //这里省略了具体解析逻辑。
//                        //SPUtils.saveUserToken(userToken);
//                        //SPUtils.saveUserId(id);
//                    } else {
//                        //那么问题来了，当先调用的是createUser接口，如果用户以存在，这时走到了loginUser接口，当loginUser接口的返回的code也不等于0呢？
//                        //如果按照这种逻辑调用，且code也不等于0时，那么就是属于参数格式不正确，或者参数遗漏造成的，参数规则请往下看。
//                        String message = jsonObject.getString("message");
//                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(String result) {
//                Log.i("kf5测试", "登录失败" + result);
//            }
//        });
//    }
}
