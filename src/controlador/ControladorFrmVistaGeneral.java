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
import javax.swing.table.DefaultTableModel;
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
    
    private void obtenerDineroTotal() throws SQLException{
        try {
            //conectar();
            Sistema.st=Sistema.con.createStatement();
            Sistema.rs=Sistema.st.executeQuery("SELECT dinero_total FROM usuario WHERE nombre_usuario='"+Sistema.usuarioConectado.getNombre_usuario()+"'");
            if(Sistema.rs.next()){
                vista.txtDineroTotal.setText(String.format("%.2f", Sistema.rs.getFloat("dinero_total")));
            }
        } catch (Exception e) {
        }
    }
    
    private void designTablaResumenMedios() throws SQLException{
        DefaultTableModel modeloT = new DefaultTableModel();
        vista.tblResumenMedios.setModel(modeloT);
        modeloT.addColumn("Codigo de medio");
        modeloT.addColumn("Nombre");
        modeloT.addColumn("Monto");
        modeloT.addColumn("Descripcion");
        
        //conectar();
        PreparedStatement ps=null;
        ResultSet rsT;
        String sql ="SELECT cod_medio,nombre_medio,monto_total,descripcion FROM medio WHERE nombre_usuario='"+Sistema.usuarioConectado.getNombre_usuario()+"'";
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
    }
    
    public void design() throws SQLException{
        vista.txtNombreUsuario.setText(Sistema.usuarioConectado.getNombre_usuario());
        obtenerDineroTotal();
        designTablaResumenMedios();
    }
    
    public void frmIniciar() throws SQLException{
        Sistema.actualizar_montos_bd();
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        design();
    }
}
