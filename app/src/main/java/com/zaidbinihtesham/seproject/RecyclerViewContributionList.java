package com.zaidbinihtesham.seproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecyclerViewContributionList extends AppCompatActivity {
    RecyclerView rv;
    ArrayList<Contribution> ls=new ArrayList<>();
    RecyclerViewContributionAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_contribution_list);
        rv=findViewById(R.id.rv_get);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserContribution")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String getTitle = (String) document.getData().get("Title");
                                String getContribution=(String) document.getData().get("Contribution");
                                String getUsername=(String) document.getData().get("Username");
                                ls.add(new Contribution(getTitle,getContribution,getUsername));
                                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(RecyclerViewContributionList.this, 1);
                                rv.setLayoutManager(mLayoutManager);
                                adapter = new RecyclerViewContributionAdapter(ls, RecyclerViewContributionList.this);
                                rv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }}}});






    }
}
