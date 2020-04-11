import java.rmi.*;
import java.util.*;
import java.rmi.server.*;
import java.io.*;

class OrderSLImpl extends UnicastRemoteObject implements OrderSL {
	List<Usuario> Usuarios;
        List <Producto> Productos;
	List <Pedido> Pedidos;
    OrderSLImpl() throws RemoteException {
	Usuarios = new LinkedList<Usuario>();  //Inicializamos las listas de objetos
       	Productos = new LinkedList<Producto>();
	Pedidos = new LinkedList<Pedido>();
	try{
	ObjectInputStream usu_entrada = new ObjectInputStream(new FileInputStream("Usuarios.dat"));
	ObjectInputStream pro_entrada = new ObjectInputStream(new FileInputStream("Productos.dat"));
	ObjectInputStream ped_entrada = new ObjectInputStream(new FileInputStream("Pedidos.dat"));
	Usuarios = (List <Usuario>) usu_entrada.readObject();
	Productos = (List <Producto>) pro_entrada.readObject();
	Pedidos = (List <Pedido>) ped_entrada.readObject();
	usu_entrada.close();
	pro_entrada.close();
	ped_entrada.close();   
	}catch(Exception e){
		System.out.println(e);
	}
	
     }
        public void crearUsuario(String nombre, String contraseña, float saldo, String direccion, boolean admin) throws RemoteException { //Crea el usuario y devuelve el objeto
	Usuario usuario = new Usuario(nombre,contraseña,saldo, direccion, null, admin);
        Usuarios.add(usuario);
    }
	public void crearProducto(String nombre, float precio) throws RemoteException { //Crea el producto
		boolean repetido = false;
		int id;
		do{
		Random numAleatorio = new Random();   // Genera un int de forma aleatoria y comprueba que no esté repetido en la lista
		id = numAleatorio.nextInt();
		for (Producto i: Productos){
			if(id==i.obtenerId())
				repetido = true;		
		}
		}while(repetido);
		Producto producto = new Producto(nombre,id,precio);
		Productos.add(producto);
	    }
	public int realizarPedido(int listaProductos[], Usuario usuario) throws RemoteException{ //Crea una nueva Lista de Productos que será el carrito
	List <Producto> Carrito=new LinkedList<Producto>(); //Estará compuesto por los productos cuya id coincida con las id proporcionadas por el cliente
	for(int i=0; i<listaProductos.length;i++){
		for(Producto j: Productos){
			if (j.obtenerId()==listaProductos[i])
				Carrito.add(j);
		}
	}
	int id;
	boolean repetido = false;
		do{
		Random numAleatorio = new Random();   // Genera un int de forma aleatoria y comprueba que no esté repetido en la lista
		id = numAleatorio.nextInt();
		for (Pedido i: Pedidos){
			if(id==i.obtenerId())
				repetido = true;		
		}
		}while(repetido);
	Date fecha = new Date();
	Pedido ped = new Pedido(id,fecha,Carrito,usuario);
	return id;		  //Devuelve el id del pedido
    }
    public Usuario iniciarSesion(String nombre, String contraseña) throws RemoteException { //Para comprobar tupla nombre-contraseña,devuelve 1 si existe y 0 en caso contrario

	Usuario usuario = null;
	
 	for (Usuario i: Usuarios) {
                if(nombre==i.obtenerNombre())
			if(contraseña==i.obtenerContraseña())
				usuario=i;
            }
        return usuario;

	}
	public void guardarCambios() throws RemoteException { //Guarda las listas de objetos en los obj sobreescribiéndolos
	
	File usu_borrar = new File ("Usuarios.dat");
	File pro_borrar = new File ("Productos.dat");
	File ped_borrar = new File ("Pedidos.dat");   //Borramos los ficheros anteriores(con ObjectOutputStream sobreescribir genera un problema de cabeceras)
	usu_borrar.delete();
	pro_borrar.delete();
	ped_borrar.delete();
	

          	
	
	try  {
	ObjectOutputStream usu_salida = new ObjectOutputStream(new FileOutputStream("Usuarios.dat"));
	ObjectOutputStream pro_salida = new ObjectOutputStream(new FileOutputStream("Productos.dat"));  //Volvemos a crear los .dat para guardar nuestras listas de objetos
	ObjectOutputStream ped_salida = new ObjectOutputStream(new FileOutputStream("Pedidos.dat"));

	usu_salida.writeObject(Usuarios);
	pro_salida.writeObject(Productos);
	ped_salida.writeObject(Pedidos);

	usu_salida.close();
	pro_salida.close();
	ped_salida.close();
	}catch(Exception e){
		System.out.println(e);
	}	

	}
    public boolean existeUsuario(String nombre) throws RemoteException { //Para comprobar si el usuario existe, devuelve 1 si existe y 0 en caso contrario
        boolean encontrado = false;
            for (Usuario i: Usuarios) {
                if(nombre==i.obtenerNombre())
			encontrado = true;
            }
        return encontrado;
  	}
    public List<Producto> obtenerProductos() throws RemoteException{
	return Productos;
    }
}

