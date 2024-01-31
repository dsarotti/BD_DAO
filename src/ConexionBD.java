import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase para gestionar la conexión con una base de datos
 */
public class ConexionBD {
    /**
     * Datos de la conexión
     */
    private static String SERVER="localhost";
    private static String PUERTO="3306";
    private static String DATABASE="dantebd";
    private static String USER="dantebd";
    private static String PASSWORD="abc123.";
    private static ConexionBD conexionBDInstance;
    private Connection conn;

    /**
     * Constructor privado. Abre la conexión
     */
    private ConexionBD(){
        abrirConexion();
    }

    /**
     * Devuelve la instancia única de esta clase
     * @return
     */
    public static ConexionBD getConexionBDInstance (){
        if (conexionBDInstance == null){
            conexionBDInstance = new ConexionBD();
        }
        return conexionBDInstance;
    }

    /**
     * Devuelve el objeto Connection de esta clase, si no existe lo crea.
     * @return
     */
    public Connection getConnection(){
        try {
            if(conn.isClosed()||conn==null){
                abrirConexion();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }


    /**
     * Abre la conexión al servidor.
     */
    private void abrirConexion(){
        Properties propiedades = new Properties();
        propiedades.put("user",USER);
        propiedades.put("password",PASSWORD);
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://"+SERVER+":"+PUERTO+"/"+DATABASE,propiedades);
        } catch (SQLException e) {
            System.err.println("Error al abrir conexión");
            e.printStackTrace();
        }
    }

    /**
     * Cierra la conexión con el servidor.
     */
    public void cerrarConexion(){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}