package cf.nhattruong.ehugo.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import cf.nhattruong.ehugo.R;
import cf.nhattruong.ehugo.Utils.TokenManager;
import cf.nhattruong.ehugo.Utils.Utils;
import cf.nhattruong.ehugo.Utils.AccessToken;
import cf.nhattruong.ehugo.network.ApiError;
import cf.nhattruong.ehugo.network.ApiService;
import cf.nhattruong.ehugo.network.RetrofitBuilder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    TextView btn_gotoregister;
    Button btnLogin;
    ApiService service;
    Call<AccessToken> call;
    TokenManager tokenManager;
    TextInputEditText inputEmail, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        setLightStatusBar(this);

        if (tokenManager.getToken().getAccessToken() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        btn_gotoregister = findViewById(R.id.btn_gotoregister);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btn_gotoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
    }

    private static void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
        }
    }

    public void onLoginFailed() {
        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
        btn_gotoregister.setEnabled(true);
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        inputEmail.setError(null);
        inputPassword.setError(null);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.ThemeOverlay_MaterialComponents_Dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        service = RetrofitBuilder.createService(ApiService.class);
        call = service.login(email, password);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        Toast.makeText(LoginActivity.this, "Không thể kết nối", Toast.LENGTH_SHORT).show();
                    }else {
                        tokenManager.saveToken(response.body());
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                } else {
                    if (response.code() == 401) {
                        Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 400) {
                        handleErrors(response.errorBody());
                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                progressDialog.dismiss();
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void goToRegister() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    private void handleErrors(ResponseBody response) {
        ApiError apiError = Utils.converErrors(response);
        for (Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()) {
            if (error.getKey().equals("email")) {
                inputEmail.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("password")) {
                inputPassword.setError(error.getValue().get(0));
            }
        }
    }

    public boolean validate() {
        boolean valid = true;

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Email không được để trống hoặc không đúng dịnh dạng");
            valid = false;
        } else {
            inputEmail.setError(null);
        }
        if (password.isEmpty()) {
            inputPassword.setError("Mật khẩu không được để trống");
            valid = false;
        } else {
            inputPassword.setError(null);
        }
        return valid;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
            call = null;
        }
    }
}
