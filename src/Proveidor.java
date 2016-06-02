import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by fjcambilr on 26/05/16.
 */
public class Proveidor {
    private String user;
    private String password;
    private String dbClassName;
    private String conection;
    public Connection c;
    Scanner userInput = new Scanner(System.in);
    public Proveidor(String user, String password, String dbClassName, String conection) throws SQLException, ClassNotFoundException {
        this.user = user;
        this.password = password;
        this.dbClassName = dbClassName;
        this.conection = conection;
        c = ConexionBBDD();
    }

    private Connection ConexionBBDD() throws ClassNotFoundException, SQLException {
        // creates a drivermanager class factory
        Class.forName(dbClassName);
        // Properties for user and password. Here the user and password are both 'paulr'
        Properties p = new Properties();
        p.put("user", user);
        p.put("password", password);
        // Now try to connect
        Connection c = DriverManager.getConnection(conection, p);
        return c;
    }

    public void Menu() {
        System.out.println("MENU PROVEIDOR \n" +
                "1.- INSERTAR PROVEIDOR \n" +
                "2.- MÀXIM CODI PROVEIDOR \n" +
                "3.- CONSULTAR PROVEIDOR \n" +
                "4.- ELIMINAR PROVEIDOR \n" +
                "5.- MODIFICACION CLIENT PERSONA \n" +
                "6.- ATRAS \n" +
                "");
    }

    public void Option(int opcion) throws SQLException, ClassNotFoundException {
        Scanner s = new Scanner(System.in);
        switch (opcion) {
            case 1:
                Insertar();
                break;
            case 2:
                System.out.println("Codigo mas alto: " + maxCodiProveidor());
                break;
            case 3:
                VisualizarProveedor();
                break;
            case 6:
                break;
            default:
                System.out.println("No existe la accion");
                break;
        }
    }

    public void VisualizarProveedor() throws SQLException, ClassNotFoundException {
        //suposam que el client és una PERSONA_CLI
        String sql = "";
        System.out.println("");
        System.out.println("Desea filtrar la consulta(y or n): ");
        switch (userInput.next()){
            case "y":
                sql = VisualtizarFiltrado();
                break;
            case "n":
                sql = VisualitzarAll();
                break;
        }
        PreparedStatement preparedStatement = c.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            int codi = rs.getInt("id_proveidor");
            String nom = rs.getString("nom");
            int direccion = rs.getInt("id_adreça");
            String telefon = rs.getString("telefon");
            String cif = rs.getString("CIF");
            System.out.println(
                    "id_proveidor: " +codi+ "\n" +
                    "nom:          " +nom+ "\n"+
                    "direccion: " +direccion+ "\n" +
                    "id_adreça: "+direccion+"\n"+
                    "telefon: " +telefon+ "\n" +
                            "CiF: " +cif+ "\n"

            );
        }

    }

    private String  VisualitzarAll() throws SQLException {
        String sql = "SELECT * FROM PROVEIDOR ";
        PreparedStatement preparedStatement = c.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        return sql;
    }

    private String VisualtizarFiltrado(){
        String sql = "SELECT * FROM PROVEIDOR WHERE ";
        System.out.println("Introduce los campos separado por espacios");
        String[] campos = userInput.nextLine().split(" +");
        System.out.println();
        for (int i = 0; i < campos.length; i++) {

        }
    }




    public void Insertar() {

    }

    private int maxCodiProveidor() throws ClassNotFoundException, SQLException {
        String sql = "select max(id_proveidor) as max from PROVEIDOR";
        PreparedStatement preparedStatement = c.prepareStatement(sql);
        // execute select SQL stetement
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int max = rs.getInt("max");
        c.close();
        return max;

    }


}
