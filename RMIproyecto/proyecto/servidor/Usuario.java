
import java.rmi.*;

interface Usuario extends Remote {
    String obtenerNombre() throws RemoteException;
    float obtenerSaldo() throws RemoteException;
    float operacion(float valor) throws RemoteException;
}
