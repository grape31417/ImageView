package com.example.bluedream.hitmouse;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

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
    @BindView(R.id.txtlocation)
    TextView txtlocation;
    OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        firstplayset();
        locateUser();
    }

    private void firstplayset() {
        firstplay = getSharedPreferences("first_play", MODE_PRIVATE);
        if (firstplay.getBoolean("first_play", false) == false) {
            firstplay.edit().putBoolean("first_play", true).commit();
            scoresave = getSharedPreferences("scoresave", MODE_PRIVATE);
            scoresave.edit().putString("name1", "").putInt("score1", 0).putString("lat1","0").putString("lng1","0").commit();
            scoresave.edit().putString("name2", "").putInt("score2", 0).putString("lat2","0").putString("lng2","0").commit();
            scoresave.edit().putString("name3", "").putInt("score3", 0).putString("lat3","0").putString("lng3","0").commit();
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
            txtlocation.setText(s);

        }
    }





    @OnClick(R.id.GameStart)
    public void onGameStartClicked() {
        Intent it = new Intent();
        it.setClass(MainActivity.this, GameActivitty.class);
        startActivity(it);
    }

    @OnClick(R.id.HighScore)
    public void onHighScoreClicked() {
        Intent it = new Intent();
        it.setClass(MainActivity.this, ScoreActivity.class);
        startActivity(it);


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
}
