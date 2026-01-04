package com.example.examquestionbank;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainNavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);

        // 设置底部导航栏
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // 配置AppBar，确保底部导航栏的每个目的地都有对应的标题
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.questionBankFragment,
                R.id.questionImportFragment,
                R.id.profileFragment)
                .build();

        // 将底部导航栏与导航控制器关联
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}