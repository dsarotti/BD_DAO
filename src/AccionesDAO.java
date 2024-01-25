import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccionesDAO {
    private static final Connection conn = ConexionBD.getConexionBDInstance().getConnection();
    private static PreparedStatement personajesPorUsuariosStm = null;
    private static PreparedStatement numeroPersonajesDeUsuarioStm = null;
    private static PreparedStatement personajesDeUnUsuarioStm = null;
    private static PreparedStatement numeroPersonajesDeUsuarioPorServidorStm = null;
    private static PreparedStatement numeroServidoresEnRegionStm = null;
    private static PreparedStatement numeroServidoresEnCadaRegionStm = null;
    private static PreparedStatement zonasDeUnMapaStm = null;

    private AccionesDAO() {
    };

    public static ResultSet getPersonajesPorUsuarios() {
        checkStatements();
        return select(personajesPorUsuariosStm);
    }

    public static ResultSet getNumeroPersonajesDeUsuario(int idUsuario) {
        checkStatements();
        try {
            numeroPersonajesDeUsuarioStm.setString(1, Integer.toString(idUsuario));
        } catch (SQLException e) {
            System.err.println("Error al preparar el statement para getNumeroPersonajesDeUsuario(" + idUsuario + ")");
            e.printStackTrace();
        }
        return select(numeroPersonajesDeUsuarioStm);
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

    public static ResultSet getNumeroServidoresEnCadaRegionStm() {
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

        return select(numeroServidoresEnRegionStm);
    }

    private static void checkStatements() {
        if (personajesPorUsuariosStm == null
                ||
                numeroPersonajesDeUsuarioStm == null
                ||
                personajesDeUnUsuarioStm == null
                ||
                numeroPersonajesDeUsuarioPorServidorStm == null
                ||
                numeroServidoresEnRegionStm == null
                ||
                numeroServidoresEnCadaRegionStm == null
                ||
                zonasDeUnMapaStm == null) {
            initStatements();
        }
    }

    private static void initStatements() {
        try {
            personajesPorUsuariosStm = conn.prepareStatement(
                    "SELECT U.nombre AS NombreUsuario, COUNT(P.id) AS NumeroPersonajes FROM Usuarios U LEFT JOIN Personajes P ON U.id = P.usuario_id GROUP BY U.id, U.nombre");
            numeroPersonajesDeUsuarioStm = conn.prepareStatement(
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

        } catch (SQLException e) {
            System.err.println("Error al inicializar los Statement");
            e.printStackTrace();
        }
    }

    public static void cerrarStatements(){
        //Nota de la documentación de java.sql.Statement.close(): **Note:**When a Statement object is closed, its current ResultSet object, if one exists, is also closed.
            if(personajesPorUsuariosStm!=null){
                try {
                    personajesPorUsuariosStm.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el Statement personajesPorUsuariosStm");
                    e.printStackTrace();
                }
            }
            if(personajesDeUnUsuarioStm!=null){
                try {
                    personajesDeUnUsuarioStm.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el Statement personajesDeUnUsuarioStm");
                    e.printStackTrace();
                }
            }
            if(numeroServidoresEnRegionStm!=null){
                try {
                    numeroServidoresEnRegionStm.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el Statement numeroServidoresEnRegionStm");
                    e.printStackTrace();
                }
            }
            if(numeroServidoresEnCadaRegionStm!=null){
                try {
                    numeroServidoresEnCadaRegionStm.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el Statement numeroServidoresEnCadaRegionStm");
                    e.printStackTrace();
                }
            }
            if(numeroPersonajesDeUsuarioStm!=null){
                try {
                    numeroPersonajesDeUsuarioStm.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el Statement numeroPersonajesDeUsuarioStm");
                    e.printStackTrace();
                }
            }
            if(numeroPersonajesDeUsuarioPorServidorStm!=null){
                try {
                    numeroPersonajesDeUsuarioPorServidorStm.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el Statement numeroPersonajesDeUsuarioPorServidorStm");
                    e.printStackTrace();
                }
            }
            if(personajesPorUsuariosStm!=null){
                try {
                    personajesPorUsuariosStm.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el Statement personajesPorUsuariosStm");
                    e.printStackTrace();
                }
            }
            if(zonasDeUnMapaStm!=null){
                try {
                    zonasDeUnMapaStm.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el Statement zonasDeUnMapaStm");
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