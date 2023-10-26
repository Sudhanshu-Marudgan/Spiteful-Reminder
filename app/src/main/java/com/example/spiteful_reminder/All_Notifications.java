package com.example.spiteful_reminder;

import static com.example.spiteful_reminder.NewReminder.reference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class All_Notifications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notifications);

        //Code to make the status bar black (Start)
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));
        //Code to make the status bar black (End)

        ListView lv = findViewById(R.id.lv);
        final ArrayList<String> list = new ArrayList<>();
        List<String> itemKeys = new ArrayList<>();
        final ArrayAdapter ad = new ArrayAdapter<String>(this,R.layout.listview_items,R.id.tv,list);
        lv.setAdapter(ad);

        reference = FirebaseDatabase.getInstance().getReference().child("helper");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<helper> pendingHelpers = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()){
                    helper info = child.getValue(helper.class);
                    String txt = info.getMemo() + '\n' + info.getTime() + '\n' + info.getDate() + '\n' + info.getStatus();
                    list.add(txt);
                    itemKeys.add(child.getKey());
                    pendingHelpers.add(info);

                    //Sorting Code
                    Collections.sort(pendingHelpers, new Comparator<helper>() {
                        @Override
                        public int compare(helper helper1, helper helper2) {
                            // Parse the date and time strings from your Helper objects
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                            Date date1, date2;
                            try {
                                date1 = format.parse(helper1.getDate() + " " + helper1.getTime());
                                date2 = format.parse(helper2.getDate() + " " + helper2.getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                                Log.e("DateParsingError", "Error parsing date: " + e.getMessage());
                                return 0; // Handle parsing error
                            }

                            // Compare the dates
                            return date1.compareTo(date2);
                        }
                    });

                    list.clear();
                    itemKeys.clear();

                    // Re-add the sorted items to your list and update the keys
                    for (helper helper : pendingHelpers) {
                        txt = helper.getMemo() + '\n' + helper.getTime() + '\n' + helper.getDate() + '\n' + helper.getStatus();
                        list.add(txt);
                        itemKeys.add(child.getKey());
                    }

                }
                ad.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void nav(View view){
        Intent i = new Intent(this, Navigation.class);
        startActivity(i);
    }
}