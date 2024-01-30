import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccionesDAO {
    private static final Connection conn = ConexionBD.getConexionBDInstance().getConnection();
    private static PreparedStatement numeroPersonajesPorCadaUsuarioStm = null;
    private static PreparedStatement numeroPersonajesDeUnUsuarioStm = null;
    private static PreparedStatement personajesDeUnUsuarioStm = null;
    private static PreparedStatement numeroPersonajesDeUsuarioPorServidorStm = null;
    private static PreparedStatement numeroServidoresEnRegionStm = null;
    private static PreparedStatement numeroServidoresEnCadaRegionStm = null;
    private static PreparedStatement zonasDeUnMapaStm = null;
    private static PreparedStatement servidoresPorRegionStm = null;

    private AccionesDAO() {
    };

    public static ResultSet getPersonajesPorUsuarios() {
        checkStatements();
        return select(numeroPersonajesPorCadaUsuarioStm);
    }

    public static ResultSet getNumeroPersonajesDeUsuario(int idUsuario) {
        checkStatements();
        try {
            numeroPersonajesDeUnUsuarioStm.setString(1, Integer.toString(idUsuario));
        } catch (SQLException e) {
            System.err.println("Error al preparar el statement para getNumeroPersonajesDeUsuario(" + idUsuario + ")");
            e.printStackTrace();
        }
        return select(numeroPersonajesDeUnUsuarioStm);
    }

    public static ResultSet getPersonajesDeUnUsuario(int idUsuario) {
        checkStatements();
        try {
            personajesDeUnUsuarioStm.setInt(1, idUsuario);
        } catch (SQLException e) {
            System.err.println("Error al preparar el statement para getPersonajesDeUnUsuario(" + idUsuario + ")");
            e.printStackTrace();
        }
        return select(personajesDeUnUsuarioStm);
    }

    public static ResultSet getNumeroPersonajesDeUsuarioPorServidor(int numero) {
        checkStatements();
        try {
            numeroPersonajesDeUsuarioPorServidorStm.setInt(1, numero);
        } catch (SQLException e) {
            System.err.println("Error al preparar el statement para getNumeroPersonajesDeUsuarioPorServidor()");
            e.printStackTrace();
        }

        return select(numeroPersonajesDeUsuarioPorServidorStm);
    }

    public static ResultSet getNumeroServidoresEnRegion(String region) {
        checkStatements();
        try {
            numeroServidoresEnRegionStm.setString(1, region);
        } catch (SQLException e) {
            System.err.println("Error al preparar el statement para getNumeroServidoresEnRegion()");
            e.printStackTrace();
        }

        return select(numeroServidoresEnRegionStm);
    }

    public static ResultSet getNumeroServidoresEnCadaRegion() {
        checkStatements();
        return select(numeroServidoresEnCadaRegionStm);
    }

    public static ResultSet getZonasDeUnMapa(int idMapa) {
        checkStatements();
        try {
            zonasDeUnMapaStm.setInt(1, idMapa);
        } catch (SQLException e) {
            System.err.println("Error al preparar el statement para getNumeroServidoresEnRegion()");
            e.printStackTrace();
        }

        return select(zonasDeUnMapaStm);
    }


    public static ResultSet getServidoresPorRegion(){
        
    }

    private static void checkStatements() {
        if (numeroPersonajesPorCadaUsuarioStm == null
                ||
                numeroPersonajesDeUnUsuarioStm == null
                ||
                personajesDeUnUsuarioStm == null
                ||
                numeroPersonajesDeUsuarioPorServidorStm == null
                ||
                numeroServidoresEnRegionStm == null
                ||
                numeroServidoresEnCadaRegionStm == null
                ||
                zonasDeUnMapaStm == null
                ||
                servidoresPorRegionStm == null) {
            initStatements();
        }
    }

    private static void initStatements() {
        try {
            numeroPersonajesPorCadaUsuarioStm = conn.prepareStatement(
                    "SELECT U.nombre AS NombreUsuario, COUNT(P.id) AS NumeroPersonajes FROM Usuarios U LEFT JOIN Personajes P ON U.id = P.usuario_id GROUP BY U.id, U.nombre");
            numeroPersonajesDeUnUsuarioStm = conn.prepareStatement(
                    "SELECT U.nombre , COUNT(P.id) AS NumeroPersonajes FROM Usuarios U LEFT JOIN Personajes P ON U.id = P.usuario_id WHERE U.id = ? GROUP BY U.id, U.nombre");
            personajesDeUnUsuarioStm = conn.prepareStatement(
                    "SELECT Usuarios.nombre AS nombre_usuario, Personajes.nombre AS nombre_personaje, Servidores.nombre AS nombre_servidor FROM Personajes JOIN Usuarios ON Personajes.usuario_id = Usuarios.id JOIN Servidores ON Personajes.servidor_id = Servidores.id WHERE Usuarios.id = ?");
            numeroPersonajesDeUsuarioPorServidorStm = conn.prepareStatement(
                    "SELECT Servidores.nombre AS nombre_servidor, COUNT(Personajes.id) AS num_personajes FROM Personajes JOIN Servidores ON Personajes.servidor_id = Servidores.id GROUP BY Servidores.nombre ORDER BY num_personajes DESC LIMIT ?");
            numeroServidoresEnRegionStm = conn.prepareStatement(
                    "SELECT Regiones.nombre AS nombre_region,  COUNT(Servidores.id) AS num_servidores FROM Regiones JOIN Servidores ON Regiones.id = Servidores.region_id WHERE Regiones.nombre = ? GROUP BY Regiones.nombre");
            numeroServidoresEnCadaRegionStm = conn.prepareStatement(
                    "SELECT Regiones.nombre AS nombre_region, COUNT(Servidores.id) AS num_servidores FROM Regiones LEFT JOIN Servidores ON Regiones.id = Servidores.region_id GROUP BY Regiones.nombre");
            zonasDeUnMapaStm = conn.prepareStatement("SELECT nombre, alto, ancho FROM Zonas WHERE mapa_id = ?");
            servidoresPorRegionStm = conn.prepareStatement("SELECT Regiones.nombre AS nombre_region, Servidores.nombre AS nombre_servidor FROM Servidores JOIN Regiones ON Servidores.region_id = Regiones.id ORDER BY Regiones.nombre, Servidores.nombre");

        } catch (SQLException e) {
            System.err.println("Error al inicializar los Statement");
            e.printStackTrace();
        }
    }

    /**
     * Cierra todos los statements abiertos, al cerrarlos también se cierran los ResultSet que tengan asociados.
     */
    public static void cerrarTodosStatements() {
        // Nota de la documentación de java.sql.Statement.close(): **Note:**When a
        // Statement object is closed, its current ResultSet object, if one exists, is
        // also closed.
        cerrarStatement(numeroPersonajesPorCadaUsuarioStm);
        cerrarStatement(personajesDeUnUsuarioStm);
        cerrarStatement(numeroServidoresEnRegionStm);
        cerrarStatement(numeroServidoresEnCadaRegionStm );
        cerrarStatement(numeroPersonajesDeUnUsuarioStm);
        cerrarStatement (numeroPersonajesDeUsuarioPorServidorStm );
        cerrarStatement(numeroPersonajesPorCadaUsuarioStm );
        cerrarStatement(zonasDeUnMapaStm );
    }

    private static void cerrarStatement(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar el Statement");
                e.printStackTrace();
            }
        }
    }

    private static ResultSet select(PreparedStatement statement) {
        ResultSet result = null;
        try {
            result = statement.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la select");
            e.printStackTrace();
        }
        return result;
    }

}