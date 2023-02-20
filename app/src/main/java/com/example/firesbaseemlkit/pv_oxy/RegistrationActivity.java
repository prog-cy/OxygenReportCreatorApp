package com.example.firesbaseemlkit.pv_oxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firesbaseemlkit.pv_oxy.userdata.AppUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText firstNameET, lastNameET, addressET, cityET,
            stateET, pinET, emailET, mobileET, userNameET, passwordET, confirmPasswordET;

    private AppCompatButton submitBT;

    //Firebase authentication service
    private FirebaseAuth firebaseAuth;

    //Firebase FireStore database service.
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reference = db.collection("User");

    //List to contain all the user name present in UserCredentials collection in FireStore database.
    private List<String> container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        firstNameET = findViewById(R.id.first_name);
        lastNameET = findViewById(R.id.last_name);
        addressET = findViewById(R.id.address);
        cityET = findViewById(R.id.city);
        stateET = findViewById(R.id.state);
        pinET = findViewById(R.id.pin_code);
        emailET = findViewById(R.id.email_address);
        mobileET = findViewById(R.id.mobile_no);
        userNameET = findViewById(R.id.user_id_name);
        passwordET = findViewById(R.id.reg_password);
        confirmPasswordET = findViewById(R.id.reg_confirm_password);

        firebaseAuth = FirebaseAuth.getInstance();

        //This method will fetch all the user names present in db.
        fetchUserName();

        // On submit button click save entire data to FireStore database.
        submitBT = findViewById(R.id.reg_submit);
        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first_name = firstNameET.getText().toString().trim();
                String last_name = lastNameET.getText().toString().trim();
                String address = addressET.getText().toString().trim();
                String city = cityET.getText().toString().trim();
                String state = stateET.getText().toString().trim();
                String pin = pinET.getText().toString().trim();
                String email = emailET.getText().toString().trim();
                String mobileNo = mobileET.getText().toString().trim();
                String userName = userNameET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                String confirm_password = confirmPasswordET.getText().toString().trim();

                if(first_name.length() == 0 || last_name.length() == 0 || address.length() == 0
                || city.length() == 0 || state.length() == 0 || pin.length() == 0 || email.length() == 0
                || mobileNo.length() == 0 || userName.length() == 0 ||
                        password.length() == 0 || confirm_password.length() == 0){
                    Toast.makeText(RegistrationActivity.this, "few text fields is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!password.equals(confirm_password)){
                    Toast.makeText(RegistrationActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(container.contains(userName)){
                    Toast.makeText(RegistrationActivity.this, "User name is already present", Toast.LENGTH_SHORT).show();
                }
                else{

                    AppUser user = new AppUser(first_name, last_name, address, city, state, pin, email, mobileNo, userName,
                            password, confirm_password);
                    saveUserData(user);
                }
            }
        });
    }


    //Method will add all user names in container of type List<String> container.
    private void fetchUserName() {

        container = new ArrayList<>();

        db.collection("UserCredentials").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){

                    String user_name = snapshot.getString("userName");
                    container.add(user_name);
                }
            }
        });
    }

    //Save userName and documentId of AppUser data.
    private void saveUserCredentials(String documentId, String userName){

        Map<String, String> map = new HashMap<>();
        map.put("Document Id", documentId);
        map.put("userName", userName);

        db.collection("UserCredentials").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                container.add(userName);
                Toast.makeText(RegistrationActivity.this, "Credentials added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistrationActivity.this, "Fail to add credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //This method is used to create account using email and password.
    private void createUserAccount(String email, String password){

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                            Toast.makeText(RegistrationActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrationActivity.this, "Account has not been created", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //This method will save data into FireStore database whenever invoked.
    private void saveUserData(AppUser user) {

        reference.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String documentId = documentReference.getId(); //This line is to get id of AppUser document.
                String userName = user.getUser_name();
                saveUserCredentials(documentId, userName);
//                createUserAccount(user.getEmail(), user.getPassword());
                Toast.makeText(RegistrationActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistrationActivity.this, "Data haven't saved successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}