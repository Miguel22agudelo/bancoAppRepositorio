package com.example.bancoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

public class reporteTransferencias extends AppCompatActivity implements Response.Listener<JSONObject>,
        Response.ErrorListener {
    RecyclerView recyclerTransferencias;
    ArrayList<transferencia> listadoTransferenias;
    RequestQueue rq;
    JsonRequest jrq;
    String user;
    Button cerrar, regresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_transferencias);
        cerrar = findViewById(R.id.btncerrarsesion_rep);
        regresar = findViewById(R.id.btnregresar_rep);
        user = getIntent().getStringExtra("usuario");
        recyclerTransferencias = findViewById(R.id.rvreportetrf);
        listadoTransferenias = new ArrayList<>();
        rq = Volley.newRequestQueue(getApplicationContext());
        recyclerTransferencias.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerTransferencias.setHasFixedSize(true);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cargarWebService();

    }

    private void cargarWebService() {
        String url="http://192.168.64.2/servicioswebbanco/listarTrf.php?usuario1="+user;
        jrq = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(this,user,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onResponse(JSONObject response) {
        //Toast.makeText(this, "Hecho", Toast.LENGTH_SHORT).show();
        transferencia transferencia = null;
        //Definición de array de JSON para recibir los datos del archivo php
        JSONArray json = response.optJSONArray("transacciones");
        //Recorrido del arreglo json
        try {
            for (int i=0;i<json.length();i++){
                transferencia = new transferencia();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                transferencia.setCuentaDestino(jsonObject.optString("numeroCuentaDestino"));
                transferencia.setHora(jsonObject.optString("hora"));
                transferencia.setFecha(jsonObject.optString("fecha"));
                transferencia.setValor(jsonObject.optString("valor"));
                listadoTransferenias.add(transferencia);
            }
            //progress.hide();
            transferAdapter adaptertrf = new transferAdapter(listadoTransferenias);
            recyclerTransferencias.setAdapter(adaptertrf);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
        }
    }
}