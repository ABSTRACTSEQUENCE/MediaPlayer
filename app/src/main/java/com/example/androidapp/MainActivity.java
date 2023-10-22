package com.example.androidapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.BlendMode;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.FileObserver;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileReader;
import java.security.cert.Extension;
import java.util.logging.FileHandler;
import android.os.Handler;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;
public class MainActivity extends AppCompatActivity {
    MediaPlayer mp;

    //AudioManager mng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Init//////////
        SeekBar trackLength = findViewById(R.id.sb_track_progress);
        ImageButton btPlay = findViewById(R.id.bt_play_pause);
        ImageButton btNext = findViewById(R.id.bt_next);
        ImageButton btPrev = findViewById(R.id.bt_prev);
        ImageButton btSettings = findViewById(R.id.bt_settings);
        TextView trackName = findViewById(R.id.tv_text_name);
        TextView timeStamp = findViewById(R.id.timeStamp);
        TextView authorName = findViewById(R.id.tv_author_name);
        TextView sleepTimerStatus = findViewById(R.id.tv_sleepTimer);
        TextView sleepTimerCounter = findViewById(R.id.tv_sleepTimer_counter);
        ////////////////
        try{
            Track[] tracks = new Track[] {
                    new Track(R.raw.track, "snowfall", "Øneheart"),
                    new Track(R.raw.track2,"Hypnostasis","Marble Pawns"),
                    new Track(R.raw.track3,"Forbidden Roses", "Iruka")
            };

            mp = MediaPlayer.create(this, tracks[0].resId);
                sleepTimerStatus.setVisibility(View.VISIBLE); sleepTimerCounter.setVisibility(View.VISIBLE);
                Bundle extras = getIntent().getExtras();
            if(extras != null) {
                PlayerSettings settings = new PlayerSettings(
                        extras.getInt("increment"),
                        extras.getInt("cooldown"));
                AudioManager mng = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                Handler handler = new Handler();
                sleepTimerCounter.setText(Integer.toString(settings.cooldown));
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        timerStatus(true);
                        int counter = Integer.parseInt(sleepTimerCounter.getText().toString());
                        if(counter<= 0){
                            for(int i = 0; i<= settings.increment;i++){
                                mng.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                            }
                            counter = settings.cooldown;
                        }
                        else counter--;
                        sleepTimerCounter.setText(Integer.toString(counter));
                        if(mng.getStreamVolume(AudioManager.STREAM_MUSIC) <= 0) {pause(); timerStatus(false); return;}
                        handler.postDelayed(this,1000);
                    }
                };
                handler.post(r);
            }
            else {sleepTimerStatus.setVisibility(View.INVISIBLE); sleepTimerCounter.setVisibility(View.INVISIBLE);}

            mp.setOnCompletionListener(MediaPlayer::stop);
            mp.seekTo(0);
            tracks[0].isPlaying = true;
            trackName.setText(tracks[0].name);
            authorName.setText(tracks[0].author);

            //Button listeners///////////////
            btPlay.setOnClickListener((v)-> { if(mp.isPlaying()) pause(); else play(); });
            btNext.setOnClickListener((v)-> mp = changeTrack(tracks, mp, true));
            btPrev.setOnClickListener((v)-> mp = changeTrack(tracks,mp,false));
            btSettings.setOnClickListener((v)->{
                mp.stop();
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
            });
            /////////////////////////////////

            //mp.start();
            trackLength.setMin(0);
            trackLength.setMax(mp.getDuration());
            trackLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if(b) mp.seekTo(i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            Handler handler = new Handler();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    if(mp.isPlaying()) {
                        int min = 0, sec = trackLength.getProgress() / 1000;
                        while (sec >= 60) {
                            min++;
                            sec -= 60;
                        }
                        timeStamp.setText(min + ":" + sec);
                        if (mp.isPlaying()) {
                            if (trackLength.getProgress() == mp.getDuration()) {
                                pause();
                                return;
                            }
                            trackLength.setProgress(trackLength.getProgress() + 1000);
                        }
                    }
                    handler.postDelayed(this, 1000);
                }
            };
            handler.postDelayed(r, 1000);
        }
        catch(Exception ex){ Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();}
    }

    public MediaPlayer changeTrack(@NotNull Track[] tracks, MediaPlayer mp, boolean next){
        Track newTrack = null;
        for(int i = 0; i < tracks.length; i++){
            if(tracks[i].isPlaying)
            {
                tracks[i].isPlaying = false;
                if(next){
                    try {
                        newTrack = tracks[i+1]; tracks[i].isPlaying = false; break;
                    }
                    catch (ArrayIndexOutOfBoundsException ex){
                        if(tracks[i] == tracks[tracks.length-1]) { newTrack = tracks[0]; break;}
                    }
                }
                else if(i!=0) { newTrack = tracks[i-1]; break;}
                else {newTrack = tracks[tracks.length-1]; break;}
            }
        }

            try{
                newTrack.isPlaying = true;
                ((TextView)findViewById(R.id.tv_author_name)).setText(newTrack.author);
                ((TextView)findViewById(R.id.tv_text_name)).setText(newTrack.name);
                ((SeekBar)findViewById(R.id.sb_track_progress)).setProgress(0);
            }
            catch (NullPointerException ex){ Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();}

        MediaPlayer newMedia = MediaPlayer.create(this, newTrack.resId);
            pause();
        mp.release();
        newMedia.seekTo(0);
        return newMedia;
    }
    public void pause() {
        ImageButton bt_playPause = findViewById(R.id.bt_play_pause);
        if (!mp.isPlaying()) return;
        mp.pause();
        bt_playPause.setImageResource(R.drawable.play);
    }

    public void play(){
        ImageButton bt_playPause = findViewById(R.id.bt_play_pause);
        if(mp.isPlaying()) return;
        mp.start(); bt_playPause.setImageResource(R.drawable.pause);
    }
    public void timerStatus(boolean on){
        TextView sleepTimerStatus = findViewById(R.id.tv_sleepTimer);
        TextView sleepTimerCounter = findViewById(R.id.tv_sleepTimer_counter);
        if(on) {sleepTimerStatus.setVisibility(View.VISIBLE); sleepTimerCounter.setVisibility(View.VISIBLE);}
        else {sleepTimerStatus.setVisibility(View.INVISIBLE); sleepTimerCounter.setVisibility(View.INVISIBLE);}
    }
}