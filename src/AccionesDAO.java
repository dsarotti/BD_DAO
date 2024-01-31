import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase con métodos de acceso a la base de datos
 */
public class AccionesDAO {

    /**
     * Statements para inicializar y resutilizar si fuera necesario
     */
    private static PreparedStatement numeroPersonajesPorCadaUsuarioStm = null,
        numeroPersonajesDeUnUsuarioStm = null,
        personajesDeUnUsuarioStm = null,
        servidoresConMasPersonajes = null,
        numeroServidoresEnRegionStm = null,
        numeroServidoresEnCadaRegionStm = null,
        zonasDeUnMapaStm = null,
        servidoresPorRegionStm = null,
        numeroPersonajesPorUsuarioPorServidor;

    /**
     * Constructor privado
     */
    private AccionesDAO() {};

    /**
     * Devuelve el número de personajes por usuario, devolviendo el nombre de usuario y número de personajes siempre que el número sea igual o mayor que 1.
     * @return El ResultSet correspondiente.
     */
    public static ResultSet getPersonajesPorUsuarios() {
        checkStatements();
        return select(numeroPersonajesPorCadaUsuarioStm);
    }

    /**
     * El número de personajes de un usuario X, devolviendo su nombre, y número.
     * @param idUsuario id del usuario a consultar.
     * @return El ResultSet correspondiente.
     */
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

    /**
     * Los personajes de un usuario X, devolviendo el nombre del usuario, de cada personaje y el servidor en el que están.
     * @param idUsuario id del usuario a consultar
     * @return El ResultSet correspondiente.
     */
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

    /**
     * El número de personajes de cada usuario en cada servidor devolviendo el nombre de usuario, 
     * número de personajes y nombre del servidor.
     * @return El ResultSet correspondiente.
     */
    public static ResultSet getNumeroPersonajesPorUsuarioPorServidor(){
        checkStatements();
        return select(personajesDeUnUsuarioStm);
    }

    /**
     * Los X servidores con más personajes ordenados de mayor a menor, devolviendo el nombre y el número.
     * @param numero la cantidad de personajes a consultar por cada usuario.
     * @return El ResultSet correspondiente.
     */
    public static ResultSet getServidoresConMasPersonajes(int numero) {
        checkStatements();
        try {
            servidoresConMasPersonajes.setInt(1, numero);
        } catch (SQLException e) {
            System.err.println("Error al preparar el statement para getServidoresConMasPersonajes()");
            e.printStackTrace();
        }
        return select(servidoresConMasPersonajes);
    }

    /**
     * El número de servidores de X región.     
     * @param region El nombre de la región a consultar
     * @return El ResultSet correspondiente.
     */
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

    /**
     * El número de servidores de cada región.
     * @return El ResultSet correspondiente
     */
    public static ResultSet getNumeroServidoresEnCadaRegion() {
        checkStatements();
        return select(numeroServidoresEnCadaRegionStm);
    }

    /**
     * Las zonas de un mapa, su nombre y sus dimensiones.
     * @param idMapa id del mapa a consultar.
     * @return El ResultSet correspondiente
     */
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

    /**
     * Los servidores en cada región.
     * @return El ResultSet correspondiente
     */
    public static ResultSet getServidoresPorRegion(){
        checkStatements();
        return select(servidoresPorRegionStm);
    }

    /**
     * Comprueba si los Statements están inicializados, en caso contrario los inicializa.
     */
    private static void checkStatements() {
        if (numeroPersonajesPorCadaUsuarioStm == null
                ||
                numeroPersonajesDeUnUsuarioStm == null
                ||
                personajesDeUnUsuarioStm == null
                ||
                servidoresConMasPersonajes == null
                ||
                numeroServidoresEnRegionStm == null
                ||
                numeroServidoresEnCadaRegionStm == null
                ||
                zonasDeUnMapaStm == null
                ||
                servidoresPorRegionStm == null
                ||
                numeroPersonajesPorUsuarioPorServidor==null) {
            initStatements();
        }
    }

    /**
     * Inicializa los Statements.
     */
    private static void initStatements() {
        Connection conn = ConexionBD.getConexionBDInstance().getConnection();
        try {
            numeroPersonajesPorCadaUsuarioStm = conn.prepareStatement(
                    "SELECT U.nombre AS NombreUsuario, COUNT(P.id) AS NumeroPersonajes FROM Usuarios U LEFT JOIN Personajes P ON U.id = P.usuario_id GROUP BY U.id, U.nombre");
            numeroPersonajesDeUnUsuarioStm = conn.prepareStatement(
                    "SELECT U.nombre , COUNT(P.id) AS NumeroPersonajes FROM Usuarios U LEFT JOIN Personajes P ON U.id = P.usuario_id WHERE U.id = ? GROUP BY U.id, U.nombre");
            personajesDeUnUsuarioStm = conn.prepareStatement(
                    "SELECT Usuarios.nombre AS nombre_usuario, Personajes.nombre AS nombre_personaje, Servidores.nombre AS nombre_servidor FROM Personajes JOIN Usuarios ON Personajes.usuario_id = Usuarios.id JOIN Servidores ON Personajes.servidor_id = Servidores.id WHERE Usuarios.id = ?");
            servidoresConMasPersonajes = conn.prepareStatement(
                    "SELECT Servidores.nombre AS nombre_servidor, COUNT(Personajes.id) AS num_personajes FROM Personajes JOIN Servidores ON Personajes.servidor_id = Servidores.id GROUP BY Servidores.id ORDER BY num_personajes DESC LIMIT ?");
            numeroServidoresEnRegionStm = conn.prepareStatement(
                    "SELECT Regiones.nombre AS nombre_region,  COUNT(Servidores.id) AS num_servidores FROM Regiones JOIN Servidores ON Regiones.id = Servidores.region_id WHERE Regiones.nombre = ? GROUP BY Regiones.nombre");
            numeroServidoresEnCadaRegionStm = conn.prepareStatement(
                    "SELECT Regiones.nombre AS nombre_region, COUNT(Servidores.id) AS num_servidores FROM Regiones LEFT JOIN Servidores ON Regiones.id = Servidores.region_id GROUP BY Regiones.nombre");
            zonasDeUnMapaStm = conn.prepareStatement("SELECT nombre, alto, ancho FROM Zonas WHERE mapa_id = ?");
            servidoresPorRegionStm = conn.prepareStatement("SELECT Regiones.nombre AS nombre_region, Servidores.nombre AS nombre_servidor FROM Servidores JOIN Regiones ON Servidores.region_id = Regiones.id ORDER BY Regiones.nombre, Servidores.nombre");
            numeroPersonajesPorUsuarioPorServidor = conn.prepareStatement("SELECT Usuarios.nombre AS nombre_usuario, Servidores.nombre AS nombre_servidor, COUNT(Personajes.id) AS num_personajes FROM Personajes JOIN Usuarios ON Personajes.usuario_id = Usuarios.id JOIN Servidores ON Personajes.servidor_id = Servidores.id GROUP BY Usuarios.nombre, Servidores.nombre ORDER BY `num_personajes` DESC");
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
        cerrarStatement (servidoresConMasPersonajes );
        cerrarStatement(numeroPersonajesPorCadaUsuarioStm );
        cerrarStatement(zonasDeUnMapaStm );
    }

    /**
     * Cierra un Statement
     * @param statement
     */
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

    /**
     * Realiza la consulta Preparada en el Statement recibido por parámetro
     * @param statement El PreparedStatement a ejecutar.
     * @return El ResultSet correspondiente.
     */
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