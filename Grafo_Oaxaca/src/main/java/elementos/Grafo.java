/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package elementos;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author axelm
 */
public class Grafo {
    private List<Nodo> vertices = new ArrayList<>();
    private List<Arista> aristas = new ArrayList<>();
    
    public void agregarNodo(String municipio, int x, int y){
        Nodo nodo = new Nodo(municipio, x, y);
        vertices.add(nodo);
    }

    public List<Nodo> getVertices() {
        return vertices;
    }

    public void setVertices(List<Nodo> vertices) {
        this.vertices = vertices;
    }

    public List<Arista> getAristas() {
        return aristas;
    }

    public void setAristas(List<Arista> aristas) {
        this.aristas = aristas;
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
    
    public void resetearColores(){
        for(Nodo v : vertices){
            v.setColor(Color.WHITE);
        }
    }
    
    public List<Nodo> getNodosAdyacentesDe(Nodo nodo){
        List<Nodo> nodos = new ArrayList<>();
        for(Nodo v : vertices){
            if(v.getMunicipio().equalsIgnoreCase(nodo.getMunicipio())){
                nodos.add(v);
            }
        }
        if(!nodos.isEmpty()){
            Nodo aux = nodos.getFirst();
            nodos = aux.getNodosAdyacentes();
        }
        return nodos;
    }
    
    public List<Arista> getAristasDe(Nodo nodo){
        return nodo.getAristas();
    }
}
