
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;


class ClienteComercio {
    static public void main (String args[]) {
	public static int COMPROBAR_PEDIDOS = 1;
	public static int MOSTRAR_CATÁLOGO = 2;
	public static int REALIZAR_PEDIDO = 3;
	public static int MODIFICAR_DATOS_USUARIO = 4;
	public static int SALIR = 5;
	    
	int flag = 1; //Bandera para la salida del menu
	List <Producto> catalogo; //Lista en la que guardaremos los objetos de la clase Producto
	
        if (args.length!=3) {
            System.err.println("Uso: ClienteComercio hostregistro numPuertoRegistro Usuario");
            return;
        }

       if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            OrderSL srv = (OrderSL) Naming.lookup("//" + args[0] + ":" + args[1] + "/OrderSL");
            Usuario u = srv.crearUsuario(args[2]); //Obtenemos el usuario que tiene el nombre introducido por el cliente
	    catalogo=srv.ObtenerCatalogo(); //La función ObtenerCatalogo() devolvera una lista de clase Producto con todos los productos disponibles 
	    while( flag == 1){ //El bucle se seguirá realizando e imprimiendo el menú hasta que el cliente quiera salir
		FileOutputStream os = new FileOutputStream("Menu.txt");
		PrintStream ps = new PrintStream(os); //Imprimimos el menú
		Scanner sc = new Scanner(System.in);
		int i = sc.nextInt();
		switch (i){
		case COMPROBAR_PEDIDOS:
		    ComprobarPedidos(u); 
		    break;
		case MOSTRAR_CATÁLOGO:
		  
		    for(Producto z: catalogo)
			System.out.println(z.getNombre() + "	precio: " + z.getPrecio() +"euros	id: " z.getId() "\n" ); //Imprimimos el catálago
		    break;
		case REALIZAR_PEDIDO:
		    RealizarPedido(u);
		    break;
		case MODIFICAR_DATOS_USUARIO:
		    
		    break;
		case SALIR:
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
	
    public void ComprobarPedidos(Usuario u){ //Esta función llamará al servidor para que nos devuelva todos los pedidos que tenga el usuario
	List <Pedido> p;
	p=u.ObtenerPedidos();
	for (Pedido x : p){

	}
    }
    public void RealizarPedido(Usuario u){ //Esta función le pasará al servidor un array de int con las id de los productos que quiere pedir
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
	u.RealizarPedido(listaProductos);
}
