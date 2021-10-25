package com.example.a4nature.HomeScreen.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a4nature.HomeScreen.HomeActivity;
import com.example.a4nature.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /**
     * Maps activity
     */
    private GoogleMap mMap;

    private FloatingActionButton btnAddIssue;

    private DatabaseReference database;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    CheckBox cbAchieved;

    protected LocationManager locationManager;

    /**
     * Problem pop-up
     */

    private EditText etName;
    private AutoCompleteTextView etAutoCategory;
    private EditText etMultilineDescription;
    private Button btnSubmit;

    private ImageView ivProblemImage;
    public Uri problemImageUri;

    private String[] categories = {"Garbage", "Polution"};

    int contor = 0;

    /**
     * Solution pop-up
     */

    private TextView title;
    private TextView category;
    private TextView description;
    private ImageView ivExProblem;
    private ImageView ivSolution;
    private Button btnSubmitSolution;
    private Uri solutionImageUri;

    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        /**
         * Initialising the map
         */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance().getReference("Problems");

        /**
         * Open pop-up
         */
        cbAchieved = (CheckBox) findViewById(R.id.cbAchieved);
        cbAchieved.setChecked(true);

        btnAddIssue = (FloatingActionButton) findViewById(R.id.btnAddIssue);
        btnAddIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewContactDialog();
            }
        });

        cbAchieved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMap.clear();

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            for (DataSnapshot problems : snapshot.getChildren()) {
                                Problem currentProblem = problems.getValue(Problem.class);
                                if (currentProblem.isArchievedByAdmin() == false || cbAchieved.isChecked() == true) {
                                    LatLng coordinates = new LatLng(currentProblem.getLatitude(), currentProblem.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(coordinates).title(currentProblem.getName()));
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };

                database.addListenerForSingleValueEvent(valueEventListener);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Map<String, String> mapa = new HashMap<String, String>();
        mapa.clear();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot problems : snapshot.getChildren()) {
                        Problem currentProblem = problems.getValue(Problem.class);
                        LatLng coordinates = new LatLng(currentProblem.getLatitude(), currentProblem.getLongitude());
                        mapa.put(currentProblem.getName(), problems.getKey());
                        mMap.addMarker(new MarkerOptions().position(coordinates).title(currentProblem.getName()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        database.addListenerForSingleValueEvent(valueEventListener);

        /**
         * Aici  trb sa ia pozitia curenta
         */


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markerTitle = marker.getTitle();
                String desiredFirebase = mapa.get(markerTitle);
                try {
                    createSolutionPopUpDialog(desiredFirebase);
                } catch (Error e) {
                    Toast.makeText(MapsActivity.this, "An error occured. Please wait 30seconds then continue. Thank you", Toast.LENGTH_SHORT);
                }

                return false;
            }
        });
    }

    public void createNewContactDialog() {
        /**
         * Pop-up window declaration and initializations
         */
        try {
            contor = 1;
            dialogBuilder = new AlertDialog.Builder(this);
            final View problemPopOut = getLayoutInflater().inflate(R.layout.problem_popup, null);

            etName = (EditText) problemPopOut.findViewById(R.id.etName);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, categories);
            etAutoCategory = (AutoCompleteTextView) problemPopOut.findViewById(R.id.etAutoCategory);
            etAutoCategory.setThreshold(1);
            etAutoCategory.setAdapter(adapter);

            etMultilineDescription = (EditText) problemPopOut.findViewById(R.id.etMultilineDescription);

            btnSubmit = (Button) problemPopOut.findViewById(R.id.btnSubmit);

            ivProblemImage = (ImageView) problemPopOut.findViewById(R.id.ivProblemImage);
            ivProblemImage.setImageResource(R.drawable.camera);

            dialogBuilder.setView(problemPopOut);
            dialog = dialogBuilder.create();

            dialog.show();

            /**
             * Set image
             */
            ivProblemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choosePicture();
                }
            });


            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = etName.getText().toString().trim();
                    String category = etAutoCategory.getText().toString().trim();
                    String description = etMultilineDescription.getText().toString().trim();

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String randomKey = UUID.randomUUID().toString();

                    String id = userId + "_" + randomKey;

                    double latitude = 45;
                    double longitude = 45;

                    realtimeDatabaseUpload(name, category, description, latitude, longitude, id);
                }
            });

        } catch (Error e) {
            Toast.makeText(MapsActivity.this, "An error occured. Please wait 30seconds then continue. Thank you", Toast.LENGTH_SHORT);
        }
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if(contor == 1) {
                problemImageUri = data.getData();
                ivProblemImage.setImageURI(problemImageUri);
                scaleImage(ivProblemImage);
            } else if(contor == 2) {
                solutionImageUri = data.getData();
                ivSolution.setImageURI(solutionImageUri);
                scaleImage(ivSolution);
            }
        }
    }

    void realtimeDatabaseUpload(String name, String category, String description, double latitude, double longitude, String id) {

        StorageReference riversRef = storageReference.child("images/").child(id).child("Problem");

        riversRef.putFile(problemImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadPhotoUrl) {
                        Problem problem = new Problem(name, category, description, downloadPhotoUrl.toString(), " ", latitude, longitude, false);
                        FirebaseDatabase.getInstance().getReference().child("Problems").child(id).setValue(problem)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Snackbar.make(findViewById(android.R.id.content), "Problem uploaded", Snackbar.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(findViewById(android.R.id.content), "Image not uploaded", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    void createSolutionPopUpDialog(String id) {
        contor = 2;
        dialogBuilder = new AlertDialog.Builder(this);
        final View solutionPopUp = getLayoutInflater().inflate(R.layout.solution_popup, null);

        title = (TextView) solutionPopUp.findViewById(R.id.tvName);
        category = (TextView) solutionPopUp.findViewById(R.id.tvCategory);
        description = (TextView) solutionPopUp.findViewById(R.id.tvDescription);

        ivExProblem = (ImageView) solutionPopUp.findViewById(R.id.ivExProblem);
        ivExProblem.setImageResource(R.drawable.camera);
        ivSolution = (ImageView) solutionPopUp.findViewById(R.id.ivSolution);
        ivSolution.setImageResource(R.drawable.camera);

        btnSubmitSolution = (Button) solutionPopUp.findViewById(R.id.btnSubmitSolution);

        FirebaseDatabase.getInstance().getReference().child("Problems").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    Problem problemDisplay = snapshot.getValue(Problem.class);
                    title.setText(problemDisplay.getName());
                    category.setText(problemDisplay.getCategory());
                    description.setText(problemDisplay.getDescription());
                    String imageUrl = problemDisplay.getPhotoProblemId();

                    if(problemDisplay.isArchievedByAdmin() == true) {
                        btnSubmitSolution.setVisibility(View.INVISIBLE);

                        new DownLoadImageTask(ivSolution).execute(problemDisplay.getPhotoSolutionId());
                    }
                    new DownLoadImageTask(ivExProblem).execute(imageUrl);
                } catch (Error e) {
                    Toast.makeText(MapsActivity.this, "An error occured. Please wait 30seconds then continue. Thank you", Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        dialogBuilder.setView(solutionPopUp);
        dialog = dialogBuilder.create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        ivSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        btnSubmitSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSoltuion(id);
            }
        });
    }

    void addSoltuion(String id) {
        StorageReference riversRef = storageReference.child("images/").child(id).child("Solution");

        riversRef.putFile(solutionImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Snackbar.make(findViewById(android.R.id.content), "Image uploaded", Snackbar.LENGTH_LONG).show();
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri getSolutionUri) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("score").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("score").setValue(Integer.parseInt(snapshot.getValue().toString()) - 50);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        FirebaseDatabase.getInstance().getReference().child("Problems").child(id).child("archievedByAdmin").setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("Problems").child(id).child("photoSolutionId").setValue(getSolutionUri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(findViewById(android.R.id.content), "Image not uploaded", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();

                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }


        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
            scaleImage(imageView);
        }
    }

    private void scaleImage(ImageView view) throws NoSuchElementException  {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        try {
            Drawable drawing = view.getDrawable();
            bitmap = ((BitmapDrawable) drawing).getBitmap();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) {
            // Check bitmap is Ion drawable
        }

        // Get current dimensions AND the desired bounding box
        int width = 0;

        try {
            width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        int height = bitmap.getHeight();
        int bounding = dpToPx(200);
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        Log.i("Test", "done");
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
}