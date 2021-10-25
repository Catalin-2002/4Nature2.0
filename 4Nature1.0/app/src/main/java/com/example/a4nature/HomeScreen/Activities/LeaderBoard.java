package com.example.a4nature.HomeScreen.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a4nature.Authentification.LoginActivity;
import com.example.a4nature.Authentification.Profile;
import com.example.a4nature.Authentification.User;
import com.example.a4nature.HomeScreen.HomeActivity;
import com.example.a4nature.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<String> myList;
    private ListView myListView;


    DatabaseReference database;

    /**
     * Navbar
     */

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);
        menu.findItem(R.id.nav_profile).setVisible(false);

        auth = FirebaseAuth.getInstance();

        myList=new ArrayList<String>();

        myListView = (ListView) findViewById(R.id.listview1);


        ArrayAdapter<String> myArrayAdapter= new ArrayAdapter<String>
                (LeaderBoard.this, android.R.layout.simple_list_item_1,myList);
        myListView.setAdapter(myArrayAdapter);

        database= FirebaseDatabase.getInstance().getReference("Users");



        Query query= FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("score");



        ValueEventListener valueEventListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myList.clear();
                myList.add("1234");
                if(snapshot.exists()) {
                    for (DataSnapshot snapshot1: snapshot.getChildren()) {
                        User user= snapshot1.getValue(User.class);
                        myList.add(user.getFullName() + "   " + (-user.getScore()));
                    }
                    myArrayAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        query.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent intent1 = new Intent(LeaderBoard.this, HomeActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_leaderboard:
                break;
            case R.id.nav_problems:
                Intent intent2 = new Intent(LeaderBoard.this, MapsActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_quests:
                Intent intent6 = new Intent(LeaderBoard.this, Quests.class);
                startActivity(intent6);
                break;
            case R.id.nav_profile:
                Intent intent4 = new Intent(LeaderBoard.this, Profile.class);
                startActivity(intent4);
                break;
            case R.id.nav_logout:
                auth.signOut();
                Intent intent3 = new Intent(LeaderBoard.this, LoginActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_feedback:
                Intent intent5 = new Intent(LeaderBoard.this, Feedback.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START); return true;
    }
}