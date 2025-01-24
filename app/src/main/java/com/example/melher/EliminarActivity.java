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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EliminarActivity extends AppCompatActivity {

    private EditText editTextIdEliminar;
    private Button buttonEliminar;
    private Button buttonRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);

        // Inicializa los elementos de la interfaz
        editTextIdEliminar = findViewById(R.id.editText_id_eliminar);
        buttonEliminar = findViewById(R.id.button_eliminar);
        buttonRegresar = findViewById(R.id.regresar);

        // Configurar el botón "Eliminar"
        buttonEliminar.setOnClickListener(v -> eliminarCliente());

        // Configurar el botón "Regresar"
        buttonRegresar.setOnClickListener(v -> {
            // Aquí puedes manejar lo que sucede cuando se presiona "Regresar"
            // Por ejemplo, volver a la pantalla principal
            finish();  // Termina la actividad actual
        });
    }

    private void eliminarCliente() {
        // Obtener el ID del cliente
        String idCliente = editTextIdEliminar.getText().toString().trim();

        // Verificar que el campo no esté vacío
        if (idCliente.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un ID de cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        // Realizar la solicitud HTTP en un hilo aparte para no bloquear la UI
        new Thread(() -> {
            try {
                // URL del script PHP (cambia la dirección si es necesario)
                String urlString = "http://10.0.2.2/eliminar_cliente.php";  // Cambia a la URL correcta si usas otro entorno
                URL url = new URL(urlString);

                // Configurar la conexión
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);

                // Cuerpo de la solicitud (parámetro id)
                String postData = "id=" + idCliente;

                // Escribir los datos en el flujo de salida
                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.close();

                // Leer la respuesta del servidor
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
                    try {
                        JSONObject jsonResponse = new JSONObject(responseStr);
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        // Mostrar el mensaje de la respuesta
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                        if (success) {
                            // Si la eliminación fue exitosa, puedes realizar otras acciones si es necesario
                            // Por ejemplo, limpiar el campo de texto o cerrar la actividad
                            editTextIdEliminar.setText("");  // Limpiar el campo de texto
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                    }
                });

                // Cerrar la conexión
                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}