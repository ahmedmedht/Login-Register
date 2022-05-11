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
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private TextView banner;
    private EditText name,age,email,password;
    private Button register;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth=FirebaseAuth.getInstance();
        banner=findViewById(R.id.banner2);
        name=findViewById(R.id.fullname);
        age=findViewById(R.id.age);
        email=findViewById(R.id.emil);
        password=findViewById(R.id.Password);
        register=findViewById(R.id.btnreg);
        progressBar=findViewById(R.id.progressBar2);

        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,MainActivity.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registeruser();
            }
        });


    }

    private void registeruser() {
        String emaill=email.getText().toString().trim();
        String agee=age.getText().toString().trim();
        String namee=name.getText().toString().trim();
        String passwordd=password.getText().toString().trim();

        if(namee.isEmpty()){
            name.setError("Full Name is required!");
            name.requestFocus();
            return;
        }
        if(agee.isEmpty()){
            age.setError("Age is required!");
            age.requestFocus();
            return;
        }
        if(emaill.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emaill).matches()){
            email.setError("Please provide valid email!");
            email.requestFocus();
            return;
        }

        if(passwordd.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }

        if(passwordd.length() < 6){
            password.setError("Min password lengh should be 6 characters!");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(emaill,passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user=new User(namee,agee,emaill);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Register.this, "User has been registered successfully!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.VISIBLE);

                                }else{
                                    Toast.makeText(Register.this, "Failed to register! try again!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                        }
                    });

                }else{
                    Toast.makeText(Register.this, "Failed to register!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


    }
}