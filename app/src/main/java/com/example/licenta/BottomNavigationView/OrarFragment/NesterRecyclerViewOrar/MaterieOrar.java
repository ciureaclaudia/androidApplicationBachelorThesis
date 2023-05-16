package com.example.licenta.BottomNavigationView.OrarFragment.NesterRecyclerViewOrar;

public class MaterieOrar {
    private String denumire;
    private String sala;
    private String profesor;
    private String ziSapt;
    private String Inceput,Sfarsit;

    public MaterieOrar(){}

    public MaterieOrar(String denumire, String sala, String profesor, String ziSapt, String inceput, String sfarsit) {
        this.denumire = denumire;
        this.sala = sala;
        this.profesor = profesor;
        this.ziSapt = ziSapt;
        Inceput = inceput;
        Sfarsit = sfarsit;
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

    public String getInceput() {
        return Inceput;
    }

    public String getSfarsit() {
        return Sfarsit;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public void setZiSapt(String ziSapt) {
        this.ziSapt = ziSapt;
    }

    public void setInceput(String inceput) {
        Inceput = inceput;
    }

    public void setSfarsit(String sfarsit) {
        Sfarsit = sfarsit;
    }
}


