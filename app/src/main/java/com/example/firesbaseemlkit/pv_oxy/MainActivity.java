package com.example.firesbaseemlkit.pv_oxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Widgets
    private AppCompatButton registrationBT, loginBT;
    private Button forgetBT;
    private EditText userET;
    private EditText passwordET;

    //Firebase authentication service.
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //Firebase FireStore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    Map<String, String> credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

       //Initializing widgets
       userET = findViewById(R.id.userName);
       passwordET = findViewById(R.id.password);
       registrationBT = findViewById(R.id.register);
       loginBT = findViewById(R.id.loginbutton);
       forgetBT = findViewById(R.id.forgetpassword);

       fetchCredentials();

        registrationBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = userET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();

                if(user_name.length() == 0 || password.length() == 0){
                    Toast.makeText(MainActivity.this, "Text field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(credentials.containsKey(user_name)){

                    String originalPassword = credentials.get(user_name);
                    if(password.equalsIgnoreCase(originalPassword)){
                        startActivity(new Intent(MainActivity.this, TaskActivity.class));
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Invalid User Name", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //when forget password
        forgetBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ForgetPasswordActivity.class));
            }
        });
    }

    private void fetchCredentials() {

        credentials = new HashMap<>();

        db.collection("UserCredentials").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){

                    String documentRef = snapshot.getString("Document Id");
                    String userName = snapshot.getString("userName");
                    assert documentRef != null;
                    DocumentReference documentReference = db.collection("User").document(documentRef);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String password = documentSnapshot.getString("password");
                            credentials.put(userName, password);
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Network error...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}