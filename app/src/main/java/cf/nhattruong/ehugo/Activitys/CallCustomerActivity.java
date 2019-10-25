package cf.nhattruong.ehugo.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import cf.nhattruong.ehugo.R;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CallCustomerActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE_SMS = 1;



    public TextView txtName;
    public TextView txtPhoneNum;
    public String strName;
    public String strPhoneNum;

    Button btnCall;
    private Button sendSMS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_customer);

        Intent intent = getIntent();

        strName = intent.getStringExtra("customer_name");
        strPhoneNum = intent.getStringExtra("tel_customer");


        txtName = findViewById(R.id.nameee);
        txtPhoneNum = findViewById(R.id.phoneNum);
        txtName.setText(strName);
        txtPhoneNum.setText(strPhoneNum);

        final TextView phoneNumber = txtPhoneNum;
        btnCall = findViewById(R.id.btnCall);

        final EditText smsText = findViewById(R.id.message);
        sendSMS = findViewById(R.id.sendSMS);
        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission() && checkPermissionSms()) {
                        Log.e("permission", "Permission already granted.");
                    } else {
                        requestPermissionSms();
                        //requestPermission();
                    }
                }
                String sms = smsText.getText().toString();
                String phoneNum = phoneNumber.getText().toString();
                if (!TextUtils.isEmpty(sms) && !TextUtils.isEmpty(phoneNum)) {
                    if (checkPermissionSms()) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNum, null, sms, null, null);
                    } else {
                        Toast.makeText(CallCustomerActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission() && checkPermissionSms()) {
                        Log.e("permission", "Permission already granted.");
                    } else {
                        //requestPermissionSms();
                        requestPermission();
                    }
                }

                final TextView phoneNumber = findViewById(R.id.phoneNum);
                String phoneNum = phoneNumber.getText().toString();
                if (!TextUtils.isEmpty(phoneNum)) {
                    String dial = "tel:" + phoneNum;
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
                    intent.setData(Uri.parse("tel: " + phoneNum));

                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    startActivity(intent);
                } else {
                    Toast.makeText(CallCustomerActivity.this, "Please enter a valid telephone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkPermission() {
        int CallPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
        return CallPermissionResult == PackageManager.PERMISSION_GRANTED;
    }
    private boolean checkPermissionSms() {
        int result = ContextCompat.checkSelfPermission(CallCustomerActivity.this, Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(CallCustomerActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
//        ActivityCompat.requestPermissions(CallCustomerActivity.this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE_SMS);
    }
    private void requestPermissionSms() {
        ActivityCompat.requestPermissions(CallCustomerActivity.this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean CallPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (CallPermission) {
                        Toast.makeText(CallCustomerActivity.this, "Permission accepted", Toast.LENGTH_LONG).show();
                    }
                    else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(CallCustomerActivity.this, "Permission accepted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CallCustomerActivity.this, "Permission denied", Toast.LENGTH_LONG).show();
                        btnCall.setEnabled(false);
                        Button sendSMS =findViewById(R.id.sendSMS);
                        sendSMS.setEnabled(false);
                    }
                    break;
                }
        }
        /*switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(CallCustomerActivity.this,
                            "Permission accepted", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(CallCustomerActivity.this,  "Permission denied", Toast.LENGTH_LONG).show();
                    Button sendSMS = (Button) findViewById(R.id.sendSMS);
                    sendSMS.setEnabled(false);

                }
                break;
        }*/
    }

    public void call(View view) {
        final TextView phoneNumber = findViewById(R.id.phoneNum);
        String phoneNum = phoneNumber.getText().toString();
        if (!TextUtils.isEmpty(phoneNum)) {
            String dial = "tel:" + phoneNum;
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
            intent.setData(Uri.parse("tel: " + phoneNum));

            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            startActivity(intent);
        } else {
            Toast.makeText(CallCustomerActivity.this, "Please enter a valid telephone number", Toast.LENGTH_SHORT).show();
        }
    }
}
