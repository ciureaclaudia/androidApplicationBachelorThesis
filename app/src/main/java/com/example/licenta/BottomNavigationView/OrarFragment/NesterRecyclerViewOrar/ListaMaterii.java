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

        holder.cv_child_item_tv_Materie.setText(listaMaterii.get(position).getDenumire());
        holder.cv_child_item_tv_startTime.setText(listaMaterii.get(position).getInceput());
        holder.cv_child_item_tv_endTime.setText(listaMaterii.get(position).getSfarsit());
        holder.cv_child_item_tv_sala.setText(listaMaterii.get(position).getSala());
        holder.cv_child_item_tv_profesor.setText(listaMaterii.get(position).getProfesor());

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
