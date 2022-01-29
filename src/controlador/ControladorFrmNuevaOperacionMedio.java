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
import vista.FrmMedio;
import vista.FrmNuevaOperacionMedio;

public class ControladorFrmNuevaOperacionMedio {
    private FrmNuevaOperacionMedio vista;
    private String nombre_medio,cod_medio;
    boolean b;
    
    public ControladorFrmNuevaOperacionMedio(FrmNuevaOperacionMedio vista,String nombre_medio,String cod_medio) throws SQLException {
        this.vista = vista;
        this.nombre_medio=nombre_medio;
        this.cod_medio=cod_medio;
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
                    Sistema.st.execute("INSERT INTO operacion VALUES('"+GeneradorCodigo.generarCodigoOperacion()+"',"+vista.txtMonto.getText()+",'"+vista.txtDescripcion.getText()+"','"+cod_medio+"','"+Sistema.getNow()+"')");
                    
                    JOptionPane.showMessageDialog(null, "Se agregó la operacion");
                    
                    FrmMedio vistaM = new FrmMedio();
                    ControladorFrmMedio controladorM = new ControladorFrmMedio(vistaM);
                    vista.dispose();
                    
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
        vista.txtNombreMedio.setText(nombre_medio);
    }
    
    public void iniciarFrm() throws SQLException{
        Sistema.actualizar_montos_bd();
        funcionalidades();
        design();
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

}
