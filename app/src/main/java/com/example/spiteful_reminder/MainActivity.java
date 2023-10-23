package com.example.spiteful_reminder;


import static com.example.spiteful_reminder.NewReminder.reference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Code to make the status bar black (Start)
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));
        //Code to make the status bar black (End)

        //Code for Notification channel

            String channelId = "channel_id"; // Choose a unique channel ID
            CharSequence channelName = "Channel Name"; // The name that will be displayed to the user
            String channelDescription = "Channel Description"; // A description for the channel
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            // Register the channel with the system; you can't change the importance or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

        ListView lv = findViewById(R.id.lv);
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter ad = new ArrayAdapter<String>(this,R.layout.reminder_item,R.id.tv,list);
        lv.setAdapter(ad);
        reference = FirebaseDatabase.getInstance().getReference().child("helper");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()){
                    helper info = child.getValue(helper.class);
                    String txt = info.getMemo() + "\n" + info.getTime() + "\n" + info.getDate();
                    list.add(txt);
                }
                ad.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //new reminder start
    public void addreminder(View view){
        Intent j = new Intent(MainActivity.this, NewReminder.class);
        startActivity(j);
    }
    //new reminder end

    //searchbar start
    public void search(View view){
        Intent i=new Intent(MainActivity.this,search.class);
        startActivity(i);
    }
    //searchbar end
}

