package cf.nhattruong.ehugo.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import cf.nhattruong.ehugo.R;
import cf.nhattruong.ehugo.Utils.TokenManager;
import cf.nhattruong.ehugo.Utils.Utils;
import cf.nhattruong.ehugo.network.ApiError;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cf.nhattruong.ehugo.Utils.AccessToken;
import cf.nhattruong.ehugo.network.ApiService;
import cf.nhattruong.ehugo.network.RetrofitBuilder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    ApiService service;
    Button btnRegister;
    Call<AccessToken> call;
    TextView link_singin;
    TokenManager tokenManager;

    TextInputEditText inputName,inputEmail,inputPassword,inputRepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        service = RetrofitBuilder.createService(ApiService.class);
        setLightStatusBar(this);
        btnRegister =findViewById(R.id.btn_register);
        link_singin= findViewById(R.id.link_login);

        inputName = findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);
        inputPassword=findViewById(R.id.input_password);
        inputRepassword=findViewById(R.id.input_repassword);
        link_singin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                goToLogin();

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private static void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
        }
    }
    public void register(){
        if (!validate()) {
            onRegisterFailed();
            return;
        }
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        inputName.setError(null);
        inputEmail.setError(null);
        inputPassword.setError(null);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.ThemeOverlay_MaterialComponents_Dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        call = service.register(name,email,password);

        call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body() == null) {
                            Toast.makeText(RegisterActivity.this, "Không thể kết nối", Toast.LENGTH_SHORT).show();
                        }
                        else{
//                            tokenManager.saveToken(response.body());
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }
                    }else {
                        handleErrors(response.errorBody());
                    }
                }
                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());
                    progressDialog.dismiss();
                }
            });
        }

    void goToLogin(){
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

    private void handleErrors(ResponseBody response) {
        ApiError apiError = Utils.converErrors(response);
        for (Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()) {
            if (error.getKey().equals("name")) {
                inputName.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("email")) {
                inputEmail.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("password")) {
                inputPassword.setError(error.getValue().get(0));
            }
        }
    }
    public void onRegisterFailed() {
        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_LONG).show();
        btnRegister.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;

        String name = inputName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String repass = inputRepassword.getText().toString();

        if (name.isEmpty() ) {
            inputName.setError("Tên không được để trống");
            valid = false;
        } else {
            inputName.setError(null);
        }
        if (email.isEmpty() ) {
            inputEmail.setError("Email không được để trống");
            valid = false;
        } else {
            inputEmail.setError(null);
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Email không đúng định dạng");
            valid = false;
        } else {
            inputEmail.setError(null);
        }
        if (password.isEmpty() || password.length() < 8) {
            inputPassword.setError("Mật khẩu không được nhỏ hơn 8 ký tự");
            valid = false;
        } else {
            inputPassword.setError(null);
        }
        if (repass.isEmpty() || repass.length() < 8) {
            inputRepassword.setError("Xác nhận mật khẩu không được nhỏ hơn 8 ký tự");
            valid = false;
        } else {
            inputRepassword.setError(null);
        }
        if (!repass.equals(password)) {
            inputRepassword.setError("Xác nhận mật khẩu không khớp");
            valid = false;
        }
        return valid;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(call != null){
            call.cancel();
            call= null;
        }
    }
}
