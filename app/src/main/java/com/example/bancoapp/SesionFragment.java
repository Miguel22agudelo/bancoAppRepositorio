package com.example.bancoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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


public class SesionFragment extends Fragment implements Response.Listener<JSONObject>,
        Response.ErrorListener{
    RequestQueue rq;    //Permite crear un objeto para realizar una petición http
    JsonRequest jrq;    //Permite recibir los datos en formato de JSON
    EditText usuario,contraseña;
    Button iniciar;
    TextView registrarseAqui;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_sesion,container,false);
        usuario = vista.findViewById(R.id.etusuario);
        contraseña = vista.findViewById(R.id.etpassword);
        iniciar = vista.findViewById(R.id.btniniciarsesion);
        registrarseAqui = vista.findViewById(R.id.tvregistrarseaqui);
        rq = Volley.newRequestQueue(getContext()); //Requerimiento Volley
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
        return vista;
    }

    private void iniciarSesion() {
        String url = "http://192.168.64.2/servicioswebbanco/buscarusuario.php?usuario="+
                usuario.getText().toString()+"&contraseña="+contraseña.getText().toString();
        jrq = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(),"No se ha encontrado el usuario "+usuario.getText().toString(),
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(JSONObject response) {
        //Se utiliza la clase usuario para tomar los campos del arreglo usuarios del archivo php
        cliente cliente = new cliente();
        //usuarios: arreglo que envía los datos en el arreglo JSON, en el archivo php
        JSONArray jsonArray = response.optJSONArray("usuarios");
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(0); //Posición 0 del arreglo.
            cliente.setUsuario(jsonObject.optString("usuario"));
            //usua.setClave(jsonObject.optString("clave"));
            cliente.setNombre(jsonObject.optString("nombre"));
            cliente.setId(jsonObject.optString("ident"));


        }catch (JSONException e){
            e.printStackTrace();
        }
        Intent intOperaciones = new Intent(getContext(),operaciones.class);
        intOperaciones.putExtra(operaciones.usuario,cliente.getUsuario());
        intOperaciones.putExtra(operaciones.nombre,cliente.getNombre());
        intOperaciones.putExtra(operaciones.ident,cliente.getFecha());

        startActivity(intOperaciones);}
}