package com.example.recogidascyo_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
 import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import Utilidades.contenedor_usuario;

public class MainActivity2 extends AppCompatActivity
{
    private volatile boolean flag = true;
    Button btn_ir_reco,btn_list,btnDoble;
    Connection connect;
    ConexionSQLiteHelper conn;
TextView txt_total,txt_estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_ir_reco=(Button)findViewById(R.id.btn_ir_registro);
        btn_list=(Button)findViewById(R.id.btn_list);
        btnDoble=(Button)findViewById(R.id.btnDoble);
        txt_total=(TextView)findViewById(R.id.txt_total);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        pendientes();
       /* hilo_regis thread = new hilo_regis();

        thread.start();

*/


        final MyThread thread = new MyThread();

        thread.start();



        /* final Runnable tarea = new Runnable() {

            public void run() {

                conexion c = new conexion();

                if(c.getConexion()!=null){
                   // Toast.makeText( MainActivity.this,"CONEXION EXITOSA",Toast.LENGTH_LONG).show();
                    exportar();
                    pendientes();
                    txt_estado.setText("EN LINEA");
                }
                else {
                    //Toast.makeText( MainActivity.this,"ERROR DE CONEXION",Toast.LENGTH_LONG).show();
                    pendientes();
                    txt_estado.setText("SIN CONEXION");
                }

            }
        };
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(tarea, 1, 3, TimeUnit.SECONDS);
*/

       getSupportActionBar().setTitle("USUARIO: "+contenedor_usuario.nombre_usuario);
        getSupportActionBar().setSubtitle("AREA: "+contenedor_usuario.area);
if(contenedor_usuario.area.equals("1")){
    Intent intent = new Intent(getApplicationContext(), login2.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
}
else{

}
        btn_ir_reco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.stopRunning();
                 Intent i=new Intent(MainActivity2.this,Recogidas.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                finish();
                startActivity(i);
              }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.stopRunning();
                Intent i=new Intent(MainActivity2.this,lista_recogidas.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                finish();
                startActivity(i);

             }
        });

        btnDoble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.stopRunning();
                Intent i=new Intent(MainActivity2.this,Recogidas_doble.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                finish();
                startActivity(i);
            }
        });



    }
    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("ATENCION!!!.")
                .setMessage("DESEA CERRAR SESION.")
                .setPositiveButton("SI", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        android.os.Process.killProcess(android.os.Process.myPid());

                        Intent intent = new Intent(getApplicationContext(), login2.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);
                    }

                })
                .setNegativeButton("NO", null)
                .show();
    }


/*
private  void test_conexion(){
    conexion c = new conexion();

    if(c.getConexion()!=null){
        try {
            conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
            SQLiteDatabase db=conn.getReadableDatabase();
            ConnectionHelperGrupomaehara conexion = new ConnectionHelperGrupomaehara();
            connect = conexion.Connections();
             Cursor cursors=db.rawQuery("SELECT  *  FROM recogidas where   estado_sincro ='P'  " ,null);

            if (cursors.moveToFirst())
            {
                exportar();
                pendientes();
                txt_estado.setText("EN LINEA");
                txt_estado.setTextColor(Color.GREEN);
               // Toast.makeText( getApplicationContext(),"un dato enviado",Toast.LENGTH_LONG).show();
            }

            else {
                exportar();
                pendientes();
                txt_estado.setText("EN LINEAs");
                txt_estado.setTextColor(Color.GREEN);
            //  Toast.makeText( getApplicationContext(),"SIN DATOS PARA ENVIAR",Toast.LENGTH_LONG).show();
            }

         }catch(Exception e)
        {

        }

     }
    else {
       pendientes();
        txt_estado.setText("SIN CONEXION");
        txt_estado.setTextColor(Color.RED);
      // Toast.makeText( getApplicationContext(),"ERROR DE CONEXION",Toast.LENGTH_LONG).show();


    }
}*/
    private void exportar()
    {
        SQLiteDatabase db=conn.getReadableDatabase();
        ConnectionHelperGrupomaehara conexion = new ConnectionHelperGrupomaehara();
        connect = conexion.Connections();
        Cursor cursor,cursor_eliminar;
        int c=0;
        try {
              cursor=db.rawQuery("SELECT   " +
                    "idregistro ," +
                    "fecha," +
                    "area," +
                    "doble," +
                    "velocidad," +
                    "velocidadMaq," +
                    "responsable, " +
                    "tipo_recogida," +
                    "hora, " +
                    "minuto ," +
                    "observacion , " +
                    "responsable2 ," +
                    "pr_cumplido ," +
                    "estado ," +
                    "obs2 ," +
                    "hora_inicio , " +
                    "hora_fin ," +
                    "estado2 ," +
                    "estado_sincro,aviario " +
                    " FROM recogidas where   estado_sincro ='P'  " ,null);
            while (cursor.moveToNext()){
                c++;
                String idregistro=cursor.getString(0);
                String fecha=cursor.getString(1);
                String area=cursor.getString(2);
                String doble=cursor.getString(3);
                String velocidad=cursor.getString(4);
                String velocidadmaq=cursor.getString(5);
                String responsable=cursor.getString(6);
                String tipo_recogida=cursor.getString(7);
                String hora=cursor.getString(8);
                String minuto=cursor.getString(9);
                String observacion=cursor.getString(10);
                String responsable2=cursor.getString(11);
                String pr_cumplido=cursor.getString(12);
                String estado=cursor.getString(13);
                String obs2=cursor.getString(14);
                String hora_inicio=cursor.getString(15);
                String hora_fin=cursor.getString(16);
                String aviario=cursor.getString(19);
                String estado2=cursor.getString(17);
                String hora_inicio_format="";
                String hora_fin_format="";
                String fecha_format="";
                if (hora_inicio.equals("NULL")){

                    hora_inicio_format="NULL";
                }
                else {
                    hora_inicio_format="'"+hora_inicio.replaceAll("-","")+"'";
                }
                if (hora_fin.equals("NULL")){

                    hora_fin_format="NULL";
                }
                else {
                    hora_fin_format="'"+hora_fin.replaceAll("-","")+"'";
                }
                if (fecha.equals("NULL")){

                    fecha_format="NULL";
                }
                else {
                    fecha_format="'"+fecha.replaceAll("-","")+"'";
                }
                int idGenerado=0;
                String insertar = "insert into recogidas(" +
                        "fecha," +
                        "area," +
                        "doble," +
                        "velocidad," +
                        "velocidadMaq," +
                        "responsable," +
                        "tipo_recogida," +
                       // "hora, " +
                       //"minuto ," +
                        "observacion , " +
                      "responsable2 ," +
                      "pr_cumplido ," +
                        "estado ," +
                        "obs2 ," +
                        "estado2," +
                        "hora_inicio," +
                        "hora_fin," +
                        "aviario" +//6850
                        ") values  " +
                        "("+fecha_format+"," +
                         "'"+area+"','"+doble+"'," +
                         "'"+velocidad+"'," +
                        "'"+velocidadmaq+"'," +
                         "'"+responsable+"'," +
                        "'"+tipo_recogida+"'," +
                     //  "'"+hora+"'," +
                       //"'"+minuto+"'," +
                      "'"+observacion+"'," +
                        "'"+responsable2+"'," +
                       "'"+pr_cumplido+"'," +
                        "'"+estado+"'," +
                        "'"+obs2+"'," +
                        "'"+estado2+"',"+hora_inicio_format+","+hora_fin_format+",'"+aviario+"')";

                PreparedStatement ps = connect.prepareStatement(insertar,Statement.RETURN_GENERATED_KEYS);

                ps.executeUpdate();
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idGenerado = generatedKeys.getInt(1);
                }

                SQLiteDatabase db_upd=conn.getReadableDatabase();
                String strSQL = "UPDATE recogidas SET  estado_sincro='C', id_sql="+idGenerado+" WHERE    idregistro='"+idregistro+"' ";
                db_upd.execSQL(strSQL);
                ps.close();
                generatedKeys.close();


            }


            SQLiteDatabase db_UPDATE=conn.getReadableDatabase();
            Statement stmt = connect.createStatement();

            String query = "select idregistro from recogidas where estado='E' and convert(date,fecha)='04/11/2019'";
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next())
            {

                String SQL_UPDATE = " update recogidas set  estado ='E', estado2='E' where id_sql=24114";
                db_UPDATE.execSQL(SQL_UPDATE);
                db_UPDATE.close();
            }

            rs.close();


        }catch(Exception e)
        {

         }
    }












    class hilo_regis extends Thread {
        @Override
        public void run() {

            try {


                Thread.sleep(5000);
              //
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                       // test_conexion();
                      //  Toast.makeText(getApplicationContext(),"holaaaaaaaaa", Toast.LENGTH_LONG).show();
                        new hilo_regis().start();

                    /* prodialog = ProgressDialog.show(exportar.this, "EXPORTANDO",
                                "ENVIANDO...", true);
                        new exportar.hilo_regis_animales_upd().start();*/
                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void pendientes()
    {String total="";
        int c=0;
        try {
            conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
            SQLiteDatabase db=conn.getReadableDatabase();
            ConnectionHelperGrupomaehara conexion = new ConnectionHelperGrupomaehara();
            connect = conexion.Connections();
            Cursor cursor=db.rawQuery("SELECT  count(*)  FROM recogidas where   estado_sincro ='P'  " ,null);

            while (cursor.moveToNext()){
                c++;
                  total=cursor.getString(0);
                }
            txt_total.setText(total);
        }catch(Exception e)
        {
            //  Toast.makeText( MainActivity.this,e.toString()+" "+String.valueOf(c),Toast.LENGTH_LONG).show();
        }
    }
    public void stopRunning()
    {
        flag = false;
    }














    class MyThread extends Thread
    {
        //Initially setting the flag as true
        private volatile boolean flag = true;
        //This method will set flag as false
        public void stopRunning()
        {
            flag = false;
        }
        @Override
        public void run()
        { //Keep the task in while loop

            //This will make thread continue to run until flag becomes false

            while (flag)
            {

                try {

                    Thread.sleep((long) 5000);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            System.out.println("CONECTADO");
                          //  test_conexion();
                            // Stuff that updates the UI

                        }
                    });

                } catch (InterruptedException e) {


                }
            }

        }
    }

}

