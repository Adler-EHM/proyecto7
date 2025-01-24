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

public class ActualizarActivity extends AppCompatActivity {

    private EditText editTextId, editTextNombre, editTextEmail, editTextTelefono, editTextDireccion;
    private Button buttonActualizar, buttonRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        // Asignar las vistas
        editTextId = findViewById(R.id.editText_id_actualizar);
        editTextNombre = findViewById(R.id.editText_nuevo_nombre);
        editTextEmail = findViewById(R.id.editText_nuevo_email);
        editTextTelefono = findViewById(R.id.editText_nuevo_telefono);
        editTextDireccion = findViewById(R.id.editText_nueva_direccion);
        buttonActualizar = findViewById(R.id.button_actualizar);
        buttonRegresar = findViewById(R.id.regresar);

        // Botón regresar
        buttonRegresar.setOnClickListener(v -> finish());

        // Botón actualizar
        buttonActualizar.setOnClickListener(v -> actualizarCliente());
    }

    private void actualizarCliente() {
        // Obtener los valores de los EditText
        String id = editTextId.getText().toString().trim();
        String nombre = editTextNombre.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String telefono = editTextTelefono.getText().toString().trim();
        String direccion = editTextDireccion.getText().toString().trim();

        // Verificar que los campos no estén vacíos
        if (id.isEmpty() || nombre.isEmpty() || email.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Realizar la solicitud HTTP en un hilo aparte para no bloquear la UI
        new Thread(() -> {
            try {
                // URL de tu script PHP
                String urlString = "http://10.0.2.2/actualizar_cliente.php"; // Cambia esto si es necesario
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);

                // Crear el cuerpo de la solicitud
                String postData = "id=" + id + "&nombre=" + nombre + "&email=" + email + "&telefono=" + telefono + "&direccion=" + direccion;

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

                // Procesar la respuesta (JSON)
                String responseStr = response.toString();
                runOnUiThread(() -> {
                    if (responseStr.contains("success\":true")) {
                        Toast.makeText(this, "Cliente actualizado con éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error al actualizar cliente", Toast.LENGTH_SHORT).show();
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
