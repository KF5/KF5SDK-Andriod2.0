package com.kf5.sdk.helpcenter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kf5.sdk.R;
import com.kf5.sdk.system.utils.ClickUtils;
import com.kf5.sdk.system.utils.DefaultTextWatcher;

/**
 * @author Chosen
 * @create 2019/4/3 10:34
 * @email 812219713@qq.com
 */
public class SearchActivity extends FragmentActivity implements View.OnClickListener {

    public static final String SEARCH_KEY = "search_key";
    private EditText etContent;
    private ImageView imgDelete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kf5_activity_search);
        findViewById(R.id.kf5_return_img).setOnClickListener(this);
        imgDelete = findViewById(R.id.kf5_img_delete_content);
        imgDelete.setOnClickListener(this);
        etContent = findViewById(R.id.kf5_search_content_edittext);
        etContent.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s) || TextUtils.isEmpty(s.toString().trim())) {
                    if (imgDelete.getVisibility() != View.INVISIBLE) {
                        if (imgDelete.getAnimation() != null) {
                            imgDelete.getAnimation().cancel();
                        }
                        ScaleAnimation animation = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        animation.setDuration(100);
                        animation.setFillAfter(false);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                imgDelete.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        imgDelete.startAnimation(animation);
                    }
                } else {
                    if (imgDelete.getVisibility() != View.VISIBLE) {
                        if (imgDelete.getAnimation() != null) {
                            imgDelete.getAnimation().cancel();
                        }
                        imgDelete.setVisibility(View.VISIBLE);
                        ScaleAnimation animation = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        animation.setDuration(100);
                        animation.setFillAfter(false);
                        imgDelete.startAnimation(animation);
                    }
                }
            }
        });
        etContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    String key = etContent.getText().toString();
                    if (!TextUtils.isEmpty(key.trim())) {
                        setResult(RESULT_OK, new Intent().putExtra(SEARCH_KEY, key));
                        finish();
                    } else {
                        Toast.makeText(SearchActivity.this, R.string.kf5_content_not_null, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (ClickUtils.isInvalidClick(v)) {
            return;
        }
        int id = v.getId();
        if (id == R.id.kf5_return_img) {
            finish();
        } else if (id == R.id.kf5_img_delete_content) {
            if (!TextUtils.isEmpty(etContent.getText())) {
                etContent.setText("");
            }
        }
    }
}
