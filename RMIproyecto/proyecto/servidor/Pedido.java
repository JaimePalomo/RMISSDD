import java.io.*;
import java.lang.*;
import java.util.*;

class Pedido implements Serializable {
	private int id;
	private Date fecha; 
	List <Producto> Carrito;
	Usuario usuario;
	public Pedido(int id, Date fecha, List <Producto> Carrito, Usuario usu)
	{
		this.Carrito=Carrito;
		this.id=id;
		this.fecha=fecha;
		this.usuario=usuario;
	}
     	public List<Producto> obtenerProductos() {
        	return this.Productos;
     	}
	public int obtenerId() {
        	return this.id;
     	}
	public Date obtenerFecha() {
        	return this.fecha;
     	}
    /*	public void añadirProducto(Producto producto) {
        	Carrito.add(producto);
		this.Carrito=Carrito;
     	}
	public void borrarProducto(Producto producto) {
        	Carrito.delete(producto);
		this.Carrito=Carrito;
     	} */
}
