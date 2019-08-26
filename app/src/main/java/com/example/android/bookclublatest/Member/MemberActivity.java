package com.example.android.bookclublatest.Member;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bookclublatest.Faq.AddFaqActivity;
import com.example.android.bookclublatest.HomePage.MainSliderAdapter;
import com.example.android.bookclublatest.HomePage.PicassoImageLoadingService;
import com.example.android.bookclublatest.Member.AddBook.AddBookActivity;
import com.example.android.bookclublatest.Member.ConfirmIssue.ConfirmIssueActivity;
import com.example.android.bookclublatest.Member.ConfirmReturn.ConfirmActivity;
import com.example.android.bookclublatest.Member.RequestedBooks.RequestBooksActivity;
import com.example.android.bookclublatest.R;
import com.example.android.bookclublatest.RemoveBookActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import ss.com.bannerslider.Slider;

public class MemberActivity extends AppCompatActivity {

    @BindView(R.id.request_confirm)
    Button request_book;
    @BindView(R.id.issue_confirm)
    Button issue_book;
    @BindView(R.id.return_confirm)
    Button return_book;
    @BindView(R.id.addbook)
    Button addBook;
    @BindView(R.id.remove_book)
    Button remove_book;
    @BindView(R.id.post_faqs)
    Button post_faqs;
    @BindView(R.id.textView26)
    TextView title;
    @BindView(R.id.return_home)
    ImageView home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        ButterKnife.bind(this);

        title.setText("Member Portal");
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });


        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MemberActivity.this, AddBookActivity.class));
            }
        });

        issue_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MemberActivity.this, ConfirmIssueActivity.class));
            }
        });
        return_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MemberActivity.this, ConfirmActivity.class));
            }
        });
        request_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MemberActivity.this, RequestBooksActivity.class));
            }
        });
        remove_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MemberActivity.this, RemoveBookActivity.class));
            }
        });
        post_faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MemberActivity.this, AddFaqActivity.class));
            }
        });
    }
}
