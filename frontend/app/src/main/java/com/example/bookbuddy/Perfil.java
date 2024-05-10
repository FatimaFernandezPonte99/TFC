package com.example.bookbuddy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Perfil extends Fragment {
    private Button botModPerfil;
    private Button botHistorial;
    private Button botCerrarSesion;

    public Perfil() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Inicializamos los botones
        botModPerfil = view.findViewById(R.id.boton_mod_perfil);
        botHistorial = view.findViewById(R.id.boton_historial);
        botCerrarSesion = view.findViewById(R.id.boton_cerrar_sesion);

        //Configuramos los listener para los botones

        //Modificar perfil
        botModPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new ModPerfil();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, myFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //Historial de libros subidos
        botHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new HistorialLibrosSubidos();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, myFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //Cerrar sesión
        //Va a ser un PopUp
    }
}