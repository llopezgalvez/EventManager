package org.lesterlopez.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.lesterlopez.main.Principal;
import org.lesterlopez.report.GenerarReporte;

public class MenuPrincipalController implements Initializable {

    private Principal  escenarioPrincipal;
    
    private final String Fondo = "/org/lesterlopez/image/FondoReporte.png";
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal(); ;
    }
    
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
    
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    
    public void ventanaProducto(){
        escenarioPrincipal.ventanaProducto();
    }
    
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    
    public void ventanaEmpleado(){
        escenarioPrincipal.ventanaEmpleado();
    }
    
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
    
    public void ventanaServicio(){
        escenarioPrincipal.ventanaServicio();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    public void ventaUsuario(){
        escenarioPrincipal.vetanaUsuario();
    }
    
    public void ventanaProductoHasPlato(){
        escenarioPrincipal.ventanaProductoHasPlato();
    }
    
    public void ventanaServicioHasPlato(){
        escenarioPrincipal.ventanaServicioHasPlato();
    }
    
    public void ventanaServicioHasEmpleado(){
        escenarioPrincipal.ventanaServicioHasEmpleado();
    }
    
    //Reportes
    public void reporteEmpresa(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        parametros.put("Fondo", this.getClass().getResourceAsStream(Fondo));
        GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de Empresas", parametros);
    }
    
    public void reporteEmpleado(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpleado", null);
        parametros.put("Fondo", this.getClass().getResourceAsStream(Fondo));
        GenerarReporte.mostrarReporte("ReporteEmpleado.jasper","Reporte Empleado", parametros);
    }
    
    public void reportePlato() {
        Map parametros = new HashMap();
        parametros.put("codigoPlato", null);
        parametros.put("Fondo", this.getClass().getResourceAsStream(Fondo));
        GenerarReporte.mostrarReporte("ReportePlatos.jasper", "Reporte Platos", parametros);
    }
    
    public void reporteProducto(){
        Map parametros = new HashMap();
        parametros.put("codigoProducto", null);
        parametros.put("Fondo", this.getClass().getResourceAsStream(Fondo));
        GenerarReporte.mostrarReporte("ReporteProductos.jasper", "Reporte Producto", parametros);
    }
    
    public void reporteServicio(){
        Map parametros = new HashMap();
        parametros.put("codigoServicio", null);
        parametros.put("Fondo", this.getClass().getResourceAsStream(Fondo));
        GenerarReporte.mostrarReporte("ReporteServicios.jasper", "Reporte Servicio", parametros);
    }
    
    public void reporteTipoEmpleado(){
        Map parametros = new HashMap();
        parametros.put("CodigoTipoEmpleado", null);
        parametros.put("Fondo", this.getClass().getResourceAsStream(Fondo));
        GenerarReporte.mostrarReporte("ReporteTipoEmpleado.jasper", "Reporte Tipo Empleado", parametros);
    }
    
    public void reporteTipoPlato(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        parametros.put("Fondo", this.getClass().getResourceAsStream(Fondo));
        GenerarReporte.mostrarReporte("ReporteTipoPlato.jasper", "Reporte Tipo Plato", parametros);
    }
  
}
