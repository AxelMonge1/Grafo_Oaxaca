/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package elementos;

import java.util.List;

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
}
