package com.zaidbinihtesham.seproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateGoal extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String TAG = "X1212";
    private static final String ONESIGNAL_APP_ID = "d704ffce-b306-4eba-b958-c3583bdec538";

    Spinner spinner;
    String category;
    EditText title;
    EditText description;
    EditText targetamount;
    Button creategoal;
    Spinner spinner2;
    String city;
    ImageView profile_image;
    DatabaseReference mDatabaseRef;
    StorageReference mStorageRef;
    Uri selectedImageUri_upload;
    ProgressBar simpleProgressBar;
    ImageButton gobackbutton;
    StorageTask muploadtask;



    private static final String[] paths = {"Education","Climate Change","Medical Support"};
    private static final String[] cities = {"Lahore", "Islamabad", "Rawalpindi", "Multan", "Faisalabad", "Karachi", "Hyderabad", "New York", "Washington DC", "Florida", "London"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);
        title=findViewById(R.id.title);
        profile_image=findViewById(R.id.profile_image);
        description=findViewById(R.id.description);
        targetamount=findViewById(R.id.targetamount);
        creategoal=findViewById(R.id.creategoalbutton);
        spinner2=findViewById(R.id.city);
        spinner = (Spinner)findViewById(R.id.spinner);
        simpleProgressBar=findViewById(R.id.simpleProgressBar);
        mDatabaseRef=FirebaseDatabase.getInstance().getReference("uploads");
        mStorageRef=FirebaseStorage.getInstance().getReference("uploads");
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(CreateGoal.this,
                android.R.layout.simple_spinner_item,paths);

        ArrayAdapter<String>adapter2 = new ArrayAdapter<String>(CreateGoal.this,
                android.R.layout.simple_spinner_item,cities);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Notif", "Notif", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        gobackbutton=findViewById(R.id.gobackbutton);


        gobackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CreateGoal.this,MainScreen_Tabs.class);
                startActivity(intent);
            }
        });


        creategoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("Title",title.getText().toString());
                user.put("Description",description.getText().toString());
                user.put("TargetAmount",targetamount.getText().toString());
                user.put("Category",category);
                user.put("Status","Active");
                user.put("city", city);


// Add a new document with a generated ID
                db.collection("GoalInformation")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });




                if(muploadtask!=null && muploadtask.isInProgress()){
                    Toast.makeText(CreateGoal.this,"File Already Uploading Please Wait",Toast.LENGTH_LONG).show();
                }
                else {
                    uploadfile(selectedImageUri_upload);
                }










                // OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
                // OneSignal Initialization
                // OneSignal.initWithContext(CreateGoal.this);
                // OneSignal.setAppId(ONESIGNAL_APP_ID);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(CreateGoal.this, "Generating Goal")
                        .setSmallIcon(R.drawable.tick)
                        .setContentTitle("Goal Created")
                        .setContentTitle("Goal created successfully and fundings will start to arrive soon.")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(CreateGoal.this);
                managerCompat.notify(1, builder.build());

                Intent intent=new Intent(CreateGoal.this,RecyclerViewList.class);
                startActivity(intent);
            }

        });


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });


    }



    private void uploadfile(Uri uri){
        if(uri!=null){
            StorageReference fileReference=mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(uri));
            muploadtask=fileReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl = uri.toString();
                                    Handler handler=new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            simpleProgressBar.setProgress(0);
                                        }
                                    },5000);

                                    Toast.makeText(CreateGoal.this,"Upload Successful",Toast.LENGTH_LONG).show();

                                    Upload upload = new Upload(downloadUrl,title.getText().toString().trim()
                                            ,description.getText().toString(),targetamount.getText().toString(),category.toString(),"Active", city.toString());
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(upload);
                                    title.setText("");
                                    description.setText("");
                                    targetamount.setText("");



                                }
                            });









                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateGoal.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress=(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            simpleProgressBar.setProgress((int) progress);

                        }
                    });

        }
        else{
            Toast.makeText(CreateGoal.this,"No File Selected",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {

                    Picasso.with(this).load(selectedImageUri).into(profile_image);
                    selectedImageUri_upload=selectedImageUri;

                }
            }
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        Spinner spinner = (Spinner) parent;
        Spinner spinner1 = (Spinner) parent;

        if (spinner.getId() == R.id.spinner) {
            switch (position) {
                case 0:
                    category=paths[0];
                    break;
                case 1:
                    category=paths[1];
                    break;
                case 2:
                    category=paths[2];
                    break;

            }
        }
        if (spinner1.getId() == R.id.city) {
            switch (position) {
                case 0:
                    city=cities[0];
                    break;
                case 1:
                    city=cities[1];
                    break;
                case 2:
                    city=cities[2];
                    break;
                case 3:
                    city=cities[3];
                    break;
                case 4:
                    city=cities[4];
                    break;
                case 5:
                    city=cities[5];
                    break;
                case 6:
                    city=cities[6];
                    break;
                case 7:
                    city=cities[7];
                    break;
                case 8:
                    city=cities[8];
                    break;
                case 9:
                    city=cities[9];
                    break;
                case 10:
                    city=cities[10];
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }


}