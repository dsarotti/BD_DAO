import java.sql.ResultSet;
import java.sql.SQLException;

public class AnalisisBD {
    
    public static void rankServers(){
        ResultSet resultSet=null;
        final int NUM_ENTRADAS=5;
        try {
            resultSet = AccionesDAO.getNumeroPersonajesDeUsuarioPorServidor(NUM_ENTRADAS);
            
            int count = 0;
            while (resultSet.next() && count < NUM_ENTRADAS) {
                String nombreServidor = resultSet.getString("nombre_servidor");
                int numPersonajes = resultSet.getInt("num_personajes");
                System.out.println("El servidor " + nombreServidor + " con " + numPersonajes + " personajes.");
                count++;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los servidores con más personajes.");
            e.printStackTrace();
        }finally{
            if(resultSet!=null)
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el ResultSet");
                    e.printStackTrace();
                }
        }
    }

    public static void listServers(){
        try {
            ResultSet resultSet = AccionesDAO.getNumeroServidoresEnCadaRegion();

            String currentRegion = null;

            while (resultSet.next()) {
                String nombreRegion = resultSet.getString("nombre_region");

                if (!nombreRegion.equals(currentRegion)) {
                    // Nueva región, imprime el nombre de la región
                    System.out.println("Región " + nombreRegion);
                    currentRegion = nombreRegion;
                }

                // Imprime el nombre del servidor indentado
                System.out.println("----" + resultSet.getString("nombre_servidor"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de servidores por región.");
            e.printStackTrace();
        }
    }
}
