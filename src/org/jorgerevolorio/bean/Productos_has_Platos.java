package org.jorgerevolorio.bean;

public class Productos_has_Platos {
    
    private int Producto_CodigoProducto;
    private int codigoPlato;
    private int codigoProducto;

    public Productos_has_Platos() {
    }

    public Productos_has_Platos(int Producto_CodigoProducto, int codigoPlato, int codigoProducto) {
        this.Producto_CodigoProducto = Producto_CodigoProducto;
        this.codigoPlato = codigoPlato;
        this.codigoProducto = codigoProducto;
    }

    public int getProducto_CodigoProducto() {
        return Producto_CodigoProducto;
    }

    public void setProducto_CodigoProducto(int Producto_CodigoProducto) {
        this.Producto_CodigoProducto = Producto_CodigoProducto;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }
    
    
    
}
