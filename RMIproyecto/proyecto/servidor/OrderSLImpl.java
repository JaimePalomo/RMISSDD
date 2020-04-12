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
	
	File archivo_usuario = new File("Usuarios.dat");
	File archivo_producto = new File("Productos.dat");	  //Abrimos los ficheros .dat donde tenemos guardados los datos
	File archivo_pedido = new File("Pedidos.dat");
				
	if (archivo_usuario.length()!=0){
		try{
		ObjectInputStream usu_entrada = new ObjectInputStream(new FileInputStream("Usuarios.dat")); //Leemos todos los .dat y los guardamos en listas de objetos, pero solo si no están vacíos
		Usuarios = (List <Usuario>) usu_entrada.readObject();
		usu_entrada.close(); 
	}catch(Exception e){
		System.out.println(e);
	}	
	}
	if (archivo_producto.length()!=0){
		try{
		ObjectInputStream pro_entrada = new ObjectInputStream(new FileInputStream("Productos.dat"));
		Productos = (List <Producto>) pro_entrada.readObject();
		pro_entrada.close();
	}catch(Exception e){
		System.out.println(e);
	}	
	}
	if (archivo_pedido.length()!=0){
		try{
		ObjectInputStream ped_entrada = new ObjectInputStream(new FileInputStream("Pedidos.dat"));
		Pedidos = (List <Pedido>) ped_entrada.readObject();
		ped_entrada.close(); 
	}catch(Exception e){
		System.out.println(e);
	}	
	}
	
	
     }
        public Usuario crearUsuario(String nombre, String contraseña, float saldo, String direccion, boolean admin) throws RemoteException { //Crea el usuario y devuelve el objeto
	Usuario usuario = new Usuario(nombre,contraseña,saldo, direccion, admin);
        Usuarios.add(usuario);
	return usuario;
    }
	public void crearProducto(String nombre, float precio) throws RemoteException { //Crea el producto
		boolean repetido = false;
		int id;
		do{
		Random numAleatorio = new Random();   // Genera un int de forma aleatoria y comprueba que no esté repetido en la lista y que no sea negativo
		id = numAleatorio.nextInt();
		if (id<0)
			id = id * (-1);
		for (Producto i: Productos){
			if(id==i.obtenerId())
				repetido = true;		
		}
		}while(repetido);
		Producto producto = new Producto(nombre,id,precio);
		Productos.add(producto);
	    }
	public void realizarPedido(int listaProductos[], Usuario usuario) throws RemoteException{ //Crea una nueva Lista de Productos que será el carrito
	List <Producto> Carrito=new LinkedList<Producto>(); //Estará compuesto por los productos cuya id coincida con las id proporcionadas por el cliente
	float precio=0;
	for(int i=0; i<listaProductos.length;i++){
		for(Producto j: Productos){
			if (j.obtenerId()==listaProductos[i]){
				Carrito.add(j);
				precio+=j.obtenerPrecio();
			}
		}
	}
	int id;
	boolean repetido = false;
		do{
		Random numAleatorio = new Random();   // Genera un int de forma aleatoria y comprueba que no esté repetido en la lista y que no sea negativo	
		id = numAleatorio.nextInt();
		if (id<0)
			id = id * (-1);
		for (Pedido i: Pedidos){
			if(id==i.obtenerId() )
				repetido = true;		
		}
		}while(repetido);
	Date fecha = new Date();
	String direccion = usuario.obtenerDireccion();
	Pedido ped = new Pedido(id,fecha,Carrito,usuario,precio,direccion);
	Pedidos.add(ped); //Añadimos el pedido a la lista de pedidos
    }
    public Usuario iniciarSesion(String nombre, String contraseña) throws RemoteException { //Para comprobar tupla nombre-contraseña,devuelve 1 si existe y 0 en caso contrario

	Usuario usuario = null;
	
 	for (Usuario i: Usuarios) {
                if(nombre.equals(i.obtenerNombre()))
			if(contraseña.equals(i.obtenerContraseña()))
				usuario=i;
            }
        return usuario;

	}
	public void guardarCambios() throws RemoteException { //Guarda las listas de objetos en los obj sobreescribiéndolos
	
	File usu_borrar = new File ("Usuarios.dat");
	File pro_borrar = new File ("Productos.dat");
	File ped_borrar = new File ("Pedidos.dat");   //Borramos los .dat anteriores para guardar todos los datos con las modificaciones que se hayan hecho durante la sesión(con ObjectOutputStream sobreescribir genera un problema de cabeceras)
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
        boolean existe = false;
            for (Usuario i: Usuarios) {
                if(nombre.equals(i.obtenerNombre()))
					existe = true;
			}
        return existe;
  	}
	public boolean existeProducto(String nombre) throws RemoteException { //Para comprobar si el producto existe, devuelve 1 si existe y 0 en caso contrario
        boolean existe = false;
            for (Producto i: Productos) {
                if(nombre.equals(i.obtenerNombre()))
					existe = true;
            }
        return existe;
  	}
    public List<Producto> obtenerProductos() throws RemoteException{   //Obtener todos los productos existentes

	return Productos;
    }
    public List<Pedido> obtenerPedidos() throws RemoteException{    //Obtener todos los pedidos existentes
	return Pedidos;
    }
    public List<Usuario> obtenerUsuarios() throws RemoteException{    //Obtener todos los usuarios existentes
	return Usuarios;
    }
	public void modificarUsuario(Usuario usuario_nuevo, String nombre_usuario_antiguo){
		for (Usuario i: Usuarios){
			if(i.obtenerNombre().equals(nombre_usuario_antiguo))  //Buscamos la coincidencia del nombre a cambiar en nuestra base de datos y borramos el objeto Usuario y creamos uno con los nuevos atributos de usuario_nuevo
				Usuarios.remove(i);
		}
		Usuarios.add(usuario_nuevo);	
		for (Pedido p: Pedidos){
			if(p.obtenerUsuario().obtenerNombre().equals(nombre_usuario_antiguo))
				p.cambiarUsuario(usuario_nuevo);		
		}
	}
	/*public void borrarProducto(Producto producto) {
        	Productos.remove(producto);

     	}*/
}

