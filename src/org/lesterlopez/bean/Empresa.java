package org.lesterlopez.bean;

public class Empresa {

    //Atributos
    private int codigoEmpresa;
    private String nombreEmpresa;
    private String direccion;
    private String telefono;

    //Constructor sin parámetros
    public Empresa() {
    }

    //Constructor con parámetros
    public Empresa(int codigoEmpresa, String nombreEmpresa, String direccion, String telefono) {
        this.codigoEmpresa = codigoEmpresa;
        this.nombreEmpresa = nombreEmpresa;
        this.direccion = direccion;
        this.telefono = telefono;
    }
    
    //Métodos Getter and Setter
    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    
    //Método toString
    //Se utiliza para convertir un objeto en una cadena de texto
    @Override
    public String toString() {
        return codigoEmpresa + " | " + nombreEmpresa;
    }
    
    


}
