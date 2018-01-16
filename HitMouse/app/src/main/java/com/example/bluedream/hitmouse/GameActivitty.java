package com.example.bluedream.hitmouse;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipecsl.gifimageview.library.GifImageView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivitty extends AppCompatActivity {

    @BindView(R.id.mouse1)
    GifImageView mouse1;
    @BindView(R.id.mouse2)
    GifImageView mouse2;
    @BindView(R.id.mouse3)
    GifImageView mouse3;
    @BindView(R.id.mouse4)
    GifImageView mouse4;
    @BindView(R.id.mouse5)
    GifImageView mouse5;
    @BindView(R.id.mouse6)
    GifImageView mouse6;
    @BindView(R.id.mouse7)
    GifImageView mouse7;
    @BindView(R.id.mouse8)
    GifImageView mouse8;
    @BindView(R.id.mouse9)
    GifImageView mouse9;
    @BindView(R.id.mouse10)
    GifImageView mouse10;
    @BindView(R.id.mouse11)
    GifImageView mouse11;
    @BindView(R.id.mouse12)
    GifImageView mouse12;

    public static int THREAD_SLEEP_TIME = 1000;
    public static  int THREAD_Hammer_TIME = 200;
    public static final int TIME = 30;
    @BindView(R.id.Time)
    TextView Time;
    @BindView(R.id.txtScore)
    TextView txtScore;
    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.imghammer)
    ImageView imghammer;
    private int next = 99;
    private Random random = new Random();
    private ArrayList<GifImageView> mousearr = new ArrayList<>();

    private int level =1;
    private int time;
    private int totalTime = TIME;
    private int hammerpic = 1;
    private Thread t = null;
    private Thread h = null;
    private int score = 0;
    private int old;
    boolean firstClick = true;
    private int speed = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_activitty);
        ButterKnife.bind(this);
        initOnClick();
        GameStartCheck();


    }

    private void GameStartCheck() {
        AlertDialog.Builder gameStartCheck = new AlertDialog.Builder(GameActivitty.this);
        gameStartCheck.setTitle("遊戲開始");
        gameStartCheck.setMessage("要開始遊戲了嗎?");
        gameStartCheck.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ThreadTime1();
            }
        });

        gameStartCheck.setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                GameActivitty.this.finish();
            }
        });
        gameStartCheck.show();
    }

    private void GameContinue()
    {
        AlertDialog.Builder gameContinue = new AlertDialog.Builder(GameActivitty.this);
        gameContinue.setTitle("恭喜");
        gameContinue.setMessage("接續到下一關嗎?");
        gameContinue.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(level==2)
                go2ThreadTime();
            }
        });

        gameContinue.setNegativeButton("結束遊戲", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent it =new Intent();
                it.setClass(GameActivitty.this,GameOverActivity.class);
                it.putExtra("score",score);
                startActivity(it);
            }
        });
        gameContinue.show();
    }


    private void go2ThreadTime() {
        THREAD_SLEEP_TIME=750;
        speed=4;

    }


    private void initOnClick() {

        mousearr.add(mouse1);
        mousearr.add(mouse2);
        mousearr.add(mouse3);
        mousearr.add(mouse4);
        mousearr.add(mouse5);
        mousearr.add(mouse6);
        mousearr.add(mouse7);
        mousearr.add(mouse8);
        mousearr.add(mouse9);
        mousearr.add(mouse10);
        mousearr.add(mouse11);
        mousearr.add(mouse12);

        mouse1.setOnTouchListener(Hit);
        mouse2.setOnTouchListener(Hit);
        mouse3.setOnTouchListener(Hit);
        mouse4.setOnTouchListener(Hit);
        mouse5.setOnTouchListener(Hit);
        mouse6.setOnTouchListener(Hit);
        mouse7.setOnTouchListener(Hit);
        mouse8.setOnTouchListener(Hit);
        mouse9.setOnTouchListener(Hit);
        mouse10.setOnTouchListener(Hit);
        mouse11.setOnTouchListener(Hit);
        mouse12.setOnTouchListener(Hit);
    }


    private void ThreadTime1() {
        if (t == null) {
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (totalTime != 0) {
                            Thread.sleep(THREAD_SLEEP_TIME);
                            if (next != 99)
                                old = next;

                            if (time % speed == 0)
                                totalTime--;
                            next = random.nextInt(12);
                            firstClick = true;

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mousearr.get(old).setVisibility(View.INVISIBLE);
                                    mousearr.get(next).setVisibility(View.VISIBLE);
                                    String s = Integer.toString(totalTime);
                                    Time.setText(s);
                                }
                            });
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (totalTime == 0) {
                        finish();
                    }
                }
            });
        }
        t.start();
    }




    GifImageView.OnTouchListener Hit = new GifImageView.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (firstClick == true) {
                VibratorUtil.Vibrate(GameActivitty.this, 200);
                score = score + 10;
                String s = Integer.toString(score);
                txtScore.setText(s);
                HammerTime(view);
                firstClick = false;
            }
            return false;
        }
    };

    private void HammerTime(final View view) {
        hammerpic = 1;
        int x = view.getLeft();
        int y = view.getTop();
        imghammer.setX(x + 20);
        imghammer.setY(y - 20);
        imghammer.setImageResource(R.drawable.hammer1);
        imghammer.setVisibility(View.VISIBLE);

        if (h == null) {
            h = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (hammerpic == 1) {
                            Thread.sleep(THREAD_Hammer_TIME);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hammerpic = 2;
                                    imghammer.setImageResource(R.drawable.hammer2);
                                }
                            });
                            //Thread.sleep(THREAD_Hammer_TIME);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (hammerpic == 2) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imghammer.setVisibility(View.INVISIBLE);
                                view.setVisibility(View.INVISIBLE);

                            }
                        });
                        h.interrupt();
                        h = null;

                    }

                }
            });

        }
        h.start();
    }


}

