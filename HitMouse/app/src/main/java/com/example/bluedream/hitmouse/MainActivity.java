package com.example.bluedream.hitmouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.GameStart)
    Button GameStart;
    @BindView(R.id.HighScore)
    Button HighScore;
    @BindView(R.id.EndGame)
    Button EndGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.GameStart)
    public void onGameStartClicked() {
        Intent it =new Intent();
        it.setClass(MainActivity.this,GameActivitty.class);
        startActivity(it);
    }

    @OnClick(R.id.HighScore)
    public void onHighScoreClicked() {
    }

    @OnClick(R.id.EndGame)
    public void onEndGameClicked() {
        finish();;
    }
}
