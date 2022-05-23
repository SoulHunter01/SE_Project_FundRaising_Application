package com.zaidbinihtesham.seproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginScreen extends AppCompatActivity {

    private void animatelogo(){

        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(10000);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        ImageView image= (ImageView) findViewById(R.id.imageview_logo);
        image.startAnimation(rotate);


    }

    private static final String TAG = null;
    FirebaseAuth mAuth;
    EditText username;
    EditText password;
    Button loginbutton;
    TextView registrationintent;
    TextView forgotpassword;
    ImageView logoimageview;
    Button fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        forgotpassword=findViewById(R.id.forgotpassword);
        animatelogo();
        mAuth = FirebaseAuth.getInstance();
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        loginbutton=findViewById(R.id.loginbutton);
        registrationintent=findViewById(R.id.registrationintent);
        registrationintent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginScreen.this,SignUpScreen.class);
                startActivity(intent);
            }
        });


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginScreen.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Username",username.getText().toString());
                editor.apply();


                if(!username.getText().toString().equals("") || !password.getText().toString().equals("")) {
                    mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(LoginScreen.this, MainScreen_Tabs.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginScreen.this, "Credentials Not Correct", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("123", "Failed To Authenticate");
                            Toast.makeText(LoginScreen.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else{
                    username.setError("Fill In Username");
                    password.setError("Fill In Password");
                }

            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginScreen.this,ForgotPassword.class);
                startActivity(intent);
            }
        });



    }
}