package com.csgradqau.terminalexamsectionb;
import com.csgradqau.terminalexamsectionb.Database.DatabaseHelper;
import com.csgradqau.terminalexamsectionb.Database.model.task;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.csgradqau.terminalexamsectionb.utils.MyDividerItemDecoration;
import com.csgradqau.terminalexamsectionb.utils.RecyclerTouchListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private taskAdapter mAdapter;
    private List<task> notesList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView emptytask;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        emptytask = findViewById(R.id.empty_task_view);

        db = new DatabaseHelper(this);

        notesList.addAll(db.getAllTasks());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTaskDialog(false, null, -1);
            }
        });

        mAdapter = new taskAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyTasks();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Toast.makeText(getBaseContext(),"Pressed", Toast.LENGTH_LONG).show();
                final Dialog dialog = new Dialog(getBaseContext()); // Context, this, etc.
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle("Task");
                TextView title = (TextView)dialog.findViewById(R.id.dialog_tastTitle);
                TextView det = (TextView)dialog.findViewById(R.id.dialog_Details);
                TextView dd = (TextView)dialog.findViewById(R.id.dialog_deadline);
                task t = db.getTask(position);
                title.setText(t.getTitle().toString());
                det.setText(t.getTaskDetails().toString());
                dd.setText(t.getDeadline().toString());
                dialog.show();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    /**
     * Inserting new note in db
     * and refreshing the list
     */
    private void addTask(int i, String title, String details, String deadline) {
        // inserting note in db and getting
        // newly inserted note id
        task t = new task(i,title,details,deadline);
        long id = db.addTask(t);

        // get the newly inserted note from db
        task n = db.getTask(id);

        if (n != null) {
            // adding new note to array list at 0 position
            notesList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyTasks();
        }
    }

    private  void showTask(final boolean update, final task t, final int position)
    {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.task_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);
        final EditText inputTitle = view.findViewById(R.id.dialog_tastTitle);
        final EditText inputDetails = view.findViewById(R.id.dialog_Details);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
    }

    private void showTaskDialog(final boolean shouldUpdate, final task t, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.task_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputTitle = view.findViewById(R.id.dialog_tastTitle);
        final EditText inputDetails = view.findViewById(R.id.dialog_Details);
        final EditText inputDeadline = view.findViewById(R.id.dialog_deadline);
        inputDeadline.setKeyListener(null);
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        final MaterialDatePicker mdp = builder.build();
        mdp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                inputDeadline.setText(mdp.getHeaderText());
            }
        });

        inputDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdp.show(getSupportFragmentManager(),"DOB_PICKER");
            }
        });
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? "New Task" : "");

        if (shouldUpdate && t != null) {
            inputTitle.setText(t.getTitle());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        task t = new task(1, inputTitle.getText().toString(),inputDetails.getText().toString(),inputDeadline.getText().toString());
                            db.addTask(t);
                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputTitle.getText().toString())||TextUtils.isEmpty(inputDetails.getText().toString())||TextUtils.isEmpty((inputDeadline.getText().toString()))) {
                    Toast.makeText(MainActivity.this, "You left something blank", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                //task a = new task(1, inputTitle.getText().toString(),inputDetails.getText().toString(),inputDeadline.getText().toString());
                    addTask(1, inputTitle.getText().toString(),inputDetails.getText().toString(),inputDeadline.getText().toString());
            }
        });
    }

    /**
     * Toggling list and empty notes view
     */
    private void toggleEmptyTasks() {
        // you can check notesList.size() > 0

        if (db.getTaskCount() > 0) {
            emptytask.setVisibility(View.GONE);
        } else {
            emptytask.setVisibility(View.VISIBLE);
        }
    }
}