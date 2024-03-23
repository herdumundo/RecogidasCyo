package entidades;

public class Recogidas {

    private String area;
    private String aviario;
    private String velocidad;
    private String velocidadMaq;
    private String responsable;
    private String tipo_recogida;
    private String observacion;
    private String responsable2;
    private String cierre;



    private String estado;
    private String fecha;
    private String idregistro;
    private String fecha_fin;
    private String hora_inicio;
    private String hora_fin;
    private String estado2;
    private String estado_sincro;
    private String id_sql;


    public Recogidas(String aviario,String area,String velocidad,String idregistro,String velocidadMaq,String responsable,
                     String tipo_recogida,String observacion,String responsable2,String estado,String fecha,String fecha_fin,
                     String hora_inicio,String hora_fin,String estado2,String estado_sincro, String id_sql,String cierre) {
        this.aviario = aviario;
        this.area = area;
        this.velocidad = velocidad;
        this.idregistro = idregistro;
        this.velocidadMaq = velocidadMaq;
        this.responsable = responsable;
        this.tipo_recogida = tipo_recogida;
        this.observacion = observacion;
        this.responsable2 = responsable2;
        this.estado = estado;
        this.fecha = fecha;
        this.fecha_fin = fecha_fin;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.estado2 = estado2;
        this.estado_sincro=estado_sincro;
        this.id_sql=id_sql;
        this.cierre=cierre;
    }

    public Recogidas(){

    }

    public String getAviario() {
        return aviario;
    }

    public void setAviario(String aviario) {
        this.aviario = aviario;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }

    public String getIdregistro() {
        return idregistro;
    }

    public void setIdregistro(String idregistro) {
        this.idregistro = idregistro;
    }

    public String getVelocidadMaq() {
        return velocidadMaq;
    }

    public void setVelocidadMaq(String velocidadMaq) {
        this.velocidadMaq = velocidadMaq;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getTipo_recogida() {
        return tipo_recogida;
    }

    public void setTipo_recogida(String tipo_recogida) {
        this.tipo_recogida = tipo_recogida;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getResponsable2() {
        return responsable2;
    }

    public void setResponsable2(String responsable2) {
        this.responsable2 = responsable2;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
    }

    public String getEstado2() {
        return estado2;
    }

    public void setEstado2(String estado2) {
        this.estado2 = estado2;
    }
    public String getEstado_sincro() {
        return estado_sincro;
    }

    public void setEstado_sincro(String estado_sincro) {
        this.estado_sincro = estado_sincro;
    }

    public String getId_sql() {
        return id_sql;
    }

    public void setId_sql(String id_sql) {
        this.id_sql = id_sql;
    }





    public String getCierre() {
        return cierre;
    }

    public void setCierre(String cierre) {
        this.cierre = cierre;
    }
}
