import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta clase contiene métodos útiles para visualizar la información de la base de datos.
 */
public class AnalisisBD {
    
    /**
     * Muestra por pantalla los 5 servidores con más personajes de forma "El servidor X con Y personajes"
     */
    public static void rankServers(){
        ResultSet resultSet=null;
        final int NUM_ENTRADAS=5;
        try {
            resultSet = AccionesDAO.getServidoresConMasPersonajes(NUM_ENTRADAS);
            
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

    /**
     * Muestra el nombre de los servidores por región
     */
    public static void listServers(){
        ResultSet resultSet = null;
        try {
            String currentRegion = null;
            resultSet=AccionesDAO.getServidoresPorRegion();
            while (resultSet.next()) {
                String nombreRegion = resultSet.getString("nombre_region");
                String nombreServidor = resultSet.getString("nombre_servidor");

                if (!nombreRegion.equals(currentRegion)) {
                    // Nueva región, imprime el nombre de la región
                    System.out.println("Región " + nombreRegion);
                    currentRegion = nombreRegion;
                }
                // Imprime el nombre del servidor bajo la región actual con indentación de 4 espacios
                System.out.print(nombreServidor.indent(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * muestra por pantalla el número de personajes de un usuario en concreto, el número de personajes por servidor y sus nombres
     * @param idUsuario id del usuario a mostrar
     */
    public static void getUserPJ(int idUsuario) {
    
        // Obtener el número total de personajes del usuario
        ResultSet totalPersonajesUsuario = AccionesDAO.getNumeroPersonajesDeUsuario(idUsuario);
        try {
            if (totalPersonajesUsuario.next()) {
                int totalPersonajes = totalPersonajesUsuario.getInt("NumeroPersonajes");
                System.out.println("Usuario " + idUsuario + " (" + totalPersonajes + " personajes)");
    
                // Utilizar un Map para agrupar personajes por servidor
                Map<String, List<String>> personajesPorServidor = new HashMap<>();
    
                // Obtener el detalle de personajes por servidor del usuario
                ResultSet personajesPorServidorUsuario = AccionesDAO.getPersonajesDeUnUsuario(idUsuario);
                while (personajesPorServidorUsuario.next()) {
                    String nombreServidor = personajesPorServidorUsuario.getString("nombre_servidor");
                    String nombrePersonaje = personajesPorServidorUsuario.getString("nombre_personaje");
    
                    // Agregar el personaje a la lista correspondiente al servidor
                    // computeIfAbsent comprueba si ya existe la clave 'nombreServidor'. 
                    // Si ya existe devuelve la lista asociada y si no existe crea la entrada
                    // correspondiente en el mapa con una nueva lista y la devuelve.
                    // después se añade el personaje a dicha lista.
                    personajesPorServidor.computeIfAbsent(nombreServidor, k -> new ArrayList<>()).add(nombrePersonaje);
                }
    
                // Mostrar la información del mapa servidor a servidor.
                for (Map.Entry<String, List<String>> entry : personajesPorServidor.entrySet()) {
                    String nombreServidor = entry.getKey();
                    List<String> personajes = entry.getValue();

                    System.out.print(nombreServidor.indent(2));

                    // Mostrar los nombres de los personajes para el servidor actual
                    for (String nombrePersonaje : personajes) {
                        System.out.print(nombrePersonaje.indent(4));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra por pantalla todos los usuarios y el número de personajes que tienen. Muestra 5 por línea con el número entre paréntesis.
     */
    public static void userPJs() {

        // Obtener la información de usuarios y el número de personajes que tienen
        ResultSet usuariosConPersonajes = AccionesDAO.getPersonajesPorUsuarios();

        try {
            int count = 0;

            while (usuariosConPersonajes.next()) {
                String nombreUsuario = usuariosConPersonajes.getString("NombreUsuario");
                int numPersonajes = usuariosConPersonajes.getInt("NumeroPersonajes");

                System.out.print(nombreUsuario + " (" + numPersonajes + ")  ");

                count++;

                // Imprimir 5 usuarios por línea
                if (count == 5) {
                    System.out.println();
                    count = 0;
                }
            }

            // Imprimir un salto de línea si no hay un múltiplo de 5 usuarios
            if (count > 0) {
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el área de un mapa con un id en concreto. 
     * @param idMapa id del mapa a mostrar
     */
    public static void areaMap(int idMapa) {
        try (ResultSet resultSet = AccionesDAO.getZonasDeUnMapa(idMapa)) {
            int areaTotal = 0;

            while (resultSet.next()) {
                int ancho = resultSet.getInt("ancho");
                int alto = resultSet.getInt("alto");
                areaTotal += ancho * alto;
            }

            System.out.println("Área total del mapa con ID " + idMapa + ": " + areaTotal);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
