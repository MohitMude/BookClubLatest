package com.example.android.bookclublatest.Member.ConfirmIssue;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookclublatest.Member.ConfirmIssue.ConfirmProceed.ProceedActivity;
import com.example.android.bookclublatest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmIssueActivity extends AppCompatActivity implements ConfirmIssueAdapter.Clicklistener {

    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference("Issue Requests");

    @BindView(R.id.issues_bar)
    ProgressBar progressBar;
    @BindView(R.id.issues_recycler)
    RecyclerView recyclerView;

    List<ConfirmIssueModel> list=new ArrayList<>();
    ConfirmIssueAdapter adapter;

    String email,name,isbn,ism,status,time,url;

    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_issue);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        title = (TextView) findViewById(R.id.textView26);
        title.setText("ISSUE REQUESTS");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                progressBar.setVisibility(View.VISIBLE);
                list.clear();
                for(DataSnapshot ds1:dataSnapshot.getChildren())
                {
                    email=ds1.getKey();
                    for(DataSnapshot ds2:ds1.getChildren())
                    {
                        status=ds2.child("Status").getValue().toString();
                        if(status.equals("pending"))
                        {
                            name=ds2.child("Name").getValue().toString();
                            time=ds2.child("Timestamp").getValue().toString();
                            isbn=ds2.child("ISBN").getValue().toString();
                            ism=ds2.child("ISM").getValue().toString();
                            url = ds2.child("url").getValue().toString();
                            list.add(new ConfirmIssueModel(email,isbn,name,time,ism,url));
                        }
                    }
                }
                progressBar.setVisibility(View.GONE);
                adapter=new ConfirmIssueAdapter(list,ConfirmIssueActivity.this,ConfirmIssueActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void proceed(int pos)
    {
        Intent intent=new Intent(this, ProceedActivity.class);
        intent.putExtra("Email",list.get(pos).getEmail());
        intent.putExtra("Book",list.get(pos).getName());
        intent.putExtra("isbn",list.get(pos).getIsbn());
        intent.putExtra("ism",list.get(pos).getIsm());
        intent.putExtra("url",list.get(pos).getUrl());
        finish();
        startActivity(intent);
    }
}
