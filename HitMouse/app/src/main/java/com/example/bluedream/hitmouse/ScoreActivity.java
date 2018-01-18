package com.example.bluedream.hitmouse;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScoreActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.first)
    TextView first;
    @BindView(R.id.second)
    TextView second;
    @BindView(R.id.third)
    TextView third;
    @BindView(R.id.backmenu)
    Button backmenu;
    @BindView(R.id.clearbutton)
    Button clearbutton;
    @BindView(R.id.fl)
    LinearLayout fl;
    @BindView(R.id.imageButton1)
    ImageButton imageButton1;
    @BindView(R.id.imageButton2)
    ImageButton imageButton2;
    @BindView(R.id.imageButton3)
    ImageButton imageButton3;
    private SharedPreferences scoresave;
    private int score1, score2, score3;
    private String name1, name2, name3;
    private double lat1, lat2, lat3, lng1, lng2, lng3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);
        scoresave = getSharedPreferences("scoresave", MODE_PRIVATE);
        getscore();
        pastetext();

    }

    private void getscore() {
        score1 = scoresave.getInt("score1", 999);
        score2 = scoresave.getInt("score2", 999);
        score3 = scoresave.getInt("score3", 999);
        name1 = scoresave.getString("name1", "");
        name2 = scoresave.getString("name2", "");
        name3 = scoresave.getString("name3", "");
        lat1 = Double.valueOf(scoresave.getString("lat1", "0"));
        lat2 = Double.valueOf(scoresave.getString("lat2", "0"));
        lat3 = Double.valueOf(scoresave.getString("lat3", "0"));
        lng1 = Double.valueOf(scoresave.getString("lng1", "0"));
        lng2 = Double.valueOf(scoresave.getString("lng2", "0"));
        lng3 = Double.valueOf(scoresave.getString("lng3", "0"));
    }

    private void pastetext() {
        getscore();
        if (name1.equals(""))
            first.setText("第一名:  無紀錄");
        else
            first.setText("第一名:  " + name1 + "  " + score1 + "分");
        if (name2.equals(""))
            second.setText("第二名:  無紀錄");
        else
            second.setText("第二名:  " + name2 + "  " + score2 + "分");
        if (name3.equals(""))
            third.setText("第三名:  無紀錄");
        else
            third.setText("第三名:  " + name3 + "  " + score3 + "分");
    }

    @OnClick(R.id.backmenu)
    public void onBackmenuClicked() {
        finish();
    }

    @OnClick(R.id.clearbutton)
    public void onClearbuttonClicked() {
        AlertDialog.Builder clearscore = new AlertDialog.Builder(this);
        clearscore.setTitle("刪除紀錄");
        clearscore.setMessage("確定清空紀錄嗎?");
        clearscore.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                scoresave.edit().putString("name1", "").putInt("score1", 0).putString("lat1", "0").putString("lng1", "0").commit();
                scoresave.edit().putString("name2", "").putInt("score2", 0).putString("lat2", "0").putString("lng2", "0").commit();
                scoresave.edit().putString("name3", "").putInt("score3", 0).putString("lat3", "0").putString("lng3", "0").commit();
                pastetext();

            }
        });

        clearscore.setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        clearscore.setCancelable(false);
        clearscore.show();
    }

    @OnClick(R.id.imageButton1)
    public void onImageButton1Clicked() {
        Bundle bundle=new Bundle();
        Intent it = new Intent();
        bundle.putDouble("lat",lat1);
        bundle.putDouble("lng",lng1);
        bundle.putString("mark",name1 + "  " + score1 +"分");
        it.setClass(ScoreActivity.this, MapsActivity.class);
        it.putExtras(bundle);
        startActivity(it);

    }

    @OnClick(R.id.imageButton2)
    public void onImageButton2Clicked() {
        Bundle bundle=new Bundle();
        Intent it = new Intent();
        bundle.putDouble("lat",lat2);
        bundle.putDouble("lng",lng2);
        bundle.putString("mark",name2 + "  " + score2 +"分");
        it.setClass(ScoreActivity.this, MapsActivity.class);
        it.putExtras(bundle);
        startActivity(it);
    }

    @OnClick(R.id.imageButton3)
    public void onImageButton3Clicked() {
        Bundle bundle=new Bundle();
        Intent it = new Intent();
        bundle.putDouble("lat",lat3);
        bundle.putDouble("lng",lng3);
        bundle.putString("mark",name3 + "  " + score3 +"分");
        it.setClass(ScoreActivity.this, MapsActivity.class);
        it.putExtras(bundle);
        startActivity(it);
    }
}
