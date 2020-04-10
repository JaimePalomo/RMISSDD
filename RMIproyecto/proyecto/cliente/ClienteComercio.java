
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.lang.*;


class ClienteComercio {
    static public void main (String args[]) {
	public static int COMPROBAR_PEDIDOS = 1;
	public static int MOSTRAR_CATÁLOGO = 2;
	public static int REALIZAR_PEDIDO = 3;
	public static int MODIFICAR_DATOS_USUARIO = 4;
	public static int AÑADIR_SALDO = 5;
	public static int SALIR = 6;
	boolean existe;
	boolean administrador;
	Usuario usuario;
	int flag = 1; //Bandera para la salida del menu
	int flag2 = 1; //Bandera para la salida de creación de usuario e inicio de sesion
	int flag3 = 1; //Bandera para la salida de la concesión de administrador al usuario
	List <Producto> catalogo; //Lista en la que guardaremos los objetos de la clase Producto
	
        if (args.length!=2) {
            System.err.println("Uso: ClienteComercio hostregistro numPuertoRegistro");
            return;
        }

       if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            OrderSL srv = (OrderSL) Naming.lookup("//" + args[0] + ":" + args[1] + "/OrderSL");
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
		Scanner sc = new Scanner(System.in);
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
		    Scanner sc = new Scanner(System.in);
		    String direccion = sc.nextLine();
		    System.out.println("Introduzca el saldo inicial para tu usuario: ");
		    Scanner sc = new Scanner(System.in);
		    float saldoIni = sc.nextFloat();
		    while(flag3==0){
			System.out.println("¿El usuario va a ser admin?");
			System.out.println("1.Sí");
			System.out.println("2.No");
			Scanner sc = new Scanner(System.in);
			int aux = sc.nextInt();
			switch (aux){
			case 1:
			    administrador=1;
			    break;
			case 2:
			    administrador=0;
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
	    
            
	    catalogo=srv.obtenerCatalogo(); //La función ObtenerCatalogo() devolvera una lista de clase Producto con todos los productos disponibles 
	    while( flag == 1){ //El bucle se seguirá realizando e imprimiendo el menú hasta que el cliente quiera salir
		FileOutputStream os = new FileOutputStream("Menu.txt");
		PrintStream ps = new PrintStream(os); //Imprimimos el menú
		Scanner sc = new Scanner(System.in);
		int i = sc.nextInt();
		switch (i){
		case COMPROBAR_PEDIDOS:
		    comprobarPedidos(u); 
		    break;
		case MOSTRAR_CATÁLOGO:
		  
		    for(Producto z: catalogo)
			System.out.println(z.getNombre() + "	precio: " + z.getPrecio() +"euros	id: " z.getId() "\n" ); //Imprimimos el catálago
		    break;
		case REALIZAR_PEDIDO:
		    realizarPedido(u, catalogo, srv);
		    break;
		case MODIFICAR_DATOS_USUARIO:
		    
		    break;
		case AÑADIR_SALDO:
		    añadirSaldo(u);
		    srv.guardarCambios();
		    break;
		case SALIR:
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
	
    public void comprobarPedidos(Usuario u){ //Esta función llamará al servidor para que nos devuelva todos los pedidos que tenga el usuario
	int numPedido = 1;
	List <Producto> pro;
	List <Pedido> p;
	p=u.obtenerPedidos();
	for (Pedido x : p){
	    System.out.println("PEDIDO NUMERO "+ numPedido);
	    System.out.println("Fecha: " + x.obtenerFecha() + "Id: "+ x.obtenerID());
	    System.out.println("Dirección de envío: "+ x.usuario.obtenerDireccion());
	    System.out.println("Productos: ");
	    pro=x.obtenerCarrito();
	    for(Producto z : pro){
		System.out.println("Nombre: "+ z.obtenerNombre()+ "Precio: "+ z.obtenerPrecio());
	    }
	    numPedido++;
	    System.out.println("-------------------------------------------");
	}
    }
    
    public void realizarPedido(Usuario u, List <Producto> catalogo, OrderSL srv){ //Esta función le pasará al servidor un array de int con las id de los productos que quiere pedir
	flag = 0;
	int listaProductos[];
	int aux=0;
	while(flag == 0){
	    System.out.println("Introduzca el id del producto \n");
	    System.out.println("Introduzca 0 para finalizar pedido \n");
	    Scanner sc = new Scanner(System.in);
	    int i = sc.nextInt();
	    switch(i){
	    case 0:
		flag=1;
		break;
	    default:
		listaProductos[aux]=i;
		aux++;
	    }
	}
	
	srv.realizarPedido(listaProductos, u);
	srv.guardarCambios();
    }

    public void añadirSaldo(Usuario u){
	String nuevoSaldo; 
	System.out.println("Saldo actual: "+ u.obtenerSaldo() +"euros \n");
	System.out.println("Saldo a añadir: ");
	Scanner sc = new Scanner(System.in);
	float aux = sc.nextFloat();
	nuevoSaldo=u.añadirSaldo(aux);
	System.out.println(" \n Nuevo saldo: "+ nuevoSaldo+"euros \n");

    }

    public void modificarUsuario(Usuario usuario){

	System.out.println("1. Modificar nombre");
	System.out.println("2. Modificar contraseña");
	System.out.println("3. Modificar direccion");

	Scanner sc = new Scanner(System.in);
	int i = sc.nextInt();

	switch(i){
	case 1:
	    System.out.println("Introduzca nuevo nombre de usuario: ");
	    Scanner sc = new Scanner(System.in);
	    String nombre = sc.nextLine();
	    usuario.cambiarNombre(nombre);
	    break;
	case 2:
	    System.out.println("Introduzca nueva contraseña: ");
	    Scanner sc = new Scanner(System.in);
	    String password = sc.nextLine();
	    usuario.cambiarContraseña(password);
	    break;
	case 3:
	    System.out.println("Introduzca nueva direccion: ");
	    Scanner sc = new Scanner(System.in);
	    String direccion = sc.nextLine();
	    usuario.cambiarDireccion(direccion);
	    break;
	}
    }
}

    
