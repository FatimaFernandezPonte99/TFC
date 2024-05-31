package com.example.bookbuddy;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HistorialLibrosSubidos extends Fragment {
    private Context context;
    private RequestQueue requestQueue;
    private LibroAdapter2 libroAdapter2;
    private List<LibroData> allLibros;
    private TextView rango;
    private ImageView medalla;



    public HistorialLibrosSubidos() {allLibros = new ArrayList<>();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_historial_libros_subidos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        requestQueue = Volley.newRequestQueue(context);

        showRecyclerView(view);
    }

    private void showRecyclerView (View view) {
        //Limpiamos la lista porque, si no, al volver atrás, aparecen los libros por duplicado
        allLibros.clear();

        //Iniciamos el TextView y el ImageView
        rango = view.findViewById(R.id.status);
        medalla = view.findViewById(R.id.medalla);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);


        //Lanzamos la petición
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                Server.name + "/api/BookBuddy/books_history",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonElement = response.getJSONObject(i);
                                LibroData libro = new LibroData(jsonElement.getString("title"), jsonElement.getString("author"), jsonElement.getInt("num_pages"));
                                allLibros.add(libro);
                            }
                            //Comprobamos cuantos libros hay en la lista para determinar el rango del usuario
                            if (allLibros.size() < 6) {
                                rango.setText("BRONCE");
                                medalla.setImageResource(R.drawable.medalla_bronce);
                            } else if (allLibros.size() < 16) {
                                rango.setText("PLATA");
                                medalla.setImageResource(R.drawable.medalla_plata);
                            } else if (allLibros.size() >= 16) {
                                rango.setText("ORO");
                                medalla.setImageResource(R.drawable.medalla_oro);
                            }

                            //Configuramos el RecyclerView con el adaptador de libros
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            libroAdapter2 = new LibroAdapter2(allLibros, requireActivity());
                            recyclerView.setAdapter(libroAdapter2);


                        } catch (JSONException e) {
                            // Manejar errores al procesar la respuesta JSON
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar errores de Volley
                if(error.networkResponse == null) {
                    Toast.makeText(context, "error: Internal Server Error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context,"error: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                // Adjuntar el token de usuario a los encabezados de la solicitud
                Map<String, String> headers = new HashMap<>();
                headers.put("token", Server.token);
                return headers;
            }
        };

        //Agregamos la solicitud a la cola de Volley
        requestQueue.add(request);
    }
}