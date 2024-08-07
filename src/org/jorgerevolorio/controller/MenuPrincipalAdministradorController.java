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
import javafx.fxml.Initializable;
import javax.swing.JOptionPane;
import org.jorgerevolorio.bean.Usuario;
import org.jorgerevolorio.db.Conexion;
import org.jorgerevolorio.main.Principal;
import org.jorgerevolorio.report.GenerarReporte;

public class MenuPrincipalAdministradorController implements Initializable{
    
    private Principal escenarioPrincipal;
    private static int codigoUsuario;
    private static String nombreUsuario;
    private static String apellidoUsuario;
    private static String nombreRol;
    private static int recibido = 1;
    
    RolesController rController = new RolesController();
    UserController uController = new UserController();
    EmpresaController eController = new EmpresaController();
    EmpleadoController emController = new EmpleadoController();
    PlatoController pController = new PlatoController();
    PresupuestoController prController = new PresupuestoController();
    ProductoController proController = new ProductoController();
    Productos_has_PlatosController php = new Productos_has_PlatosController();
    ProgramadorController progra = new ProgramadorController();
    ServicioController servicio = new ServicioController();
    Servicios_has_EmpleadosController she = new Servicios_has_EmpleadosController();
    Servicios_has_PlatosController shp = new Servicios_has_PlatosController();
    TipoEmpleadoController te = new TipoEmpleadoController();
    TipoPlatoController tp = new TipoPlatoController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaProgramador(){
        progra.verificacionMenu(recibido);
        escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaEmpresas(){
        eController.verificacionMenu(recibido);
        escenarioPrincipal.ventanaEmpresas();
    }
    
    public void ventanaProductos(){
        proController.verificacionMenu(recibido);
        escenarioPrincipal.ventanaProductos();
    }
    
    public void ventanaEmpleados(){
        emController.verificacionMenu(recibido);
        escenarioPrincipal.ventanaEmpleados();
    }
    
    public void ventanaTipoPlato(){
        tp.verificacionMenu(recibido);
        escenarioPrincipal.ventanaTipoPlato();
    }
    
    public void ventanaTipoEmpleado(){
        te.verificacionMenu(recibido);
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    
    public void ventanaPresupuesto(){
        prController.verificacionMenu(recibido);
        escenarioPrincipal.ventanaPresupuesto();
    }
    
    public void ventanaPlato(){
        pController.verificacionMenu(recibido);
        escenarioPrincipal.ventanaPlato();
    }
    
    public void ventanaServicio(){
        servicio.verificacionMenu(recibido);
        escenarioPrincipal.ventanaServicio();
    }
    
    public void ventanaProductos_has_Platos(){
        php.verificacionMenu(recibido);
        escenarioPrincipal.ventanaProductos_has_Platos();
    }
    
    public void ventanaServicios_has_Platos(){
        shp.verificacionMenu(recibido);
        escenarioPrincipal.ventanaServicios_has_Platos();
    }
    
    public void ventanaServicios_has_Empleados(){
        she.verificacionMenu(recibido);
        escenarioPrincipal.ventanaServicios_has_Empleados();
    }
    
    public void login(){
        
        JOptionPane.showMessageDialog(null, "Cerrando Sesión");
        
        escenarioPrincipal.menuLogin();
    }
    
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
    
    public void ventanaUser(){
        
        escenarioPrincipal.ventanaUser();  
        
    }
    
    public void verificacionUsuario(String nombre, String apellido, int codUser){
        
        
        
        codigoUsuario = codUser;
        nombreUsuario = nombre;
        apellidoUsuario = apellido;
        
        uController.verificacionUsuario(codUser);
        
    }
    
    public void verificacionRol(String nRol){
        
        nombreRol = nRol;
        
        
    }
    
    public void informacion(){
        
        JOptionPane.showMessageDialog(null, "Usuario Activo\n"+ "Nombre: " + nombreUsuario + "\nApellido: " + apellidoUsuario + "\nRol: " + nombreRol);
        
    }
    
    public void ventanaRoles(){
        
        if(nombreRol.equals("Soporte")){
            
            JOptionPane.showMessageDialog(null, "Acceso Autorizado");
            escenarioPrincipal.ventanaRoles();
            
        }else{
            
            JOptionPane.showMessageDialog(null, "Acceso Denegado", "Acceso Restringido", JOptionPane.ERROR_MESSAGE);
            
        }
        
    }
   
    public void ventanaReporteGeneral(){
        
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        GenerarReporte.mostrarReporte("ReporteGeneral.jasper", "Reporte General", parametros);
        
    }
    
    public void ventanaReporteEmpresa(){
        
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de Empresas", parametros);
        
    }
    
    public void ventanaReporteTipoEmpleado(){
        
        Map parametros = new HashMap();
        parametros.put("codigoTipoEmpleado", null);
        GenerarReporte.mostrarReporte("ReporteTipoEmpleado.jasper", "Reporte de Tipo Empleado", parametros);
        
    }
    
    public void ventanaReporteTipoPlato(){
        
        Map parametros = new HashMap();
        parametros.put("codigoTipoPlato", null);
        GenerarReporte.mostrarReporte("ReporteTipoPlato.jasper", "Reporte Tipo Plato", parametros);
        
    }
    
    public void ventanaProducto(){
        
        Map parametros = new HashMap();
        parametros.put("codigoProducto", null);
        GenerarReporte.mostrarReporte("ReporteProducto.jasper", "Reporte Producto", parametros);
        
    }
    
    public void ventanaReportePresupuesto(){
        
        URL subReportePresupuesto = this.getClass().getResource("/org/jorgerevolorio/report/");
        Map parametros = new HashMap();
        int codEmpresa = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el código Empresa"));
        parametros.put("codEmpresa", codEmpresa);
        parametros.put("subReportePresupuesto", subReportePresupuesto);
        GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte de Presupuesto", parametros);
        
    }
    
    public void ventanaReportePlato(){

        Map parametros = new HashMap();
        int codTipoPlato = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el código Tipo Plato"));
        parametros.put("codTipoPlato", codTipoPlato);
        GenerarReporte.mostrarReporte("ReportePlato.jasper", "Reporte de Plato", parametros);
        
    }
    
    public void ventanaReporteEmpleado(){

        Map parametros = new HashMap();
        int codTipoEmpleado = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el código Tipo Empleado"));
        parametros.put("codTipoEmpleado", codTipoEmpleado);
        GenerarReporte.mostrarReporte("ReporteEmpleado.jasper", "Reporte de Empleado", parametros);
        
    }
}
