package com.example.licenta.BottomNavigationView.OrarFragment.NesterRecyclerViewOrar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ViewHolder> {

    HashMap<ParentModelClass, ArrayList<MaterieOrar>> hashMap;
    Context context;

    public ParentAdapter(HashMap<ParentModelClass, ArrayList<MaterieOrar>> hashMap, Context context) {
        this.hashMap = hashMap;
        this.context = context;
    }


    @NonNull
    @Override
    public ParentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.parent_rv_layout_orar, null, false);
        return new ViewHolder(view);
    }

    //functia asta parcurge hashMapul
    //preiau cheia si valoarea pt fiecare elem din hashMap
    //daca idulul este la fel ca positie atunci lista de materii va fi afisata in ziua cu idul corespunzator
    @Override
    public void onBindViewHolder(@NonNull ParentAdapter.ViewHolder holder, int position) {

        ParentModelClass parentModelClassCurrentPosition = null;
        ArrayList<MaterieOrar> listaObiecteCurente = null;
        for (Map.Entry<ParentModelClass, ArrayList<MaterieOrar>> entry : hashMap.entrySet()) {
            ParentModelClass key = entry.getKey();
            ArrayList<MaterieOrar> value = entry.getValue();
            if(key.id==position){
                parentModelClassCurrentPosition=key;
                listaObiecteCurente=value;
                break;
            }
        }

        //aici se vor seta cu zilele sapt fiecare banda reprezentand zi a sapt din orar
        holder.tv_parent_title.setText(parentModelClassCurrentPosition.weekDay);

        ListaMaterii listaMaterii = new ListaMaterii(listaObiecteCurente, context);

        holder.rv_child.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rv_child.setAdapter(listaMaterii);
        listaMaterii.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return hashMap.keySet().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rv_child;
        TextView tv_parent_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_child = itemView.findViewById(R.id.rv_child_orar);
            tv_parent_title = itemView.findViewById(R.id.tv_parent_title);
        }
    }
}
