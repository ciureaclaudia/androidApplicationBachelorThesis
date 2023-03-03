package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
//    Button btn;TextView textView;
    FirebaseAuth auth;
    FirebaseUser user;

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment=new HomeFragment();
    ProfileFragment profileFragment=new ProfileFragment();
    NoteFragment noteFragment=new NoteFragment();
    OrarFragment orarFragment=new OrarFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        btn=findViewById(R.id.logout);
//        textView=findViewById(R.id.textView_email);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        if(user==null){
            Intent intent=new Intent(getApplicationContext(), LogIn.class);
            startActivity(intent);
            finish();
        }
        else{
//            textView.setText(user.getEmail());

            bottomNavigationView=findViewById(R.id.bottom_navigation);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit(); //replace the container with the home fragment

            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected( MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.home:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit(); //replace the container with the home fragment
                            return true;
                        case R.id.profile:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit(); //replace the container with the home fragment
                            return true;
                        case R.id.note:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,noteFragment).commit(); //replace the container with the home fragment
                            return true;
                        case R.id.orar:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,orarFragment).commit(); //replace the container with the home fragment
                            return true;
                    }
                    return false;
                }
            });



        }

//        btn.setOnClickListener(view -> {
//            FirebaseAuth.getInstance().signOut();
//            Intent intent=new Intent(getApplicationContext(), LogIn.class);
//            startActivity(intent);
//            finish();
//        });

    }


}