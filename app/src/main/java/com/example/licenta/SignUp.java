package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextParola, editTextNume, editTextPrenume,
            editTextParola2, editTextDataNastere, editTextFacultate;
    Button buttonSignup;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;
    private static final String TAG="SignUpActivity";
    int ok=1;
    FirebaseFirestore fStore;
    String userID;



//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        //verific daca utilizatoruo este deja loggedIn si daca el este deja deschid activitatea principala
//        if(currentUser != null){
//            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(intent);
//            finish();//inchis Login si deschid activitatea principala
//
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        editTextEmail=findViewById(R.id.email);
        editTextParola=findViewById(R.id.parola);
        editTextNume=findViewById(R.id.nume_signup);
        editTextPrenume=findViewById(R.id.prenume_signup);
        editTextDataNastere=findViewById(R.id.dataNastere_signup);
        editTextParola2=findViewById(R.id.parola2);
        editTextFacultate=findViewById(R.id.facultate_signup);

        buttonSignup=findViewById(R.id.btn_signup);
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.loginNow);
        textView.setOnClickListener(view -> {
            //deschid activitatea de LogIn
            Intent intent=new Intent(getApplicationContext(),LogIn.class);
            startActivity(intent);
            finish(); //inchid activitatea
        });

        buttonSignup.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);

            String email, parola, parola2, facultate, nume, prenume,dataNastere;

            //preiau  toate campurile introduse
            email= String.valueOf(editTextEmail.getText());
            parola= String.valueOf(editTextParola.getText());
            parola2= String.valueOf(editTextParola2.getText());
            facultate= String.valueOf(editTextFacultate.getText());
            nume= String.valueOf(editTextNume.getText());
            prenume= String.valueOf(editTextPrenume.getText());
            dataNastere= String.valueOf(editTextDataNastere.getText());


            //verificari la campuri
            if(TextUtils.isEmpty(nume)){
                Toast.makeText(SignUp.this,"Introdu numele" ,Toast.LENGTH_SHORT).show();
                editTextNume.setError("Name is required");
                editTextNume.requestFocus();
                progressBar.setVisibility(View.GONE);
                ok=0;
            }else if(TextUtils.isEmpty(prenume)){
                    Toast.makeText(SignUp.this,"Introdu prenumele" ,Toast.LENGTH_SHORT).show();
                    editTextPrenume.setError("Name is required");
                    editTextPrenume.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    ok=0;
            }else if(TextUtils.isEmpty(dataNastere)){
//                try {
//                    Date date=new SimpleDateFormat("dd/mm/yyyy").parse(dataNastere.toString());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                Toast.makeText(SignUp.this,"Introdu data de nastere" ,Toast.LENGTH_SHORT).show();
                editTextDataNastere.setError("Date of Birth is required");
                editTextDataNastere.requestFocus();
                ok=0;
            }else if(TextUtils.isEmpty(facultate)){
                Toast.makeText(SignUp.this,"Introdu facultatea" ,Toast.LENGTH_SHORT).show();
                editTextFacultate.setError("Faculty is required");
                editTextFacultate.requestFocus();
                progressBar.setVisibility(View.GONE);
                ok=0;
            }else  if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignUp.this,"Introdu email-ul" ,Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                    progressBar.setVisibility(View.GONE);
                ok=0;
            }else  if( !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(SignUp.this,"Reintrodu email-ul" ,Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Valid email is required");
                    editTextEmail.requestFocus();
                    progressBar.setVisibility(View.GONE);
                ok=0;
            }else if( TextUtils.isEmpty(parola)){
                Toast.makeText(SignUp.this,"Introdu parola" ,Toast.LENGTH_SHORT).show();
                editTextParola.setError("Password is required");
                editTextParola.requestFocus();
                progressBar.setVisibility(View.GONE);
                ok=0;
            } else if(parola.length()<6){
                Toast.makeText(SignUp.this,"Parola prea scurta! min 6 caractere" ,Toast.LENGTH_SHORT).show();
                editTextParola.setError("Password too weak");
                editTextParola.requestFocus();
                progressBar.setVisibility(View.GONE);
                ok=0;
            }else  if( TextUtils.isEmpty(parola2)){
                Toast.makeText(SignUp.this,"Confirma parola" ,Toast.LENGTH_SHORT).show();
                editTextParola2.setError("Password Confirmation is required");
                editTextParola2.requestFocus();
                progressBar.setVisibility(View.GONE);
                ok=0;
            } else if(! parola.equals(parola2)){
                Toast.makeText(SignUp.this,"Introdu aceeasi parola" ,Toast.LENGTH_SHORT).show();
                editTextParola2.setError("Password must be the same");
                editTextParola2.requestFocus();
                progressBar.setVisibility(View.GONE);
                //Clear the entered password
                editTextParola2.clearComposingText();
                editTextParola.clearComposingText();
                ok=0;
            }
            else{
                //toate valorile sunt bune si datele sunt validate
                progressBar.setVisibility(View.VISIBLE);
                registerUser(nume, prenume,dataNastere,facultate,email,parola,parola2);
            }


//            mAuth.createUserWithEmailAndPassword(email, parola).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            progressBar.setVisibility(View.GONE);
//                            if (task.isSuccessful()) {
//                                Toast.makeText(SignUp.this, "Cont creat", Toast.LENGTH_SHORT).show();
//                                Intent intent=new Intent(getApplicationContext(),LogIn.class);
//                                startActivity(intent);
//                                finish(); //inchid activitatea
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Toast.makeText(SignUp.this, "Esuare autentificare", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });


        });
    }

    //Register User using the credentials given
    private void registerUser(String nume, String prenume, String dataNastere, String facultate,
                              String email, String parola, String parola2) {
        mAuth=FirebaseAuth.getInstance();//am initializat obiectul
        mAuth.createUserWithEmailAndPassword(email,parola).addOnCompleteListener(SignUp.this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //user was created
                    Toast.makeText(SignUp.this, "Cont creat", Toast.LENGTH_SHORT).show();

                    //register user in firebase
                    userID=mAuth.getCurrentUser().getUid();
                    fStore=FirebaseFirestore.getInstance();
                    DocumentReference documentReference=fStore.collection("users").document(userID);
                    //hashmap
                    Map<String ,Object> user= new HashMap<>();
                    user.put("nume",nume);
                    user.put("prenume",prenume);
                    user.put("dNastere",dataNastere);
                    user.put("facultate",facultate);
                    user.put("email",email);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG,"Succes:user profile is created for"+userID);
                        }
                    });



                    FirebaseUser firebaseUser= mAuth.getCurrentUser();

                    //Send verification email
                    firebaseUser.sendEmailVerification();

                    //Open user profile after successful registration
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    //To prevent User from returning back to Register Activity on pressing back button after registration
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);

                    progressBar.setVisibility(View.GONE);
                    startActivity(intent);
                    finish(); //Close SignUp activity
                } else {
                    try{
                        throw  task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        editTextParola.setError("Your password is too weak. Use alphabets, numbers and special characters");
                        editTextParola.requestFocus();
                        progressBar.setVisibility(View.GONE);
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        editTextEmail.setError("Invalid email or already used");
                        editTextEmail.requestFocus();
                        progressBar.setVisibility(View.GONE);
                    } catch (FirebaseAuthUserCollisionException e){
                        editTextEmail.setError("User is already registered with this email");
                        editTextEmail.requestFocus();
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

    }
}