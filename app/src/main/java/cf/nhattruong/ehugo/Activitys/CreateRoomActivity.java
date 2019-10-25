package cf.nhattruong.ehugo.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import cf.nhattruong.ehugo.Models.HouseModel;
import cf.nhattruong.ehugo.Models.RoomModel;
import cf.nhattruong.ehugo.Models.UserModel;
import cf.nhattruong.ehugo.R;
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

public class CreateRoomActivity extends AppCompatActivity {
    ImageButton back;
    private static final String TAG = "CreateHouseActivity";
    TokenManager tokenManager;
    ApiService service;
    ApiService serviceWithAuth;
    Call<RoomModel> call;
    public EditText editName, editDescription, editPrice, editAcreage, editName_House;
    MaterialButton btnCre;
    public String token;
    public Integer house_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_room);
        setLightStatusBar(this);
        autoLoad();

        editName_House = findViewById(R.id.input_name_house);
        editName = findViewById(R.id.input_name_room);
        editAcreage = findViewById(R.id.input_acreage_room);
        editDescription = findViewById(R.id.input_description);
        editPrice = findViewById(R.id.input_price_room);
        btnCre = findViewById(R.id.btn_Cre_Room);

        btnCre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cre_room();
            }
        });

        back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
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

    public void cre_room() {
        if (!validate()) {
            onCre_Room_Failed();
            return;
        }

        String name = editName.getText().toString();
        String description = editDescription.getText().toString();
        float price = Float.parseFloat(editPrice.getText().toString());
        float acreage =Float.parseFloat(editAcreage.getText().toString());

        Intent intent = getIntent();
        house_id = intent.getIntExtra("house_id", -1);

        editName.setError(null);
        editPrice.setError(null);
        editDescription.setError(null);
        editAcreage.setError(null);

        final ProgressDialog progressDialog = new ProgressDialog(CreateRoomActivity.this,
                R.style.ThemeOverlay_MaterialComponents_Dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        token = tokenManager.getToken().getAccessToken();

        call = service.cre_room(name, price, acreage,  description, house_id, token);

        call.enqueue(new Callback<RoomModel>() {
            @Override
            public void onResponse(Call<RoomModel> call, Response<RoomModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Toast.makeText(CreateRoomActivity.this, "Thêm phòng thành công", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                } else {
                    handleErrors(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<RoomModel> call, Throwable t) {
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
            if (error.getKey().equals("price")) {
                editPrice.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("description")) {
                editDescription.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("acreage")) {
                editAcreage.setError(error.getValue().get(0));
            }
        }
    }

    public void onCre_Room_Failed() {
        Toast.makeText(CreateRoomActivity.this, "Thêm phòng thất bại", Toast.LENGTH_LONG).show();
        btnCre.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = editName.getText().toString();
        String acreage = editAcreage.getText().toString();
        String description = editDescription.getText().toString();
        String price = editPrice.getText().toString();

        if (name.isEmpty()) {
            editName.setError("Tên không được để trống");
            valid = false;
        } else {
            editName.setError(null);
        }
        if (acreage.isEmpty()) {
            editAcreage.setError("Diện tích không được để trống");
            valid = false;
        } else {
            editAcreage.setError(null);
        }
        if (price.isEmpty()) {
            editPrice.setError("Giá phòng không được để trống");
            valid = false;
        } else {
            editPrice.setError(null);
        }
        if (description.isEmpty()) {
            editDescription.setError("Mô tả không được để trống");
            valid = false;
        } else {
            editDescription.setError(null);
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
                    Toast.makeText(CreateRoomActivity.this, "Lỗi kết nối, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void onForwardLogin() {
        Intent intent = new Intent(CreateRoomActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
