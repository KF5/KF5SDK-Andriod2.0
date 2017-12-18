package com.kf5sdk.exam;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tvChinese, tvEnglish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        tvChinese = (TextView) findViewById(R.id.tvChinese);
        tvChinese.setOnClickListener(this);
        tvEnglish = (TextView) findViewById(R.id.tvEnglish);
        tvEnglish.setOnClickListener(this);
        findViewById(R.id.back_img).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvChinese: {
                Locale myLocale = new Locale("zh");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
                finish();
            }
            break;
            case R.id.tvEnglish: {
                Locale myLocale = new Locale("en");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
                finish();
            }
            break;
            case R.id.back_img:
                finish();
                break;
        }
    }
}
