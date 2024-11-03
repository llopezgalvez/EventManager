package org.lesterlopez.controller;

import com.jfoenix.controls.JFXTimePicker;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.lesterlopez.bean.Empleado;
import org.lesterlopez.bean.Servicio;
import org.lesterlopez.bean.ServicioHasEmpleado;
import org.lesterlopez.db.Conexion;
import org.lesterlopez.main.Principal;

public class ServicioHasEmpleadoController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, NINGUNO, ACTUALIZAR
    }
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ServicioHasEmpleado> listaServiciosHasEmpleado;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empleado> listaEmpleado;
    private DatePicker fecha;
    @FXML
    private GridPane grpFecha;
    @FXML
    private TextField txtServicioCodigoServicio;
    @FXML
    private TextField txtHoraEvento;
    @FXML
    private TextField txtLugarEvento;
    @FXML
    private ComboBox cmbCodigoServicio;
    @FXML
    private ComboBox cmbCodigoEmpleado;
    @FXML
    private TableView tblServicioHasEmpleado;
    @FXML
    private TableColumn colServicioCodigoServicio;
    @FXML
    private TableColumn colCodigoServicio;
    @FXML
    private TableColumn colCodigoEmpleado;
    @FXML
    private TableColumn colFechaEvento;
    @FXML
    private TableColumn colHoraEvento;
    @FXML
    private TableColumn colLugarEvento;
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
    private JFXTimePicker jfxTimePicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(true);
        fecha.getStylesheets().add("/org/lesterlopez/resource/TonysKinal.css");
        grpFecha.add(fecha, 1, 3);
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoEmpleado.setItems(getEmpleado());
        desactivarControles();
    }

    public void cargarDatos() {
        tblServicioHasEmpleado.setItems(getServicios_has_Empleado());
        colServicioCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Integer>("Servicios_codigoServicio"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Integer>("codigoEmpleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Time>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Integer>("lugarEvento"));
    }

    public void seleccionarElemento() {
        txtServicioCodigoServicio.setText(String.valueOf(((ServicioHasEmpleado) tblServicioHasEmpleado.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
        cmbCodigoServicio.getSelectionModel().select(buscarServicio((((ServicioHasEmpleado) tblServicioHasEmpleado.getSelectionModel().getSelectedItem()).getCodigoServicio())));
        cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado((((ServicioHasEmpleado) tblServicioHasEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado())));
        fecha.selectedDateProperty().set(((ServicioHasEmpleado) tblServicioHasEmpleado.getSelectionModel().getSelectedItem()).getFechaEvento());

        tblServicioHasEmpleado.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String horaText = (String.valueOf(((ServicioHasEmpleado) tblServicioHasEmpleado.getSelectionModel().getSelectedItem()).getHoraEvento()));
                try {
                    LocalTime hora = LocalTime.parse(horaText, DateTimeFormatter.ofPattern("HH:mm:ss"));
                    jfxTimePicker.setValue(hora);
                } catch (Exception e) {
                    System.out.println("Error: Formato de hora inválido");
                }
            }
        });

        txtLugarEvento.setText(((ServicioHasEmpleado) tblServicioHasEmpleado.getSelectionModel().getSelectedItem()).getLugarEvento());

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

    public ObservableList<Empleado> getEmpleado() {
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }

    public Empleado buscarEmpleado(int codigoEmpleado) {
        Empleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpleado(?)");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Empleado(registro.getInt("codigoEmpleado"),
                        registro.getInt("numeroEmpleado"),
                        registro.getString("apellidosEmpleado"),
                        registro.getString("nombresEmpleado"),
                        registro.getString("direccionEmpleado"),
                        registro.getString("telefonoContacto"),
                        registro.getString("gradoCocinero"),
                        registro.getInt("codigoTipoEmpleado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public Servicio buscarServicio(int codigoServicio) {
        Servicio resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarServicio(?)");
            procedimiento.setInt(1, codigoServicio);
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

    public ObservableList<ServicioHasEmpleado> getServicios_has_Empleado() {
        ArrayList<ServicioHasEmpleado> lista = new ArrayList<ServicioHasEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServiciosHasEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ServicioHasEmpleado(resultado.getInt("Servicios_codigoServicio"),
                        resultado.getInt("codigoServicio"),
                        resultado.getInt("codigoEmpleado"),
                        resultado.getDate("fechaEvento"),
                        resultado.getTime("horaEvento"),
                        resultado.getString("lugarEvento")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServiciosHasEmpleado = FXCollections.observableArrayList(lista);
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
                if (txtServicioCodigoServicio.getText().isEmpty() || cmbCodigoServicio.getSelectionModel().getSelectedItem() == "" || cmbCodigoEmpleado.getSelectionModel().getSelectedItem() == "" || fecha.selectedDateProperty().getValue() == null || jfxTimePicker.getValue() == null || txtLugarEvento.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresar todos los datos.!");
                } else {
                    try {
                        Integer.parseInt(txtServicioCodigoServicio.getText());
                        try {
                            jfxTimePicker.getValue();
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
                            JOptionPane.showMessageDialog(null, "Verifique la hora");
                        }

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ingrese correctamente Servicio codigo servicio");
                    }

                }
                break;

        }
    }

    public void guardar() {
        ServicioHasEmpleado registro = new ServicioHasEmpleado();
        registro.setServicios_codigoServicio(Integer.parseInt(txtServicioCodigoServicio.getText()));
        registro.setCodigoServicio(((Servicio) cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoEmpleado(((Empleado) cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setHoraEvento(java.sql.Time.valueOf(jfxTimePicker.getValue()));
        registro.setLugarEvento(txtLugarEvento.getText());
        try {
            try {
                colServicioCodigoServicio.getText();
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicioHasEmpleado(?,?,?,?,?,?)");
                procedimiento.setInt(1, registro.getServicios_codigoServicio());
                procedimiento.setInt(2, registro.getCodigoServicio());
                procedimiento.setInt(3, registro.getCodigoEmpleado());
                procedimiento.setDate(4, new java.sql.Date(registro.getFechaEvento().getTime()));
                try {
                    procedimiento.setTime(5, new java.sql.Time(registro.getHoraEvento().getTime()));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresar de la siguiente manera la hora: HH:MM:SS");
                }
                procedimiento.setString(6, registro.getLugarEvento());
                procedimiento.execute();
                listaServiciosHasEmpleado.add(registro);
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
        btnReporte.setDisable(false);
        btnEditar.setDisable(false);
        imgNuevo.setImage(new Image("/org/lesterlopez/image/AgregarIcon.png"));
        imgEliminar.setImage(new Image("/org/lesterlopez/image/EliminarIcon.png"));
        tipoDeOperacion = operaciones.NINGUNO;
        tblServicioHasEmpleado.getSelectionModel().clearSelection();
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblServicioHasEmpleado.getSelectionModel().getSelectedItem() != null) {
                    activarControles();
                    btnEditar.setText("   Actualizar");
                    btnReporte.setText("   Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/lesterlopez/image/actualizar.png"));
                    imgReporte.setImage(new Image("/org/lesterlopez/image/CancelarIcon.png"));
                    cmbCodigoEmpleado.setDisable(true);
                    cmbCodigoServicio.setDisable(true);
                    txtServicioCodigoServicio.setEditable(false);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionnar un elemento.!");
                }
                break;
            case ACTUALIZAR:

                if (txtServicioCodigoServicio.getText().isEmpty() || fecha.selectedDateProperty().getValue() == null || jfxTimePicker.getValue() == null || txtLugarEvento.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresar todos los datos.!");

                } else {
                    try {
                        jfxTimePicker.getValue();
                        actualizar();
                        btnEditar.setText("   Editar");
                        btnReporte.setText("   Reporte");
                        btnNuevo.setDisable(false);
                        btnEliminar.setDisable(false);
                        imgEditar.setImage(new Image("/org/lesterlopez/image/EditarIcon.png"));
                        imgReporte.setImage(new Image("/org/lesterlopez/image/ReporteIcon.png"));
                        cargarDatos();
                        limpiarControles();
                        desactivarControles();
                        tipoDeOperacion = operaciones.NINGUNO;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Verifique la hora");
                    }
                }

                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarServicioHasEmpleado(?,?,?,?)");
            ServicioHasEmpleado registro = (ServicioHasEmpleado) tblServicioHasEmpleado.getSelectionModel().getSelectedItem();
            registro.setFechaEvento(fecha.getSelectedDate());
            try {
                java.sql.Time horaServicio = java.sql.Time.valueOf(jfxTimePicker.getValue());
                registro.setHoraEvento(horaServicio);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Ingrese correctamente la hora");
            }
            registro.setLugarEvento(txtLugarEvento.getText());
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setTime(3, registro.getHoraEvento());
            procedimiento.setString(4, registro.getLugarEvento());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reporte() {
        limpiarControles();
        desactivarControles();
        btnEditar.setText("   Editar");
        btnReporte.setText("   Reporte");
        btnNuevo.setDisable(false);
        btnEliminar.setDisable(false);
        imgEditar.setImage(new Image("/org/lesterlopez/image/EditarIcon.png"));
        imgReporte.setImage(new Image("/org/lesterlopez/image/ReporteIcon.png"));
        tipoDeOperacion = operaciones.NINGUNO;
        tblServicioHasEmpleado.getSelectionModel().clearSelection();
    }

    public void activarControles() {
        txtServicioCodigoServicio.setEditable(true);
        cmbCodigoServicio.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
        fecha.setDisable(false);
        jfxTimePicker.setDisable(false);
        txtLugarEvento.setEditable(true);
    }

    public void desactivarControles() {
        txtServicioCodigoServicio.setEditable(false);
        cmbCodigoServicio.setDisable(true);
        cmbCodigoEmpleado.setDisable(true);
        fecha.setDisable(true);
        jfxTimePicker.setDisable(true);
        txtLugarEvento.setEditable(false);
    }

    public void limpiarControles() {
        txtServicioCodigoServicio.clear();
        cmbCodigoServicio.setValue("");
        cmbCodigoEmpleado.setValue("");
        fecha.selectedDateProperty().set(null);
        jfxTimePicker.setValue(null);
        txtLugarEvento.clear();
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

}
