/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Esteban
 */
public class Operacion {
    private String cod_operacion;
    private float monto;

    public String getCod_operacion() {
        return cod_operacion;
    }

    public float getMonto() {
        return monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCod_medio() {
        return cod_medio;
    }
    private String descripcion;
    private String cod_medio;

    public Operacion(String cod_operacion, float monto, String descripcion, String cod_medio) {
        this.cod_operacion = cod_operacion;
        this.monto = monto;
        this.descripcion = descripcion;
        this.cod_medio = cod_medio;
    }
    
    
}
