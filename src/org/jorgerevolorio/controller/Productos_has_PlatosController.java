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
import org.jorgerevolorio.bean.Producto;
import org.jorgerevolorio.bean.Productos_has_Platos;
import org.jorgerevolorio.db.Conexion;
import org.jorgerevolorio.main.Principal;

public class Productos_has_PlatosController implements Initializable{

    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO}
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private Principal escenarioPrincipal;
    
    private ObservableList<Productos_has_Platos> listaProductos_has_Platos;
    private ObservableList<Plato> listaPlato;
    private ObservableList<Producto> listaProducto;
    
    @FXML private TextField txtProducto_CodigoProducto;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private TableView tblProductos_has_Platos;
    @FXML private TableColumn colProducto_CodigoProducto;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colProducto;
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
        cmbCodigoProducto.setItems(getProducto());
        
    }
    
    public void cargarDatos(){
        
        tblProductos_has_Platos.setItems(getProductos_has_Platos());
        colProducto_CodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos, Integer>("Producto_CodigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos, Integer>("codigoPlato"));
        colProducto.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos, Integer>("codigoProducto"));
        
    }
    
    public ObservableList<Productos_has_Platos> getProductos_has_Platos(){
        
        ArrayList <Productos_has_Platos> lista = new ArrayList<Productos_has_Platos>();
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos_Has_Platos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Productos_has_Platos(resultado.getInt("Producto_CodigoProducto"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoProducto")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaProductos_has_Platos = FXCollections.observableArrayList(lista);
        
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
    
    public Producto buscarProducto(int codigoProducto){
        
        Producto resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProductos(?)}");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Producto (registro.getInt("codigoProducto"),
                        registro.getString("nombreProducto"),
                        registro.getInt("cantidad"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
        
    }
    
    public ObservableList<Producto> getProducto(){
        
        ArrayList<Producto> lista = new ArrayList<Producto>();
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto (resultado.getInt("codigoProducto"),
                        resultado.getString("nombreProducto"),
                        resultado.getInt("cantidad")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaProducto = FXCollections.observableArrayList(lista);
        
    }
    
    public void seleccionarElemento(){
        
        operaciones estado = tipoDeOperacion;
        
        if(estado == tipoDeOperacion.ACTUALIZAR | estado == tipoDeOperacion.GUARDAR){
            
        }else{
            
            if(tblProductos_has_Platos.getSelectionModel().getSelectedItem() != null){
                
                txtProducto_CodigoProducto.setText(String.valueOf(((Productos_has_Platos)tblProductos_has_Platos.getSelectionModel().getSelectedItem()).getProducto_CodigoProducto()));
                cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Productos_has_Platos)tblProductos_has_Platos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
                cmbCodigoProducto.getSelectionModel().select(buscarProducto(((Productos_has_Platos)tblProductos_has_Platos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
                
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
                
                if(tblProductos_has_Platos.getSelectionModel().getSelectedItem() != null){
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el registro", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                    if(respuesta == JOptionPane.YES_OPTION){
                    
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProducto_Has_Plato(?)}");
                            procedimiento.setInt(1, ((Productos_has_Platos)tblProductos_has_Platos.getSelectionModel().getSelectedItem()).getProducto_CodigoProducto());
                            procedimiento.execute();
                            listaProductos_has_Platos.remove(tblProductos_has_Platos.getSelectionModel().getSelectedIndex());
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
                
                if(tblProductos_has_Platos.getSelectionModel().getSelectedItem() != null){
                    
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
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarProducto_Has_Plato(?, ?, ?)}");
            Productos_has_Platos registro = (Productos_has_Platos)tblProductos_has_Platos.getSelectionModel().getSelectedItem();
            registro.setProducto_CodigoProducto(Integer.parseInt(txtProducto_CodigoProducto.getText()));
            registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
            registro.setCodigoProducto(((Producto)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
            procedimiento.setInt(1, registro.getProducto_CodigoProducto());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoProducto());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void guardar(){
        
        Productos_has_Platos registro = new Productos_has_Platos();
        
        registro.setProducto_CodigoProducto(Integer.parseInt(txtProducto_CodigoProducto.getText()));
        registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoProducto(((Producto)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProducto_Has_Plato(?, ?, ?)}");
            procedimiento.setInt(1, registro.getProducto_CodigoProducto());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoProducto());
            procedimiento.execute();
            listaProductos_has_Platos.add(registro);
            cargarDatos();
            
        }catch(Exception e){
            e.printStackTrace();
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
    
    public void desactivarControles(){
        
        txtProducto_CodigoProducto.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoProducto.setDisable(true);
        
    }
    
    public void activarControles(){
        
        txtProducto_CodigoProducto.setEditable(true);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoProducto.setDisable(false);
        
    }
    
    public void limpiarControles(){
        
        txtProducto_CodigoProducto.setText("");
        cmbCodigoPlato.setValue(null);
        cmbCodigoProducto.setValue(null);
        
    }

    public Productos_has_PlatosController() {
    }

    public Productos_has_PlatosController(Principal escenarioPrincipal) {
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
