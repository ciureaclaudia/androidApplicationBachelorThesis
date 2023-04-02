package com.example.licenta.BottomNavigationView.OrarFragment;

public class NumeMaterieOrar {
    private int index;
    private String nume;
    private String sala;
    private int oraIncepere;
    private int oraFinalizare;

    public NumeMaterieOrar(int index, String nume, String sala, int oraIncepere, int oraFinalizare) {
        this.index = index;
        this.nume = nume;
        this.sala = sala;
        this.oraIncepere = oraIncepere;
        this.oraFinalizare = oraFinalizare;
    }

    public int getIndex() {
        return index;
    }

    public String getNume() {
        return nume;
    }

    public int getOraIncepere() {
        return oraIncepere;
    }

    public int getOraFinalizare() {
        return oraFinalizare;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    @Override
    public String toString() {
        return "NumeMaterieOrar{" +
                "index=" + index +
                ", nume='" + nume + '\'' +
                ", sala='" + sala + '\'' +
                ", oraIncepere=" + oraIncepere +
                ", oraFinalizare=" + oraFinalizare +
                '}';
    }
}
