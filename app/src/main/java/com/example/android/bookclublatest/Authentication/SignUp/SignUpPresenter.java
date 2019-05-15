package com.example.android.bookclublatest.Authentication.SignUp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bookclublatest.Base.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpPresenter<V extends SignUpContract.View> extends BasePresenter<V> implements SignUpContract.Presenter<V> {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private static final String TAG = "SignUpPresenter";
    //SignUpContract.View view = getMvpView();
    @Override
    public void CreateAccount(String name, String email, String admissionnumber, String phonenumber, String password) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(); // root node.


        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    getMvpView().showSignUpResult();

                } else {
                    getMvpView().erroronLoading(task.getException().getMessage());
                }

            }
        });


        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Log.d(TAG, "onComplete: ");
                }
                else
                {
                    Log.d(TAG, "onFailed: ");
                }
            }
        });


        //String userId = firebaseAuth.getCurrentUser().getUid();

        SignUpModel signUpModel = new SignUpModel(name, email, admissionnumber, phonenumber, "student");
        String userIdd;

        databaseReference.child("users").child("user1").setValue(signUpModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Log.e("node", "Uploaded");
                else
                    Log.e("node", task.getException().getMessage());
            }
        });


    }
}
