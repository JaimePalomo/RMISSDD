
import java.io.*;
import java.lang.*;

class Usuario implements Serializable {
     private String nombre;
     private String contraseña;
     private String saldo; 
     private String direccion;
	public Usuario(String nombre, String contraseña, String saldo, String direccion)
	{
		this.nombre=nombre;
		this.contraseña=contraseña;
		this.saldo=saldo;
		this.direccion=direccion
	}
     	public String obtenerSaldo() {
        	 return this.saldo;
     	}
	public void cambiarContraseña(String nueva) {
		this.contraseña = nueva;	
	}
	public void cambiarDireccion(String nueva) {
		this.direccion = nueva;	
	}
	public String añadirSaldo(float valor) {
		float saldo_actual=valueOf(this.saldo);
        	this.saldo=toString(saldo_actual + valor);
        	return this.saldo;
    	}
        public void RealizarPedido(int listaProductos[], List <Producto> catalogo){

	

	
	}
}
