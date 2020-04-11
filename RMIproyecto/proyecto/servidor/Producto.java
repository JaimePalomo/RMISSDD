import java.util.*;
import java.io.*;
import java.lang.*;

class Producto implements Serializable {
	private String nombre;
	private int id;
	private float precio; 
	public Producto(String nombre, int id, float precio)
	{
		this.nombre=nombre;
		this.id=id;
		this.precio=precio;
	}
     	public String obtenerNombre() {
        	return this.nombre;
     	}
	public int obtenerId() {
        	return this.id;
     	}
	public float obtenerPrecio() {
        	return this.precio;
     	}
    	public void cambiarPrecio(float valor) {
        	this.precio=valor;
     	}
	public void cambiarNombre(String nombre_nuevo) {
        	this.nombre=nombre_nuevo;
     	}
	public void cambiarId(int id_nuevo) {
        	this.id=id_nuevo;
     	}
    	
}

