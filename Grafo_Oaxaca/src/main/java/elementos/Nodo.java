/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package elementos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author axelm
 */
public class Nodo {
    private String municipio;
    private List<Arista> aristas;

    public Nodo() {
    }

    public Nodo(String municipio) {
        this.municipio = municipio;
        this.aristas = new ArrayList<>();
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public void addArista(Arista arista) {
        this.aristas.add(arista);
    }

    public String getMunicipio() {
        return municipio;
    }

    public List<Arista> getAristas() {
        return aristas;
    }
    
    public List<Nodo> getNodosAdyacentes(){
        List<Nodo> nodos = new ArrayList<>();
        aristas.forEach(a -> {
            if(a.nodoDestino != this){
                nodos.add(a.nodoDestino);
            }
        });
        return nodos;
    }
}
