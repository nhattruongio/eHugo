package cf.nhattruong.ehugo.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import cf.nhattruong.ehugo.R;

import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cf.nhattruong.ehugo.Adapters.MyPagerAdapter;

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private LinearLayout layoutDot;
    private TextView[]dotsv;
    private  int[]layouts;
    private Button btnSkip;
    private Button btnNext;

    private MyPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isFirstTimeStartApp()){
            startLoginActivity();
            finish();
        }

        setStatusBarTransparent();

        setContentView(R.layout.activity_welcome);

        viewPager = findViewById(R.id.view_pager);
        layoutDot = findViewById(R.id.dotLayout);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginActivity();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPage = viewPager.getCurrentItem()+1;
                if (currentPage<layouts.length){
                    // move to next
                    viewPager.setCurrentItem(currentPage);
                }else {
                    startLoginActivity();
                }
            }
        });
        layouts = new int[]{
                R.layout.slider_1,
                R.layout.slider_2,
                R.layout.slider_3,
                R.layout.slider_4
        };

        pagerAdapter = new MyPagerAdapter(layouts, getApplicationContext());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == layouts.length - 1){
                    btnNext.setText("Bắt đầu");
                    btnSkip.setVisibility(View.GONE);
                }else {
                    btnNext.setText("Tiếp theo");
                    btnSkip.setVisibility(View.VISIBLE);
                }
                setDotStatus(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setDotStatus(0);
    }
    private boolean isFirstTimeStartApp(){
        SharedPreferences ref = getApplicationContext().getSharedPreferences("IntroSliderApp", Context.MODE_PRIVATE);
        return ref.getBoolean("FirstTimeStartFlag",true);
    }

    private  void setFirstTimeStartStatus(boolean stt){
        SharedPreferences ref = getApplicationContext().getSharedPreferences("IntroSliderApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putBoolean("FirstTimeStartFlag",stt);
        editor.commit();
    }

    private  void setDotStatus (int page){
        layoutDot.removeAllViews();
        dotsv  = new TextView[layouts.length];
        for (int i = 0; i < dotsv.length; i++){
            dotsv[i] = new TextView(this);
            dotsv[i].setText(Html.fromHtml("&#8226;"));
            dotsv[i].setTextSize(30);
            dotsv[i].setTextColor(Color.parseColor("#a9b4bb"));
            layoutDot.addView(dotsv[i]);
        }

        //Set current dot active
        if (dotsv.length>0){
            dotsv[page].setTextColor(Color.parseColor("#ffffff"));
        }
    }

    private void startLoginActivity (){
        setFirstTimeStartStatus(false);
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
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
