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
import com.kf5.sdk.im.entity.CardConstant;
import com.kf5.sdk.im.ui.KF5ChatActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.ticket.ui.FeedBackActivity;
import com.kf5.sdk.ticket.ui.LookFeedBackActivity;
import com.kf5sdk.exam.LanguageActivity;
import com.kf5sdk.exam.R;

import org.json.JSONObject;

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
//                boolean isFullScreen = (getActivity().getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN;
//                // 全屏 66816 - 非全屏 65792
//                if (!isFullScreen) {//非全屏
//                    getActivity().getWindow().setFlags(
//                            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    LogUtil.printf("设置全屏");
//                } else {//取消全屏
//                    getActivity().getWindow().clearFlags(
//                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    LogUtil.printf("取消全屏");
//                }
//                getActivity().getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        getActivity().getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                        startActivity(new Intent(getContext(), KF5ChatActivity.class));
//                    }
//                });
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(CardConstant.LINK_URL, "www.baidu.com");
                    jsonObject.put(CardConstant.TITLE, "京东平台卖家销售并发货的商品，由平台卖家提供发票和相应的售后服务。请您放心购买！注：因厂家会在没有任何提前通知的情况下更改产品包装、产地或者一些附件，本司不能确保客户收到的货物与商城图片、产地、附件说明完全一致。只能确保为原厂正货！并且保证与当时市场上同样主流新品一致。若本商城没有及时更新，请大家谅解！");
                    jsonObject.put(CardConstant.IMG_URL, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504695965182&di=5b4e8b755cebd1abf17341215f89b5c6&imgtype=0&src=http%3A%2F%2Fimghr.heiguang.net%2F3%2F2014%2F0707%2F2014070753ba66264d67a2.jpg");
                    jsonObject.put(CardConstant.PRICE, "123456");
                    jsonObject.put(CardConstant.LINK_TITLE, "发送东西");
                    Intent intent = new Intent(getContext(), KF5ChatActivity.class);
//                    intent.putExtra(KF5ChatActivity.CARD_MESSAGE_KEY, jsonObject.toString());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tvSetting:
                startActivity(new Intent(getContext(), LanguageActivity.class));
                break;
            default:

                break;
        }
    }
}
