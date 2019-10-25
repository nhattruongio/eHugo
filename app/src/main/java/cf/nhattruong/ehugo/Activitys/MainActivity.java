package cf.nhattruong.ehugo.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import cf.nhattruong.ehugo.Fragments.AccountFragment;
import cf.nhattruong.ehugo.Fragments.HomeFragment;
import cf.nhattruong.ehugo.Fragments.HousesFragment;
import cf.nhattruong.ehugo.Fragments.NotificationsFragment;
import cf.nhattruong.ehugo.Models.UserModel;
import cf.nhattruong.ehugo.R;
import cf.nhattruong.ehugo.Utils.TokenManager;
import cf.nhattruong.ehugo.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    TokenManager tokenManager;
    ApiService serviceWithAuth;
    private static final String TAG = "MainActivity";



    private static void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tokenManager = TokenManager.getInstance(this.getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken().getAccessToken() == null) {
            checkAuth();
            tokenManager.deleteToken();
            onForwardLogin();
        }

        setLightStatusBar(this);

        BottomNavigationView botNavView = findViewById(R.id.navigation);
        botNavView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();


    }
    public void onForwardLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()) {
                case R.id.nav_houses:
                    selectedFragment = new HousesFragment();
                    break;
                case R.id.nav_notifications:
                    selectedFragment = new NotificationsFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new AccountFragment();
                    break;
                default:
                    selectedFragment = new HomeFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(this, "Bấm nút thoát 2 lần để đóng ứng dụng", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
    private void checkAuth() {
        serviceWithAuth.get_data_user(tokenManager.getToken().getAccessToken()).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().user_name != null) {
                        Log.d(TAG, "onResponse: " + response.body());
                    } else {
                        tokenManager.deleteToken();
                        onForwardLogin();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Lỗi kết nối, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
