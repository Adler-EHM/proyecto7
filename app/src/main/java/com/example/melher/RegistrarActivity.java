package com.example.melher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.melher.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrarActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;
    private Button buttonRegistrar, buttonRegresar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        // Inicializar vistas
        editTextUsername = findViewById(R.id.editText_username);
        editTextPassword = findViewById(R.id.editText_password);
        buttonRegistrar = findViewById(R.id.button_register);
        buttonRegresar = findViewById(R.id.button_regresar);

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos de los campos de texto
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // Validar que los campos no estén vacíos
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegistrarActivity.this, "Por favor ingrese todos los datos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Enviar los datos al servidor para registrar el usuario
                registerUser(username, password);
            }
        });

        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Regresar a la actividad anterior
            }
        });
    }

    private void registerUser(final String username, final String password) {
        // URL del archivo PHP que procesará el registro
        String url = "http://10.0.2.2/registrar_usuario.php"; // Cambia 'localhost' por la IP de tu PC

        // Crear una solicitud de tipo POST a la URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Analizar la respuesta JSON
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            // Mostrar un mensaje dependiendo del resultado
                            if (success) {
                                Toast.makeText(RegistrarActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegistrarActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegistrarActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar los errores de la solicitud
                Toast.makeText(RegistrarActivity.this, "Error en la conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Enviar los datos al servidor en el cuerpo de la solicitud POST
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        // Agregar la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

