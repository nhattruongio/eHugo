package cf.nhattruong.ehugo.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cf.nhattruong.ehugo.Activitys.CreateHouseActivity;
import cf.nhattruong.ehugo.Activitys.CreateRoomActivity;
import cf.nhattruong.ehugo.Activitys.ListRoomActivity;
import cf.nhattruong.ehugo.Activitys.LoginActivity;
import cf.nhattruong.ehugo.Adapters.HouseAdapter;
import cf.nhattruong.ehugo.R;
import cf.nhattruong.ehugo.Utils.TokenManager;
import cf.nhattruong.ehugo.Models.HouseModel;
import cf.nhattruong.ehugo.Models.UserModel;
import cf.nhattruong.ehugo.network.ApiService;
import cf.nhattruong.ehugo.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HousesFragment extends Fragment implements HouseAdapter.OnHouseListener {

    FloatingActionButton creHouse;
    private static final String TAG = "HousesFragment";
    private static final int REQUEST_CODE = 1;
    private Context context;

    TokenManager tokenManager;
    ApiService service;
    ApiService serviceWithAuth;

    Call<ArrayList<HouseModel>> call;

    HouseAdapter houseAdapter;
    RecyclerView recyclerView;

    ArrayList<HouseModel> houseModelArrayList;


    public HousesFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_houses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        autoLoad();

        recyclerView = view.findViewById(R.id.recycleView);
        list_houses();
        creHouse = view.findViewById(R.id.floating_action_button);
        creHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goTo();
            }
        });
    }

    private void autoLoad() {
        context = getContext();
        service = RetrofitBuilder.createService(ApiService.class);
        tokenManager = TokenManager.getInstance(context.getSharedPreferences("prefs", MODE_PRIVATE));
        serviceWithAuth = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        if (tokenManager.getToken().getAccessToken() == null) {
            tokenManager.deleteToken();
            onForwardLogin();
        } else {
            checkAuth();
        }
    }

    private void list_houses() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.ThemeOverlay_MaterialComponents_Dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        call = service.list_houses(tokenManager.getToken().getAccessToken());
        call.enqueue(new Callback<ArrayList<HouseModel>>() {
            @Override
            public void onResponse(@Nullable Call<ArrayList<HouseModel>> call, @Nullable Response<ArrayList<HouseModel>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        houseModelArrayList = new ArrayList<>();
                        houseModelArrayList.addAll(response.body());
                        initHouse(houseModelArrayList);
                    } else {
                        Toast.makeText(context, "Quá thời gian xử lý, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Có lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<HouseModel>> call, Throwable t) {
                progressDialog.dismiss();
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void initHouse(ArrayList<HouseModel> hires) {
        houseAdapter = new HouseAdapter(hires, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(houseAdapter);
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
                    Toast.makeText(context, "Lỗi kết nối, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void onForwardLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onHouseClick(int position) {
        HouseModel house = houseModelArrayList.get(position);

        Intent intent = new Intent(context, ListRoomActivity.class);
        intent.putExtra("house_id", house.getId());
        intent.putExtra("house_name",house.getName_house());

        Intent intent2 = new Intent(context, CreateRoomActivity.class);
        intent2.putExtra("house_id", house.getId());
        intent2.putExtra("house_name",house.getName_house());

/*
        startActivityForResult(intent2, REQUEST_CODE);
*/
        startActivityForResult(intent, REQUEST_CODE);
    }



    public void goTo() {
        Intent intent = new Intent(getContext(), CreateHouseActivity.class);
        startActivity(intent);

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
