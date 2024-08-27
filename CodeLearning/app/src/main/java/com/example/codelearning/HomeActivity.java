package com.example.codelearning;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (itemId == R.id.nav_document) {
            Intent intent = new Intent(HomeActivity.this, SelectQuestionActivity.class);
            startActivity(intent);
            Toast.makeText(this, "選擇測驗章節", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_checklist) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChecklistFragment()).commit();
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
            Toast.makeText(this, "查看排名", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RankingResult.class);
            startActivity(intent);
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