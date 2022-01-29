/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import modelo.MedioArreglo;
import modelo.OperacionArreglo;
import modelo.Usuario;

/**
 *
 * @author Esteban
 */
public class Sistema {
    public static Connection con;
    public static Statement st;
    public static ResultSet rs;
    
    private static final String driver="com.mysql.jdbc.Driver";
    private static final String user="esteban";
    private static final String pass="123";
    //private static final String url="jdbc:mysql://localhost:3306/ahorros_bd";
    private static final String url="jdbc:mysql://8.12.17.20:3306/finance_bd";
    
    
    public static MedioArreglo medios = new MedioArreglo();
    public static Usuario usuarioConectado;
    public static OperacionArreglo operaciones = new OperacionArreglo();
    
    
    public static String formatFloat(float f){
        return String.format("%.2f", f);
    }
    
    public static void conectar() throws SQLException {
        // Reseteamos a null la conexion a la bd
        con=null;
        try{
            Class.forName(driver);
            // Nos conectamos a la bd
            con= (Connection) DriverManager.getConnection(url, user, pass);
            /*if (con!=null){
                System.out.println("conectado");
            }*/
        }
        // Si la conexion NO fue exitosa mostramos un mensaje de error
        catch (ClassNotFoundException | SQLException e){
            System.out.println(e);
        }
    }
    
    public static void actualizar_montos_bd(){
        try {
            conectar();
            st=con.createStatement();
            
            rs=st.executeQuery("SELECT cod_medio FROM medio WHERE nombre_usuario='"+Sistema.usuarioConectado.getNombre_usuario()+"'");
            while(rs.next()){
                st=con.createStatement();

                //SUMA VALORES PARA LOS MEDIOS
                String sql="update medio set monto_total=((select distinct ifnull((select sum(monto) from operacion where cod_medio='"+rs.getString("cod_medio")+"'),0) from (select * from medio) as b where cod_medio='"+rs.getString("cod_medio")+"')) where cod_medio='"+rs.getString("cod_medio")+"'";
                String sql2="update usuario set dinero_total=((select distinct ifnull((select sum(monto_total) from medio where nombre_usuario='"+usuarioConectado.getNombre_usuario()+"'),0) from (select * from usuario) as u where nombre_usuario='"+usuarioConectado.getNombre_usuario()+"')) where nombre_usuario='"+usuarioConectado.getNombre_usuario()+"'";
                
                st.execute(sql);
                st.execute(sql2);

            }
            
        } catch (Exception e) {
            System.out.println("en actualizar: "+e);
        }

        
    }
    
    public static String getNow(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
    
}
