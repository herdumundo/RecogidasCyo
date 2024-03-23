package com.example.recogidascyo_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class Utilidades {



   public static final String CREAR_TABLA_AVIARIOS="CREATE TABLE aviarios (aviario TEXT PRIMARY KEY, bloque TEXT  )";

   public static final String CREAR_TABLA_USUARIOS="CREATE TABLE usuarios (cod_usuario INTEGER PRIMARY KEY,usuario TEXT ,password TEXT, clasificadora TEXT,rol TEXT, nombre TEXT)";
   public static final String CREAR_TABLA_MOTIVO_PARADA="CREATE TABLE motivo_parada (id INTEGER PRIMARY KEY ,descripcion TEXT)";
   public static final String CREAR_TABLA_RECOGIDAS="CREATE TABLE recogidas (idregistro INTEGER PRIMARY KEY AUTOINCREMENT,FECHA TEXT ,area TEXT,aviario TEXT,doble TEXT,velocidad TEXT,velocidadMaq TEXT,responsable TEXT, tipo_recogida TEXT,hora TEXT, minuto TEXT,observacion TEXT, responsable2 TEXT,pr_cumplido TEXT,estado TEXT,obs2 TEXT,hora_inicio TEXT, hora_fin TEXT,estado2 TEXT,estado_sincro TEXT,id_sql INTEGER,cierre TEXT,fecha_apertura TEXT)";
   public static   AlertDialog.Builder builder;
   public static   AlertDialog ad;

   public static void volver_atras(Context context, Activity activity, Class clase_destino, String texto, int tipo)
   {
      switch(tipo)
      {
         // declaración case
         // los valores deben ser del mismo tipo de la expresión
         case 1 :
            builder = new AlertDialog.Builder(context);
            builder.setIcon(context.getResources().getDrawable(R.drawable.ic_danger));
            builder.setTitle("¡Atención!");
            builder.setMessage(texto);
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
            {
               @Override
               public void onClick(DialogInterface dialog, int which) {

                  Intent intent = new Intent(context, clase_destino);
                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                  context.startActivity(intent);
                  activity.finish();
                  // CustomIntent.customType(context,"right-to-left");

               }
            });
            builder.setNegativeButton("No",null);
            ad = builder.show();
            ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.verdeOscuro));
            ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.verdeOscuro));
            ad.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            ad.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
            break; // break es opcional
         case 3 :
            builder = new AlertDialog.Builder(context);
            builder.setIcon(context.getResources().getDrawable(R.drawable.ic_danger));
            builder.setTitle("¡Atención!");
            builder.setMessage(texto);
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
            {
               @Override
               public void onClick(DialogInterface dialog, int which)
               {
                  Intent intent = new Intent(context, clase_destino);
                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  context.startActivity(intent);
                  activity.finish();
               }
            });
            builder.setNegativeButton("No",null);
            ad = builder.show();
            ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
            ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
            ad.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            ad.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
            break; // break es opcional
         case 5 :
            // Declaraciones
            //CustomIntent.customType(context,"right-to-left");
            activity.finish();
            System.exit(0);
      }
   }
}