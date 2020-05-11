package com.example.proyectof;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Registro extends AppCompatActivity {

    EditText etUsuario, etPasswd, etConfPasswd;
    Button btnReg;
    Spinner spinner;
    public static String elemento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etUsuario = findViewById(R.id.etUsuario);
        etPasswd = findViewById(R.id.etPasswd);
        etConfPasswd = findViewById(R.id.etPasswd);
        btnReg = findViewById(R.id.btnReg);
        spinner = (Spinner) findViewById(R.id.spinnerTipoUsuario);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Registro.this, R.array.tipoUsuarios, R.layout.spinner_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                elemento = (String) spinner.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario, passwd, confPasswd, tipoUsuario;

                usuario = etUsuario.getText().toString();
                passwd = etPasswd.getText().toString();
                confPasswd = etConfPasswd.getText().toString();
                tipoUsuario = elemento;

                if (passwd.equals("") && passwd.equals("") && confPasswd.equals("") && spinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(Registro.this, "Rellenar todos los campos", Toast.LENGTH_SHORT).show();
                }
                else if (usuario.equals("")) {
                    Toast.makeText(Registro.this, "Nombre de usuario requerido", Toast.LENGTH_SHORT).show();
                }
                else if (passwd.equals("")) {
                    Toast.makeText(Registro.this, "Contraseña requerida", Toast.LENGTH_SHORT).show();
                }
                else if (confPasswd.equals("")) {
                    Toast.makeText(Registro.this, "Contraseña requerida", Toast.LENGTH_SHORT).show();
                }
                else if (!confPasswd.equals(passwd)) {
                    Toast.makeText(Registro.this, "La contraseña no coincide", Toast.LENGTH_SHORT).show();
                }
                else if(spinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(Registro.this, "Selecciona un tipo de usuario", Toast.LENGTH_SHORT).show();
                }
                else {
                    new DescarregaImatge(Registro.this).execute(usuario, passwd, tipoUsuario);
                    //Toast.makeText(Registro.this, "Usuario registrado", Toast.LENGTH_SHORT).show(tipoUsuario);
                }
            }
        });
    }

    public static class DescarregaImatge extends AsyncTask<String, Void, String>
    {

        private WeakReference<Context> context;

        public DescarregaImatge(Context context)
        {
            this.context = new WeakReference<>(context);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            String registrar_url = "http://192.168.1.22/registrar.php";
            String resultat;

            try
            {
                URL url = new URL(registrar_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                String usuario = params[0];
                String passwd = params[1];
                String tipoUsuario = params[2];

                String data = URLEncoder.encode("usuario", "UTF-8") + "=" + URLEncoder.encode(usuario, "UTF-8") + "&" + URLEncoder.encode("passwd", "UTF-8") + "=" + URLEncoder.encode(passwd, "UTF-8") + "&" + URLEncoder.encode("tipoUsuario", "UTF-8") + "=" + URLEncoder.encode(tipoUsuario, "UTF-8");

                //System.out.println(data);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line);
                    //resultat += line;
                }
                resultat = stringBuilder.toString();

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //return  resultat;

            } catch (MalformedURLException e) {
                Log.d("APP", "S'ha utilitzat una URL amb format incorrecte");
                resultat = "S'ha produit un error";

            } catch (IOException e) {

                System.out.println(e);

                Log.d("APP", "Error inesperat!!! Possibles problemes de connexió de xarxa");
                resultat = "S'ha produit un error. Comprova la teva connexió";
            }
            return  resultat;
        }

        @Override
        protected void onPostExecute(String resultat) {
            Toast.makeText(context.get(), resultat, Toast.LENGTH_LONG).show();
        }
    }

}
