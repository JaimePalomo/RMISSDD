
import java.rmi.*;
import java.rmi.*;
import java.util.*;
import java.rmi.server.*;
import java.io.*;


interface OrderSL extends Remote {
    Usuario crearUsuario(String nombre, String contraseña, float saldo, String direccion, boolean admin) throws RemoteException;
    void crearProducto(String nombre, float precio) throws RemoteException;
    void realizarPedido(int listaProductos[], Usuario usuario) throws RemoteException;
    Usuario iniciarSesion(String nombre, String contraseña) throws RemoteException;
    void leerDatosBBDD() throws RemoteException;
    void escribirDatosBBDD() throws RemoteException;
    boolean existeUsuario(String nombre) throws RemoteException;
    boolean existeProducto(String nombre) throws RemoteException;
    List<Producto> obtenerProductos() throws RemoteException;
    List<Pedido> obtenerPedidos() throws RemoteException;
    List<Usuario> obtenerUsuarios() throws RemoteException;
    Usuario añadirSaldo(String nomUsu, float dinero)  throws RemoteException;
    Usuario cambiarNombre(String nomUsu, String nuevo_nombre)  throws RemoteException;
    Usuario cambiarDireccion(String nomUsu, String nueva_direccion)  throws RemoteException;
    Usuario cambiarContraseña(String nomUsu, String nueva_contraseña)  throws RemoteException;
    float obtenerSaldo(String nomUsu)  throws RemoteException;
    void modificarPedido(Usuario usuario_nuevo, String nombre_usuario_antiguo) throws RemoteException;
}
