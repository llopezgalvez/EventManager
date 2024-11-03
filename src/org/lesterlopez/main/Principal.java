/*
    Lester René López Gálvez
    IN5AV 
    2022064

    Inicio: 28/03/2023
    Modificaciones: 28/03/2023, 11/04/2023, 12/04/2023, 17/04/2023, 18/04/2023,
        19/04/2023, 22/04/2023, 23/04/2023, 24/04/2023, 25/04/2023, 02/05/2023,
        03/05/2023, 08/05/2022, 09/05/2022, 10/05/2022
 */
package org.lesterlopez.main;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.lesterlopez.bean.ServicioHasEmpleado;
import org.lesterlopez.controller.EmpleadoController;
import org.lesterlopez.controller.EmpresaController;
import org.lesterlopez.controller.LoginController;
import org.lesterlopez.controller.MenuPrincipalController;
import org.lesterlopez.controller.PlatoController;
import org.lesterlopez.controller.PresupuestoController;
import org.lesterlopez.controller.ProductoController;
import org.lesterlopez.controller.ProductoHasPlatoController;
import org.lesterlopez.controller.ProgramadorController;
import org.lesterlopez.controller.ServicioController;
import org.lesterlopez.controller.ServicioHasEmpleadoController;
import org.lesterlopez.controller.ServiciosHasPlatosController;
import org.lesterlopez.controller.TipoEmpleadoController;
import org.lesterlopez.controller.TipoPlatoController;
import org.lesterlopez.controller.UsuarioController;

/**
 *
 * @author informatica
 */

/*Esta clase se encarga de controlar la navegación entre las distintas ventanas 
y escenas de la aplicación.*/
public class Principal extends Application { //Extiende la clase Application, lo que indica que es una aplicación JavaFX

    private final String PAQUETE_VISTA = "/org/lesterlopez/view/";
    private Stage escenarioPrincipal;
    private Scene escena;

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("Tony's Kinal 2023");
        escenarioPrincipal.getIcons().add(new Image("/org/lesterlopez/image/Logo2022064.png"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/lesterlopez/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        
        //La ventana que querramos abrir
        ventanaLogin();
        //menuPrincipal();
        escenarioPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void menuPrincipal() {
        try {
            MenuPrincipalController menu = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 416, 423);
            menu.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ventanaProgramador() {
        try {
            ProgramadorController progra = (ProgramadorController) cambiarEscena("ProgramadorView.fxml", 654, 539);
            progra.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmpresa() {
        try {
            EmpresaController empresa = (EmpresaController) cambiarEscena("EmpresaView.fxml", 790, 467);
            empresa.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaPresupuesto() {
        try {
            PresupuestoController presupuestoController = (PresupuestoController) cambiarEscena("PresupuestoView.fxml", 797, 439);
            presupuestoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServicio(){
        try{
            ServicioController servicioContro = (ServicioController) cambiarEscena("ServicioView.fxml", 929, 462);
            servicioContro.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    

    public void ventanaTipoEmpleado() {
        try {
            TipoEmpleadoController TipoEmpleado = (TipoEmpleadoController) cambiarEscena("TipoEmpleadoView.fxml", 797, 468);
            TipoEmpleado.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmpleado() {
        try {
            EmpleadoController empleadoController = (EmpleadoController) cambiarEscena("EmpleadoView.fxml", 1237, 559);
            empleadoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaTipoPlato() {
        try {
            TipoPlatoController plato = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml", 797, 508);
            plato.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaPlato(){
        try{
            PlatoController platoContro = (PlatoController) cambiarEscena ("PlatoView.fxml",846,439);
            platoContro.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void ventanaProducto() {
        try {
            ProductoController producto = (ProductoController) cambiarEscena("ProductoView.fxml", 797, 439);
            producto.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaLogin(){
        try{
            LoginController loginController = (LoginController) cambiarEscena("LoginView.fxml",721,411);
            loginController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void vetanaUsuario(){
        try{
            UsuarioController usuarioController = (UsuarioController) cambiarEscena("UsuarioView.fxml", 629, 439);
            usuarioController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProductoHasPlato(){
        try{
            ProductoHasPlatoController productohasplato = (ProductoHasPlatoController) cambiarEscena("ProductoHasPlatoView.fxml", 797, 439);
            productohasplato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaServicioHasPlato(){
        try{
            ServiciosHasPlatosController serviciohasplato = (ServiciosHasPlatosController) cambiarEscena("ServicioHasPlatoView.fxml", 797, 439);
            serviciohasplato.setEscenarioPirncipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaServicioHasEmpleado(){
        try{
            ServicioHasEmpleadoController serviciohasempleado = (ServicioHasEmpleadoController) cambiarEscena("ServicioHasEmpleadoView.fxml",905,439);
            serviciohasempleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    

    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));
        escena = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;
    }

}
