package com.example.melher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

    public class AnadirActivity extends AppCompatActivity {


        private EditText etUsername, etPassword, etNombre; // Declara los EditText

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_anadir);

             // Obtén las referencias a los EditText
                    etUsername = findViewById(R.id.editText_username); // Reemplaza con el ID de tu EditText para username
            etPassword = findViewById(R.id.editText_password); // Reemplaza con el ID de tu EditText para password
            etNombre = findViewById(R.id.editText_nombre); // Reemplaza con el ID de tu EditText para nombre

            Button btnRegistrar = findViewById(R.id.button_registrar); // Reemplaza con el ID de tu botón de registro
            btnRegistrar.setOnClickListener(view -> {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String nombre = etNombre.getText().toString().trim();

                registrarUsuario(username, password, nombre);
            });

            Button btn_regresar = findViewById(R.id.regresar);
            btn_regresar.setOnClickListener(view -> {
                finish();
            });

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        private void registrarUsuario(java.lang.String username, java.lang.String password, java.lang.String nombre)
        {
            new Thread(() -> {
                try {
                    // 1. URL del endpoint de registro
                    URL url = new URL("http://10.0.2.2/registrar.php"); // Reemplaza con la URL de tu endpoint

                    // 2. Crear la conexión
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);

                    // 3. Datos a enviar
                    JSONObject json = new JSONObject();
                    json.put("username", username);
                    json.put("password", password);
                    json.put("nombre", nombre); // Agrega otros datos del usuario si es necesario

                    // 4. Enviar los datos
                    OutputStream os = conn.getOutputStream();
                    os.write(json.toString().getBytes("UTF-8"));
                    os.close();

                    // 5. Leer la respuesta
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            // Puedes navegar a otra actividad o realizar alguna otra acción después del registro
                            // Por ejemplo, finish() para cerrar la actividad actual
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show());
                    }

                    // 6. Cerrar la conexión
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show());
                }
            }).start();
        }



    }



