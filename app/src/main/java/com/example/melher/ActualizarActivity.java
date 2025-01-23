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

public class ActualizarActivity extends AppCompatActivity {

    private EditText etIdActualizar, etNuevoNombre, etNuevaEdad; // Agrega más EditText si es necesario

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actualizar);

        etIdActualizar = findViewById(R.id.editText_id_actualizar); // Reemplaza con el ID de tu EditText
        etNuevoNombre = findViewById(R.id.editText_nuevo_nombre); // Reemplaza con el ID de tu EditText
        etNuevaEdad = findViewById(R.id.editText_nueva_edad); // Reemplaza con el ID de tu EditText
        // Obtén las referencias a otros EditText si es necesario

        Button btnActualizar = findViewById(R.id.button_actualizar); // Reemplaza con el ID de tu Button
        btnActualizar.setOnClickListener(view -> {
            String idActualizar = etIdActualizar.getText().toString().trim();
            String nuevoNombre = etNuevoNombre.getText().toString().trim();
            String nuevaEdad = etNuevaEdad.getText().toString().trim();
            // Obtén los valores de otros EditText si es necesario

            actualizarUsuario(idActualizar, nuevoNombre, nuevaEdad); // Agrega más parámetros si es necesario
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

    private void actualizarUsuario(String idActualizar, String nuevoNombre, String nuevaEdad) { // Agrega más parámetros si es necesario
        new Thread(() -> {
            try {
                // 1. URL del endpoint de actualización
                URL url = new URL("http://10.0.2.2/actualizar.php"); // Reemplaza con la URL de tu endpoint

                // 2. Crear la conexión
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST"); // O el método que use tu API
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                // 3. Datos a enviar
                JSONObject json = new JSONObject();
                json.put("id", idActualizar); // Envía el ID del cliente a actualizar
                json.put("nombre", nuevoNombre); // Envía el nuevo nombre
                json.put("edad", nuevaEdad); // Envía la nueva edad
                // Agrega más campos al JSON si es necesario

                // 4. Enviar los datos
                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes("UTF-8"));
                os.close();

                // 5. Leer la respuesta
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Cliente actualizado exitosamente", Toast.LENGTH_SHORT).show();
                        // Puedes realizar alguna otra acción después de la actualización
                        // Por ejemplo, finish() para cerrar la actividad actual
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Error al actualizar el cliente", Toast.LENGTH_SHORT).show());
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