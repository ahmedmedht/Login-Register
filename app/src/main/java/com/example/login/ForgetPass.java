package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPass extends AppCompatActivity {
    private EditText emailedt;
    private Button resetpass;
    private ProgressBar bar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        auth=FirebaseAuth.getInstance();


        emailedt=findViewById(R.id.emailforget);
        resetpass=findViewById(R.id.btnreset);
        bar=findViewById(R.id.progressBar3);

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String email=emailedt.getText().toString().trim();

        if(email.isEmpty()){
            emailedt.setError("Email is required!");
            emailedt.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailedt.setError("Please Enter valid email!");
            emailedt.requestFocus();
            return;
        }

        bar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(ForgetPass.this, "Check your email to reset your password!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ForgetPass.this, "Try Again! Something wrong happened!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}