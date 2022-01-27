/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.mysql.jdbc.Connection;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import general.GeneradorCodigo;
import general.Sistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import modelo.Medio;
import vista.FrmMedio;
import vista.FrmNuevaOperacion;
import vista.FrmVistaGeneral;

/**
 *
 * @author Esteban
 */
public class ControladorFrmNuevaOperacion {
    private FrmNuevaOperacion vista;

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
    
    public ControladorFrmNuevaOperacion(FrmNuevaOperacion vista) throws SQLException{
        this.vista=vista;
        frmIniciar();
        ComboBox();
        conectar();
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
    
    public void obtenerDatos() throws SQLException{
        try {
            conectar();
            st=con.createStatement();
            rs=st.executeQuery("SELECT COUNT(*) FROM medio");

            if(rs.next()){
                    CantidadFilas=rs.getInt("COUNT(*)");
                }else{
                    CantidadFilas=0;
                }
            rs=st.executeQuery("SELECT * FROM medio");
            ResultSetMetaData rsMD = rs.getMetaData();
            
            CantidadColumnas=rsMD.getColumnCount();
            
            while(rs.next()){           
                System.out.println("a");
                Medio m = new Medio(rs.getObject(1).toString(), Float.parseFloat(rs.getObject(2).toString()), rs.getObject(3).toString(), rs.getObject(4).toString());
                Sistema.medios.addMedio(m);
            }
            System.out.println("Datos obtenidos");
        } catch (Exception e) {
            System.out.println("Error al obtener datos: "+e);
        }
        
        
        
    }
    
    public void ComboBox(){
        DefaultComboBoxModel<String> cboMedios = new DefaultComboBoxModel<String>();
        for (int i = 0; i < CantidadFilas; i++) {
            cboMedios.addElement(Sistema.medios.getMedio(i).getCod_medio());
        }
        this.vista.cboMedios.setModel(cboMedios);
    }
    
    public void afectarUsuario() throws SQLException{
        conectar();
        st=con.createStatement();
        float montob=0, montoa=0;
        String selectm="SELECT dinero_total from usuario where nombre_usuario='"+Sistema.usuarioConectado.getNombre_usuario()+"'";
        rs=st.executeQuery(selectm);
        
        if(rs.next()){
                montob=rs.getFloat("dinero_total");

                montoa=Float.parseFloat(vista.txtMonto.getText())+montob;

                String ex="UPDATE usuario SET dinero_total="+montoa+" WHERE nombre_usuario='"+Sistema.usuarioConectado.getNombre_usuario()+"'";
                System.out.println(ex);
                st.execute(ex);
                System.out.println("se afecto al usuario con exito");
                Sistema.usuarioConectado.setDinero_total(montoa);
                
            }else{
                System.out.println("error en el result set");
            }
    }
    
    public void afectarMedio() throws SQLException{
        
        try {
            conectar();
            st=con.createStatement();
            float montob=0, montoa=0;
            
            String selectm="SELECT monto_total from medio where cod_medio='"+vista.cboMedios.getSelectedItem().toString()+"'";
            rs=st.executeQuery(selectm);
            
            if(rs.next()){
                montob=rs.getFloat("monto_total");

                montoa=Float.parseFloat(vista.txtMonto.getText())+montob;

                String ex="UPDATE medio SET monto_total="+montoa+" WHERE cod_medio='"+vista.cboMedios.getSelectedItem().toString()+"'";
                System.out.println(ex);
                st.execute(ex);
                System.out.println("se afecto al medio con exito");                
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
            conectar();
            String codg=generarCodigoOperacion();
            String result="INSERT INTO operacion VALUES ('"+codg+"',"+vista.txtMonto.getText()+",'"+vista.txtDescripcion.getText()+"','"+vista.cboMedios.getSelectedItem().toString()+"')";
            System.out.println(result);
            st=con.createStatement();
            st.execute(result);
            
            /*afectarMedio();
            afectarUsuario();*/
            
            Sistema.actualizar_montos_bd();
            System.out.println("Se añadió");
            
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
        
        /*this.vista.cboMedios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vista.txtNombreMedio.setText("Nombre: "+Sistema.obtenerNombrexCodigo(vista.cboMedios.getSelectedItem().toString()));
            }
        });*/
    }
    
    public void frmIniciar() throws SQLException{
        obtenerDatos();
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }
}
