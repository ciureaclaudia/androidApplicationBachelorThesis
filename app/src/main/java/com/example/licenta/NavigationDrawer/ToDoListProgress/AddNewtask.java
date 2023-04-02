package com.example.licenta.NavigationDrawer.ToDoListProgress;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.licenta.NavigationDrawer.ToDoListProgress.Model.ToDoModel;
import com.example.licenta.NavigationDrawer.ToDoListProgress.RoomDB.DBManagerRoom;
import com.example.licenta.NavigationDrawer.ToDoListProgress.Utils.DataBaseManager;
import com.example.licenta.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewtask  extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskText;
    private Button newTaskSaveButton;
    private DBManagerRoom db;
    ToDoModel taskCopie = new ToDoModel();

    public static AddNewtask newInstance() {
        return new AddNewtask();
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);


        newTaskText = requireView().findViewById(R.id.newTaskText);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            assert task != null;
            if (task.length() > 0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.lavander));
        }

//        db = new DataBaseManager(getActivity());
//        db.openDatabase();

//        db = DBManagerRoom.getInstance(getContext()); -> IRINA
        db= Room.databaseBuilder(getContext(), DBManagerRoom.class, "TabelItemDate").build();

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                } else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.lavander));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();
                if (finalIsUpdate) {
                    new Thread(() -> {
                        db.getDao().updateTask(bundle.getInt("id"), text);
                    }).start();

//                    db.updateTask(bundle.getInt("id"), text);
                } else {
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);
                    task.setStatus(0);

                    new Thread(() -> {
//            DBManagerRoom db = Room.databaseBuilder(getContext(), DBManagerRoom.class, "TabelItemDate").build();
                        long id=db.getDao().insertTask(task);
                        task.setId((int)id);
                        taskCopie=task;
                        //transmite catre progress, add to tasklist si notif adaptor cu notifyiteminserted(0)
                        getActivity().runOnUiThread(()->{
                            Log.d("IDTASK", task.getId()+"");
                        });
                    }).start();

//                    db.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity(); //get parent activity
        if(activity instanceof DialogCloseListener){
            //asta e o interfata care trebuie implementata
            ((DialogCloseListener)activity).handleDialogClose(dialog);

        }
        System.out.printf("todolist "+taskCopie.toString());
        Log.d("todolist",taskCopie.toString() );
    }


}
