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
import org.lesterlopez.bean.Servicio;
import org.lesterlopez.bean.ServicioHasPlato;
import org.lesterlopez.db.Conexion;
import org.lesterlopez.main.Principal;

public class ServiciosHasPlatosController implements Initializable {

    private Principal escenarioPirncipal;

    private enum operaciones {
        GUARDAR, ELIMINAR, NINGUNO
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ServicioHasPlato> listaServicioHasPlato;
    private ObservableList<Plato> listaPlato;
    private ObservableList<Servicio> listaServicio;
    @FXML
    private TextField txtCodigoServicioHasPlato;
    @FXML
    private ComboBox cmbCodigoPlato;
    @FXML
    private ComboBox cmbCodigoServicio;
    @FXML
    private TableView tblServiciosHasPlatos;
    @FXML
    private TableColumn colCodigoServicioHasPlato;
    @FXML
    private TableColumn colCodigoPlato;
    @FXML
    private TableColumn colCodigoServicio;
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
        cmbCodigoServicio.setItems(getServicio());
        desactivarControles();
    }

    public void cargarDatos() {
        tblServiciosHasPlatos.setItems(getServicioPlato());
        colCodigoServicioHasPlato.setCellValueFactory(new PropertyValueFactory<ServicioHasPlato, Integer>("Servicios_codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ServicioHasPlato, Integer>("codigoPlato"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioHasPlato, Integer>("codigoServicio"));
    }

    public ObservableList<ServicioHasPlato> getServicioPlato() {
        ArrayList<ServicioHasPlato> lista = new ArrayList<ServicioHasPlato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServiciosHasPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ServicioHasPlato(resultado.getInt("Servicios_codigoServicio"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoServicio")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaServicioHasPlato = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElemento() {
        if (tipoDeOperacion != operaciones.GUARDAR) {
            try {
                txtCodigoServicioHasPlato.setText(String.valueOf(((ServicioHasPlato) tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
                cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ServicioHasPlato) tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
                cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServicioHasPlato) tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Seleccione un elemento válido");
            }
        }
    }

    public Plato buscarPlato(int codPlato) {
        Plato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPlato(?)");
            procedimiento.setInt(1, codPlato);
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

    public Servicio buscarServicio(int codServicio) {
        Servicio resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarServicio(?)");
            procedimiento.setInt(1, codServicio);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Servicio(registro.getInt("codigoServicio"),
                        registro.getDate("fechaServicio"),
                        registro.getString("tipoServicio"),
                        registro.getTime("horaServicio"),
                        registro.getString("lugarServicio"),
                        registro.getString("telefonoContacto"),
                        registro.getInt("codigoEmpresa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public ObservableList<Servicio> getServicio() {
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio"),
                        resultado.getString("tipoServicio"),
                        resultado.getTime("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(lista);
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
                if (txtCodigoServicioHasPlato.getText().isEmpty() || cmbCodigoPlato.getSelectionModel().getSelectedItem() == "" || cmbCodigoServicio.getSelectionModel().getSelectedItem() == "") {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresar todos los datos.!");
                } else {
                    try {
                        Integer.parseInt(txtCodigoServicioHasPlato.getText());
                        guardar();
                        limpiarControles();
                        desactivarControles();
                        btnNuevo.setText("   Nuevo");
                        btnEliminar.setText("   Eliminar");
                        btnEditar.setDisable(false);
                        btnReporte.setDisable(false);
                        imgNuevo.setImage(new Image("/org/lesterlopez/image/AgregarIcon.png"));
                        imgEliminar.setImage(new Image("/org/lesterlopez/image/EliminarIcon.png"));
                        cargarDatos();
                        tipoDeOperacion = operaciones.NINGUNO;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ingrese correctamente Servicio codigo servicio  ");
                    }

                }
                break;
        }
    }

    public void guardar() {
        ServicioHasPlato registro = new ServicioHasPlato();
        registro.setServicios_codigoServicio(Integer.parseInt(txtCodigoServicioHasPlato.getText()));
        registro.setCodigoPlato(((Plato) cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoServicio(((Servicio) cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        try {
            try {
                colCodigoServicioHasPlato.getText();
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicioHasPlato(?,?,?)");
                procedimiento.setInt(1, registro.getServicios_codigoServicio());
                procedimiento.setInt(2, registro.getCodigoPlato());
                procedimiento.setInt(3, registro.getCodigoServicio());
                procedimiento.execute();
                listaServicioHasPlato.add(registro);

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
        txtCodigoServicioHasPlato.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoServicio.setDisable(true);
    }

    public void activarControles() {
        txtCodigoServicioHasPlato.setEditable(true);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoServicio.setDisable(false);
    }

    public void limpiarControles() {
        txtCodigoServicioHasPlato.clear();
        cmbCodigoPlato.setValue("");
        cmbCodigoServicio.setValue("");
    }

    public void memuPrincipal() {
        escenarioPirncipal.menuPrincipal();
    }

    public Principal getEscenarioPirncipal() {
        return escenarioPirncipal;
    }

    public void setEscenarioPirncipal(Principal escenarioPirncipal) {
        this.escenarioPirncipal = escenarioPirncipal;
    }

}
