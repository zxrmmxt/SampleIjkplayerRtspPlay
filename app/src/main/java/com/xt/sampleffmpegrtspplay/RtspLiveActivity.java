package com.xt.sampleffmpegrtspplay;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.xt.ijkplayer.rtsp.RtspPlayer;

public class RtspLiveActivity extends AppCompatActivity {
    private final static String TAG = RtspLiveActivity.class.getSimpleName();
    private String[]       permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private RtspPlayer mRtspPlayer;

    public static void start(Context context) {
        Intent starter = new Intent(context, RtspLiveActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        requestPermission();
        initRtsp();
    }

    private void initRtsp() {
        mRtspPlayer = new RtspPlayer();
        mRtspPlayer.init(RtspLiveActivity.this, new RtspPlayer.BaseLoadingView() {
            @Override
            public void showLoading() {

            }

            @Override
            public void dismissLoading() {

            }
        });
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1234);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRtspPlayer.startPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRtspPlayer.stopPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRtspPlayer.release();
    }
}
