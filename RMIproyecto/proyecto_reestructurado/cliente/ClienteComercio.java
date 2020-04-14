
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.lang.*;
import java.io.*;


class ClienteComercio {
    //Constantes que utilizaremos para saber qué acción del menú quiere realizar el cliente
    public static final int COMPROBAR_PEDIDOS = 1;
    public static final int MOSTRAR_CATÁLOGO = 2;
    public static final int REALIZAR_PEDIDO = 3;
    public static final int MODIFICAR_DATOS_USUARIO = 4;
    public static final int AÑADIR_SALDO = 5;
    public static final int AÑADIR_PRODUCTO = 6;
    public static final int MOSTRAR_TODOS_PEDIDOS = 7;
    public static final int SALIR = 8;
    
    static public void main (String args[]) {

	boolean existe = false; //Boolean que modificaremos dependiendo de si el nombre de usuario existe o no
	boolean administrador = false; //Boolean que indicará si el usuario es administrador o no
	Usuario usuario = null; //Declaramos el objeto usuario para mas tarde usarlo
	int flag = 1; //Bandera para la salida del menu
	int flag2 = 1; //Bandera para la salida de creación de usuario e inicio de sesion
	int flag3 = 0; //Bandera para la salida de la concesión de administrador al usuario
	int flag4; //Bandera para la entrada de datos
	int numPedidos=0;
	int aux=0;
	List <Producto> catalogo;//Lista en la que guardaremos los objetos de la clase Producto
	List <Usuario> Usuarios;//Lista en la que guardaremos los objetos de tipo usuario que hay en el servidor
	String nomUsu=""; //Declaramos e inicializamos las variables que necesitaremos
	String password="";
	String direccion="";
	float saldoIni=0;
	int i=0;
	Scanner sc = new Scanner(System.in); //Objeto de la clase Scanner con el que capturaremos la entrada por teclado
        if (args.length!=2) { //Comprobamos que el cliente ha introducido la cantidad de argumentos necesarios
            System.err.println("Uso: ClienteComercio hostregistro numPuertoRegistro");
            return;
        }

	if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
	    //Obtenemos del servidor un objeto de la clase OrderSL.
            OrderSL srv = (OrderSL) Naming.lookup("//" + args[0] + ":" + args[1] + "/OrderSL"); 
	    clear();
	    //Este while se encargará de que las operaciones de inicio de sesión se impriman en bucle mientras el cliente no introduzca la contraseña correctamente
	    while(flag2 == 1){
		/*A partir de este punto y durante el resto del código, cada vez que pidamos una entrada de texto
		 por el teclado por parte del cliente realizaremos una estructura do while con un try-catch dentro del do.
		 Con esto conseguiremos comprobar que el dato introducido por el teclado es del mismo tipo que está pidiendo el
		 programa, en caso de no ser así, nos avisará y tendremos que volver a introducir el valor por teclado.
		 La variable flag4 la utilizaremos siempre para salir del bucle una vez comprobemos que el valor introducido por
		 el teclado es del tipo correcto*/
		do{
		    System.out.println("Introduzca nombre de usuario: ");
		    flag4 = 1;
		    try{
			nomUsu = sc.nextLine(); //Obtenemos el nombre de usuario
			if(nomUsu.length()!=0)
			    flag4 = 0; //Cambiamos la bandera para salir del bucle
		    }catch(InputMismatchException e){
			System.out.println("Error introduciendo datos"); 
			sc.next();
		    }
		}while(flag4==1);
		existe = srv.existeUsuario(nomUsu);
		/*Comprobamos si ese usuario ya existe en la base de datos del servidor. En caso de que exista
		 pediremos una contraseña para iniciar sesión. Si no existe, pediremos una contraseña para
		 la creación de un nuevo usuario con ese nombre*/
		do{
		    if(existe){
			System.out.println("Introduzca contraseña: ");
		    }
		    else{
			System.out.println("Introduzca contraseña para la creacion de un usuario: ");
		    }
		    flag4 = 1;
		    try{
			Console console = System.console();
			password = new String(console.readPassword());
			if(password.length()!=0)
			    flag4 = 0;
		    }catch(InputMismatchException e){
			System.out.println("Error introduciendo datos"); 
		    }
		}while(flag4==1);
		if(existe){
		    /*Si el usuario existía, con el nombre y la contraseña intentaremos obtener el objeto de tipo usuario del servidor.
		     En caso de que la contraseña fuera correcta y hayamos obtenido un objeto del tipo usuario, cambiaremos el valor de 
		     flag2 para que no se repita el bucle de la petición de contraseña*/
		    usuario = srv.iniciarSesion(nomUsu, password);  
		    if(usuario !=null){
			flag2=0;
		    }
		    else{
			System.out.println("Contraseña incorrecta");
		    }
		}
		else{
		    /*Si el usuario no existía, le pediremos al cliente que introduzca por teclado diversa información por
		     teclado con la estructura de do-while anteriormente explicada*/
		    do{
			System.out.println("Introduzca la dirección de envío para vincularla a tu usuario: ");
			flag4 = 1;
			try{
			    direccion = sc.nextLine(); //Obtenemos la dirección de envío del usuario
			    if(direccion.length()!=0)
				flag4 = 0;
			}catch(InputMismatchException e){
			    System.out.println("Error introduciendo datos"); 
			}
		    }while(flag4==1);
		    flag4 = 1;		    
		    do{
			System.out.println("Introduzca el saldo inicial para tu usuario: ");
			try{
			    saldoIni = sc.nextFloat(); //Obtenemos el saldo inicial del usuario
			    flag4 = 0;
			}catch(InputMismatchException e){
			    System.out.println("Error introduciendo datos"); 
			    sc.next();
			}
		    }while(flag4==1);
		    while(flag3==0){
			flag4 = 1;
			do{
			    System.out.println("¿El usuario va a ser admin?");
			    System.out.println("1.Sí");
			    System.out.println("2.No");
			    try{
				aux = sc.nextInt(); //Obtenemos si el cliente quiere que el usuario sea admin o no
				flag4 = 0;
			    }catch(InputMismatchException e){
				System.out.println("Error introduciendo datos"); 
				sc.next();
			    }
			}while(flag4==1);
			//En este switch asignaremos al usuario el permiso de administrador según haya introducido el cliente.
			//Si ha introducido un valor que no es ni 1 ni 2, se le volverá a pedir que introduzca un valor
			//Flag3 es la bandera que controla el bucle
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
		    //Creamos el usuario en la base de datos con los valores introducidospor teclado por el cliente
		    usuario = srv.crearUsuario(nomUsu, password, saldoIni, direccion, administrador); 
		    flag2=0;
		    System.out.println("Registro de usuario completado");
		}
	    }
	    
	    //Imprimimos por pantalla el archivo "OrderSL.txt" que es donde se encuentra el logo del programa
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
		srv.escribirDatosBBDD(); //Avisamos al servidor de que escriba en su BBDD los posibles cambios que se hayan hecho
		usuario=srv.iniciarSesion(usuario.obtenerNombre(), usuario.obtenerContraseña()); //Comprobamos si la sesión ha caducado comparando
		if(usuario==null){                                                            //nuestro usuario con el de la BBDD del servidor
		    clear();
		    System.out.println("La sesión ha caducado");
		    Thread.sleep(2000);
		    i = SALIR; //Para que salga del programa si la sesión ha caducado
		}			
		else{
		    System.out.println("¡Bienvenido "+usuario.obtenerNombre()+"!");
		    System.out.println("--------------------------------------------------------------------");		
		}
		//Imprimimos por pantalla el archivo "OrderSL.txt" que es donde se encuentra el menú del programa
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
		if(i!=SALIR){		
		    do{
			try{
			    i = sc.nextInt(); //La variable i contendrá el valor de la acción del menú que quiere ejecutar el cliente. 
			    flag4 = 0;
			}catch(InputMismatchException e){
			    System.out.println("Error introduciendo datos"); 
			    sc.next();
			}
		    }while(flag4==1);
		}
		catalogo=srv.obtenerProductos(); //Obtenemos el catalogo de productos actualizado
		switch (i){ //Según la opción que haya elegido el cliente ejecutaremos un case u otro
		case COMPROBAR_PEDIDOS:
		    clear(); //Limpiamos el terminal
		    numPedidos = verPedidos(usuario, srv); //Llamamos a la función que imprime los pedidos vinculados a este usuario. Devuelve el número de pedidos
		    if (numPedidos==0)
			System.out.println("No existe ningún pedido vinculado a este usuario"); 
		    System.out.println("--------------------------------------------------------------------"); 
		    break;
		case MOSTRAR_CATÁLOGO:
		    clear(); //Limpiamos el terminal
		    System.out.println("--------------------------------------------------------------------"); 
		    if(catalogo.size()==0) 
			System.out.println("Catálogo vacío"); 
		    for(Producto z: catalogo){ //Imprimimos el catálogo por pantalla
			System.out.println(z.obtenerNombre() + "	precio: " + z.obtenerPrecio() +" €	id: " + z.obtenerId()); 
		    }							   			
		    System.out.println("---------------------------------------------------------------------"); 
		    break;
		case REALIZAR_PEDIDO:
		    clear();
		    usuario=realizarPedido(usuario, catalogo, srv); //Realizamos un pedido y devolvemos el usuario
		    Thread.sleep(3000);                       
		    clear();
		    break;
		case MODIFICAR_DATOS_USUARIO:
		    clear();
		    nomUsu= usuario.obtenerNombre();
		    usuario=modificarUsuario(nomUsu, srv); //Modifica los datos del usuario en el cliente
		    srv.modificarPedido(usuario, nomUsu); //Modifica los pedidos del usuario por si se ha cambiado el nombre
		    clear();
		    System.out.println("Datos de usuario modificados correctamente");
		    break;
		case AÑADIR_SALDO:
		    clear();
		    usuario=srv.iniciarSesion(usuario.obtenerNombre(), usuario.obtenerContraseña()); //Obtenemos el usuario actualizado
		    nomUsu = usuario.obtenerNombre();                                       //por si se ha realizado algún cambio desde
		    usuario=añadirSaldo(nomUsu,srv);                                       //otra máquina
		    Thread.sleep(3000);
		    clear();
		    break;
		case AÑADIR_PRODUCTO:
		    clear();
		    if(usuario.isAdmin()){ //Comprobamos que el usuario sea admin
			añadirProducto(srv);
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
		    System.out.println("ID PEDIDO: "+ x.obtenerId());
		    System.out.println("Realizado por: "+ u.obtenerNombre());
		    System.out.println("Fecha: " + x.obtenerFecha());
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

    
    public static Usuario realizarPedido(Usuario u, List <Producto> catalogo, OrderSL srv){ //Esta función le pasará al servidor un array de int con las id de los productos que quiere pedir
	Usuario usuario=u;
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
		listaProductos[aux]=i;
		aux++;
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
	if(listaProductos[0]!=0 ){
	    try{
		if (precio_total>srv.obtenerSaldo(u.obtenerNombre()) && cancelado==false){
		    System.out.println("Saldo insuficiente");
		    cancelado=true;
		}
		if(!cancelado){
		    srv.realizarPedido(listaProductos, u);
		    usuario=srv.añadirSaldo(u.obtenerNombre(), -precio_total);
		    System.out.println("--------------------------------");
		    System.out.println("Precio total: "+precio_total+" €");
		    System.out.println("Pedido realizado correctamente");
		}
	    }catch (RemoteException e) {
		System.err.println("Error de comunicacion: " + e.toString());
	    }
	}
	return usuario;
    }

    public static Usuario añadirSaldo(String nombreUsu, OrderSL srv){
	Usuario usuario=null;
	float nuevoSaldo; 
	float aux=0;
	int flag4 = 1;
	try{
	    System.out.println("Saldo actual: "+ srv.obtenerSaldo(nombreUsu) +" € ");
	    System.out.println("Saldo a añadir: ");
	}catch(RemoteException e){
	    System.err.println("Error de comunicacion: " + e.toString());
	}
	do{
	    try{
		Scanner sc = new Scanner(System.in);
		aux = sc.nextFloat();
		flag4 = 0;
	    }catch(InputMismatchException e){
		System.out.println("Error introduciendo datos"); 
	    }
	}while(flag4==1);
	try{
	usuario=srv.añadirSaldo(nombreUsu,aux);
	System.out.println(" \n Nuevo saldo: "+ usuario.obtenerSaldo()+" € ");
	}catch(RemoteException e){
	    System.err.println("Error de comunicacion: " + e.toString());
	}
	return usuario;
    }

    public static Usuario modificarUsuario(String nomUsu, OrderSL srv){
	Usuario usuario=null;
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
		flag4=1;				
		do{
		    System.out.println("Introduzca nuevo nombre de usuario: ");
		    try{
			Scanner sc = new Scanner(System.in);
			nombre = sc.nextLine();
			if(nombre.length()!=0)
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
		try{
		    usuario=srv.cambiarNombre(nomUsu,nombre);
		}catch(RemoteException e){
		    System.err.println("Error de comunicacion: " + e.toString()); 
		}
		break;
	    case 2:
		flag4=1;				
		do{
		    System.out.println("Introduzca nueva contraseña: ");
		    try{
			Console console = System.console();
			password = new String(console.readPassword());
			if(password.length()!=0)
			    flag4 = 0;
		    }catch(InputMismatchException e){
			System.out.println("Error introduciendo datos"); 
		    }
		}while(flag4==1);
		try{
		    usuario=srv.cambiarContraseña(nomUsu,password);
		}catch(RemoteException e){
		    System.err.println("Error de comunicacion: " + e.toString());
		}
		break;
	    case 3:
		flag4=1;				
		do{
		    System.out.println("Introduzca nueva direccion: ");
		    try{
			Scanner sc = new Scanner(System.in);
			direccion = sc.nextLine();
			if(direccion.length()!=0)
			    flag4 = 0;
		    }catch(InputMismatchException e){
			System.out.println("Error introduciendo datos"); 
		    }
		}while(flag4==1);
		try{
		usuario=srv.cambiarDireccion(nomUsu,direccion);
		}catch(RemoteException e){
		System.err.println("Error de comunicacion: " + e.toString());
	    }
		break;
	    case 4:
		flag = 0;
		clear();
		break;
	    default:
		System.out.println("Opcion no valida");
	    }
	}
	return usuario;
    }

    public static void añadirProducto(OrderSL srv){
	int flag4=1;
	int flag2=1;
	String nombre="";
	float precio=0;
	boolean existe=false;
	do{
	    System.out.println("Nombre del producto a añadir: ");
	    try{
		Scanner sc = new Scanner(System.in);
		nombre = sc.nextLine();
		if(nombre.length()!=0)
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
	flag4=1;    
	do{
	    System.out.println("Precio del producto a añadir: ");		
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
