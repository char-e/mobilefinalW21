package com.kakaladies.newsdemon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ((TextView)findViewById(R.id.authorizeAmount)).setText(getIntent().getStringExtra("Message"));

        findViewById(R.id.helpButton).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
            builder.setMessage(R.string.paymenthelptext)
                    .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create().show();

        });
    }
}