package org.jorgerevolorio.bean;

public class Roles {
    
    private int codigoRol;
    private String nombreRol;

    public Roles() {
    }

    public Roles(int codigoRol, String nombreRol) {
        this.codigoRol = codigoRol;
        this.nombreRol = nombreRol;
    }

    public int getCodigoRol() {
        return codigoRol;
    }

    public void setCodigoRol(int codigoRol) {
        this.codigoRol = codigoRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    @Override
    public String toString() {
        return codigoRol + " | " + nombreRol;
    }
    
    
    
}
