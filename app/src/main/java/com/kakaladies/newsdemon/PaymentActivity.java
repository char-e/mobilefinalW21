package com.kakaladies.newsdemon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ((TextView)findViewById(R.id.authorizeAmount)).setText(getIntent().getStringExtra("Message"));
    }
}