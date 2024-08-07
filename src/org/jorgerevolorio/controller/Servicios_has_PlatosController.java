package org.jorgerevolorio.controller;

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
import org.jorgerevolorio.bean.Plato;
import org.jorgerevolorio.bean.Servicio;
import org.jorgerevolorio.bean.Servicios_has_Platos;
import org.jorgerevolorio.db.Conexion;
import org.jorgerevolorio.main.Principal;

public class Servicios_has_PlatosController implements Initializable{
    
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO}
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private Principal escenarioPrincipal;
    
    private ObservableList<Servicios_has_Platos> listaServicios_has_Platos;
    private ObservableList<Plato> listaPlato;
    private ObservableList<Servicio>listaServicio;
    
    @FXML private TextField txtServicios_CodigoServicio;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private TableView tblServicios_has_Platos;
    @FXML private TableColumn colServicios_CodigoServicio;
    @FXML private TableColumn colCodigoPlato;
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
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoProducto.setItems(getServicio());
        
        
    }
    
    public void cargarDatos(){
        tblServicios_has_Platos.setItems(getServicios_has_Platos());
        colServicios_CodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos, Integer>("Servicios_codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos, Integer>("codigoPlato"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos, Integer>("codigoServicio"));
    }
    
    public ObservableList<Servicios_has_Platos> getServicios_has_Platos(){
        
        ArrayList<Servicios_has_Platos> lista = new ArrayList<Servicios_has_Platos>();
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_Has_Platos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios_has_Platos(resultado.getInt("Servicios_codigoServicio"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoServicio")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaServicios_has_Platos = FXCollections.observableArrayList(lista);
        
    }
    
    public Plato buscarPlato(int codigoPlato){
        
        Plato resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlato(?)}");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Plato(registro.getInt("codigoPlato"),
                        registro.getInt("cantidad"),
                        registro.getString("nombrePlato"),
                        registro.getString("descripcionPlato"),
                        registro.getDouble("precioPlato"),
                        registro.getInt("codigoTipoPlato"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
        
    }
    
    public ObservableList<Plato> getPlato(){
        
        ArrayList<Plato> lista = new ArrayList<Plato>();
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidad"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPlato = FXCollections.observableArrayList(lista);
        
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
                
                if(tblServicios_has_Platos.getSelectionModel().getSelectedItem() != null){
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el registro", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                    if(respuesta == JOptionPane.YES_OPTION){
                        
                        try{
                            
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio_Has_Plato(?)}");
                            procedimiento.setInt(1, ((Servicios_has_Platos)tblServicios_has_Platos.getSelectionModel().getSelectedItem()).getServicios_codigoServicio());
                            procedimiento.execute();
                            listaServicios_has_Platos.remove(tblServicios_has_Platos.getSelectionModel().getSelectedIndex());
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
                
               if(tblServicios_has_Platos.getSelectionModel().getSelectedItem() != null){
                    
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
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarServicio_Has_Plato (?, ?, ?)}");
            Servicios_has_Platos registro = (Servicios_has_Platos)tblServicios_has_Platos.getSelectionModel().getSelectedItem();
            registro.setServicios_codigoServicio(Integer.parseInt(txtServicios_CodigoServicio.getText()));
            registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
            registro.setCodigoServicio(((Servicio)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoServicio());
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoServicio());
            procedimiento.execute();
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void guardar(){
        
        Servicios_has_Platos registro = new Servicios_has_Platos();
        
        registro.setServicios_codigoServicio(Integer.parseInt(txtServicios_CodigoServicio.getText()));
        registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoServicio(((Servicio)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoServicio());
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio_Has_Plato(?, ?, ?)}");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoServicio());
            procedimiento.execute();
            listaServicios_has_Platos.add(registro);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void seleccionarElemento(){
        
        operaciones estado = tipoDeOperacion;
        
        if(estado == tipoDeOperacion.ACTUALIZAR | estado == tipoDeOperacion.GUARDAR){
            
        }else{
            
            if(tblServicios_has_Platos.getSelectionModel().getSelectedItem() != null){
                
                txtServicios_CodigoServicio.setText(String.valueOf(((Servicios_has_Platos)tblServicios_has_Platos.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
                cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Servicios_has_Platos)tblServicios_has_Platos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
                cmbCodigoProducto.getSelectionModel().select(buscarServicio(((Servicios_has_Platos)tblServicios_has_Platos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
                
            }else{
                
            }
            
        }
        
    }
    
    public void desactivarControles(){
        
        txtServicios_CodigoServicio.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoProducto.setDisable(true);
        
    }
    
    public void activarControles(){
        
        txtServicios_CodigoServicio.setEditable(true);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoProducto.setDisable(false);
        
    }
    
    public void limpiarControles(){
        
        txtServicios_CodigoServicio.setText("");
        cmbCodigoPlato.setValue(null);
        cmbCodigoProducto.setValue(null);
        
    }

    public Servicios_has_PlatosController() {
    }

    public Servicios_has_PlatosController(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
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
