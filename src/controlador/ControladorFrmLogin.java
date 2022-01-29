/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import general.Sistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.Usuario;
import vista.FrmLogin;
import vista.FrmRegistroUsuario;
import vista.FrmVistaGeneral;

/**
 *
 * @author Esteban
 */
public class ControladorFrmLogin {
    private FrmLogin vista;

    public ControladorFrmLogin(FrmLogin vista) throws SQLException{
        //conectar();
        this.vista=vista;
        funcionalidades();
        frmIniciar();
    }

    public boolean ingresar() throws SQLException{
        //conectar();
        Sistema.st=Sistema.con.createStatement();
        Sistema.rs=Sistema.st.executeQuery("SELECT * from usuario where nombre_usuario='"+vista.txtUsuario.getText()+"'");
        if(Sistema.rs.next()){
            if(vista.txtPassword.getText().equals(Sistema.rs.getString("password"))){
                Sistema.usuarioConectado=new Usuario(Sistema.rs.getString("nombre_usuario"), Sistema.rs.getString("password"),Sistema.rs.getFloat("dinero_total"));
                Sistema.usuarioConectado.setConectado(true);
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
