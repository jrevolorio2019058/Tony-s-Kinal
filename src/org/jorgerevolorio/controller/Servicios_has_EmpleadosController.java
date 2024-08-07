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
import org.jorgerevolorio.bean.Empleado;
import org.jorgerevolorio.bean.Servicio;
import org.jorgerevolorio.bean.Servicios_has_Empleados;
import org.jorgerevolorio.db.Conexion;
import org.jorgerevolorio.main.Principal;

public class Servicios_has_EmpleadosController implements Initializable{
    
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO}
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private Principal escenarioPrincipal;
    
    private ObservableList<Servicios_has_Empleados> listaServicios_has_Empleados;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empleado> listaEmpleado;
    
    private DatePicker fecha;
    
    @FXML private TextField txtServicios_codigoServicio;
    @FXML private TextField txtLugarEvento;
    @FXML private GridPane grpFecha;
    @FXML private JFXTimePicker jfxTimePicker;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private TableView tblServicios_has_Empleados;
    @FXML private TableColumn colServicios_CodigoServicio;
    @FXML private TableColumn colLugarEvento;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colCodigoServicio;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        cargarDatos();
        
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/jorgerevolorio/resource/TonysKinal.css");
        grpFecha.add(fecha, 3, 0);
        jfxTimePicker.setDisable(true);
        fecha.setDisable(true);
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoEmpleado.setItems(getEmpleado());
        
    }
    
    public void cargarDatos(){
        
        tblServicios_has_Empleados.setItems(getServicios_has_Empleados());
        colServicios_CodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Integer>("Servicios_codigoServicio"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, String>("lugarEvento"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, String>("horaEvento"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Integer>("codigoEmpleado"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Integer>("codigoServicio"));
        
    }
    
    public ObservableList<Servicios_has_Empleados> getServicios_has_Empleados(){
        
        ArrayList<Servicios_has_Empleados> lista = new ArrayList<Servicios_has_Empleados>();
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_Has_Empleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios_has_Empleados(resultado.getInt("Servicios_codigoServicio"),
                        resultado.getInt("codigoServicio"),
                        resultado.getInt("codigoEmpleado"),
                        resultado.getDate("fechaEvento"),
                        resultado.getString("horaEvento"),
                        resultado.getString("lugarEvento")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaServicios_has_Empleados = FXCollections.observableArrayList(lista);
        
    }
    
    public Servicio buscarServicio(int codigoServicio){
        
        Servicio resultado = null;
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Servicio(registro.getInt("codigoServicio"),
                        registro.getDate("fechaServicio"),
                        registro.getString("tipoServicio"),
                        registro.getString("horaServicio"),
                        registro.getString("lugarServicio"),
                        registro.getString("telefonoContacto"),
                        registro.getInt("codigoEmpresa"));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
        
    }
    
    public ObservableList<Servicio> getServicio(){
        
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios()}");
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
    
    public Empleado buscarEmpleado(int codigoEmpleado){
        
        Empleado resultado = null;
        
        try{
            
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleado(?)}");
           procedimiento.setInt(1, codigoEmpleado);
           ResultSet registro = procedimiento.executeQuery();
           while(registro.next()){
               
               resultado = new Empleado(registro.getInt("codigoEmpleado"),
                        registro.getInt("numeroEmpleado"),
                        registro.getString("apellidosEmpleado"),
                        registro.getString("nombresEmpleado"),
                        registro.getString("direccionEmpleado"),
                        registro.getString("telefonoContacto"),
                        registro.getString("gradoCocinero"),
                        registro.getInt("codigoTipoEmpleado"));
               
           }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
        
    }
    
    public ObservableList<Empleado> getEmpleado(){
        
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaEmpleado = FXCollections.observableArrayList(lista);
        
    }
    
    public void seleccionarElemento(){
        
        operaciones estado = tipoDeOperacion;
        
        if(estado == tipoDeOperacion.ACTUALIZAR | estado == tipoDeOperacion.GUARDAR){
            
        }else{
            
            if(tblServicios_has_Empleados.getSelectionModel().getSelectedItem() != null){
                
                txtServicios_codigoServicio.setText(String.valueOf(((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
                txtLugarEvento.setText(((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getLugarEvento());
                jfxTimePicker.setValue(LocalTime.parse(((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getHoraEvento()));
                fecha.selectedDateProperty().set(((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getFechaEvento());
                cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
                cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
                
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
                
                if(tblServicios_has_Empleados.getSelectionModel().getSelectedItem() != null){
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el registro", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                    if(respuesta == JOptionPane.YES_OPTION){
                        
                        try{
                            
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio_Has_Empleado(?)}");
                            procedimiento.setInt(1, ((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio());
                            procedimiento.execute();
                            listaServicios_has_Empleados.remove(tblServicios_has_Empleados.getSelectionModel().getSelectedIndex());
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
                
                if(tblServicios_has_Empleados.getSelectionModel().getSelectedItem() != null){
                    
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
    
    public void generarReporte(){
        
        switch(tipoDeOperacion){
            
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
    
    public void actualizar(){
        
        try{
            
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarServicio_Has_Empleado(?, ?, ?, ?, ?, ?)}");
           Servicios_has_Empleados registro = (Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem();
           registro.setServicios_codigoServicio(Integer.parseInt(txtServicios_codigoServicio.getText()));
           registro.setFechaEvento(fecha.getSelectedDate());
           registro.setHoraEvento(String.valueOf(jfxTimePicker.getValue()));
           registro.setLugarEvento(String.valueOf(txtLugarEvento.getText()));
           registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
           registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
           procedimiento.setInt(1, registro.getServicios_codigoServicio());
           procedimiento.setInt(2, registro.getCodigoServicio());
           procedimiento.setInt(3, registro.getCodigoEmpleado());
           procedimiento.setDate(4, new java.sql.Date(registro.getFechaEvento().getTime()));
           procedimiento.setString(5, registro.getHoraEvento());
           procedimiento.setString(6, registro.getLugarEvento());
           procedimiento.execute();
           
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void guardar(){
        
        Servicios_has_Empleados registro = new Servicios_has_Empleados();
        
        registro.setServicios_codigoServicio(Integer.parseInt(txtServicios_codigoServicio.getText()));
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setHoraEvento(String.valueOf(jfxTimePicker.getValue()));
        registro.setLugarEvento(String.valueOf(txtLugarEvento.getText()));
        registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio_Has_Empleado(?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoServicio());
            procedimiento.setInt(3, registro.getCodigoEmpleado());
            procedimiento.setDate(4, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(5, registro.getHoraEvento());
            procedimiento.setString(6, registro.getLugarEvento());
            procedimiento.execute();
            listaServicios_has_Empleados.add(registro);
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void desactivarControles(){
        
        txtServicios_codigoServicio.setEditable(false);
        txtLugarEvento.setEditable(false);
        jfxTimePicker.setDisable(true);
        fecha.setDisable(true);
        cmbCodigoEmpleado.setDisable(true);
        cmbCodigoServicio.setDisable(true);
        
    }
    
    public void activarControles(){
        
        txtServicios_codigoServicio.setEditable(true);
        txtLugarEvento.setEditable(true);
        jfxTimePicker.setDisable(false);
        fecha.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
        cmbCodigoServicio.setDisable(false);
        
    }
    
    public void limpiarControles(){
        
        txtServicios_codigoServicio.setText("");
        txtLugarEvento.setText("");
        jfxTimePicker.setValue(null);
        fecha.setSelectedDate(null);
        cmbCodigoEmpleado.setValue(null);
        cmbCodigoServicio.setValue(null);
        
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
    
}
