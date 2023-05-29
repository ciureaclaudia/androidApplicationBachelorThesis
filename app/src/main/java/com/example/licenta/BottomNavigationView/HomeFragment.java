package com.example.licenta.BottomNavigationView;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.licenta.R;



public class HomeFragment extends Fragment {

    TextView zi_curenta;
    TextView welcomeBack;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        zi_curenta=view.findViewById(R.id.zi_curenta);

        welcomeBack=view.findViewById(R.id.mesaj);
        ConstraintLayout constraintLayout=view.findViewById(R.id.mainLayout);

        AnimationDrawable animationDrawable= (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

        return view;
    }
}