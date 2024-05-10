package com.example.bookbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Menu extends AppCompatActivity {
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Ponemos de principal el fragment Inicio
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new MapsFragment()).commit();

        BottomNavigationView bar = findViewById(R.id.bottomNavigation);

        //Configuramos los clics
        bar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.inicio) {
                    Fragment myFragment = new MapsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).commit();
                }

                if (item.getItemId() == R.id.subirlibro) {
                    Fragment myFragment = new SubirLibro();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).commit();
                }

                if (item.getItemId() == R.id.perfil) {
                    Fragment myFragment = new Perfil();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).commit();
                }
                return true;
            }
        });


    }
}