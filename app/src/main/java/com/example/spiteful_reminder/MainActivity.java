package com.example.spiteful_reminder;


import static com.example.spiteful_reminder.NewReminder.memo;
import static com.example.spiteful_reminder.NewReminder.reference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ThemedSpinnerAdapter;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
        // Define a list to store keys
        List<String> itemKeys = new ArrayList<>();

         final ArrayAdapter ad = new ArrayAdapter<String>(this,R.layout.reminder_item,R.id.tv,list){
            @NonNull
            @Override
            public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){
                View view = super.getView(position,convertView,parent);
                final CheckBox checkBox = view.findViewById(R.id.tv);

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            String itemKey = itemKeys.get(position);
                            HashMap user = new HashMap<>();
                            user.put("status","Completed");
                            reference.child(itemKey).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    Toast.makeText(MainActivity.this, "Reminder Completed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent refresh = getIntent();
                            startActivity(refresh);

                        }
                    }
                });
                return view;
            }

        };
        lv.setAdapter(ad);

        //get data from database and add in listview (start)
        reference = FirebaseDatabase.getInstance().getReference().child("helper");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<helper> pendingHelpers = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()){
                    helper info = child.getValue(helper.class);
                    if(info.getStatus().equals("Pending")){
                        String txt = info.getMemo();
//                        String txt = info.getMemo() + "\n" + info.getTime() + "\n" + info.getDate();
                        list.add(txt);
                        itemKeys.add(child.getKey());
                        pendingHelpers.add(info);

                        //sorting code (start)
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
                                    Toast.makeText(MainActivity.this, "Parsing Error!", Toast.LENGTH_SHORT).show();
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
                            txt = helper.getMemo();
                            list.add(txt);
                            itemKeys.add(child.getKey());
                        }
                        //sorting code (Ends)
                    }
                    else{

                    }

                }
                // Create a custom comparator to sort by date and time

                ad.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //get data from database and add in listview (end)


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

    public void nav(View view){
        Intent i = new Intent(MainActivity.this, Navigation.class);
        startActivity(i);
    }

}

