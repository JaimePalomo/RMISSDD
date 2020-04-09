import java.rmi.*;
import java.rmi.server.*;

class ServidorComercio  {
    static public void main (String args[]) {
       if (args.length!=1) {
            System.err.println("Uso: ServidorComercio numPuertoRegistro");
            return;
        }
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        try {
            BancoImpl srv = new OrderSLImpl();
            Naming.rebind("rmi://localhost:" + args[0] + "/OrderSL", srv);
        }
        catch (RemoteException e) {
            System.err.println("Error de comunicacion: " + e.toString());
            System.exit(1);
        }
        catch (Exception e) {
            System.err.println("Excepcion en ServidorComercio:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
