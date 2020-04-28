package com.example.proyectof;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    EditText etUsuario, etPasswd, etConfPasswd;
    Button btnReg;
    String usuario, passwd , confPasswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etUsuario = findViewById(R.id.etUsuario);
        etPasswd = findViewById(R.id.etPasswd);
        etConfPasswd = findViewById(R.id.etPasswd);

        btnReg = findViewById(R.id.btnReg);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                usuario = etUsuario.getText().toString();
                passwd = etPasswd.getText().toString();
                confPasswd = etConfPasswd.getText().toString();


                if (passwd.equals("") &&  confPasswd.equals("")) {
                    Toast.makeText(Registro.this, "Rellenar todos los campos", Toast.LENGTH_SHORT).show();
                } else if (usuario.equals("")) {
                    Toast.makeText(Registro.this, "Nombre de usuario requerido", Toast.LENGTH_SHORT).show();
                } else if (passwd.equals("")) {
                    Toast.makeText(Registro.this, "Contraseña requerida", Toast.LENGTH_SHORT).show();
                } else if (confPasswd.equals("")) {
                    Toast.makeText(Registro.this, "Contraseña requerida", Toast.LENGTH_SHORT).show();
                } else if (!confPasswd.equals(passwd)) {
                    Toast.makeText(Registro.this, "La contraseña no coincide", Toast.LENGTH_SHORT).show();
                }
                ejecutarServicio("http://192.168.141.1:8080/bbd_appempresa/insertar_usuario.php");
            }
        });
    }

    private void ejecutarServicio(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override

            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("usuario",usuario);
                parametros.put("contraseña",passwd);

                return super.getParams();
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}