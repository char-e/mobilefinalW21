package com.kakaladies.newsdemon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class UnlockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);

        findViewById(R.id.buy15).setOnClickListener(v -> {
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("Message", "Authorize $9.99 charge?");
            startActivity(intent);
        });

        findViewById(R.id.buy50).setOnClickListener(v -> {
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("Message", "Authorize $19.99 charge?");
            startActivity(intent);

        });

        findViewById(R.id.buy1000).setOnClickListener(v -> {
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("Message", "Authorize $45.00 charge?");
            startActivity(intent);

        });

        findViewById(R.id.unlockHelpButton).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(UnlockActivity.this);
            builder.setMessage("This page is for unlocking more articles")
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