package org.jorgerevolorio.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;

import org.jorgerevolorio.bean.Usuario;
import org.jorgerevolorio.db.Conexion;
import org.jorgerevolorio.main.Principal;

public class UsuarioController implements Initializable{
    
    private Principal escenarioPrincipal;
    
    static int comprobacion = 1;
    
    private enum operaciones{GUARDAR, NINGUNO}
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    @FXML private TextField txtCodigoUsuario;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtApellidoUsuario;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtClave;
    @FXML private TextField txtClave2;
    @FXML private TextField txtError;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void nuevo(){
        
        switch(tipoDeOperacion){
            
            case NINGUNO:
                
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/jorgerevolorio/image/Guardar.png"));
                imgEliminar.setImage(new Image("/org/jorgerevolorio/image/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                
                break;
                
            case GUARDAR:
                
                guardar();
                
                if(comprobacion == 1){
                    
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("");
                    imgNuevo.setImage(new Image("/org/jorgerevolorio/image/Ingresar.png"));
                    imgEliminar.setImage(null);
                    tipoDeOperacion = operaciones.NINGUNO;
                    ventanaLogin();
                
                }
                
                break;
            
        }
        
    }
    
    public void eliminar(){
        
        switch(tipoDeOperacion){
            
            case GUARDAR:
                
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("");
                imgNuevo.setImage(new Image("/org/jorgerevolorio/image/Ingresar.png"));
                imgEliminar.setImage(null);
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
            
        }
        
    }
    
    public void guardar(){
        
        if(txtClave.getText().equals(txtClave2.getText())){
            
            comprobacion = 1;
            Usuario registro = new Usuario();
            registro.setNombreUsuario(txtNombreUsuario.getText());
            registro.setApellidoUsuario(txtApellidoUsuario.getText());
            registro.setUsuarioLogin(txtUsuario.getText());
            registro.setClave(txtClave.getText());
            registro.setCodigoRol(3);
        
            try{

                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarUsuario(?, ?, ?, ?, ?)}");
                procedimiento.setString(1, registro.getNombreUsuario());
                procedimiento.setString(2, registro.getApellidoUsuario());
                procedimiento.setString(3, registro.getUsuarioLogin());
                procedimiento.setString(4, registro.getClave());
                procedimiento.setInt(5, registro.getCodigoRol());
                procedimiento.execute();

            }catch(Exception e){
                e.printStackTrace();
            }
            
        }else{
            
            comprobacion = 2;
            txtError.setText("Contraseñas no coinciden");
            txtClave.setText("");
            txtClave2.setText("");
            
            
            
        }
        
    }
    
    public void desactivarControles(){
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtClave.setEditable(false);
        txtClave2.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtClave.setEditable(true);
        txtClave2.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoUsuario.setText("");
        txtNombreUsuario.setText("");
        txtApellidoUsuario.setText("");
        txtUsuario.setText("");
        txtClave.setText("");
        txtClave2.setText("");
    }

    public UsuarioController() {
    }
    
    

    public UsuarioController(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.menuLogin();
    }
    
}
