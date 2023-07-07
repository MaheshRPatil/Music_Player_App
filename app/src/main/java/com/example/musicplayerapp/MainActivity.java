package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //Widgets
    TextView appname,songtitle,timer;
    Button play,pause,forward,rewind;
    SeekBar seekBar;

    //Media Player
    MediaPlayer mediaPlayer;

    //Handler
    Handler handler = new Handler();

    //variable
    double startTime=0;
    double finalTime=0;
    int forwardTime=10000;
    int backwardTime=10000;
    static int oneTimeonly=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play=findViewById(R.id.playbutton);
        pause=findViewById(R.id.pause);
        forward=findViewById(R.id.forward);
        rewind=findViewById(R.id.rewind);

        songtitle=findViewById(R.id.songtitle);
        appname=findViewById(R.id.appname);
        timer=findViewById(R.id.timer);

        seekBar=findViewById(R.id.timebar);

        //media Player
        mediaPlayer = MediaPlayer.create(this,R.raw.terevaaste);

        seekBar.setClickable(false);

        //Add functionalities for the button
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayMusic();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp=(int) startTime;
                if ((temp+forwardTime)<=finalTime){
                    startTime=startTime+forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }
                else {
                    Toast.makeText(MainActivity.this, "Can't Jump Forward", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp=(int) startTime;

                if((temp - backwardTime)>0){
                    startTime =startTime-backwardTime;
                    mediaPlayer.seekTo((int)startTime);
                }else{
                    Toast.makeText(MainActivity.this, "Can't go Back", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void PlayMusic() {
        mediaPlayer.start();

        finalTime=mediaPlayer.getDuration();
        startTime=mediaPlayer.getCurrentPosition();

        if (oneTimeonly == 0){
            seekBar.setMax((int)finalTime);
            oneTimeonly=1;
        }
        timer.setText(String.format("%d min,%d sec", TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long)finalTime)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)finalTime))));

        seekBar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime,100);



    }

    //Creating the Runnable
    private Runnable UpdateSongTime=new Runnable() {
        @Override
        public void run() {
            startTime=mediaPlayer.getCurrentPosition();
            timer.setText(String.format("%d min,%d sec",TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long)startTime)
                    -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)startTime))
            ));

            seekBar.setProgress((int) startTime);
            handler.postDelayed(this,100);
        }
    };
}