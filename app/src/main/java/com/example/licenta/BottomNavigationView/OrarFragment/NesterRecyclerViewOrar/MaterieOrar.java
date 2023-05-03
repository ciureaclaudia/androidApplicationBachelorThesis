package com.example.licenta.BottomNavigationView.OrarFragment.NesterRecyclerViewOrar;

public class MaterieOrar {
    private String denumire;
    private String sala;
    private String profesor;
    private String ziSapt;
    private int oraInceput,minutIneput, oraSfarsit,minutSfarsit;

    public MaterieOrar(String denumire, String sala, String profesor, String ziSapt, int oraInceput, int minutIneput, int oraSfarsit, int minutSfarsit) {
        this.denumire = denumire;
        this.sala = sala;
        this.profesor = profesor;
        this.ziSapt = ziSapt;
        this.oraInceput = oraInceput;
        this.minutIneput = minutIneput;
        this.oraSfarsit = oraSfarsit;
        this.minutSfarsit = minutSfarsit;
    }

    public String getDenumire() {
        return denumire;
    }

    public String getSala() {
        return sala;
    }

    public String getProfesor() {
        return profesor;
    }

    public String getZiSapt() {
        return ziSapt;
    }

    public int getOraInceput() {
        return oraInceput;
    }

    public int getMinutIneput() {
        return minutIneput;
    }

    public int getOraSfarsit() {
        return oraSfarsit;
    }

    public int getMinutSfarsit() {
        return minutSfarsit;
    }
}


