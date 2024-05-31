package com.example.bookbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
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

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    private final Context context = this;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private RequestQueue queue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Así va al menú si existe token
        if (Server.token != null) {
            Intent intent = new Intent (MainActivity.this, Menu.class);
            startActivity(intent);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Inicializamos los botones y demás
        Button botIniciarSesion = findViewById(R.id.boton_iniciar_sesion);
        Button botCrearCuenta = findViewById(R.id.boton_registro);
        editTextUsername = findViewById(R.id.nombre_usuario);
        editTextPassword = findViewById(R.id.contrasena);

        //Le asignamos al EditText de la contraseña un InputType para que no se vea cuando la escribas
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        //Registro
        botCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                   sendRegisterRequest();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //Iniciar sesión
        botIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Añade la solicitud a la cola
                try {
                    sendPostLogin();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        queue = Volley.newRequestQueue(context);

    }

    //Método para iniciar sesión
    private void sendPostLogin() throws JSONException {

        JSONObject requestBody = new JSONObject();

        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        try {
            requestBody.put("name", username);
            requestBody.put("password", password);
        }catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Server.name + "/api/BookBuddy/login",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Extraemos el token de la respuesta
                            String token = response.getString("token");

                            //Guardamos el token en la clase Server
                            Server.setToken(token);

                            //Va al Menú si el inicio de sesión es correcto
                            Intent intent = new Intent(MainActivity.this, Menu.class);
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Toast.makeText(context, "El servidor respondió con " + error.networkResponse.statusCode, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Error de conexión", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
        //Añadimos la solicitud a la cola de Volley
        queue.add(request);
    }

    //Método para el registro
    private void sendRegisterRequest() throws  JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", editTextUsername.getText().toString());
        jsonObject.put("password", editTextPassword.getText().toString());

        JsonObjectRequest petition = new JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                Server.name + "/api/BookBuddy/session",
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(MainActivity.this, "Registro exitoso!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, Menu.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            String falloReg;

                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                falloReg = data.getString("error");
                            }catch (JSONException e) {
                                throw new RuntimeException(e);
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }

                            //Si hay un código de error del servidor, se muestra en un toast
                            Toast.makeText(MainActivity.this, falloReg, Toast.LENGTH_LONG).show();

                        } else if (error instanceof com.android.volley.NoConnectionError) {
                            //Si hay un problema estableciendo la conexión, se muestra en un Toast
                            Toast.makeText(MainActivity.this, "Imposible conectar al servidor", Toast.LENGTH_LONG).show();

                        } else {
                            //Para cualquier otro error, se muestra un Toast genérico
                            Toast.makeText(MainActivity.this, "Error en el registro", Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );
        //Añadimos la solicitud a la cola de Volley
        queue.add(petition);
    }

}