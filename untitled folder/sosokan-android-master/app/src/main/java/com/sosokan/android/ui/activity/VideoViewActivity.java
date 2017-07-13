package com.sosokan.android.ui.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.sosokan.android.R;
import com.sosokan.android.control.FullscreenVideoLayout;

import java.io.IOException;

/**
 * Created by macintosh on 2/15/17.
 */

public class VideoViewActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        View.OnClickListener {

    // Declare variables

    FullscreenVideoLayout videoLayout;

    // Insert your Video URL
    String VideoURL;

    private SurfaceView surfaceViewFrame;
    private static final String TAG = "VideoPlayer";
    private SurfaceHolder holder;

    private ImageView pause;
    private MediaPlayer player;
    boolean hasActiveHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_videoview);
       // VideoURL = "https://firebasestorage.googleapis.com/v0/b/sosokan-1452b.appspot.com/o/videos%2F-KWBPW_pVyWSa48VCdc11478750771.59446?alt=media&token=23f7de11-7232-48b1-837b-05f25e2e838c";
        if (getIntent() != null) {
            VideoURL = getIntent().getStringExtra("videoUrl");
        }

        videoLayout = new FullscreenVideoLayout(this);
        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
        videoLayout.setActivity(this);
       // videoLayout.setFullscreen(true);
        videoLayout.setShouldAutoplay(true);
        videoLayout.hideControls();
        //  videoLayout.stop();

        Uri videoUri = Uri.parse(VideoURL);
        try {
            videoLayout.setVideoURI(videoUri);
            if(!videoLayout.isPlaying())
            {
                videoLayout.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void playVideo() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    player.setDataSource(VideoURL);
                    player.prepare();
                } catch (Exception e) { // I can split the exceptions to get which error i need.

                    Log.i(TAG, "Error");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.stop();

        finish();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        int videoWidth = player.getVideoWidth();
        int videoHeight = player.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;
        android.view.ViewGroup.LayoutParams lp = surfaceViewFrame.getLayoutParams();

        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        surfaceViewFrame.setLayoutParams(lp);

        if (!player.isPlaying()) {
            player.start();
        }
        surfaceViewFrame.setClickable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        synchronized (this) {
            hasActiveHolder = true;
            this.notifyAll();
            player.setDisplay(holder);
            playVideo();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this) {
            hasActiveHolder = false;

            synchronized (this) {
                this.notifyAll();
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
