package com.zaidbinihtesham.seproject;

import static com.google.android.gms.common.util.CollectionUtils.mapOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class RecyclerViewSpecificItem extends AppCompatActivity {

    TextView title_of_goal;
    TextView description_of_goal;
    TextView category_of_goal;
    TextView targetamount_of_goal;
    TextView status_of_goal;
    Button changegoalstatus;
    Button applypayment;
    Button generate_QR;
    final String[] Status_Value = {null};

    String title_get = null;
    public static String qr_title;
    String description_get = null;
    String category_get = null;
    String targetamount_get = null;
    String status_get = null;

    private void animatelogo() {

        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(10000);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.startAnimation(rotate);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_specific_item);
        title_of_goal = findViewById(R.id.title_of_goal);
        description_of_goal = findViewById(R.id.description_of_goal);
        category_of_goal = findViewById(R.id.category_of_goal);
        targetamount_of_goal = findViewById(R.id.targetamount_of_goal);
        changegoalstatus = findViewById(R.id.goal_status_change);
        status_of_goal = findViewById(R.id.status_of_goal);
        applypayment = findViewById(R.id.applypayment);
        generate_QR = findViewById(R.id.generate_QR);

        animatelogo();

        Intent intent = getIntent();


        title_get = intent.getStringExtra("Title");
        qr_title = title_get;
        description_get = intent.getStringExtra("Description");
        category_get = intent.getStringExtra("Category");
        targetamount_get = intent.getStringExtra("TargetAmount");
        status_get = intent.getStringExtra("Status");

        title_of_goal.setText(title_get);
        description_of_goal.setText(description_get);
        category_of_goal.setText(category_get);
        targetamount_of_goal.setText(targetamount_get);
        status_of_goal.setText(status_get);

        generate_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(RecyclerViewSpecificItem.this, generate_QR_Code.class);
               // startActivity(intent);
            }
        });

        changegoalstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference questionsRef = FirebaseDatabase.getInstance().getReference().child("uploads");
                questionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot questionsSnapshot) {
                        for (DataSnapshot questionSnapshot: questionsSnapshot.getChildren()) {
                            DataSnapshot name_of_goal_in_database=questionSnapshot.child("mName");
                            String value=name_of_goal_in_database.getValue().toString().toLowerCase(Locale.ROOT).trim();
                            String title_get=title_of_goal.getText().toString().toLowerCase(Locale.ROOT).trim();

                            if(value.equals(title_get)){

                                DataSnapshot status_of_goal_in_database=questionSnapshot.child("mStatus");
                                String status=status_of_goal_in_database.getValue().toString();
                                if(status.equals("Active")){
                                    status_of_goal_in_database.getRef().setValue("Not Active");
                                    status_of_goal.setText("Not Active");
                                }else{
                                    status_of_goal_in_database.getRef().setValue("Active");
                                    status_of_goal.setText("Active");
                                }


                            }

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
            }
        });

        applypayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status_of_goal.getText().toString().startsWith("A")) {
                   // Intent intent1 = new Intent(RecyclerViewSpecificItem.this, PaymentScreen.class);
                   // intent1.putExtra("Title", title_of_goal.getText().toString());
                   // intent1.putExtra("Target", targetamount_of_goal.getText().toString());
                   // startActivityForResult(intent1,1);
                } else {
                    Toast.makeText(RecyclerViewSpecificItem.this, "Goal Not Active", Toast.LENGTH_LONG).show();
                }



            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        title_of_goal = findViewById(R.id.title_of_goal);
        description_of_goal = findViewById(R.id.description_of_goal);
        category_of_goal = findViewById(R.id.category_of_goal);
        targetamount_of_goal = findViewById(R.id.targetamount_of_goal);
        status_of_goal = findViewById(R.id.status_of_goal);
        applypayment = findViewById(R.id.applypayment);


        title_of_goal.setText(title_get);
        description_of_goal.setText(description_get);
        category_of_goal.setText(category_get);
        status_of_goal.setText(status_get);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("GoalInformation")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String getTitle = (String) document.getData().get("Title");
                                if (getTitle.equals(title_of_goal.getText().toString())) {
                                    String targetAmount = (String) document.getData().get("TargetAmount");
                                    targetamount_of_goal.setText(targetAmount);
                                    targetamount_of_goal.setTextColor(Color.WHITE);

                                }


                            }
                        } else {
                            Log.w("TAGXAXAXAXA", "Error getting documents.", task.getException());
                        }
                    }
                });

    }



}


