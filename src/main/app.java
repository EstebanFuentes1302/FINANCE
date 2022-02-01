/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.formdev.flatlaf.FlatDarkLaf;
import controlador.ControladorFrmLogin;
import general.Sistema;
import java.sql.SQLException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import vista.FrmLogin;

/**
 *
 * @author Esteban
 */
public class app {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     * @throws javax.swing.UnsupportedLookAndFeelException
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    public static void main(String[] args) throws SQLException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        
        UIManager.setLookAndFeel(new FlatDarkLaf());
        
        //TEMA DE SISTEMA OPERATIVO
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        //CONEXION A BASE DE DATOS
        Sistema.conectar();
        
        //INICIO DEL FORMULARIO INICIAL
        FrmLogin vista = new FrmLogin();
        ControladorFrmLogin controlador= new ControladorFrmLogin(vista);
        

    }
    
}
