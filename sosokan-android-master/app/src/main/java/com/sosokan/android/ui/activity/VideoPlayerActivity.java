package com.sosokan.android.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.sosokan.android.R;

import java.io.IOException;

import wseemann.media.FFmpegMediaPlayer;

/**
 * Created by macintosh on 3/19/17.
 */

public class VideoPlayerActivity extends FragmentActivity {

    private SurfaceView mmSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Surface mFinalSurface;

    private FFmpegMediaPlayer mMediaPlayer;
    String videoUrl;
    public void onCreate(Bundle icicle) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(icicle);
        setContentView(R.layout.activity_video_player);


        if (getIntent() != null) {
            videoUrl = getIntent().getStringExtra("videoUrl");
        }
        // set up the Surface video sink
        mmSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        mSurfaceHolder = mmSurfaceView.getHolder();

        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.v("TAG", "surfaceChanged format=" + format + ", width=" + width + ", height="
                        + height);
            }

            public void surfaceCreated(SurfaceHolder holder) {
                mFinalSurface = holder.getSurface();

                //mMediaPlayer.setSurface(mFinalSurface);
                try {
                    mMediaPlayer.reset();

                    mMediaPlayer.setDataSource(videoUrl);
                    if (mFinalSurface != null) {
                        mMediaPlayer.setSurface(mFinalSurface);
                    }
                    mMediaPlayer.prepareAsync();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.v("TAG", "surfaceDestroyed");
            }

        });

        mMediaPlayer = new FFmpegMediaPlayer();
        mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mMediaPlayer.setOnErrorListener(mOnErrorListener);
        //mMediaPlayer.stop();
        try {
            mMediaPlayer.setDataSource(videoUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mFinalSurface != null) {
            mMediaPlayer.setSurface(mFinalSurface);
        }
        mMediaPlayer.prepareAsync();
    }

    private FFmpegMediaPlayer.OnPreparedListener mOnPreparedListener = new FFmpegMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(FFmpegMediaPlayer mp) {
            mp.start();
        }
    };

    private FFmpegMediaPlayer.OnErrorListener mOnErrorListener = new FFmpegMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(FFmpegMediaPlayer mp, int what, int extra) {
            Log.d(VideoPlayerActivity.class.getName(), "Error: " + what);
            return true;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }
}
