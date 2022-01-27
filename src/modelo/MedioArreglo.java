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
public class MedioArreglo {
    private ArrayList<Medio> medios;
    private int cantidadMedios;
    
    public MedioArreglo() {
        this.medios = new ArrayList<Medio>();
        this.cantidadMedios=0;
    }
    
    public void addMedio(Medio m){
        this.medios.add(m);
        cantidadMedios++;
    }
    
    public Medio getMedio(int i){
        return medios.get(i);
    }
    public int getCantidadMedios() {
        return cantidadMedios;
    }
    
    @Override
    public String toString() {
        return "MedioArreglo{" + "medios=" + medios + ", cantidadMedios=" + cantidadMedios + '}';
    }
    
}
