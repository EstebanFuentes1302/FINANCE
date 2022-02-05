/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import general.Sistema;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import modelo.Medio;
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
    private String nombre_medio;
    public ControladorFrmMedio(FrmMedio vista) throws SQLException{
        this.vista=vista;
        frmIniciar();
    }
    
    public ControladorFrmMedio(FrmMedio vista,String nombre_medio) throws SQLException{
        this.vista=vista;
        this.nombre_medio=nombre_medio;
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
                    ControladorFrmNuevaOperacionMedio controlador2 = new ControladorFrmNuevaOperacionMedio(vista2,new Medio(vista.txtCodigoMedio.getText(), vista.cboNombreMedio.getSelectedItem().toString(), vista.txtMoneda.getText()));
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
        
        this.vista.btnInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Sistema.st=Sistema.con.createStatement();
                    String sql = "select descripcion from medio where cod_medio='"+vista.txtCodigoMedio.getText()+"'"; 
                    
                    Sistema.rs=Sistema.st.executeQuery(sql);
                    if(Sistema.rs.next()){
                        JOptionPane.showMessageDialog(null, Sistema.rs.getString("descripcion"),"Información de Medio",JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                } catch (SQLException ex) {
                    System.out.println("Error en la info: "+e);
                }
            }
        });
        
    }
    
    
    
    public void accionComboBox(){
        try {
            if(vista.cboNombreMedio.getSelectedItem()!=null){
                if(nombre_medio!=null){
                    Sistema.st=Sistema.con.createStatement();
                    String sql="SELECT cod_medio,cod_moneda FROM medio WHERE nombre_medio='"+nombre_medio+"'";
                    Sistema.rs=Sistema.st.executeQuery(sql);
                    System.out.println("adfnkladfl");
                }else{
                    Sistema.st=Sistema.con.createStatement();
                    String sql="SELECT cod_medio,cod_moneda FROM medio WHERE nombre_medio='"+vista.cboNombreMedio.getSelectedItem().toString()+"'";
                    Sistema.rs=Sistema.st.executeQuery(sql);
                } 
                datosTabla();

                while(Sistema.rs.next()){
                    vista.txtCodigoMedio.setText(Sistema.rs.getString("cod_medio"));
                    vista.txtMoneda.setText(Sistema.rs.getString("cod_moneda"));
                    obtenerDineroMedioTotal();
                    datosTabla();
                }
                vista.btnNuevaOperacion.setEnabled(true);
                vista.btnInfo.setEnabled(true);
            }else{
                System.out.println("A");
            }
            

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
                String sql="select cod_moneda,monto_total from medio where cod_medio='"+rs2.getString("cod_medio")+"'";
                Sistema.rs=Sistema.st.executeQuery(sql);
                if(Sistema.rs.next()){
                    vista.txtDineroTotal.setText(Sistema.rs.getString("cod_moneda")+" "+Sistema.formatFloat(Sistema.rs.getFloat("monto_total")));
                }
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    private void datosTabla() throws SQLException{
        DefaultTableModel modeloT = new DefaultTableModel();
        vista.tblOperaciones.setModel(modeloT);
        modeloT.addColumn("Codigo");
        modeloT.addColumn("Monto");
        modeloT.addColumn("Fecha");
        modeloT.addColumn("Descripcion");
        
        
        PreparedStatement ps=null;
        ResultSet rsT;
        String sql ="SELECT cod_operacion,monto,fecha as date,descripcion FROM operacion WHERE cod_medio='"+vista.txtCodigoMedio.getText()+"' order by date desc";
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
        designTabla();
    }
    
     private void designTabla(){
        vista.tblOperaciones.setRowHeight(30);
        vista.tblOperaciones.getTableHeader().setPreferredSize(new Dimension(20,25));
        TableColumnModel modelo = vista.tblOperaciones.getColumnModel();
        
         
        modelo.getColumn(0).setPreferredWidth(20);
        modelo.getColumn(1).setPreferredWidth(10);
        
        
         DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
         centerRenderer.setHorizontalAlignment(JLabel.CENTER);
         int cc = modelo.getColumnCount();
         for (int i = 0; i < cc; i++) {
             modelo.getColumn(i).setCellRenderer(centerRenderer);
         }
         
         //ACCION AL SELECCIONAR
         vista.tblOperaciones.setDefaultEditor(Object.class, null);
         vista.tblOperaciones.setEnabled(true);
         
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
