package in.rajpusht.pc.ui.animation;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.TransitionManager;

import in.rajpusht.pc.R;


/**
 * Created by User on 07-Mar-18.
 */

public class ActivityVideoPlay extends AppCompatActivity {

    private VideoView video_views;
    private ProgressBar progressBar = null;
    private MediaController mc;

    void releaseVideoView() {
        try {

            video_views.stopPlayback();
            mc.setAnchorView(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        video_views = findViewById(R.id.videoview);
        progressBar = findViewById(R.id.progressbar);
        video_views.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                mp.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int arg1,
                                                   int arg2) {
                        progressBar.setVisibility(View.GONE);
                        mp.start();
                    }
                });
            }
        });

        mc = new MediaController(this);
        mc.setAnchorView(video_views);
        mc.setMediaPlayer(video_views);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.film);
        video_views.setMediaController(mc);
        video_views.setVideoURI(uri);
        video_views.start();
        video_views.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        video_views.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        releaseVideoView();
        super.onDestroy();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

//        String s="";
//        if(newConfig.orientation== Configuration.ORIENTATION_LANDSCAPE){
//            s="Landscape Ori...";
//        }else if (newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
//
//            s="Landscape Ori...";
//        }
//        Toast.makeText(ActivityVideoPlay.this,s,Toast.LENGTH_LONG).show();
    }
}
