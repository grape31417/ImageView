package com.example.bluedream.hitmouse;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
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
    public static int THREAD_Hammer_TIME = 200;
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
    MediaPlayer mp ;
    private  SoundPool soundPool;
    private int alertId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_activitty);
        ButterKnife.bind(this);
        mp= MediaPlayer.create(GameActivitty.this, R.raw.battle);
        mp.setLooping(true);
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
        alertId = soundPool.load(this, R.raw.hit, 1);
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
                mp.start();
            }
        });

        gameStartCheck.setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                GameActivitty.this.finish();
            }
        });
        gameStartCheck.setCancelable(false);
        gameStartCheck.show();
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
                Bundle bundle=new Bundle();
                bundle.putInt("score",score);
                Intent it = new Intent();
                it.setClass(GameActivitty.this, GameOverActivity.class);
                it.putExtras(bundle);
                startActivity(it);
                mp.stop();
                finish();
            }
        });
        gameContinue.setCancelable(false);
        gameContinue.show();
    }


    private void go2ThreadTime() {
        THREAD_SLEEP_TIME = 500;
        speed = 2;
        next = 99;
        time = 0;
        totalTime = TIME;
        clearmouse ();

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

    private void clearmouse ()
    {
        for(int i=0;i<mousearr.size();i++)
        {
            mousearr.get(i).setVisibility(View.INVISIBLE);
        }
    }


    private void ThreadTime1() {
        THREAD_SLEEP_TIME = 1000;
        speed = 1;
        next = 99;
        time = 0;
        clearmouse ();


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
        time = 0;
        totalTime = TIME;
        clearmouse ();

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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                t.interrupt();
                                t = null;
                                Bundle bundle=new Bundle();
                                bundle.putInt("score",score);
                                Intent it = new Intent();
                                it.setClass(GameActivitty.this, GameOverActivity.class);
                                it.putExtras(bundle);
                                startActivity(it);
                                mp.stop();
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
                                    mousearr.get(old2).setVisibility(View.INVISIBLE);
                                    mousearr.get(next2).setVisibility(View.VISIBLE);
                                }
                            });
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (totalTime == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                t2.interrupt();
                                t2 = null;
                            }
                        });
                    }

                }
            });
        }
        t2.start();
    }

    GifImageView.OnTouchListener Hit = new GifImageView.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view.getVisibility() == View.VISIBLE) {
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
                        // h.interrupt();
                        h = null;

                    }

                }
            });

        }
        h.start();
    }


}

