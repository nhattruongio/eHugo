package cf.nhattruong.ehugo.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import cf.nhattruong.ehugo.Models.HouseModel;
import cf.nhattruong.ehugo.Models.UserModel;
import cf.nhattruong.ehugo.R;
import cf.nhattruong.ehugo.Utils.AccessToken;
import cf.nhattruong.ehugo.Utils.TokenManager;
import cf.nhattruong.ehugo.Utils.Utils;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Map;

public class CreateHouseActivity extends AppCompatActivity {
    ImageButton back;
    private static final String TAG = "CreateHouseActivity";
    TokenManager tokenManager;
    ApiService service;
    ApiService serviceWithAuth;
    Call<HouseModel> call;
    EditText editName, editDescription, editAddress, editQuantity;
    MaterialButton btnCre;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_house);
        setLightStatusBar(this);
        service = RetrofitBuilder.createService(ApiService.class);

        editName = findViewById(R.id.input_name_house);
        editAddress = findViewById(R.id.input_address_house);
        editDescription = findViewById(R.id.input_description);
        editQuantity = findViewById(R.id.input_quantity_room);
        btnCre = findViewById(R.id.btn_Cre_House);
        autoLoad();
        back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
        btnCre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cre_house();
            }
        });


    }

    private void autoLoad() {
        service = RetrofitBuilder.createService(ApiService.class);
        tokenManager = TokenManager.getInstance(this.getSharedPreferences("prefs", MODE_PRIVATE));
        serviceWithAuth = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        if (tokenManager.getToken().getAccessToken() == null) {
            tokenManager.deleteToken();
            onForwardLogin();
        } else {
            checkAuth();
        }
    }

    public void cre_house() {
        if (!validate()) {
            onCre_House_Failed();
            return;
        }

        String name = editName.getText().toString();
        String address = editAddress.getText().toString();
        String description = editDescription.getText().toString();
        String quantity = editQuantity.getText().toString();

        editName.setError(null);
        editAddress.setError(null);
        editDescription.setError(null);
        editQuantity.setError(null);

        final ProgressDialog progressDialog = new ProgressDialog(CreateHouseActivity.this,
                R.style.ThemeOverlay_MaterialComponents_Dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        token = tokenManager.getToken().getAccessToken();
        call = service.cre_house(name, address, description, quantity, token);

        call.enqueue(new Callback<HouseModel>() {
            @Override
            public void onResponse(Call<HouseModel> call, Response<HouseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Toast.makeText(CreateHouseActivity.this, "Thêm phòng thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    handleErrors(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<HouseModel> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void handleErrors(ResponseBody response) {
        ApiError apiError = Utils.converErrors(response);
        for (Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()) {
            if (error.getKey().equals("name")) {
                editName.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("address")) {
                editAddress.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("description")) {
                editDescription.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("quantity")) {
                editQuantity.setError(error.getValue().get(0));
            }
        }
    }

    public void onCre_House_Failed() {
        Toast.makeText(CreateHouseActivity.this, "Thêm phòng thất bại", Toast.LENGTH_LONG).show();
        btnCre.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = editName.getText().toString();
        String address = editAddress.getText().toString();
        String description = editDescription.getText().toString();
        String quantity = editQuantity.getText().toString();

        if (name.isEmpty()) {
            editName.setError("Tên không được để trống");
            valid = false;
        } else {
            editName.setError(null);
        }
        if (address.isEmpty()) {
            editAddress.setError("Địa chỉ không được để trống");
            valid = false;
        } else {
            editAddress.setError(null);
        }
        if (description.isEmpty()) {
            editDescription.setError("Mô tả không được để trống");
            valid = false;
        } else {
            editDescription.setError(null);
        }
        if (quantity.isEmpty()) {
            editQuantity.setError("Số lượng không được để trống");
            valid = false;
        } else {
            editQuantity.setError(null);
        }
        return valid;
    }

    private static void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
        }
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
                    Toast.makeText(CreateHouseActivity.this, "Lỗi kết nối, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void onForwardLogin() {
        Intent intent = new Intent(CreateHouseActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
