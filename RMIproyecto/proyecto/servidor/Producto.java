
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
     	public static void getProducto() {
        	;
     	}
	public void cambiarContraseña(String nueva) {
		this.contraseña = nueva;
		
	}
	public String añadirSaldo(float valor) {
		float saldo_actual=valueOf(this.saldo);
        	this.saldo=toString(saldo_actual + valor);
        	return this.saldo;
    	}
