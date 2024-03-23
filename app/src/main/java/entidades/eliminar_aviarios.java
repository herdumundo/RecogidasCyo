package entidades;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.recogidascyo_android.ConexionSQLiteHelper;

public class eliminar_aviarios {




    public static void eliminar_aviario_pendiente(Context context,Integer id)
    {

        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context,"bd_usuarios",null,1);
     try {
            SQLiteDatabase db_UPDATE=conn.getReadableDatabase();
            db_UPDATE.execSQL(" update recogidas set  estado ='E' ,estado_sincro ='E', estado2='E' where idregistro="+id+"");
            conn.close();


           Toast.makeText(context,"REGISTRO ELIMINADO CON EXITO.",Toast.LENGTH_LONG).show();

     }catch(Exception e)
        {
            Toast.makeText(context, e.toString() ,Toast.LENGTH_LONG).show();       }
    }



}
