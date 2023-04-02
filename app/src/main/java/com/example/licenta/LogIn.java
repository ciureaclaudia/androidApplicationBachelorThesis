package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextParola;
    Button buttonLogIn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();


        FirebaseUser currentUser = mAuth.getCurrentUser();
        //verific daca utilizatoruo este deja loggedIn si daca el este deja deschid activitatea principala
        if(currentUser != null){
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();//inchis Login si deschid activitatea principala

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();//hides the action bar

        setContentView(R.layout.activity_log_in);

        mAuth=FirebaseAuth.getInstance();//am initializat obiectul
        editTextEmail=findViewById(R.id.email);
        editTextParola=findViewById(R.id.parola);
        buttonLogIn=findViewById(R.id.btn_logIn);
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.signupNow);
        textView.setOnClickListener(view -> {
            //deschid activitatea de LogIn
            Intent intent=new Intent(getApplicationContext(),SignUp.class);
            startActivity(intent);
            finish(); //inchid activitatea
        });
        
        showHidePassword();

        buttonLogIn.setOnClickListener(view -> {

            progressBar.setVisibility(View.VISIBLE);

            String email, parola;
            //citesc email si parola
            email= String.valueOf(editTextEmail.getText());
            parola= String.valueOf(editTextParola.getText());


            //verific daca parola sau email e gol
            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(parola)){
                Toast.makeText(LogIn.this,"Email sau Parola incomplete" ,Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }


            mAuth.signInWithEmailAndPassword(email, parola).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(LogIn.this, "Autentificare reusita", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finish();//inchis Login si deschid activitatea principala

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LogIn.this, "Nu s-a reusit autentificarea", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        });
    }

    //show/ hide password using eye icon
    private void showHidePassword() {
        ImageView imageViewShowHidePwd=findViewById(R.id.imageView_show_hide_password);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);

        imageViewShowHidePwd.setOnClickListener(view -> {
            //if pwd is visible then hide it
            if(editTextParola.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                editTextParola.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd); //Change Icon
            }else{
                editTextParola.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
            }
        });
    }
}