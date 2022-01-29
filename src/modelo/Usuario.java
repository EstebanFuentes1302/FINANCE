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
public class Usuario {
    private String nombre_usuario;
    private String password;
    private float dinero_total;
    private boolean conectado;

    public Usuario(String nombre_usuario, String password, float dinero_total) {
        this.nombre_usuario = nombre_usuario;
        this.password = password;
        this.dinero_total=dinero_total;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }
    
    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setDinero_total(float dinero_total) {
        this.dinero_total = dinero_total;
    }


    public String getPassword() {
        return password;
    }

    public float getDinero_total() {
        return dinero_total;
    }

    
}
