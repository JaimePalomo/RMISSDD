
import java.rmi.*;

interface OrderSL extends Remote {
    Usuario crearUsuario(String nombre, String contraseña) throws RemoteException;
	boolean comprobarContraseña(String nombre, String contraseña) throws RemoteException;
	boolean existeUsuario(String nombre) throws RemoteException;
}
