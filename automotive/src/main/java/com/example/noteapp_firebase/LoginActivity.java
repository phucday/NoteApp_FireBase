package com.example.noteapp_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteapp_firebase.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v -> LoginUser());
        binding.creat.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,Creat_Acount.class)));
        binding.btnForgotPassword.setOnClickListener(v ->ForgotPassWord());
    }
    private void LoginUser(){
        String email_login = binding.emailLogin.getText().toString();
        String password_login  = binding.passwordLogin.getText().toString();

        boolean isValidated = validateData(email_login,password_login);
        if(!isValidated)return;

        LoginAccountInFireBase(email_login,password_login);

    }
    private void LoginAccountInFireBase(String email,String password){
         ChangedInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                ChangedInProgress(false);
                if(task.isSuccessful()){
                    //success
                    if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this, "Email isn't verified, please verify your email.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //failure
                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("AAA",task.getException().getLocalizedMessage());
                }
            }
        });
    }
    private boolean validateData(String email,String password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailLogin.setError("Email is invalid");
            return false;
        }
        return true;
    }
    private void ChangedInProgress(boolean inprogress){
        if(inprogress){
            binding.progress.setVisibility(View.VISIBLE);
            binding.btnLogin.setVisibility(View.GONE);
        }else{
            binding.progress.setVisibility(View.GONE);
            binding.btnLogin.setVisibility(View.VISIBLE);
        }
    }

    private void ForgotPassWord(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot_password,null);
        EditText email = dialogView.findViewById(R.id.email_sendPassword_forgot);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                if(TextUtils.isEmpty(emailText) || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
                    Toast.makeText(LoginActivity.this, "Email Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(emailText).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Succesed check your email", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else {
                                Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });
        dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if(dialog.getWindow() != null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.show();
    }
}