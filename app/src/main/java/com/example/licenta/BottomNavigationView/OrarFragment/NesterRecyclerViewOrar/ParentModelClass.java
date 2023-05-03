package com.example.licenta.BottomNavigationView.OrarFragment.NesterRecyclerViewOrar;

import android.content.Intent;

import com.google.common.hash.HashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParentModelClass {
    String weekDay;
    Integer id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParentModelClass that = (ParentModelClass) o;
        return Objects.equals(weekDay, that.weekDay) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weekDay, id);
    }

    public ParentModelClass(String weekDay, Integer id) {
        this.weekDay = weekDay;
        this.id=id;
    }
}
