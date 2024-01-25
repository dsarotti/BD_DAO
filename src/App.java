import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws Exception {

        ConexionBD conexionBD= ConexionBD.getConexionBDInstance();
        
        System.out.println(
            conexionBD.getConnection().isValid(200)?"Existe conexión!":"La conexion no está creada :("
        );
        // System.out.println("Numero de personajes por usuario");
        // ResultSet personajesPorUsuariosResultSet = AccionesDAO.getPersonajesPorUsuarios();
        // printResultSet(personajesPorUsuariosResultSet);

        // System.out.println("Numero de personajes de un usuario");
        // ResultSet numeroDePersonajesDeUnUsuarioResultSet = AccionesDAO.getNumeroPersonajesDeUsuario(15);
        // printResultSet(numeroDePersonajesDeUnUsuarioResultSet);
        // numeroDePersonajesDeUnUsuarioResultSet.close();
        
        System.out.println("personajes de un usuario");
        ResultSet personajesDeUnUsuario = AccionesDAO.getPersonajesDeUnUsuario(1);
        printResultSet(personajesDeUnUsuario);
        personajesDeUnUsuario.close();

        // CreadorBD.crearTablas();
        // GeneradorBD.generarDatos();
        // conexionBD.cerrarConexion();

    }

    public static void printResultSet(ResultSet resultSet) {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Imprimir nombres de columnas
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", metaData.getColumnName(i));
            }
            System.out.println();

            // Imprimir datos
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("%-20s", resultSet.getString(i));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar el ResultSet (opcional, dependiendo del caso)
            try {
                if (resultSet != null && !resultSet.isClosed()) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}