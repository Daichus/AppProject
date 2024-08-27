package com.example.codelearning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class HomeFragment extends Fragment{
    private ListView todoLV;
    private ArrayList<String> todoArrayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ActivityResultLauncher<Intent> checklistLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checklistLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                String action = data.getStringExtra("ACTION");
                                if ("new".equals(action)) {
                                    String todo = data.getStringExtra("TODO");
                                    if (todo != null && !todo.isEmpty()) {
                                        addTodoItem(todo);
                                        Log.d("HomeFragment", "Added new todo: " + todo);
                                    }
                                }
                            }
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        todoLV = view.findViewById(R.id.todoLV);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, todoArrayList);
        todoLV.setAdapter(adapter);

        Button newButton = (Button) view.findViewById(R.id.newButton);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChecklistActivity.class);
                intent.putExtra("ACTION", "new");
                checklistLauncher.launch(intent);
            }
        });
        return view;
    }

    public void addTodoItem(String todo) {
        if (todo != null && !todo.isEmpty()) {
            todoArrayList.add(todo);
            refreshTodoLV();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshTodoLV();
    }

    public void refreshTodoLV() {
        if (todoLV == null) {
            Log.e("HomeFragment", "ListView is null. Make sure it exists in the layout.");
            return;
        }

        if (todoArrayList.isEmpty()) {
            ArrayList<String> empty = new ArrayList<>();
            empty.add("請先加入今天目標");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, empty);
            todoLV.setAdapter(adapter);
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, todoArrayList);
            todoLV.setAdapter(adapter);
            todoLV.setOnItemClickListener((adapterView, view, i, l) -> {
                // 處理點擊事件
            });
            todoLV.setOnItemLongClickListener((adapterView, view, itemIndex, l) -> {
                // 處理長按事件
                showDeleteDialog(itemIndex);
                return true;
            });
        }
    }

    private void showDeleteDialog(int itemIndex) {
        new AlertDialog.Builder(requireContext())
                .setTitle("確定刪除？")
                .setMessage("確定刪除這個目標？")
                .setPositiveButton("確定", (dialog, which) -> {
                    todoArrayList.remove(itemIndex);
                    refreshTodoLV();
                })
                .setNeutralButton("取消", null)
                .show();
    }

}