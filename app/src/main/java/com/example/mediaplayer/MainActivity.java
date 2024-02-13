package com.example.mediaplayer;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;
public class MainActivity extends AppCompatActivity {
    private Button btnback, btnfor, btnpause, btnplay;
    private MediaPlayer mediaplayer;
    private TextView txt1, txt2, txt3;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    public static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnback = findViewById(R.id.button);
        btnfor = findViewById(R.id.button4);
        btnpause = findViewById(R.id.button3);
        btnplay = findViewById(R.id.button2);
        txt1 = (TextView) findViewById(R.id.textView4);
        txt2 = (TextView) findViewById(R.id.textView3);
        txt3 = (TextView) findViewById(R.id.textView2);
        txt3.setText("Song.mp3");
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        btnpause.setEnabled(false);
        btnplay.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                if (mediaplayer == null) {
                    mediaplayer = MediaPlayer.create(getBaseContext(),
                            R.raw.song);
                    mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.stop();
                        }
                    });
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mediaplayer.setPlaybackParams(mediaplayer.getPlaybackParams().setSpeed(0.8f));
                    }
                    finalTime = mediaplayer.getDuration();
                    startTime = mediaplayer.getCurrentPosition();
                    if (oneTimeOnly == 0) {
                        seekbar.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }
                }
                mediaplayer.start();
                txt2.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime)
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        finalTime)))
                );
                txt1.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                                -

                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        startTime)))
                );
                seekbar.setProgress((int) startTime);
                myHandler.postDelayed(UpdateSongTime, 100);
                btnpause.setEnabled(true);
                btnplay.setEnabled(false);
            }
        });
        btnpause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
                mediaplayer.pause();
                btnpause.setEnabled(false);
                btnplay.setEnabled(true);
            }
        });
        btnfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaplayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(), "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if ((temp - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaplayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(), "You have Jumped backward 5 seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaplayer.getCurrentPosition();
            txt1.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -

                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };
}