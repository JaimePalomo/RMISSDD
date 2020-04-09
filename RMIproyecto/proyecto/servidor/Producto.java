
import java.io.*;
import java.lang.*;

class Producto implements Serializable {
     private String nombre;
     private String id;
     private String precio; 
	public Producto(String nombre, String id, String precio)
	{
		this.nombre=nombre;
		this.id=id;
		this.precio=precio;
	}
     	public String getNombre() {
        	return this.nombre
     	}
	public String getId() {
        	return this.id
     	}
    	public String getPrecio() {
        	return this.precio
     	}
