package com.example.bluedream.hitmouse;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.GameStart)
    Button GameStart;
    @BindView(R.id.HighScore)
    Button HighScore;
    @BindView(R.id.EndGame)
    Button EndGame;
    SharedPreferences firstplay;
    SharedPreferences scoresave;
    public static double lat;
    public static double lng;
    OkHttpClient client = new OkHttpClient();
    @BindView(R.id.GetItem)
    Button GetItem;
    @BindView(R.id.gameset)
    Button gameset;
    private boolean getadddtime = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        scoresave = getSharedPreferences("scoresave", MODE_PRIVATE);
        firstplay = getSharedPreferences("first_play", MODE_PRIVATE);
        firstplayset();
        locateUser();
    }

    private void firstplayset() {

        if (firstplay.getBoolean("first_play", false) == false) {
            firstplay.edit().putBoolean("first_play", true).commit();
            scoresave.edit().putString("name1", "").putInt("score1", 0).putString("lat1", "0").putString("lng1", "0").commit();
            scoresave.edit().putString("name2", "").putInt("score2", 0).putString("lat2", "0").putString("lng2", "0").commit();
            scoresave.edit().putString("name3", "").putInt("score3", 0).putString("lat3", "0").putString("lng3", "0").commit();
            scoresave.edit().putInt("timeadd", 0).commit();
            firstplay.edit().putInt("vib", 1).putInt("sound", 1).commit();
        }
    }


    private void locateUser() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); // 取得裝置的定位服務
        String bestProvider = LocationManager.GPS_PROVIDER; // 指定最佳定位是用 GPS 定位
        Criteria criteria = new Criteria(); // Criteria 會依照裝置的定位設定依狀況幫你取得裝置位置，見備註1
        bestProvider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            MainActivityPermissionsDispatcher.needPermisionWithCheck(this);
            return;
        }
        Location location = locationManager.getLastKnownLocation(bestProvider); // 取得最後定位到的位置，見備註2
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            String s = lat + " " + lng;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                if (result.getContents().equals("addtime")) {
                    Toast.makeText(this, "已獲得一個加時道具", Toast.LENGTH_SHORT).show();
                    scoresave.edit().putInt("timeadd", scoresave.getInt("timeadd", 0) + 1).commit();
                } else
                    Toast.makeText(this, "無效的條碼", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }


    @OnClick(R.id.GameStart)
    public void onGameStartClicked() {
        Bundle bundle = new Bundle();
        Intent it = new Intent();
        Configuration configuration = getResources().getConfiguration();
        //获取屏幕方向
        int l = configuration.ORIENTATION_LANDSCAPE;
        int p = configuration.ORIENTATION_PORTRAIT;
        if (configuration.orientation == l) {
            //System.out.println("现在是横屏");
            bundle.putInt("windowset", 2);
        }
        if (configuration.orientation == p) {
            bundle.putInt("windowset", 1);
            //System.out.println("现在是竖屏");
        }

        it.putExtras(bundle);

        it.setClass(MainActivity.this, GameActivitty.class);
        startActivity(it);
    }


    @OnClick(R.id.HighScore)
    public void onHighScoreClicked() {
        Intent it = new Intent();
        it.setClass(MainActivity.this, ScoreActivity.class);
        startActivity(it);

    }

    @OnClick(R.id.GetItem)
    public void onViewClicked() {
        new IntentIntegrator(this).initiateScan();
    }

    @OnClick(R.id.EndGame)
    public void onEndGameClicked() {
        finish();
    }


    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void needPermision() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void need(final PermissionRequest request) {
    }


    @OnClick(R.id.gameset)
    public void ongamesetClicked() {
        Intent it = new Intent();
        it.setClass(MainActivity.this, GameSetActivity.class);
        startActivity(it);



    }
}
