package cf.nhattruong.ehugo.Activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import cf.nhattruong.ehugo.Adapters.RoomAdapter;
import cf.nhattruong.ehugo.Models.CustomerModel;
import cf.nhattruong.ehugo.Models.DealModel;
import cf.nhattruong.ehugo.Models.RoomModel;
import cf.nhattruong.ehugo.Models.UserModel;
import cf.nhattruong.ehugo.R;
import cf.nhattruong.ehugo.Utils.CustomDialog;
import cf.nhattruong.ehugo.Utils.TokenManager;
import cf.nhattruong.ehugo.network.ApiService;
import cf.nhattruong.ehugo.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class IndexRoomActivity extends AppCompatActivity {
    private  androidx.cardview.widget.CardView cardView;
    private static final String TAG = "CreateHouseActivity";
    TokenManager tokenManager;
    ApiService service;
    ApiService serviceWithAuth;
    Call<CustomerModel> call;
    Call<DealModel> call2;
    public  String token;
    public int room_id;
    TextView txt_name_room,txt_price_room,txt_acreage_room,txt_tel_customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_index_room );
        cardView = findViewById( R.id.btn_cre_deal);
        txt_name_room = findViewById( R.id.txt_name_room);
        txt_price_room = findViewById( R.id.txt_price_room );
        setLightStatusBar(this);
        autoLoad();

        cardView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexRoomActivity.this,CreDealActivity.class );
                startActivity( intent );
//                finish();
            }
        } );
    }
//    private void show_data_room(){
//        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog);
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        progressDialog.setIndeterminate(true);
//        progressDialog.show();
//
//        call = service.get_once_book(book_id);
//        call.enqueue(new Callback<Book>() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onResponse(@Nullable Call<RoomModel> call, @Nullable Response<RoomModel> response) {
//                progressDialog.dismiss();
//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
//
//                        txt_name_room.setText(response.body().);
//                        txt_acreage_room.setText(response.body()..name);
//                        txtDescription.setText(response.body().description_book);
//
//                        DecimalFormat df = new DecimalFormat("#,##0");
//                        df.setDecimalFormatSymbols(new DecimalFormatSymbols( Locale.US));
//
//                        txt_price_room.setText(df.format(response.body().price_book) + " đ");
//                        price = response.body().price_book;
//                    } else {
//                        CustomDialog.confirmDialog(IndexRoomActivity.this, "Có lỗi, vui lòng thử lại", 1000);
//                    }
//                } else {
//                    CustomDialog.confirmDialog(IndexRoomActivity.this, "Quá thời gian, vui lòng thử lại", 1000);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Book> call, Throwable t) {
//                progressDialog.dismiss();
//                Log.w(TAG, "onFailure: " + t.getMessage());
//            }
//        });
//    }
    private void initRoom(RoomModel rooms) {

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
                    Toast.makeText(IndexRoomActivity.this, "Lỗi kết nối, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void onForwardLogin() {
        Intent intent = new Intent(IndexRoomActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
