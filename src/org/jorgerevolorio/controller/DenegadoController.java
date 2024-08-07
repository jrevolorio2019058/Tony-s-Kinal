package org.jorgerevolorio.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.jorgerevolorio.main.Principal;

public class DenegadoController implements Initializable{

    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public DenegadoController() {
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaOpcion(){
        
        escenarioPrincipal.menuLogin();
    
}
    
}
