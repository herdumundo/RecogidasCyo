package com.example.recogidascyo_android;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import entidades.eliminar_aviarios;
import entidades.Recogidas;

public class lista_recogidas extends AppCompatActivity {
    Button btn_buscar;
    ListView listViewreco;
    ArrayList<String> listaInformacion;
    ArrayList<Recogidas> listaRecogidas;
    TextView txt_fecha;
    DatePickerDialog picker;
    ConexionSQLiteHelper conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_recogidas);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        listViewreco= (ListView) findViewById(R.id.listViewReco);
        txt_fecha=(TextView)findViewById(R.id.txt_fecha);
        btn_buscar=(Button)findViewById(R.id.btn_buscar);

       // consultarListaregistro();
        txt_fecha.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(lista_recogidas.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                DecimalFormat df = new DecimalFormat("00");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

                                cldr.set(year, monthOfYear, dayOfMonth);
                                String strDate = format.format(cldr.getTime());
                                txt_fecha.setText(year + "-" + df.format((monthOfYear + 1))  + "-" +df.format((dayOfMonth)));



                            }
                        }, year, month, day);
                picker.show();


            }
        });

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 llenar_grilla();
            } });

        listViewreco.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int pos, long l) {
                final int id_registro =Integer.parseInt(listaRecogidas.get(pos).getIdregistro().toString());
                String  informacion="NRO: "+listaRecogidas.get(pos).getIdregistro()+"\n";
                informacion+="AREA: "+listaRecogidas.get(pos).getArea()+"\n";
                informacion+="AVIARIO: "+listaRecogidas.get(pos).getAviario()+"\n";
                informacion+="VELOCIDAD DE RECOGIDA: "+listaRecogidas.get(pos).getVelocidad()+"\n";
                informacion+="VELOCIDAD DE MAQUINA: "+listaRecogidas.get(pos).getVelocidadMaq()+"\n";
                informacion+="RESPONSABLE: "+listaRecogidas.get(pos).getResponsable2()+"\n";
                informacion+="TIPO DE RECOGIDA: "+listaRecogidas.get(pos).getTipo_recogida()+"\n";
                informacion+="OBSERVACION: "+listaRecogidas.get(pos).getObservacion()+"\n";
                informacion+="FECHA: "+listaRecogidas.get(pos).getFecha()+"\n";
                informacion+="ESTADO: "+listaRecogidas.get(pos).getEstado()+"\n";
                informacion+="HORA INICIO PARADA: "+listaRecogidas.get(pos).getHora_inicio()+"\n";
                informacion+="HORA FINALIZACION DE PARADA: "+listaRecogidas.get(pos).getHora_fin()+"\n";
                informacion+="ESTADO DE PARADA: "+listaRecogidas.get(pos).getEstado2()+"\n";
                informacion+="ESTADO DE ENVIO: "+listaRecogidas.get(pos).getEstado_sincro()+"\n";
                informacion+="ID SQL: "+listaRecogidas.get(pos).getId_sql()+"\n";
                informacion+="CIERRE: "+listaRecogidas.get(pos).getCierre()+"\n";

                AlertDialog.Builder builder = new AlertDialog.Builder(lista_recogidas.this);

                        if(listaRecogidas.get(pos).getEstado_sincro().toString().trim().equals("PENDIENTEDEENVIO")){
                            builder.setMessage(informacion)
                                    .setCancelable(false)
                                    .setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            AlertDialog.Builder builder_2 = new AlertDialog.Builder(lista_recogidas.this);

                                            builder_2.setMessage("ESTA SEGURO QUE DESEA ELIMINAR EL REGISTRO?")
                                                    .setCancelable(false)
                                                    .setPositiveButton("SI, ELIMINAR", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                            eliminar_aviarios.eliminar_aviario_pendiente(lista_recogidas.this,id_registro);
                                                            llenar_grilla();
                                                            dialog.cancel();
                                                        }
                                                    })
                                                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                                    AlertDialog alert_2 = builder_2.create();
                                                    alert_2.show();


                                        }
                                    })
                                    .setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                        }
                        else {
                            builder.setMessage(informacion)
                                    .setCancelable(false)

                                    .setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                        }

                AlertDialog alert = builder.create();
                alert.show();
               }
        });

    }

    private void llenar_grilla(){
        consultarListaregistro();
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_3, R.id.text1, listaInformacion) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.text1);
                TextView text3 = (TextView) view.findViewById(R.id.text3);
                TextView text4 = (TextView) view.findViewById(R.id.text4);
                String estado_registro="";
                String est_reg="";
                String est_par="";
                String estado_parada="";


                estado_registro=listaRecogidas.get(position).getEstado();
                estado_parada=listaRecogidas.get(position).getEstado2();

                if(estado_registro.equals("")){
                    if(estado_parada.equals("P")){
                        est_par="PARADA INICIADA";
                        est_reg="";
                        // view.setBackgroundColor(Color.BLUE);

                    }
                    else if(estado_parada.equals("H")){
                        est_par="PARADA FINALIZADA";
                        est_reg="";
                        view.setBackgroundColor(Color.BLUE);
                    }
                    else {
                        //  view.setBackgroundColor(Color.RED);
                        est_par="";
                    }

                }
                else if (estado_parada.equals("")) {
                    if(estado_registro.equals("A")){

                        est_reg="ABIERTO";
                        est_par="";             }
                    else  if(estado_registro.equals("F")){
                        est_reg="CERRADO";
                        est_par="";
                    }
                    else  if(estado_registro.equals("-")){
                        est_reg="CAMBIO DE VELOCIDAD";
                        est_par="";
                    }
                    else {

                        est_reg="";
                    }


                }
                text4.setText("AVIARIO: "+listaRecogidas.get(position).getAviario());

                text1.setText("ESTADO: "+est_reg+est_par);
                text3.setText("RESPONSABLE: "+listaRecogidas.get(position).getResponsable2());

                if(estado_parada.equals("P")){
                    view.setBackgroundColor(Color.LTGRAY);
                }
                else if(estado_parada.equals("H")){

                    view.setBackgroundColor(Color.RED);
                }
                else {
                    view.setBackgroundColor(Color.WHITE);
                }
                return view;
            }
        };
        listViewreco.setAdapter(adapter);
    }
    private void consultarListaregistro() {
        SQLiteDatabase db=conn.getReadableDatabase();

        Recogidas Recogidas=null;
        listaRecogidas=new ArrayList<Recogidas>();
        //select * from usuarios
        //Cursor cursor=db.rawQuery("SELECT a.cod_interno,b.desc_potrero FROM registro_cabecera a,potrero b where a.cab_id_potrero=b.id_potrero"  ,null);
        Cursor cursor=db.rawQuery("select area,aviario,velocidad,velocidadMaq,responsable,tipo_recogida,observacion," +
                "responsable2,estado,idregistro,fecha,hora_inicio,hora_fin,estado2,estado_sincro,id_sql,cierre " +
                "from recogidas where (date(fecha)='"+txt_fecha.getText().toString()+"' or date(hora_inicio)='"+txt_fecha.getText().toString()+"') " +
                "and    (estado in ('F','A','-') or (estado2 in ('H','P'))) " +
                "" +
                " order by aviario, idregistro"   ,null);
//                "responsable2,estado,DATE('NOW'),DATE(fecha) from recogidas"   ,null);

        while (cursor.moveToNext()){
            String envio="";
            String tipo_recogida="";

            if (cursor.getString(14).equals("P")||cursor.getString(14).equals("A")){
                envio="PENDIENTE DE ENVIO";
            }
            else {

                envio="ENVIADO";
            }
            if (cursor.getString(5).equals("M")){
                tipo_recogida="MECANIZADA";
            }
            else {

                tipo_recogida="MANUAL";
            }
            Recogidas=new Recogidas();
            Recogidas.setArea(cursor.getString(0));
            Recogidas.setAviario(cursor.getString(1));
            Recogidas.setVelocidad(cursor.getString(2));
            Recogidas.setVelocidadMaq(cursor.getString(3));
            Recogidas.setResponsable(cursor.getString(4));
            Recogidas.setTipo_recogida(tipo_recogida);
            Recogidas.setObservacion(cursor.getString(6));
            Recogidas.setResponsable2(cursor.getString(7));
            Recogidas.setEstado(cursor.getString(8));
            Recogidas.setIdregistro(cursor.getString(9));
            Recogidas.setFecha(cursor.getString(10));
            Recogidas.setHora_inicio(cursor.getString(11));
            Recogidas.setHora_fin(cursor.getString(12));
            Recogidas.setEstado2(cursor.getString(13));
            Recogidas.setEstado_sincro(envio);
            Recogidas.setId_sql(cursor.getString(15));
            Recogidas.setCierre(cursor.getString(16));
            listaRecogidas.add(Recogidas);
        }
        obtenerLista();
    }


    private void obtenerLista() {
        listaInformacion=new ArrayList<String>();
        String estado_registro="";
        String est_reg="";
        String est_par="";
        String estado_parada="";

        for (int i=0; i<listaRecogidas.size();i++){
            estado_registro=listaRecogidas.get(i).getEstado();
            estado_parada=listaRecogidas.get(i).getEstado2();

            if(estado_registro.equals("")){
                if(estado_parada.equals("P")){
                    est_par="PARADA INICIADA";
                    est_reg="";
                }
                else if(estado_parada.equals("H")){
                    est_par="PARADA FINALIZADA";
                    est_reg="";

                }
                else {

                    est_par="";
                }

            }
            else if (estado_parada.equals("")) {
                if(estado_registro.equals("A")){

                    est_reg="ABIERTO";
                    est_par="";             }
                 else  if(estado_registro.equals("F")){
                    est_reg="CERRADO";
                    est_par="";
                }
                else {

                    est_reg="";
                }
            }


            listaInformacion.add("AVIARIO:"+listaRecogidas.get(i).getAviario()+"-"+est_par+est_reg);


        }

    }

    @Override
    public void onBackPressed()
    {


        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        finish();
                        startActivity(i);

    }
}
