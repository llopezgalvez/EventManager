package org.lesterlopez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.lesterlopez.bean.Plato;
import org.lesterlopez.bean.Producto;
import org.lesterlopez.bean.ProductoHasPlato;
import org.lesterlopez.db.Conexion;
import org.lesterlopez.main.Principal;

public class ProductoHasPlatoController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, NINGUNO
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ProductoHasPlato> listaProductoHasPlato;
    private ObservableList<Plato> listaPlato;
    private ObservableList<Producto> listaProducto;
    @FXML
    private TextField txtProductoCodigoProducto;
    @FXML
    private ComboBox cmbCodigoPlato;
    @FXML
    private ComboBox cmbCodigoProducto;
    @FXML
    private TableView tblProductoHasPlato;
    @FXML
    private TableColumn colProductoCodigoProducto;
    @FXML
    private TableColumn colCodigoPlato;
    @FXML
    private TableColumn colCodigoProducto;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoProducto.setItems(getProducto());
        desactivarControles();
    }

    public void cargarDatos() {
        tblProductoHasPlato.setItems(getProductohasPlato());
        colProductoCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductoHasPlato, Integer>("Productos_codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ProductoHasPlato, Integer>("codigoPlato"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductoHasPlato, Integer>("codigoProducto"));
    }

    public ObservableList<ProductoHasPlato> getProductohasPlato() {
        ArrayList<ProductoHasPlato> lista = new ArrayList<ProductoHasPlato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductosHasPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ProductoHasPlato(resultado.getInt("Productos_codigoProducto"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoProducto")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProductoHasPlato = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Plato> getPlato() {
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidad"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcion"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
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

    public void seleccionarElemento() {
        try {
            txtProductoCodigoProducto.setText(String.valueOf(((ProductoHasPlato) tblProductoHasPlato.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
            cmbCodigoPlato.getSelectionModel().select(buscarPlato((((ProductoHasPlato) tblProductoHasPlato.getSelectionModel().getSelectedItem()).getCodigoPlato())));
            cmbCodigoProducto.getSelectionModel().select(buscarProducto((((ProductoHasPlato) tblProductoHasPlato.getSelectionModel().getSelectedItem()).getCodigoProducto())));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Seleccionar un elemento válido.!");
        }

    }

    public Producto buscarProducto(int codigoProducto) {
        Producto resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarProducto(?)");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Producto(registro.getInt("codigoProducto"),
                        registro.getString("nombreProducto"),
                        registro.getInt("cantidadProducto"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public Plato buscarPlato(int codigoPlato) {
        Plato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPlato(?)");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Plato(registro.getInt("codigoPlato"),
                        registro.getInt("cantidad"),
                        registro.getString("nombrePlato"),
                        registro.getString("descripcion"),
                        registro.getDouble("precioPlato"),
                        registro.getInt("codigoTipoPlato"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/lesterlopez/image/GuardarIcon.png"));
                imgEliminar.setImage(new Image("/org/lesterlopez/image/CancelarIcon.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if (txtProductoCodigoProducto.getText().isEmpty() || cmbCodigoPlato.getSelectionModel().getSelectedItem() == "" || cmbCodigoProducto.getSelectionModel().getSelectedItem() == "") {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresar datos.!");
                } else {
                    try {
                        Integer.parseInt(txtProductoCodigoProducto.getText());
                        guardar();
                        limpiarControles();
                        desactivarControles();
                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        btnEditar.setDisable(false);
                        btnReporte.setDisable(false);
                        imgNuevo.setImage(new Image("/org/lesterlopez/image/AgregarIcon.png"));
                        imgEliminar.setImage(new Image("/org/lesterlopez/image/EliminarIcon.png"));
                        cargarDatos();
                        tipoDeOperacion = operaciones.NINGUNO;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ingresar correctamente el Producto codigo producto");
                    }

                }
                break;
        }
    }

    public void guardar() {
        ProductoHasPlato registro = new ProductoHasPlato();
        registro.setProductos_codigoProducto(Integer.parseInt(txtProductoCodigoProducto.getText()));
        registro.setCodigoPlato(((Plato) cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoProducto(((Producto) cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        try {
            try {
                colProductoCodigoProducto.getText();
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProductoHasPlato(?,?,?)");
                procedimiento.setInt(1, registro.getProductos_codigoProducto());
                procedimiento.setInt(2, registro.getCodigoPlato());
                procedimiento.setInt(3, registro.getCodigoProducto());
                procedimiento.execute();
                listaProductoHasPlato.add(registro);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "El código que ha ingresado ya existe.!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelar() {
        limpiarControles();
        desactivarControles();
        btnNuevo.setText("   Nuevo");
        btnEliminar.setText("   Eliminar");
        btnEditar.setDisable(false);
        btnReporte.setDisable(false);
        imgNuevo.setImage(new Image("/org/lesterlopez/image/AgregarIcon.png"));
        imgEliminar.setImage(new Image("/org/lesterlopez/image/EliminarIcon.png"));
        tipoDeOperacion = operaciones.NINGUNO;
    }

    public void desactivarControles() {
        txtProductoCodigoProducto.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoProducto.setDisable(true);
    }

    public void activarControles() {
        txtProductoCodigoProducto.setEditable(true);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoProducto.setDisable(false);
    }

    public void limpiarControles() {
        txtProductoCodigoProducto.clear();
        cmbCodigoPlato.setValue("");
        cmbCodigoProducto.setValue("");
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
