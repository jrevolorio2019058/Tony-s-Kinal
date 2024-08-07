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
import org.jorgerevolorio.bean.Roles;
import org.jorgerevolorio.bean.Usuario;
import org.jorgerevolorio.db.Conexion;
import org.jorgerevolorio.main.Principal;

public class UserController implements Initializable{

    private Principal escenarioPrincipal;
    private static int codigoUsuario;
    private static int estado = 0;
    
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private ObservableList<Usuario> listaUsuario;
    private ObservableList<Roles> listaRol;
    
    @FXML private TextField txtCodigoUsuario;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtApellidoUsuario;
    @FXML private TextField txtUsuarioLogin;
    @FXML private TextField txtClave;
    @FXML private ComboBox cmbCodigoRol;
    @FXML private TableView tblUsuarios;
    @FXML private TableColumn colCodigoUsuario;
    @FXML private TableColumn colNombreUsuario;
    @FXML private TableColumn colApellidoUsuario;
    @FXML private TableColumn colUsuarioLogin;
    @FXML private TableColumn colClave;
    @FXML private TableColumn colRol;
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
        
        cmbCodigoRol.setItems(getRol());
        
    }
    
    public void verificacionUsuario(int codUser){
        
        
        codigoUsuario = codUser;
        
    }
    
    public void cargarDatos(){
        
        tblUsuarios.setItems(getUsuario());
        colCodigoUsuario.setCellValueFactory(new PropertyValueFactory<Usuario,Integer>("codigoUsuario"));
        colNombreUsuario.setCellValueFactory(new PropertyValueFactory<Usuario,String>("nombreUsuario"));
        colApellidoUsuario.setCellValueFactory(new PropertyValueFactory<Usuario,String>("apellidoUsuario"));
        colUsuarioLogin.setCellValueFactory(new PropertyValueFactory<Usuario,String>("usuarioLogin"));
        colClave.setCellValueFactory(new PropertyValueFactory<Usuario,String>("clave"));
        colRol.setCellValueFactory(new PropertyValueFactory<Usuario,Integer>("codigoRol"));
        
    }
    
    public ObservableList<Usuario> getUsuario(){
        
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarUsuarios");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                
                lista.add(new Usuario(resultado.getInt("codigoUsuario"),
                        resultado.getString("nombreUsuario"),
                        resultado.getString("apellidoUsuario"),
                        resultado.getString("usuarioLogin"),
                        resultado.getString("clave"),
                        resultado.getInt("codigoRol")));
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaUsuario = FXCollections.observableArrayList(lista);
        
    }
    
    public Roles buscarRol(int codigoRol){
        
        Roles resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_buscarRol(?)"); 
            procedimiento.setInt(1, codigoRol);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                
                resultado = new Roles(registro.getInt("codigoRol"),
                        registro.getString("nombreRol"));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
        
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
    
    public void seleccionarElemento(){
        
        operaciones estado = tipoDeOperacion;
        
        if(estado == tipoDeOperacion.ACTUALIZAR | estado == tipoDeOperacion.GUARDAR){
            
        }else{
            
            if(tblUsuarios.getSelectionModel().getSelectedItem() != null){
                
                txtCodigoUsuario.setText(String.valueOf(((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getCodigoUsuario()));
                txtNombreUsuario.setText(((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getNombreUsuario());
                txtApellidoUsuario.setText(((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getApellidoUsuario());
                txtUsuarioLogin.setText(((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getUsuarioLogin());
                txtClave.setText(((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getClave());
                cmbCodigoRol.getSelectionModel().select(buscarRol(((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getCodigoRol()));
                
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
                
                if(tblUsuarios.getSelectionModel().getSelectedItem() != null){
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el registro", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                    if(respuesta == JOptionPane.YES_OPTION){
                        
                        try{
                            
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarUsuario(?)");
                            procedimiento.setInt(1, ((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getCodigoUsuario());
                            procedimiento.execute();
                            listaUsuario.remove(tblUsuarios.getSelectionModel().getSelectedIndex());
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
                
                if(tblUsuarios.getSelectionModel().getSelectedItem() != null){
                    
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
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarUsuario(?, ?, ?, ?, ?, ?)");
            Usuario registro = (Usuario)tblUsuarios.getSelectionModel().getSelectedItem();
            registro.setNombreUsuario(txtNombreUsuario.getText());
            registro.setApellidoUsuario(txtApellidoUsuario.getText());
            registro.setUsuarioLogin(txtUsuarioLogin.getText());
            registro.setClave(txtClave.getText());
            registro.setCodigoRol(((Roles)cmbCodigoRol.getSelectionModel().getSelectedItem()).getCodigoRol());
            
            if(codigoUsuario == registro.getCodigoUsuario()){
                
                procedimiento.setInt(1, registro.getCodigoUsuario());
                procedimiento.setString(2, registro.getNombreUsuario());
                procedimiento.setString(3, registro.getApellidoUsuario());
                procedimiento.setString(4, registro.getUsuarioLogin());
                procedimiento.setString(5, registro.getClave());
                procedimiento.setInt(6, registro.getCodigoRol());
                estado = 1;
                procedimiento.execute();
            }else{
                
                procedimiento.setInt(1, registro.getCodigoUsuario());
                procedimiento.setString(2, registro.getNombreUsuario());
                procedimiento.setString(3, registro.getApellidoUsuario());
                procedimiento.setString(4, registro.getUsuarioLogin());
                procedimiento.setString(5, registro.getClave());
                procedimiento.setInt(6, registro.getCodigoRol());
                estado = 0;
                procedimiento.execute();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void guardar(){
        
        Usuario registro = new Usuario();
        registro.setNombreUsuario(txtNombreUsuario.getText());
        registro.setApellidoUsuario(txtApellidoUsuario.getText());
        registro.setUsuarioLogin(txtUsuarioLogin.getText());
        registro.setClave(txtClave.getText());
        registro.setCodigoRol(((Roles)cmbCodigoRol.getSelectionModel().getSelectedItem()).getCodigoRol());
        try{

                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarUsuario(?, ?, ?, ?, ?)}");
                procedimiento.setString(1, registro.getNombreUsuario());
                procedimiento.setString(2, registro.getApellidoUsuario());
                procedimiento.setString(3, registro.getUsuarioLogin());
                procedimiento.setString(4, registro.getClave());
                procedimiento.setInt(5, registro.getCodigoRol());
                procedimiento.execute();
                listaUsuario.add(registro);
                cargarDatos();

            }catch(Exception e){
                e.printStackTrace();
            }
        
    }
    
    public void desactivarControles(){
        
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuarioLogin.setEditable(false);
        txtClave.setEditable(false);
        cmbCodigoRol.setDisable(true);
        
    }
    
    public void activarControles(){
        
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuarioLogin.setEditable(true);
        txtClave.setEditable(true);
        cmbCodigoRol.setDisable(false);
        
    }
    
    public void limpiarControles(){
        
        txtCodigoUsuario.setText("");
        txtNombreUsuario.setText("");
        txtApellidoUsuario.setText("");
        txtUsuarioLogin.setText("");
        txtClave.setText("");
        cmbCodigoRol.setValue(null);
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipalAdministrador(){
        if(estado == 1){
            
            JOptionPane.showMessageDialog(null, "Cerrando Sesión, Usuario Actual Actualizado");
            escenarioPrincipal.menuLogin();
            
            
        }else{
         
            escenarioPrincipal.menuPrincipalAdministrador();
        }
        
    }
    
}
