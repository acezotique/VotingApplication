package com.example.votingapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.votingapplication.databinding.ActivityChangePasswordBinding;
import com.example.votingapplication.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void fnReset(View view) {
        Intent intent = new Intent(ChangePassword.this, LoginActivity.class);
        String username = binding.editTextTextPersonName.getText().toString();
        String password = passwordHashing(binding.editTextTextPassword.getText().toString());
        String confirmPassword = passwordHashing(binding.confirmPassword.getText().toString());
        if (password.equals(confirmPassword)) {
            Map<String, Object> data = new HashMap<>();
            data.put("password", password);
            db.collection("student").document(username).get().addOnCompleteListener(
                    new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    db.collection("student").document(username)
                                            .set(data, SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("Result", "DocumentSnapshot successfully written!");
                                                    Toast.makeText(getApplicationContext(),
                                                            "Successfully change password", Toast.LENGTH_SHORT).show();
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("Error", "Error writing document", e);
                                                    Toast.makeText(getApplicationContext(),
                                                            "Error changing password"+e.toString(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            });
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
        }else{
            Toast.makeText(getApplicationContext(), "Password is not same", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public String passwordHashing(String password) {
        String hashedPass = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
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