package com.example.bluedream.hitmouse;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class GameSetActivity extends AppCompatActivity {

    AudioManager audiomanage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_set);
        audiomanage = (AudioManager)GameSetActivity.this.getSystemService(this.AUDIO_SERVICE);
    }
}
