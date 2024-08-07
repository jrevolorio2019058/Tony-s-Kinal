package org.jorgerevolorio.controller;

import com.jfoenix.controls.JFXTimePicker;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
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
import org.jorgerevolorio.bean.Empresa;
import org.jorgerevolorio.bean.Servicio;
import org.jorgerevolorio.db.Conexion;
import org.jorgerevolorio.main.Principal;
import org.jorgerevolorio.report.GenerarReporte;

public class ServicioController  implements Initializable{
    
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO}
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private Principal escenarioPrincipal;
    
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empresa> listaEmpresa;
    
    private DatePicker fecha;
    
    @FXML private TextField txtCodigoServicio;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtTelefonoContacto;
    @FXML private GridPane grpFecha;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblServicios;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colFechaServicio;
    @FXML private TableColumn colTipoServicio;
    @FXML private TableColumn colHoraServicio;
    @FXML private TableColumn colLugarServicio;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private JFXTimePicker jfxTimePicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        cargarDatos();
        
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/jorgerevolorio/resource/TonysKinal.css");
        grpFecha.add(fecha, 1, 1);
        cmbCodigoEmpresa.setItems(getEmpresa());
        
        jfxTimePicker.setDisable(true);
        
    }
    
    public void cargarDatos(){
        
        tblServicios.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoEmpresa"));
        
    }
    
    public ObservableList<Servicio> getServicio(){
        
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio"),
                        resultado.getString("tipoServicio"),
                        resultado.getString("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaServicio = FXCollections.observableArrayList(lista);
        
    }
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                        registro.getString("nombreEmpresa"),
                        registro.getString("direccion"),
                        registro.getString("telefono"));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public ObservableList<Empresa> getEmpresa(){
        
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaEmpresa = FXCollections.observableArrayList(lista); 
        
    }
    
    public void seleccionarElemento(){
        
        operaciones estado = tipoDeOperacion;
        
        if(estado == tipoDeOperacion.ACTUALIZAR | estado == tipoDeOperacion.GUARDAR){
            
        }else{
            
            if(tblServicios.getSelectionModel().getSelectedItem() != null){
                
                txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
                fecha.selectedDateProperty().set(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
                txtTipoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio()));
                jfxTimePicker.setValue(LocalTime.parse(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio()));
                txtLugarServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio()));
                txtTelefonoContacto.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
                cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
                
            }else{
                
            }
            
        }
        
    }
    
    public void nuevo(){
        
        switch(tipoDeOperacion){
            
            case NINGUNO:
                
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/jorgerevolorio/image/Guardar.png"));
                imgEliminar.setImage(new Image("/org/jorgerevolorio/image/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                
                break;
                
            case GUARDAR:
                
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/jorgerevolorio/image/Ingresar.png"));
                imgEliminar.setImage(new Image("/org/jorgerevolorio/image/Eliminar.png"));
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
            
        }
        
    }
    
    public void eliminar(){
        
        switch(tipoDeOperacion){
            
            case GUARDAR:
                
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/jorgerevolorio/image/Ingresar.png"));
                imgEliminar.setImage(new Image("/org/jorgerevolorio/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
                
            default:
                
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el registro", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                    if(respuesta == JOptionPane.YES_OPTION){
                        
                        try{
                            
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio(?)}");
                            procedimiento.setInt(1, ((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.execute();
                            listaServicio.remove(tblServicios.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                JOptionPane.showMessageDialog(null, "Deberas Seleccionar un Elemento.");
            } 
        }
    }
    
    public void editar(){
        
        switch(tipoDeOperacion){
            
            case NINGUNO:
                
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                    
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/jorgerevolorio/image/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/jorgerevolorio/image/Cancelar.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                }else{
                    
                    JOptionPane.showMessageDialog(null, "Debe de Seleccionar un Elemento.");
                    
                }
                
                break;
                
            case ACTUALIZAR:
                
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/jorgerevolorio/image/Editar.png"));
                imgReporte.setImage(new Image("/org/jorgerevolorio/image/Listar.png"));
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
            
        }
        
    }
    
    public void actualizar(){
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarServicio(?, ?, ?, ?, ?, ?, ?)}");
            Servicio registro = (Servicio)tblServicios.getSelectionModel().getSelectedItem();
            registro.setFechaServicio(fecha.getSelectedDate());
            registro.setTipoServicio(String.valueOf(txtTipoServicio.getText()));
            registro.setLugarServicio(String.valueOf(txtLugarServicio.getText()));
            registro.setHoraServicio(String.valueOf(jfxTimePicker.getValue()));
            registro.setTelefonoContacto(String.valueOf(txtTelefonoContacto.getText()));
            registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(3, registro.getTipoServicio());
            procedimiento.setString(4, registro.getHoraServicio());
            procedimiento.setString(5, registro.getLugarServicio());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setInt(7, registro.getCodigoEmpresa());
            procedimiento.execute();
        
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void guardar(){
        
        Servicio registro = new Servicio();
        registro.setFechaServicio(fecha.getSelectedDate());
        registro.setTipoServicio(String.valueOf(txtTipoServicio.getText()));
        registro.setLugarServicio(String.valueOf(txtLugarServicio.getText()));
        registro.setHoraServicio(String.valueOf(jfxTimePicker.getValue()));
        registro.setTelefonoContacto(String.valueOf(txtTelefonoContacto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicio(?, ?, ?, ?, ?, ?)");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(2, registro.getTipoServicio());
            procedimiento.setString(3, registro.getHoraServicio());
            procedimiento.setString(4, registro.getLugarServicio());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setInt(6, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaServicio.add(registro);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void generarReporte(){
        
        switch(tipoDeOperacion){
            
            case NINGUNO:
                
                imprimirReporte();
                
                break;
            
            case ACTUALIZAR:
                
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/jorgerevolorio/image/Editar.png"));
                imgReporte.setImage(new Image("/org/jorgerevolorio/image/Listar.png"));
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
            
        }
        
    }
    
    public void imprimirReporte(){
        
        URL subReportePresupuesto = this.getClass().getResource("/org/jorgerevolorio/report/");
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("codEmpresa", codEmpresa);
        parametros.put("subReportePresupuesto", subReportePresupuesto);
        GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte de Presupuesto", parametros);
        
    }
    
    public void desactivarControles(){
        
        txtCodigoServicio.setEditable(true);
        fecha.setDisable(true);
        txtTipoServicio.setEditable(false);
        txtLugarServicio.setEditable(false);
        jfxTimePicker.setDisable(true);
        txtTelefonoContacto.setEditable(false);
        cmbCodigoEmpresa.setEditable(true);
        
    }
    
    public void activarControles(){
        
        txtCodigoServicio.setEditable(false);
        fecha.setDisable(false);
        txtTipoServicio.setEditable(true);
        txtLugarServicio.setEditable(true);
        jfxTimePicker.setDisable(false);
        txtTelefonoContacto.setEditable(true);
        cmbCodigoEmpresa.setEditable(false);
        
    }
    
    public void limpiarControles(){
        
        txtCodigoServicio.setText("");
        fecha.setSelectedDate(null);
        txtTipoServicio.setText("");
        txtLugarServicio.setText("");
        jfxTimePicker.valueProperty().set(null);
        txtTelefonoContacto.setText("");
        cmbCodigoEmpresa.valueProperty().set(null);
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    private static int verificacion;
    
    public void verificacionMenu(int recibido){
        
        verificacion = recibido;
        
        
    }
    
    public void menuPrincipal(){
        if(verificacion == 1){
            escenarioPrincipal.menuPrincipalAdministrador();
        }else{
            escenarioPrincipal.menuPrincipal();
        }
    }
    
    public void ventanaServicios_has_Platos(){
        escenarioPrincipal.ventanaServicios_has_Platos();
    }
    
    public void ventanaServicios_has_Empleados(){
        escenarioPrincipal.ventanaServicios_has_Empleados();
    }
    
}
