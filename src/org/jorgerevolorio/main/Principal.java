/*
Nombre: Jorge Abraham Revolorio Mazariegos
Código Técnico: IN5BM
Carné: 2019058
Fecha de Inicio: 12/04/2023
Fecha de Modificación: 13/04/2023
Fecha de Modificación: 18/04/2023
Fecha de Modificación: 19/04/2023
Fecha de Modificación: 23/04/2023
Fecha de Modificación: 24/04/2023
Fecha de Modificación: 25/04/2023
Fecha de Modificación: 27/04/2023
Fecha de Modificación: 28/04/2023
Fecha de Modificación: 02/05/2023
Fecha de Modificación: 09/05/2023
Fecha de Modificación: 10/05/2023
Fecha de Modificación: 23/05/2023
Fecha de Modificación: 29/05/2023
Fecha de Modificación: 31/05/2023
Fecha de Modificación: 02/06/2023
Fecha de Modificación: 03/06/2023
Fecha de Modificación: 05/06/2023
Fecha de Modificación: 06/06/2023
Fecha de Modificación: 07/06/2023
Fecha de Modificación: 08/06/2023
Fecha de Modificación: 03/07/2023

 */
package org.jorgerevolorio.main;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.jorgerevolorio.controller.BienvenidaAdminController;
import org.jorgerevolorio.controller.BienvenidaController;
import org.jorgerevolorio.controller.DenegadoController;
import org.jorgerevolorio.controller.EmpleadoController;
import org.jorgerevolorio.controller.EmpresaController;
import org.jorgerevolorio.controller.LoginController;
import org.jorgerevolorio.controller.MenuPrincipalAdministradorController;
import org.jorgerevolorio.controller.MenuPrincipalController;
import org.jorgerevolorio.controller.PlatoController;
import org.jorgerevolorio.controller.PresupuestoController;
import org.jorgerevolorio.controller.ProductoController;
import org.jorgerevolorio.controller.Productos_has_PlatosController;
import org.jorgerevolorio.controller.ProgramadorController;
import org.jorgerevolorio.controller.RolesController;
import org.jorgerevolorio.controller.ServicioController;
import org.jorgerevolorio.controller.Servicios_has_EmpleadosController;
import org.jorgerevolorio.controller.Servicios_has_PlatosController;
import org.jorgerevolorio.controller.TipoEmpleadoController;
import org.jorgerevolorio.controller.TipoPlatoController;
import org.jorgerevolorio.controller.UserController;
import org.jorgerevolorio.controller.UsuarioController;
import org.jorgerevolorio.report.GenerarReporte;

public class Principal extends Application {
    
    private final String PAQUETE_VISTA = "/org/jorgerevolorio/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        music();
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal 2023");
        escenarioPrincipal.getIcons().add(new Image("/org/jorgerevolorio/image/Logo2.png"));
        escenarioPrincipal.setResizable(false);
        menuLogin();
        escenarioPrincipal.show();
    }
    
    public void menuLogin(){
        try{
            LoginController login = (LoginController) cambiarEscena("LoginView.fxml", 800, 514);
            login.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void ventanaUsuario(){
        try{
            UsuarioController usuario = (UsuarioController) cambiarEscena("UsuarioView.fxml", 800, 555);
            usuario.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuPrincipal(){
        try{
           MenuPrincipalController menu = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 688,600);
           menu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuPrincipalAdministrador(){
        try{
           MenuPrincipalAdministradorController menu = (MenuPrincipalAdministradorController) cambiarEscena("MenuPrincipalAdministradorView.fxml", 688,600);
           menu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProgramador(){
        try{
          ProgramadorController programador = (ProgramadorController) cambiarEscena("ProgramadorView.fxml", 600, 400);
          programador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpresas(){
        try{
            EmpresaController empresas = (EmpresaController) cambiarEscena("EmpresasView.fxml", 800, 600);
            empresas.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProductos(){
        try{
            ProductoController productos = (ProductoController) cambiarEscena("ProductosView.fxml", 800, 600);
            productos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpleados(){
        try{
            EmpleadoController empleados = (EmpleadoController) cambiarEscena("EmpleadosView.fxml", 1450, 600);
            empleados.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoPlato(){
        try{
            TipoPlatoController tipoPlato = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml", 800, 600);
            tipoPlato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoEmpleado(){
        try{
            TipoEmpleadoController tipoEmpleado = (TipoEmpleadoController) cambiarEscena("TipoEmpleadosView.fxml", 800, 600);
            tipoEmpleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPresupuesto(){
        try{
            PresupuestoController presupuesto = (PresupuestoController) cambiarEscena("PresupuestoView.fxml", 1000, 600);
            presupuesto.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPlato(){
        try{
            PlatoController plato = (PlatoController) cambiarEscena("PlatoView.fxml", 1000, 600);
            plato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaServicio(){
        try{
            ServicioController servicio = (ServicioController) cambiarEscena("ServiciosView.fxml", 1200, 600);
            servicio.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProductos_has_Platos(){
        try{
            Productos_has_PlatosController php = (Productos_has_PlatosController) cambiarEscena("Productos_has_PlatosView.fxml", 800, 600);
            php.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaServicios_has_Platos(){
        
        try{
            Servicios_has_PlatosController shp = (Servicios_has_PlatosController) cambiarEscena("Servicios_has_PlatosView.fxml",800,600);
            shp.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void ventanaServicios_has_Empleados(){
        
        try{
            Servicios_has_EmpleadosController she = (Servicios_has_EmpleadosController) cambiarEscena("Servicios_has_EmpleadosView.fxml", 1000, 600);
            she.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void ventanaRoles(){
        
        try{
            
            RolesController rc = (RolesController) cambiarEscena("RolesView.fxml", 800, 600);
            rc.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void ventanaUser(){
        
        try{
            
            UserController uc = (UserController) cambiarEscena("UserView.fxml", 1200, 600);
            uc.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void ventanaBienvenida(){
        
        try{
            
            BienvenidaController bc = (BienvenidaController) cambiarEscena("BienvenidaView.fxml", 800, 514);
            bc.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void ventanaBienvenidaAdmin(){
        
        try{
            
            BienvenidaAdminController bac = (BienvenidaAdminController) cambiarEscena("BienvenidaAdminView.fxml", 800, 514);
            bac.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void ventanaDenegado(){
        
        try{
            
            DenegadoController dc = (DenegadoController) cambiarEscena("DenegadoView.fxml", 800, 514);
            dc.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    MediaPlayer mediaPlayer;
    public void music(){
        String S = "musicaDeFondo.mp3";
        Media h = new Media(Paths.get(S).toUri().toString());
        mediaPlayer = new MediaPlayer(h);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play();
    }
       
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }
    
}
