/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.mysql.jdbc.Connection;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
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
    private String nombre_medio;
    
    private static Connection con;
    private static Statement st;
    private static ResultSet rs;
    
    private static final String driver="com.mysql.jdbc.Driver";
    private static final String user="esteban";
    private static final String pass="123";
    //private static final String url="jdbc:mysql://localhost:3306/ahorros_bd";
    private static final String url="jdbc:mysql://8.12.17.20:3306/finance_bd";
    
    private int CantidadFilas;
    private int CantidadColumnas;
    private int ContadorRegistros;
    private int UbicacionRegistros;
    
    public ControladorFrmMedio(FrmMedio vista) throws SQLException{
        this.vista=vista;
        frmIniciar();
    }
    
    public void conectar() throws SQLException {
        // Reseteamos a null la conexion a la bd
        con=null;
        try{
            Class.forName(driver);
            // Nos conectamos a la bd
            con= (Connection) DriverManager.getConnection(url, user, pass);
            /*if (con!=null){
                System.out.println("conectado");
            }*/
        }
        // Si la conexion NO fue exitosa mostramos un mensaje de error
        catch (ClassNotFoundException | SQLException e){
            System.out.println(e);
        }
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
            st=con.createStatement();
            String sql="SELECT cod_medio FROM medio WHERE nombre_medio='"+vista.cboNombreMedio.getSelectedItem().toString()+"'";
            System.out.println(sql);
            rs=st.executeQuery(sql);
            while(rs.next()){
                vista.txtCodigoMedio.setText(rs.getString("cod_medio"));
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
            //conectar();
            float mt;
            st=con.createStatement();
            ResultSet rs2;
            rs2 = st.executeQuery("SELECT cod_medio from medio WHERE nombre_medio='"+vista.cboNombreMedio.getSelectedItem().toString()+"'");
            if(rs2.next()){
                String sql="select monto_total from medio where cod_medio='"+rs2.getString("cod_medio")+"'";
                rs=st.executeQuery(sql);
                if(rs.next()){
                    vista.txtDineroTotal.setText(Sistema.formatFloat(rs.getFloat("monto_total")));
                }
            }
            
        } catch (Exception e) {
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
        
        //conectar();
        PreparedStatement ps=null;
        ResultSet rsT;
        String sql ="SELECT cod_operacion,monto,cod_medio,descripcion FROM operacion WHERE cod_medio='"+vista.txtCodigoMedio.getText()+"'";
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
    
    
    
    public void ComboBox() throws SQLException{
        DefaultComboBoxModel<String> cboMedios = new DefaultComboBoxModel<String>();
        int cc;
        //conectar();
        st=con.createStatement();
        rs=st.executeQuery("SELECT nombre_medio FROM medio WHERE nombre_usuario='"+Sistema.usuarioConectado.getNombre_usuario()+"'");
        ResultSetMetaData rsMD = rs.getMetaData();
        cc=rsMD.getColumnCount();
        while(rs.next()){
            Object[] row = new Object[cc];
            for (int i = 0; i < cc; i++) {
                cboMedios.addElement(rs.getString("nombre_medio"));
            }
        }
        this.vista.cboNombreMedio.setModel(cboMedios);
    }
    
    private void design() throws SQLException{
        vista.btnNuevaOperacion.setEnabled(false);
        ComboBox();
        accionComboBox();
    }
    
    public void frmIniciar() throws SQLException{conectar();
        Sistema.actualizar_montos_bd();
        conectar();
        design();
        funcionalidades();
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);  
    }
}
