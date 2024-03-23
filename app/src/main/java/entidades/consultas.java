package entidades;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recogidascyo_android.ConexionSQLiteHelper;
import com.example.recogidascyo_android.MainActivity;
import com.example.recogidascyo_android.MultiSelectionSpinner;
import com.example.recogidascyo_android.R;
import com.example.recogidascyo_android.Recogidas;
import com.example.recogidascyo_android.Recogidas_doble;

import java.util.ArrayList;

import Utilidades.contenedor_usuario;
public class consultas {
    public static ArrayList<String> lista_aviarios;
    public static ArrayList<String> lista_estados;
    public static ArrayList<String> lista_tipo_recogidas;
    public static ArrayList<String> lista_motivo;
    public static ArrayList<String> lista_aviarios_ini;
    public static ArrayList<String> lista_aviarios_fin;

    public static void llenar_aviarios(Context context ,int tipo)
    {
        try
        {
            ConexionSQLiteHelper conn;
            conn=new ConexionSQLiteHelper(context,"bd_usuarios",null,1);
            SQLiteDatabase db=conn.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT aviario FROM aviarios where bloque='"+ contenedor_usuario.area.trim()+"' ", null);
            lista_aviarios = new ArrayList<String>();
            while (cursor.moveToNext())
            {
                lista_aviarios.add(cursor.getString(0).toString());
            }
            db.close();
            ArrayAdapter<CharSequence> adaptador_aviarios=new ArrayAdapter (context,R.layout.spinner_item_estado,consultas.lista_aviarios);
            if(tipo==1)
            {
                Recogidas.cbox_aviario.setAdapter(adaptador_aviarios);
            }
            else
            {
                Recogidas_doble.cbox_aviario.setAdapter(adaptador_aviarios);
            }
            //Toast.makeText(context,"AVIARIOS SETEADOS.",Toast.LENGTH_LONG).show();

        }catch(Exception e)
        {
            Toast.makeText(context, e.toString() ,Toast.LENGTH_LONG).show();
        }
    }
    public static void llenar_estados(Context context,int tipo)
    {
        lista_estados=new ArrayList<String>();
        lista_estados.add("ABIERTO");
        lista_estados.add("CAMBIO DE VELOCIDAD");
        lista_estados.add("CERRADO");
        ArrayAdapter<CharSequence> adaptador_estado=new ArrayAdapter (context, R.layout.spinner_item_estado,consultas.lista_estados);
        if(tipo==1){
            Recogidas.cbox_estado.setAdapter(adaptador_estado);
        }
        else {
            Recogidas_doble.cbox_estado.setAdapter(adaptador_estado);
        }
       // Toast.makeText(context,"ESTADOS SETEADOS.",Toast.LENGTH_LONG).show();
    }

    public static void llenar_tipo_recogida(Context context,int tipo)
    {
        lista_tipo_recogidas=new ArrayList<String>();
        lista_tipo_recogidas.add("MECANIZADA");
        lista_tipo_recogidas.add("MANUAL");
        ArrayAdapter<CharSequence> adaptador_tipo_recogida=new ArrayAdapter (context, R.layout.spinner_item_estado,lista_tipo_recogidas);
        if(tipo==1){
            Recogidas.cbox_tipo_recogida.setAdapter(adaptador_tipo_recogida);
        }
        else {
            Recogidas_doble.cbox_tipo_recogida.setAdapter(adaptador_tipo_recogida);
        }
    }

    public static void llenar_motivo(Context context,int tipo)
    {   ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context,"bd_usuarios",null,1);

        SQLiteDatabase db=conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM motivo_parada ", null);
        lista_motivo = new ArrayList<String>();

        while (cursor.moveToNext())
        {
            lista_motivo.add(cursor.getString(1));
        }
        db.close();

    }

    public static void llenar_aviario_parada_inicio(Context context,int tipo,String doble){
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context,"bd_usuarios",null,1);

        SQLiteDatabase db=conn.getReadableDatabase();
        lista_aviarios_ini = new ArrayList<String>();

        String select_inicio="select aviario from recogidas where area='"+contenedor_usuario.area+"' " +
            "and (date(fecha)=date('now','localtime') or date(fecha)=date('now','-1 day','localtime'))  and doble='"+doble+"' and estado='A' and cierre='P'" +
            "and aviario not in " +
            "(select aviario from recogidas where  (date(hora_inicio)=date('now','localtime') or date(hora_inicio)=date('now','-1 day','localtime')) and  estado2='P')  order by aviario";
        Cursor cursor = db.rawQuery(select_inicio, null);
        String aviarios_pendientes="";
        int c=0;

        String aviarios_abiertos="select aviario from recogidas where area='"+contenedor_usuario.area+"'" +
            "  and (date(fecha)=date('now','localtime') or date(fecha)=date('now','-1 day','localtime'))  and doble='"+doble+"' and estado='A' and cierre='P'";
        Cursor cursor_aviarios = db.rawQuery(aviarios_abiertos, null);
        while (cursor_aviarios.moveToNext())
        {
            if(c==0){
                aviarios_pendientes=cursor_aviarios.getString(0);
            }
            else {
                aviarios_pendientes=aviarios_pendientes+","+cursor_aviarios.getString(0);

            }
            c++;
        }

        while (cursor.moveToNext())
        {
            lista_aviarios_ini.add(cursor.getString(0));
        }
        db.close();

            if(tipo==1){

                Recogidas.lbl_aviarios_abiertos.setText("AVIARIOS ABIERTOS: "+aviarios_pendientes);
            }
       }

    public static void llenar_aviario_parada_fin(Context context,int tipo,String doble){
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context,"bd_usuarios",null,1);

        SQLiteDatabase db=conn.getReadableDatabase();
        //String select_fin="select aviario from recogidas where area='"+contenedor_usuario.area+"' and date(fecha)=date('now','localtime')  and doble='NO' and estado='A' and aviario not in (select aviario from recogidas where date(fecha)=date('now','localtime') and  estado='F') order by aviario";
        String select_fin=" select aviario from recogidas where doble='"+doble+"' and area='"+contenedor_usuario.area+"' and estado2='P' " +
                "and ( date(hora_inicio)=date('now','localtime') or date(hora_inicio)=date('now','-1 day','localtime'))  order by aviario";
        Cursor cursor = db.rawQuery(select_fin, null);
        lista_aviarios_fin = new ArrayList<String>();
        String aviarios_pendientes="";
        int c=0;
        while (cursor.moveToNext())
        {
            if(c==0){
                aviarios_pendientes=cursor.getString(0);
            }
            else {
                aviarios_pendientes=aviarios_pendientes+","+cursor.getString(0);
                }
            lista_aviarios_fin.add(cursor.getString(0));
            c++;
        }
        db.close();
        if(tipo==1){
            Recogidas.lbl_aviarios_pendientes_cierre.setText("PARADAS PENDIENTES A FINALIZAR: "+aviarios_pendientes);
        }

    }


}
