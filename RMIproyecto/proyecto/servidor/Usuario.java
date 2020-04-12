
import java.io.*;
import java.lang.*;
import java.util.*;

class Usuario implements Serializable {
	private String nombre;
	private String contraseña;
	private float saldo; 
	private String direccion;
	private boolean admin;
	private static final long serialVersionUID=6762531975668221922L; //Forzamos la coincidencia del serialVersionUID con los que están guardados en el .dat
	public Usuario(String nombre, String contraseña, float saldo, String direccion, boolean admin)
	{
		this.nombre=nombre;
		this.contraseña=contraseña;
		this.saldo=saldo;
		this.direccion=direccion;
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
	public void cambiarNombre(String nuevo) {
		this.nombre = nuevo;
	}
	public void cambiarContraseña(String nueva) {
		this.contraseña = nueva;
	}
	public void cambiarDireccion(String nueva) {
		this.direccion = nueva;
	}
	public Usuario hacerAdmin(boolean admin_nuevo){
		this.admin=admin_nuevo;
		return this;	
		
	}
	public float añadirSaldo(float valor) {
        	this.saldo+=valor;
        	return this.saldo;
    	}
}
