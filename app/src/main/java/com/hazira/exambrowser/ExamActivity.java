package com.hazira.exambrowser;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ExamActivity extends AppCompatActivity {

    private WebView webView;
    private boolean isPinned = false;

    private long backPressedTime = 0; // Tracks the time of the last back press
    private static final int BACK_PRESS_INTERVAL = 2000; // 2 seconds interval

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String examUrl = getIntent().getStringExtra("EXAM_URL");
        if (examUrl != null) {
            webView.loadUrl(examUrl);
            enableScreenPinning();
        } else {
            Toast.makeText(this, "No URL provided", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void enableScreenPinning() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startLockTask();
            isPinned = true;
            Toast.makeText(this, "App is now pinned", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Screen pinning is not supported on your device", Toast.LENGTH_LONG).show();
        }
    }

    private void showUnpinWarning() {
        new AlertDialog.Builder(this)
                .setTitle("Unpin Warning")
                .setMessage("If you unpin the app, it will be terminated, and you will lose your progress. Do you want to proceed?")
                .setPositiveButton("Yes", (dialog, which) -> terminateApp())
                .setNegativeButton("No", null)
                .show();
    }

    private void terminateApp() {
        Toast.makeText(this, "App is being terminated", Toast.LENGTH_SHORT).show();
        finishAndRemoveTask();
        System.exit(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !isAppPinned()) {
            terminateApp();
        }
    }

    private boolean isAppPinned() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            return activityManager.getLockTaskModeState() != ActivityManager.LOCK_TASK_MODE_NONE;
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isPinned) {
            terminateApp();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        if (backPressedTime + BACK_PRESS_INTERVAL > System.currentTimeMillis()) {
            // Exit the activity if back is pressed within the interval
            terminateApp();
        } else {
            // Show a warning message
            Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show();
        }

        // Update the back pressed time
        backPressedTime = System.currentTimeMillis();
    }

    
}
