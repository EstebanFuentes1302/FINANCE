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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import vista.FrmNuevaOperacion;
import vista.FrmVistaGeneral;

/**
 *
 * @author Esteban
 */
public class ControladorFrmNuevaOperacion {
    private FrmNuevaOperacion vista;
    
    private int CantidadFilas;
    private int CantidadColumnas;
    private int ContadorRegistros;
    private int UbicacionRegistros;
    
    public ControladorFrmNuevaOperacion(FrmNuevaOperacion vista) throws SQLException{
        this.vista=vista;
        frmIniciar();
        ComboBox();
    }
    
    public void ComboBox(){
        DefaultComboBoxModel<String> cboMedios = new DefaultComboBoxModel<String>();
        for (int i = 0; i < CantidadFilas; i++) {
            cboMedios.addElement(Sistema.medios.getMedio(i).getCod_medio());
        }
        this.vista.cboMedios.setModel(cboMedios);
    }
    
    public void afectarUsuario() throws SQLException{
        Sistema.st=Sistema.con.createStatement();
        float montob=0, montoa=0;
        String selectm="SELECT dinero_total from usuario where nombre_usuario='"+Sistema.usuarioConectado.getNombre_usuario()+"'";
        Sistema.rs=Sistema.st.executeQuery(selectm);
        
        if(Sistema.rs.next()){
                montob=Sistema.rs.getFloat("dinero_total");

                montoa=Float.parseFloat(vista.txtMonto.getText())+montob;

                String ex="UPDATE usuario SET dinero_total="+montoa+" WHERE nombre_usuario='"+Sistema.usuarioConectado.getNombre_usuario()+"'";
                Sistema.st.execute(ex);
                Sistema.usuarioConectado.setDinero_total(montoa);
                
            }else{
                System.out.println("error en el result set");
            }
    }
    
    public void afectarMedio() throws SQLException{
        
        try {
            //conectar();
            Sistema.st=Sistema.con.createStatement();
            float montob=0, montoa=0;
            
            String selectm="SELECT monto_total from medio where cod_medio='"+vista.cboMedios.getSelectedItem().toString()+"'";
            Sistema.rs=Sistema.st.executeQuery(selectm);
            
            if(Sistema.rs.next()){
                montob=Sistema.rs.getFloat("monto_total");

                montoa=Float.parseFloat(vista.txtMonto.getText())+montob;

                String ex="UPDATE medio SET monto_total="+montoa+" WHERE cod_medio='"+vista.cboMedios.getSelectedItem().toString()+"'";
                Sistema.st.execute(ex);             
            }else{
                System.out.println("error en el result set");
            }
            
        } catch (Exception e) {
            System.out.println("Error al afectar medio: "+e);
        }
        
        
    }
    
    public String generarCodigoOperacion() throws SQLException{
        String cod="";
        try {
            cod=GeneradorCodigo.generarCodigoOperacion();
        } catch (Exception e) {
            System.out.println("Error al generar codigo: "+e);
        }  
        return cod;
    }
    
    public void AñadirOperacion() throws SQLException{
        try {
            //conectar();
            String codg=generarCodigoOperacion();
            String result="INSERT INTO operacion VALUES ('"+codg+"',"+vista.txtMonto.getText()+",'"+vista.txtDescripcion.getText()+"','"+vista.cboMedios.getSelectedItem().toString()+"')";
            Sistema.st=Sistema.con.createStatement();
            Sistema.st.execute(result);
            
            /*afectarMedio();
            afectarUsuario();*/
            
            Sistema.actualizar_montos_bd();
            
            FrmVistaGeneral vistaG = new FrmVistaGeneral();
            ControladorFrmVistaGeneral controladorG = new ControladorFrmVistaGeneral(vistaG);
            vista.dispose();
        } catch (Exception e) {
            System.out.println("Error al añadir: "+e);
        }
        
    }
    
    public void funcionalidades(){
        this.vista.btnAgregarOperacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AñadirOperacion();
                    JOptionPane.showMessageDialog(null, "Se agregó la operación");
                } catch (SQLException ex) {
                    System.out.println(e);
                }
            }
        });
        this.vista.btnRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmVistaGeneral vistaG = new FrmVistaGeneral();
                try {
                    ControladorFrmVistaGeneral controladorVG = new ControladorFrmVistaGeneral(vistaG);
                } catch (SQLException ex) {
                    System.out.println(e);
                }
                vista.dispose();
            }
        });
        
    }
    
    public void frmIniciar() throws SQLException{
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }
}
