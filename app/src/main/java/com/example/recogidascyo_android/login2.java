package com.example.recogidascyo_android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
 import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Utilidades.contenedor_usuario;


public class login2 extends AppCompatActivity {
    Connection connect;
    ConexionSQLiteHelper conn;
    Button btn_login;
    TextView txt_usuario,txt_pass;
    ConnectionHelperGrupomaehara conexion = new ConnectionHelperGrupomaehara();
    private ProgressDialog progress_sincro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
         btn_login=(Button)findViewById(R.id.btn_login);
        txt_usuario=(TextView)findViewById(R.id.txt_usuario);
        txt_pass=(TextView)findViewById(R.id.password);
        
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);




        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            login();
                }
                    });
                }



     class Hilo_sincro extends Thread {
        @Override
        public void run() {

            try {
                sincronizar_usuarios();
                sincronizar_aviarios();
                sincronizar_motivos();

                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(login2.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                        progress_sincro.dismiss();

                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }


    class Hilo_login extends Thread {
        @Override
        public void run() {

            try {
                login();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(login2.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                        progress_sincro.dismiss();

                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void sincronizar(View v){

                new AlertDialog.Builder(login2.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("SINCRONIZACION DE USUARIOS.")
                        .setMessage("DESEA ACTUALIZAR USUARIOS DISPONIBLES?.")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progress_sincro = ProgressDialog.show(login2.this, "SINCRONIZANDO",
                                        "ESPERE...", true);
                                new Hilo_sincro().start();

                            }

                        })
                        .setNegativeButton("NO", null)
                        .show();


      }

    private void borrar_usuario(){
        SQLiteDatabase db1=conn.getReadableDatabase();
        String strSQL = "delete from usuarios";
        db1.execSQL(strSQL);
        db1.close();
    }
    private void sincronizar_usuarios() {
                  try {
                      borrar_usuario();
              SQLiteDatabase db=conn.getReadableDatabase();
            ConnectionHelperGrupomaehara conexion = new ConnectionHelperGrupomaehara();
            connect = conexion.Connections();

            Statement stmt = connect.createStatement();
           String query = "select * from usuarios where clasificadora in ('A','B','O','C','H') and rol <> 'i'";
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next())
            {

                ContentValues values=new ContentValues();
                values.put("nombre",rs.getString("nombre"));
                values.put("usuario",rs.getString("usuario"));
                values.put("password",rs.getString("clave"));

                values.put("cod_usuario",rs.getString("cod_usuario"));
                values.put("rol",rs.getString("rol"));
                values.put("clasificadora",rs.getString("clasificadora"));

               db.insert("usuarios", "cod_usuario",values);
            }
            db.close();
            rs.close();
        }catch(Exception e){

        }}











    private void sincronizar_motivos() {
        try {
             SQLiteDatabase db=conn.getReadableDatabase();
             connect = conexion.Connections();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery("select * from motivo_parada_recogida");
            while ( rs.next())
            {
                ContentValues values=new ContentValues();
                values.put("descripcion",rs.getString("descripcion"));
                values.put("id",rs.getString("id"));
                db.insert("motivo_parada", "id",values);
            }
            db.close();
        }catch(Exception e){
        }}



    private void sincronizar_aviarios() {
        try {

                SQLiteDatabase db=conn.getReadableDatabase();
                 connect = conexion.Connections();
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery("select * from aviarios");

                while ( rs.next())
                {
                    ContentValues values=new ContentValues();
                    values.put("aviario",rs.getString("descripcion"));
                    values.put("bloque",rs.getString("bloque"));
                    db.insert("aviarios", "aviario",values);
                }
                db.close();
            }catch(Exception e){

        }}


    private void login (){

            SQLiteDatabase db=conn.getReadableDatabase();
            String respuesta="0";
        MessageDigest m;
        try
        {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        m.reset();
        m.update(txt_pass.getText().toString().getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String clavehASH = bigInt.toString(16);


           Cursor cursor=db.rawQuery("SELECT usuario,clasificadora,nombre FROM  usuarios where usuario='"+txt_usuario.getText().toString().trim()+"' and password='"+clavehASH+"'" ,null);


        while (cursor.moveToNext())
                {
                respuesta=(cursor.getString(0));
                contenedor_usuario.area=(cursor.getString(1));
                contenedor_usuario.nombre_usuario=(cursor.getString(2));
                contenedor_usuario.usuario=(cursor.getString(0));
               }
     //   Toast.makeText(login2.this, respuesta, Toast.LENGTH_SHORT).show();

               if (respuesta.equals("0")){
                   new AlertDialog.Builder(login2.this)
                           .setTitle("ATENCION!!!")
                           .setMessage("USUARIO INCORRECTO")
                           .setNegativeButton("CERRAR", null).show();


               }
               else{

                   Intent i=new Intent(this,MainActivity.class);
                   startActivity(i);
                   finish();
               }


    }
    @Override
    public void onBackPressed() {



                        finish();


        moveTaskToBack(true);


    }
}
