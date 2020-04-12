
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.lang.*;
import java.io.*;


class ClienteComercio {
    public static final int COMPROBAR_PEDIDOS = 1;
    public static final int MOSTRAR_CATÁLOGO = 2;
    public static final int REALIZAR_PEDIDO = 3;
    public static final int MODIFICAR_DATOS_USUARIO = 4;
    public static final int AÑADIR_SALDO = 5;
    public static final int AÑADIR_PRODUCTO = 6;
    public static final int MOSTRAR_TODOS_PEDIDOS = 7;
    public static final int SALIR = 8;
    
    static public void main (String args[]) {

	boolean existe = false;
	boolean administrador = false;
	Usuario usuario = null;
	int flag = 1; //Bandera para la salida del menu
	int flag2 = 1; //Bandera para la salida de creación de usuario e inicio de sesion
	int flag3 = 0; //Bandera para la salida de la concesión de administrador al usuario
	List <Producto> catalogo;//Lista en la que guardaremos los objetos de la clase Producto
	List <Usuario> Usuarios;
        if (args.length!=2) {
            System.err.println("Uso: ClienteComercio hostregistro numPuertoRegistro");
            return;
        }

       if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            OrderSL srv = (OrderSL) Naming.lookup("//" + args[0] + ":" + args[1] + "/OrderSL");
		clear();
	    while(flag2 == 1){
		System.out.println("Introduzca nombre de usuario: ");
		Scanner sc = new Scanner(System.in);
		String nomUsu = sc.nextLine();
		existe = srv.existeUsuario(nomUsu);
		if(existe){
		    System.out.println("Introduzca contraseña: ");
		}
		else{
		    System.out.println("Introduzca contraseña para la creacion de un usuario: ");
		}
		String password = sc.nextLine();
		if(existe){
		    usuario = srv.iniciarSesion(nomUsu, password);
		    if(usuario !=null){
			flag2=0;
		    }
		    else{
			System.out.println("Contraseña incorrecta");
		    }
		}
		else{
		    System.out.println("Introduzca la dirección de envío para vincularla a tu usuario: ");
		    String direccion = sc.nextLine();
		    System.out.println("Introduzca el saldo inicial para tu usuario: ");
		    float saldoIni = sc.nextFloat();
		    while(flag3==0){
			System.out.println("¿El usuario va a ser admin?");
			System.out.println("1.Sí");
			System.out.println("2.No");
			
			int aux = sc.nextInt();
			switch (aux){
			case 1:
			    flag3=1;
			    administrador=true;
			    break;
			case 2:
			    flag3=1;
			    administrador=false;
			    break;
			default:
			    flag3=0;
			    System.out.println("Introduce 1 ó 2");
			}
		    }
		    
		    usuario = srv.crearUsuario(nomUsu, password, saldoIni, direccion, administrador);
		    flag2=0;
		    srv.guardarCambios();
		    System.out.println("Registro de usuario completado");
		}
	    }
	    

	    try{
		    File f = new File("OrderSL.txt");
		    FileReader fr = new FileReader(f);
		    BufferedReader br = new BufferedReader(fr);
		    String linea;
		    
		    while((linea=br.readLine())!=null)
			System.out.println(linea);
		}catch(Exception e){
		    e.printStackTrace();
		}
	    while( flag == 1){ //El bucle se seguirá realizando e imprimiendo el menú hasta que el cliente quiera salir
		 catalogo=srv.obtenerProductos(); //La función ObtenerCatalogo() devolvera una lista de clase Producto con todos los productos disponibles	
		try{
		    File f = new File("Menu.txt");
		    FileReader fr = new FileReader(f);
		    BufferedReader br = new BufferedReader(fr);
		    String linea;
		    
		    while((linea=br.readLine())!=null)
			System.out.println(linea);
		}catch(Exception e){
		    e.printStackTrace();
		}
		Scanner sc = new Scanner(System.in);
		int i = sc.nextInt();
		switch (i){
		case COMPROBAR_PEDIDOS:
			clear();
		    verPedidos(usuario, srv); 
		    break;
		case MOSTRAR_CATÁLOGO:
			clear();
			System.out.println("-----------------------------------------------------------------------------------------"); 
		    for(Producto z: catalogo){
			System.out.println(z.obtenerNombre() + "	precio: " + z.obtenerPrecio() +"euros	id: " + z.obtenerId()); 
		    }
										   			
			System.out.println("-----------------------------------------------------------------------------------------"); 
		    break;
		case REALIZAR_PEDIDO:
			clear();
		    realizarPedido(usuario, catalogo, srv);
			clear();
		    break;
		case MODIFICAR_DATOS_USUARIO:
			clear();
		    modificarUsuario(usuario);
		    srv.guardarCambios();
			clear();
		    break;
		case AÑADIR_SALDO:
			clear();
		    añadirSaldo(usuario);
		    srv.guardarCambios();
			clear();
		    break;
		case AÑADIR_PRODUCTO:
			clear();
		    if(usuario.isAdmin()){
			añadirProducto(srv);
			srv.guardarCambios();  
			clear(); 		    
			}
		    else
			System.out.println("Necesita ser administrador para realizar esta acción"); 	
		    break;
		case MOSTRAR_TODOS_PEDIDOS:
			clear();
		    if(usuario.isAdmin()){
			Usuarios = srv.obtenerUsuarios();
			for(Usuario usuario_x: Usuarios) {   //Obtenemos todos los usuarios y vamos mostrando sus pedidos
				verPedidos(usuario_x, srv);	
	System.out.println("-------------------------------------"); 		
				}
										  System.out.println("-----------------------------------------------------------------------------------------"); 
		    }
		    else
			System.out.println("Necesita ser administrador para realizar esta acción");
		    break;
		case SALIR:
			clear();
		    srv.guardarCambios();
		    flag = 0;
		    break;
		default:
		    System.err.println("Número introducido no válido ");
		}
	    }
        }
        catch (RemoteException e) {
            System.err.println("Error de comunicacion: " + e.toString());
        }
        catch (Exception e) {
            System.err.println("Excepcion en ClienteComercio:");
            e.printStackTrace();
        }
    }
	
    public static void verPedidos(Usuario u, OrderSL srv){ //Esta función llamará al servidor para que nos devuelva todos los pedidos que tenga el usuario
	int numPedido = 1;
	List <Producto> pro;
	List <Pedido> p;
	try{
	p=srv.obtenerPedidos();
	for (Pedido x: p){
		if(u.obtenerNombre().equals(x.obtenerUsuario().obtenerNombre())){
		    System.out.println("PEDIDO NUMERO "+ numPedido);
		    System.out.println("Realizado por: "+ u.obtenerNombre());
		    System.out.println("Fecha: " + x.obtenerFecha() + "Id: "+ x.obtenerId());
		    System.out.println("Dirección de envío: "+ x.obtenerUsuario().obtenerDireccion());
		    System.out.println("Productos: ");
		    pro=x.obtenerCarrito();
		    for(Producto z: pro){
			System.out.println("Nombre: "+ z.obtenerNombre()+ "Precio: "+ z.obtenerPrecio());
		    }
		    numPedido++;
		    System.out.println("-------------------------------------------");
	    }
	}
	}catch(Exception e){
			    System.out.println("No se ha podido obtener el catálogo");
	}
    }

    
    public static void realizarPedido(Usuario u, List <Producto> catalogo, OrderSL srv){ //Esta función le pasará al servidor un array de int con las id de los productos que quiere pedir
	int flag = 0;
	int listaProductos[]= new int[100];
	int aux=0;
	boolean existe = false;
	
	for(Producto z: catalogo){
			System.out.println(z.obtenerNombre() + "	precio: " + z.obtenerPrecio() +"euros	id: " + z.obtenerId()); 
		    }
			    System.out.println("-------------------------------------------");	
	while(flag == 0){
	    System.out.println("Introduzca el id del producto");
	    System.out.println("Introduzca 0 para finalizar pedido");
	    System.out.println("Introduzca -1 para cancelar pedido");
	    Scanner sc = new Scanner(System.in);
	    int i = sc.nextInt();
	    switch(i){
	    case 0:
		flag=1;
		break;
	    case -1:
		System.out.println("Pedido cancelado");
		flag = 1;
		break;
	    default:
		for(Producto x:catalogo){
		    if(x.obtenerId() == i){
			existe = true; 
		    }
		}
		if(existe){
		    listaProductos[aux]=i;
		    aux++;
		    System.out.println("Producto añadido al pedido");
		}
		else{
		    System.out.println("El producto introducido no existe");
		}
	    }
	}
	if(listaProductos.length > 0){
	    try{
		srv.realizarPedido(listaProductos, u);
		System.out.println("Pedido realizado correctamente");
		srv.guardarCambios();
	    }catch (RemoteException e) {
		System.err.println("Error de comunicacion: " + e.toString());
	    }
	}
    }

    public static void añadirSaldo(Usuario u){
	float nuevoSaldo; 
	System.out.println("Saldo actual: "+ u.obtenerSaldo() +"euros \n");
	System.out.println("Saldo a añadir: ");
	Scanner sc = new Scanner(System.in);
	float aux = sc.nextFloat();
	nuevoSaldo=u.añadirSaldo(aux);
	System.out.println(" \n Nuevo saldo: "+ nuevoSaldo+"euros \n");

    }

    public static void modificarUsuario(Usuario usuario){
	int flag = 1;
	while(flag == 1){
	    System.out.println("1. Modificar nombre");
	    System.out.println("2. Modificar contraseña");
	    System.out.println("3. Modificar direccion");
	    System.out.println("4. Volver al menu");
	    Scanner sc = new Scanner(System.in);
	    int i = sc.nextInt();
	    System.out.println("-------------------------------------");
	    switch(i){
	    case 1:
		System.out.println("Introduzca nuevo nombre de usuario: ");
		Scanner sc2 = new Scanner(System.in);
		String nombre = sc2.nextLine();
		usuario.cambiarNombre(nombre);
		break;
	    case 2:
		System.out.println("Introduzca nueva contraseña: ");
		Scanner sc3 = new Scanner(System.in);
		String password = sc3.nextLine();
		usuario.cambiarContraseña(password);
		break;
	    case 3:
		System.out.println("Introduzca nueva direccion: ");
		Scanner sc4 = new Scanner(System.in);
		String direccion = sc4.nextLine();
		usuario.cambiarDireccion(direccion);
		break;
	    case 4:
		flag = 0;
		clear();
		break;
	    default:
		System.out.println("Opcion no valida");
	    }
	}
    }

    public static void añadirProducto(OrderSL srv){
	try{
	    System.out.println("Nombre del producto a añadir: ");
	    Scanner sc = new Scanner(System.in);
	    String nombre = sc.nextLine();
	    System.out.println("Precio del producto a añadir: ");
	    float precio = sc.nextFloat();
	    srv.crearProducto(nombre, precio);
	    System.out.println("Producto añadido correctamente");
	}catch (RemoteException e) {
            System.err.println("Error de comunicacion: " + e.toString());
        }
    }

   /* public static void obtenerPedidos(OrderSL srv){
	List <Pedido> pedidos;
	int numPedidos = 0;

	try{
	    pedidos = srv.obtenerPedidos();
	
	    for(Pedido x:pedidos){
		System.out.println("ID: " + x.obtenerId());
		System.out.println("Fecha: " + x.obtenerFecha());
		System.out.println("Usuario: " + x.obtenerUsuario().obtenerNombre());
		System.out.println("Direccion de envio: " + x.obtenerUsuario().obtenerDireccion());
		numPedidos++;
	    }
	    System.out.println("Pedidos totales: "+ numPedidos);
	}catch (RemoteException e) {
            System.err.println("Error de comunicacion: " + e.toString());
        }
    }*/
	public static void clear() {  
	    System.out.print("\033[H\033[2J");  
	    System.out.flush();  
	} 
}
