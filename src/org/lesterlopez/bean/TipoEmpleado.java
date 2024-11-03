package org.lesterlopez.bean;

public class TipoEmpleado {

    private int codigoTipoEmpleado;
    private String descripcion;

    public TipoEmpleado() {
    }

    public TipoEmpleado(int codigoTipoEmpleado, String descripcion) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
        this.descripcion = descripcion;
    }

    public TipoEmpleado(int aInt, int aInt0, String string, String string0, String string1, String string2, String string3, int aInt1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getCodigoTipoEmpleado() {
        return codigoTipoEmpleado;
    }

    public void setCodigoTipoEmpleado(int codigoTipoEmpleado) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }   
    
    @Override
    public String toString(){
        return codigoTipoEmpleado + " | " + descripcion; 
    }

}
