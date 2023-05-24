package com.example.licenta.BottomNavigationView.FragmentNote;

import static java.security.AccessController.getContext;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterMaterii extends RecyclerView.Adapter<RecyclerViewAdapterMaterii.MyViewHolder> {

    Context context;
    ArrayList<Materie> materii;

    public RecyclerViewAdapterMaterii(Context context, ArrayList<Materie> materii, OnButtonClickListener listener){
        this.context=context;
        this.materii=materii;
        this.mListener=listener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterMaterii.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_item_note,parent,false);
        return new RecyclerViewAdapterMaterii.MyViewHolder(view);
    }

    private final OnButtonClickListener mListener;

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterMaterii.MyViewHolder holder, int position) {
        holder.txt_materie.setText(materii.get(position).getDenumire());

        List<Float> listanote = materii.get(position).getListanote();

        StringBuilder noteStringuri = new StringBuilder();
        for (Float i : listanote) {
            noteStringuri.append(i).append("   ");
        }
        holder.txt_note.setText(noteStringuri.toString().trim());

//        holder.txt_note.setText(materii.get(position).getListanote().toString());
        holder.txt_option.setOnClickListener(view -> {
            //aici se va deschide un meniu, alertDialog
            PopupMenu popupMenu=new PopupMenu(this.context,holder.txt_option);
            popupMenu.inflate(R.menu.option_menu_fragmentnote);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId())
                {
                    case R.id.meniu_edit:

                        if (mListener != null) {
                            mListener.onEditClick(position);
                        }

//                        //se deschide alertDialog -trebuie sa aiba datele in el
//                        Dialog dialog = new Dialog(context);
//                        dialog.setContentView(R.layout.alertdialog_add);
//                        EditText edtNameMaterie = dialog.findViewById(R.id.edtNameMaterie);
//                        Button btn_saveNote = dialog.findViewById(R.id.btn_saveNote);
//                        Button btn_add = dialog.findViewById(R.id.btn_add);
//                        layout_listaNote = dialog.findViewById(R.id.layout_listaNote);
//
//                        dialog.show();

                        break;
                    case R.id.meniu_sterge:

                        if (mListener != null) {
                            mListener.onDeleteClick(position);
                        }

//                        if (position < materii.size()) {
//                            System.out.println("Pozitie stearsa "+ position+ " IAR LISTA OBIECTE MARIME: "+materii.size());
//                            materii.remove(position);
//                            notifyItemRemoved(position);
//                            System.out.println( "LISTA OBIECTE RAMASE: "+materii.size());
//                        }

                        break;
                }
                return false;
            });
            popupMenu.show();
        });

    }

    //adaugat
    public interface OnButtonClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    @Override
    public int getItemCount() {
        return materii.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_materie,txt_option,txt_note;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_materie=itemView.findViewById(R.id.txt_materie);
            txt_option=itemView.findViewById(R.id.txt_option);
            txt_note=itemView.findViewById(R.id.txt_note);


        }
    }
}
