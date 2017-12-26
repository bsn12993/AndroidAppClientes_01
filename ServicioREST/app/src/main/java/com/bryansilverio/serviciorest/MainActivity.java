package com.bryansilverio.serviciorest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;;
import android.widget.CheckBox;
import android.widget.Toast;

import com.bryansilverio.serviciorest.com.adapters.RVAdapter;
import com.bryansilverio.serviciorest.com.models.Cliente;
import com.bryansilverio.serviciorest.com.models.RespuestaConsumo;
import com.bryansilverio.serviciorest.com.services.ClienteServices;

import java.util.List;


public class MainActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private List<Cliente> clientes;
    private Cliente cliente=null;
    private ProgressDialog progressDialog;
    private CheckBox checkBox;
    private RVAdapter rvAdaptar;
    private RespuestaConsumo respuestaConsumo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("");
        progressDialog.setMessage("Loading data");
        progressDialog.show();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkBox=(CheckBox)findViewById(R.id.chk);

        recyclerView=(RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        respuestaConsumo=new ClienteServices().getClientes();
        Consumo(respuestaConsumo,progressDialog);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.icon_crear:
                Intent iCrear=new Intent(this,CreateActivity.class);
                startActivity(iCrear);
                break;
            case R.id.icon_borrar:
                int lstId=rvAdaptar.idSelected;
                boolean resultado=new ClienteServices().deleteCliente(lstId);
                if(!resultado)Toast.makeText(this,"Ocurrio un error al eliminar",Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(this,"Se eliminaron los elementos",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.icon_editar:
                int idEditar=rvAdaptar.idSelected;
                if(idEditar!=0){
                    for (Cliente c:respuestaConsumo.getClientes()){
                        if(c.getId()==idEditar){
                            cliente=new Cliente();
                            cliente.setId(idEditar);
                            cliente.setNombre(c.getNombre());
                            cliente.setTelefono(c.getTelefono());
                        }
                    }
                    if(cliente!=null){
                        Bundle parametros=new Bundle();
                        parametros.putInt("id",cliente.getId());
                        parametros.putString("nombre",cliente.getNombre());
                        parametros.putString("telefono",cliente.getTelefono());
                        parametros.putInt("accion",2);
                        Intent iEditar=new Intent(this,CreateActivity.class);
                        iEditar.putExtras(parametros);
                        startActivity(iEditar);
                    }
                }
                break;
            case R.id.icon_buscar:
                Intent iBuscar=new Intent(this,SearchActivity.class);
                startActivity(iBuscar);
                break;
        }
        return true;
    }

    public void Consumo(RespuestaConsumo consumo,ProgressDialog progressDialog){
        if(!consumo.getEstatus().equals("Error")){
            rvAdaptar=new RVAdapter(consumo.getClientes());
            recyclerView.setAdapter(rvAdaptar);
        }else{
            Toast.makeText(getApplicationContext(),consumo.getEstatus()+": "+consumo.getMensaje(),Toast.LENGTH_SHORT);
        }
        progressDialog.dismiss();
    }




/**
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_insertar:
                WSInsertar insertar=new WSInsertar();
                insertar.execute(txt_nombre.getText().toString(),txt_telefono.getText().toString());
                break;
            case R.id.btn_actualizar:
                WSActualizr actualizr=new WSActualizr();
                actualizr.execute(txt_id.getText().toString(),txt_nombre.getText().toString(),txt_telefono.getText().toString());
                break;
            case R.id.btn_obtener:
                WSConsulta consulta=new WSConsulta();
                consulta.execute(txt_id.getText().toString());
                break;
            case R.id.btn_listar:
                WSListar listar=new WSListar();
                listar.execute();
                break;
            case R.id.btn_eliminar:
                WSEliminar eliminar=new WSEliminar();
                eliminar.execute(txt_id.getText().toString());
                break;
        }
    }
**/
/**

    private class WSInsertar extends AsyncTask<String,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean resultado=true;
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost post=new HttpPost("http://192.168.0.105:16738/Api/Cliente");
            post.setHeader("content-type","application/json");
            try {
                //Se construye el obtjeto cliente en formato JSON
                JSONObject dato=new JSONObject();
                //dato.put{"Id",Integer.parseInt(txtid)}
                dato.put("Nombre",strings[0]);
                dato.put("Telefono",strings[1]);
                StringEntity entity=new StringEntity(dato.toString());
                post.setEntity(entity);
                HttpResponse response=httpClient.execute(post);
                String respStr= EntityUtils.toString(response.getEntity());

                if(!respStr.equals("true"))resultado=false;

            }catch (Exception e){
                e.printStackTrace();
                Log.e("ServicioRest","Error",e);
                resultado=false;
            }
            return resultado;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //super.onPostExecute(aBoolean);
            if (aBoolean)lb_resultado.setText("Insertado Ok.");
        }
    }

    private class WSActualizr extends AsyncTask<String,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean resultado=true;
            HttpClient httpClient=new DefaultHttpClient();
            HttpPut put=new HttpPut("http://192.168.0.105:16738/Api/Cliente");
            put.setHeader("content-type","application/json");
            try {
                //Se construye el obtjeto cliente en formato JSON
                JSONObject dato=new JSONObject();
                dato.put("Id",Integer.parseInt(strings[0]));
                dato.put("Nombre",strings[1]);
                dato.put("Telefono",strings[2]);
                StringEntity entity=new StringEntity(dato.toString());
                put.setEntity(entity);
                HttpResponse response=httpClient.execute(put);
                String respStr= EntityUtils.toString(response.getEntity());

                if(!respStr.equals("true"))resultado=false;

            }catch (Exception e){
                e.printStackTrace();
                Log.e("ServicioRest","Error",e);
                resultado=false;
            }
            return resultado;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //super.onPostExecute(aBoolean);
            if (aBoolean)lb_resultado.setText("Actualizado Ok.");
        }
    }


    private class WSConsulta extends AsyncTask<String,Integer,Boolean>{

        private int idCli;
        private String nombreCli;
        private String telefonoCli;

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean resultado=true;
            HttpClient httpClient=new DefaultHttpClient();
            String id=strings[0];
            HttpGet get=new HttpGet("http://192.168.0.105:16738/Api/Clientes/Cliente/"+id);
            get.setHeader("content-type","application/json");
            try {
                HttpResponse response=httpClient.execute(get);
                String respStr= EntityUtils.toString(response.getEntity());
                JSONObject respJSON=new JSONObject(respStr);
                idCli=respJSON.getInt("Id");
                nombreCli=respJSON.getString("Nombre");
                telefonoCli=respJSON.getString("Telefono");

            }catch (Exception e){
                e.printStackTrace();
                Log.e("ServicioRest","Error",e);
                resultado=false;
            }
            return resultado;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //super.onPostExecute(aBoolean);
            if (aBoolean)lb_resultado.setText(""+idCli+"-"+nombreCli+"-"+telefonoCli);
        }
    }


    private class WSListar extends AsyncTask<String,Integer,Boolean>{

        private String[]cliente;

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean resultado=true;
            HttpClient httpClient=new DefaultHttpClient();
            String id=strings[0];
            //HttpGet get=new HttpGet("http://192.168.0.105:16738/Api/Clientes");
            HttpGet get=new HttpGet("http://192.168.0.105:8082/WS_REST/ServicioREST/ClientesServices/listaClientes");
            get.setHeader("content-type","application/json");
            try {
                HttpResponse response=httpClient.execute(get);
                String respStr= EntityUtils.toString(response.getEntity());
                JSONArray respJSON=new JSONArray(respStr);
                cliente=new String[respJSON.length()];

                for (int i=0;i<respJSON.length();i++){
                    JSONObject object=respJSON.getJSONObject(i);
                    int idCli=object.getInt("Id");
                    String nmbCli=object.getString("Nombre");
                    String telCli=object.getString("Telefono");
                    cliente[i]=""+idCli+"-"+nmbCli+"-"+telCli;

                }

            }catch (Exception e){
                e.printStackTrace();
                Log.e("ServicioRest","Error",e);
                resultado=false;
            }
            return resultado;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //super.onPostExecute(aBoolean);
            if (aBoolean){
                ArrayAdapter<String>arrayAdapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,cliente);
                lst_cliente.setAdapter(arrayAdapter);
            }
        }
    }


    private class WSEliminar extends AsyncTask<String,Integer,Boolean>{


        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean resultado=true;
            HttpClient httpClient=new DefaultHttpClient();
            String id=strings[0];
            HttpDelete delete=new HttpDelete("http://192.168.0.105:16738/Api/Clientes/Cliente/"+id);
            delete.setHeader("content-type","application/json");
            try {
                HttpResponse response=httpClient.execute(delete);
                String respStr= EntityUtils.toString(response.getEntity());
                if (!respStr.equals("true"))resultado=false;

            }catch (Exception e){
                e.printStackTrace();
                Log.e("ServicioRest","Error",e);
                resultado=false;
            }
            return resultado;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //super.onPostExecute(aBoolean);
            if (aBoolean)lb_resultado.setText("Eliminar Ok");
        }
    }

**/
}
