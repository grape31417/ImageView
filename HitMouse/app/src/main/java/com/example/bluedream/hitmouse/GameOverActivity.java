package com.example.bluedream.hitmouse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.bluedream.hitmouse.MainActivity.lat;
import static com.example.bluedream.hitmouse.MainActivity.lng;

public class GameOverActivity extends AppCompatActivity {

    @BindView(R.id.HighScoreText)
    TextView HighScoreText;
    @BindView(R.id.nametext)
    TextView nametext;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.OKbutton)
    Button OKbutton;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.fl)
    FrameLayout fl;
    private SharedPreferences scoresave;
    private int level = 0;
    private int score,score1,score2,score3;
    private String name1,name2,name3;
    private boolean High = false;
    private Bundle bundle = new Bundle();
    private String lat1,lat2,lat3,lng1,lng2,lng3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        ButterKnife.bind(this);
        judgescore();
        getscore();

    }

    private void getscore()
    {
        score1=scoresave.getInt("score1",999);
        score2=scoresave.getInt("score2",999);
        score3=scoresave.getInt("score3",999);
        name1=scoresave.getString("name1","");
        name2=scoresave.getString("name2","");
        name3=scoresave.getString("name3","");
        lat1=scoresave.getString("lat1","0");
        lat2=scoresave.getString("lat2","0");
        lat3=scoresave.getString("lat3","0");
        lng1=scoresave.getString("lng1","0");
        lng2=scoresave.getString("lng2","0");
        lng3=scoresave.getString("lng3","0");
    }


    private void judgescore() {
        scoresave = getSharedPreferences("scoresave", MODE_PRIVATE);
        Intent it = getIntent();
        bundle = it.getExtras();
        score = bundle.getInt("score");
        if (score > scoresave.getInt("score1", 0)) {
            textView.setText("你獲得"+score+"分");
            HighScoreText.setText("打破紀錄成為第一名");
            HighScoreText.setVisibility(View.VISIBLE);
            nametext.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
            level=1;
            High=true;

        } else if (score > scoresave.getInt("score2", 0)) { textView.setText("你獲得"+score+"分");
            HighScoreText.setText("打破紀錄成為第二名");
            HighScoreText.setVisibility(View.VISIBLE);
            nametext.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
            level=2;
            High=true;

        } else if (score > scoresave.getInt("score3", 0)) {
            textView.setText("你獲得"+score+"分");
            HighScoreText.setText("打破紀錄成為第三名");
            HighScoreText.setVisibility(View.VISIBLE);
            nametext.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
            level=3;
            High=true;
        }
        else
        {
            textView.setText("你獲得"+score+"分");
            HighScoreText.setText("繼續努力");
            HighScoreText.setVisibility(View.VISIBLE);
            level=0;
            High=false;

        }
    }


    @OnClick(R.id.OKbutton)
    public void onViewClicked() {
        String name;
        if(editText.getText().toString().equals(""))
            name="無名氏";
        else
        name=editText.getText().toString();
        switch (level){
            case 1:
                scoresave.edit()
                        .putInt("score1",score)
                        .putInt("score2",score1)
                        .putInt("score3",score2)
                        .putString("name1",name)
                        .putString("name2",name1)
                        .putString("name3",name2)
                        .putString("lat1",String.valueOf(lat))
                        .putString("lat2",lat1)
                        .putString("lat3",lat2)
                        .putString("lng1",String.valueOf(lng))
                        .putString("lng2",lng1)
                        .putString("lng3",lng2)
                        .commit();

                break;
            case 2:
                scoresave.edit()
                        .putInt("score2",score)
                        .putInt("score3",score2)
                        .putString("name2",name)
                        .putString("name3",name2)
                        .putString("lat2",String.valueOf(lat))
                        .putString("lat3",lat2)
                        .putString("lng2",String.valueOf(lng))
                        .putString("lng3",lng2)
                        .commit();
                break;
            case 3:
                scoresave.edit()
                        .putInt("score3",score)
                        .putString("name3",name)
                        .putString("lat3",String.valueOf(lat))
                        .putString("lng3",String.valueOf(lng))
                        .commit();
                break;
            case 0:
                break;
        }

        Intent it =new Intent();
        it.setClass(GameOverActivity.this,ScoreActivity.class);
        startActivity(it);
        finish();



    }
}
