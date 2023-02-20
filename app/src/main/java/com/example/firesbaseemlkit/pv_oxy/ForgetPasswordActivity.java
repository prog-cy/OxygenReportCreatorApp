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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ForgetPasswordActivity extends AppCompatActivity {

    private AppCompatButton submitBT;
    private EditText userNameET, passwordET, confirmPasswordET;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("User");
    private final CollectionReference collectionReference1 = db.collection("UserCredentials");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        userNameET = findViewById(R.id.user_id);
        passwordET = findViewById(R.id.diff_password);
        confirmPasswordET = findViewById(R.id.confirm_password);
        submitBT = findViewById(R.id.submit);

        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userNameET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                String confirm = confirmPasswordET.getText().toString().trim();

                if(userName.length() == 0 || password.length() == 0 || confirm.length() == 0){
                    Toast.makeText(ForgetPasswordActivity.this, "Any of the is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!confirm.equalsIgnoreCase(password)){
                    Toast.makeText(ForgetPasswordActivity.this, "Password is not matching", Toast.LENGTH_SHORT).show();
                }else{
                    updateCredentials(userName, password, confirm);
                }
            }
        });
    }

    private void updateCredentials(String userName, String password, String confirm) {

        collectionReference1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){

                    String documentRef = snapshot.getString("Document Id");
                    String originUserName = snapshot.getString("userName");

                    assert originUserName != null;
                    if(originUserName.equalsIgnoreCase(userName)){
                        assert documentRef != null;
                        DocumentReference documentReference = collectionReference.document(documentRef);
                        documentReference.update("password", password);
                        documentReference.update("confirm_password", confirm);
                        break;
                    }
                }
                Toast.makeText(ForgetPasswordActivity.this, "Password updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgetPasswordActivity.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgetPasswordActivity.this, "Error occurred...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}