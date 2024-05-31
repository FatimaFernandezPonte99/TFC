package com.example.bookbuddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


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
        //Creamos el AlertDialog (pop up)
        botCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle);
                builder.setMessage("¿Seguro que quieres cerrar sesión?");

                //Botón positivo
                builder.setPositiveButton("Cerrar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Método DELETE para cerrar sesión
                        JsonObjectRequest request = new JsonObjectRequest(
                                Request.Method.DELETE,
                                Server.name + "/api/BookBuddy/login",
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //Mostrar mensaje de éxito y volver a MainActivity
                                        Toast.makeText(getContext(), "Sesión cerrada con éxito", Toast.LENGTH_SHORT).show();
                                        volveraMainActivity();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Manejamos errores de Volley
                                if(error.networkResponse == null) {
                                    Toast.makeText(getContext(), "error: Internal Server Error", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(),"error: " + error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                //Adjuntamos el token de usuario al encabezado de la solicitud
                                Map<String, String> headers = new HashMap<>();
                                headers.put("token", Server.token);
                                return headers;
                            }
                        };
                        // Agregar la solicitud a la cola de Volley
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(request);
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Se cierra el pop up sin más
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    public void volveraMainActivity(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        //Obtenemos el FragmentManager
        FragmentManager fragmentManager = getParentFragmentManager();

        //Limpiamos la pila de fragmentos para que no haya fragmentos en la parte superior
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        //Obtenemos el AppCompatActivity y finalizarlo
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        startActivity(intent);
        //Ponemos el token a null en la clase Server
        Server.token = null;
        activity.finish();

    }
}