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
import javax.swing.JOptionPane;
import vista.FrmMedio;
import vista.FrmNuevaOperacionMedio;

public class ControladorFrmNuevaOperacionMedio {
    private FrmNuevaOperacionMedio vista;
    private String nombre_medio,cod_medio;
    boolean b;
    
    public ControladorFrmNuevaOperacionMedio(FrmNuevaOperacionMedio vista,String nombre_medio,String cod_medio) {
        this.vista = vista;
        this.nombre_medio=nombre_medio;
        this.cod_medio=cod_medio;
        iniciarFrm();
    }
    
    
    
    public void funcionalidades() throws SQLException{       
        vista.txtNombreMedio.setText(nombre_medio);
        b=false;
        while(b=false){
            vista.btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Sistema.st=Sistema.con.createStatement();
                    Sistema.rs=Sistema.st.executeQuery("INSERT INTO operacion VALUES("+GeneradorCodigo.generarCodigoOperacion()+","+vista.txtMonto.getText()+","+vista.txtDescripcion.getText()+","+cod_medio);
                    b=true;
                    JOptionPane.showMessageDialog(null, "Se agregó el medio");
                    FrmMedio vistaM = new FrmMedio();
                    ControladorFrmMedio controladorM = new ControladorFrmMedio(vistaM);
                    vista.dispose();
                    
                } catch (SQLException sqle) {
                    System.out.println("Error al agregar: "+sqle);
                }
            }
            });
        }
        vista.btnRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vista.dispose();
                FrmMedio vistaM = new FrmMedio();
                try {
                    ControladorFrmMedio controladorM = new ControladorFrmMedio(vistaM);
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
            }
        });
        
        
        
        
    }
    
    public void design(){
        vista.txtNombreMedio.setText(nombre_medio);
    }
    
    public void iniciarFrm(){
        Sistema.actualizar_montos_bd();
        design();
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }
}
