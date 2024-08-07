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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.jorgerevolorio.bean.Roles;
import org.jorgerevolorio.db.Conexion;
import org.jorgerevolorio.main.Principal;

public class RolesController implements Initializable{
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    private Principal escenarioPrincipal;
    
    private ObservableList<Roles> listaRol;
    
    @FXML private TextField txtCodigoRol;
    @FXML private TextField txtNombreRol;
    @FXML private TableColumn colCodigoRol;
    @FXML private TableColumn colNombreRol;
    @FXML private TableView tblRoles;
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
        
        tblRoles.setItems(getRol());
        colCodigoRol.setCellValueFactory(new PropertyValueFactory<Roles, Integer>("codigoRol"));
        colNombreRol.setCellValueFactory(new PropertyValueFactory<Roles, String>("nombreRol"));
        
    }
    
    public ObservableList<Roles> getRol(){
        
        ArrayList<Roles> lista = new ArrayList<Roles>();
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarRoles");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Roles(resultado.getInt("codigoRol"),
                        resultado.getString("nombreRol")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaRol = FXCollections.observableArrayList(lista);
        
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
                
                if(tblRoles.getSelectionModel().getSelectedItem() != null){
                    
                    int verificacion = (((Roles)tblRoles.getSelectionModel().getSelectedItem()).getCodigoRol());
                    
                    if(verificacion == 1){
                        
                        JOptionPane.showMessageDialog(null, "Rol Principal, Borre desde la Base de Datos", "Error", JOptionPane.ERROR_MESSAGE);
                        limpiarControles();
                    
                    }else if(verificacion == 2){
                        
                        JOptionPane.showMessageDialog(null, "Rol Principal, Borre desde la Base de Datos", "Error", JOptionPane.ERROR_MESSAGE);
                        limpiarControles();
                        
                    }else{
                        int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                        if(respuesta == JOptionPane.YES_OPTION){

                            try{

                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarRol(?)");
                                procedimiento.setInt(1, ((Roles)tblRoles.getSelectionModel().getSelectedItem()).getCodigoRol());
                                procedimiento.execute();
                                listaRol.remove(tblRoles.getSelectionModel().getSelectedIndex());
                                limpiarControles();

                            }catch(Exception e){
                                e.printStackTrace();
                            }

                        }
                    }
                    
                }else {
                    JOptionPane.showMessageDialog(null, "Debe Seleccionar un Elemento.");
                }
            
        }
        
    }
    
    public void seleccionarElemento(){
        
        operaciones estado = tipoDeOperacion;
        
        if(estado == tipoDeOperacion.ACTUALIZAR | estado == tipoDeOperacion.GUARDAR){
            
        }else{
            
            if(tblRoles.getSelectionModel().getSelectedItem() != null){
                
                txtCodigoRol.setText(String.valueOf(((Roles)tblRoles.getSelectionModel().getSelectedItem()).getCodigoRol()));
                txtNombreRol.setText(((Roles)tblRoles.getSelectionModel().getSelectedItem()).getNombreRol());
                
            }else{
                
            }
            
        }
        
    }
    
    public void editar(){
        
        switch(tipoDeOperacion){
            
            case NINGUNO:
                
                if(tblRoles.getSelectionModel().getSelectedItem() != null){
                    
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
        
        int verificacion = (((Roles)tblRoles.getSelectionModel().getSelectedItem()).getCodigoRol());
        int codigoMestro = 12042005;
                    
                    if(verificacion == 1){
                        
                        int verificacion2 = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el código Maestro"));
                        
                        if(verificacion2 != codigoMestro){
                            
                            JOptionPane.showMessageDialog(null, "Código Maestro Erroneo\n" + "Contacte a el Jefe de Soporte para brindar este código", "Error", JOptionPane.ERROR_MESSAGE);
                            
                        }else{
                            
                            JOptionPane.showMessageDialog(null, "Código Autorizado\n" + "Editando Rol Principal");
                            try{
            
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarRol(?,?)");
                            Roles registro = (Roles)tblRoles.getSelectionModel().getSelectedItem();
                            registro.setNombreRol(txtNombreRol.getText());
                            procedimiento.setInt(1, registro.getCodigoRol());
                            procedimiento.setString(2, registro.getNombreRol());
                            procedimiento.execute();

                            }catch(Exception e){
                                    e.printStackTrace();
                            }
                        }
                    
                    }else if(verificacion == 2){
                        
                        int verificacion2 = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el código Maestro"));
                        
                        if(verificacion2 != codigoMestro){
                            
                            JOptionPane.showMessageDialog(null, "Código Maestro Erroneo\n" + "Contacte a el Jefe de Soporte para brindar este código", "Error", JOptionPane.ERROR_MESSAGE);
                            
                        }else{
                            
                            JOptionPane.showMessageDialog(null, "Código Autorizado\n" + "Editando Rol Principal");
                            try{
            
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarRol(?,?)");
                            Roles registro = (Roles)tblRoles.getSelectionModel().getSelectedItem();
                            registro.setNombreRol(txtNombreRol.getText());
                            procedimiento.setInt(1, registro.getCodigoRol());
                            procedimiento.setString(2, registro.getNombreRol());
                            procedimiento.execute();

                            }catch(Exception e){
                                    e.printStackTrace();
                            }
                        }
                        
                    }else{

                        try{
            
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarRol(?,?)");
                            Roles registro = (Roles)tblRoles.getSelectionModel().getSelectedItem();
                            registro.setNombreRol(txtNombreRol.getText());
                            procedimiento.setInt(1, registro.getCodigoRol());
                            procedimiento.setString(2, registro.getNombreRol());
                            procedimiento.execute();

                        }catch(Exception e){
                                e.printStackTrace();
                        }
                    }
        
    }
    
    public void guardar(){
        
        Roles registro = new Roles();
        registro.setNombreRol(txtNombreRol.getText());
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarRole(?)");
            procedimiento.setString(1, registro.getNombreRol());
            procedimiento.execute();
            listaRol.add(registro);
            cargarDatos();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void desactivarControles(){
        
        txtCodigoRol.setEditable(false);
        txtNombreRol.setEditable(false);  
        
    }
    
    public void activarControles(){
        
        txtCodigoRol.setEditable(false);
        txtNombreRol.setEditable(true);
        
    }
    
    public void limpiarControles(){
        
        txtCodigoRol.setText("");
        txtNombreRol.setText("");
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipalAdministrador(){
        escenarioPrincipal.menuPrincipalAdministrador();
    }
    
}
