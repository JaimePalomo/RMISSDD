import java.rmi.*;
import java.rmi.server.*;

class ComercioImpl extends UnicastRemoteObject implements Comercio {
    ComercioImpl() throws RemoteException {
    }
    public Usuario crearUsuario(String nombre) throws RemoteException {
        return new UsuarioImpl(nombre);
    }
}
