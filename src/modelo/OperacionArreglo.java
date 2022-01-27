/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author Esteban
 */
public class OperacionArreglo {
    private ArrayList<Operacion> operaciones;
    private int cantidadOperaciones;

    public OperacionArreglo() {
        this.operaciones = new ArrayList<Operacion>();
        cantidadOperaciones=0;
    }
}
