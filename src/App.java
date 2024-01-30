import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws Exception {

        // ConexionBD conexionBD= ConexionBD.getConexionBDInstance();
        
        // System.out.println(
        //     conexionBD.getConnection().isValid(200)?"Existe conexión!":"La conexion no está creada :("
        // );
        // System.out.println("Numero de personajes por usuario");
        // ResultSet personajesPorUsuariosResultSet = AccionesDAO.getPersonajesPorUsuarios();
        // printResultSet(personajesPorUsuariosResultSet);
        // personajesPorUsuariosResultSet.close();

        // System.out.println("Numero de personajes de un usuario");
        // ResultSet numeroDePersonajesDeUnUsuarioResultSet = AccionesDAO.getNumeroPersonajesDeUsuario(15);
        // printResultSet(numeroDePersonajesDeUnUsuarioResultSet);
        // numeroDePersonajesDeUnUsuarioResultSet.close();
        
        // System.out.println("personajes de un usuario");
        // ResultSet personajesDeUnUsuario = AccionesDAO.getPersonajesDeUnUsuario(1);
        // printResultSet(personajesDeUnUsuario);
        // personajesDeUnUsuario.close();

        // System.out.println("numero de servidores en una región");
        // ResultSet numeroServidoresEnRegion = AccionesDAO.getNumeroServidoresEnRegion("EUROPA");
        // printResultSet(numeroServidoresEnRegion);
        // numeroServidoresEnRegion.close();
    
        // System.out.println("numero de servidores en cada región");
        // ResultSet numeroServidoresEnCadaRegionStm = AccionesDAO.getNumeroServidoresEnCadaRegionStm();
        // printResultSet(numeroServidoresEnCadaRegionStm);
        // numeroServidoresEnCadaRegionStm.close();

        // System.out.println("zonas de un mapa");
        // ResultSet zonasDeUnMapa = AccionesDAO.getZonasDeUnMapa(3);
        // printResultSet(zonasDeUnMapa);
        // zonasDeUnMapa.close();

        // CreadorBD.crearTablas();
        // GeneradorBD.generarDatos();
        // conexionBD.cerrarConexion();

        AnalisisBD.rankServers();
        AnalisisBD.listServers();


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