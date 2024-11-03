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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.lesterlopez.bean.Empleado;
import org.lesterlopez.bean.TipoEmpleado;
import org.lesterlopez.db.Conexion;
import org.lesterlopez.main.Principal;
import org.lesterlopez.report.GenerarReporte;

public class TipoEmpleadoController implements Initializable {

    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    @FXML
    private TextField txtCodigoTipoEmpleado;
    @FXML
    private TextField txtDescripcionTipoEmpleado;
    @FXML
    private TableView tblEmpleados;
    @FXML
    private TableColumn colCodigoTipoEmpleado;
    @FXML
    private TableColumn colDescripcionTipoEmpleado;
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
    private ImageView imgEmpleado;

    private final String Fondo = "/org/lesterlopez/image/FondoReporte.png";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();

        Tooltip tooltip = new Tooltip("Empleados");
        tooltip.install(imgEmpleado, tooltip);
    }

    public void cargarDatos() {
        tblEmpleados.setItems(getTipoEmpleado());
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("codigoTipoEmpleado"));
        colDescripcionTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("descripcion"));
    }

    public ObservableList<TipoEmpleado> getTipoEmpleado() {
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
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
                if (txtDescripcionTipoEmpleado.getText().isEmpty()) {
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
                break;
        }
    }

    public void guardar() {
        TipoEmpleado registro = new TipoEmpleado();
        registro.setDescripcion(txtDescripcionTipoEmpleado.getText());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoEmpleado(?)");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaTipoEmpleado.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seleccionarElemento() {
        if (tipoDeOperacion != operaciones.GUARDAR) {
            try {
                //desactivarControles();
                txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
                txtDescripcionTipoEmpleado.setText(((TipoEmpleado) tblEmpleados.getSelectionModel().getSelectedItem()).getDescripcion());
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
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Desea eliminar el registro?", "Eliminar Tipo Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {

                            int codigoTipoEmpleado = ((TipoEmpleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado();

                            PreparedStatement busqueda = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpleadoTipoEmpleado(?)");
                            busqueda.setInt(1, codigoTipoEmpleado);
                            ResultSet resultado = busqueda.executeQuery();

                            if (resultado.next()) {
                                JOptionPane.showMessageDialog(null, "Error, al tratar de eliminar la información, \neliminar todos los datos que esten relacionados.");
                                tblEmpleados.getSelectionModel().clearSelection();
                                limpiarControles();
                            } else {
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoEmpleado(?)");
                                procedimiento.setInt(1, ((TipoEmpleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                                procedimiento.execute();
                                listaTipoEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        limpiarControles();
                        tblEmpleados.getSelectionModel().clearSelection();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }

        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
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
                if (txtDescripcionTipoEmpleado.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor ingresar datos!");
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
                }
                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoEmpleado(?,?)");
            TipoEmpleado registro = (TipoEmpleado) tblEmpleados.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcionTipoEmpleado.getText());
            procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
            procedimiento.setString(2, registro.getDescripcion());
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
                tblEmpleados.getSelectionModel().clearSelection();
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("CodigoTipoEmpleado", null);
        parametros.put("Fondo", this.getClass().getResourceAsStream(Fondo));
        GenerarReporte.mostrarReporte("ReporteTipoEmpleado.jasper", "Reporte Tipo Empleado", parametros);
    }

    public void activarControles() {
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipoEmpleado.setEditable(true);
    }

    public void desactivarControles() {
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipoEmpleado.setEditable(false);
    }

    public void limpiarControles() {
        txtCodigoTipoEmpleado.clear();
        txtDescripcionTipoEmpleado.clear();
        tblEmpleados.getSelectionModel().clearSelection();
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

    public void ventanaEmpleado() {
        escenarioPrincipal.ventanaEmpleado();
    }

}
