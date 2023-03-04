package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.licenta.BottomNavigationView.HomeFragment;
import com.example.licenta.BottomNavigationView.NoteFragment;
import com.example.licenta.BottomNavigationView.OrarFragment;
import com.example.licenta.BottomNavigationView.ProfileFragment;
import com.example.licenta.NavigationDrawer.Countdown;
import com.example.licenta.NavigationDrawer.Grafic;
import com.example.licenta.NavigationDrawer.Harta;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//    Button btn;TextView textView;
    FirebaseAuth auth;
    FirebaseUser user;

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment=new HomeFragment();
    ProfileFragment profileFragment=new ProfileFragment();
    NoteFragment noteFragment=new NoteFragment();
    OrarFragment orarFragment=new OrarFragment();

    private DrawerLayout drawerLayout;


    @SuppressLint("WrongViewCast")
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


            MaterialToolbar toolbar = findViewById(R.id.toolbar1); //Ignore red line errors
            setSupportActionBar(toolbar);


            drawerLayout = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                    R.string.close_nav);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            toggle.syncState();
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit(); //replace the container with the home fragment
                navigationView.setCheckedItem(R.id.nav_home);
            }

        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit(); //replace the container with the home fragment
                break;
            case R.id.nav_harta:
                intent=new Intent(getApplicationContext(), Harta.class);
                startActivity(intent);
                finish(); //inchid activitatea
                break;
            case R.id.nav_grafic:
                intent=new Intent(getApplicationContext(), Grafic.class);
                startActivity(intent);
                finish(); //inchid activitatea
                break;
            case R.id.nav_countdown:
                intent=new Intent(getApplicationContext(), Countdown.class);
                startActivity(intent);
                finish(); //inchid activitatea
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                intent=new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}