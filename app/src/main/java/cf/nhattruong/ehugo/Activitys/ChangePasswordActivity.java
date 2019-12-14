package cf.nhattruong.ehugo.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import cf.nhattruong.ehugo.Models.CustomerModel;
import cf.nhattruong.ehugo.Models.UserModel;
import cf.nhattruong.ehugo.R;
import cf.nhattruong.ehugo.Utils.CustomDialog;
import cf.nhattruong.ehugo.Utils.TokenManager;
import cf.nhattruong.ehugo.network.ApiService;
import cf.nhattruong.ehugo.network.RetrofitBuilder;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "ChangePasswordActivity";
    public static final int REQUEST_CODE = 1;
    private int code;

    ApiService service;
    ApiService serviceWithAuth;
    TokenManager tokenManager;


    // LinearLayout
    LinearLayout llBack;

    // EditText
    EditText edtOld,edtNew, edtNew1;

    // Button
    Button btnUpdate;

    private String txtOld,txtNew,txtNew1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setLightStatusBar(ChangePasswordActivity.this);

//        service = RetrofitBuilder.createService(ApiService.class);
//
//        tokenManager = TokenManager.getInstance(this.getSharedPreferences("prefs", MODE_PRIVATE));
//        serviceWithAuth = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

//        if (tokenManager.getToken().getAccessToken() == null) {
//            tokenManager.deleteToken();
//            __onForwardLogin();
//        }

//        __getLastActivity();
        __ref();

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                __back();
            }
        });
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                __update();
//            }
//        });

    }

    private void __ref() {
        llBack = findViewById(R.id.ll_back);
//        edtOld = findViewById(R.id.input_old_pass);
//        edtNew = findViewById(R.id.input_new_pass);
//        edtNew1 = findViewById(R.id.input_new_repass);
//        btnUpdate = findViewById(R.id.btn_update);
    }

    private void __back() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
//    private void __update() {
//        if (!validate()) {
//            onRegisterFailed();
//            return;
//        }
//
//        String old_password = edtOld.getText().toString().trim();
//        String new_password = edtOld.getText().toString().trim();
//
//        edtOld.setError(null);
//        edtOld.setError(null);
//        edtNew1.setError(null);
//
//
//        final ProgressDialog progressDialog = new ProgressDialog(ChangePasswordActivity.this,
//                R.style.ThemeOverlay_MaterialComponents_Dialog);
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        progressDialog.setIndeterminate(true);
//        progressDialog.show();
//
//
//        serviceWithAuth.change_password(tokenManager.getToken().getAccessToken(),old_password,new_password).enqueue(new Callback<UserModel>() {
//            @Override
//            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                progressDialog.dismiss();
//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
////                        CustomDialog.confirmDialog(ChangePasswordActivity.this, "OKE", 1000);
//                        Toast.makeText(ChangePasswordActivity.this, Toast.LENGTH_LONG, "OKE").show();
//                        __returnLastActivity();
//                    } else {
//                        tokenManager.deleteToken();
//                        __onForwardLogin();
//                    }
//                } else {
//                    if (response.code() == 401) {
//                        CustomDialog.confirmDialog(ChangePasswordActivity.this, response.message(), 1000);
//                    } else if (response.code() == 422) {
//                        CustomDialog.confirmDialog(ChangePasswordActivity.this, response.message(), 1000);
//                    } else {
//                        CustomDialog.confirmDialog(ChangePasswordActivity.this, "Lỗi kết nối, vui lòng thử lại", 1000);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserModel> call, Throwable t) {
//                progressDialog.dismiss();
//                Log.w(TAG, "onFailure: " + t.getMessage());
//                tokenManager.deleteToken();
//                __onForwardLogin();
//            }
//        });
//    }
//    public void onRegisterFailed() {
//        CustomDialog.confirmDialog(ChangePasswordActivity.this, "Đ thất bại", 2000);
//
////        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_LONG).show();
//        btnUpdate.setEnabled(true);
//    }
//
//    public boolean validate() {
//        boolean valid = true;
//
//        String pass_old = edtOld.getText().toString();
//        String pass_new = edtNew.getText().toString();
//        String repass_new = edtNew1.getText().toString();
//
//        if (pass_old.isEmpty() ) {
//            edtOld.setError("Không được để trống");
//            valid = false;
//        } else {
//            edtOld.setError(null);
//        }
//        if (pass_new.isEmpty() ) {
//            edtNew.setError("Không được để trống");
//            valid = false;
//        } else {
//            edtNew.setError(null);
//        }
//        if (pass_new.isEmpty() || pass_new.length() < 8) {
//            edtNew.setError("Mật khẩu không được nhỏ hơn 8 ký tự");
//            valid = false;
//        } else {
//            edtNew.setError(null);
//        }
//        if (repass_new.isEmpty() || repass_new.length() < 8) {
//            edtNew1.setError("Xác nhận mật khẩu không được nhỏ hơn 8 ký tự");
//            valid = false;
//        } else {
//            edtNew1.setError(null);
//        }
//        if (!repass_new.equals(pass_new)) {
//            edtNew1.setError("Xác nhận mật khẩu không khớp");
//            valid = false;
//        }
//        return valid;
//    }
//
    private static void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
        }
    }
//
//    private void __returnLastActivity() {
//        Intent intent = new Intent();
//        setResult(Activity.RESULT_OK, intent);
//        finish();
//
//    }
//
//    private void __checkAuth() {
//        serviceWithAuth.get_data_user(tokenManager.getToken().getAccessToken()).enqueue(new Callback<UserModel>() {
//            @Override
//            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                if (response.isSuccessful()) {
//                    if (response.body() != null && response.body().user_name != null) {
//                        Log.d(TAG, "onResponse: " + response.body());
//                    } else {
//                        tokenManager.deleteToken();
//                        __onForwardLogin();
//                    }
//                } else {
//                    CustomDialog.confirmDialog(ChangePasswordActivity.this, "Lỗi xác thực, vui lòng thử lại", 1000);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserModel> call, Throwable t) {
//                Log.w(TAG, "onFailure: " + t.getMessage());
//            }
//        });
//    }
//
//    private void __getLastActivity() {
//        Intent intent = getIntent();
//        code = intent.getIntExtra("code", -1);
//    }
//
//    private void __onForwardLogin() {
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//        } else if (requestCode == 2) {
//            Toast.makeText(this, "NOT OKE", Toast.LENGTH_LONG).show();
//        }
//    }
}
