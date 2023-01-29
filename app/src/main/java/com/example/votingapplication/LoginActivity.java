package com.example.votingapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.votingapplication.databinding.ActivityLoginBinding;
import com.example.votingapplication.model.Student;
import com.example.votingapplication.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityLoginBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
    }

    public void fnLogin(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        String matricNo = binding.editTextTextPersonName.getText().toString();
        db.collection("student").document(matricNo).get().addOnCompleteListener(
                                new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        JSONObject data = new JSONObject(document.getData());
                        try {
                            if (!passwordHashing().equals(data.getString("password"))){
                                Log.d("password: ", passwordHashing());
                                Toast.makeText(getApplicationContext(),
                                        "Wrong username/password: "+passwordHashing(), Toast.LENGTH_SHORT).show();
                            }else {
                                Student student = new Student(matricNo,
                                        data.getString("password"),
                                        data.getString("name"),
                                        data.getString("ic"),
                                        data.getString("faculty"),
                                        data.getString("year"),
                                        data.getString("semester"),
                                        data.getString("email"));
                                intent.putExtra("student", student);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d(TAG, "Account not exist");
                        Toast.makeText(getApplicationContext(), "Account not exist",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void fnForgotPassword(View view) {
        Intent intent = new Intent(LoginActivity.this, ChangePassword.class);
        startActivity(intent);
    }

    public String passwordHashing() {
        String hashedPass = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(binding.editTextTextPassword.getText().toString().getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPass = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPass;
    }
}