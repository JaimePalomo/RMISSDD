import java.rmi.*;
import java.util.*;
import java.rmi.server.*;
import java.io.BufferedReader;
import java.io.FileReader;

class OrderSLImpl extends UnicastRemoteObject implements OrderSL {
	List<Usuario> u;
        List <Producto> catalogo;
    OrderSLImpl() throws RemoteException {
	        u = new LinkedList<Usuario>();
		catalogo = new LinkedList<Producto>();
     }
    public Usuario crearUsuario(String nombre, String contraseña) throws RemoteException { //Crea el usuario(con 10e de saldo inicial) y devuelve el objeto
	String saldo = 10;
	Usuario usu = new Usuario(nombre,contraseña,saldo);
        u.add(usu);
        return usu;
    }
    public boolean comprobarContraseña(String nombre, String contraseña) throws RemoteException { //Para comprobar tupla nombre-contraseña,devuelve 1 si existe y 0 en caso contrario
		
        boolean correcto = false;
      	
 	for (Cuenta i: u) {
                if(nombre==i.nombre())
			if(contraseña==i.contraseña());
				correcto=true;
            }
        return correcto;

}
	
    public boolean existeUsuario(String nombre) throws RemoteException { //Para comprobar si el usuario existe, devuelve 1 si existe y 0 en caso contrario
        boolean encontrado = false;
            for (Cuenta i: u) {
                if(nombre==i.nombre())
			encontrado = true;
            }
        return encontrado;
  }
    public List<Producto> obtenerCatalogo() throws RemoteException{
	
	try {
      FileReader fr = new FileReader("catalogo.txt");
      BufferedReader br = new BufferedReader(fr);
 
      String linea;
      while((linea = br.readLine()) != null){
	  String[] parts = linea.split("-");
          String nombre = parts[0];
          String id = parts[1];
          String precio = parts[2];
	  Producto p = new Producto(nombre,id,precio);
          catalogo.add(p);
      }
 
      fr.close();
    }
    catch(Exception e) {
      System.out.println("Excepcion leyendo fichero catalogo.txt");
    }
	return catalogo;
    }
}

