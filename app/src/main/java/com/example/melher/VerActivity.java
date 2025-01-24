package com.example.melher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VerActivity extends AppCompatActivity {

    private LinearLayout linearLayout4;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        linearLayout4 = findViewById(R.id.linearLayout4);

        Button btn_regresar = findViewById(R.id.regresar);
        btn_regresar.setOnClickListener(view -> finish());  // Regresa a la pantalla anterior

        obtenerClientes(); // Llama al método para obtener los datos de los clientes
    }
    @Override
    protected void onResume() {
        super.onResume();
        obtenerClientes();  // Recargar la lista de clientes
    }


    // Método para obtener los clientes desde el servidor
    private void obtenerClientes() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/ver_clientes.php"); // Cambia esta URL por la correcta de tu servidor
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET"); // O el método que use tu API

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Mostrar los clientes en el UI
                    JSONArray jsonArray = new JSONArray(response.toString());
                    runOnUiThread(() -> {
                        // Limpiar el LinearLayout antes de agregar nuevas vistas
                        linearLayout4.removeAllViews();

                        // Iterar sobre los elementos del JSON y agregar un TextView para cada cliente
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String nombre = jsonObject.getString("nombre");
                                String email = jsonObject.getString("email");
                                String telefono = jsonObject.optString("telefono", "No disponible");

                                TextView textView = new TextView(VerActivity.this);
                                textView.setText("ID: " + id + ", Nombre: " + nombre + ", Email: " + email + ", Teléfono: " + telefono);
                                textView.setPadding(10, 10, 10, 10);  // Espaciado entre elementos
                                linearLayout4.addView(textView);  // Agregar el TextView al LinearLayout
                            } catch (JSONException e) {
                                e.printStackTrace();
                                runOnUiThread(() -> Toast.makeText(VerActivity.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show());
                            }
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(VerActivity.this, "Error al obtener los clientes", Toast.LENGTH_SHORT).show());
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(VerActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

}