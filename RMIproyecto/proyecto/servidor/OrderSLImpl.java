import java.rmi.*;
import java.util.*;
import java.rmi.server.*;
import java.ObjectOutputStream;
import java.ObjectInputStream;
import java.FileOutputStream;
import java.FileInputStream;

class OrderSLImpl extends UnicastRemoteObject implements OrderSL {
	List<Usuario> Usuarios;
        List <Producto> Productos;
	List <Pedido> Pedidos;
    OrderSLImpl() throws RemoteException {
	Usuarios = new LinkedList<Usuario>();  //Inicializamos las listas de objetos
       	Productos = new LinkedList<Producto>();
	Pedidos = new LinkedList<Pedido>();

	ObjectInputStream usu = new ObjectInputStream(new FileInputStream("Usuarios.obj"));
	ObjectInputStream pro = new ObjectInputStream(new FileInputStream("Productos.obj"));
	ObjectInputStream ped = new ObjectInputStream(new FileInputStream("Pedidos.obj"));
	Usuarios = (List <Usuario>) usu.readObject();
	Productos = (List <Producto>) pro.readObject();
	Pedidos = (List <Pedido>) ped.readObject();
	usu.close();
	pro.close();
	ped.close();   
     }
        public void crearUsuario(String nombre, String contraseña, float saldo, String direccion, boolean admin) throws RemoteException { //Crea el usuario y devuelve el objeto
	Usuario usuario = new Usuario(nombre,contraseña,saldo, direccion, null, admin);
        Usuarios.add(usuario);
    }
	public void crearProducto(String nombre, int id, float precio) throws RemoteException { //Crea el producto y devuelve el objeto
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
	Random numAleatorio = new Random();   // Genera un int de forma aleatoria
	int id = numAleatorio.nextInt();
	Date fecha = new Date();
	Pedido ped = new Pedido(id,fecha,Carrito,usuario);
	return id;		  //Devuelve el id del pedido
    }
    public Usuario iniciarSesion(String nombre, String contraseña) throws RemoteException { //Para comprobar tupla nombre-contraseña,devuelve 1 si existe y 0 en caso contrario

	Usuario usuario = null;
	
 	for (Usuario i: Usuarios) {
                if(nombre==i.obtenerNombre())
			if(contraseña==i.obtenerContraseña());
				usuario=i;
            }
        return usuario;

	}
	public void guardarCambios() throws RemoteException { //Guarda las listas de objetos en los obj sobreescribiéndolos
	
	File usu = new File ("Usuarios.obj");
	File pro = new File ("Productos.obj");
	File ped = new File ("Pedidos.obj");   //Borramos los ficheros anteriores(con ObjectOutputStream sobreescribir genera un problema de cabeceras)
	usu.delete();
	pro.delete();
	ped.delete();	

	ObjectOutputStream usu = new ObjectInputStream(new FileOutputStream("Usuarios.obj"));
	ObjectOutputStream pro = new ObjectInputStream(new FileOutputStream("Productos.obj"));  //Volvemos a crear los .obj para guardar nuestras listas de objetos
	ObjectOutputStream ped = new ObjectInputStream(new FileOutputStream("Pedidos.obj"));

	usu.writeObject(Usuarios);
	pro.writeObject(Productos);
	ped.writeObject(Pedidos);

	usu.close();
	pro.close();
	ped.close();

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

