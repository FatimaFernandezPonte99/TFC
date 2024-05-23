package com.example.bookbuddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class InfoLibro extends Fragment {
    private TextView titulo;
    private TextView autor;
    private TextView paginas;
    private TextView Texteditorial;
    private TextView resena;
    private Button bot_seleccionar_libro;
    RequestQueue queue;



    public InfoLibro() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_libro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Inicializamos los TextView
        titulo = view.findViewById(R.id.info_titulo_libro);
        autor = view.findViewById(R.id.info_autor_libro);
        paginas = view.findViewById(R.id.info_paginas_libro);
        Texteditorial = view.findViewById(R.id.info_editorial_libro);
        resena = view.findViewById(R.id.info_resena_libro);

        queue = Volley.newRequestQueue(getContext());

        //Obtenemos el título del libro del que queremos mostrar la información
        String title = getArguments().getString("title", "a");

        //Lanzamos la petición
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Server.name + "/api/BookBuddy/book_info/" + title,//NO SÉ SI ESTO VA
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Extramos los datos de la respuesta JSON
                            String title = response.getString("title");
                            String author = response.getString("author");
                            String num_pages = response.getString("num_pages");
                            String editorial = response.getString("editorial");
                            String review = response.getString("review");

                            //Utilizamos los datos de la respuesta para mostrar información sobre el libro
                            titulo.setText(title);
                            autor.setText(author);
                            paginas.setText(num_pages);
                            Texteditorial.setText(editorial);
                            resena.setText(review);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error al obtener los datos del libro", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                // Adjuntar el token de usuario a los encabezados de la solicitud
                Map<String, String> headers = new HashMap<>();
                headers.put("token", Server.token);
                return headers;
            }
        };
        //Agreamos la solicitud a la cola de Volley
        queue.add(request);


        //PRUEBA PARA SELECCIONAR LIBRO
        queue = Volley.newRequestQueue(getContext());//ESTO NO SÉ SI ES ASÍ
        bot_seleccionar_libro = view.findViewById(R.id.bot_confirmar_cambios);

        bot_seleccionar_libro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //QUE SALGA EL POPUP Y LUEGO YA EL PATCH
                //Creamos el AlertDialog (pop up)
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle);
                builder.setMessage("Si confirmas, el libro dejará de estar disponible en el puesto");

                //Botón positivo
                builder.setPositiveButton("Confirmar selección", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Lanzamos el PATCH
                        JsonObjectRequest request1 = new JsonObjectRequest(
                                Request.Method.PATCH,
                                Server.name + "/api/BookBuddy/exchanged_book/" + title,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //DESPUÉS, HACER UN POP UP
                                        Toast.makeText(getContext(), "Has cogido este libro del stand", Toast.LENGTH_LONG).show();
                                        Fragment myFragment = new MapsFragment();
                                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack(null).commit();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if (error.networkResponse == null) {
                                            Toast.makeText(getContext(), "La conexión no se ha establecido", Toast.LENGTH_LONG).show();
                                        } else {
                                            int serverCode = error.networkResponse.statusCode;
                                            Toast.makeText(getContext(), "Estado de respuesta"+serverCode, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                        )
                        {
                            @Override
                            public Map<String, String> getHeaders() {
                                // Adjuntar el token de usuario a los encabezados de la solicitud
                                Map<String, String> headers = new HashMap<>();
                                headers.put("token", Server.token);
                                return headers;
                            }
                        };
                        //ESTO NO SÉ SI ES ASÍ
                        queue.add(request1);
                    }
                });

                builder.setNegativeButton("Cancelar selección", new DialogInterface.OnClickListener() {
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


}