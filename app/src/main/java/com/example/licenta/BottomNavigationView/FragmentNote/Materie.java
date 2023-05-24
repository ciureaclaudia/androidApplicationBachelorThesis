package com.example.licenta.BottomNavigationView.FragmentNote;

import java.util.ArrayList;
import java.util.List;

public class Materie {
    private String denumire;
    private List<Float> listanote = new ArrayList<>();
    private String id;
    public Materie() {

    }

    public Materie(String denumire, List<Float> listanote) {
        this.denumire = denumire;
        this.listanote = listanote;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public List<Float> getListanote() {
        return listanote;
    }

    public void setListanote(List<Float> listanote) {
        this.listanote = listanote;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Materie{");
        stringBuilder.append("denumire='").append(denumire).append('\'');
        stringBuilder.append(", listanote=");

        // Iterate over each element in the list and append it to the string
        for (int i = 0; i < listanote.size(); i++) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(listanote.get(i));
        }

        stringBuilder.append('}');
        return stringBuilder.toString();
    }

}
