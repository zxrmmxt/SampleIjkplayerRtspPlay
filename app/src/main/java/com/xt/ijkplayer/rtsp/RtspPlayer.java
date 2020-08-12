package com.xt.ijkplayer.rtsp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.xt.ijkplayer.rtsp.listener.IjkPlayerListener;
import com.xt.ijkplayer.rtsp.widget.IjkVideoView;
import com.xt.sampleffmpegrtspplay.R;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author xt on 2020/7/21 13:21
 * 使用ijkplayer库，功能单一，只有rtsp播放功能，对播放延迟做了优化
 */
public class RtspPlayer {
    private final static String          url = "rtsp://192.168.1.254/xxxx.mp4";
    private static final String          TAG = RtspPlayer.class.getSimpleName();
    private              IjkMediaPlayer  mIjkMediaPlayer;
    private              IjkVideoView    mVideoView;
    private              BaseLoadingView mLoadingView;

    public void init(Activity activity, BaseLoadingView loadingView) {
        mLoadingView = loadingView;
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mVideoView = activity.findViewById(R.id.video_view);
        mVideoView.setIjkPlayerListener(new IjkPlayerListener() {
            @Override
            public void onIjkPlayer(IjkMediaPlayer ijkMediaPlayer) {
                mIjkMediaPlayer = ijkMediaPlayer;
            }
        });
        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                mVideoView.resume();
            }
        });
        mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                if (i == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    mLoadingView.dismissLoading();
                }
                return true;
            }
        });
    }

    public void start() {
        mLoadingView.showLoading();
        mVideoView.setVideoPath(url);
        mVideoView.start();
    }

    public void release() {
        mVideoView.stopPlayback();
        mVideoView.release(true);
        IjkMediaPlayer.native_profileEnd();
    }

    public IjkVideoView getVideoView() {
        return mVideoView;
    }

    public IjkMediaPlayer getIjkMediaPlayer() {
        return mIjkMediaPlayer;
    }

    public void updateSize() {
        Context        context       = mVideoView.getContext();
        WindowManager  windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm            = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);

        int           screenWidth  = dm.widthPixels;
        int           screenHeight = dm.heightPixels;
        final boolean isPortrait   = mVideoView.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;


        double displayWidth;
        double displayHeight;
        if (isPortrait) {
            displayWidth = screenWidth;
            displayHeight = displayWidth * (9 / 16f);
        } else {
            displayWidth = screenWidth;
            displayHeight = screenHeight;

        }

        ViewGroup.LayoutParams layoutParams = mVideoView.getLayoutParams();
        layoutParams.width = (int) displayWidth;
        layoutParams.height = (int) displayHeight;
        mVideoView.setLayoutParams(layoutParams);
    }

    public abstract static class BaseLoadingView {
        public abstract void showLoading();

        public abstract void dismissLoading();
    }
}
