/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import general.Sistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import vista.FrmMedio;
import vista.FrmNuevaOperacionMedio;
import vista.FrmNuevoMedio;
import vista.FrmVistaGeneral;

/**
 *
 * @author Esteban
 */
public class ControladorFrmMedio {
    private FrmMedio vista;
    
    public ControladorFrmMedio(FrmMedio vista) throws SQLException{
        this.vista=vista;
        frmIniciar();
    }
    
    public void funcionalidades(){
        this.vista.btnRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmVistaGeneral vistaG = new FrmVistaGeneral();
                try {
                    ControladorFrmVistaGeneral controladorVG = new ControladorFrmVistaGeneral(vistaG);
                    vista.dispose();
                } catch (SQLException ex) {
                    System.out.println(e);
                }
            }
        });
        
        this.vista.btnNuevaOperacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FrmNuevaOperacionMedio vista2 = new FrmNuevaOperacionMedio();
                    ControladorFrmNuevaOperacionMedio controlador2 = new ControladorFrmNuevaOperacionMedio(vista2,vista.cboNombreMedio.getSelectedItem().toString(),vista.txtCodigoMedio.getText());
                    vista.dispose();
                } catch (SQLException ex) {
                    System.out.println(e);
                }
            }
        });
        
        this.vista.cboNombreMedio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionComboBox();
            }
        });
        
        vista.btnNuevoMedio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FrmNuevoMedio vistaNM = new FrmNuevoMedio();
                    ControladorFrmNuevoMedio controladorNM = new ControladorFrmNuevoMedio(vistaNM);
                    vista.dispose();
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorFrmMedio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public void accionComboBox(){
        try {
            DatosTabla();
            Sistema.st=Sistema.con.createStatement();
            String sql="SELECT cod_medio FROM medio WHERE nombre_medio='"+vista.cboNombreMedio.getSelectedItem().toString()+"'";
            System.out.println(sql);
            Sistema.rs=Sistema.st.executeQuery(sql);
            while(Sistema.rs.next()){
                vista.txtCodigoMedio.setText(Sistema.rs.getString("cod_medio"));
                obtenerDineroMedioTotal();
                DatosTabla();
            }
            vista.btnNuevaOperacion.setEnabled(true);

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    private void obtenerDineroMedioTotal(){
        try {
            Sistema.st=Sistema.con.createStatement();
            ResultSet rs2;
            rs2 = Sistema.st.executeQuery("SELECT cod_medio from medio WHERE nombre_medio='"+vista.cboNombreMedio.getSelectedItem().toString()+"'");
            if(rs2.next()){
                String sql="select monto_total from medio where cod_medio='"+rs2.getString("cod_medio")+"'";
                Sistema.rs=Sistema.st.executeQuery(sql);
                if(Sistema.rs.next()){
                    vista.txtDineroTotal.setText(Sistema.formatFloat(Sistema.rs.getFloat("monto_total")));
                }
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public void DatosTabla() throws SQLException{
        DefaultTableModel modeloT = new DefaultTableModel();
        vista.tblOperaciones.setModel(modeloT);
        modeloT.addColumn("Codigo");
        modeloT.addColumn("Monto");
        modeloT.addColumn("Codigo de medio");
        modeloT.addColumn("Descripcion");
        
        PreparedStatement ps=null;
        ResultSet rsT;
        String sql ="SELECT cod_operacion,monto,cod_medio,descripcion FROM operacion WHERE cod_medio='"+vista.txtCodigoMedio.getText()+"'";
        ps=Sistema.con.prepareStatement(sql);
        rsT=ps.executeQuery();
        
        ResultSetMetaData rsMD = rsT.getMetaData();
        
        int cc=rsMD.getColumnCount();
        
        while(rsT.next()){
            Object[] row = new Object[cc];
            for (int i = 0; i < cc; i++) {
                row[i]=rsT.getObject(i+1);
            }
            modeloT.addRow(row);
        }
        
        
    }

    public void ComboBox() throws SQLException{
        DefaultComboBoxModel<String> cboMedios = new DefaultComboBoxModel<String>();
        int cc;
        Sistema.st=Sistema.con.createStatement();
        Sistema.rs=Sistema.st.executeQuery("SELECT nombre_medio FROM medio WHERE nombre_usuario='"+Sistema.usuarioConectado.getNombre_usuario()+"'");
        ResultSetMetaData rsMD = Sistema.rs.getMetaData();
        cc=rsMD.getColumnCount();
        while(Sistema.rs.next()){
            Object[] row = new Object[cc];
            for (int i = 0; i < cc; i++) {
                cboMedios.addElement(Sistema.rs.getString("nombre_medio"));
            }
        }
        this.vista.cboNombreMedio.setModel(cboMedios);
    }
    
    private void design() throws SQLException{
        vista.btnNuevaOperacion.setEnabled(false);
        ComboBox();
        accionComboBox();
    }
    
    public void frmIniciar() throws SQLException{
        Sistema.actualizar_montos_bd();
        design();
        funcionalidades();
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);  
    }
}
