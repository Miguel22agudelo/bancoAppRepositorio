package com.example.bancoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class logeado extends AppCompatActivity  {

    public static final String usuarioOrigen = "usuario";
    public static final String cuentaOrigen = "numeroCuenta";
    public static final String saldoOrigen = "saldo";

    EditText saldo, destinol, valor, hora, fecha;
    Button transferir,cerrarSesion;
    String numeroCuentaorigen,date,time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logeado);
        destinol = findViewById(R.id.etDestino);
        saldo = findViewById(R.id.etSaldo);
        hora = findViewById(R.id.ethora);
        fecha = findViewById(R.id.etfecha);
        valor = findViewById(R.id.etvalor);
        transferir = findViewById(R.id.btntransferir);
        cerrarSesion = findViewById(R.id.btnCerrar);
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });



        TimeZone tz = TimeZone.getTimeZone("GMT-05:00");
        numeroCuentaorigen = getIntent().getStringExtra("numeroCuenta");
        saldo.setEnabled(false);
        saldo.setText(getIntent().getStringExtra("saldo"));
        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        fecha.setEnabled(false);
        fecha.setText(date);
        hora.setEnabled(false);
        hora.setText(time);
        int saldoEntero = Integer.parseInt(saldo.getText().toString());

        transferir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mcuentadest = destinol.getText().toString();
                String mvalor = valor.getText().toString();
                int valorEntero = Integer.parseInt(mvalor);
                if (!mcuentadest.isEmpty() && !mvalor.isEmpty()) {
                    if (saldoEntero-valorEntero>10000){
                        Calendar c = Calendar.getInstance(tz);
                        time = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
                        hora.setText(time);
                        realizarTransferencia(mcuentadest,mvalor);
                    }else{
                        Toast.makeText(getApplicationContext(),"Saldo Insuficiente",
                                Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Debe ingresar Destino y Valor",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void realizarTransferencia(String mcuentadest, String mvalor) {
        String url = "http://192.168.64.2/servicioswebbanco/agregartransferencia.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("1")){
                    Toast.makeText(logeado.this, "Transferencia realizada", Toast.LENGTH_SHORT).show();
                    destinol.setText("");
                    valor.setText("");
                    hora.setText("");
                    fecha.setText(date);
                    saldo.setText("");
                    destinol.requestFocus();
                }
                else if(response.equals("2")){
                    Toast.makeText(logeado.this, "Saldo insuficiente", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(logeado.this, "La cuenta de destino no existe", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(logeado.this, "Error. Intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("numeroCuentaDestino",destinol.getText().toString().trim());
                params.put("valor", valor.getText().toString().trim());
                params.put("hora",hora.getText().toString().trim());
                params.put("fecha",fecha.getText().toString().trim());
                params.put("numeroCuentaOrigen",numeroCuentaorigen);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
    }





}