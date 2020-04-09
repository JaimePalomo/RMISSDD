
import java.rmi.*;

interface OrderSL extends Remote {
        void crearUsuario(String nombre, String contraseña) throws RemoteException;
	void crearProducto(String nombre, int id, float precio) throws RemoteException;
	int RealizarPedido(int listaProductos[], Usuario usuario) throws RemoteException;
	boolean iniciarSesion(String nombre, String contraseña) throws RemoteException;
	void cerrarSesion() throws RemoteException;
	boolean existeUsuario(String nombre) throws RemoteException;
	List<Producto> obtenerProductos() throws RemoteException;
}
