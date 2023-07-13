package com.example.licenta.NavigationDrawer.GraficFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.licenta.BottomNavigationView.FragmentNote.Materie;
import com.example.licenta.NavigationDrawer.toDoList.Adapter.ToDoAdapter;
import com.example.licenta.NavigationDrawer.toDoList.Model.ToDoModel;
import com.example.licenta.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class GraficFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userID;
    DocumentReference documentReference;


    private Query query;
    private ListenerRegistration listenerRegistration;

    ArrayList<Materie> materiiNote;
    private List<ToDoModel> toDoList;//lista de taskuri


    Button btn_pieChart;
    Button btn_barChart;
    Button btn_regresie;
    PieChart pieChart;
    BarChart barChart;

    //pt bar chart-metoda calculateMetric
//    int metric;
//    HashMap<String, Integer> hashMap = new HashMap<>();
//    int countCheckBok;
//    int countCheckedBoxOk;
//    int sum;

    double barHeight = 0.0;
    HashMap<String, Double> hashMap = new HashMap<>();
    List<Integer> listaTotalBifate = new ArrayList<>();

    int countTaskHard;
    int countTaskEasy;
    int countTaskMedium;
    int countNotOk;
    int taskTotalBifat;

    //  pt searchbar
    // List View object
    ListView listView_searchBar;
    // Define array adapter for ListView
    ArrayAdapter<String> adapterSearBar;
    // Define array List for List View data
    ArrayList<String> mylistSearBar;

    ArrayList<String> materiiDenumireNote;
    ArrayList<String> materiiDenumireToDo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grafic, container, false);
        materiiNote = new ArrayList<>();
        toDoList = new ArrayList<>();
        mylistSearBar = new ArrayList<>();
        materiiDenumireNote = new ArrayList<>();
        materiiDenumireToDo = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance(); //preaiu instanta de la BD
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid(); //preaiu ID ul de la user
        documentReference = db.collection("users").document(userID);
        CollectionReference colectieNote = documentReference.collection("NoteMaterii"); //colectie
        CollectionReference colectieToDo = documentReference.collection("Tasks"); //colectie

        btn_pieChart = view.findViewById(R.id.btn_pieChart);
        btn_barChart = view.findViewById(R.id.btn_barChart);
        btn_regresie = view.findViewById(R.id.btn_regresie);
        pieChart = view.findViewById(R.id.pieChart);
        barChart = view.findViewById(R.id.barChart);
        listView_searchBar = view.findViewById(R.id.listView_regresie);

        //preaiu notele din BD
        if (materiiNote.size() == 0) {
            colectieNote.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot queryDocumentSnapshots1 : queryDocumentSnapshots) {
                        Materie materie = queryDocumentSnapshots1.toObject(Materie.class);
                        String materieID = queryDocumentSnapshots1.getId();
                        materie.setId(materieID);
                        materiiNote.add(materie);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("NOTOK", e.getMessage().toString());
                }
            });
        }


        //preau materiile de la To do
        if (toDoList.size() == 0) {
            colectieToDo.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot queryDocumentSnapshots1 : queryDocumentSnapshots) {
                        String id = queryDocumentSnapshots1.getId();
                        ToDoModel toDoModel = queryDocumentSnapshots1.toObject(ToDoModel.class).withId(id);
                        toDoList.add(toDoModel);
//                        Toast.makeText(getContext(), materii.get(0).getDenumire(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("NOTOK", e.getMessage().toString());
                }
            });
        }

        // Define a custom Comparator
        Comparator<ToDoModel> comparator = new Comparator<ToDoModel>() {
            @Override
            public int compare(ToDoModel model1, ToDoModel model2) {
                // First, compare by 'materie' property
                int materieComparison = model1.getMaterie().compareTo(model2.getMaterie());
                if (materieComparison != 0) {
                    return materieComparison;
                }

                // If 'materie' is the same, compare by 'due' property
                return model1.getDue().compareTo(model2.getDue());
            }
        };
        // Sort the list using the custom comparator
        Collections.sort(toDoList, comparator);


        btn_pieChart.setOnClickListener(view1 -> {
            listView_searchBar.setVisibility(View.GONE);
            setHasOptionsMenu(false);

            ArrayList<PieEntry> visitors = new ArrayList<>();

            String denumire;
            List<Float> listaNote;
            Float suma;
            Float medie;
            for (Materie m : materiiNote) {
                denumire = m.getDenumire();
                listaNote = m.getListanote();
                suma = Float.valueOf(0);
                for (Float f : listaNote) {
                    suma += f;
                }
                if (listaNote.size() != 0) {
                    medie = (suma / (listaNote.size()));
                    visitors.add(new PieEntry(medie, denumire));
                }
            }

            PieDataSet pieDataSet = new PieDataSet(visitors, "Visitors");
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            pieDataSet.setValueTextColor(Color.BLACK);
            pieDataSet.setValueTextSize(16f);

            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.getDescription().setEnabled(false);
            pieChart.setCenterText("Medii note");
            pieChart.animate();
            pieChart.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "piechart", Toast.LENGTH_SHORT).show();

        });

        btn_barChart.setOnClickListener(view1 -> {
            // Clear any existing data
            pieChart.setVisibility(View.GONE);
            listView_searchBar.setVisibility(View.GONE);
            setHasOptionsMenu(false);

            barChart.clear();

            // Create lists for storing bar entries and labels
            List<BarEntry> entries = new ArrayList<>();
            List<String> labels = new ArrayList<>();

            // Create a map to track the total task counts for each difficulty level
            Map<String, float[]> taskCountsMap = new HashMap<>();

            // Iterate through the toDoList and calculate task counts for each materie and difficulty level
            for (ToDoModel model : toDoList) {
                String materie = model.getMaterie();
                int dificultate = model.getDificultate() - 1;

                float[] taskCounts = new float[0];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    taskCounts = taskCountsMap.getOrDefault(materie, new float[3]);
                }
                assert taskCounts != null;
                taskCounts[dificultate]++;

                taskCountsMap.put(materie, taskCounts);
            }

            // Generate bar entries and labels based on the task counts
            int barIndex = 0;
            for (Map.Entry<String, float[]> entry : taskCountsMap.entrySet()) {
                String materie = entry.getKey();
                float[] taskCounts = entry.getValue();

                // Calculate the height for each difficulty level
                float easyHeight = taskCounts[0] * 5f;
                float mediumHeight = taskCounts[1] * 10f;
                float hardHeight = taskCounts[2] * 15f;

                // Create a stacked bar entry
                BarEntry barEntry = new BarEntry(barIndex, new float[]{easyHeight, mediumHeight, hardHeight});
                entries.add(barEntry);

                // Add the materie as a label
                labels.add(materie);
                barIndex++;
            }

            // Create a stacked bar dataset
            BarDataSet barDataSet = new BarDataSet(entries, "");
//            barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            barDataSet.setValueTextSize(10f);
            barDataSet.setStackLabels(new String[]{"Easy", "Medium", "Hard"});
// Define an array of custom colors for the bars
            int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED};

// Set the colors for the bars in the dataset
            barDataSet.setColors(colors);

            // Create a bar data object with the dataset
            BarData barData = new BarData(barDataSet);

            // Configure the X-axis and set labels
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                    xAxis.setTextSize(5f);
            xAxis.setGranularity(1f);
            xAxis.setDrawGridLines(false);

            // Configure the Y-axis
            YAxis yAxisLeft = barChart.getAxisLeft();
            yAxisLeft.setAxisMinimum(0f);

            // Hide the right Y-axis
            barChart.getAxisRight().setEnabled(false);

            // Set the bar data to the chart and invalidate it to refresh the view
            barChart.setData(barData);
            barChart.invalidate();
            barChart.animateY(1500);
            barChart.setVisibility(View.VISIBLE);
        });



/*
        btn_barChart.setOnClickListener(view1 -> {
            pieChart.setVisibility(View.GONE);

            ArrayList<BarEntry> visitorsBar = new ArrayList<>();

            calculateMetric();
            ArrayList<String> labels = new ArrayList<>(); //denumirea de materie
            float i = 0;

            for (Map.Entry<String, Double> entry : hashMap.entrySet()) {
                String key = entry.getKey();
                double value = entry.getValue();
                labels.add(key);
                visitorsBar.add(new BarEntry(i, (float) value));
                i++;
            }

            //populez visitorBar
//            visitorsBar.add(new BarEntry(2014, 420));
//            visitorsBar.add(new BarEntry(2015, 475));

            BarDataSet barDataSet = new BarDataSet(visitorsBar, "VisitorsBar");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(16f);


            BarData barData = new BarData(barDataSet);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getAxisLeft().setAxisMinimum(0f);
            barChart.setFitBars(true);
            barChart.setData(barData);
            barChart.getDescription().setText("Bar Chart exemplu");

            barChart.setEnabled(true);
            barChart.setDrawMarkers(true);
//            barChart.setMarker(new CustomMarkerView(this,R.layout.));

            barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    float x=e.getX();
                    float y=e.getY();
                    String subject=labels.get((int)x);
                    String tooltip = "Subiect: " + subject
                            + "\nTaskuri in total: " + (countTaskEasy+countTaskHard+countTaskMedium+countNotOk)
                            +"\nBifate: "+(countTaskEasy+countTaskHard+countTaskMedium);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Detalii")
                            .setMessage(tooltip)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Handle the positive button click

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Handle the negative button click
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }

                @Override
                public void onNothingSelected() {

                }
            });

            barChart.animateY(1500);
            barChart.setVisibility(View.VISIBLE);

        });


 */

        btn_regresie.setOnClickListener(view1 -> {
            pieChart.setVisibility(View.GONE);
            barChart.setVisibility(View.GONE);
            listView_searchBar.setVisibility(View.VISIBLE);
            setHasOptionsMenu(true);

            //searchbar
            //populez mylistSearBar
            for (Materie m : materiiNote)
                mylistSearBar.add(m.getDenumire());
            for (ToDoModel mm : toDoList)
                mylistSearBar.add(mm.getMaterie());

            // Convert the list to a set to remove duplicates
            HashSet<String> set = new HashSet<>(mylistSearBar);

            // Create a new list from the set
            mylistSearBar = new ArrayList<>(set);

            // Set adapter to ListView
            adapterSearBar = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mylistSearBar);
            listView_searchBar.setAdapter(adapterSearBar);

            listView_searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get the selected item from the ListView
                    String selectedItem = (String) parent.getItemAtPosition(position);

                    // Create an AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Confirmare")
                            .setMessage("Alegi sa continui cu materia " + selectedItem + "?")
                            .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // User clicked Yes, perform desired actions


                                    Toast.makeText(getContext(), "Proceeding with " + selectedItem, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Nu", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // User clicked No, perform desired actions or dismiss the dialog
                                    dialog.dismiss();
                                }
                            });
                    // Show the AlertDialog
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });


        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate menu with items using MenuInflator
        inflater.inflate(R.menu.regresie_searchbar_menu, menu);

        // Initialise menu item search bar
        // with id and take its object
        MenuItem searchViewItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        // attach setOnQueryTextListener
        // to search view defined above
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Override onQueryTextSubmit method which is called when submit query is searched
            @Override
            public boolean onQueryTextSubmit(String query) {
                // If the list contains the search query, then filter the adapter
                // using the filter method with the query as its argument
                if (mylistSearBar.contains(query)) {
                    adapterSearBar.getFilter().filter(query);
                } else {
                    // Search query not found in List View
                    Toast.makeText(getContext(), "Not found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            // This method is overridden to filter the adapter according
            // to a search query when the user is typing search
            @Override
            public boolean onQueryTextChange(String newText) {
                adapterSearBar.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    void calculateMetric() {
//        double metricc = 0.0;
//        HashMap<String, Double> hashMap = new HashMap<>();
//        int countCheckBok;
//        int countCheckedBoxOk;
//        int sum;
        //todoList si materiiNote

        for (ToDoModel mm : toDoList) {
            countTaskHard = 0;
            countTaskEasy = 0;
            countTaskMedium = 0;
            countNotOk = 0;
            barHeight = 0f;
            if (mm.getStatus() == 1) { //daca e bifat
                if (mm.getDificultate() == 0) {
                    barHeight += 10f;
                    countTaskEasy++;
                } else if (mm.getDificultate() == 1) {
                    barHeight += 15f;
                    countTaskMedium++;
                } else {
                    barHeight += 20f;
                    countTaskHard++;
                }
            } else {
                barHeight = 5f;
                countNotOk++;
            }
            hashMap.put(mm.getMaterie(), barHeight);
            taskTotalBifat = countTaskEasy + countTaskMedium + countNotOk + countTaskHard;
            listaTotalBifate.add(taskTotalBifat);
        }


    }


//    void calculateMetric() {
////        int metric;
////        HashMap<String, Integer> hashMap=new HashMap<>();
////        int countCheckBok;
////        int countCheckedBoxOk;
////        int sum;
//        //todoList si materiiNote
//
//        for (Materie m : materiiNote) {
//            countCheckBok = 0;
//            countCheckedBoxOk = 0;
//            metric = 0;
//            for (ToDoModel mm : toDoList) {
//                sum = 0;
//                if (m.getDenumire().equals(mm.getMaterie())) {
//                    //o materie la care s-au introdus note are si taskuri introduse
//                    countCheckBok++;//exista un check box
//                    if (mm.getStatus() == 1) { //daca e bifat
//                        countCheckedBoxOk++;
//                        if (mm.getDificultate() == 0) {
//                            metric += 1;
//                        } else if (mm.getDificultate() == 1) {
//                            metric += 2;
//                        } else metric += 3;
//                    } else {
//                        //nu e bifat
//                        metric++;
//                    }
//                }
//                //daca nu se gaseste se calc doar asta
//                for (Float f : m.getListanote()) {
//                    sum += f;
//                }
//                if ((int) sum / (m.getListanote().size()) > 5) {
//                    //notele sunt mai mari de 5
//                    metric += 3;
//                } else metric+=2;
//            }
//            hashMap.put(m.getDenumire(),metric);
//        }
//
//    }


}