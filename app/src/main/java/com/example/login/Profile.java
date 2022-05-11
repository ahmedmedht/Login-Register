package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    private Button out;
    private FirebaseAuth auth;
    private String userId;
    private TextView emailview,nameview,ageview;
    private FirebaseUser user;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth=   FirebaseAuth.getInstance();

        out=findViewById(R.id.btnout);
        emailview=findViewById(R.id.txtemail);
        nameview=findViewById(R.id.txtname);
        ageview=findViewById(R.id.txtage);

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(Profile.this, MainActivity.class));

            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userId=user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);

                if(userProfile !=null){
                    String fullname= userProfile.getFullName();
                    String email=userProfile.getEmail();
                    String age=userProfile.getAge();

                    emailview.setText(emailview.getText().toString()+email);
                    nameview.setText(nameview.getText().toString()+fullname);
                    ageview.setText(ageview.getText().toString()+age);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}