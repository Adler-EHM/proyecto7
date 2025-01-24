package com.example.melher;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AnadirActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextEmail, editTextTelefono, editTextDireccion;
    private Button buttonAnadir, buttonRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir);

        // Asignar vistas
        editTextNombre = findViewById(R.id.editText_nombre);
        editTextEmail = findViewById(R.id.editText_email);
        editTextTelefono = findViewById(R.id.editText_telefono);
        editTextDireccion = findViewById(R.id.editText_direccion);
        buttonAnadir = findViewById(R.id.button_anadir);
        buttonRegresar = findViewById(R.id.regresar);

        // Botón Regresar
        buttonRegresar.setOnClickListener(v -> finish());

        // Botón Añadir
        buttonAnadir.setOnClickListener(v -> anadirCliente());
    }

    private void anadirCliente() {
        // Obtener datos de los EditText
        String nombre = editTextNombre.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String telefono = editTextTelefono.getText().toString().trim();
        String direccion = editTextDireccion.getText().toString().trim();

        // Verificar que los campos no estén vacíos
        if (nombre.isEmpty() || email.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Realizar la solicitud HTTP
        new Thread(() -> {
            try {
                String urlString = "http://10.0.2.2/anadir_cliente.php";  // Cambia la URL según tu servidor
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);

                // Crear el cuerpo de la solicitud
                String postData = "nombre=" + nombre + "&email=" + email + "&telefono=" + telefono + "&direccion=" + direccion;

                // Escribir los datos en la conexión
                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.close();

                // Leer la respuesta
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Procesar la respuesta
                String responseStr = response.toString();
                runOnUiThread(() -> {
                    if (responseStr.contains("success\":true")) {
                        Toast.makeText(this, "Cliente añadido con éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error al añadir cliente", Toast.LENGTH_SHORT).show();
                    }
                });

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
