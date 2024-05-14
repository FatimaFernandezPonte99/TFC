package com.example.bookbuddy;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubirLibro extends Fragment {
    private EditText editTextTitulo;
    private EditText editTextAutor;
    private EditText editTextEditorial;
    private EditText editTextResena;
    private EditText ediTextPaginas;
    private Button botSubirLibro;
    private RequestQueue queue;
    private Spinner spinner;
    public SubirLibro() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subir_libro, container, false);

    }

    //Llamamos al método después de que la vista haya sido creada

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Inicializamos los EditText y el Botón
        editTextTitulo = view.findViewById(R.id.texto_nombre_libro);
        editTextAutor = view.findViewById(R.id.texto_autor);
        editTextEditorial = view.findViewById(R.id.texto_editorial);
        editTextResena = view.findViewById(R.id.texto_resena);
        ediTextPaginas = view.findViewById(R.id.texto_paginas);
        botSubirLibro = view.findViewById(R.id.boton_subir_libro);

        //SPINNER
        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.stands, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //CON UN GETINTEXTRA QUE TE MANDE EL ID DEL PUESTO

        //Configuramos el listener para el botón
        botSubirLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    subirLibro();
                    //Lanzamos de nuevo el fragment para que los campos vuelvan a aparecer en blanco
                    Fragment myFragment = new SubirLibro();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, myFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        queue = Volley.newRequestQueue(getContext());

    }

    private void subirLibro() throws JSONException {
        JSONObject requestBody = new JSONObject();

        String stand = spinner.getSelectedItem().toString();
        //CONSEGUIMOS EL ID DEL STAND
        int stand_id = 1;
        if (stand.equalsIgnoreCase("Puesto Calle San Andrés")) {
            stand_id = 1;
        } else if (stand.equalsIgnoreCase("Puesto Calle Real")) {
            stand_id = 2;
        } else if (stand.equalsIgnoreCase("Puesto Los Mallos")) {
            stand_id = 3;
        } else if (stand.equalsIgnoreCase("Puesto Elviña")) {
            stand_id = 4;
        } else if (stand.equalsIgnoreCase("Puesto Monte Alto"))  {
            stand_id =5;
        } else if (stand.equalsIgnoreCase("Puesto Espacio Coruña")) {
            stand_id = 6;
        } else if (stand.equalsIgnoreCase("Puesto Matogrande")) {
            stand_id = 7;
        }

        String title = editTextTitulo.getText().toString();
        String author = editTextAutor.getText().toString();
        String editorial = editTextEditorial.getText().toString();
        String review = editTextResena.getText().toString();
        String num_pages = ediTextPaginas.getText().toString();

        try {
            requestBody.put("title", title);
            requestBody.put("author", author);
            requestBody.put("editorial", editorial);
            requestBody.put("review", review);
            requestBody.put("num_pages", num_pages);
        }catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Server.name + "/api/BookBuddy/book/" + stand_id,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Lanza un Toast si el libro ha sido subido con éxito
                        Toast.makeText(getContext(), "Libro subido con éxito!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            Toast.makeText(getContext(), "La conexión no se ha establecido", Toast.LENGTH_LONG).show();
                        } else {
                            int serverCode = error.networkResponse.statusCode;
                            Toast.makeText(getContext(), "Estado de respuesta "+serverCode, Toast.LENGTH_LONG).show();
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
        //Añadimos la solicitud a la cola de Volley
        queue.add(request);
    }
}