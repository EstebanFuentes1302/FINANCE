/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.mysql.jdbc.Connection;
import general.Sistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import vista.FrmMedio;
import vista.FrmNuevaOperacion;
import vista.FrmVistaGeneral;

/**
 *
 * @author Esteban
 */
public class ControladorFrmVistaGeneral {
    private FrmVistaGeneral vista;

    private static Connection con;
    private static Statement st;
    private static ResultSet rs;
    
    private static final String driver="com.mysql.jdbc.Driver";
    private static final String user="esteban";
    private static final String pass="123";
    //private static final String url="jdbc:mysql://localhost:3306/ahorros_bd";
    private static final String url="jdbc:mysql://8.12.17.20:3306/finance_bd";
    
    public ControladorFrmVistaGeneral(FrmVistaGeneral vista) throws SQLException {
        this.vista = vista;
        frmIniciar(); 
        funcionalidades();
    }
    
    public void conectar() throws SQLException {
        con=null;
        try{
            Class.forName(driver);
            con= (Connection) DriverManager.getConnection(url, user, pass);
        }
        catch (ClassNotFoundException | SQLException e){
            System.out.println("Error de conexion: "+e);
        }
    }
    
    
    
    private void obtenerDineroTotal() throws SQLException{
        try {
            conectar();
            st=con.createStatement();
            rs=st.executeQuery("SELECT dinero_total FROM usuario WHERE nombre_usuario='"+Sistema.usuarioConectado.getNombre_usuario()+"'");
            if(rs.next()){
                vista.txtDineroTotal.setText(String.format("%.2f", rs.getFloat("dinero_total")));
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
        ps=con.prepareStatement(sql);
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
                FrmMedio vistaM = new FrmMedio();
                try {
                    ControladorFrmMedio controladorM = new ControladorFrmMedio(vistaM);
                    vista.dispose();
                } catch (SQLException ex) {
                    System.out.println(e);
                }
            }
        });
        
        /*this.vista.btnNuevaOperacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmNuevaOperacion vistaNO = new FrmNuevaOperacion();
                try {
                    ControladorFrmNuevaOperacion controladorNO = new ControladorFrmNuevaOperacion(vistaNO);
                    vista.dispose();
                } catch (SQLException ex) {
                    System.out.println("Error en nueva operación: "+e);
                }
                
            }
        });
        
        this.vista.btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });*/
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
