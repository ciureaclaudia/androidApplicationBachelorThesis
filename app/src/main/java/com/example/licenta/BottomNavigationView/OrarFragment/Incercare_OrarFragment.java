package com.example.licenta.BottomNavigationView.OrarFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.licenta.BottomNavigationView.FragmentNote.Materie;
import com.example.licenta.R;

import java.util.ArrayList;


public class Incercare_OrarFragment extends Fragment {
    GridLayout gridLayout,gridLayoutMaterie;
    ArrayList<Materie> listaMateriiZi=new ArrayList<>(); //lista de materii intr-o zi


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incercare__orar, container, false);


        gridLayout = view.findViewById(R.id.gridLayout_zileSapt); //pt zilele sapt

        TextView luniTV = new TextView(getContext());
        setTextViewZileSapt(luniTV,"Luni",23,10);
        gridLayout.addView(luniTV);

        TextView martiTV = new TextView(getContext());
        setTextViewZileSapt(martiTV,"Marti",23,10);
        gridLayout.addView(martiTV);

        TextView miercuriTV = new TextView(getContext());
        setTextViewZileSapt(miercuriTV,"Miercuri",23,10);
        gridLayout.addView(miercuriTV);

        TextView joiTV = new TextView(getContext());
        setTextViewZileSapt(joiTV,"Joi",23,10);
        gridLayout.addView(joiTV);

        TextView vineriTV = new TextView(getContext());
        setTextViewZileSapt(vineriTV,"Vineri",23,10);
        gridLayout.addView(vineriTV);

        gridLayoutMaterie = view.findViewById(R.id.gridLayout_ore); //pt materii

        TextView MateTv = new TextView(getContext());
        MateTv.setText("Mate");
        MateTv.setTextSize(20);
        MateTv.setGravity(Gravity.CENTER);
        gridLayoutMaterie.addView(MateTv);

        TextView PooTv = new TextView(getContext());
        PooTv.setText("Poo");
        PooTv.setTextSize(20);
        PooTv.setGravity(Gravity.CENTER);
        gridLayoutMaterie.addView(PooTv);


        // Get the child view at row 1 and column 2
//        View childView = gridLayout.getChildAt(3);

        // Cast the child view to a TextView and set its text
//        if (childView instanceof TextView) {
//            TextView luniTV = (TextView) childView;
//            luniTV.setText("Luni");
//            luniTV.setGravity(Gravity.CENTER);
//            luniTV.setBackgroundResource(R.drawable.orar_cell_backround);
//            gridLayout.addView(luniTV);
//        }

        return view;

    }

    public void setTextViewZileSapt(TextView tv, String zi, int textSize ,int padding){
        tv.setText(zi);
        tv.setTextSize(textSize);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(padding,padding,padding,padding);
//        tv.setBackgroundResource(R.drawable.orar_cell_backround);

    }
}