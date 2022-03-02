package com.example.aplicacion_concesionario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrarseActivity extends AppCompatActivity {

    EditText jetidentificacion,jetnombre,jetusuario,jetclave,jetclave1;
    Button jbtguardar,jbtconsultar,jbtanular,jbtcancelar,jbtregresar;
    long resp;
    int sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        getSupportActionBar().hide();
        jetidentificacion=findViewById(R.id.etidentificacion);
        jetnombre=findViewById(R.id.etnombre);
        jetusuario=findViewById(R.id.etusuario);
        jetclave=findViewById(R.id.etclave);
        jetclave1=findViewById(R.id.etclave1);
        jbtguardar=findViewById(R.id.btguardar);
        jbtconsultar=findViewById(R.id.btconsultar);
        jbtanular=findViewById(R.id.btanular);
        jbtcancelar=findViewById(R.id.btcancelar);
        jbtregresar=findViewById(R.id.btregresar);
        sw=0;
    }

    public void Guardar(View view){
        String identificacion,nombre,usuario,clave,clave1;
        identificacion=jetidentificacion.getText().toString();
        nombre=jetnombre.getText().toString();
        usuario=jetusuario.getText().toString();
        clave=jetclave.getText().toString();
        clave1=jetclave1.getText().toString();
        if (identificacion.isEmpty() || nombre.isEmpty() || usuario.isEmpty()
        || clave.isEmpty() || clave1.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
        else{
            if(!clave.equals(clave1)){
                Toast.makeText(this, "Las claves son diferentes", Toast.LENGTH_SHORT).show();
                jetclave.requestFocus();
            }
            else{
                Conexion_Sqlite admin=new Conexion_Sqlite(this, "concesionario.db",null,1);
                SQLiteDatabase db=admin.getWritableDatabase();
                ContentValues registro=new ContentValues();
                registro.put("idcliente",identificacion);
                registro.put("nomcliente",nombre);
                registro.put("usuario",usuario);
                registro.put("clave",clave);
                if(sw == 0)
                    resp=db.insert("TblCliente",null,registro);
                else{
                    resp=db.update("tblCliente",registro,"idcliente='"+identificacion+"'",null);
                    sw=0;
                }
                if (resp>0){
                    limpiar_campos();
                    Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        }
    }
    public void anular(View view){
        consultar_Registro();
        if (sw==1){
            String identificacion=jetidentificacion.getText().toString();
            Conexion_Sqlite admnin=new Conexion_Sqlite(this,"concesionario.db",null,1);
            SQLiteDatabase db=admnin.getWritableDatabase();
            ContentValues dato=new ContentValues();
            dato.put("idcliente",identificacion);
            dato.put("activo","no");
            resp=db.update("TblCliente",dato,"idcliente='"+identificacion+"'",null);
            if(resp>0){
                Toast.makeText(this, "Registro anulado", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Error anulado registro", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }

    public void Consultar(View view){
        consultar_Registro();

    }


    public void consultar_Registro(){
        String identificacion;
        identificacion=jetidentificacion.getText().toString();
        if(identificacion.isEmpty()){
            Toast.makeText(this, "La identificacion es requerida para buscar", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
        else{
            Conexion_Sqlite admin=new Conexion_Sqlite(this, "concesionario.db",null,1);
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor fila=db.rawQuery("select * from TblCliente where idCliente='"+identificacion+ "'",null);
            if(fila.moveToNext()){
                if(fila.getString(4).equals("no")){
                    Toast.makeText(this, "Registro existe pero esta anulado", Toast.LENGTH_SHORT).show();
                }
                else{
                    sw=1;
                    jetnombre.setText(fila.getString(1));
                    jetusuario.setText(fila.getString(2));
                    jetclave.setText(fila.getString(3));
                }
            }
            else{
                Toast.makeText(this, "Registro no hallado", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }

    }




    public void limpiar_campos(){
        sw=0;
        jetidentificacion.setText("");
        jetnombre.setText("");
        jetusuario.setText("");
        jetclave.setText("");
        jetclave1.setText("");
        jetidentificacion.setText("");
    }



    public void Cancelar(View view){
        jetidentificacion.setText("");
        jetnombre.setText("");
        jetusuario.setText("");
        jetclave.setText("");
        jetclave1.setText("");
        jetidentificacion.requestFocus();
    }

    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }
}