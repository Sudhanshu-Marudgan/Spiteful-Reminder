package com.example.spiteful_reminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class search extends Activity {
    private DataSnapshot Snapshot;
    public static String searchmemo;
    public static String searchtime;
    public static String searchdate;
    public static String searchstatus;
    private SearchView searchView;
    private ListView listView;
    private ArrayAdapter<String> adapter;
//    private DatabaseReference databaseReference; // Firebase Realtime Database reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Code to make the status bar black
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));

        searchView = findViewById(R.id.sv1);
        listView = findViewById(R.id.lv1);

        // Initialize the Firebase Realtime Database reference
        NewReminder.reference= FirebaseDatabase.getInstance().getReference().child("helper"); // Replace with your Firebase data node

        // Create an ArrayAdapter and set it to the ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        // Set a text change listener for the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the items based on the user's input
                queryFirebase(newText);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String memo = adapter.getItem(position); // Get the selected memo (which is a String)

                // Find the corresponding helper object in your data
                for (DataSnapshot itemSnapshot : Snapshot.getChildren()) {
                    String memoFromSnapshot = itemSnapshot.child("memo").getValue(String.class);

                    if (memo.equals(memoFromSnapshot)) {
                        String time = itemSnapshot.child("time").getValue(String.class);
                        String date = itemSnapshot.child("date").getValue(String.class);
                        String status = itemSnapshot.child("status").getValue(String.class);

                        searchmemo = memo;
                        searchtime = time;
                        searchdate = date;
                        searchstatus = status;
                        Intent i = new Intent(search.this, editview.class);
                        startActivity(i);

//                        String toastMessage = "Memo: " + memo + "\nTime: " + time + "\nDate: " + date + "\nStatus: " + status;
//                        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });
    }




    private void queryFirebase(String query) {
        // Clear the existing data
        adapter.clear();

        // Query Firebase for matching records
        NewReminder.reference.orderByChild("memo").startAt(query).endAt(query + "\uf8ff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Snapshot = dataSnapshot;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //String item = snapshot.child("memo").getValue(String.class);
                            String memo = snapshot.child("memo").getValue(String.class);
                            String time = snapshot.child("time").getValue(String.class);
                            String date = snapshot.child("date").getValue(String.class);
                            String status = snapshot.child("status").getValue((String.class));
                            helper item = new helper(memo, time, date, status);
                            adapter.add(memo);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Handle error
                    }
                });
    }
}
