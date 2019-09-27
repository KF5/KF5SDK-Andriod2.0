package com.chosen.videoplayer.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.chosen.videoplayer.Jzvd;
import com.chosen.videoplayer.JzvdStd;
import com.kf5.sdk.R;

public class VideoPlayActivity extends AppCompatActivity {

    private static final String TEST_VIDEO_URL = "http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4";

    public static final String URL = "video_url";
    public static final String TITLE = "video_title";


    public static void openVideoPlayActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra(URL, url);
        intent.putExtra(TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kf5_videoplay_activity_video_play);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(JzvdStd.CLICK_BACK_EVENT);
        registerReceiver(broadcastReceiver, intentFilter);
        String title = getIntent().getStringExtra(TITLE);
        String url = getIntent().getStringExtra(URL);
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this, R.string.kf5_videoplay_no_url, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Jzvd.startFullscreen(this, JzvdStd.class, url, !TextUtils.isEmpty(title) ? title : "视频");
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            finish();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        Jzvd.releaseAllVideos();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
//            JZMediaManager.pause();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(JzvdStd.CLICK_BACK_EVENT, intent.getAction())) {
                finish();
            }
        }
    };
}
