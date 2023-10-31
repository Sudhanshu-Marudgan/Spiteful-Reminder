package com.example.spiteful_reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

public class editview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editview);

        //Code to make the status bar black (Start)
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));
        //Code to make the status bar black (End)

        TextView tv1 = (TextView) findViewById(R.id.ed1);
        TextView tv2 = (TextView) findViewById(R.id.sw1);
        TextView tv3 = (TextView) findViewById(R.id.sw2);
        TextView tv4 = (TextView) findViewById(R.id.sw3);

        tv1.setText(""+search.searchmemo);
        tv2.setText("Time: "+search.searchtime);
        tv3.setText("Date: "+search.searchdate);
        tv4.setText("Status: "+search.searchstatus);
    }

    public void done(View view){
        Intent i =  new Intent(editview.this,MainActivity.class);
        startActivity(i);
    }
}