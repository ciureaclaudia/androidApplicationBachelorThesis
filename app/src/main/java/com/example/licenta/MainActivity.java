package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.BottomNavigationView.HomeFragment;
import com.example.licenta.BottomNavigationView.FragmentNote.NoteFragment;
import com.example.licenta.BottomNavigationView.OrarFragment.Incercare_OrarFragment;
import com.example.licenta.BottomNavigationView.OrarFragment.OrarFragment;
import com.example.licenta.BottomNavigationView.ProfileFragment.ProfileFragment;
import com.example.licenta.NavigationDrawer.ToDoListProgress.ProgresFragment;
import com.example.licenta.NavigationDrawer.GraficFragment;
import com.example.licenta.NavigationDrawer.HartaFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//    Button btn;TextView textView;
    FirebaseAuth auth;
    FirebaseUser user;

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment=new HomeFragment();
    ProfileFragment profileFragment=new ProfileFragment();
    NoteFragment noteFragment=new NoteFragment();
    OrarFragment orarFragment=new OrarFragment();
    Incercare_OrarFragment incercare_orarFragment=new Incercare_OrarFragment();

    private DrawerLayout drawerLayout;
    ProgresFragment progresFragment =new ProgresFragment();
    GraficFragment graficFragment=new GraficFragment();
    HartaFragment hartaFragment =new HartaFragment();

    boolean logout_Click=false;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        btn=findViewById(R.id.logout);
//        textView=findViewById(R.id.textView_email);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

//       NavigationView navigationViewHEADER = findViewById(R.layout.nav_header);
//       View headerView=navigationViewHEADER.getHeaderView(0);
//
//        TextView textViewNume=headerView.findViewById(R.id.nav_header_NumePrenume);
//        TextView textViewEmail=headerView.findViewById(R.id.nav_header_email);
//        textViewNume.setText(user.getDisplayName());
//        textViewEmail.setText(user.getEmail());


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
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).addToBackStack(null).commit(); //replace the container with the home fragment
                            return true;
                        case R.id.profile:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).addToBackStack(null).commit(); //replace the container with the home fragment
                            return true;
                        case R.id.note:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,noteFragment).addToBackStack(null).commit(); //replace the container with the home fragment
                            return true;
                        case R.id.orar:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, incercare_orarFragment).addToBackStack(null).commit(); //replace the container with the home fragment
                            return true;
                    }
                    return false;
                }
            });


            MaterialToolbar toolbar = findViewById(R.id.toolbar1);
            setSupportActionBar(toolbar);


            drawerLayout = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);

            View headerView=navigationView.getHeaderView(0);
            TextView textViewEmail=headerView.findViewById(R.id.nav_header_email);
            TextView textViewNume=headerView.findViewById(R.id.nav_header_Nume);
            TextView textViewPrenume=headerView.findViewById(R.id.nav_header_Prenume);

            textViewEmail.setText(user.getEmail());

            //get dao poza


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String userID = mAuth.getCurrentUser().getUid();

            DocumentReference documentReference = db.collection("users").document(userID);

               documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                   @Override
                   public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                       if(!logout_Click){
                           textViewNume.setText(value.getString("nume"));
                           textViewPrenume.setText(value.getString("prenume"));
                       }

                   }
               });


            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                    R.string.close_nav);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            toggle.syncState();
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).addToBackStack(null).commit(); //replace the container with the home fragment
                navigationView.setCheckedItem(R.id.nav_home);
            }

        }

    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).addToBackStack(null).commit(); //replace the container with the home fragment

                break;
            case R.id.nav_harta:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,hartaFragment).addToBackStack(null).commit(); //replace the container with the home fragment

                break;
            case R.id.nav_grafic:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,graficFragment).addToBackStack(null).commit(); //replace the container with the home fragment

                break;
            case R.id.nav_progres:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, progresFragment).addToBackStack(null).commit(); //replace the container with the home fragment

                break;
            case R.id.nav_logout:
                logout_Click=true;
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