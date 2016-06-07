import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;


public class Gestio {
    private static final String user = "root";
    private static final String password = "terremoto11";
    private static final String dbClassName = "com.mysql.jdbc.Driver";
    private static final String CONNECTION =
            "jdbc:mysql://172.16.10.156/gestio";

    public static void menu() {
        //System.out.print("\033[H\033[2J");
        System.out.println("MENU \n" +
                "1.- VISUALITZAR CLIENT \n" +
                "2.- MÀXIM CODI CLIENT \n" +
                "3.- NOU CLIENT PERSONA \n" +
                "4.- ELIMINAR CLIENT PERSONA \n" +
                "5.- MODIFICACION CLIENT PERSONA \n"+
                "6.- PROVEIDOR \n"+
                "Q.- SORTIR \n" +
                "");
    }

    public static void main(String[] args)
            throws ClassNotFoundException, SQLException {
        Scanner userInput = new Scanner(System.in);
        String strChoice = "";
        while (!strChoice.equals("q") && !strChoice.equals("Q")) {
            menu();
            strChoice = userInput.next();
            switch (strChoice) {
                case "1":
                    System.out.print("\033[H\033[2J");
                    System.out.println("Introduir codi client");
                    String strCodi = userInput.next();
                    int codi = Integer.parseInt(strCodi);
                    visualitzaClient(codi);
                    break;
                case "2":
                    System.out.print("\033[H\033[2J");
                    int maxCodiClient = maxCodiClient();
                    System.out.println("MAX CODI CLIENT " + maxCodiClient);
                    System.out.print("\033[H\033[2J");
                    break;
                case "3":
                    nouClientPersona();
                    break;
                case "4":
                    System.out.print("\033[H\033[2J");
                    EliminarCliente();
                    System.out.print("\033[H\033[2J");
                    break;
                case "5":
                    System.out.print("\033[H\033[2J");
                    ModificarCliente();
                    System.out.print("\033[H\033[2J");
                    break;
                case "6":
                        Proveidor p = new Proveidor(user, password, dbClassName, CONNECTION);
                        MenuPoveidor m = new MenuPoveidor(p);
                        m.MenuPrincipal();
                    break;
            }
        }
    }


    private static Connection ConexionBBDD()throws ClassNotFoundException, SQLException{
        // creates a drivermanager class factory
        Class.forName(dbClassName);
        // Properties for user and password. Here the user and password are both 'paulr'
        Properties p = new Properties();
        p.put("user", user);
        p.put("password", password);
        // Now try to connect
        Connection c = DriverManager.getConnection(CONNECTION, p);
        return c;
    }

    private static void visualitzaClient(int id_client) throws ClassNotFoundException, SQLException {

        Connection c = ConexionBBDD();

        //suposam que el client és una PERSONA_CLI
        String sql = "select C.id_client,PC.nom,PC.llinatge1,PC.llinatges2 FROM CLIENT C inner join PERSONA_CLI PC on PC.id_client=C.id_client";

        PreparedStatement preparedStatement = c.prepareStatement(sql);
        // execute select SQL stetement
        ResultSet rs = preparedStatement.executeQuery();


            while (rs.next()) {
                String nom = rs.getString("nom");
                String llinatge1 = rs.getString("llinatge1");
                String llinatge2 = rs.getString("llinatges2");
                System.out.println("persona nom: " + nom + "\n" +
                        "llinatge1: " + llinatge1 + "\n" +
                        "llinatge2: " + llinatge2 + "\n");
            }

//        } else {
            //comprovam si el client és una empresa
//            sql = "SELECT C.id_client,nom,cif,telefon";
//            sql += " FROM gestio_d.EMPRESA_CLI EC INNER JOIN CLIENT C ";
//            sql += " on C.id_client=EC.id_client where C.id_client=?";
//            preparedStatement = c.prepareStatement(sql);
//            preparedStatement.setInt(1, id_client);
            // execute select SQL stetement
//            ResultSet rs2 = preparedStatement.executeQuery();
//            if (rs2.next()) {
//                String nom = rs2.getString("nom");
//                System.out.println("empresa nom: " + nom);
//            } else {
//                System.out.println("client no trobat");
//            }

        c.close();
    }

    private static void nouClientPersona() throws ClassNotFoundException, SQLException {
        File f = new File("../file/usuarios.txt");
        try {
            InputStream b = new FileInputStream(f);
            int caracter = 0;
            char caracterBueno;
            String palabra ="";
            List<String> valoresIntroducidos = new ArrayList<>();
            while ((caracter = b.read()) != -1){
                caracterBueno = (char)caracter;
               if(caracterBueno == ','){
                valoresIntroducidos.add(palabra);
                   palabra ="";
               }else if(caracterBueno == '\n'){
                    continue;
                }else{
                   palabra+= caracterBueno;
               }
            }
            System.out.println(valoresIntroducidos.toString());
            Connection c = ConexionBBDD();
            for (int i = 0; i < valoresIntroducidos.size(); i++) {
                int maxCodiClient = maxCodiClient()+1;
                String nom = valoresIntroducidos.get(i);
                i++;
                String llinatge1 = valoresIntroducidos.get(i);
                i++;
                String llinatges2 = valoresIntroducidos.get(i);
                i++;
                String nif = valoresIntroducidos.get(i);
                i++;
                String telefon = valoresIntroducidos.get(i);
                String sql = "insert into CLIENT (id_client) values (?)";
                PreparedStatement ps = c.prepareStatement(sql);
                ps.setInt(1, maxCodiClient);
                ps.execute();
                sql ="insert into PERSONA_CLI (id_client,nom,llinatge1,llinatges2,nif,telefon) values (?,?,?,?,?,?)";
                PreparedStatement ps2 = c.prepareStatement(sql);
                ps2.setInt(1,maxCodiClient);
                ps2.setString(2, nom);
                ps2.setString(3, llinatge1);
                ps2.setString(4, llinatges2);
                ps2.setString(5, nif);
                ps2.setString(6,telefon);
                ps2.execute();
            }

            // creates a drivermanager class factory

//            int codi = maxCodiClient() + 1;

//            ps.setInt(1,codi);
//            ps.execute();

//            Scanner userInput = new Scanner(System.in);
//            System.out.println("Introduir nom del client");
//            String nom = userInput.next();
//            System.out.println("Introduir llinatge1 del client");
//            String llinatge1 = userInput.next();
//            System.out.println("Introduir llinatges2 del client");
//            String llinatge2 = userInput.next();
//            System.out.println("Introduir nif del client");
//            String nif = userInput.next();
//            System.out.println("Introduir telèfon del client");
//            String telefon = userInput.next();


            c.close();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void EliminarCliente() throws ClassNotFoundException, SQLException {

        Connection c = ConexionBBDD();

        Scanner userInput = new Scanner(System.in);
        System.out.println("Introduir id client:");

        int codi = userInput.nextInt();
        String sql = "DELETE FROM PERSONA_CLI WHERE id_client = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, codi);
        ps.execute();

        sql = "DELETE FROM CLIENT WHERE id_client = ?";
        ps = c.prepareStatement(sql);
        ps.setInt(1, codi);
        ps.execute();
        c.close();

    }


    private static void ModificarCliente() throws ClassNotFoundException, SQLException {
        String palabra = "";
        Connection c = ConexionBBDD();
        Scanner userInput = new Scanner(System.in);
        System.out.println("Introduzca los campos que desea modificar: ");
        String texto = userInput.next();
        List<String> campos = new ArrayList<>();
        for (int i = 0; i < texto.length(); i++) {
            if(texto.charAt(i) != ','){
                palabra+= texto.charAt(i);
            }else{
                campos.add(palabra);
                palabra = "";
            }
        }
        System.out.println(campos.toString());
        palabra = "";
        System.out.println("Que valores desea introducir: ");
        String textocambios = userInput.next();
        List<String> valoresIntroducidos = new ArrayList<>();
        for (int i = 0; i < textocambios.length(); i++) {
            if(textocambios.charAt(i) != ','){
                palabra+= textocambios.charAt(i);
            }else{
                valoresIntroducidos.add(palabra);
                palabra = "";
            }
        }
        System.out.println(valoresIntroducidos.toString());
        palabra = "";
        System.out.println("Campos condicioanles?");
        String camposCon = userInput.next();
        List<String> camposCondicioanles = new ArrayList<>();
        for (int i = 0; i < camposCon.length(); i++) {
            if(camposCon.charAt(i) != ','){
                palabra+= camposCon.charAt(i);
            }else{
                camposCondicioanles.add(palabra);
                palabra = "";
            }
        }
        System.out.println(camposCondicioanles.toString());
        palabra = "";
        System.out.println("Valores condicioanles?");
        String valoresCon = userInput.next();
        List<String> valoresCondicioanles = new ArrayList<>();
        for (int i = 0; i < valoresCon.length(); i++) {
            if(valoresCon.charAt(i) != ','){
                palabra+= valoresCon.charAt(i);
            }else{
                valoresCondicioanles.add(palabra);
                palabra = "";
            }
        }
        System.out.println(valoresCondicioanles.toString());
        String sql = "UPDATE PERSONA_CLI SET ";
        for (int i = 0,x = 1; i < campos.size() ; i++,x++) {
            if(campos.size() == 1){
                sql += campos.get(i)+"="+valoresIntroducidos.get(i);
            }else{
                if(i == campos.size()-1){
                    sql += campos.get(i)+"="+valoresIntroducidos.get(i);
                }else {
                    sql += campos.get(i)+"="+ valoresIntroducidos.get(i)+", ";
                }
            }
        }
        sql += " WHERE ";
        for (int i = 0,x = 1; i < camposCondicioanles.size() ; i++,x++) {
            if(camposCondicioanles.size() == 1){
                sql += camposCondicioanles.get(i)+"="+valoresCondicioanles.get(i);
            }else{
                if(i == camposCondicioanles.size()-1){
                    sql += camposCondicioanles.get(i)+"="+valoresCondicioanles.get(i);
                }else {
                    sql += camposCondicioanles.get(i)+"="+valoresCondicioanles.get(i)+"&& ";
                }
            }
        }
        PreparedStatement ps = c.prepareStatement(sql);
        System.out.print("\033[H\033[2J");
        System.out.println(sql);
        ps.execute();

        c.close();
    }


    private static int maxCodiClient() throws ClassNotFoundException, SQLException {

        Connection c = ConexionBBDD();

        String sql = "select max(id_client) as max from CLIENT";
       PreparedStatement preparedStatement = c.prepareStatement(sql);
        // execute select SQL stetement
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int max = rs.getInt("max");
        c.close();
        return max;

    }
}
