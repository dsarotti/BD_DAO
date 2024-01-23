import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccionesDAO {

    private final Connection conn = ConexionBD.getConnection();

    public ResultSet getPersonajesPorUsuarios(){
        return select("SELECT U.nombre AS NombreUsuario, COUNT(P.id) AS NumeroPersonajes FROM Usuarios U LEFT JOIN Personajes P ON U.id = P.usuario_id GROUP BY U.id, U.nombre");
    }

    public ResultSet getNumeroPersonajesDeUsuario(int idUsuario){
        return select("SELECT U.nombre AS NombreUsuario, COUNT(P.id) AS NumeroPersonajes FROM Usuarios U LEFT JOIN Personajes P ON U.id = P.usuario_id WHERE U.nombre = '"+idUsuario+"' GROUP BY U.id, U.nombre");
    }



    private ResultSet select(String query){
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            conexion = ConexionBD.getConnection();
            statement = conexion.prepareStatement(query);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cierre de recursos
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultSet;
    }
}
