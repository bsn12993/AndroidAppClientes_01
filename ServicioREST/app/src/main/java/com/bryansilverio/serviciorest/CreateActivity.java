package com.bryansilverio.serviciorest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bryansilverio.serviciorest.com.models.Cliente;
import com.bryansilverio.serviciorest.com.models.RespuestaConsumo;
import com.bryansilverio.serviciorest.com.services.ClienteServices;

public class CreateActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText nombre;
    private EditText telefono;
    private Button btn_crear;
    private Cliente cliente;
    private ProgressDialog progressDialog;
    public Bundle parametros=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nombre=(EditText)findViewById(R.id.txt_nombre);
        telefono=(EditText)findViewById(R.id.txt_telefono);
        btn_crear=(Button)findViewById(R.id.btn_crear);

        parametros=this.getIntent().getExtras();
        if(parametros!=null){
            nombre.setTag(parametros.get("id").toString());
            nombre.setText(parametros.get("nombre").toString());
            telefono.setText(parametros.get("telefono").toString());
            btn_crear.setText("Editar");
        }

        btn_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=new ProgressDialog(CreateActivity.this);
                progressDialog.setTitle("Creando");
                progressDialog.setMessage("Loading");
                if(parametros.get("accion").equals(1)){
                    cliente=new Cliente();
                    cliente.setNombre(nombre.getText().toString());
                    cliente.setTelefono(telefono.getText().toString());
                    PostCliente(cliente,progressDialog);
                }else if(parametros.get("accion").equals(2)){
                    cliente=new Cliente();
                    cliente.setId(Integer.parseInt(nombre.getTag().toString()));
                    cliente.setNombre(nombre.getText().toString());
                    cliente.setTelefono(telefono.getText().toString());
                    PutCliente(cliente,progressDialog);
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent iHome=new Intent(this,MainActivity.class);
            startActivity(iHome);
        }
        return true;
    }

    public void PostCliente(Cliente cliente, ProgressDialog progressDialog){
        progressDialog.show();
        ClienteServices clienteServices=new ClienteServices();
        boolean respuesta=clienteServices.postCliente(cliente);
        if(!respuesta)Toast.makeText(this,"Ocurrio un error",Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(this,"Se creo cliente",Toast.LENGTH_SHORT).show();
            clearFields();
        }
        progressDialog.dismiss();
    }

    public void PutCliente(Cliente cliente, ProgressDialog progressDialog){
        progressDialog.show();
        ClienteServices clienteServices=new ClienteServices();
        boolean respuesta=clienteServices.putCliente(cliente);
        if(!respuesta)Toast.makeText(this,"Ocurrio un error",Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(this,"Se modifico cliente",Toast.LENGTH_SHORT).show();
            clearFields();
        }
        progressDialog.dismiss();
        Intent iHome=new Intent(this,MainActivity.class);
        startActivity(iHome);
    }

    public void clearFields(){
        nombre.setTag("");
        nombre.setText("");
        telefono.setText("");
    }
}
