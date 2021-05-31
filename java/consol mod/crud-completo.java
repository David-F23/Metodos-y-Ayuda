//Crud compeleto en modo consola
//Conexion y metodos
public class ConexionCRUD {
    
    private final String servidor = "jdbc:mysql://127.0.0.1:3306/bd_recurso_humano";
    
    private final String usuario = "root";
    
    private final String clave = "";
    
    private final String DriverConector = "com.mysql.jdbc.Driver";
    
    private static Connection conexion;
    
    
    public ConexionCRUD(){
        
        try{//Aquí se establece la conexion
            Class.forName(DriverConector);
            conexion = DriverManager.getConnection(servidor, usuario, clave);
            
        }catch(ClassNotFoundException | SQLException e){
            
            System.out.println("Coneción Fallida! Error!" + e.getMessage());
        }
    }
    
    //metodo para conectarse
    public Connection getConnection(){
        return conexion;
    }
    
    //metodo para insertar registros
    public void guardarRegistros(String tabla, String camposTabla, String valoresCampos){
        
        ConexionCRUD conectar = new ConexionCRUD();
        Connection cone = conectar.getConnection();
        
        try{
            String sqlQueryStmt = "INSERT INTO " + tabla + " ("+ camposTabla +") VALUES (" + valoresCampos + ");";
            
            Statement stmt;
            stmt = cone.createStatement();
            stmt.executeUpdate(sqlQueryStmt);
            
            stmt.close();
            cone.close();
            System.out.println("Registro Guardado Correctamente!");
            
        }catch(Exception e){
            
            System.out.println(e.getMessage());
        }
    }
    
    //metodo para catualizar o elimnar
    public void actualizarEliminarRegistro(String tabla, String valoresCamposNuevos, String condicion){
        
        ConexionCRUD conectar = new ConexionCRUD();
        Connection cone = conectar.getConnection();
        
        try{
            
            Statement stmt;
            String sqlQueryStmt;
            
            if(valoresCamposNuevos.isEmpty()){
                
                sqlQueryStmt = "DELETE FROM " + tabla + " WHERE " + condicion + ";";
                
            }else{
                
                sqlQueryStmt = "UPDATE " + tabla + " SET " + valoresCamposNuevos + " WHERE " + condicion + ";";
                
            }
            
            stmt = cone.createStatement();
            stmt.executeUpdate(sqlQueryStmt);
            stmt.close();
            cone.close();
            
        }catch(SQLException ex){
            
            System.out.println("Ha ocurrido el siguiente error: " + ex.getMessage());
        }
    }
    
    //Metodo para mostrar
    public void desplegarRegistros(String tablaBuscar, String campoBuscar, String condicionBuscar) throws SQLException{
        
        ConexionCRUD conectar = new ConexionCRUD();
        Connection cone = conectar.getConnection();
        
        try{
            
            Statement stmt;
            String sqlQueryStmt;
            
            if(condicionBuscar.equals("")){
                
                sqlQueryStmt = "SELECT " + campoBuscar + " FROM " + tablaBuscar + ";";
                
            }else{
                
                sqlQueryStmt = "SELECT " + campoBuscar + " FROM " + tablaBuscar + " WHERE " + condicionBuscar;
                
            }
            
            stmt = cone.createStatement();
            stmt.executeQuery(sqlQueryStmt);
            
            try(ResultSet miResultSet = stmt.executeQuery(sqlQueryStmt)){
                
                if(miResultSet.next()){
                    
                    ResultSetMetaData metaData = miResultSet.getMetaData();
                    int numColumnas = metaData.getColumnCount();
                    System.out.print("<< REGISTROS ALMACENADOS >>");
                    System.out.println();
                    
                    for(int i = 1; i <= numColumnas; i++){
                        
                        System.out.printf("%-20s\t", metaData.getColumnName(i));
                    }
                    System.out.println();
                    
                    do{
                        for(int i = 1; i <= numColumnas; i++){
                            
                            System.out.printf("%-20s\t", miResultSet.getObject(i));
                        }
                        System.out.println();
                    }while(miResultSet.next());
                    System.out.println();
                }else{
                    
                    System.out.println("No se han encontrado Registros");
                }
                
                miResultSet.close();
                
            }finally{
                
                stmt.close();
                cone.close();
            }
            
        }catch(SQLException ex){
            
            System.out.println("Ha ocurrido el siguiente error: " + ex.getMessage());
        }
    }
}

//=================================================================================================//

//Clases a ejecutar

//-1-menu
public class MenuPrincipal {
    
    public static void main(String[] args) throws SQLException {
        
        desplegarMenu();
    }
    
    public static void desplegarMenu() throws SQLException {
        
        Scanner leer = new Scanner(System.in);
        
        String opcionMenu = "";
        
        System.out.println("|=================================|");
        System.out.println("|     CRUD DE JAVA EN CONSOLA     |");
        System.out.println("|=================================|");
        System.out.println("| Opciones:                       |");
        System.out.println("|      1. Crear registros         |");
        System.out.println("|      2. Consultar registros     |");
        System.out.println("|      3. Actualizar registros    |");
        System.out.println("|      4. Eliminar registros      |");
        System.out.println("|      5. Salir                   |");
        System.out.println("|=================================|");
        System.out.println("Selecionar Opción");
        opcionMenu = leer.next();
        
        switch(opcionMenu){
            
            case "1":
                Create create = new Create();
                break;
            case "2":
                Read read = new Read();
                break;
            case "3":
                Update update = new Update();
                break;
            case "4":
                Delete delete = new Delete();
                break;
            case "5":
                System.exit(0);
                break;
            default:
                System.out.println("Opción Invalida");
                break;
        }
    }
}

//================================================================================================//

//-2-clase para crear

public class Create {
    
    Create()throws SQLException{
        
        Scanner leer = new Scanner(System.in);
        Persona person = new Persona();
        
        System.out.println("<<  CREAR REGISTRO  >>");
        
        System.out.println("nombre: ");
        person.setNomPersona(leer.nextLine());
        
        System.out.println("Correo Electrónico: ");
        person.setEmailPersona(leer.nextLine());
        
        System.out.println("Teléfono: ");
        person.setTelPersona(leer.nextLine());
        
        String tabla = "tb_contacto";
        String camposTabla = "nom_contacto, email_contacto, tel_contacto";
        String valoresCampos = "'" + person.getNomPersona() + "','" + person.getEmailPersona() + "','" + person.getTelPersona() + "'";
        
        ConexionCRUD utilerias = new ConexionCRUD();
        
        utilerias.guardarRegistros(tabla, camposTabla, valoresCampos);
        MenuPrincipal.desplegarMenu();
    }
}

//===============================================================================================//

//-3-Clase para borrar

public class Delete {
    
    Delete() throws SQLException{
        
        Scanner leer = new Scanner(System.in);
        ConexionCRUD utilerias = new ConexionCRUD();
        System.out.println("<<  ELIMINAR REGISTROS  >>");
        
        System.out.println("Ingresar el id del registro a eliminar");
        String idContactoEliminar = leer.next();
        
        String tabla = "tb_contacto";
        String campos = "*";
        String condicion = "id_contacto = " + idContactoEliminar;
        utilerias.desplegarRegistros(tabla, campos, condicion);
        
        System.out.println("presione << Y >> para confirmar");
        String confirmarBorrar = leer.next();
        
        if("Y".equals(confirmarBorrar) || "y".equals(confirmarBorrar)){
            
            String valoresCamposNuevos = "";
            utilerias.actualizarEliminarRegistro(tabla, valoresCamposNuevos, condicion);
            
            System.out.println("Registro Eliminado Correctamente!!");
        }
        
        MenuPrincipal.desplegarMenu();
    }
}

//==============================================================================================//

//-4-Clase para mostrar

public class Read {
    
    public Read() throws SQLException{
        
        System.out.println("<<  Consulta de Registros  >>");
        mostrarResultados();
    }
    
    private void mostrarResultados() throws SQLException{
        
        try{
            ConexionCRUD utilerias = new ConexionCRUD();
            String tabla = "tb_contacto";
            String camposTabla = "*";
            String condicionBusqueda = "";
            
            utilerias.desplegarRegistros(tabla, camposTabla, condicionBusqueda);
            
        }catch(SQLException ex){
            
            System.out.println("Ha ocurrido el siguiente error: " + ex.getMessage());
            
        }finally{
            
            MenuPrincipal.desplegarMenu();
        }
    }
}

//============================================================================================//

//-5-Clase para actualizar

public class Update {
    
    Update() throws SQLException{
        
        Scanner leer = new Scanner(System.in);
        Persona person = new Persona();
        ConexionCRUD utilerias = new ConexionCRUD();
        System.out.println("<<  ACTUALIZAR REGISTROS  >>");
        
        System.out.println("ingresar el id del registro a modificar");
        person.setIdPersona(leer.nextInt());
        
        String tablaBuscar = "tb_contacto";
        String camposBuscar = "id_contacto, nom_contacto, email_contacto, tel_contacto";
        String condicionBuscar = "id_contacto = " + person.getIdPersona();
        
        utilerias.desplegarRegistros(tablaBuscar, camposBuscar, condicionBuscar);
        
        System.out.println("Nombre: ");
        person.setNomPersona(leer.next());
        
        System.out.println("Correo Electrónico: ");
        person.setEmailPersona(leer.next());
        
        System.out.println("Teléfono: ");
        person.setTelPersona(leer.next());
        
        String tabla = "tb_contacto";
        String camposValoresNuevos = "nom_contacto = '" + person.getNomPersona() + "', email_contacto = '" + person.getEmailPersona() + "', tel_contacto = '" + person.getTelPersona() + "'";
        utilerias.actualizarEliminarRegistro(tabla, camposValoresNuevos, condicionBuscar);
        
        System.out.println("Modificado Correctamente!!");
        
        MenuPrincipal.desplegarMenu();
    }
}