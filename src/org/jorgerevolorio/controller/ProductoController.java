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
import org.jorgerevolorio.bean.Producto;
import org.jorgerevolorio.db.Conexion;
import org.jorgerevolorio.main.Principal;
import org.jorgerevolorio.report.GenerarReporte;

public class ProductoController implements Initializable{
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private Principal escenarioPrincipal;
    
    private ObservableList<Producto> listaProducto;
    
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidadProducto;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidadProducto;
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
        tblProductos.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colCantidadProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidad"));
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
                
                if(tblProductos.getSelectionModel().getSelectedItem() != null){
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                    if(respuesta == JOptionPane.YES_OPTION){
                        
                        try{
                            
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarProducto(?)");
                            procedimiento.setInt(1, ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaProducto.remove(tblProductos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        
                    }
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe Seleccionar un Elemento");
                }
            
        }
        
    }
    
    public void seleccionarElemento(){
        
        operaciones estado = tipoDeOperacion;
        
        if(estado == tipoDeOperacion.ACTUALIZAR | estado == tipoDeOperacion.GUARDAR){
            
        }else{
            
            if(tblProductos.getSelectionModel().getSelectedItem() != null){
                
                txtCodigoProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
                txtNombreProducto.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
                txtCantidadProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
                
            }else{
                
            }
            
        }
        
    }
    
    public void editar(){
        
        switch(tipoDeOperacion){
            
            case NINGUNO:
                
                if(tblProductos.getSelectionModel().getSelectedItem() != null){
                    
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/jorgerevolorio/image/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/jorgerevolorio/image/Cancelar.png"));
                    activarControles();
                    txtCodigoProducto.setEditable(false);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                }else{
                    
                    JOptionPane.showMessageDialog(null, "Debe de Seleccionar un Elemento");
                    
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
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarProducto(?, ?, ?)");
            Producto registro = (Producto)tblProductos.getSelectionModel().getSelectedItem();
            registro.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidad(Integer.parseInt(txtCantidadProducto.getText()));
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidad());
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
        parametros.put("codigoProducto", null);
        GenerarReporte.mostrarReporte("ReporteProducto.jasper", "Reporte Producto", parametros);
        
    }
    
    public void guardar(){
        
        Producto registro = new Producto();
        registro.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));
        registro.setNombreProducto(txtNombreProducto.getText());
        registro.setCantidad(Integer.parseInt(txtCantidadProducto.getText()));
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProducto(?, ?, ?)");
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.execute();
            listaProducto.add(registro);
            cargarDatos();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidadProducto.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoProducto.setEditable(true);
        txtNombreProducto.setEditable(true);
        txtCantidadProducto.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtCantidadProducto.setText("");
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
    
    public void ventanaProductos_has_Platos(){
        escenarioPrincipal.ventanaProductos_has_Platos();
    }
    
}
