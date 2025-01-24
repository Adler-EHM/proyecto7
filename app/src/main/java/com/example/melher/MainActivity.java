package com.example.melher;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private final String URL_API = "http://tuservidor.com/login.php"; // Cambia por la URL de tu servidor


    TextView tv_registrate;
    EditText et_username, et_password;
    Button btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tv_registrate = findViewById(R.id.textView_registrate);
        et_username = findViewById(R.id.editText_username);
        et_password = findViewById(R.id.editText_password);
        btn_login = findViewById(R.id.button_login);




        tv_registrate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrarActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(view -> {
            String username = et_username.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                realizarLogin(username, password);
            } else {
                Toast.makeText(MainActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        });





    }

        private void realizarLogin(String username, String password) {
            new Thread(() -> {
                try {
                    // URL del archivo PHP
                    URL url = new URL("http://10.0.2.2/login.php"); // Cambia por la URL de tu servidor

                    // Crear la conexión
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);

                    // Datos a enviar
                    JSONObject json = new JSONObject();
                    json.put("username", username);
                    json.put("password", password);

                    // Enviar los datos
                    OutputStream os = conn.getOutputStream();
                    os.write(json.toString().getBytes("UTF-8"));
                    os.close();

                    // Leer la respuesta
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                        startActivity(intent);
                        runOnUiThread(() -> Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show());
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show());
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show());
                }
            }).start();
        }
}




