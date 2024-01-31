import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Clase que muestra un menú básico por la salida estándar para visualizar el estado de la base de datos
 */
public class App {
    public static void main(String[] args) throws Exception {
        ConexionBD conexionBD= ConexionBD.getConexionBDInstance();
        Scanner scanner = new Scanner(System.in);
        
        // Si la base de datos está vacía, puede crear las tablas e introducir algunos datos nuevos con los siguientes métodos:
        // CreadorBD.crearTablas();
        // GeneradorBD.generarDatos();

        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Rank Servers");
            System.out.println("2. List Servers");
            System.out.println("3. Get User PJ");
            System.out.println("4. User PJs");
            System.out.println("5. Area Map");
            System.out.println("6. Salir");

            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    AnalisisBD.rankServers();
                    break;
                case 2:
                    AnalisisBD.listServers();
                    break;
                case 3:
                    System.out.print("Ingrese el ID del usuario: ");
                    int idUsuario = scanner.nextInt();
                    AnalisisBD.getUserPJ(idUsuario);
                    break;
                case 4:
                    AnalisisBD.userPJs();
                    break;
                case 5:
                    System.out.print("Ingrese el ID del mapa: ");
                    int idMapa = scanner.nextInt();
                    AnalisisBD.areaMap(idMapa);
                    break;
                case 6:
                    System.out.println("Saliendo del programa. ¡Hasta luego!");
                    AccionesDAO.cerrarTodosStatements();
                    conexionBD.cerrarConexion();
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
            }
        }

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