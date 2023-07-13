package com.example.licenta.BottomNavigationView;

import static android.content.ContentValues.TAG;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.BottomNavigationView.OrarFragment.NesterRecyclerViewOrar.MaterieOrar;
import com.example.licenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class HomeFragment extends Fragment {

    TextView zi_curenta;
    TextView welcomeBack;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userID;
    private CollectionReference orarCollection;
    private DocumentReference documentReference;
    List<String> listaMateriiOrar ; //lista cu toate materiile

    ListView listViewMateriiCurente;
    private ArrayAdapter<String> adapterListView;

    Calendar calendar;
    SimpleDateFormat dateFormat;
    String formattedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance(); //preaiu instanta de la BD
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid(); //preaiu ID ul de la user
        // Get the Firestore reference for the user's "Orar" collection
        documentReference = db.collection("users").document(userID);
        orarCollection = documentReference.collection("Orar");
        zi_curenta = view.findViewById(R.id.zi_curenta);
        welcomeBack = view.findViewById(R.id.mesaj);
        listViewMateriiCurente=view.findViewById(R.id.listView_materii);
        listaMateriiOrar=new ArrayList<>();
        ConstraintLayout constraintLayout = view.findViewById(R.id.mainLayout);

        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

        // Get the current date and time
         calendar = Calendar.getInstance();
        // Create a SimpleDateFormat instance for formatting the date
         dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault());
        // Format the current date
         formattedDate = dateFormat.format(calendar.getTime());

        // Display the formatted date in the "zi_curenta" view
        zi_curenta.setText(formattedDate);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        populez lista
        showData();

        adapterListView = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listaMateriiOrar);
//         Set the adapter on the ListView
        listViewMateriiCurente.setAdapter(adapterListView);
    }


    //preiau datele introduse in orar si le salvez in:  List<String> listaMateriiOrar
    // populez lista listaMateriiOrar doar cu denumirile de materii din orar PT SPINNER
    public void showData() {
        orarCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CollectionReference materiiRef = document.getReference().collection("Materii");
                        materiiRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        MaterieOrar materieOrar = doc.toObject(MaterieOrar.class);

                                        //verific ziua saptamanii
                                        // Extract the weekday from the formatted date
                                        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                                        String weekday = dayFormat.format(calendar.getTime());
                                        if(weekday.equals("Monday") && Objects.equals(materieOrar.getZiSapt(), "Luni")){
//                                            Toast.makeText(getContext(), "DA", Toast.LENGTH_SHORT).show();
                                            listaMateriiOrar.add(materieOrar.getDenumire()); //populez lista mea de denumiri de materii cu denumirile preluate din bd
                                        } else if(weekday.equals("Tuesday") && Objects.equals(materieOrar.getZiSapt(), "Marti")){
                                            listaMateriiOrar.add(materieOrar.getDenumire()); //populez lista mea de denumiri de materii cu denumirile preluate din bd
                                        }   else if(weekday.equals("Wednesday") && Objects.equals(materieOrar.getZiSapt(), "Miercuri")){
                                            listaMateriiOrar.add(materieOrar.getDenumire()); //populez lista mea de denumiri de materii cu denumirile preluate din bd
                                        } else if(weekday.equals("Thursday") && Objects.equals(materieOrar.getZiSapt(), "Joi")){
                                            listaMateriiOrar.add(materieOrar.getDenumire()); //populez lista mea de denumiri de materii cu denumirile preluate din bd
                                        } else if(weekday.equals("Friday") && Objects.equals(materieOrar.getZiSapt(), "Vineri")){
                                            listaMateriiOrar.add(materieOrar.getDenumire()); //populez lista mea de denumiri de materii cu denumirile preluate din bd
                                        }


                                    }
                                    adapterListView.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getContext(), "Eroare", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                    }
//                    callback.onDataLoaded(listaMateriiOrar);
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }





}