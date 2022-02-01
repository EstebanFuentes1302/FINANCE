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
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.Medio;
import vista.FrmMedio;
import vista.FrmNuevaOperacionMedio;

public class ControladorFrmNuevaOperacionMedio {
    private FrmNuevaOperacionMedio vista;
    private Medio m;
    boolean b;
    
    public ControladorFrmNuevaOperacionMedio(FrmNuevaOperacionMedio vista,Medio m) throws SQLException {
        this.vista = vista;
        this.m=new Medio(m.getCod_medio(),m.getCod_medio(),m.getMoneda());
        
        iniciarFrm();
    }

    public void funcionalidades() throws SQLException{       
        b=false;
        vista.btnAgregar.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean b=false;
            while (b==false){
                try {
                    Sistema.st=Sistema.con.createStatement();
                    ResultSet rs = Sistema.st.executeQuery("SELECT SYSDATE()");
                    Sistema.st.execute("INSERT INTO operacion VALUES('"+GeneradorCodigo.generarCodigoOperacion()+"',"+vista.txtMonto.getText()+",'"+vista.txtDescripcion.getText()+"','"+m.getCod_medio()+"','"+Sistema.getNow()+"')");
                    
                    JOptionPane.showMessageDialog(null, "Se agregó la operacion");
                    
                    FrmMedio vistaM = new FrmMedio();
                    ControladorFrmMedio controladorM = new ControladorFrmMedio(vistaM);
                    b=true;
                } catch (SQLException sqle) {
                    System.out.println("Error al agregar: "+sqle);
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
                    System.out.println(ex);
                }
                
            }
        });
        
        
        
        
    }
    
    public void design(){
        vista.txtNombreMedio.setText(m.getCod_medio());
        vista.txtMoneda.setText(m.getMoneda());
        
    }
    
    public void iniciarFrm() throws SQLException{
        Sistema.actualizar_montos_bd();
        funcionalidades();
        design();
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

}
