package org.jorgerevolorio.controller;

import java.applet.AudioClip;
import java.awt.Color;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;
import org.jorgerevolorio.bean.Login;
import org.jorgerevolorio.bean.Roles;
import org.jorgerevolorio.bean.Usuario;
import org.jorgerevolorio.db.Conexion;
import org.jorgerevolorio.main.Principal;

public class LoginController implements Initializable{
    
    private Principal escenarioPrincipal;
    
    private static int numeroRol;
    
    private ObservableList<Usuario> listaUsuario;
    private ObservableList<Roles> listaRol;
    
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtClave;
    @FXML private TextField txtError;
    @FXML private Button btnLogin;
    @FXML private ImageView imgAdvertencia1;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        txtError.setText("");
        imgAdvertencia1.setImage(null);
        
    }
    
//    String contra = txtClave.getText();
//    String encript = DigestUtils.md5Hex(contra);
//    txtClave.setText(encript);
    
    public ObservableList<Usuario> getUsuario(){
        
        ArrayList<Usuario> lista = new ArrayList <Usuario>();
        try{
            PreparedStatement procedimiento  = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarUsuarios()}");
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
    
    public void verificacion(int conteo){
        
        if(conteo == 1){
            
            this.autorizado();
            
        }else{
            
            this.denegado();
            
        }
        
    }
    
    public void autorizado(){
        
        JOptionPane.showMessageDialog(null, "Acceso Autorizado");
        
    }
    
    public void denegado(){
        
        JOptionPane.showMessageDialog(null, "Acceso Autorizado");
        
    }
    
    
    @FXML 
    private void Sesion(){
        
        Login login = new Login();
        MenuPrincipalController menu = new MenuPrincipalController();
        MenuPrincipalAdministradorController menuAdministrador = new MenuPrincipalAdministradorController();
        BienvenidaController bc = new BienvenidaController();
        BienvenidaAdminController bac = new BienvenidaAdminController();
        int x = 0;
        boolean bandera = false;
        login.setUsuarioMaster(txtUsuario.getText());
        login.setPasswordLogin(txtClave.getText());
        
        while(x < getUsuario().size()){
            
            int codUser = getUsuario().get(x).getCodigoUsuario();
            String name = getUsuario().get(x).getNombreUsuario();
            String lastName = getUsuario().get(x).getApellidoUsuario();
            String user = getUsuario().get(x).getUsuarioLogin();
            String pass = getUsuario().get(x).getClave();
            String rol = String.valueOf(getUsuario().get(x).getCodigoRol());
            
            if(user.equals(login.getUsuarioMaster()) && pass.equals(login.getPasswordLogin())){
                if(rol.equals("3")){
                    
                    bc.verificacionUsuario(name, lastName);
                    escenarioPrincipal.ventanaBienvenida();
                    menuAdministrador.verificacionUsuario(name, lastName, codUser);
                    menu.verificacionUsuario(name, lastName);
                    numeroRol = Integer.parseInt(rol);
                    
                }else if(rol.equals("1")){
                    
                    bac.verificacionUsuario(name, lastName);
                    escenarioPrincipal.ventanaBienvenidaAdmin();
                    menuAdministrador.verificacionUsuario(name, lastName, codUser);
                    menu.verificacionUsuario(name, lastName);
                    numeroRol = Integer.parseInt(rol);
                    
                    
                }else if(rol.equals("2")){
                    
                    bac.verificacionUsuario(name, lastName);
                    escenarioPrincipal.ventanaBienvenidaAdmin();
                    menuAdministrador.verificacionUsuario(name, lastName, codUser);
                    menu.verificacionUsuario(name, lastName);
                    numeroRol = Integer.parseInt(rol);
                    
                }else{
                    
                    bc.verificacionUsuario(name, lastName);
                    escenarioPrincipal.ventanaBienvenida();
                    menuAdministrador.verificacionUsuario(name, lastName, codUser);
                    menu.verificacionUsuario(name, lastName);
                    numeroRol = Integer.parseInt(rol);
                    
                }
                x = getUsuario().size();
                bandera = true;
            }
            
            x++;
            
        }
        
        if(txtUsuario.getText().equals("") && txtClave.getText().equals("")){
                
            imgAdvertencia1.setImage(new Image("/org/jorgerevolorio/image/Warning.png"));
            txtError.setText("Ingrese Usuario y Constraseña");
            bandera = true;
                
        }
        
        if(txtUsuario.getText().equals("")){
                
            imgAdvertencia1.setImage(new Image("/org/jorgerevolorio/image/Warning.png"));
            txtError.setText("Ingrese Usuario");
            bandera = true;
                
        }
        
        if(txtClave.getText().equals("")){
                
            imgAdvertencia1.setImage(new Image("/org/jorgerevolorio/image/Warning.png"));
            txtError.setText("Ingrese Clave");
            bandera = true;
                
        }
        
        if (bandera == false){
            escenarioPrincipal.ventanaDenegado();
            txtUsuario.setText("");
            txtClave.setText("");
            imgAdvertencia1.setImage(new Image("/org/jorgerevolorio/image/Warning.png"));
        }
        
        SesionRol();
        
    }
    
    @FXML
    private void SesionRol(){
        
        MenuPrincipalAdministradorController menuAdministrador = new MenuPrincipalAdministradorController();
        MenuPrincipalController menu = new MenuPrincipalController();
        
        int y = 0;
        
        while(y < getRol().size()){
            
            Integer cRol = getRol().get(y).getCodigoRol();
            String nRol = getRol().get(y).getNombreRol();
            
            if(numeroRol == cRol){
                
                String nombreRol = nRol;
                menuAdministrador.verificacionRol(nombreRol);
                menu.verificacionRol(nombreRol);
                y = getRol().size();
            }
            
            y++;
            
        }
        
    }

    public LoginController() {
    }

    public LoginController(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
    
    public void salir(){
        
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Quieres cerrar la aplicación?", "Cierre Tony's Kinal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if(respuesta == JOptionPane.YES_OPTION){
            
            JOptionPane.showMessageDialog(null, "Gracias por visitar Tony's Kinal");
            System.exit(0);
            
        }
        
    }
    
}
