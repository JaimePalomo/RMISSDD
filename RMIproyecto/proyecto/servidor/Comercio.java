
import java.rmi.*;

interface Comercio extends Remote {
    Comercio crearUsuario(String nombre, String correo, String contraseña) throws RemoteException;
	Comercio comprobarUsuario(String nombre) throws RemoteException;
}
