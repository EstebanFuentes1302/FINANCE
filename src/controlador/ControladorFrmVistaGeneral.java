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
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import vista.FrmLogin;
import vista.FrmMedio;
import vista.FrmVistaGeneral;

/**
 *
 * @author Esteban
 */
public class ControladorFrmVistaGeneral {
    
    private FrmVistaGeneral vista;

    public ControladorFrmVistaGeneral(FrmVistaGeneral vista) throws SQLException {
        this.vista = vista;
        frmIniciar(); 
        funcionalidades();
    }
    
    private void designTablaResumenMedios(){
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        
        //TAMAÑO DE COLUMNAS
        TableColumnModel modeloC = vista.tblResumenMedios.getColumnModel();
        modeloC.getColumn(0).setPreferredWidth(25);
        modeloC.getColumn(1).setPreferredWidth(20);
        modeloC.getColumn(2).setPreferredWidth(20);
        
        
        //CENTRAR CABECERA
        ((DefaultTableCellRenderer) vista.tblResumenMedios.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(0);
       
        //PROHIBIR MOVER LA CABECERA
        vista.tblResumenMedios.getTableHeader().setReorderingAllowed(false);

        TableColumnModel modelo;
        modelo=vista.tblResumenMedios.getColumnModel();
        int cc= modelo.getColumnCount();
        for (int i = 0; i < cc; i++) {
            modelo.getColumn(i).setCellRenderer(centerRenderer);
        }
        
        vista.tblResumenMedios.setRowHeight(25);
        vista.tblResumenMedios.getTableHeader().setPreferredSize(new Dimension(20,25));
    }
    
    private void designTablaResumenDinero(){
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        
        //TAMAÑO DE COLUMNAS
        TableColumnModel modeloC = vista.tblResumenDinero.getColumnModel();
        modeloC.getColumn(0).setPreferredWidth(15);
        modeloC.getColumn(1).setPreferredWidth(50);
        
        
        //CENTRAR CABECERA
        ((DefaultTableCellRenderer) vista.tblResumenDinero.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(0);
       
        //PROHIBIR MOVER LA CABECERA
        vista.tblResumenDinero.getTableHeader().setReorderingAllowed(false);
        
        
        
        TableColumnModel modelo;
        modelo=vista.tblResumenDinero.getColumnModel();
        
        
        int cc= modelo.getColumnCount();
        for (int i = 0; i < cc; i++) {
            modelo.getColumn(i).setCellRenderer(centerRenderer);
        }
        
        vista.tblResumenDinero.setRowHeight(25);
        vista.tblResumenDinero.getTableHeader().setPreferredSize(new Dimension(20,25));
    }
    
    private void datosTablaResumenMedios() throws SQLException{
        
        DefaultTableModel modeloT = new DefaultTableModel();
        vista.tblResumenMedios.setModel(modeloT);
        modeloT.addColumn("Nombre");
        modeloT.addColumn("Monto Total");
        modeloT.addColumn("Moneda");
        
        try {
            Sistema.st=Sistema.con.createStatement();
            
            ResultSet rsT;
            
            String sql ="SELECT nombre_medio,monto_total,cod_moneda FROM medio where nombre_usuario='"+Sistema.usuarioConectado.getNombre_usuario()+"'";

            rsT=Sistema.st.executeQuery(sql);
            ResultSetMetaData rsMD=rsT.getMetaData();
            int cc=rsMD.getColumnCount();

            while(rsT.next()){      
                Object[] row = new Object[cc];
                for (int i = 0; i < cc; i++) {
                    row[i]=rsT.getObject(i+1);
                }
                modeloT.addRow(row);
            }
            
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void datosTablaResumenDinero() throws SQLException{
        DefaultTableModel modeloT = new DefaultTableModel();
        vista.tblResumenDinero.setModel(modeloT);
        modeloT.addColumn("Moneda");
        modeloT.addColumn("Dinero total");
        
        ResultSet rsT;
        String sql ="SELECT distinct cod_moneda FROM medio where nombre_usuario='"+Sistema.usuarioConectado.getNombre_usuario()+"'";
        
        Sistema.st=Sistema.con.createStatement();

        rsT=Sistema.st.executeQuery(sql);
        
        ResultSetMetaData rsMD = rsT.getMetaData();
        int cc=rsMD.getColumnCount();
        
        
        while(rsT.next()){
            Statement st = Sistema.con.createStatement();
            String sql2 = "select distinct ifnull((select sum(monto_total) from medio where cod_moneda='"+rsT.getString("cod_moneda")+"'),0) as suma from (select * from medio) as b where nombre_usuario='"+Sistema.usuarioConectado.getNombre_usuario()+"'";

            ResultSet rs = st.executeQuery(sql2);
            Object[] row = new Object[2];

            while(rs.next()){
                row[0]=rsT.getString("cod_moneda");
                row[1]=rs.getString("suma");
            }
            
            modeloT.addRow(row);
        }
    }
    
    public void funcionalidades(){
        this.vista.btnMedios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FrmMedio vistaM = new FrmMedio();
                    ControladorFrmMedio controladorM = new ControladorFrmMedio(vistaM);
                    vista.dispose();
                } catch (SQLException ex) {
                    System.out.println(e);
                }
            }
        });
        
        vista.btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Sistema.st=Sistema.con.createStatement();
                    Sistema.st.execute("update usuario set conectado=false where nombre_usuario='"+Sistema.usuarioConectado.getNombre_usuario()+"'");
                    vista.dispose();
                    JOptionPane.showMessageDialog(null, "Desconectado");
                    FrmLogin vistaL = new FrmLogin();
                    ControladorFrmLogin controladorL = new ControladorFrmLogin(vistaL);
                    vista.dispose();
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        });
    }

    
    public void design() throws SQLException{
        vista.txtNombreUsuario.setText(Sistema.usuarioConectado.getNombre_usuario());
        
        datosTablaResumenDinero();
        designTablaResumenDinero();
        datosTablaResumenMedios();
        designTablaResumenMedios();
                
    }
    
    public void frmIniciar() throws SQLException{
        Sistema.actualizar_montos_bd();
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        design();
    }
}
