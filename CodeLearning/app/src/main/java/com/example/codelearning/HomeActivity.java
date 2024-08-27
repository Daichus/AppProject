package com.example.codelearning;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    ArrayList<String> todoArrayList = new ArrayList<>();
    private NavigationView navigationView;
    private ActivityResultLauncher<Intent> intentActivityResultLanucher;

    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intentActivityResultLanucher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getData() != null && o.getResultCode() == Activity.RESULT_OK){
                            String action = o.getData().getStringExtra("ACTION");
                            if(action.equals("new")){
                                String todo = o.getData().getStringExtra("TODO");
                                Log.d("TODO", todo);
                                if (homeFragment != null && homeFragment.isAdded()) {
                                    homeFragment.addTodoItem(todo);
                                }
//                                todoArrayList.add(todo);
                            }
                        }
                    }
                }
        );

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        long userid = intent.getLongExtra("_ID",-1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, homeFragment)
                    .commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }else {
            homeFragment = (HomeFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_container);
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d("onResume", "onResume");
//
//        processIncomingData();
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        Log.d("NEW_INTENT", "new intent");
//
//        setIntent(intent);
//        processIncomingData();
//    }
//
//    private void processIncomingData() {
//
//
//        Intent intent = getIntent();
//        String todo = intent.getStringExtra("TODO");
//        if (todo != null && !todo.isEmpty()) {
//            Log.d("TODO", todo);
//            if (homeFragment != null && homeFragment.isAdded()) {
//                homeFragment.addTodoItem(todo);
//            }
//            intent.removeExtra("TODO");
//        }else {
//            Log.d("TODO", "NULL");
//        }
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Intent getIntent = getIntent();
        if (itemId == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (itemId == R.id.nav_document) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DocumentFragment()).commit();
        } else if (itemId == R.id.nav_checklist) {
            Intent intent = new Intent(this, ChecklistActivity.class);
            // 設定一個bundle來放資料
            Bundle bundle = new Bundle();
            bundle.putString("ACTION", "new");

            // 利用intent攜帶bundle的資料
            intent.putExtras(bundle);
            intentActivityResultLanucher.launch(intent);
        } else if (itemId == R.id.nav_timer) {
            Intent intent = new Intent(HomeActivity.this, TimerActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_note){
            Intent intentFromLogin_ID = getIntent();
            long userid = intentFromLogin_ID.getLongExtra("_ID", -1);

            Intent intent = new Intent(HomeActivity.this, NewNoteActivity.class);
            intent.putExtra("_ID", userid);
            startActivity(intent);
        } else if (itemId == R.id.nav_person) {
        // 接收從 LoginActivity 傳遞過來的 username
        Intent intentFromLogin_name = getIntent();
        String username = intentFromLogin_name.getStringExtra("username");

        // 傳遞 username 到 MemberInfoActivity
        Intent intent = new Intent(HomeActivity.this, MemberInfoActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        } else if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return  true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}