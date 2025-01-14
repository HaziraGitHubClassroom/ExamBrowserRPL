package com.hazira.exambrowser;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private static final String AES_KEY = "0123456789abcdef"; // Replace with your 16-character secure key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scanButton = findViewById(R.id.scanButton);
        Button infoButton = findViewById(R.id.infoButton);

        scanButton.setOnClickListener(v -> scanQrCode());
        infoButton.setOnClickListener(v -> showInfo());
    }

    private void scanQrCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan the QR code for the exam");
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    private void showInfo() {
        Toast.makeText(this, "This is an exam browser app.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                try {
                    // Decrypt the URL if needed
                    String decryptedUrl = AESUtils.decrypt(result.getContents());

                    // Start ExamActivity and pass the decrypted URL
                    Intent intent = new Intent(MainActivity.this, ExamActivity.class);
                    intent.putExtra("EXAM_URL", decryptedUrl);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "Invalid QR Code: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "QR Code scanning canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
