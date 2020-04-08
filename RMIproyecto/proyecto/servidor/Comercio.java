
import java.rmi.*;

interface Comercio extends Remote {
    Cuenta crearUsuario(String nombre) throws RemoteException;
}
