public class App {
    public static void main(String[] args) throws Exception {

        ConexionBD conexionBD= ConexionBD.getConexionBDInstance();
        
        System.out.println(
            conexionBD.getConnection().isValid(200)?"Existe conexión!":"La conexion no está creada :("
        );

        CreadorBD.crearTablas();
        GeneradorBD.generarDatos();
        conexionBD.cerrarConexion();
        
    }
}