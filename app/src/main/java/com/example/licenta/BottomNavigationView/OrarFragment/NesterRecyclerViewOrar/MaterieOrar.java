package com.example.licenta.BottomNavigationView.OrarFragment.NesterRecyclerViewOrar;

public class MaterieOrar {
    private String denumire;
    private String sala;
    private String profesor;
    private String ziSapt;
    private String oraInceput,minutIneput, oraSfarsit,minutSfarsit;

    public MaterieOrar(String denumire, String sala, String profesor, String ziSapt, String oraInceput, String minutIneput, String oraSfarsit, String minutSfarsit) {
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

    public String getOraInceput() {
        return oraInceput;
    }

    public String getMinutIneput() {
        return minutIneput;
    }

    public String getOraSfarsit() {
        return oraSfarsit;
    }

    public String getMinutSfarsit() {
        return minutSfarsit;
    }
}


