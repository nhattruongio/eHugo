package cf.nhattruong.ehugo.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import cf.nhattruong.ehugo.Activitys.LoginActivity;
import cf.nhattruong.ehugo.Models.UserModel;
import cf.nhattruong.ehugo.R;
import cf.nhattruong.ehugo.Utils.TokenManager;
import cf.nhattruong.ehugo.network.ApiService;
import cf.nhattruong.ehugo.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class AccountFragment extends Fragment {

    private static final int REQUEST_CODE = 1;
    private Context context;
     ApiService service;
     ApiService serviceWithAuth;
     TokenManager tokenManager;

    private static final String TAG = "AccountFragment";
    ConstraintLayout llChage, llLogout;
    TextView txtUser_name;

    public AccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llLogout = view.findViewById(R.id.ll_logout);
        txtUser_name = view.findViewById(R.id.txtUser_name);
        llChage = view.findViewById(R.id.ll_change_password);
        autoLoad();
        getDataUser();

        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tokenManager.deleteToken();
                onForwardLogin();
                getActivity().finish();
            }
        });

        /*llChage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateUserActivity.class);
                intent.putExtra("code", 1);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });*/
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

    private void getDataUser() {
        final ProgressDialog progressDialog = new ProgressDialog(context,
                R.style.ThemeOverlay_MaterialComponents_Dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        serviceWithAuth.get_data_user(tokenManager.getToken().getAccessToken()).enqueue(new Callback<UserModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().user_name != null) {
                        txtUser_name.setText("Hello: "+response.body().getUser_name());
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
                progressDialog.dismiss();
                Log.w(TAG, "onFailure: " + t.getMessage());
                tokenManager.deleteToken();
                onForwardLogin();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            onRefreshFragment();
        } else if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
    }

    private void onRefreshFragment() {
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();
    }

}
