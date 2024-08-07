package org.jorgerevolorio.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.jorgerevolorio.main.Principal;

public class BienvenidaAdminController implements Initializable{

    private Principal escenarioPrincipal;
    private static String nombreUsuario;
    private static String apellidoUsuario;
    
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        txtNombre.setText(nombreUsuario);
        txtApellido.setText(apellidoUsuario);
        
    }

    public BienvenidaAdminController() {
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void verificacionUsuario(String nombre, String apellido){
        
        nombreUsuario = nombre;
        apellidoUsuario = apellido;
        
    }
    
    public void ventanaOpcion(){
        
        escenarioPrincipal.menuPrincipalAdministrador();
    
}
    
}
