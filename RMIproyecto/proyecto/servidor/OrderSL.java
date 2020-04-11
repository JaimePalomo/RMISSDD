
import java.rmi.*;
import java.rmi.*;
import java.util.*;
import java.rmi.server.*;
import java.io.*;


interface OrderSL extends Remote {
        void crearUsuario(String nombre, String contraseña, float saldo, String direccion, boolean admin) throws RemoteException;
	void crearProducto(String nombre, float precio) throws RemoteException;
	int realizarPedido(int listaProductos[], Usuario usuario) throws RemoteException;
	Usuario iniciarSesion(String nombre, String contraseña) throws RemoteException;
	void guardarCambios() throws RemoteException;
	boolean existeUsuario(String nombre) throws RemoteException;
	List<Producto> obtenerProductos() throws RemoteException;
}
