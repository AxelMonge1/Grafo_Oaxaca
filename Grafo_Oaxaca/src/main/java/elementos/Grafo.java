/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package elementos;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author axelm
 */
public class Grafo {
    private List<Nodo> vertices;
    private List<Arista> aristas;
    
    public void agregarNodo(String municipio){
        Nodo nodo = new Nodo(municipio);
        vertices.add(nodo);
    }
    
    public void agregarArista(String origen, String destino, float peso) throws IllegalArgumentException{
        Nodo nodoOrigen = buscarNodo(origen);
        Nodo nodoDestino = buscarNodo(destino);
        if(nodoOrigen == null || nodoDestino == null){
            throw new IllegalArgumentException("No existe el origen o el destino (hacer debug)");
        }
        Arista arista = new Arista(nodoOrigen, nodoDestino, peso);
        aristas.add(arista);
    }
    
    public Nodo buscarNodo(String municipio){
        Nodo nodo = null;
        for(Nodo v : vertices){
            if(v.getMunicipio().equalsIgnoreCase(municipio)){
                nodo = v;
            }
        }
        return nodo;
    }
    
    public List<Nodo> buscarRuta(String origen, String destino){
        Nodo nodoOrigen = buscarNodo(origen);
        Nodo nodoDestino = buscarNodo(destino);
        List<Nodo> ruta = new ArrayList<>();
        if(nodoOrigen == null || nodoDestino == null){
            throw new IllegalArgumentException("No existe el origen o el destino (hacer debug)");
        }
        if(buscarRutaDFS(ruta, nodoOrigen, nodoDestino)){
            return ruta;
        }else{
            return null;
        }
    }
    
    private boolean buscarRutaDFS(List<Nodo> ruta, Nodo nodoOrigen, Nodo nodoDestino){
        ruta.add(nodoOrigen);
        if(nodoOrigen.getMunicipio().equals(nodoDestino.getMunicipio())){
            return true;
        }
        Stack<Nodo> pila = new Stack<>();
        ArrayList<Nodo> nodosVisitados = new ArrayList<>();
        pila.add(nodoOrigen);
        while(!pila.isEmpty()){
            Nodo actual = pila.pop();
            if(nodosVisitados.contains(actual)){
                continue;
            }
            if(actual.equals(nodoDestino)){
                ruta.addAll(pila);
                ruta.add(nodoDestino);
                return true;
            }else{
                nodosVisitados.add(actual);
                for(Nodo n : actual.getNodosAdyacentes()){
                    if(!pila.contains(n)){
                        pila.add(n);
                    }
                }
            }
        }
        return false;
    }
}
