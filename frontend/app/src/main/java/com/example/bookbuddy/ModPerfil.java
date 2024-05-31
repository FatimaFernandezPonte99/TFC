package com.example.bookbuddy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


public class ModPerfil extends Fragment {
    private RequestQueue queue;
    private EditText editTextNuevoNombre;
    private EditText editTextNuevaContrasena;
    private EditText editTextNuevoPais;
    private Button botConfirmarCambios;


    public ModPerfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mod_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        queue = Volley.newRequestQueue(getContext());

        //Inicializamos el Botón
        botConfirmarCambios = view.findViewById(R.id.bot_confirmar_cambios);

        //Configuramos un listener para el botón
        botConfirmarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Método para el PATCH
                sendPatchModInfo();
            }
        });

        queue = Volley.newRequestQueue(getContext());
    }

    public void sendPatchModInfo() {
        //Inicializamos los EditText
        editTextNuevoNombre = getView().findViewById(R.id.texto_nuevo_nombre_usuario);
        editTextNuevaContrasena = getView().findViewById(R.id.texto_nueva_contrasena);
        editTextNuevoPais = getView().findViewById(R.id.texto_nuevo_pais);

        String name = editTextNuevoNombre.getText().toString();
        String password = editTextNuevaContrasena.getText().toString();
        String country = editTextNuevoPais.getText().toString();

        //Creamos y llenamos el json
        JSONObject requestBody = new JSONObject();
        try {
            //Antes de cada campo, verificamos que no está sin cubrir
            if (!name.equals("")) {
                requestBody.put("name", name);
            }
            if (!password.equals("")) {
                requestBody.put("password", password);
            }
            if (!country.equals("")) {
                requestBody.put("country", country);
            }
        }catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //Instanciamos un JsonObjectRequest
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PATCH,
                Server.name + "/api/BookBuddy/update_profile",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), "Has confirmado los cambios", Toast.LENGTH_LONG).show();
                        //Te lleva de nuevo al perfil
                        Fragment myFragment = new Perfil();
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
        //Añadimos la solicitud a la cola de Volley
        queue.add(request);
    }
}