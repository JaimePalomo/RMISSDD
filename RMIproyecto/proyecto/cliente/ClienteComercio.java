
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;


class ClienteComercio {
    static public void main (String args[]) {
	int flag = 1;
	List <Producto> catalogo;
	
        if (args.length!=3) {
            System.err.println("Uso: ClienteComercio hostregistro numPuertoRegistro Usuario");
            return;
        }

       if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            OrderSL srv = (OrderSL) Naming.lookup("//" + args[0] + ":" + args[1] + "/OrderSL");
            Usuario u = srv.crearUsuario(args[2]);
	    catalogo=srv.ObtenerCatalogo();
	    while( flag == 1){
		FileOutputStream os = new FileOutputStream("Menu.txt");
		PrintStream ps = new PrintStream(os);
		Scanner sc = new Scanner(System.in);
		int i = sc.nextInt();
		switch (i){
		case 1:
		    ComprobarPedidos(u);
		    break;
		case 2:
		  
		    for(Producto z: catalogo)
			System.out.println(z.getNombre() + "id:" z.getId() "\n" );
		    break;
		case 3:
		    RealizarPedido(u);
		    break;
		case 4:
		    
		    break;
		case 5:
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

    public void ComprobarPedidos(Usuario u){
	List <Pedido> p;
	p=u.ObtenerPedidos();
	for (Pedido x : p){

	}
    }
    public void RealizarPedido(Usuario u){
	flag = 0;
	int listaProductos[];
	int aux=0;
	while(flag == 0){
	    System.out.println("Introduzca el id del producto \n");
	    System.out.println("Introduzca 100 para finalizar pedido \n");
	    Scanner sc = new Scanner(System.in);
	    int i = sc.nextInt();
	    switch(i){
	    case 100:
		flag=1;
		break;
	    default:
		listaProductos[aux]=i;
		aux++;		
	    }
	}
	u.RealizarPedido(listaProductos);
}
