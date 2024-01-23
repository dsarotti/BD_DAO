import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {
    private static String SERVER="localhost";
    private static String PUERTO="3306";
    private static String DATABASE="dantebd";
    private static String USER="dantebd";
    private static String PASSWORD="abc123.";
    private static ConexionBD conexionBDInstance;
    private static Connection conn;

    private ConexionBD(){
        abrirConexion();
    }

    private static ConexionBD getConexionBDInstance (){
        if (conexionBDInstance == null){
            conexionBDInstance = new ConexionBD();
        }
        return conexionBDInstance;
    }

    public static Connection getConnection(){
        try {
            if(conn.isClosed()||conn==null){
                getConexionBDInstance().abrirConexion();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private void abrirConexion(){
        Properties propiedades = new Properties();
        propiedades.put("user",USER);
        propiedades.put("password",PASSWORD);
        try {
            conn = DriverManager.getConnection("jdbc:mysql://"+SERVER+":"+PUERTO+"/"+DATABASE,propiedades);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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