package cf.nhattruong.ehugo.Activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cf.nhattruong.ehugo.Adapters.RoomAdapter;
import cf.nhattruong.ehugo.Models.RoomModel;
import cf.nhattruong.ehugo.Models.UserModel;
import cf.nhattruong.ehugo.R;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListRoomActivity extends AppCompatActivity implements RoomAdapter.OnRoomListener {

    private static final String TAG = "ListRoomActivity";
    private static final int REQUEST_CODE = 1;
    ApiService service;
    ApiService serviceWithAuth;
    Call<ArrayList<RoomModel>> call;
    TokenManager tokenManager;
    RecyclerView recyclerView;
    RoomAdapter roomAdapter;
    ArrayList<RoomModel> roomArrayList;
    FloatingActionButton btnCreate;
    public  String token ,house_name;
    public int house_id;
    TextView txt_House_Name;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room);
        autoLoad();
        list_rooms();
        setLightStatusBar(this);

        Intent intent2 = getIntent();
        house_id = intent2.getIntExtra("house_id",1);
        imageButton = findViewById(R.id.btnBack);
        imageButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListRoomActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } );
        btnCreate =findViewById(R.id.floating_action_button);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListRoomActivity.this, CreateRoomActivity.class);
                intent.putExtra("house_id",house_id);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycleView_room);
    }
    private void list_rooms() {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        token = tokenManager.getToken().getAccessToken();

        Intent intent = getIntent();
        house_id = intent.getIntExtra("house_id",1);
        house_name = intent.getStringExtra("house_name");

        call = service.list_room(token,house_id);

        call.enqueue(new Callback<ArrayList<RoomModel>>() {
            @Override
            public void onResponse(@Nullable Call<ArrayList<RoomModel>> call, @Nullable Response<ArrayList<RoomModel>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.w(TAG, "onResponse: " + response.body());
                        roomArrayList = new ArrayList<>();
                        roomArrayList.addAll(response.body());
                        initRoom(roomArrayList);
                    } else {
                        Toast.makeText(ListRoomActivity.this, "Quá thời gian xử lý, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ListRoomActivity.this, "Có lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RoomModel>> call, Throwable t) {
                progressDialog.dismiss();
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void initRoom(ArrayList<RoomModel> rooms) {
        roomAdapter = new RoomAdapter(rooms, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(roomAdapter);
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

    private void checkAuth() {
        serviceWithAuth.get_data_user(tokenManager.getToken().getAccessToken()).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().user_email != null) {
                        Log.d(TAG, "onResponse: " + response.body());
                    } else {
                        tokenManager.deleteToken();
                        onForwardLogin();
                    }
                } else {
                    Toast.makeText(ListRoomActivity.this, "Lỗi kết nối, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void onForwardLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRoomClick(int position) {
        RoomModel room = roomArrayList.get(position);
        Intent intent = new Intent(this, CreDealActivity.class);
        intent.putExtra("room_id", room.id_room);
            intent.putExtra("house_id",room.getHouse_id() );
        startActivityForResult(intent, REQUEST_CODE);

    }

    private static void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
        }
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
