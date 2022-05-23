package com.zaidbinihtesham.seproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaymentScreen extends AppCompatActivity {

    private static final String TAG = "XAXAXAXAXA";
    TextView title_of_goal;
    TextView target_of_goal;
    EditText YourContribution;
    Button paynow;
    DatabaseReference mDatabaseRef;

    String CHANNEL_ID="Channel 1";


    private void animatelogo(){

        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(10000);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        ImageView image= (ImageView) findViewById(R.id.imageView);
        image.startAnimation(rotate);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screen2);
        Intent intent = getIntent();
        animatelogo();
        String title = intent.getStringExtra("Title");
        String target = intent.getStringExtra("Target");
        title_of_goal = findViewById(R.id.title_of_goal);
        title_of_goal.setText(title);
        target_of_goal = findViewById(R.id.targetamount_of_goal);
        target_of_goal.setText(target);
        YourContribution = findViewById(R.id.YourContribution);
        paynow = findViewById(R.id.paynow);

        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!YourContribution.getText().toString().equals("")) {

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PaymentScreen.this);
                    String username = preferences.getString("Username", "");


                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("GoalInformation")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String getTitle = (String) document.getData().get("Title");

                                            if (getTitle.equals(title)) {
                                                int target_int = Integer.parseInt(target_of_goal.getText().toString());
                                                int contribution = Integer.parseInt(YourContribution.getText().toString());
                                                int amount = target_int - contribution;

                                                db.collection("GoalInformation").document(document.getId())
                                                        .update("TargetAmount", String.valueOf(amount));


                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                // Create a new user with a first and last name
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("Title", title_of_goal.getText().toString());
                                                user.put("Contribution", YourContribution.getText().toString());
                                                user.put("Username", username);


// Add a new document with a generated ID
                                                db.collection("UserContribution")
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
                                            }


                                        }
                                    } else {
                                        Log.w("TAGXAXAXAXA", "Error getting documents.", task.getException());
                                    }
                                }
                            });


                }

                else{

                    YourContribution.setError("Enter Amount Please...");
                }

                Intent intent = new Intent(PaymentScreen.this, Payment_Activity.class);
                intent.putExtra("price", YourContribution.getText().toString());
                startActivityForResult(intent, 0);
            }

        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == 0 && resultCode == RESULT_OK) {
            // Get String data from Intent
            String ResponseCode = data.getStringExtra("pp_ResponseCode");
            System.out.println("DateFn: ResponseCode:" + ResponseCode);
            if (ResponseCode.equals("000")) {
                Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();


                DatabaseReference questionsRef = FirebaseDatabase.getInstance().getReference().child("uploads");
                questionsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot questionsSnapshot) {
                        for (DataSnapshot questionSnapshot: questionsSnapshot.getChildren()) {
                            DataSnapshot name_of_goal_in_database=questionSnapshot.child("mName");
                            String value=name_of_goal_in_database.getValue().toString().toLowerCase(Locale.ROOT).trim();
                            String title_get=title_of_goal.getText().toString().toLowerCase(Locale.ROOT).trim();

                            if(value.equals(title_get)){

                                DataSnapshot amount_of_goal_in_database=questionSnapshot.child("mTargetAmount");
                                String amount=amount_of_goal_in_database.getValue().toString();
                                String val1=target_of_goal.getText().toString();
                                String val2=YourContribution.getText().toString();
                                int val1_int=Integer.valueOf(val1);
                                int val2_int=Integer.valueOf(val2);
                                int newval=val1_int-val2_int;

                                amount_of_goal_in_database.getRef().setValue(String.valueOf(newval));
                                Toast.makeText(PaymentScreen.this,"Updated In Realtime From "+amount+" To "+String.valueOf(newval),Toast.LENGTH_LONG).show();

                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });






                Intent newintent=new Intent(getApplicationContext(),RecyclerViewSpecificItem.class);
                PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,newintent,PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                        .setContentIntent(pendingIntent)
                        .setContentTitle(title_of_goal.getText().toString())
                        .setContentText("Target Of "+title_of_goal.getText().toString()+" Has Been Updated To "+target_of_goal.getText().toString())
                        .setSmallIcon(R.drawable.notification_icon);

                NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,"This Is My First Notification",NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(notificationChannel);
                notificationManager.notify(0,builder.build());



            }
            finish();
        }
    }
}