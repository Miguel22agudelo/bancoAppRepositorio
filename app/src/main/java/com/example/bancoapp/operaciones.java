package com.example.bancoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class operaciones extends AppCompatActivity implements Response.Listener<JSONObject>,
        Response.ErrorListener{
    public static final String nombre = "nombre";
    public static final String usuario = "usuario";
    public static final String ident = "ident";
    Button listar,transferir;
    RequestQueue rq;//permite crear un objeto para realizar una petición
    JsonRequest jrq;
    String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operaciones);
        transferir = findViewById(R.id.btntransferir);
        rq = Volley.newRequestQueue(getApplicationContext());
        user = getIntent().getStringExtra("usuario");
        listar = findViewById(R.id.btnlistar);
        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intreport = new Intent(getApplicationContext(), reporteTransferencias.class);
                intreport.putExtra("usuario", user);
                startActivity(intreport);
            }
        });

        transferir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarTransferencia();
            }
        });

    }

    private void realizarTransferencia() {
        String url = "http://192.168.64.2/servicioswebbanco/buscarcuenta.php?usuario1="+
                user;
        jrq = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"No se encuentra esta cuenta",
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onResponse(JSONObject response) {
        cuenta cuenta = new cuenta();
        JSONArray jsonArray = response.optJSONArray("cuenta");
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(0);//posición 0 del arreglo
            cuenta.setUsuario(jsonObject.optString("usuario"));
            cuenta.setNumeroCuenta(jsonObject.optString("numeroCuenta"));
            cuenta.setSaldo(jsonObject.optString("saldo"));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        Intent intlogeado = new Intent(getApplicationContext(), logeado.class);
        intlogeado.putExtra(logeado.usuarioOrigen, cuenta.getUsuario());
        intlogeado.putExtra(logeado.cuentaOrigen, cuenta.getNumeroCuenta());
        intlogeado.putExtra(logeado.saldoOrigen, cuenta.getSaldo());
        startActivity(intlogeado);
    }
}