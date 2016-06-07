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




    public String[] ObtenerDatosLocalidadyAdreca() throws SQLException {
        String[] datos = new String[6];
        String sql = "SELECT a.descripcio as adrecaDescripcio,l.descripcio as locDescripcio,a.numero,a.porta,a.pis,a.codi_postal " +
                "FROM (ADRECA as a INNER JOIN LOCALITAT as l ON a.id_localitat = l.id_localitat) INNER JOIN PROVEIDOR as p on p.id_adreça = a.id_adreca";
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs2 = ps.executeQuery();
        rs2.next();
        datos[0] = rs2.getString("adrecaDescripcio");
        datos[1] = rs2.getString("locDescripcio");
        datos[2] = rs2.getString("numero");
        datos[3] = rs2.getString("porta");
        datos[4] = rs2.getString("pis");
        datos[5] = rs2.getString("codi_postal");
        return datos;
    }


    public int ObtenerIdLocalidad(String localidad) throws SQLException {
        //Obtenemos el idetificador de la localidad.

        String consultaidLocalida = "SELECT id_localitat FROM LOCALITAT WHERE descripcio=?";
        PreparedStatement ps2 = c.prepareStatement(consultaidLocalida);
        ps2.setString(1, localidad);
        ResultSet rs2 = ps2.executeQuery();
        rs2.next();
        return rs2.getInt("id_localitat");

    }

    public int ObtenerIdAdreca() throws SQLException {
        //Obtenemos los datos para insertar en la tabala ADRECA
        String ConsultaMaxAdreca = "SELECT MAX(id_adreca)+1 as adreca FROM ADRECA";
        PreparedStatement prs3 = c.prepareStatement(ConsultaMaxAdreca);
        ResultSet rs3 = prs3.executeQuery();
        rs3.next();
        return rs3.getInt("adreca");
    }

    public int ObtenerIdTipusAdreca() throws SQLException {
        //Obtenemos identificador del tipo de adreca
        String consutaTipoVia = "SELECT descripcio FROM TIPUS_ADRECA";
        PreparedStatement ps = c.prepareStatement(consutaTipoVia);
        ResultSet rs = ps.executeQuery();
        int i = 1;
        System.out.println("Selecciona un tipo de via: ");
        while (rs.next()) {
            String descripcio = rs.getString("descripcio");
            System.out.println(i + "." + descripcio);
            i++;
        }
        return userInput.nextInt();
    }

    public void EliminarProveedor(String cif) throws SQLException {
        String sql = "UPDATE PROVEIDOR SET activo='n' WHERE cif=?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, cif);
        ps.execute();
    }

    public int maxCodiProveidor() throws ClassNotFoundException, SQLException {
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

class MenuPoveidor{
    Scanner userInput = new Scanner(System.in);
    Proveidor p;

    public MenuPoveidor(Proveidor p) {
        this.p = p;
    }

    //Menu de Proveedores
    public void MenuPrincipal() throws SQLException, ClassNotFoundException {
        System.out.println("MENU PROVEIDOR \n" +
                "1.- INSERTAR PROVEIDOR \n" +
                "2.- MÀXIM CODI PROVEIDOR \n" +
                "3.- CONSULTAR PROVEIDOR \n" +
                "4.- ELIMINAR PROVEIDOR \n" +
                "5.- MODIFICACION CLIENT PERSONA \n" +
                "6.- ATRAS \n" +
                "");
                OptionMenuPrincipal(userInput.nextInt());
    }

    public void OptionMenuPrincipal(int opcion) throws SQLException, ClassNotFoundException {
        switch (opcion) {
            case 1:
                MenuInsertar();
                break;
            case 2:
                System.out.println("Codigo mas alto: " + p.maxCodiProveidor());
                MenuPrincipal();
                break;
            case 3:
                MenuConsulta();
                break;
            case 4:
                System.out.println("Indique el cif del proveedor que desea eliminar: ");
                p.EliminarProveedor(userInput.next());
                break;
            case 6:
                break;
            default:
                System.out.println("No existe la accion");
                break;
        }
    }

    public void MenuConsulta() throws SQLException, ClassNotFoundException {
        System.out.println("MENU PROVEIDOR/CONSULTAS \n" +
                "1.- CONSULTAR TODOS LOS REGISTROS(Solo saldran proveedores activos) \n" +
                "2.- CONSULTAR REGISTROS POR LOCALIDAD \n" +
                "3.- CONSULTAR REGISTROS POR OTROS CAMPOS(todos los campos menos localidad) \n" +
                "4.- ATRAS \n" +
                "");
        OptionMenuConsulta(userInput.nextInt());
    }

    public void OptionMenuConsulta(int opcion) throws SQLException, ClassNotFoundException {
        ConsultaProveedor cp = new ConsultaProveedor(p);
        switch (opcion) {
            case 1:
                cp.VisualitzarAll();
                break;
            case 2:
                cp.VisualizarPorLocalidad();
                break;
            case 3:
                CamposElegir();
                cp.VisualtizarFiltrado();
                break;
            case 4:
                MenuPrincipal();
                break;
            default:
                System.out.println("No existe la accion");
                MenuConsulta();
                break;
        }
    }

    public void MenuInsertar() throws SQLException, ClassNotFoundException {
        System.out.println("MENU PROVEIDOR/CONSULTAS \n" +
                "1.- CONSULTAR TODOS LOS REGISTROS(Solo saldran proveedores activos) \n" +
                "2.- ATRAS \n" +
                "");
        OptionMenuInsertar(userInput.nextInt());
    }

    public void OptionMenuInsertar(int opcion) throws SQLException, ClassNotFoundException {
       InsertarProveedor ip = new InsertarProveedor(p);
        switch (opcion) {
            case 1:
                ip.InsertarProveedor();
                break;
            case 2:
                MenuPrincipal();
                break;
            default:
                System.out.println("No existe la accion");
                MenuInsertar();
                break;
        }
    }

    public void CamposElegir(){
        System.out.println("MENU PROVEIDOR/CONSULTAS/CAMPOS A ELEGIR \n" +
                "------------------------------------------------------------------------- \n" +
                "nom \n" +
                "telefon\n" +
                "CIF \n" +
                "activo \n" +
                "");
    }
}

class ConsultaProveedor{
    Proveidor p;
    Scanner userInput = new Scanner(System.in);

    public ConsultaProveedor(Proveidor p) {
        this.p = p;
    }

    public void VisualitzarAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM PROVEIDOR WHERE activo='s'";
        PreparedStatement preparedStatement = p.c.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        ImprimirConuslta(rs);
    }

    public void VisualitzarProveidorComanda() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM PROVEIDOR WHERE activo='s'";
        PreparedStatement preparedStatement = p.c.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        ConsultaComanda(rs);
    }


    public void VisualtizarFiltrado() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM PROVEIDOR WHERE ";
        System.out.println("Introduce los campos separado por comas(ej: nom,activo, ): ");
        String[] campos =  userInput.nextLine().split(",");
        System.out.println("Introduce los valores separado por comas(ej: paco,s, ): ");
        String valores[] =  userInput.nextLine().split(",");
        for (int i = 0; i < campos.length; i++) {
            if (i < campos.length - 1) {
                sql += campos[i] + "=? && ";
            } else {
                sql += campos[i] + "=? ";
            }
        }
        PreparedStatement ps = p.c.prepareStatement(sql);
        for (int i = 0,x=1; i < valores.length; i++,x++) {
                ps.setString(x,valores[i]);
        }
        ResultSet rs = ps.executeQuery();
        ImprimirConuslta(rs);
    }

    public void VisualizarPorLocalidad() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM (PROVEIDOR as p INNER JOIN ADRECA as a ON p.id_adreça = a.id_adreca) INNER JOIN LOCALITAT as l ON a.id_localitat = l.id_localitat WHERE a.id_localitat=? ";
        PreparedStatement ps = p.c.prepareStatement(sql);
        System.out.println("Introduzca el nombre de la localidad");
        ps.setInt(1, p.ObtenerIdLocalidad(userInput.nextLine()));
        ResultSet rs = ps.executeQuery();
        ImprimirConuslta(rs);
    }


    public void ImprimirConuslta(ResultSet rs) throws SQLException, ClassNotFoundException {

        while (rs.next()) {
            int codi = rs.getInt("id_proveidor");
            String nom = rs.getString("nom");
            String telefon = rs.getString("telefon");
            String cif = rs.getString("CIF");
            //Obtener datos de localidad y de adreca
            String[] datos = p.ObtenerDatosLocalidadyAdreca();
            System.out.println(
                    "id_proveidor: " + codi + "\n" +
                            "nom: " + nom + "\n" +
                            "adreca: " + datos[0] + "\n" +
                            "localidad: " + datos[1] + "\n" +
                            "numero: " + datos[2] + "\n" +
                            "porta: " + datos[3] + "\n" +
                            "pis: " + datos[4] + "\n" +
                            "codiPostal: " + datos[5] + "\n" +
                            "telefon: " + telefon + "\n" +
                            "CiF: " + cif + "\n"
            );
        }
        MenuPoveidor m = new MenuPoveidor(p);
        m.MenuConsulta();
    }

    public void ConsultaComanda(ResultSet rs) throws SQLException, ClassNotFoundException {
        while (rs.next()) {
            int codi = rs.getInt("id_proveidor");
            String nom = rs.getString("nom");
            String telefon = rs.getString("telefon");
            String cif = rs.getString("CIF");
            //Obtener datos de localidad y de adreca
            String[] datos = p.ObtenerDatosLocalidadyAdreca();
            System.out.println(
                    "id_proveidor: " + codi + "\n" +
                            "nom: " + nom
            );
        }
    }

}

class InsertarProveedor{
    Proveidor p;
    Scanner userInput = new Scanner(System.in);


    public InsertarProveedor(Proveidor p) {
        this.p = p;
    }

    public void InsertarProveedor() throws SQLException {
        //Insert que ira dntro de la tabla Proveedor
        System.out.println("Introduce el nombre del proveedor");
        String nomProveedor = userInput.nextLine();
        System.out.println("Introduce el numero de telefono");
        String telefono = userInput.next();
        System.out.println("Introduce el cif");
        String cif = userInput.next();

        int tipoVia = p.ObtenerIdTipusAdreca();
        int id_adreca = p.ObtenerIdAdreca();

        userInput.nextLine();
        System.out.println("Introduce el nombre de la calle: ");
        String descripcion = userInput.nextLine();
        System.out.println("Introduce el numero del portal: ");
        String numerPortal = userInput.next();
        System.out.println("Introduce la letra del portal si tiene: ");
        String letraPortal = userInput.next();
        System.out.println("Introduce el piso con su letra: ");
        String pisoYletra = userInput.next();
        System.out.println("Introduce el codigo postal: ");
        String codigoPostal = userInput.next();
        System.out.println("Introduce la localidad: ");
        userInput.nextLine();
        String localidad = userInput.nextLine();
        int codiLoc = p.ObtenerIdLocalidad(localidad.toUpperCase());

        InsertAdr(id_adreca, tipoVia, codiLoc, descripcion, numerPortal, letraPortal, pisoYletra, codigoPostal);
        InsertPro(nomProveedor, id_adreca, telefono, cif);

    }


    private void InsertAdr(int id_adreca, int tipoVia, int codiLoc, String descripcion, String numerPortal,
                           String letraPortal, String pisoYletra, String codigoPostal) {
        try {
            //Hacemos el insert dentro de ADRECA.
            String InsertAdreca = "INSERT INTO ADRECA VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement inAdreca = p.c.prepareStatement(InsertAdreca);
            inAdreca.setInt(1, id_adreca);
            inAdreca.setInt(2, tipoVia);
            inAdreca.setInt(3, codiLoc);
            inAdreca.setString(4, descripcion);
            inAdreca.setString(5, numerPortal);
            inAdreca.setString(6, letraPortal);
            inAdreca.setString(7, pisoYletra);
            inAdreca.setString(8, codigoPostal);
            inAdreca.execute();
        } catch (Exception e) {
            System.out.println("ha haabido un error");
        }
    }

    private void InsertPro(String nomProveedor, int id_adreca, String telefono, String cif) {
        //Hacemos el insert dentro de ADRECA.
        try {
            String InsertProveedor = "INSERT INTO PROVEIDOR (nom,id_adreça,telefon,CIF,activo) VALUES(?,?,?,?,?)";
            PreparedStatement inProveidor = p.c.prepareStatement(InsertProveedor);
            inProveidor.setString(1, nomProveedor);
            inProveidor.setInt(2, id_adreca);
            inProveidor.setString(3, telefono);
            inProveidor.setString(4, cif);
            inProveidor.setString(5, "s");
            inProveidor.execute();
        } catch (Exception e) {

        }
    }
}
