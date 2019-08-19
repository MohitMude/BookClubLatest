package com.example.android.bookclublatest.HomePage.Return;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.bookclublatest.HomePage.History.HistoryModel;
import com.example.android.bookclublatest.HomePage.HomePageActivity;
import com.example.android.bookclublatest.R;
import com.example.android.bookclublatest.SharedPref.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class ReturnActivity extends AppCompatActivity implements ReturnAdapter.Clicklistener
{
    @BindView(R.id.no_return_layout)
    RelativeLayout relativeLayout;
    @BindView(R.id.return_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.return_home)
    ImageView home;

    SharedPref sharedPref;
    List<HistoryModel> list=new ArrayList<>();
    ReturnAdapter adapter;
    String email;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);
        ButterKnife.bind(this);
        sharedPref=new SharedPref(this);
        email=sharedPref.getEmail();
        email=email.replace('.',',');

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference("Issue History");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if(ds.getKey().equals(email))
                    {
                        for(DataSnapshot ds2:ds.getChildren())
                        {
                            String bookname,isbn,issue_date,return_date,status,ism,url;
                            bookname=ds2.child("bookname").getValue().toString();
                            isbn=ds2.child("isbn").getValue().toString();
                            ism=ds2.child("ism").getValue().toString();
                            issue_date=ds2.child("issue_date").getValue().toString();
                            return_date=ds2.child("return_date").getValue().toString();
                            status=ds2.child("status").getValue().toString();
                            url = ds2.child("url").getValue().toString();
                            HistoryModel model=new HistoryModel(bookname,isbn,ism,issue_date,return_date,status,url);
                            if(model.getStatus().equals("Not Returned"))
                                list.add(model);
                        }
                    }
                }
                if (list.size()==0)
                    relativeLayout.setVisibility(View.VISIBLE);
                else {
                    relativeLayout.setVisibility(View.GONE);
                    adapter = new ReturnAdapter(ReturnActivity.this, list, ReturnActivity.this);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void proceed(int pos) {

        String isbn=list.get(pos).getIsbn();
        String book=list.get(pos).getBookname();
        String issuedate=list.get(pos).getIssue_date();
        String returndtae=list.get(pos).getReturn_date();
        String ismcode=list.get(pos).getIsmcode();
        String url = list.get(pos).getUrl();
        final String status="pending";

        HistoryModel model=new HistoryModel(book,isbn,ismcode,issuedate,returndtae,status,url);

        //Create a request to return
        FirebaseDatabase.getInstance().getReference().child("Return Requests").child(email).child(ismcode).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toasty.success(ReturnActivity.this, "Your Request Has Been Recieved, Return the Book To the Club", Toast.LENGTH_LONG,false).show();
                    startActivity(new Intent(ReturnActivity.this, HomePageActivity.class));
                    finish();
                }
                else
                {
                    Toasty.error(ReturnActivity.this, "Something went wrong, try again", Toast.LENGTH_SHORT,true).show();
                }
            }
        });
    }
}
