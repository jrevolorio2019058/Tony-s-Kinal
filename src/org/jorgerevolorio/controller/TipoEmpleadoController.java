package org.jorgerevolorio.controller;

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
import org.jorgerevolorio.bean.TipoEmpleado;
import org.jorgerevolorio.db.Conexion;
import org.jorgerevolorio.main.Principal;
import org.jorgerevolorio.report.GenerarReporte;

public class TipoEmpleadoController implements Initializable{
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private Principal escenarioPrincipal;
    
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    
    @FXML private TextField txtCodigoTipoEmpleado;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblTipoEmpleados;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcion;
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
        
    }
    
    public void cargarDatos(){
        
        tblTipoEmpleados.setItems(getTipoEmpleado());
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("codigoTipoEmpleado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("descripcion"));
        
    }
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        
        ArrayList <TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
        
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
                
                if(tblTipoEmpleados.getSelectionModel().getSelectedItem() != null){
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                    if(respuesta == JOptionPane.YES_OPTION){
                        
                        try{
                            
                           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoEmpleado(?)");
                           procedimiento.setInt(1, ((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                           procedimiento.execute();
                           listaTipoEmpleado.remove(tblTipoEmpleados.getSelectionModel().getSelectedIndex());
                           limpiarControles();
                            
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        
                    }
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe Seleccionar un Elemento.");
                }
            
        }
        
    }
    
    public void seleccionarElemento(){
        
        operaciones estado = tipoDeOperacion;
        
        if(estado == tipoDeOperacion.ACTUALIZAR | estado == tipoDeOperacion.GUARDAR){
            
        }else{
            
            if(tblTipoEmpleados.getSelectionModel().getSelectedItem() != null){
                
                txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
                txtDescripcion.setText(((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getDescripcion());
                
            }else{
                
            }
            
        }
        
    }
    
    public void editar(){
        
        switch(tipoDeOperacion){
            
            case NINGUNO:
                
                if(tblTipoEmpleados.getSelectionModel().getSelectedItem() != null){
                    
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
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoEmpleado(?, ?)");
            TipoEmpleado registro = (TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            
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
        Map parametros = new HashMap();
        parametros.put("codigoTipoEmpleado", null);
        GenerarReporte.mostrarReporte("ReporteTipoEmpleado.jasper", "Reporte de Tipo Empleado", parametros);
    }
    
    public void guardar(){
        
        TipoEmpleado registro = new TipoEmpleado();
        
        registro.setDescripcion(txtDescripcion.getText());
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoEmpleado(?)");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaTipoEmpleado.add(registro);
            cargarDatos();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void desactivarControles(){
        
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcion.setEditable(false);
        
    }
    
    public void activarControles(){
        
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcion.setEditable(true);
        
    }
    
    public void limpiarControles(){
        
        txtCodigoTipoEmpleado.setText("");
        txtDescripcion.setText("");
        
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
    
    public void ventanaEmpleados(){
        
        escenarioPrincipal.ventanaEmpleados();
        
    }
    
}
