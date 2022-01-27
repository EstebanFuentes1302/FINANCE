/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.mysql.jdbc.Connection;
import general.Sistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import modelo.Usuario;
import vista.FrmLogin;
import vista.FrmMedio;
import vista.FrmRegistroUsuario;
import vista.FrmVistaGeneral;

/**
 *
 * @author Esteban
 */
public class ControladorFrmLogin {
    private static Connection con;
    private static Statement st;
    private static ResultSet rs;
    
    private static final String driver="com.mysql.jdbc.Driver";
    private static final String user="esteban";
    private static final String pass="123";
    //private static final String url="jdbc:mysql://localhost:3306/ahorros_bd";
    private static final String url="jdbc:mysql://8.12.17.20:3306/finance_bd";
    
    
    private FrmLogin vista;

    public ControladorFrmLogin(FrmLogin vista) throws SQLException{
        conectar();
        this.vista=vista;
        funcionalidades();
        frmIniciar();
    }
    
    public void conectar() throws SQLException {
        con=null;
        try{
            Class.forName(driver);
            con= (Connection) DriverManager.getConnection(url, user, pass);
            if (con!=null){
                System.out.println("conectado");
            }
        }
        catch (ClassNotFoundException | SQLException e){
            System.out.println(e);
        }
    }
    
    public boolean ingresar() throws SQLException{
        //conectar();
        st=con.createStatement();
        rs=st.executeQuery("SELECT * from usuario where nombre_usuario='"+vista.txtUsuario.getText()+"'");
        if(rs.next()){
            if(vista.txtPassword.getText().equals(rs.getString("password"))){
                System.out.println(rs.getString("dinero_total"));
                Sistema.usuarioConectado=new Usuario(rs.getString("nombre_usuario"), rs.getString("password"),rs.getFloat("dinero_total"));
                JOptionPane.showMessageDialog(null, "Ingreso exitoso");
                return true;
            }
        }else{
            JOptionPane.showMessageDialog(null, "No se pudo ingresar");
        }
        return false;
        
    }
    
    public void funcionalidades() throws SQLException{
        vista.btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(ingresar()){
                        FrmVistaGeneral vistaG = new FrmVistaGeneral();
                        ControladorFrmVistaGeneral controladorG = new ControladorFrmVistaGeneral(vistaG);
                        vista.dispose();
                    }
                } catch (SQLException ex) {
                    System.out.println(e);
                }
            }
        });
        vista.btnRegistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FrmRegistroUsuario vistaR = new FrmRegistroUsuario();
                    ControladorFrmRegistrarUsuario controladorR = new ControladorFrmRegistrarUsuario(vistaR);
                    vista.dispose();
                } catch (SQLException ex) {
                    System.out.println("en boton: "+e);
                }
                
                
            }
        });
    }
    
    public void frmIniciar() throws SQLException{
        
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }
}
