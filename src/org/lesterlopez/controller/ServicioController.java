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
import java.util.HashMap;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.lesterlopez.bean.Empresa;
import org.lesterlopez.bean.Servicio;
import org.lesterlopez.db.Conexion;
import org.lesterlopez.main.Principal;
import org.lesterlopez.report.GenerarReporte;

public class ServicioController implements Initializable {

    private Principal EscenarioPrincipal;

    private enum operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO
    };
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empresa> listaEmpresa;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private DatePicker fecha;
    @FXML
    private TextField txtCodigoServicio;
    @FXML
    private GridPane grpFecha;
    @FXML
    private TextField txtTipoServicio;
    @FXML
    private TextField txtHoraServicio;
    @FXML
    private TextField txtLugarServicio;
    @FXML
    private TextField txtTelefono;
    @FXML
    private ComboBox cmbCodigoEmpresa;
    @FXML
    private TableView tblServicios;
    @FXML
    private TableColumn colCodigoServicio;
    @FXML
    private TableColumn colFechaServicio;
    @FXML
    private TableColumn colTipoServicio;
    @FXML
    private TableColumn colHoraServicio;
    @FXML
    private TableColumn colLugarServicio;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colCodigoEmpresa;
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

    private final String Fondo = "/org/lesterlopez/image/FondoReporte.png";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().setValue("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/lesterlopez/resource/TonysKinal.css");
        grpFecha.add(fecha, 3, 0);
        cmbCodigoEmpresa.setItems(getEmpresa());
        desactivarControles();
    }

    public void cargarDatos() {
        tblServicios.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Time>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("lugarServicio"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoEmpresa"));
    }

    public ObservableList<Servicio> getServicio() {
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio"),
                        resultado.getString("tipoServicio"),
                        resultado.getTime("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElemento() {
        if (tipoDeOperacion != operaciones.GUARDAR) {
            try {
                txtCodigoServicio.setText(String.valueOf(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
                fecha.selectedDateProperty().set(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
                txtTipoServicio.setText(String.valueOf(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio()));

                tblServicios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        String horaText = (String.valueOf(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio()));
                        try {
                            LocalTime hora = LocalTime.parse(horaText, DateTimeFormatter.ofPattern("HH:mm:ss"));
                            jfxTimePicker.setValue(hora);
                        } catch (Exception e) {
                            System.out.println("Error: Formato de hora inválido");
                        }
                    }
                });

                txtLugarServicio.setText(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
                txtTelefono.setText(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
                cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Seleccione un elemento válido");
            }
        } else {

        }
    }

    public Empresa buscarEmpresa(int codEmpresa) {
        Empresa resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa(?)");
            procedimiento.setInt(1, codEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                        registro.getString("nombreEmpresa"),
                        registro.getString("direccion"),
                        registro.getString("telefono"));
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
                btnNuevo.setText("   Guardar");
                btnEliminar.setText("   Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/lesterlopez/image/GuardarIcon.png"));
                imgEliminar.setImage(new Image("/org/lesterlopez/image/CancelarIcon.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if (fecha.selectedDateProperty().getValue() == null || txtTipoServicio.getText().isEmpty() || jfxTimePicker.getValue() == null || txtLugarServicio.getText().isEmpty() || txtTelefono.getText().isEmpty() || cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == "") {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresar datos!");
                } else {
                    try {
                        Integer.parseInt(txtTelefono.getText());
                        try {
                            try {
                                jfxTimePicker.getValue();

                                if (txtTelefono.getText().length() != 8) {
                                    JOptionPane.showMessageDialog(null, "El número de teléfono debe contener 8 digítos");
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
                                JOptionPane.showMessageDialog(null, "Verifique la hora");
                            }
                        } catch (Exception e) {

                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ingrese correctamente el TELÉFONO");
                    }

                    break;
                }
        }
    }

    public void guardar() {
        Servicio registro = new Servicio();
        registro.setFechaServicio(fecha.getSelectedDate());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(java.sql.Time.valueOf(jfxTimePicker.getValue()));
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefono.getText());
        registro.setCodigoEmpresa(((Empresa) cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicio(?,?,?,?,?,?)");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(2, registro.getTipoServicio());
            procedimiento.setTime(3, new java.sql.Time(registro.getHoraServicio().getTime()));
            procedimiento.setString(4, registro.getLugarServicio());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setInt(6, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaServicio.add(registro);
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
                if (tblServicios.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Desea eliminar el registro?", "Eliminar Servicio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicio(?)");
                            procedimiento.setInt(1, ((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.execute();
                            listaServicio.remove(tblServicios.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        limpiarControles();
                        tblServicios.getSelectionModel().clearSelection();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }

        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblServicios.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("   Actualizar");
                    btnReporte.setText("   Cancelar");
                    imgEditar.setImage(new Image("/org/lesterlopez/image/actualizar.png"));
                    imgReporte.setImage(new Image("/org/lesterlopez/image/CancelarIcon.png"));
                    activarControles();
                    cmbCodigoEmpresa.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                if (fecha.selectedDateProperty().getValue() == null || txtTipoServicio.getText().isEmpty() || jfxTimePicker.getValue() == null || txtLugarServicio.getText().isEmpty() || txtTelefono.getText().isEmpty() || cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null) {
                    System.out.println("Fecha: " + (fecha.selectedDateProperty().getValue() == null));
                    System.out.println("Time: " + (jfxTimePicker.getValue() == null));
                    System.out.println("CEmpresa: " + (cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null));
                    System.out.println("Vlor Time: " + jfxTimePicker.getValue());

                    JOptionPane.showMessageDialog(null, "Por favor, ingresar datos!");
                } else {
                    try {
                        Integer.parseInt(txtTelefono.getText());
                        try {
                            try {
                                jfxTimePicker.getValue();

                                if (txtTelefono.getText().length() != 8) {
                                    JOptionPane.showMessageDialog(null, "El número de teléfono debe contener 8 digítos");
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
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Verifique la hora");
                            }
                        } catch (Exception e) {

                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ingrese correctamente el TELÉFONO");
                    }

                    break;
                }
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarServicio(?,?,?,?,?,?)");
            Servicio registro = (Servicio) tblServicios.getSelectionModel().getSelectedItem();
            registro.setFechaServicio(fecha.getSelectedDate());
            registro.setTipoServicio(txtTipoServicio.getText());
            try {
                java.sql.Time horaServicio = java.sql.Time.valueOf(jfxTimePicker.getValue());
                registro.setHoraServicio(horaServicio);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Ingrese correctamente la hora");

            }
            registro.setLugarServicio(txtLugarServicio.getText());
            registro.setTelefonoContacto(txtTelefono.getText());
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(3, registro.getTipoServicio());
            procedimiento.setTime(4, new java.sql.Time(registro.getHoraServicio().getTime()));
            procedimiento.setString(5, registro.getLugarServicio());
            procedimiento.setString(6, registro.getTelefonoContacto());
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
                tblServicios.getSelectionModel().clearSelection();
                break;
        }
    }

    public void imprimirReporte() {
        Map parametros = new HashMap();
        parametros.put("codigoServicio", null);
        parametros.put("Fondo", this.getClass().getResourceAsStream(Fondo));
        GenerarReporte.mostrarReporte("ReporteServicios.jasper", "Reporte Servicio", parametros);

    }

    public void activarControles() {
        txtCodigoServicio.setEditable(false);
        fecha.setDisable(false);
        txtTipoServicio.setEditable(true);
        jfxTimePicker.setDisable(false);
        txtLugarServicio.setEditable(true);
        txtTelefono.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
    }

    public void desactivarControles() {
        txtCodigoServicio.setEditable(false);
        fecha.setDisable(true);
        txtTipoServicio.setEditable(false);
        jfxTimePicker.setDisable(true);
        txtLugarServicio.setEditable(false);
        txtTelefono.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
    }

    public void limpiarControles() {
        txtCodigoServicio.clear();
        fecha.selectedDateProperty().set(null);
        txtTipoServicio.clear();
        jfxTimePicker.setValue(null);
        txtLugarServicio.clear();
        txtTelefono.clear();
        cmbCodigoEmpresa.setValue("");
        tblServicios.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return EscenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal EscenarioPrincipal) {
        this.EscenarioPrincipal = EscenarioPrincipal;
    }

    public void menuPrincipal() {
        EscenarioPrincipal.menuPrincipal();
    }

    public void ventanaEmpresa() {
        EscenarioPrincipal.ventanaEmpresa();
    }

}
