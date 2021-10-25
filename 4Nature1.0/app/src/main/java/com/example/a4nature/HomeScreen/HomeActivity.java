package com.example.a4nature.HomeScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.a4nature.Authentification.LoginActivity;
import com.example.a4nature.Authentification.Profile;
import com.example.a4nature.HomeScreen.Activities.Feedback;
import com.example.a4nature.HomeScreen.Activities.LeaderBoard;
import com.example.a4nature.HomeScreen.Activities.MapsActivity;
import com.example.a4nature.HomeScreen.Activities.Quests;
import com.example.a4nature.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        auth = FirebaseAuth.getInstance();
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
            case R.id.nav_home: break;
            case R.id.nav_leaderboard:
                Intent intent1 = new Intent(HomeActivity.this, LeaderBoard.class);
                startActivity(intent1);
                break;
            case R.id.nav_problems:
                Intent intent2 = new Intent(HomeActivity.this, MapsActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_quests:
                Intent intent6 = new Intent(HomeActivity.this, Quests.class);
                startActivity(intent6);
                break;
            case R.id.nav_profile:
                Intent intent4 = new Intent(HomeActivity.this, Profile.class);
                startActivity(intent4);
                break;
            case R.id.nav_logout:
                auth.signOut();
                Intent intent3 = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_feedback:
                Intent intent5 = new Intent(HomeActivity.this, Feedback.class);
                startActivity(intent5);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START); return true;
    }
}