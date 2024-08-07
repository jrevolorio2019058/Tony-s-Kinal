package org.jorgerevolorio.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
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
import org.jorgerevolorio.bean.Presupuesto;
import org.jorgerevolorio.db.Conexion;
import org.jorgerevolorio.main.Principal;
import org.jorgerevolorio.report.GenerarReporte;

public class PresupuestoController implements Initializable{
    
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO}
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    private Principal escenarioPrincipal;
    
    private ObservableList<Presupuesto> listaPresupuesto;
    private ObservableList<Empresa> listaEmpresa;
    
    private DatePicker fecha;
    
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    @FXML private GridPane grpFecha;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblPresupuestos;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaDeSolicitudPresupuesto;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colCodEmpresa;
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
        grpFecha.add(fecha, 1, 1);
        cmbCodigoEmpresa.setItems(getEmpresa());
        
    }
    
    public void cargarDatos(){
        
        tblPresupuestos.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));
        colFechaDeSolicitudPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Double>("cantidadPresupuesto"));
        colCodEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa"));
        
    }
    
    public ObservableList<Presupuesto> getPresupuesto(){
        
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPresupuestos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                        resultado.getDate("fechaSolicitud"),
                        resultado.getDouble("cantidadPresupuesto"),
                        resultado.getInt("codigoEmpresa")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPresupuesto = FXCollections.observableArrayList(lista);
        
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
            
            if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                
                txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
                fecha.selectedDateProperty().set(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolicitud());
                txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
                cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
                
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
                
            if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                
               int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el registro", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
               
               if(respuesta == JOptionPane.YES_OPTION){
                   
                   try{
                       
                       PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPresupuesto(?)");
                       procedimiento.setInt(1, ((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                       procedimiento.execute();
                       listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedIndex());
                       limpiarControles();
                       
                   }catch(Exception e){
                       e.printStackTrace();
                   }
                   
               }
            }else {
                JOptionPane.showMessageDialog(null, "Deberas Seleccionar un Elemento.");
            } 
            
        }
        
    }
    
    public void editar(){
        
        switch(tipoDeOperacion){
            
            case NINGUNO:
                
                if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                    
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPresupuesto(?, ?, ?, ?)");
            Presupuesto registro = (Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem();
            registro.setFechaSolicitud(fecha.getSelectedDate());
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
            registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());
            procedimiento.setInt(4, registro.getCodigoEmpresa());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void guardar(){
        
        Presupuesto registro = new Presupuesto();
        
        registro.setFechaSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarPresupuesto(?, ?, ?)");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(2, registro.getCantidadPresupuesto());
            procedimiento.setInt(3, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaPresupuesto.add(registro);
            cargarDatos();
            
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
        
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(false);
        
    }
    
    public void activarControles(){
        
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(true);
    }
    
    public void limpiarControles(){
        
        txtCodigoPresupuesto.setText("");
        fecha.setSelectedDate(null);
        txtCantidadPresupuesto.setText("");
        cmbCodigoEmpresa.setValue(null);
        
        
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
