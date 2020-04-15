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
	File archivo_producto = new File("Productos.dat");	  //Abrimos los ficheros .dat (BBDD) donde tenemos guardados los datos
	File archivo_pedido = new File("Pedidos.dat");
				
	if (archivo_usuario.length()!=0){
		try{
		ObjectInputStream usu_entrada = new ObjectInputStream(new FileInputStream("Usuarios.dat")); //Leemos todos los .dat y los guardamos 
		Usuarios = (List <Usuario>) usu_entrada.readObject();   //en listas de objetos, pero solo si no están vacíos
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
        public Usuario crearUsuario(String nombre, String contraseña, float saldo, String direccion, boolean admin) throws RemoteException { 
	Usuario usuario = new Usuario(nombre,contraseña,saldo, direccion, admin);  //Crea el usuario y devuelve el objeto
        Usuarios.add(usuario);
	return usuario;
    }
	public void crearProducto(String nombre, float precio) throws RemoteException { //Crea el producto
		boolean repetido = false;
		int id;
		do{
		Random numAleatorio = new Random();   // Genera un int de forma aleatoria, comprueba que no esté repetido en la lista 
		id = numAleatorio.nextInt();  //y que no sea negativo
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
	public void realizarPedido(int listaProductos[], Usuario usuario) throws RemoteException{ //Crea una nueva Lista de Productos  
	List <Producto> Carrito=new LinkedList<Producto>(); 				         //que será el carrito
	float precio=0;
	for(int i=0; i<listaProductos.length;i++){  //Estará compuesto por los productos cuya id coincida con las id proporcionadas por el cliente
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
		Random numAleatorio = new Random();   // Genera un int de forma aleatoria, comprueba que no esté repetido en la lista	
		id = numAleatorio.nextInt();     // y que no sea negativo
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
	Pedidos.add(ped);      //Añadimos el pedido a la lista de pedidos
    }
    public Usuario iniciarSesion(String nombre, String contraseña) throws RemoteException { //Para comprobar tupla nombre-contraseña
	Usuario usuario = null;                                                            //devuelve el usuario o null si no lo encuentra
	
 	for (Usuario i: Usuarios) {
                if(nombre.equals(i.obtenerNombre())){
					if(contraseña.equals(i.obtenerContraseña()))
							usuario=i;
				}
            }
        return usuario;
	}
	public void escribirDatosBBDD() throws RemoteException { //Guarda las listas de objetos actuales en la BBDD para no perder los datos
	
	File usu_borrar = new File ("Usuarios.dat");
	File pro_borrar = new File ("Productos.dat");
	File ped_borrar = new File ("Pedidos.dat");   //Borramos los .dat anteriores para guardar todos los datos con las modificaciones que se 
	usu_borrar.delete();     //hayan hecho durante la sesión(con ObjectOutputStream sobreescribir genera un problema de cabeceras)
	pro_borrar.delete();
	ped_borrar.delete();
	

          	
	
	try  {
	ObjectOutputStream usu_salida = new ObjectOutputStream(new FileOutputStream("Usuarios.dat"));
	ObjectOutputStream pro_salida = new ObjectOutputStream(new FileOutputStream("Productos.dat"));  //Volvemos a crear los .dat para guardar 
	ObjectOutputStream ped_salida = new ObjectOutputStream(new FileOutputStream("Pedidos.dat")); //nuestras listas de objetos

	usu_salida.writeObject(Usuarios);  //Escribimos las listas en los .dat
	pro_salida.writeObject(Productos);
	ped_salida.writeObject(Pedidos);

	usu_salida.close();
	pro_salida.close();
	ped_salida.close();
	}catch(Exception e){
		System.out.println(e);
	}	

	}
    public boolean existeUsuario(String nombre) throws RemoteException { //Para comprobar si el usuario existe, devuelve 1 si existe
        boolean existe = false;                                // y 0 en caso contrario
            for (Usuario i: Usuarios) {
                if(nombre.equals(i.obtenerNombre()))
					existe = true;
			}
        return existe;
  	}
	public boolean existeProducto(String nombre) throws RemoteException { //Para comprobar si el producto existe
        boolean existe = false;                                       //devuelve 1 si existe y 0 en caso contrario
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
	public void modificarUsuario(Usuario usuario_nuevo, String nombre_usuario_antiguo){ //Esta funcion busca en la lista de usuarios una 
	Iterator<Usuario> recorrer_usuarios = Usuarios.iterator();                         //coincidencia con nombre_usuario_antiguo y elimina dicho 
	Iterator<Pedido> recorrer_pedidos = Pedidos.iterator();                           //usuario, después añade a la lista un nuevo usuario con 
    	while (recorrer_usuarios.hasNext()){                                             //los atributos de usuario_nuevo
		Usuario usuario = recorrer_usuarios.next(); 
		if (usuario.obtenerNombre().equals(nombre_usuario_antiguo))
			recorrer_usuarios.remove();
	}
   	Usuarios.add(usuario_nuevo); 	
	while (recorrer_pedidos.hasNext()){   //También busca en la lista de Pedidos si el usuario antiguo tenía pedidos realizados
	      Pedido pedido = recorrer_pedidos.next();
	      if (pedido.obtenerUsuario().obtenerNombre().equals(nombre_usuario_antiguo))  // y lo cambia por el nuevo
			pedido.cambiarUsuario(usuario_nuevo);
	}
    }
}

