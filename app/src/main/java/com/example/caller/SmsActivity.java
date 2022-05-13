package com.example.caller;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.caller.databinding.ActivitySmsBinding;

public class SmsActivity extends AppCompatActivity {
    public static final String PHONE_KEY = "phone_key";
    private ActivitySmsBinding binding;
    private ActivityResultLauncher<String> smsPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySmsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //
        binding.phoneTextSms.setText(
                getIntent().getStringExtra(PHONE_KEY)
        );
        //smsPermission
        smsPermission = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    if (result)
                        sendSms(binding.phoneTextSms.getText().toString(),
                                binding.textEditSms.getText().toString());
                }
        );
        //
        binding.sendButtonSms.setOnClickListener(view -> {
            smsPermission.launch(Manifest.permission.SEND_SMS);
        });
    }

    void sendSms(String phone, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phone));
        intent.putExtra("sms_body", text);
        startActivity(intent);

    }
}