package com.example.melher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private final String URL_API = "http://10.0.2.2/login.php"; // Usar esta URL para el emulador

    EditText et_username, et_password;
    Button btn_login;
    TextView tv_registrar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_username = findViewById(R.id.editText_username);
        et_password = findViewById(R.id.editText_password);
        tv_registrar = findViewById(R.id.textView_registrate);
        btn_login = findViewById(R.id.button_login);


        tv_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrarActivity.class);
                startActivity(intent);
            }
        });


        // Cuando el usuario hace clic en "Iniciar sesión"
        btn_login.setOnClickListener(view -> {
            String username = et_username.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                realizarLogin(username, password); // Esta función se llama para hacer el login
            } else {
                Toast.makeText(MainActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }


        // Método para realizar el login en el servidor
        private void realizarLogin(String usuario, String contraseña) {
            new Thread(() -> {
                try {
                    // Aquí se usa la URL del emulador de Android
                    URL url = new URL("http://10.0.2.2/login.php");  // Usa esta URL en el emulador de Android

                    // Crear la conexión HTTP
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);  // Necesario para enviar el cuerpo de la solicitud

                    // Crear el objeto JSON para enviar
                    JSONObject json = new JSONObject();
                    json.put("usuario", usuario);  // Datos del usuario
                    json.put("contraseña", contraseña);  // Datos de la contraseña

                    // Enviar el cuerpo de la solicitud
                    OutputStream os = conn.getOutputStream();
                    os.write(json.toString().getBytes("UTF-8"));
                    os.close();

                    // Leer la respuesta del servidor
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Analizar la respuesta JSON
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        if (success) {
                            // Maneja el caso de éxito (usuario logueado correctamente)
                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                // Aquí puedes navegar a otra actividad o hacer algo más
                                Intent intent = new Intent(MainActivity.this, MainActivity2.class); // O cualquier otra actividad
                                startActivity(intent);
                            });
                        } else {
                            // Maneja el caso de error
                            runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        // Si la conexión falla
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show());
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show());
                }
            }).start();
        }
}