import java.rmi.*;
import java.rmi.server.*;
import java.io.PrintWriter;
import java.util.Scanner;

class ComercioImpl extends UnicastRemoteObject implements Comercio {
	PrintWriter usu, cor, con;
    ComercioImpl() throws RemoteException {
        try {
             usu = new PrintWriter("Usuarios.txt");
             cor = new PrintWriter("Correos.txt");
             con = new PrintWriter("Contraseñas.txt");
        }
        catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        }
    }
    public Usuario crearUsuario(String nombre, String correo, String contraseña) throws RemoteException {
	
	try {
           Thread.sleep(1);
        }
        catch( InterruptedException e)
        {
        }
        usu.println(nombre);
        cor.println(correo + "-" + nombre); //Para buscar las tuplas fácilmente en el txt
        con.println(contraseña + "-" + nombre);

        fd.flush();

        return new UsuarioImpl(nombre, correo, contraseña);
    }
    public Usuario comprobarUsuario(String nombre, String contraseña) throws RemoteException { //Para comprobar tupla nombre-contraseña,devuelve 1 si existe y 0 en caso contrario
		try {
    do {
        BufferedReader br=new BufferedReader(new FileReader("Contraseñas.txt"));
	String comprobar = contraseña + "-" + nombre;
        String linea="";
        boolean encontrado = false;
        while ((linea= br.readLine())!=null) {

            if(linea.equalsIgnoreCase(comprobar)) {
                encontrado = true;
                break;
		}
        }

    }while(comprobar.equalsIgnoreCase("si"));
} catch (IOException e) {

    System.out.println("Error");
}
        return encontrado;
    }
	
    public Usuario existeUsuario(String nombre) throws RemoteException { //Para comprobar si el usuario existe, devuelve 1 si existe y 0 en caso contrario
	
	try {
    do {
        BufferedReader br=new BufferedReader(new FileReader("Usuarios.txt"));
        String linea="";
        boolean encontrado = false;
        while ((linea= br.readLine())!=null) {

            if(linea.equalsIgnoreCase(nombre)) {
                encontrado = true;
                break;
		}
        }

    }while(nombre.equalsIgnoreCase("si"));
} catch (IOException e) {

    System.out.println("Error");
}
        return encontrado;
    }
}
