package com.example.melher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver);

        linearLayout4 = findViewById(R.id.linearLayout4);

        Button btn_regresar = findViewById(R.id.regresar);
        btn_regresar.setOnClickListener(view -> {
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        obtenerClientes(); // Llama al método para obtener los datos de los clientes
    }

    private void obtenerClientes() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/ver_clientes.php"); // Reemplaza con la URL de tu endpoint
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

                    JSONArray jsonArray = new JSONArray(response.toString());
                    runOnUiThread(() -> {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String id = null;
                            try {
                                id = jsonObject.getString("id");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String nombre = null;
                            try {
                                nombre = jsonObject.getString("nombre");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String edad = null; // Agrega más campos si es necesario
                            try {
                                edad = jsonObject.getString("edad");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            TextView textView = new TextView(this);
                            textView.setText("ID: " + id + ", Nombre: " + nombre + ", Edad: " + edad); // Formatea el texto como desees
                            linearLayout4.addView(textView);
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Error al obtener los clientes", Toast.LENGTH_SHORT).show());
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}