package com.example.a4nature.HomeScreen.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a4nature.Authentification.LoginActivity;
import com.example.a4nature.Authentification.Profile;
import com.example.a4nature.HomeScreen.HomeActivity;
import com.example.a4nature.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Quests extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;

    private TextView text1, text2, text3;
    private Button btnSubmit;
    private View check_1, check_2, check_3;

    private ImageView QuestImage;
    public Uri problemImageUri;

    private Button btnAddIssue;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private FirebaseStorage storage;
    private StorageReference storageReference;


    private FirebaseAuth auth;
    private Random random;

    private DatabaseReference rootDatabaseref;



    ProgressBar progressBar;
    int counter=0;
    int ok1=0, ok2=0, ok3=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quests);

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

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        check_1 = (View) findViewById(R.id.check1);
        check_2 = (View) findViewById(R.id.check2);
        check_3 = (View) findViewById(R.id.check3);


        text1 = (TextView) findViewById(R.id.textView6);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewContactDialog("1");
            }
        });

        text2 = (TextView) findViewById(R.id.textView7);
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewContactDialog("2");
            }
        });


        text3 = (TextView) findViewById(R.id.textView8);
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewContactDialog("3");
            }
        });


        FirebaseDatabase.getInstance().getReference().child("Quests");

        int min=1, max=10;
        String aux1="0", aux2="0", aux3="0";

        random = new Random();


        int numar1 = random.nextInt((max - min) + 1) + min;
        aux1 = String.valueOf(numar1);
        FirebaseDatabase.getInstance().getReference().child("Quests").child(aux1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        text1.setText(snapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        int numar2 = random.nextInt((max - min) + 1) + min;
        while(numar2==numar1)
            numar2 = random.nextInt((max - min) + 1) + min;


        aux2 = String.valueOf(numar2);
        FirebaseDatabase.getInstance().getReference().child("Quests").child(aux2)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        text2.setText(snapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        int numar3 = random.nextInt((max - min) + 1) + min;
        while(numar3==numar1 || numar3==numar2)
            numar3 = random.nextInt((max - min) + 1) + min;

        aux3 = String.valueOf(numar3);
        FirebaseDatabase.getInstance().getReference().child("Quests").child(aux3)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        text3.setText(snapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



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
                Intent intent1 = new Intent(Quests.this, HomeActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_leaderboard:
                Intent intent6 = new Intent(Quests.this, LeaderBoard.class);
                startActivity(intent6);
                break;
            case R.id.nav_problems:
                Intent intent2 = new Intent(Quests.this, MapsActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_quests:
                break;
            case R.id.nav_profile:
                Intent intent4 = new Intent(Quests.this, Profile.class);
                startActivity(intent4);
                break;
            case R.id.nav_logout:
                auth.signOut();
                Intent intent3 = new Intent(Quests.this, LoginActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_feedback:
                Intent intent5 = new Intent(Quests.this, Feedback.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START); return true;
    }

    public void prog() {
        progressBar =(ProgressBar) findViewById(R.id.progressBar);

        final Timer t= new Timer();
        counter += 33;
        TimerTask tt= new TimerTask() {
            @Override
            public void run() {
                progressBar.setProgress(counter);

                if(counter==99)
                {
                    t.cancel();
                    //Toast.makeText(Quests.this, "Congratulations! You've completed all your quests!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        t.schedule(tt, 0, 99);
    }


    private void choosePicture () {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    public void createNewContactDialog (String id) {

        dialogBuilder = new AlertDialog.Builder(this);
        final View PopOut = getLayoutInflater().inflate(R.layout.insert_pic, null);

        btnSubmit = (Button) PopOut.findViewById(R.id.btnSubmit);

        QuestImage = (ImageView) PopOut.findViewById(R.id.QuestImage);
        QuestImage.setImageResource(R.drawable.camera);

        dialogBuilder.setView(PopOut);
        dialog = dialogBuilder.create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        QuestImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                realtimeDatabaseUpload(id, userId);
                dialog.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            problemImageUri = data.getData();
            QuestImage.setImageURI(problemImageUri);
        }
    }



    void realtimeDatabaseUpload(String nr, String userId) {


        if(nr=="1")
        {
            ok1++;
            if(ok1==1)
            {

                FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("score").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("score").setValue(Integer.parseInt(snapshot.getValue().toString()) - 30);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                check_1.setVisibility(View.VISIBLE);
                prog();
            }
        }

        if(nr=="2")
        {
            ok2++;
            if(ok2==1)
            {
                FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("score").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("score").setValue(30 + Integer.parseInt(snapshot.getValue().toString()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                check_2.setVisibility(View.VISIBLE);
                prog();
            }
        }

        if(nr=="3")
        {
            ok3++;
            if(ok3==1)
            {
                FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("score").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("score").setValue(30 + Integer.parseInt(snapshot.getValue().toString()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                check_3.setVisibility(View.VISIBLE);
                prog();
            }
        }


        StorageReference riversRef = storageReference.child("images/" + userId + "_" + nr);

        riversRef.putFile(problemImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Quests.this, "Image was uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Quests.this, "Image not uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }
}