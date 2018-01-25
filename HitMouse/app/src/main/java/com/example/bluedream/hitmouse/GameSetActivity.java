package com.example.bluedream.hitmouse;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameSetActivity extends AppCompatActivity {

    AudioManager audiomanage;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtMusic)
    TextView txtMusic;
    @BindView(R.id.up)
    ImageButton up;
    @BindView(R.id.down)
    ImageButton down;
    @BindView(R.id.TxtVib)
    TextView TxtVib;
    @BindView(R.id.vibOn)
    RadioButton vibOn;
    @BindView(R.id.viboff)
    RadioButton viboff;
    @BindView(R.id.RadioGroupVib)
    RadioGroup RadioGroupVib;
    @BindView(R.id.TxtSound)
    TextView TxtSound;
    @BindView(R.id.SoundOn)
    RadioButton SoundOn;
    @BindView(R.id.Soundoff)
    RadioButton Soundoff;
    @BindView(R.id.RadioGroupSound)
    RadioGroup RadioGroupSound;
    @BindView(R.id.BtnSet)
    Button BtnSet;
    @BindView(R.id.BtnCancel)
    Button BtnCancel;
    int gamemode=0;
    private int nowvolume,sound,vib;
    private SharedPreferences firstplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_set);
        ButterKnife.bind(this);
        audiomanage = (AudioManager) GameSetActivity.this.getSystemService(this.AUDIO_SERVICE);
        nowvolume=audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);
        firstplay = getSharedPreferences("first_play", MODE_PRIVATE);
        vib=firstplay.getInt("vib",1);
        sound=firstplay.getInt("sound",1);

        //Intent it =getIntent();
       //Bundle bundle= it.getExtras();
       //gamemode=bundle.getInt("gamemode");

       //設定 mRadioButton0 選項為選取狀態
        RadioGroupVib.setOnCheckedChangeListener(radGrpRegionOnCheckedChange);
        RadioGroupSound.setOnCheckedChangeListener(radGrpRegionOnCheckedChange);

        if(vib==1)
            RadioGroupVib.check(R.id.vibOn);
        if(vib==0)
            RadioGroupVib.check(R.id.viboff);
        if(sound==1)
            RadioGroupSound.check(R.id.SoundOn);
        if(sound==0)
            RadioGroupSound.check(R.id.Soundoff);

    }

    @OnClick({R.id.up, R.id.down, R.id.BtnSet, R.id.BtnCancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.up:
                audiomanage.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,
                        AudioManager.FX_FOCUS_NAVIGATION_UP);
                break;
            case R.id.down:
                audiomanage.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,
                    AudioManager.FX_FOCUS_NAVIGATION_UP);
                break;
            case R.id.BtnSet:
                switch(RadioGroupSound.getCheckedRadioButtonId()){
                    case R.id.SoundOn:
                        firstplay.edit().putInt("sound",1).commit();
                        break;
                    case R.id.Soundoff:
                        firstplay.edit().putInt("sound",0).commit();
                        break;
                }

                switch(RadioGroupVib.getCheckedRadioButtonId()){
                    case R.id.vibOn:
                        firstplay.edit().putInt("vib",1).commit();
                        break;
                    case R.id.viboff:
                        firstplay.edit().putInt("vib",0).commit();
                        break;
                }
                Toast.makeText(GameSetActivity.this,"變更完畢",Toast.LENGTH_LONG).show();
                setResult(RESULT_OK,getIntent());
                finish();
                break;
            case R.id.BtnCancel:
                audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC,nowvolume, 0);
                finish();
                break;
        }
    }


    private RadioGroup.OnCheckedChangeListener radGrpRegionOnCheckedChange =
            new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {


                }


            };
}
