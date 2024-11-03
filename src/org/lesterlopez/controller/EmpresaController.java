package org.lesterlopez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.lesterlopez.bean.Empresa;
import org.lesterlopez.db.Conexion;
import org.lesterlopez.main.Principal;
import org.lesterlopez.report.GenerarReporte;

//Iimplements Initializable sirve para implementar la interfaz grafica de FMXL
public class EmpresaController implements Initializable {

    //Lista de opciones que puede obtener una variable
    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    //Operacion por defecto
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;

    //Listas observables que se utilizarán para almacenar listas de objetos Empresa.
    private ObservableList<Empresa> listaEmpresa;

    @FXML
    private TextField txtCodigoEmpresa;
    @FXML
    private TextField txtNombreEmpresa;
    @FXML
    private TextField txtDireccionEmpresa;
    @FXML
    private TextField txtTelefonoEmpresa;
    @FXML
    private TableView tblEmpresas;
    @FXML
    private TableColumn colCodigoEmpresa;
    @FXML
    private TableColumn colNombreEmpresa;
    @FXML
    private TableColumn colDireccionEmpresa;
    @FXML
    private TableColumn colTelefonoEmpresa;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private ImageView imgPresupuesto;
    @FXML
    private ImageView imgServicio;

    private final String Fondo = "/org/lesterlopez/image/FondoReporte.png";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();

        //Es para que cuando se coloque el puntero encima de la imagen, aparezca el nombre de la ventana
        Tooltip toolPresupuesto = new Tooltip("Presupuesto");
        Tooltip.install(imgPresupuesto, toolPresupuesto);

        Tooltip toolServicio = new Tooltip("Servicios");
        Tooltip.install(imgServicio, toolServicio);
    }

    public void cargarDatos() {
        //Muestra los datos en la tabla
        tblEmpresas.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
        colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
    }

    //Obtener la lista de todas las empresas
    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery(); //Retorna y manda datos
            //.next sirve para continuar mientras hayan datos en existencia
            while (resultado.next()) {
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Castea la lista ObservableList a una lista de tipo ArrayList
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            //Es la accion por defecto
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("   Guardar");
                btnEliminar.setText("   Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/lesterlopez/image/GuardarIcon.png"));
                imgEliminar.setImage(new Image("/org/lesterlopez/image/CancelarIcon.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:

                //isEmpty verifica si una cadena de texto está vacía
                if (txtNombreEmpresa.getText().isEmpty() || txtDireccionEmpresa.getText().isEmpty() || txtTelefonoEmpresa.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresar datos!");
                } else if (txtTelefonoEmpresa.getText().length() != 8) {
                    JOptionPane.showMessageDialog(null, "El número de teléfono debe de contener 8 digítos!");
                } else {
                    String telefono = txtTelefonoEmpresa.getText();
                    int conteo = 0;

                    for (int i = 0; i < telefono.length(); i++) {
                        if (Character.isDigit(telefono.charAt(i))) {
                            conteo++;
                        }
                    }

                    if (conteo == 8) {
                        guardar();
                        limpiarControles();
                        desactivarControles();
                        btnNuevo.setText("   Nuevo");
                        btnEliminar.setText("   Eliminar");
                        btnEditar.setDisable(false);
                        btnReporte.setDisable(false);
                        imgNuevo.setImage(new Image("/org/lesterlopez/image/AgregarIcon.png"));
                        imgEliminar.setImage(new Image("/org/lesterlopez/image/EliminarIcon.png"));
                        tipoDeOperacion = operaciones.NINGUNO;
                        cargarDatos();
                    } else {
                        JOptionPane.showMessageDialog(null, "El número de teléfono solo debe de contener dígitos.");
                    }
                }
                break;
        }
    }

    public void guardar() {
        Empresa registro = new Empresa();
        //registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresa.getText()));
        registro.setNombreEmpresa(txtNombreEmpresa.getText());
        registro.setDireccion(txtDireccionEmpresa.getText());
        registro.setTelefono(txtTelefonoEmpresa.getText());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpresa (?,?,?)");
            procedimiento.setString(1, registro.getNombreEmpresa());
            procedimiento.setString(2, registro.getDireccion());
            procedimiento.setString(3, registro.getTelefono());
            //Enviar datos
            procedimiento.execute();
            //Agrega una nueva empresa
            listaEmpresa.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seleccionarElemento() {
        if (tipoDeOperacion != operaciones.GUARDAR) {
            //Muestra una alerta si el elemento seleccionado no existe
            try {
                txtCodigoEmpresa.setText(String.valueOf(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
                txtNombreEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
                txtDireccionEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
                txtTelefonoEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Seleccione un elemento válido");
            }
        }
    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("   Nuevo");
                btnEliminar.setText("   Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/lesterlopez/image/AgregarIcon.png"));
                imgEliminar.setImage(new Image("/org/lesterlopez/image/EliminarIcon.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                    //Mensaje a mostrar //Nombre de la ventana //Colocar "Si o No" //Colocar el simbolo de interrogación
                    int respuesta = JOptionPane.showConfirmDialog(null, "Desea eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            //Obtenemos el código de la empresa que queremos eliminar
                            int codigoEmpresa = ((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa();

                            //Verificamos si existen registros que tengan el ID de la empresa
                            PreparedStatement busqueda = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPresupuestoEmpresa(?)");
                            busqueda.setInt(1, codigoEmpresa);
                            ResultSet resultado = busqueda.executeQuery();

                            PreparedStatement busquedaEmpresaServicio = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarServicioEmpresa(?)");
                            busquedaEmpresaServicio.setInt(1, codigoEmpresa);
                            ResultSet resultadoEmpresaServicio = busquedaEmpresaServicio.executeQuery();

                            if (resultado.next() || resultadoEmpresaServicio.next()) {
                                JOptionPane.showMessageDialog(null, "Error, al tratar de eliminar la información, \neliminar todos los datos que esten relacionados a la empresa.");
                                tblEmpresas.getSelectionModel().clearSelection();
                                limpiarControles();
                            } else {
                                //Eliminamos la empresa
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpresa(?)");
                                procedimiento.setInt(1, codigoEmpresa);
                                procedimiento.execute();
                                listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        limpiarControles();
                        //clearSelection sirve para deseleccionar las filas o la celda seleccionada
                        tblEmpresas.getSelectionModel().clearSelection();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("   Actualizar");
                    btnReporte.setText("   Cancelar");
                    imgEditar.setImage(new Image("/org/lesterlopez/image/actualizar.png"));
                    imgReporte.setImage(new Image("/org/lesterlopez/image/CancelarIcon.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                if (txtNombreEmpresa.getText().isEmpty() || txtDireccionEmpresa.getText().isEmpty() || txtTelefonoEmpresa.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresar datos!");
                } else if (txtTelefonoEmpresa.getText().length() != 8) {
                    JOptionPane.showMessageDialog(null, "El número de teléfono debe de contener 8 digítos!");
                } else {

                    String telefono = txtTelefonoEmpresa.getText();
                    int conteo = 0;

                    for (int i = 0; i < telefono.length(); i++) {
                        if (Character.isDigit(telefono.charAt(i))) {
                            conteo++;
                        }
                    }

                    if (conteo == 8) {
                        actualizar();
                        limpiarControles();
                        desactivarControles();
                        //Para que todo vuelva como estaba al inicio
                        btnNuevo.setDisable(false);
                        btnEliminar.setDisable(false);
                        btnEditar.setText("   Editar");
                        btnReporte.setText("   Reporte");
                        imgEditar.setImage(new Image("/org/lesterlopez/image/EditarIcon.png"));
                        imgReporte.setImage(new Image("/org/lesterlopez/image/ReporteIcon.png"));
                        //Para mostrar la modificación
                        cargarDatos();
                        tipoDeOperacion = operaciones.NINGUNO;
                    } else {
                        JOptionPane.showMessageDialog(null, "El número de teléfono solo debe de contener dígitos.");
                    }

                }
                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpresa(?,?,?,?)");
            //Registro es un modelo de Empresa
            Empresa registro = (Empresa) tblEmpresas.getSelectionModel().getSelectedItem();
            registro.setNombreEmpresa(txtNombreEmpresa.getText());
            registro.setDireccion(txtDireccionEmpresa.getText());
            registro.setTelefono(txtTelefonoEmpresa.getText());
            procedimiento.setInt(1, registro.getCodigoEmpresa());
            procedimiento.setString(2, registro.getNombreEmpresa());
            procedimiento.setString(3, registro.getDireccion());
            procedimiento.setString(4, registro.getTelefono());
            //executeQuery solo funciona cuando hay un select
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reporte() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                imprimirReporte();
                break;
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("   Editar");
                btnReporte.setText("   Reporte ");
                imgEditar.setImage(new Image("/org/lesterlopez/image/EditarIcon.png"));
                imgReporte.setImage(new Image("/org/lesterlopez/image/ReporteIcon.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                tblEmpresas.getSelectionModel().clearSelection();
                break;
        }
    }

    public void imprimirReporte() {
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        parametros.put("Fondo", this.getClass().getResourceAsStream(Fondo));
        GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de Empresas", parametros);
    }

    public void desactivarControles() {
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccionEmpresa.setEditable(false);
        txtTelefonoEmpresa.setEditable(false);
    }

    public void activarControles() {
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(true);
        txtDireccionEmpresa.setEditable(true);
        txtTelefonoEmpresa.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoEmpresa.setText("");
        txtNombreEmpresa.setText("");
        txtDireccionEmpresa.setText("");
        txtTelefonoEmpresa.setText("");
        tblEmpresas.getSelectionModel().clearSelection();
        //txtCodigoEmpresa.clear();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

    public void ventanaPresupuesto() {
        escenarioPrincipal.ventanaPresupuesto();
    }

    public void ventanaServicio() {
        escenarioPrincipal.ventanaServicio();
    }

}
