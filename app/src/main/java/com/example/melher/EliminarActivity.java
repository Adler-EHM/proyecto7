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

public class EliminarActivity extends AppCompatActivity {

    private EditText etIdEliminar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eliminar);

        etIdEliminar = findViewById(R.id.editText_id_eliminar); // Reemplaza con el ID de tu EditText
        Button btnEliminar = findViewById(R.id.button_eliminar); // Reemplaza con el ID de tu Button

        btnEliminar.setOnClickListener(view -> {
            String idEliminar = etIdEliminar.getText().toString().trim();
            eliminarUsuario(idEliminar);
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

    private void eliminarUsuario(String idEliminar) {
        new Thread(() -> {
            try {
                // 1. URL del endpoint de eliminación
                URL url = new URL("http://10.0.2.2/eliminar.php"); // Reemplaza con la URL de tu endpoint

                // 2. Crear la conexión
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST"); // O el método que use tu API
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                // 3. Datos a enviar
                JSONObject json = new JSONObject();
                json.put("id", idEliminar); // Envía el ID del cliente a eliminar

                // 4. Enviar los datos
                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes("UTF-8"));
                os.close();

                // 5. Leer la respuesta
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Cliente eliminado exitosamente", Toast.LENGTH_SHORT).show();
                        // Puedes realizar alguna otra acción después de la eliminación
                        // Por ejemplo, finish() para cerrar la actividad actual
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Error al eliminar el cliente", Toast.LENGTH_SHORT).show());
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