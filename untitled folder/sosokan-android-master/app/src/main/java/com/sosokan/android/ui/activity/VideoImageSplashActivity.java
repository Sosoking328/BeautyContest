package com.sosokan.android.ui.activity;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.crashlytics.android.Crashlytics;
import com.sosokan.android.BaseApp;
import com.sosokan.android.R;
import com.sosokan.android.control.FullscreenVideoLayout;
import com.sosokan.android.models.VideoSplash;
import com.sosokan.android.mvp.splash.SplashPresenter;
import com.sosokan.android.mvp.splash.SplashView;
import com.sosokan.android.networking.Service;
import com.sosokan.android.rest.UrlEndpoint;
import com.sosokan.android.utils.LocaleHelper;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import wseemann.media.FFmpegMediaPlayer;

/**
 * Created by macintosh on 3/18/17.
 */

public class VideoImageSplashActivity extends BaseApp implements SplashView,SurfaceHolder.Callback{
    private static final String TAG = "VideoImageSplash";

    UrlEndpoint urlEndpoint;
    boolean isChineseApp;
    ImageView imgSplash;
    FullscreenVideoLayout videoLayout;
    WebView webViewVideoSplash;
    VideoView videoview1;

    String path;
    private MediaPlayer mMediaPlayer;
    //private FFmpegMediaPlayer mMediaPlayer;
    private SurfaceView mmSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Surface mFinalSurface;
    boolean pausing = false;
    public static String videoUrl;

    
    MediaController mMediaController;
    @Inject
    public Service service;
    SplashPresenter splashPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getDeps().injectVideoSplash(this);

        setContentView(R.layout.activity_video_splash);
        Fabric.with(this, new Crashlytics());

        initValue();

    }



    private void initValue() {
        try {
            if (getApplicationContext() != null) {
                String languageToLoad = LocaleHelper.getLanguage(getApplicationContext());
                isChineseApp = languageToLoad.toString().equals("zh");

            }
        } catch (Exception ex) {
            System.out.println("languageToLoad failed: " + ex.getMessage());
        }
        urlEndpoint = new UrlEndpoint();

        splashPresenter = new SplashPresenter(service, this, this);
        splashPresenter.getVideoSplash();
    }


    @Override
    public void onFailure(String appErrorMessage) {

    }

    @Override
    public void getListVideoSplashSuccess(List<VideoSplash> videoSplashes) {
        String videoUrl = null;
        String imageUrl = null;
        if (videoSplashes != null && videoSplashes.size() > 0) {
            for (int i = 0; i < videoSplashes.size(); i++) {
                VideoSplash videoSplash = videoSplashes.get(i);
                if (videoSplash != null && videoSplash.getLanguage().equals(urlEndpoint.getLanguage(isChineseApp))) {
                    String mime = getMimeType(Uri.parse(videoSplash.getVideo()));
                    Log.e(TAG, mime);
                    if (mime.contains("video")) {
                        videoUrl = videoSplash.getVideo();
                    } else {
                        imageUrl = videoSplash.getVideo();
                    }
                }
            }
        }
        if (videoUrl != null && !videoUrl.isEmpty()) {
            // String url = "https://firebasestorage.googleapis.com/v0/b/sosokan-1452b.appspot.com/o/videos%2F-KWBPW_pVyWSa48VCdc11478750771.59446?alt=media&token=23f7de11-7232-48b1-837b-05f25e2e838c";
            String url = "https://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
            //String url ="https://sosokan-1452b.appspot.com.storage.googleapis.com/videos/rassegna2.avi";
            showVideo(url);
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            updateImageCover(imageUrl);
        }
    }


    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getApplicationContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public void showVideo(String videoUrl) {
        Log.e("showVideo URL ", videoUrl);

        videoLayout = new FullscreenVideoLayout(this);
        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
        videoLayout.setActivity(this);
        // videoLayout.setFullscreen(true);
        videoLayout.setShouldAutoplay(true);
        videoLayout.hideControls();
        //  videoLayout.stop();

        Uri videoUri = Uri.parse(videoUrl);
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


    public void updateImageCover(String url) {
        Log.e("UpdateImageCover", url);
        Glide.with(imgSplash.getContext())
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        int imageWidth = bitmap.getWidth();
                        int imageHeight = bitmap.getHeight();
                        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        int newWidth = windowManager.getDefaultDisplay().getWidth(); //this method should return the width of device screen.
                        float scaleFactor = (float) newWidth / (float) imageWidth;
                        int newHeight = (int) ((imageHeight * scaleFactor));

                        bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

                        imgSplash.setImageBitmap(bitmap);
                        //  callSplash(10000);
                    }
                });
    }


    public void callSplash(int waitNumber) {
        try {
            Thread.sleep(waitNumber);
        } catch (InterruptedException e) {
        }
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDisplay(mSurfaceHolder);
        mMediaPlayer.setDisplay(holder);
        if (videoUrl != null && !videoUrl.isEmpty()) {

            try {
                mMediaPlayer.setDataSource(videoUrl);
                mMediaPlayer.prepare();
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        videoview1.start();
                    }
                });
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /*@Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }



}

