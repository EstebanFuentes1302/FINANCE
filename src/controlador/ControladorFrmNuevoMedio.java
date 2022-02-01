/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import general.GeneradorCodigo;
import general.Sistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import vista.FrmMedio;
import vista.FrmNuevoMedio;

/**
 *
 * @author Esteban
 */
public class ControladorFrmNuevoMedio {
    private FrmNuevoMedio vista;

    public ControladorFrmNuevoMedio(FrmNuevoMedio vista) throws SQLException {
        this.vista = vista;
        iniciarFrm();
    }
    
    public void comboBoxMoneda() throws SQLException{
        try {
            DefaultComboBoxModel modelo = new DefaultComboBoxModel();
            Sistema.st=Sistema.con.createStatement();
            Sistema.rs=Sistema.st.executeQuery("select cod_moneda from moneda");
            while(Sistema.rs.next()){
                modelo.addElement(Sistema.rs.getString("cod_moneda"));
            }
            vista.cboMoneda.setModel(modelo);
        } catch (SQLException e) {
            System.out.println("Error en Combo Box de monedas: "+e);
        }
        
        
    }
    
    public void funcionalidades(){
        vista.btnAgregarMedio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean b=false;
                while(b==false){
                    try {
                            Sistema.st=Sistema.con.createStatement();
                            String sql ="INSERT INTO medio VALUES('"+GeneradorCodigo.generarCodigoMedio()+"',0,'"+vista.txtNombreMedio.getText()+"','"+vista.txtDescripcion.getText()+"','"+Sistema.usuarioConectado.getNombre_usuario()+"','"+vista.cboMoneda.getSelectedItem().toString()+"')";

                            System.out.println(sql);
                            Sistema.st.execute(sql);
                            b=true;

                            JOptionPane.showMessageDialog(null, "Medio agregado");
                            FrmMedio vistaM = new FrmMedio();
                            ControladorFrmMedio controladorM = new ControladorFrmMedio(vistaM);
                            vista.dispose();


                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "No se pudo agregar medio");
                    }
                }
                
            }
        });
        
        vista.btnRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FrmMedio vistaM = new FrmMedio();
                    ControladorFrmMedio controladorM = new ControladorFrmMedio(vistaM);
                    vista.dispose();
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorFrmNuevoMedio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public void design() throws SQLException{
        comboBoxMoneda();

    }
    
    public void iniciarFrm() throws SQLException{
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        design();
        funcionalidades();
    }
    
}
