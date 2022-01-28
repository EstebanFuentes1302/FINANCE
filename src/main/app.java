/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.bulenkov.darcula.DarculaLaf;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import controlador.ControladorFrmLogin;
import controlador.ControladorFrmMedio;
import controlador.ControladorFrmNuevaOperacion;
import controlador.ControladorFrmVistaGeneral;
import general.GeneradorCodigo;
import general.Sistema;
import java.sql.SQLException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicLookAndFeel;
import vista.FrmLogin;
import vista.FrmMedio;
import vista.FrmNuevaOperacion;
import vista.FrmVistaGeneral;

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
        /*BasicLookAndFeel darcula = new DarculaLaf();
        UIManager.setLookAndFeel(darcula);*/
        
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        Sistema.conectar();
        FrmLogin vista = new FrmLogin();
        ControladorFrmLogin controlador= new ControladorFrmLogin(vista);
    }
    
}
