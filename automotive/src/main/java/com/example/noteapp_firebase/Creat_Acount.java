package com.example.noteapp_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.noteapp_firebase.databinding.ActivityCreatAcountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Creat_Acount extends AppCompatActivity {
    ActivityCreatAcountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatAcountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Creat_Acount.this,LoginActivity.class));
            }
        });
        binding.btnCreatAcount.setOnClickListener(v -> creatAcount());
    }

    private void creatAcount(){
        String email_creat = binding.emailCreat.getText().toString();
        String password_creat = binding.passwordCreat.getText().toString();
        String password_confirm_creat = binding.passwordConfirmCreat.getText().toString();

        boolean isValidated = validateData(email_creat,password_creat,password_confirm_creat);

        if(!isValidated) return;

        Creat_Acount_inFireBase(email_creat,password_creat);
    }

   private void Creat_Acount_inFireBase(String email,String password){
        ChangedInProgress(true);
       FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
       firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Creat_Acount.this, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               ChangedInProgress(false);
               if(task.isSuccessful()){
                   //success
                   Toast.makeText(Creat_Acount.this, "Creat Acount Success", Toast.LENGTH_SHORT).show();
                   firebaseAuth.getCurrentUser().sendEmailVerification();
                   firebaseAuth.signOut();
                   finish();
               }else{
                   //failure
                   Toast.makeText(Creat_Acount.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
               }
           }
       });
    }
    private void ChangedInProgress(boolean inprogress){
        if(inprogress){
            binding.progress.setVisibility(View.VISIBLE);
            binding.btnCreatAcount.setVisibility(View.GONE);
        }else{
            binding.progress.setVisibility(View.GONE);
            binding.btnCreatAcount.setVisibility(View.VISIBLE);
        }
    }
    private boolean validateData(String email,String password, String ConFirm_password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailCreat.setError("Email is invalid");
            return false;
        }if(password.length() < 6){
            binding.passwordCreat.setError("pass short");
            return false;
        }if(!password.equals(ConFirm_password)){
            binding.passwordConfirmCreat.setError("not equal password");
            return false;
        }
        return true;
    }

}