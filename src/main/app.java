/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.bulenkov.darcula.DarculaLaf;
import controlador.ControladorFrmLogin;
import general.Sistema;
import java.sql.SQLException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicLookAndFeel;
import vista.FrmLogin;

/**
 *
 * @author Esteban
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        
        //TEMA OSCURO
        BasicLookAndFeel darcula = new DarculaLaf();
        UIManager.setLookAndFeel(darcula);
        
        //TEMA DE SISTEMA OPERATIVO
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        //CONEXION A BASE DE DATOS
        Sistema.conectar();
        
        //INICIO DEL FORMULARIO INICIAL
        FrmLogin vista = new FrmLogin();
        ControladorFrmLogin controlador= new ControladorFrmLogin(vista);
    }
    
}
