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
import vista.FrmLogin;
import vista.FrmRegistroUsuario;

/**
 *
 * @author Esteban
 */
public class ControladorFrmRegistrarUsuario {
    FrmRegistroUsuario vista = new FrmRegistroUsuario();

    public ControladorFrmRegistrarUsuario(FrmRegistroUsuario vista) throws SQLException {
        this.vista=vista;
        frmIniciar();
        
    }
    
    public void funcionalidades(){
        vista.btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Sistema.st=Sistema.con.createStatement();
                    String sql="INSERT INTO usuario VALUES('"+vista.txtNombreUsuario1.getText()+"','"+vista.txtPassword.getText()+"',0)";
                    Sistema.st.execute(sql);
                    
                    FrmLogin vistaL = new FrmLogin();
                    ControladorFrmLogin controladorL = new ControladorFrmLogin(vistaL);
                    vista.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "No se pudo registrar: ");
                    System.out.println(e);
                }
                
            }
        });
        vista.btnRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmLogin vistaL = new FrmLogin();
                try {
                    ControladorFrmLogin controladorL = new ControladorFrmLogin(vistaL);
                } catch (SQLException ex) {
                    System.out.println("en regresar: "+e);
                }
                vista.dispose();
            }
        });
    }
    
    public void design(){
        funcionalidades();
    }
    
    public void frmIniciar() throws SQLException{
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        System.out.println("asdasdasd");
        design();
    }
    
}
