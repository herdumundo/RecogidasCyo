package com.example.recogidascyo_android;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
 import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.util.ArrayList;
import Utilidades.contenedor_usuario;
import entidades.Aviario_fin;
import entidades.Aviario_ini;
import entidades.Motivo;
import entidades.consultas;

public class Recogidas_doble extends AppCompatActivity {
    Button btn_registrar,btn_cancelar,btn_fin,btn_inicio;
  public static   Spinner cbox_estado,cbox_aviario,cbox_tipo_recogida;
    ConexionSQLiteHelper conn;
    ArrayList<Aviario_ini> aviariolist_ini;
    ArrayList<String> lista_aviarios_ini;
    Connection connect;
    ArrayList<Aviario_fin> aviariolist_fin;
    ArrayList<String> lista_aviarios_fin;
    ArrayList<Motivo> motivolist;
    ArrayList<String> lista_motivo;
TextView velocidadMaq,velocidad,txt_comentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recogidas_doble);
        btn_registrar=(Button)findViewById(R.id.btn_registrar);
        btn_cancelar=(Button)findViewById(R.id.btn_cancelar);
        btn_fin=(Button)findViewById(R.id.btn_fin);
        btn_inicio=(Button)findViewById(R.id.btn_inicio);
        cbox_aviario=(Spinner)findViewById(R.id.cbox_aviario);
        cbox_tipo_recogida=(Spinner)findViewById(R.id.cbox_tipo_recogida);
        cbox_estado=(Spinner)findViewById(R.id.cbox_estado);
        btn_fin.setBackgroundColor(getResources().getColor(R.color.negro));
        btn_inicio.setBackgroundColor(getResources().getColor(R.color.verde));
        btn_registrar.setBackgroundColor(getResources().getColor(R.color.colorazul));
        btn_cancelar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        velocidadMaq=(TextView)findViewById(R.id.txt_velocidadMaq);
        velocidad=(TextView)findViewById(R.id.txt_velocidad);
        txt_comentario=(TextView)findViewById(R.id.txt_comentario);
        if(contenedor_usuario.area.equals("1")){
            Intent intent = new Intent(getApplicationContext(), login2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else{

        }
         consultas.llenar_tipo_recogida(this,2);
        consultas.llenar_aviarios(this,2);
        consultas.llenar_estados(this,2);



        llenar_aviario_parada_inicio();
        llenar_aviario_parada_fin();
        btn_fin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ir_cuadro_fin();
            }
        });


        btn_inicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

             ir_cuadro();

            }
        });
        btn_registrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                grabar();

            }
        });


    }




    private void llenar_aviario_parada_inicio(){
    SQLiteDatabase db=conn.getReadableDatabase();
    Aviario_ini Aviario_ini=null;
    aviariolist_ini =new ArrayList<Aviario_ini>();
    String select_inicio="select aviario from recogidas where area='"+contenedor_usuario.area+"' and date(fecha)=date('now','localtime')  and doble='SI' and estado='A' and aviario not in (select aviario from recogidas where date(fecha)=date('now','localtime') and  estado='F' and doble='SI') order by aviario";

    Cursor cursor = db.rawQuery(select_inicio, null);
    while (cursor.moveToNext())
    {
        Aviario_ini=new Aviario_ini();
        Aviario_ini.setAviario(cursor.getString(0));
        Log.i("aviario",Aviario_ini.getAviario());
        aviariolist_ini.add(Aviario_ini);
    }
    obtenerListaAviario_ini();
}
    public void obtenerListaAviario_ini()
    {
        lista_aviarios_ini = new ArrayList<String>();
        for (int i = 0; i < aviariolist_ini.size(); i++)
        {
            lista_aviarios_ini.add(aviariolist_ini.get(i).getAviario());
        }

    }

    private void llenar_aviario_parada_fin(){
        SQLiteDatabase db=conn.getReadableDatabase();
        Aviario_fin Aviario_fin=null;
        aviariolist_fin =new ArrayList<Aviario_fin>();
        //String select_fin="select aviario from recogidas where area='"+contenedor_usuario.area+"' and date(fecha)=date('now','localtime')  and doble='NO' and estado='A' and aviario not in (select aviario from recogidas where date(fecha)=date('now','localtime') and  estado='F') order by aviario";
        String select_fin=" select aviario from recogidas where doble='SI' and area='"+contenedor_usuario.area+"' and estado2='P' and date(hora_inicio)=date('now','localtime')  order by aviario";
        Cursor cursor = db.rawQuery(select_fin, null);
        while (cursor.moveToNext())
        {
            Aviario_fin=new Aviario_fin();
            Aviario_fin.setAviario(cursor.getString(0));
            Log.i("aviario",Aviario_fin.getAviario());
            aviariolist_fin.add(Aviario_fin);
        }
        obtenerListaAviario_fin();
    }

    public void obtenerListaAviario_fin()
    {
        lista_aviarios_fin = new ArrayList<String>();
        for (int i = 0; i < aviariolist_fin.size(); i++)
        {
            lista_aviarios_fin.add(aviariolist_fin.get(i).getAviario());
        }
    }

 public void llenar_motivo()
    {
        SQLiteDatabase db=conn.getReadableDatabase();
        Motivo Motivo=null;
        motivolist =new ArrayList<Motivo>();
        Cursor cursor = db.rawQuery("SELECT * FROM motivo_parada ", null);
        while (cursor.moveToNext())
        {
            Motivo=new Motivo();
            Motivo.setMotivo(cursor.getString(1));
            Log.i("descripcion",Motivo.getMotivo());
            motivolist.add(Motivo);
        }
        obtenerListamotivo();
    }
    public void obtenerListamotivo()
    {
        lista_motivo = new ArrayList<String>();
        for (int i = 0; i < motivolist.size(); i++)
        {
            lista_motivo.add(motivolist.get(i).getMotivo());
        }
    }






    private void ir_cuadro()
    {

            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Recogidas_doble.this);
        final View mView = getLayoutInflater().inflate(R.layout.cuadro_inicio_parada, null);
        final MultiSelectionSpinner doble_spinner = (MultiSelectionSpinner) mView.findViewById(R.id.mSpinner);
        final Button btn_registrar = (Button) mView.findViewById(R.id.btn_grabar_inicio);
        final TextView txt_obs = (TextView) mView.findViewById(R.id.txt_comentario_parada_ini);

        final SearchableSpinner spinner_motivo=(SearchableSpinner)mView.findViewById(R.id.spinner_motivo);
        llenar_motivo();
        ArrayAdapter<CharSequence> adaptador_motivo=new ArrayAdapter (this,R.layout.spinner_item_estado,lista_motivo);
        spinner_motivo.setAdapter(adaptador_motivo);
        //set items to spinner from list
        doble_spinner.setItems(lista_aviarios_ini);
        btn_registrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String hora_actual="";
                String aviario_parte="";
                String consulta_parada="";
                String consulta_cerrado="";
                String sql= "select datetime('now','localtime')  ";
                SQLiteDatabase db_consulta_registro=conn.getReadableDatabase();
                Cursor cursor_consulta=db_consulta_registro.rawQuery(sql,null);
                while (cursor_consulta.moveToNext()){
                    hora_actual=cursor_consulta.getString(0);
                }
                String cbox_total=doble_spinner.getSelectedItem().toString();
                String [] arr_combo=cbox_total.split(",");
                for(int i=0; i<arr_combo.length; i++)
                {
                    aviario_parte=arr_combo[i];
                    String parada="select count(*) from recogidas where aviario='"+aviario_parte+"' and estado2='P'  and  date(hora_inicio)=date('now','localtime')";
                    String estado_f  ="select count(*) from recogidas where aviario='"+aviario_parte+"' and estado='F'  and  date(fecha)=date('now','localtime') and doble='SI'";
                    SQLiteDatabase db_consulta_parada=conn.getReadableDatabase();
                    Cursor cursor_consulta_parada=db_consulta_parada.rawQuery(parada,null);
                    while (cursor_consulta_parada.moveToNext())
                    {
                        consulta_parada=cursor_consulta_parada.getString(0);
                    }
                    conn.close();
                    SQLiteDatabase db_consulta_cerrado=conn.getReadableDatabase();
                    Cursor cursor_consulta_cerrado=db_consulta_cerrado.rawQuery(estado_f,null);
                    while (cursor_consulta_cerrado.moveToNext())
                    {
                        consulta_cerrado=cursor_consulta_cerrado.getString(0);
                    }
                    conn.close();
                    if(Integer.parseInt(consulta_cerrado.trim())>0)
                    {
                        Toast.makeText(Recogidas_doble.this,"ERROR, AVIARIO SE ENCUENTRA CERRADO",Toast.LENGTH_LONG).show();
                    }
                  else  if(Integer.parseInt(consulta_parada.trim())>0)
                    {
                        Toast.makeText(Recogidas_doble.this,"ERROR PARADA",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        SQLiteDatabase db=conn.getReadableDatabase();
                        ContentValues values=new ContentValues();
                        values.put("area",contenedor_usuario.area);
                        values.put("aviario",aviario_parte);
                        values.put("fecha","NULL");
                        values.put("velocidad","");
                        values.put("velocidadMaq","");
                        values.put("responsable",contenedor_usuario.usuario);
                        values.put("tipo_recogida","");
                        values.put("observacion",txt_obs.getText().toString());
                        values.put("responsable2",contenedor_usuario.nombre_usuario);
                        values.put("estado","");
                        values.put("estado2","P");
                        values.put("doble","SI");
                        values.put("hora_inicio",hora_actual);
                        values.put("hora_fin","NULL");
                        values.put("estado_sincro","A");
                        db.insert("recogidas", "area",values);
                        Toast.makeText(Recogidas_doble.this,"REGISTRADO"+consulta_cerrado+consulta_parada,Toast.LENGTH_LONG).show();
                    }
                }
                new AlertDialog.Builder(Recogidas_doble.this)
                        .setTitle("INFORME!!!")
                        .setMessage("REGISTRADO CON EXITO")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                            public void onClick(DialogInterface dialog, int id) {
                                Recogidas_doble.this.finish();
                                Intent i=new Intent(Recogidas_doble.this,MainActivity.class);
                                startActivity(i);
                            }
                        }).show();
            }
        });
        mBuilder.setView(mView);
        final  AlertDialog dialog = mBuilder.create();
        dialog.show();
        //dialog.setCanceledOnTouchOutside(false);
       // dialog.setCancelable(false);
    }

    private void ir_cuadro_fin()
    {
        final  AlertDialog.Builder mBuilder = new   AlertDialog.Builder(Recogidas_doble.this);
        final View mView = getLayoutInflater().inflate(R.layout.cuadro_fin_parada, null);
        final MultiSelectionSpinner doble_spinner_fin = (MultiSelectionSpinner) mView.findViewById(R.id.mSpinner_fin);
        final Button btn_grabar=(Button)mView.findViewById(R.id.btn_grabar_fin);

        btn_grabar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String avi_parte="";

                String cbox_total=doble_spinner_fin.getSelectedItem().toString();
                String [] arr_combo=cbox_total.split(",");

                for(int i=0; i<arr_combo.length; i++)
                {
                    avi_parte=arr_combo[i];
                    ///SQLiteDatabase db_upd_parada=conn.getReadableDatabase();



                    SQLiteDatabase db2=conn.getReadableDatabase();
                    String strSQL = "UPDATE recogidas SET  estado2='H', hora_fin=datetime('now','localtime'), estado_sincro='P' WHERE " +
                            " estado2='P' and aviario = '"+avi_parte.trim()+"' and doble='SI' ";
                    db2.execSQL(strSQL);
             }
                new AlertDialog.Builder(Recogidas_doble.this)
                        .setTitle("INFORME!!!")
                        .setMessage("REGISTRADO CON EXITO")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                            public void onClick(DialogInterface dialog, int id) {
                                Recogidas_doble.this.finish();
                                Intent i=new Intent(Recogidas_doble.this,MainActivity.class);
                                startActivity(i);
                            }
                        }).show();


            }
            });

        //set items to spinner from list
        doble_spinner_fin.setItems(lista_aviarios_fin);
        mBuilder.setView(mView);
        final  AlertDialog dialog = mBuilder.create();
        dialog.show();
        //dialog.setCanceledOnTouchOutside(false);
        // dialog.setCancelable(false);
    }

private void grabar(){

    String parse_estado="";

    String tipo_recogida_format="";

    if(cbox_tipo_recogida.getSelectedItem().toString().equals("MECANIZADA")){

        tipo_recogida_format="M";
    }
    else {
        tipo_recogida_format="T";
    }

    if(velocidad.getText().length()==0){

        new AlertDialog.Builder(Recogidas_doble.this)
                .setTitle("ERROR!!!")
                .setMessage("DEBE CARGAR VELOCIDAD DE RECOGIDA")
                .setNegativeButton("CERRAR", null).show();
    }

    else if(velocidadMaq.getText().length()==0){

        new AlertDialog.Builder(Recogidas_doble.this)
                .setTitle("ERROR!!!")
                .setMessage("DEBE CARGAR VELOCIDAD DE MAQUINA")
                .setNegativeButton("CERRAR", null).show();
    }
    else {

        if(cbox_estado.getSelectedItem().toString().equals("ABIERTO")){

            parse_estado="A";
        }
        else if(cbox_estado.getSelectedItem().toString().equals("CAMBIO DE VELOCIDAD")) {
            parse_estado="-";
        }
        else
        {
            parse_estado="F";
        }







        if(parse_estado.equals("A")){
            String datos="0";
            String sql= "select count(*) from recogidas  where date(fecha)=date('now','localtime') and aviario='"+cbox_aviario.getSelectedItem().toString().trim()+"' and estado='"+parse_estado.trim()+"' and doble='SI' ";

            SQLiteDatabase db_consulta_aviario=conn.getReadableDatabase();
            String consulta_aviario= "select * from recogidas  where date(fecha)=date('now','localtime')  and aviario='"+cbox_aviario.getSelectedItem().toString().trim()+"'   and estado='F'";
            Cursor cursor_consulta_aviario=db_consulta_aviario.rawQuery(consulta_aviario,null);


            SQLiteDatabase db_consulta_registro=conn.getReadableDatabase();
            Cursor cursor_consulta=db_consulta_registro.rawQuery(sql,null);
            while (cursor_consulta.moveToNext()){

                datos=cursor_consulta.getString(0);
            }

            if (!cursor_consulta_aviario.moveToNext()){
                new AlertDialog.Builder(Recogidas_doble.this)
                        .setTitle("ERROR!!!")
                        .setMessage("ERROR, DEBE FINALIZAR APERTURA PENDIENTE")
                        .setNegativeButton("CERRAR", null).show();

            }
            else
            {
                if (datos.equals("0"))
                {
                    String fecha_actual="";
                    String sql_fecha= "SELECT datetime('now','localtime')";

                    SQLiteDatabase db_consulta_fecha=conn.getReadableDatabase();
                    Cursor cursor_consulta_fecha=db_consulta_fecha.rawQuery(sql_fecha,null);
                    while (cursor_consulta_fecha.moveToNext()){

                        fecha_actual=cursor_consulta_fecha.getString(0);
                    }
                    SQLiteDatabase db=conn.getReadableDatabase();
                    ContentValues values=new ContentValues();
                    values.put("area",contenedor_usuario.area);
                    values.put("fecha",fecha_actual);
                    values.put("aviario",cbox_aviario.getSelectedItem().toString());
                    values.put("velocidad",velocidad.getText().toString());
                    values.put("velocidadMaq",velocidadMaq.getText().toString());
                    values.put("responsable",contenedor_usuario.usuario);
                    values.put("tipo_recogida",tipo_recogida_format);
                    values.put("observacion",txt_comentario.getText().toString());
                    values.put("responsable2",contenedor_usuario.nombre_usuario);
                    values.put("estado",parse_estado.trim());
                    values.put("estado2","");
                    values.put("hora_inicio","NULL");
                    values.put("hora_fin","NULL");
                    values.put("doble","SI");
                    values.put("estado_sincro","P");
                    db.insert("recogidas", "area",values);


                    new AlertDialog.Builder(Recogidas_doble.this)
                            .setTitle("INFORME!!!")
                            .setMessage("REGISTRADO CON EXITO")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                                public void onClick(DialogInterface dialog, int id) {
                                    Recogidas_doble.this.finish();
                                    Intent i=new Intent(Recogidas_doble.this,MainActivity.class);
                                    startActivity(i);
                                }
                            }).show();
                }
                else
                {

                    //  Toast.makeText(Recogidas.this,"ERROR, AVIARIO CARGADO ANTERIORMENTE",Toast.LENGTH_LONG).show();
                    new AlertDialog.Builder(Recogidas_doble.this)
                            .setTitle("ERROR!!!")
                            .setMessage("AVIARIO CARGADO ANTERIORMENTE")
                            .setNegativeButton("CERRAR", null).show();
                }
            }



        }









        if(parse_estado.equals("F")){
            String datos="0";

            String sql= "select count(*) from recogidas  where date(fecha)=date('now','localtime') and aviario='"+cbox_aviario.getSelectedItem().toString().trim()+"' and estado='A' and doble='SI'";
            String datos_cerrado="0";
            String datos_parada="0";
            SQLiteDatabase db_consulta_registro=conn.getReadableDatabase();
            Cursor cursor_consulta=db_consulta_registro.rawQuery(sql,null);
            while (cursor_consulta.moveToNext()){

                datos=cursor_consulta.getString(0);
            }

            String sql2= "select count(*) from recogidas  where date(fecha)=date('now','localtime') and aviario='"+cbox_aviario.getSelectedItem().toString().trim()+"' and estado='F' and doble='SI'";
            String sql_parada="select count(*) from recogidas where aviario='"+cbox_aviario.getSelectedItem().toString()+"' and estado2='P'  and date(hora_inicio)=date('now','localtime')" ;

            SQLiteDatabase db_consulta_registro_cerrado=conn.getReadableDatabase();
            Cursor cursor_consulta_cerrado=db_consulta_registro_cerrado.rawQuery(sql2,null);
            while (cursor_consulta_cerrado.moveToNext()){

                datos_cerrado=cursor_consulta_cerrado.getString(0);
            }

            SQLiteDatabase db_consulta_registro_parada=conn.getReadableDatabase();
            Cursor cursor_consulta_parada=db_consulta_registro_parada.rawQuery(sql_parada,null);
            while (cursor_consulta_parada.moveToNext()){

                datos_parada=cursor_consulta_parada.getString(0);
            }
            if (datos.equals("0"))
            {
                new AlertDialog.Builder(Recogidas_doble.this)
                        .setTitle("ERROR!!!")
                        .setMessage("ERROR, NO TIENE NINGUNA APERTURA PARA ESTE AVIARIO")
                        .setNegativeButton("CERRAR", null).show();
            }
            else if (Integer.parseInt(datos_cerrado.trim())>0){
                new AlertDialog.Builder(Recogidas_doble.this)
                        .setTitle("ERROR!!!")
                        .setMessage("EL AVIARIO YA SE ENCUENTRA CERRADO")
                        .setNegativeButton("CERRAR", null).show();

            }

            else if (Integer.parseInt(datos_parada.trim())>0){
                new AlertDialog.Builder(Recogidas_doble.this)
                        .setTitle("ERROR!!!")
                        .setMessage("ERROR, PARA REGISTRAR EL CAMBIO, DEBE FINALIZAR LA PARADA PENDIENTE")
                        .setNegativeButton("CERRAR", null).show();

            }
            else {
                String fecha_actual="";
                String sql_fecha= "SELECT datetime('now','localtime')";

                SQLiteDatabase db_consulta_fecha=conn.getReadableDatabase();
                Cursor cursor_consulta_fecha=db_consulta_fecha.rawQuery(sql_fecha,null);
                while (cursor_consulta_fecha.moveToNext()){

                    fecha_actual=cursor_consulta_fecha.getString(0);
                }
                SQLiteDatabase db=conn.getReadableDatabase();
                ContentValues values=new ContentValues();
                values.put("area",contenedor_usuario.area);
                values.put("fecha",fecha_actual);
                values.put("aviario",cbox_aviario.getSelectedItem().toString());
                values.put("velocidad",velocidad.getText().toString());
                values.put("velocidadMaq",velocidadMaq.getText().toString());
                values.put("responsable",contenedor_usuario.usuario);
                values.put("tipo_recogida",tipo_recogida_format);
                values.put("observacion",txt_comentario.getText().toString());
                values.put("responsable2",contenedor_usuario.nombre_usuario);
                values.put("estado",parse_estado.trim());
                values.put("estado2","");
                values.put("hora_fin","NULL");
                values.put("hora_inicio","NULL");
                values.put("doble","SI");
                values.put("estado_sincro","P");
                db.insert("recogidas", "area",values);
                new AlertDialog.Builder(Recogidas_doble.this)
                        .setTitle("INFORME!!!")
                        .setCancelable(false)
                        .setMessage("REGISTRADO CON EXITO")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                            public void onClick(DialogInterface dialog, int id) {
                                Recogidas_doble.this.finish();
                                Intent i=new Intent(Recogidas_doble.this,MainActivity.class);
                                startActivity(i);
                            }
                        }).show();
            }
        }




        if(parse_estado.equals("-")){

            String datos_parada="0";
             String datos_cerrado="0";
            String sql2= "select count(*) from recogidas  where date(fecha)=date('now','localtime') and aviario='"+cbox_aviario.getSelectedItem().toString().trim()+"' and estado='F' and doble='SI' ";
            String sql_parada="select count(*) from recogidas where aviario='"+cbox_aviario.getSelectedItem().toString()+"' and estado2='P'  and date(hora_inicio)=date('now','localtime')" ;

            SQLiteDatabase db_consulta_registro_cerrado=conn.getReadableDatabase();
            Cursor cursor_consulta_cerrado=db_consulta_registro_cerrado.rawQuery(sql2,null);
            while (cursor_consulta_cerrado.moveToNext()){

                datos_cerrado=cursor_consulta_cerrado.getString(0);
            }
            SQLiteDatabase db_consulta_registro_parada=conn.getReadableDatabase();
            Cursor cursor_consulta_parada=db_consulta_registro_parada.rawQuery(sql_parada,null);
            while (cursor_consulta_parada.moveToNext()){

                datos_parada=cursor_consulta_parada.getString(0);
            }


            SQLiteDatabase db_consulta_aviario=conn.getReadableDatabase();
            String consulta_aviario= "  select * from recogidas  where  date(fecha)=date('now','localtime')  and aviario='"+cbox_aviario.getSelectedItem().toString().trim()+"' and estado='A' and doble='SI'";
            Cursor cursor_consulta_aviario=db_consulta_aviario.rawQuery(consulta_aviario,null);


            if (!cursor_consulta_aviario.moveToNext()){
                new AlertDialog.Builder(Recogidas_doble.this)
                        .setTitle("ERROR!!!")
                        .setMessage("ERROR, NO TIENE NINGUNA APERTURA PARA ESTE AVIARIO")
                        .setNegativeButton("CERRAR", null).show();

            }
            else if (Integer.parseInt(datos_cerrado.trim())>0){
                new AlertDialog.Builder(Recogidas_doble.this)
                        .setTitle("ERROR!!!")
                        .setMessage("EL AVIARIO YA SE ENCUENTRA CERRADO, NO PUEDE REALIZAR UN CAMBIO DE VELOCIDAD"+datos_cerrado.length())
                        .setNegativeButton("CERRAR", null).show();

            }
            else if (Integer.parseInt(datos_parada.trim())>0){
                new AlertDialog.Builder(Recogidas_doble.this)
                        .setTitle("ERROR!!!")
                        .setMessage("ERROR, PARA REGISTRAR EL CAMBIO, DEBE FINALIZAR LA PARADA PENDIENTE")
                        .setNegativeButton("CERRAR", null).show();

            }



            else
                {
                    String fecha_actual="";
                    String sql_fecha= "SELECT datetime('now','localtime')";

                    SQLiteDatabase db_consulta_fecha=conn.getReadableDatabase();
                    Cursor cursor_consulta_fecha=db_consulta_fecha.rawQuery(sql_fecha,null);
                    while (cursor_consulta_fecha.moveToNext()){

                        fecha_actual=cursor_consulta_fecha.getString(0);
                    }
                SQLiteDatabase db=conn.getReadableDatabase();
                ContentValues values=new ContentValues();
                values.put("area",contenedor_usuario.area);
                values.put("fecha",fecha_actual);
                values.put("aviario",cbox_aviario.getSelectedItem().toString());
                values.put("velocidad",velocidad.getText().toString());
                values.put("velocidadMaq",velocidadMaq.getText().toString());
                values.put("responsable",contenedor_usuario.usuario);
                values.put("tipo_recogida",tipo_recogida_format);
                values.put("observacion",txt_comentario.getText().toString());
                values.put("responsable2",contenedor_usuario.nombre_usuario);
                values.put("estado",parse_estado.trim());
                values.put("estado2","");
                values.put("hora_inicio","NULL");
                values.put("hora_fin","NULL");
                values.put("doble","SI");
                values.put("estado_sincro","P");
                db.insert("recogidas", "area",values);
                    new AlertDialog.Builder(Recogidas_doble.this)
                            .setTitle("INFORME!!!")
                            .setCancelable(false)
                            .setMessage("REGISTRADO CON EXITO")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                        public void onClick(DialogInterface dialog, int id) {
                            Recogidas_doble.this.finish();
                            Intent i=new Intent(Recogidas_doble.this,MainActivity.class);
                            startActivity(i);
                        }
                    }).show();




                }
        }


        llenar_aviario_parada_inicio();
    }
 }


    @Override
    public void onBackPressed()
    {


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);

    }



}
