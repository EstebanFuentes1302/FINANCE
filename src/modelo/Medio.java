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
public class Medio {
    private String cod_medio;
    private float monto_total;
    private String nombre_medio;
    private String descripcion;

    

    public Medio(String cod_medio, float monto_total, String nombre_medio, String descripcion) {
        this.cod_medio = cod_medio;
        this.monto_total = monto_total;
        this.nombre_medio = nombre_medio;
        this.descripcion = descripcion;
    }

    public String getCod_medio() {
        return cod_medio;
    }

    public void setMonto_total(float monto_total) {
        this.monto_total = monto_total;
    }

    public float getMonto_total() {
        return monto_total;
    }

    public String getNombre_medio() {
        return nombre_medio;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return "Medio{" + "cod_medio=" + cod_medio + ", monto_total=" + monto_total + ", nombre_medio=" + nombre_medio + ", descripcion=" + descripcion + '}';
    }
}
