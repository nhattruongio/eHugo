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
import cf.nhattruong.ehugo.Activitys.CallCustomerActivity;
import cf.nhattruong.ehugo.Activitys.LoginActivity;
import cf.nhattruong.ehugo.Adapters.CustomerAdapter;
import cf.nhattruong.ehugo.Models.CustomerModel;
import cf.nhattruong.ehugo.Models.UserModel;
import cf.nhattruong.ehugo.R;
import cf.nhattruong.ehugo.Utils.TokenManager;
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

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsFragment extends Fragment implements CustomerAdapter.OnCustomerListener {

    private static final String TAG = "NotificationsFragment";
    private static final int REQUEST_CODE = 1;
    private Context context;

    TokenManager tokenManager;
    ApiService service;
    ApiService serviceWithAuth;

    RecyclerView recyclerView;


    Call<ArrayList<CustomerModel>> call;
    ArrayList<CustomerModel> customerModelArrayList;

    CustomerAdapter customerAdapter;
    String token;


    public NotificationsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycleView_customer);

        autoLoad();
        list_customer();
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

    private void list_customer() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.ThemeOverlay_MaterialComponents_Dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        token = tokenManager.getToken().getAccessToken();

        call = service.list_customer(token);
        call.enqueue(new Callback<ArrayList<CustomerModel>>() {
            @Override
            public void onResponse(@Nullable Call<ArrayList<CustomerModel>> call, @Nullable Response<ArrayList<CustomerModel>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        customerModelArrayList = new ArrayList<>();
                        customerModelArrayList.addAll(response.body());
                        initCustomer(customerModelArrayList);
                    } else {
                        Toast.makeText(context, "Quá thời gian xử lý, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Có lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CustomerModel>> call, Throwable t) {
                progressDialog.dismiss();
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void initCustomer(ArrayList<CustomerModel> customers) {

        customerAdapter = new CustomerAdapter(customers, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(customerAdapter);
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
    public void onCustomerClick(int position) {
        CustomerModel customer = customerModelArrayList.get(position);
        String a= customer.getName();
        Intent intent = new Intent(context, CallCustomerActivity.class);
        intent.putExtra("customer_id", customer.getId());
        intent.putExtra("customer_name",a);
        intent.putExtra( "tel_customer",customer.getTel());
        startActivityForResult(intent, REQUEST_CODE);
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
