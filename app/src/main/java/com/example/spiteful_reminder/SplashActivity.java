package com.example.spiteful_reminder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Code to make the status bar black
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));

        // Configure the window for full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the content view to your splash screen layout
        setContentView(R.layout.activity_splashactivity);

        // Use a Handler to transition to the main activity after a delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an intent to launch the MainActivity
                Intent i = new Intent(SplashActivity.this, MainActivity.class);

                // Start the new activity
                startActivity(i);

                // Finish the current activity (splash activity)
                finish();
            }
        }, 1000); // 2000 milliseconds (2 seconds) delay
    }
}
