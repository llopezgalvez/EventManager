 package org.lesterlopez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.lesterlopez.bean.Producto;
import org.lesterlopez.db.Conexion;
import org.lesterlopez.main.Principal;
import org.lesterlopez.report.GenerarReporte;

public class ProductoController implements Initializable {

    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Producto> listaProducto;
    @FXML
    private TextField txtCodigoProducto;
    @FXML
    private TextField txtNombreProducto;
    @FXML
    private TextField txtCantidadProducto;
    @FXML
    private TableView tblProductos;
    @FXML
    private TableColumn colCodigoProducto;
    @FXML
    private TableColumn colNombreProducto;
    @FXML
    private TableColumn colCantidadProducto;
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
    
    private final String Fondo = "/org/lesterlopez/image/FondoReporte.png";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public void cargarDatos() {
        tblProductos.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colCantidadProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidadProducto"));
    }

    public ObservableList<Producto> getProducto() {
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                        resultado.getString("nombreProducto"),
                        resultado.getInt("cantidadProducto")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaProducto = FXCollections.observableArrayList(lista);
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
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
                try {
                    if (txtNombreProducto.getText().isEmpty() || txtCantidadProducto.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Por favor, ingresar datos!");
                    } else {
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
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresar correctamente la cantidad!");
                }

                break;
        }
    }

    public void guardar() {
        Producto registro = new Producto();
        registro.setNombreProducto(txtNombreProducto.getText());
        registro.setCantidadProducto(Integer.parseInt(txtCantidadProducto.getText()));

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProducto(?,?)");
            procedimiento.setString(1, registro.getNombreProducto());
            procedimiento.setInt(2, registro.getCantidadProducto());
            procedimiento.execute();
            listaProducto.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seleccionarElemento() {
        if (tipoDeOperacion != operaciones.GUARDAR) {
            try {
                txtCodigoProducto.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
                txtNombreProducto.setText(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
                txtCantidadProducto.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCantidadProducto()));
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
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Desea eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarProducto(?)");
                            procedimiento.setInt(1, ((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaProducto.remove(tblProductos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        limpiarControles();
                        tblProductos.getSelectionModel().clearSelection();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
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
                if (txtNombreProducto.getText().isEmpty() || txtCantidadProducto.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresar datos!");
                } else {
                    try {
                        //Si el TextField contiene texto que no se pueden convertir en un entero, dara error.
                        Integer.parseInt(txtCantidadProducto.getText());
                        actualizar();
                        limpiarControles();
                        desactivarControles();
                        btnNuevo.setDisable(false);
                        btnEliminar.setDisable(false);
                        btnEditar.setText(" Editar");
                        btnReporte.setText(" Reporte");
                        imgEditar.setImage(new Image("/org/lesterlopez/image/EditarIcon.png"));
                        imgReporte.setImage(new Image("/org/lesterlopez/image/ReporteIcon.png"));
                        cargarDatos();
                        tipoDeOperacion = operaciones.NINGUNO;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Ingrese un valor numérico en la cantidad de producto");
                    }
                }

                /*
                if (txtNombreProducto.getText().isEmpty() || txtCantidadProducto.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresar datos!");
                } else {
                    
                    actualizar();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnEditar.setText("   Editar");
                    btnReporte.setText("   Reporte");
                    imgEditar.setImage(new Image("/org/lesterlopez/image/EditarIcon.png"));
                    imgReporte.setImage(new Image("/org/lesterlopez/image/ReporteIcon.png"));
                    cargarDatos();
                    tipoDeOperacion = operaciones.NINGUNO;
                }*/
                break;

        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarProducto(?,?,?)");
            //Registro es un modelo de Empresa
            Producto registro = (Producto) tblProductos.getSelectionModel().getSelectedItem();
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidadProducto(Integer.parseInt(txtCantidadProducto.getText()));
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidadProducto());
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
                tblProductos.getSelectionModel().clearSelection();
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoProducto", null);
        parametros.put("Fondo", this.getClass().getResourceAsStream(Fondo));
        GenerarReporte.mostrarReporte("ReporteProductos.jasper", "Reporte Producto", parametros);
    }

    public void desactivarControles() {
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidadProducto.setEditable(false);
    }

    public void activarControles() {
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(true);
        txtCantidadProducto.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoProducto.clear();
        txtNombreProducto.clear();
        txtCantidadProducto.clear();
        tblProductos.getSelectionModel().clearSelection();
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

}
