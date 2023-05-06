package com.example.licenta.BottomNavigationView.OrarFragment.NesterRecyclerViewOrar;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaMaterii extends RecyclerView.Adapter<ListaMaterii.ViewHolder> {

    List<MaterieOrar> listaMaterii;
    Context context;

    public ListaMaterii(List<MaterieOrar> listaMaterii, Context context) {
        this.listaMaterii = listaMaterii;
        this.context = context;
    }

    @NonNull
    @Override
    public ListaMaterii.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.child_rv_layout_orar,null,false);
        return new ViewHolder(view);
    }

    //aici populez CardViewul cu info cu materia din lista
    @Override
    public void onBindViewHolder(@NonNull ListaMaterii.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        FirebaseFirestore db = FirebaseFirestore.getInstance(); //preaiu instanta de la BD
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid(); //preaiu ID ul de la user
        // Get the Firestore reference for the user's "Orar" collection
        CollectionReference orarCollection  = db.collection("users").document(userID).collection("Orar"); //preiau referinta ed la colectia ORAR

        // Query the Orar collection to get all documents
        orarCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> orarList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the weekDay field from the document
                    String weekDay = document.getString("weekDay");
                    orarList.add(weekDay + ":"); // Add the weekDay to the list

                    // Get the Materii subcollection from the document
                    CollectionReference materiiRef = document.getReference().collection("Materii");

                    materiiRef.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                // Get the fields from the Materii document
                                String denumire = document1.getString("denumire");
                                String sala = document1.getString("sala");
                                String profesor = document1.getString("profesor");
                                String ziSapt = document1.getString("ziSapt");
                                String inceput = document1.getString("inceput");
                                String sfarsit = document1.getString("sfarsit");

                                // Add the Materii fields to the list
//                                        orarList.add("- " + denumire + ", " + sala + ", " + profesor + ", " + ziSapt + ", " + inceput + " - " + sfarsit);
                            }

                            // Show the list on screen
                            holder.cv_child_item_tv_Materie.setText(listaMaterii.get(position).getDenumire());
                            holder.cv_child_item_tv_startTime.setText(listaMaterii.get(position).getInceput());
                            holder.cv_child_item_tv_endTime.setText(listaMaterii.get(position).getSfarsit());
                            holder.cv_child_item_tv_sala.setText(listaMaterii.get(position).getSala());
                            holder.cv_child_item_tv_profesor.setText(listaMaterii.get(position).getProfesor());

//                                    textView.setText(TextUtils.join("\n", orarList));
                        } else {
                            Log.d(TAG, "Error getting Materii documents: ", task1.getException());
                        }
                    });
                }
            } else {
                Log.d(TAG, "Error getting Orar documents: ", task.getException());
            }
        });

//        holder.cv_child_item_tv_Materie.setText(listaMaterii.get(position).getDenumire());
//        holder.cv_child_item_tv_startTime.setText(listaMaterii.get(position).getInceput());
//        holder.cv_child_item_tv_endTime.setText(listaMaterii.get(position).getSfarsit());
//        holder.cv_child_item_tv_sala.setText(listaMaterii.get(position).getSala());
//        holder.cv_child_item_tv_profesor.setText(listaMaterii.get(position).getProfesor());

        //tin apasat pe denumirea materiei -> sa apara un alert dialog in care sa intrebe daca sa stergi materia
        holder.cv_child_item_tv_Materie.setOnClickListener(view -> {
            Toast.makeText(context, "AI APASAT", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return listaMaterii.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //declare all child items here
        TextView cv_child_item_tv_Materie;
        TextView cv_child_item_tv_startTime;
        TextView cv_child_item_tv_endTime;
        TextView cv_child_item_tv_sala;
        TextView cv_child_item_tv_profesor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv_child_item_tv_Materie=itemView.findViewById(R.id.cv_child_item_tv_Materie);
            cv_child_item_tv_startTime=itemView.findViewById(R.id.cv_child_item_tv_startTime);
            cv_child_item_tv_endTime=itemView.findViewById(R.id.cv_child_item_tv_endTime);
            cv_child_item_tv_sala=itemView.findViewById(R.id.cv_child_item_tv_sala);
            cv_child_item_tv_profesor=itemView.findViewById(R.id.cv_child_item_tv_profesor);


        }
    }
}
