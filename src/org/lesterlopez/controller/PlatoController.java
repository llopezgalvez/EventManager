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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.lesterlopez.bean.Plato;
import org.lesterlopez.bean.TipoPlato;
import org.lesterlopez.db.Conexion;
import org.lesterlopez.main.Principal;
import org.lesterlopez.report.GenerarReporte;

public class PlatoController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        NINGUNO, GUARDAR, ELIMINAR, ACTUALIZAR
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    ObservableList<TipoPlato> listaTipoPlato;
    ObservableList<Plato> listaPlato;
    @FXML
    private TextField txtCodigoPlato;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtNombrePlato;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtPrecioPlato;
    @FXML
    private ComboBox cmbCodigoTipoPlato;
    @FXML
    private TableView tblPlatos;
    @FXML
    private TableColumn colCodigoPlato;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private TableColumn colNombrePlato;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colPrecioPlato;
    @FXML
    private TableColumn colCodigoTipoPlato;
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
        cmbCodigoTipoPlato.setItems(getTipoPlato());
        desactivarControles();
    }

    public void cargarDatos() {
        tblPlatos.setItems(getPlato());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("NombrePlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Plato, String>("descripcion"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Plato, Double>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoTipoPlato"));
    }

    public void seleccionarElemento() {
        if (tipoDeOperacion != operaciones.GUARDAR) {
            try {
                txtCodigoPlato.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
                txtCantidad.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
                txtNombrePlato.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato()));
                txtDescripcion.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getDescripcion()));
                txtPrecioPlato.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
                cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Seleccione un elemento válido");
            }
        }
    }

    public TipoPlato buscarTipoPlato(int codigoTipoPlato) {
        TipoPlato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoPlato(?)");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new TipoPlato(registro.getInt("codigoTipoPlato"),
                        registro.getString("descripcion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public ObservableList<Plato> getPlato() {
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos()");
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

    public ObservableList<TipoPlato> getTipoPlato() {
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                        resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableList(lista);
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
                if (txtCantidad.getText().isEmpty() || txtNombrePlato.getText().isEmpty() || txtDescripcion.getText().isEmpty() || txtPrecioPlato.getText().isEmpty() || cmbCodigoTipoPlato.getSelectionModel().getSelectedItem() == "") {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresar datos!");
                } else {
                    try {
                        Integer.parseInt(txtCantidad.getText());
                        try {
                            Double.parseDouble(txtPrecioPlato.getText());
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
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Ingrese correctamente el precio.!");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ingrese correctamente la cantidad.!");
                    }

                }
                break;
        }
    }

    public void guardar() {
        Plato registro = new Plato();
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setNombrePlato(txtNombrePlato.getText());
        registro.setDescripcion(txtDescripcion.getText());
        registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
        registro.setCodigoTipoPlato(((TipoPlato) cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarPlato(?,?,?,?,?)");
            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setString(2, registro.getNombrePlato());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setDouble(4, registro.getPrecioPlato());
            procedimiento.setInt(5, registro.getCodigoTipoPlato());
            procedimiento.execute();
            listaPlato.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
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
                if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Desea eliminar el registro?", "Eliminar Plato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPlato(?)");
                            procedimiento.setInt(1, ((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                            procedimiento.execute();
                            listaPlato.remove(tblPlatos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        limpiarControles();
                        tblPlatos.getSelectionModel().clearSelection();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("   Actualizar");
                    btnReporte.setText("   Cancelar");
                    imgEditar.setImage(new Image("/org/lesterlopez/image/actualizar.png"));
                    imgReporte.setImage(new Image("/org/lesterlopez/image/CancelarIcon.png"));
                    activarControles();
                    cmbCodigoTipoPlato.setDisable(true);
                    //tblPlatos.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                if (txtCantidad.getText().isEmpty() || txtNombrePlato.getText().isEmpty() || txtDescripcion.getText().isEmpty() || txtPrecioPlato.getText().isEmpty() || cmbCodigoTipoPlato.getSelectionModel().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresar datos!");
                } else {
                    try {
                        Integer.parseInt(txtCantidad.getText());

                        try {
                            Double.parseDouble(txtPrecioPlato.getText());
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
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Ingresar correctamente el precio.!");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ingresar correctamente la cantidad.!");
                    }
                }
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPlato(?,?,?,?,?)");
            Plato registro = (Plato) tblPlatos.getSelectionModel().getSelectedItem();
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setNombrePlato(txtNombrePlato.getText());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
            registro.setCodigoTipoPlato(((TipoPlato) cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
            procedimiento.setInt(1, registro.getCodigoPlato());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setString(3, registro.getNombrePlato());
            procedimiento.setString(4, registro.getDescripcion());
            procedimiento.setDouble(5, registro.getPrecioPlato());
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
                tblPlatos.getSelectionModel().clearSelection();
                break;
        }
    }

    public void imprimirReporte() {
        Map parametros = new HashMap();
        parametros.put("codigoPlato", null);
        parametros.put("Fondo", this.getClass().getResourceAsStream(Fondo));
        GenerarReporte.mostrarReporte("ReportePlatos.jasper", "Reporte Platos", parametros);
    }

    public void activarControles() {
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDescripcion.setEditable(true);
        txtPrecioPlato.setEditable(true);
        cmbCodigoTipoPlato.setDisable(false);
    }

    public void desactivarControles() {
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDescripcion.setEditable(false);
        txtPrecioPlato.setEditable(false);
        cmbCodigoTipoPlato.setDisable(true);
    }

    public void limpiarControles() {
        txtCodigoPlato.clear();
        txtCantidad.clear();
        txtNombrePlato.clear();
        txtDescripcion.clear();
        txtPrecioPlato.clear();
        cmbCodigoTipoPlato.setValue("");
        tblPlatos.getSelectionModel().clearSelection();
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

    public void ventanaTipoPlato() {
        escenarioPrincipal.ventanaTipoPlato();
    }

}
