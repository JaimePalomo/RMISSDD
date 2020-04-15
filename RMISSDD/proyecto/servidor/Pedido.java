import java.io.*;
import java.lang.*;
import java.util.*;

class Pedido implements Serializable {
	private int id;
	private Date fecha; 
	List <Producto> Carrito;
	Usuario usuario;
	float precio;
	String direccion;
	private static final long serialVersionUID=4542458884459387270L;//Forzamos la coincidencia del serialVersionUID con los que están guardados en el .dat
	public Pedido(int id, Date fecha, List <Producto> Carrito, Usuario usuario, float precio, String direccion)
	{
		this.Carrito=Carrito;
		this.id=id;
		this.fecha=fecha;
		this.usuario=usuario;
		this.precio=precio;
		this.direccion=direccion;
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
	public void cambiarUsuario(Usuario usuario_nuevo){
		this.usuario = usuario_nuevo;	
	}
	public float obtenerPrecio(){
		return this.precio;	
	}
	public String obtenerDireccion(){
		return this.direccion;	
	}
}

