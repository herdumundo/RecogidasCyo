package com.example.recogidascyo_android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import Utilidades.contenedor_usuario;
import kotlinx.coroutines.CoroutineScope;
 import kotlinx.coroutines.DispatchersKt;

public class MainActivity extends AppCompatActivity
{
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Semaphore semaphore = new Semaphore(1);
    public static final String URL_TO_DOWNLOAD = "http://192.168.125.20:8086/apps/RECOGIDAS_APP.apk";
    private static final short REQUEST_CODE = 6545;
    public static final String NAME_FILE = "Recogidas.apk";


    private volatile boolean flag = true;
    Button btn_ir_reco,btn_list,btnDoble,btn_upd;
    Connection connect;
    ConexionSQLiteHelper conn;
    String total="";
     TextView txt2,txt_total,txt_estado;
    DownloadManager downloadManager;
    private long downloadID;
    String mensaje="";
    int color_mensaje=0;
    public void onBackPressed()  {
        Utilidades.volver_atras(this,this,  login2.class,"¿Desea salir de la aplicación?",5);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_ir_reco=(Button)findViewById(R.id.btn_ir_registro);
        btn_list=(Button)findViewById(R.id.btn_list);
        btnDoble=(Button)findViewById(R.id.btnDoble);
        btn_upd=(Button)findViewById(R.id.btn_upd);
        txt_total=(TextView)findViewById(R.id.txt_total);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        pendientes();

        if(contenedor_usuario.area.equals("1"))
        {
            Intent intent = new Intent(getApplicationContext(), login2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        btn_upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("ATENCION!!!.")
                        .setMessage("DESEA DESCARGAR LA ULTIMA VERSION DE LA APLICACION?.")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                               beginDownload();
                            }

                        })
                        .setNegativeButton("NO", null)
                        .show();
            }
        });

        btn_ir_reco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detenerTarea();
                 Intent i=new Intent(MainActivity.this,Recogidas.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                finish();
                startActivity(i);
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detenerTarea();

                Intent i=new Intent(MainActivity.this,lista_recogidas.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                finish();
                startActivity(i);

            }
        });

        btnDoble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detenerTarea();

                Intent i=new Intent(MainActivity.this,Recogidas_doble.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                finish();
                startActivity(i);
            }
        });

        iniciarTarea();

    }


    public   void exportar_datos (View v)
    {exportarRegistros();
         }

    private void exportarRegistros(){
        try {
            SQLiteDatabase db=conn.getReadableDatabase();
            SQLiteDatabase db_UPDATE=conn.getReadableDatabase();

            Cursor cursor;
            ConnectionHelperGrupomaehara conexion = new ConnectionHelperGrupomaehara();
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select idregistro from recogidas where estado='E'  and ( convert(date,fecha)=convert(varchar,getdate(),103) or convert(date,hora_inicio)=convert(varchar,getdate(),103))");
            while ( rs.next())
            {
                db_UPDATE.execSQL(" update recogidas set  estado ='E', estado2='E' where id_sql="+rs.getString("idregistro")+"");

            }

            cursor=db.rawQuery("SELECT idregistro ,  fecha,area,doble,velocidad,velocidadMaq,responsable, tipo_recogida, hora,  minuto ,  observacion , responsable2 ,  " +
                    "pr_cumplido ,  estado ,  obs2 , hora_inicio ,  hora_fin ,  estado2 ,   estado_sincro,aviario    " +
                    "FROM recogidas where   estado_sincro ='P'  " ,null);
            while (cursor.moveToNext())
            {
                int idGenerado=0;
                int tipo_mensaje=0;
                CallableStatement callableStatement=null;
                callableStatement = connect.prepareCall("{call pa_exportar_recogidas( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )}");
                callableStatement .setString("@fecha",cursor.getString(1));
                callableStatement .setString("@area",cursor.getString(2));
                callableStatement .setString("@doble",cursor.getString(3));
                callableStatement .setString("@velocidad",cursor.getString(4));
                callableStatement .setString("@velocidadMaq",cursor.getString(5));
                callableStatement .setString("@responsable",cursor.getString(6));
                callableStatement .setString("@tipo_recogida",cursor.getString(7));
                callableStatement .setString("@observacion",cursor.getString(10).replaceAll("'"," "));
                callableStatement .setString("@responsable2",cursor.getString(11));
                callableStatement .setString("@pr_cumplido",cursor.getString(12));
                callableStatement .setString("@estado",cursor.getString(13));
                callableStatement .setString("@obs2",cursor.getString(14));
                callableStatement .setString("@estado2",cursor.getString(17));
                callableStatement .setString("@hora_inicio",cursor.getString(15));
                callableStatement .setString("@hora_fin",cursor.getString(16));
                callableStatement .setString("@aviario",cursor.getString(19));
                callableStatement.registerOutParameter("@id", Types.INTEGER);
                callableStatement.registerOutParameter("@tipo_mensaje", Types.INTEGER);
                callableStatement.execute();
                idGenerado = callableStatement.getInt("@id");
                tipo_mensaje = callableStatement.getInt("@tipo_mensaje");

                if(tipo_mensaje==1){
                    SQLiteDatabase db_upd=conn.getReadableDatabase();
                    String strSQL = "UPDATE recogidas SET  estado_sincro='C', id_sql="+idGenerado+" WHERE    idregistro='"+cursor.getString(0)+"' ";
                    db_upd.execSQL(strSQL);
                    db_UPDATE.close();
                }
            }
            pendientes();
        }catch(Exception e)
        {
            Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
     private void pendientes()
    {
         try
         {
            SQLiteDatabase db=conn.getReadableDatabase();
            Cursor cursor=db.rawQuery("SELECT  count(*)  FROM recogidas where   estado_sincro ='P'  " ,null);
            while (cursor.moveToNext())
            {
                total=cursor.getString(0);
            }
            txt_total.setText(total);
            cursor.close();
            db.close();
        }
        catch(Exception e)
        {
            Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private static boolean isDownloadManagerAvailable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }
    private void checkSelfPermission() {

            executeDownload();

     }
    private void executeDownload() {
        File path = null;
             path = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS);
         File file = new File(path, "Recogidas.apk");
        if(file.exists()){
            file.delete();
        }
        // registrer receiver in order to verify when download is complete
        registerReceiver(new DonwloadCompleteReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        //registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        DownloadManager.Request request = null;
             request = new DownloadManager.Request(Uri.parse(URL_TO_DOWNLOAD));


             request.setDescription("Descargando actualizacion app " + NAME_FILE+", Espere...");
            request.setTitle("App Recogidas!");

         // in order for this if to run, you must use the android 3.2 to compile your app
             request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
             request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, NAME_FILE);

        // get download service and enqueue file
        DownloadManager manager = null;
             manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);




    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the work
                    executeDownload();
                } else {
                    // permission denied!
                    Toast.makeText(this, "Please give permissions ", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void beginDownload() {


            if (isDownloadManagerAvailable()) {
                checkSelfPermission();
            } else {
                Toast.makeText(this, "Download manager is not available", Toast.LENGTH_LONG).show();
            }


    }


    public void iniciarTarea() {
        scheduler.scheduleWithFixedDelay(() -> {
            try {
                semaphore.acquire();
                exportarRegistros();
                Log.i( "mensaje","Datos exportado");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }, 0, 3, TimeUnit.MINUTES);
    }
    public void detenerTarea() {
        scheduler.shutdown();
    }

}

