package com.kf5sdk.exam.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kf5.sdk.system.utils.LogUtil;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.widget.DialogBox;
import com.kf5sdk.exam.LoginActivity;
import com.kf5sdk.exam.R;
import com.kf5sdk.exam.utils.Preference;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author:chosen
 * date:2016/11/18 20:06
 * email:812219713@qq.com
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    View view;

    TextView login;

    TextView tvTest;

    private static final String htmltest = "http://www.baidu.com https://12306.com 812219713@qq.com kf5.com呵呵哒18770088339想知道印尼好吃的餐馆都在哪么，看看<a href=\"http://www.qraved.com/18770088339\">Qraved!</a>你就知道啦,this is a good place to eat dinner with friend，也可以去<a href=\"http://www.baidu.com\">百度</a>获取最新相关资讯12345 " +
            "123546879 2136879456 32113874656578964564531286897456 875646 112384789456 468456312789316 321654879645321654897684321";

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
        tvTest = (TextView) view.findViewById(R.id.home_test);
//        tvTest.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                MovementMethod method = tvTest.getMovementMethod();
//                method.onTouchEvent()
//                Toast.makeText(v.getContext(), "长按文本咯", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
//        setText(htmltest);
//        CustomTextView.dealRichList(htmltest);
//        CustomTextView.applyRichText(tvTest, htmltest, new CustomTextView.OnLongClickCallback() {
//            @Override
//            public boolean onLongClick(View view) {
//                Toast.makeText(view.getContext(), "长按文本咯", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
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


    public void setText(String text) {
        if (text == null || text.length() == 0) return;
        Object[][] output = getUrlFromJDP(text);
        int urlCount = 0;
        if (output == null || output.length == 0 || output[0] == null || output[0].length == 0) {
            tvTest.setText(text);
            return;
        }
        urlCount = output[0].length;
        LogUtil.printf("一共有" + urlCount + "个url");
        String remainText = text;
        int lastStart = 0;//截取到一部分后截掉部分的长度
        for (int i = 0; i < urlCount; i++) {
            String blueText = (String) output[0][i];//带下划线的文字
            final String href = (String) output[1][i];//下划线文字所对应的url连接
            int start = (int) output[2][i];//<a>标签在源字符串的起始位置
            int end = (int) output[3][i];//<a>标签在源字符串的结束位置
            SpannableString spannableString = new SpannableString(blueText);
            spannableString.setSpan(new ClickableSpan() {
                //在这里定义点击下划线文字的点击事件，不一定非要打开浏览器
                @Override
                public void onClick(View widget) {
                    //下面是打开系统默认浏览器的方法
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(href);
                    intent.setData(content_url);
                    getContext().startActivity(intent);
                }
            }, 0, blueText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            int subStart = start - lastStart;
            String front = remainText.substring(0, subStart);//截取出一段文字+一段url
            LogUtil.printf("起始位置" + (end - lastStart));
            remainText = remainText.substring(end - lastStart, remainText.length());//剩下的部分
            lastStart = end;
            LogUtil.printf("front是" + front);
            LogUtil.printf("spann是" + spannableString);
            LogUtil.printf("remain是" + remainText);
            if (front.length() > 0) tvTest.append(front);
            tvTest.append(spannableString);
        }
        if (remainText != null && remainText.length() > 0) tvTest.append(remainText);
        tvTest.setMovementMethod(LinkMovementMethod.getInstance());//响应点击事件
    }

    public static Object[][] getUrlFromJDP(String source) {
        ArrayList<String> hosts = new ArrayList<>(4);
        ArrayList<String> urls = new ArrayList<>(4);
        ArrayList<Integer> starts = new ArrayList<>(4);
        ArrayList<Integer> ends = new ArrayList<>(4);
        Pattern pattern = Pattern.compile("<a href=\".*?\">(.*?)</a>");//首先将a标签分离出来
        Matcher matcher = pattern.matcher(source);
        int i = 0;
        while (matcher.find()) {
            String raw = matcher.group(0);
            Pattern url_pattern = Pattern.compile("<a href=\"(.*?)\">");//将href分离出来
            Matcher url_matcher = url_pattern.matcher(raw);
            try {
                if (url_matcher.find()) {
                    String url = url_matcher.group(1);
                    LogUtil.printf("真实url是" + url);//括号里面的
                    urls.add(i, url);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String host = null;//将要显示的文字分离出来
            try {
                host = matcher.group(1);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            LogUtil.printf("蓝色文字是" + host);//括号里面的
            hosts.add(i, host);
            starts.add(i, matcher.start());
            ends.add(i, matcher.end());
            LogUtil.printf("字符串起始下标是" + matcher.start() + "结尾下标是" + matcher.end());//匹配出的字符串在源字符串的位置
            i++;
        }
        if (hosts.size() == 0) {
            LogUtil.printf("没有发现url");
            return null;
        }
        Object[][] outputs = new Object[4][hosts.size()];//第一个下标是内容的分类，第二个下标是url的序号
        outputs[0] = hosts.toArray(new String[hosts.size()]);//下标0是蓝色的文字
        outputs[1] = urls.toArray(new String[urls.size()]);//下标1是url
        outputs[2] = starts.toArray(new Integer[starts.size()]);//下标2是<a>标签起始位置
        outputs[3] = ends.toArray(new Integer[ends.size()]);//下标3是<a>标签结束位置
        return outputs;
    }


}
