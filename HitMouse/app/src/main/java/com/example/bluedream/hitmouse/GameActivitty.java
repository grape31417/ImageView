package com.example.bluedream.hitmouse;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
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
    public static int THREAD_Hammer_TIME = 200;
    public int TIME = 30;
    @BindView(R.id.Time)
    TextView Time;
    @BindView(R.id.txtScore)
    TextView txtScore;
    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.imghammer)
    ImageView imghammer;
    private int next = 99;
    private int next2 = 99;
    private Random random = new Random();
    private ArrayList<GifImageView> mousearr = new ArrayList<>();

    private int level = 1;
    private int time;
    private int totalTime = TIME;
    private int hammerpic = 1;
    private Thread t = null;
    private Thread t2 = null;
    private Thread h = null;
    private int score = 0;
    private int old;
    private int old2;
    private int speed = 1;
    MediaPlayer mp;
    private SoundPool soundPool;
    private int alertId;
    boolean pause = false;
    boolean mppause = false;
    boolean backkeydown = false;
    private SharedPreferences scoresave, firstplay;
    private int pausetime;
    private int sound, vib;
    AlertDialog.Builder ad, cc;

    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        int windows = bundle.getInt("windowset");
        if (windows == 1) {
            setContentView(R.layout.activity_game_activitty);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if (windows == 2) {

            setContentView(R.layout.game_activity2);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }


        ButterKnife.bind(this);
        mp = MediaPlayer.create(GameActivitty.this, R.raw.battle);
        mp.setLooping(true);
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
        alertId = soundPool.load(this, R.raw.hit, 1);
        scoresave = getSharedPreferences("scoresave", MODE_PRIVATE);
        firstplay = getSharedPreferences("first_play", MODE_PRIVATE);
        initOnClick();
        gameset();
        GameStartCheck();


    }

    private void GameStartCheck() {
        AlertDialog.Builder gameStartCheck = new AlertDialog.Builder(GameActivitty.this);
        gameStartCheck.setTitle("遊戲開始");
        gameStartCheck.setMessage("要開始遊戲了嗎?");
        gameStartCheck.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (scoresave.getInt("timeadd", 0) > 0)
                    checkItem();
                else {
                    Time.setVisibility(View.VISIBLE);
                    ThreadTime1();
                    mp.start();
                }
            }
        });

        gameStartCheck.setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                GameActivitty.this.finish();
            }
        });
        gameStartCheck.setCancelable(false);
        if (!isFinishing()) {
            gameStartCheck.show();
        }

    }

    private void GameContinue() {
        AlertDialog.Builder gameContinue = new AlertDialog.Builder(GameActivitty.this);
        gameContinue.setTitle("恭喜");
        gameContinue.setMessage("接續到下一關嗎? ");
        gameContinue.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (level == 2)
                    go2ThreadTime();
                if (level == 3)
                    go3ThreadTime();
            }
        });

        gameContinue.setNegativeButton("結束遊戲", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Bundle bundle = new Bundle();
                bundle.putInt("score", score);
                Intent it = new Intent();
                it.setClass(GameActivitty.this, GameOverActivity.class);
                it.putExtras(bundle);
                startActivity(it);
                mp.stop();
                finish();
            }
        });
        gameContinue.setCancelable(false);

        if (!isFinishing()) {
            gameContinue.show();
        }
    }

    private void gameset() {
        vib = firstplay.getInt("vib", 1);
        sound = firstplay.getInt("sound", 1);
    }


    private void go2ThreadTime() {

        totalTime = TIME;
        if (pause == true) {
            totalTime = pausetime;
            pause = false;
        } else {
            THREAD_SLEEP_TIME = 500;
            THREAD_Hammer_TIME = 100;
            speed = 2;
            next = 99;
            time = 0;
            clearmouse();
        }

        if (t == null) {
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (totalTime != 0) {
                            Thread.sleep(THREAD_SLEEP_TIME);
                            if (next != 99)
                                old = next;
                            time++;
                            if (time % speed == 0)
                                totalTime--;
                            next = random.nextInt(12);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (h == null && mousearr.get(old).getVisibility() == View.VISIBLE)
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
                    if (totalTime <= 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                t.interrupt();
                                t = null;
                                level++;
                                GameContinue();

                            }
                        });
                    }
                }
            });
        }
        t.start();


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

    private void clearmouse() {
        for (int i = 0; i < mousearr.size(); i++) {
            mousearr.get(i).setVisibility(View.INVISIBLE);
        }
    }

    private void ThreadTime1() {

        if (pause == true) {
            totalTime = pausetime;
            pause = false;
        } else {
            THREAD_SLEEP_TIME = 1000;
            speed = 1;
            next = 99;
            time = 0;
            totalTime = TIME;
        }
        clearmouse();


        if (t == null) {
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (totalTime != 0) {
                            Thread.sleep(THREAD_SLEEP_TIME);
                            if (next != 99)
                                old = next;
                            time++;
                            if (time % speed == 0)
                                totalTime--;
                            next = random.nextInt(12);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (h == null && mousearr.get(old).getVisibility() == View.VISIBLE)
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
                    if (totalTime <= 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                t.interrupt();
                                t = null;
                                GameContinue();
                                level++;
                            }
                        });

                    }
                }
            });
        }
        t.start();
    }

    private void go3ThreadTime() {

        if (pause == true) {
            totalTime = pausetime;
            pause = false;
        } else {
            time = 0;
            totalTime = TIME;
            clearmouse();
        }


        if (t == null) {
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (totalTime != 0) {
                            Thread.sleep(THREAD_SLEEP_TIME);
                            if (next != 99)
                                old = next;
                            time++;
                            if (time % speed == 0)
                                totalTime--;
                            next = random.nextInt(12);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // if(h==null&&mousearr.get(old).getVisibility()==View.VISIBLE)
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
                    if (totalTime <= 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                t.interrupt();
                                t = null;
                                Bundle bundle = new Bundle();
                                bundle.putInt("score", score);
                                Intent it = new Intent();
                                it.setClass(GameActivitty.this, GameOverActivity.class);
                                it.putExtras(bundle);
                                startActivity(it);
                                mp.stop();
                                finish();
                            }
                        });
                    }
                }
            });
        }
        t.start();


        if (t2 == null) {//第二隻老鼠
            t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (totalTime != 0) {
                            Thread.sleep(random.nextInt(500) + 500);
                            if (next2 != 99)
                                old2 = next2;
                            next2 = random.nextInt(12);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // if(h==null&&mousearr.get(old2).getVisibility()==View.VISIBLE)
                                    mousearr.get(old2).setVisibility(View.INVISIBLE);
                                    mousearr.get(next2).setVisibility(View.VISIBLE);
                                }
                            });
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (totalTime <= 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }

                }
            });
        }
        t2.start();
    }

    private void checkItem() {
        final int itemaddtimes = scoresave.getInt("timeadd", 0);

        AlertDialog.Builder useItemCheck = new AlertDialog.Builder(GameActivitty.this);
        useItemCheck.setTitle("使用道具");
        useItemCheck.setMessage("你擁有" + itemaddtimes + "個加時道具,使用後可增加10秒,要使用嗎?");
        useItemCheck.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                scoresave.edit().putInt("timeadd", itemaddtimes - 1).commit();
                TIME = TIME + 10;
                Time.setText("40");
                Time.setVisibility(View.VISIBLE);
                ThreadTime1();
                mp.start();
            }
        });

        useItemCheck.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ThreadTime1();
                mp.start();
            }
        });
        useItemCheck.setCancelable(false);
        useItemCheck.show();


    }

    GifImageView.OnTouchListener Hit = new GifImageView.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            txtScore.setVisibility(View.VISIBLE);
            if (view.getVisibility() == View.VISIBLE) {
                if (vib == 1)
                    VibratorUtil.Vibrate(GameActivitty.this, 200);
                score = score + 10;
                String s = Integer.toString(score);
                txtScore.setText(s);
                HammerTime(view);
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
        soundPool.stop(alertId);

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
                                    if (sound == 1)
                                        soundPool.play(alertId, 1.0F, 1.0F, 0, 0, 1.0F);
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
                        h = null;

                    }

                }
            });

        }
        h.start();
    }

    private void pause() {
        if (t != null) {
            t.interrupt();
            t = null;
            pausetime = totalTime;
            pause = true;
        }

        if (t2 != null) {
            t2.interrupt();
            t2 = null;
            pausetime = totalTime;
            pause = true;
        }

        if (h != null) {
            h.interrupt();
            h = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵
            backkeydown = true;
            pause();
            ConfirmExit(); //呼叫ConfirmExit()函數
            return true;

        }
        if (KeyEvent.KEYCODE_HOME == keyCode) {
            pause();
            onPause();
            return true;
        }


        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (backkeydown == false) {
            pause();
            mp.pause();
            mppause = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pause == true ) {
            dialog.dismiss();
            confirmcontinue();
        }
        if (mppause == true) {
            mppause = false;
            mp.start();
        }
        backkeydown = false;

    }


    private void closeThread() {
        if (t != null)
            t.interrupt();
        if (t2 != null)
            t2.interrupt();
        if (h != null)
            h.interrupt();
        t = null;
        t2 = null;
        h = null;
    }

    public void ConfirmExit() {


        ad = new AlertDialog.Builder(GameActivitty.this); //創建訊息方塊

        ad.setTitle("暫停");
        ad.setCancelable(false);

        ad.setMessage("確定要離開?");

        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式

            public void onClick(DialogInterface dialog, int i) {
                closeThread();
                GameActivitty.this.finish();//關閉activity
                mp.stop();


            }

        });

        ad.setNeutralButton("設定", new DialogInterface.OnClickListener() { //按"是",則退出應用程式

            public void onClick(DialogInterface dialog, int i) {
                Intent it = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("gamemode", 1);
                it.putExtras(bundle);
                it.setClass(GameActivitty.this, GameSetActivity.class);
                startActivityForResult(it, 1);
            }

        });


        ad.setNegativeButton("否", new DialogInterface.OnClickListener()

        { //按"否",則不執行任何操作

            public void onClick(DialogInterface dialog, int i) {

                switch (level) {
                    case 1:
                        ThreadTime1();
                        break;
                    case 2:
                        go2ThreadTime();
                        break;
                    case 3:
                        go3ThreadTime();
                        break;
                }

            }

        });

        if (!isFinishing())
        {
           dialog= ad.show();
        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                gameset();
            }
        }


    }

    private void confirmcontinue() {
        cc = new AlertDialog.Builder(GameActivitty.this);
        cc.setTitle("暫停中");

        cc.setMessage("按是繼續遊戲");
        cc.setCancelable(false);

        cc.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式

            public void onClick(DialogInterface dialog, int i) {


                switch (level) {
                    case 1:
                        ThreadTime1();
                        break;
                    case 2:
                        go2ThreadTime();
                        break;
                    case 3:
                        go3ThreadTime();
                        break;
                }

            }

        });
        if (!isFinishing())
        cc.show();
    }
}


