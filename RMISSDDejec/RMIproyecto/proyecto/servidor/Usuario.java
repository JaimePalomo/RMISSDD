
import java.io.*;
import java.lang.*;
import java.util.*;

class Usuario implements Serializable {
	private String nombre;
	private String contraseña;
	private float saldo; 
	private String direccion;
	List <Pedido> Pedidos;
	private boolean admin;
	public Usuario(String nombre, String contraseña, float saldo, String direccion, List <Pedido> Pedidos, boolean admin)
	{
		this.nombre=nombre;
		this.contraseña=contraseña;
		this.saldo=saldo;
		this.direccion=direccion;
		this.Pedidos=Pedidos;
		this.admin=admin;
	}
	public String obtenerNombre() {
        	 return this.nombre;
     	}
	public String obtenerContraseña() {
        	 return this.contraseña;
     	}
	public String obtenerDireccion() {
        	 return this.direccion;
     	}
     	public float obtenerSaldo() {
        	 return this.saldo;
     	}
	public boolean isAdmin() {
		return this.admin;	
	}
	public List<Pedido> obtenerPedidos() {
        	 return this.Pedidos;
     	}
	public void cambiarNombre(String nuevo) {
		this.nombre = nuevo;	
	}
	public void cambiarContraseña(String nueva) {
		this.contraseña = nueva;	
	}
	public void cambiarDireccion(String nueva) {
		this.direccion = nueva;	
	}
	public void hacerAdmin(boolean admin_nuevo){
		this.admin=admin_nuevo;	
	}
	public float añadirSaldo(float valor) {
        	this.saldo+=valor;
        	return this.saldo;
    	}
}
