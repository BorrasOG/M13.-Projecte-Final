package com.example.proyectof;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {

    EditText etUsuario, etPasswd;
    Button btnLogin;
    TextView tvReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = findViewById(R.id.etUsuario);
        etPasswd = findViewById(R.id.etPasswd);

        btnLogin = findViewById(R.id.btnLogin);
        tvReg = findViewById(R.id.tvReg);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario, passwd;

                usuario = etUsuario.getText().toString();
                passwd = etPasswd.getText().toString();

                if (passwd.equals("") && usuario.equals("")) {
                    Toast.makeText(MainActivity.this, "Hay campos sin rellenar", Toast.LENGTH_SHORT).show();
                }
                else if (usuario.equals("")) {
                    Toast.makeText(MainActivity.this, "Usuario requerido", Toast.LENGTH_SHORT).show();
                }
                else if (passwd.equals("") ) {
                    Toast.makeText(MainActivity.this, "Contraseña requerida", Toast.LENGTH_SHORT).show();
                }
                else {
                    //new Registro.DescarregaImatge(MainActivity.this).execute(usuario, passwd);
                    Intent i = new Intent(MainActivity.this, Inicio.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Registro.class);
                startActivity(i);
                finish();
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
            String login_url = "http://192.168.1.22/login.php";
            String resultat;

            try
            {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                String usuario = params[0];
                String passwd = params[1];

                String data = URLEncoder.encode("usuario", "UTF-8") + "=" + URLEncoder.encode(usuario, "UTF-8") + "&" + URLEncoder.encode("passwd", "UTF-8") + "=" + URLEncoder.encode(passwd, "UTF-8");

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
