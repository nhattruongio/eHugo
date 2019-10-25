package cf.nhattruong.ehugo.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import cf.nhattruong.ehugo.Models.DealModel;
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
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class CreDealActivity extends AppCompatActivity {
    private  int mYear, mMonth, mDay;
    private ImageButton back,btn_pick_date_of_birth;
    private static final String TAG = "CreateHouseActivity";
    private TokenManager tokenManager;
    private ApiService service;
    private ApiService serviceWithAuth;
    private Call<DealModel> call;
    private EditText editName, editDescription, editAddress, editTel, editDateOut, editBirth, editMonney;
    private MaterialButton btnCre;
    private String token;
    private Float total_payment;
    private int room_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cre_deal );

        //anh xa
        editName= findViewById( R.id.input_name_customer);
        editDescription =findViewById( R.id.input_description );
        editAddress =findViewById( R.id.input_address_customer );
        editTel =findViewById( R.id.input_tel_customer );
        editDateOut =findViewById( R.id.input_date_return );
        editBirth = findViewById( R.id.input_date_of_birth );
        editMonney = findViewById( R.id.input_down_payment );

        autoLoad();

        ImageButton btnPickDate = findViewById(R.id.btnPickDate);
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog( CreDealActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                int month = monthOfYear + 1;
//                                String date = dayOfMonth + "-" + month + "-" + year;
                                String date = year + "-" + month + "-" + dayOfMonth;
                                Toast.makeText( CreDealActivity.this, "Hạn hợp đồng: " + date, Toast.LENGTH_SHORT).show();
//                                getDataPrizeWithDate(date);
                                EditText edit = findViewById(R.id.input_date_return);
                                edit.setText(date);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        btn_pick_date_of_birth = findViewById(R.id.btn_pick_date_of_birth);
        btn_pick_date_of_birth.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog( CreDealActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                int month = monthOfYear + 1;
//                                String date = dayOfMonth + "-" + month + "-" + year;
                                String date = year + "-" + month + "-" + dayOfMonth;
                                Toast.makeText( CreDealActivity.this, "Ngày sinh: " + date, Toast.LENGTH_SHORT).show();
//                                getDataPrizeWithDate(date);
                                EditText edit = findViewById(R.id.input_date_of_birth);
                                edit.setText(date);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        } );

        back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        btnCre =findViewById( R.id.btn_cre_deal );
        btnCre.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cre_deal();
            }
        } );

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

    public void cre_deal() {
        if (!validate()) {
            onCre_House_Failed();
            return;
        }

        String name = editName.getText().toString();
        String address = editAddress.getText().toString();
        String description = editDescription.getText().toString();
        String tel = editTel.getText().toString();
        String date_of_birth =editBirth.getText().toString();
        String date_out = editDateOut.getText().toString();
        Float down_payment = Float.parseFloat(editMonney.getText().toString());



        editName.setError(null);
        editAddress.setError(null);
        editDescription.setError(null);
        editTel.setError(null);
        editBirth.setError(null);
        editDateOut.setError( null );



        final ProgressDialog progressDialog = new ProgressDialog(CreDealActivity.this,
                R.style.ThemeOverlay_MaterialComponents_Dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        Intent intent = getIntent();
        room_id = intent.getIntExtra("room_id", -1);
        total_payment = intent.getFloatExtra("price",-1);

        token = tokenManager.getToken().getAccessToken();
        call = service.cre_deal(room_id, name, date_of_birth, date_out, description, address, tel, down_payment, total_payment, token);

        call.enqueue(new Callback<DealModel>() {
            @Override
            public void onResponse(Call<DealModel> call, Response<DealModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Toast.makeText(CreDealActivity.this, "Tạo hợp đồng thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    handleErrors(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<DealModel> call, Throwable t) {
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
            if (error.getKey().equals("tel")) {
                editTel.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("date_out")) {
                editDateOut.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("date_of_birth")) {
                editBirth.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("down_payment")) {
                editMonney.setError(error.getValue().get(0));
            }
        }
    }

    public void onCre_House_Failed() {
        Toast.makeText(CreDealActivity.this, "Thêm phòng thất bại", Toast.LENGTH_LONG).show();
        btnCre.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = editName.getText().toString();
        String address = editAddress.getText().toString();
        String description = editDescription.getText().toString();
        String tel = editTel.getText().toString();

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
        if (tel.isEmpty()) {
            editTel.setError("Số điện thoại không được để trống");
            valid = false;
        } else {
            editTel.setError(null);
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
                    Toast.makeText(CreDealActivity.this, "Lỗi kết nối, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void onForwardLogin() {
        Intent intent = new Intent(CreDealActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}
