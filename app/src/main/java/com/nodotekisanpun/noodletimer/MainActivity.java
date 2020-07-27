package com.nodotekisanpun.noodletimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 180000;   //タイマー設定 単位 ミリ秒   final 変更できない設定値

    private TextView mTextViewCountDown;  //アクセス修飾子
    private SimpleDateFormat dataFormat = new SimpleDateFormat("mm:ss:SS", Locale.US);
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;   // OS内の CountDownTimerクラス

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.buttonreset);

        player = new MediaPlayer();

        String mediaFileUrtStr = "android.resource://" + getPackageName() + "/" + R.raw.nd3pk;

        Uri mediaFileUri = Uri.parse(mediaFileUrtStr);
        try {
            player.setDataSource(MainActivity.this, mediaFileUri);
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                player.start();
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                resetTimer();
                if (player.isPlaying()) {
                    player.pause();
                    player.seekTo(0);
                }
            }
        });

        updateCountDownText();

    }

    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,10) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTextViewCountDown.setText(dataFormat.format(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                mTextViewCountDown.setText(dataFormat.format(0));
                mButtonStartPause.setEnabled(true);
                mButtonStartPause.setText("スタート");
            }
        }.start();

        mButtonStartPause.setText("cooking");
        mButtonStartPause.setEnabled(false);
    }

    private void resetTimer(){
        mCountDownTimer.cancel();
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        mButtonStartPause.setEnabled(true);
    }

    // タイマー表示
    private void updateCountDownText(){
        long minutes = mTimeLeftInMillis/1000/60;
        long seconds = mTimeLeftInMillis/1000%60;
        long mseconds = (mTimeLeftInMillis - seconds*1000 - minutes*1000*60);

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d",minutes,seconds,mseconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

}
