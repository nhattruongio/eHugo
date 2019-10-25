package cf.nhattruong.ehugo.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cf.nhattruong.ehugo.Activitys.AnalyticsActivity;
import cf.nhattruong.ehugo.Activitys.BillActivity;
import cf.nhattruong.ehugo.Activitys.CameraViewActivity;
import cf.nhattruong.ehugo.Activitys.CancelDealActivity;
import cf.nhattruong.ehugo.Activitys.CreDealActivity;
import cf.nhattruong.ehugo.Activitys.ElecActivity;
import cf.nhattruong.ehugo.Activitys.ListRoomActivity;
import cf.nhattruong.ehugo.R;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class HomeFragment extends Fragment {

    ViewFlipper v_flipper;

    ImageView cam, analytics, bill, deal, cancel, elec;

    public HomeFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setStatusBarTransparent();
        Init();
        deal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreDealActivity.class);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CancelDealActivity.class);
                startActivity(intent);
            }
        });

        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AnalyticsActivity.class);
                startActivity(intent);
            }
        });

        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BillActivity.class);
                startActivity(intent);
            }
        });

        elec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ElecActivity.class);
                startActivity(intent);
            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CameraViewActivity.class);
                startActivity(intent);
            }
        });

        int images[] = {R.drawable.flipper1,R.drawable.flipper11};

        v_flipper = view.findViewById(R.id.v_flipper);

        for (int image: images){
            flipperImages(image);
        }
    }

    void Init(){
        deal = getView().findViewById(R.id.credeal);
        cancel = getView().findViewById(R.id.cancelDeal);
        analytics = getView().findViewById(R.id.analytics);
        bill = getView().findViewById(R.id.bill);
        elec = getView().findViewById(R.id.elec);
        cam = getView().findViewById(R.id.cam);
    }

    public  void flipperImages (int image){
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4200);
        v_flipper.setAutoStart(true);

        v_flipper.setInAnimation(getContext(),android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(getContext(),android.R.anim.slide_out_right);
    }
    private  void setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT >= 21){
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
