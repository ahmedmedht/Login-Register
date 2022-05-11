package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {
    private TextView forget,regist;
    private EditText emailedt,passwordedt;
    private Button signinbtn;
    private ProgressBar bar;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();

        forget=findViewById(R.id.forgetpass);
        regist=findViewById(R.id.regis);
        emailedt=findViewById(R.id.emailadrs);
        passwordedt=findViewById(R.id.password);
        signinbtn=findViewById(R.id.btnlogin);
        bar=findViewById(R.id.progressBar);

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ForgetPass.class));
            }
        });


        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MainActivity.this,Register.class);
                startActivity(i);
            }
        });
    }

    private void userLogin() {
        String email=emailedt.getText().toString().trim();
        String password=passwordedt.getText().toString().trim();
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

        if(password.isEmpty()){
            passwordedt.setError("Password is required!");
            passwordedt.requestFocus();
            return;
        }

        if(password.length() < 6){
            passwordedt.setError("Min password lengh should be 6 characters!");
            passwordedt.requestFocus();
            return;
        }
        bar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()) {

                        startActivity(new Intent(MainActivity.this, Profile.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email to verify your account!", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(MainActivity.this, "Faild to login! please check your credentials ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}