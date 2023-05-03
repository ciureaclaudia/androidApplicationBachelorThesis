package com.example.licenta.BottomNavigationView.OrarFragment.NesterRecyclerViewOrar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    List<MaterieOrar> childModelClassList;
    Context context;

    public ChildAdapter(List<MaterieOrar> childModelClassList, Context context) {
        this.childModelClassList = childModelClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChildAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.child_rv_layout_orar,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildAdapter.ViewHolder holder, int position) {


        holder.cv_child_item_tv_Materie.setText(childModelClassList.get(position).getDenumire());
//        holder.cv_child_item_tv_startTime.setText(childModelClassList.get(position).getOraInceput());
//        holder.cv_child_item_tv_endTime.setText(childModelClassList.get(position).getOraSfarsit());
        holder.cv_child_item_tv_sala.setText(childModelClassList.get(position).getSala());
        holder.cv_child_item_tv_profesor.setText(childModelClassList.get(position).getProfesor());

        //aici ar trebui setata informatia din CardView
        holder.cv_child_item_tv_Materie.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return childModelClassList.size();
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
