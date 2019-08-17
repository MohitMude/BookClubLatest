package com.example.android.bookclublatest;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookclublatest.HomePage.HomePageActivity;
import com.example.android.bookclublatest.SharedPref.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IssueBookDetailActivity extends AppCompatActivity {

    SharedPref  sharedPref;
    @BindView(R.id.description)
    TextView desc;
    @BindView(R.id.textView28)
    TextView tvBook;
    @BindView(R.id.det_author)
    TextView tvAuthor;
    @BindView(R.id.det_isbn)
    TextView tvIsbn;
    @BindView(R.id.det_pub)
    TextView tvPub;
    @BindView(R.id.det_tags)
    TextView tvTags;
    @BindView(R.id.btn_issue_book)
    Button issueBookButton;
    @BindView(R.id.myCoordinatorLayout)
    View coordinatorlayout;
    @BindView(R.id.textView29)
    TextView noCopies;
    @BindView(R.id.textView26)
    TextView title;
    @BindView(R.id.return_home)
    ImageView home;
    @BindView(R.id.det_book)
    TextView booktv;
    @BindView(R.id.issue_book_image)
    ImageView book_image;

    int send = 1;
    private static final String TAG = "IssueBookDetailActivity";
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_book_detail);
        sharedPref=new SharedPref(this);

        final String book = getIntent().getStringExtra("book");
        final String author = getIntent().getStringExtra("author");
        final String isbn = getIntent().getStringExtra("isbn");
        final String hardsofy = getIntent().getStringExtra("hardsofy");
        final String ism = getIntent().getStringExtra("ism");
        final String publisher = getIntent().getStringExtra("publisher");
        final String tags = getIntent().getStringExtra("tags");
        final String stat = getIntent().getStringExtra("status");
        final String no = getIntent().getStringExtra("no");
        final String url = getIntent().getStringExtra("url");
        final String descString = getIntent().getStringExtra("desc");
        ButterKnife.bind(this);
        tvBook.setText(stat);
        booktv.setText(book);
        tvAuthor.setText(author);
        //tvHardSofy.setText(hardsofy);
        tvIsbn.setText(isbn);
        //tvIsm.setText(ism);
        tvPub.setText(publisher);
        tvTags.setText(tags);
        //status.setText(stat);
        desc.setText(descString);
        noCopies.setText(no);
        Picasso.get().load(url).into(book_image);
        issueBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
                    showSnackBar();
                else
                {
                    String email=sharedPref.getEmail();
                    email=email.replace('.',',');

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Issue Requests").
                            child(email);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                if(dataSnapshot1.getKey().equals(isbn) )
                                {
                                    IssueBookDetailActivity.this.setSend(0);
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    if(send != 0)
                    {
                        FirebaseDatabase.getInstance().getReference().child("Issue Requests").child(email).child(isbn)
                                .child("Status").setValue("pending");
                        FirebaseDatabase.getInstance().getReference().child("Issue Requests").child(email).child(isbn)
                                .child("Name").setValue(book);
                        FirebaseDatabase.getInstance().getReference().child("Issue Requests").child(email).child(isbn)
                                .child("Timestamp").setValue(Calendar.getInstance().getTimeInMillis());
                        FirebaseDatabase.getInstance().getReference().child("Issue Requests").child(email).child(isbn)
                                .child("ISBN").setValue(isbn);
                        FirebaseDatabase.getInstance().getReference().child("Issue Requests").child(email).child(isbn)
                                .child("ISM").setValue(ism);
                        FirebaseDatabase.getInstance().getReference().child("Issue Requests").child(email).child(isbn)
                                .child("url").setValue(url);
                        FirebaseDatabase.getInstance().getReference().child("Issue Requests").child(email).child(isbn)
                                .child("desc").setValue(descString);

                        Toast.makeText(IssueBookDetailActivity.this, book + "requested for you", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(IssueBookDetailActivity.this, "You have already requested for this book please wait while we process your request", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        title.setText("Issue Book");
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IssueBookDetailActivity.this, HomePageActivity.class));
            }
        });
    }

    private void showSnackBar()
    {
        Snackbar snackbar = Snackbar.make(coordinatorlayout, "Verify your college e-mail id first.", Snackbar.LENGTH_INDEFINITE)
                .setAction(" Verify now", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendMail();
                    }
                });

        snackbar.setActionTextColor(Color.parseColor("#CEA100"));
        View view = snackbar.getView();
        TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#CEA100"));
        view.setBackgroundColor(Color.parseColor("#FFE588"));
        snackbar.show();
    }

    private void sendMail()
    {
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(IssueBookDetailActivity.this, "Verification Mail Has been Sent, It may Take few minutes to verify.", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(IssueBookDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setSend(int send) {
        this.send = send;
    }

    public int getSend() {
        return send;
    }
}
