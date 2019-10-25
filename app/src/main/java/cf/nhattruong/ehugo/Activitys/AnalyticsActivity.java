package cf.nhattruong.ehugo.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import cf.nhattruong.ehugo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class AnalyticsActivity extends AppCompatActivity {

    ImageButton back;

    private static void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        setLightStatusBar(this);

        back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
    }


}
