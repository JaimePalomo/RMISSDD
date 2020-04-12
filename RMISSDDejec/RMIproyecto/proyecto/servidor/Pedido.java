import java.io.*;
import java.lang.*;
import java.util.*;

class Pedido implements Serializable {
	private int id;
	private Date fecha; 
	List <Producto> Carrito;
	Usuario usuario;
	public Pedido(int id, Date fecha, List <Producto> Carrito, Usuario usuario)
	{
		this.Carrito=Carrito;
		this.id=id;
		this.fecha=fecha;
		this.usuario=usuario;
	}
     	public List<Producto> obtenerCarrito() {
        	return this.Carrito;
     	}
	public int obtenerId() {
        	return this.id;
     	}
	public Date obtenerFecha() {
        	return this.fecha;
     	}
	public Usuario obtenerUsuario(){
		return this.usuario;	
	}
}
