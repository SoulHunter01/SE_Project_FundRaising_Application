package com.zaidbinihtesham.seproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

public class MainScreen_Tabs extends AppCompatActivity {
    Button creategoalbutton;
    Button ChangeActiveStatus;
    Button btn_scan;
    Button logout;
    Button show_ad;
    Button seecontributions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_tabs);
        creategoalbutton=findViewById(R.id.creategoalbutton);
        ChangeActiveStatus=findViewById(R.id.ChangeActiveStatus);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        logout=(Button) findViewById(R.id.logout);
        seecontributions=findViewById(R.id.seecontributions);

        creategoalbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainScreen_Tabs.this,CreateGoal.class);
                startActivity(intent);
            }
        });

        ChangeActiveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(MainScreen_Tabs.this,RecyclerViewList.class);
//                startActivity(intent);
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(MainScreen_Tabs.this,Qr_Scan_Code.class);
//                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
//                Intent intent=new Intent(MainScreen_Tabs.this,LoginScreen.class);
//                startActivity(intent);
            }
        });

        seecontributions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(MainScreen_Tabs.this,RecyclerViewContributionList.class);
//                startActivity(intent);
            }
        });


    }
}