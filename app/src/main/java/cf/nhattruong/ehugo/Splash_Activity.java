package cf.nhattruong.ehugo;

import androidx.appcompat.app.AppCompatActivity;
import cf.nhattruong.ehugo.Activitys.LoginActivity;
import cf.nhattruong.ehugo.Activitys.WelcomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Splash_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTransparent();
        startActivity(new Intent(Splash_Activity.this, WelcomeActivity.class));
        finish();
    }
    private  void setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

}
