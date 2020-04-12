
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
	int flag4; //Bandera para la entrada de datos
	int numPedidos=0;
	int aux=0;
	List <Producto> catalogo;//Lista en la que guardaremos los objetos de la clase Producto
	List <Usuario> Usuarios;
	String nomUsu="";
	String password="";
	String direccion="";
	float saldoIni=0;
	int i=0;
	Scanner sc = new Scanner(System.in);
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
		do{	
		    flag4 = 1;
		    try{
			nomUsu = sc.nextLine();
			flag4 = 0;
		    }catch(InputMismatchException e){
			System.out.println("Error introduciendo datos"); 
		    }
		}while(flag4==1);
		existe = srv.existeUsuario(nomUsu);
		if(existe){
		    System.out.println("Introduzca contraseña: ");
		}
		else{
		    System.out.println("Introduzca contraseña para la creacion de un usuario: ");
		}
		do{
		    flag4 = 1;
		    try{
			Console console = System.console();
			password = new String(console.readPassword());
			flag4 = 0;
		    }catch(InputMismatchException e){
			System.out.println("Error introduciendo datos"); 
		    }
		}while(flag4==1);
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
		    do{

			flag4 = 1;
			try{
			    direccion = sc.nextLine();
			    flag4 = 0;
			}catch(InputMismatchException e){
			    System.out.println("Error introduciendo datos"); 
			}
		    }while(flag4==1);
		    System.out.println("Introduzca el saldo inicial para tu usuario: ");
			flag4 = 1;		    
			do{
			try{
			    saldoIni = sc.nextFloat();
			    flag4 = 0;
			}catch(InputMismatchException e){
			    System.out.println("Error introduciendo datos"); 
				sc.next();
			}
		    }while(flag4==1);
		    while(flag3==0){
			System.out.println("¿El usuario va a ser admin?");
			System.out.println("1.Sí");
			System.out.println("2.No");
			flag4 = 1;
			do{
			    try{
				aux = sc.nextInt();
				flag4 = 0;
			    }catch(InputMismatchException e){
				System.out.println("Error introduciendo datos"); 
			    }
			}while(flag4==1);
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
		flag4=1;		
		do{
		    try{
			i = sc.nextInt();
			flag4 = 0;
		    }catch(InputMismatchException e){
			System.out.println("Error introduciendo datos"); 
		    }
		}while(flag4==1);
		switch (i){
		case COMPROBAR_PEDIDOS:
		    clear();
		    numPedidos = verPedidos(usuario, srv); 
		    if (numPedidos==0)
			System.out.println("No existe ningún pedido vinculado a este usuario"); 
		    System.out.println("--------------------------------------------------------------------"); 
		    break;
		case MOSTRAR_CATÁLOGO:
		    clear();
		    System.out.println("--------------------------------------------------------------------"); 
			if(catalogo.size()==0)
				System.out.println("Catálogo vacío"); 
		    for(Producto z: catalogo){
			System.out.println(z.obtenerNombre() + "	precio: " + z.obtenerPrecio() +" €	id: " + z.obtenerId()); 
		    }							   			
		    System.out.println("---------------------------------------------------------------------"); 
		    break;
		case REALIZAR_PEDIDO:
		    clear();
			String nombre_usuario = usuario.obtenerNombre();
		    realizarPedido(usuario, catalogo, srv);
			srv.modificarUsuario(usuario, nombre_usuario);
			Thread.sleep(3000);
		    clear();
		    break;
		case MODIFICAR_DATOS_USUARIO:
		    clear();
			String nombre_usuario_antiguo = usuario.obtenerNombre();
		    modificarUsuario(usuario, srv); //Modifica los datos del usuario en el cliente y devuelve el usuario con los datos modificados
			srv.modificarUsuario(usuario, nombre_usuario_antiguo); //Avisa al servidor para que modifique el usuario
			srv.guardarCambios();
		    clear();
			System.out.println("Datos de usuario modificados correctamente");
		    break;
		case AÑADIR_SALDO:
		    clear();
			nombre_usuario = usuario.obtenerNombre();
		    añadirSaldo(usuario);
			srv.modificarUsuario(usuario, nombre_usuario);
		    srv.guardarCambios();
			Thread.sleep(3000);
		    clear();
		    break;
		case AÑADIR_PRODUCTO:
		    clear();
		    if(usuario.isAdmin()){
			añadirProducto(srv);
			srv.guardarCambios();  
			clear(); 
		    System.out.println("Producto añadido correctamente");	
			System.out.println("----------------------------------------------------"); 		    
		    }
		    else{
			System.out.println("Necesita ser administrador para realizar esta acción"); 	
			System.out.println("----------------------------------------------------"); 	
			}	
		    break;
		case MOSTRAR_TODOS_PEDIDOS:
		    clear();
		    if(usuario.isAdmin()){
			Usuarios = srv.obtenerUsuarios();
			for(Usuario usuario_x: Usuarios) {   //Obtenemos todos los usuarios y vamos mostrando sus pedidos
			    aux = verPedidos(usuario_x, srv);
			    numPedidos+=aux;	
			    System.out.println("-------------------------------------"); 		
			}
			if (numPedidos==0)												   				
				System.out.println("No existe ningún pedido"); 
			System.out.println("---------------------------------------"); 
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
	
    public static int verPedidos(Usuario u, OrderSL srv){ //Esta función llamará al servidor para que nos devuelva todos los pedidos que tenga el usuario
	int numPedido = 0;
	List <Producto> pro;
	List <Pedido> p;
	try{
	p=srv.obtenerPedidos();
	for (Pedido x: p){
		if(u.obtenerNombre().equals(x.obtenerUsuario().obtenerNombre())){
		    numPedido++;
		    System.out.println("PEDIDO NUMERO "+ numPedido);
		    System.out.println("Realizado por: "+ u.obtenerNombre());
		    System.out.println("Fecha: " + x.obtenerFecha() + "  Id: "+ x.obtenerId());
		    System.out.println("Dirección de envío: "+ x.obtenerDireccion());
		    System.out.println("Productos: ");
		    pro=x.obtenerCarrito();
		    for(Producto z: pro){
			System.out.println("	Nombre: "+ z.obtenerNombre()+ "  Precio: "+ z.obtenerPrecio());
		    }
			System.out.println("Precio del pedido: "+ x.obtenerPrecio() + " €");
		    System.out.println("-------------------------------------------");
	    }
	}
	}catch(Exception e){
			    System.out.println("No se ha podido obtener el catálogo");
	}
	return numPedido;
    }

    
    public static void realizarPedido(Usuario u, List <Producto> catalogo, OrderSL srv){ //Esta función le pasará al servidor un array de int con las id de los productos que quiere pedir
	int flag = 0;
	int listaProductos[]= new int[100];
	int aux=0;
	boolean existe = false;
	int i=0;
	int flag4 = 1;
	float precio_total = 0;
	boolean cancelado = false;
	for(Producto z: catalogo){
			System.out.println(z.obtenerNombre() + "	precio: " + z.obtenerPrecio() +" €	id: " + z.obtenerId()); 
		    }
			    System.out.println("-------------------------------------------");	
	while(flag == 0){
	    System.out.println("Introduzca el id del producto");
	    System.out.println("Introduzca 0 para finalizar pedido");
	    System.out.println("Introduzca -1 para cancelar pedido");
	    do{
		try{
		Scanner sc = new Scanner(System.in);	
		i = sc.nextInt();
		flag4 = 0;
		}catch(InputMismatchException e){
			System.out.println("Error introduciendo datos"); 
		}
	}while(flag4==1);
	    switch(i){
	    case 0:
		flag=1;
		break;
	    case -1:
		System.out.println("Pedido cancelado");
		cancelado = true;
		flag = 1;
		break;
	    default:
		for(Producto x:catalogo){
		    if(x.obtenerId() == i){
			existe = true;
			precio_total+=x.obtenerPrecio(); 
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
		if (precio_total>u.obtenerSaldo()){
				System.out.println("Saldo insuficiente");
				cancelado=true;
		}
		if(!cancelado){
			srv.realizarPedido(listaProductos, u);
			u.añadirSaldo(-precio_total);
			System.out.println("--------------------------------");
			System.out.println("Precio total: "+precio_total+" €");
			System.out.println("Pedido realizado correctamente");
			srv.guardarCambios();
		}
	    }catch (RemoteException e) {
		System.err.println("Error de comunicacion: " + e.toString());
	    }
	}
    }

    public static void añadirSaldo(Usuario u){
	float nuevoSaldo; 
	float aux=0;
	int flag4 = 1;
	System.out.println("Saldo actual: "+ u.obtenerSaldo() +" € ");
	System.out.println("Saldo a añadir: ");
	do{
		try{
		Scanner sc = new Scanner(System.in);
		aux = sc.nextFloat();
		flag4 = 0;
		}catch(InputMismatchException e){
			System.out.println("Error introduciendo datos"); 
			//sc.next();
		}
	}while(flag4==1);
	nuevoSaldo=u.añadirSaldo(aux);
	System.out.println(" \n Nuevo saldo: "+ nuevoSaldo+" € ");

    }

    public static void modificarUsuario(Usuario usuario, OrderSL srv){
	int flag = 1;
	int flag4 = 1;
	String nombre="";
	String password="";
	String direccion="";
	int i=0;
	boolean existe = false;
	while(flag == 1){
	    System.out.println("1. Modificar nombre");
	    System.out.println("2. Modificar contraseña");
	    System.out.println("3. Modificar direccion");
	    System.out.println("4. Volver al menu");
	  		flag4=1;		 
	 do{
		try{
		Scanner sc = new Scanner(System.in);
		i = sc.nextInt();
		flag4 = 0;
		}catch(InputMismatchException e){
			System.out.println("Error introduciendo datos"); 
		}
	}while(flag4==1);
	    System.out.println("-------------------------------------");
	    switch(i){
	    case 1:
		System.out.println("Introduzca nuevo nombre de usuario: ");
		flag4=1;				
		do{
		try{
		Scanner sc = new Scanner(System.in);
		nombre = sc.nextLine();
		flag4 = 0;
		}catch(InputMismatchException e){
			System.out.println("Error introduciendo datos"); 
		}
		try{
		existe = srv.existeUsuario(nombre);
		if(existe){
			System.out.println("El nombre de usuario ya existe"); 		//Comprobamos si existe el nuevo nombre de usuario
			flag4=1;
		}
		}catch(Exception e){
			System.out.println("Excepción comprobando si existe el usuario");		
		}
			
	}while(flag4==1);
		usuario.cambiarNombre(nombre);
		break;
	    case 2:
		System.out.println("Introduzca nueva contraseña: ");
		flag4=1;				
		do{
		try{
		Console console = System.console();
		 password = new String(console.readPassword());
		flag4 = 0;
		}catch(InputMismatchException e){
			System.out.println("Error introduciendo datos"); 
		}
	}while(flag4==1);
		usuario.cambiarContraseña(password);
		break;
	    case 3:
		System.out.println("Introduzca nueva direccion: ");
		flag4=1;				
		do{
		try{
		Scanner sc = new Scanner(System.in);
		direccion = sc.nextLine();
		flag4 = 0;
		}catch(InputMismatchException e){
			System.out.println("Error introduciendo datos"); 
		}
	}while(flag4==1);
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
	int flag4=1;
	int flag2=1;
	String nombre="";
	float precio=0;
	boolean existe=false;
	    System.out.println("Nombre del producto a añadir: ");
	do{
		try{
		Scanner sc = new Scanner(System.in);
		nombre = sc.nextLine();
		flag4 = 0;
		}catch(InputMismatchException e){
			System.out.println("Error introduciendo datos"); 
		}
		try{
		existe = srv.existeProducto(nombre);
		if(existe){
			System.out.println("El nombre del producto ya existe"); 		//Comprobamos si existe el producto
			flag4=1;
		}
		}catch(Exception e){
			System.out.println("Excepción comprobando si existe el producto");		
		}
	}while(flag4==1);
	    System.out.println("Precio del producto a añadir: ");
	flag4=1;    
	do{
		try{
		Scanner sc = new Scanner(System.in);
		precio = sc.nextFloat();
		flag4 = 0;
		}catch(InputMismatchException e){
			System.out.println("Error introduciendo datos"); 
		}
	}while(flag4==1);
		try{	 	
		srv.crearProducto(nombre, precio);
		}catch(Exception e){
		}
    }
	public static void clear() {  
	    System.out.print("\033[H\033[2J");  
	    System.out.flush();  
	} 
}
