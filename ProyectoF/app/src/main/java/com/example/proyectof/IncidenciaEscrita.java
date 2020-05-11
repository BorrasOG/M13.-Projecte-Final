package com.example.proyectof;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IncidenciaEscrita extends AppCompatActivity {

    EditText etIncidencia;
    Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencia_escrita);

        btnEnviar = findViewById(R.id.btnEnviar);
        etIncidencia = findViewById(R.id.etIncidencia);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String incidencia;

                incidencia = etIncidencia.getText().toString();

                if (incidencia.equals("")) {
                    Toast.makeText(IncidenciaEscrita.this, "Escribe la incidencia", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(IncidenciaEscrita.this, "La incidencia se ha enviado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
