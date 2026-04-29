/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package elementos;

/**
 *
 * @author axelm
 */
public class Arista {
    public Nodo nodoOrigen;
    public Nodo nodoDestino;
    public float peso;

    public Arista() {
    }

    public Arista(Nodo nodoOrigen, Nodo nodoDestino, float peso) {
        this.nodoOrigen = nodoOrigen;
        this.nodoDestino = nodoDestino;
        this.peso = peso;
    }

    public Nodo getNodoOrigen() {
        return nodoOrigen;
    }

    public void setNodoOrigen(Nodo nodoOrigen) {
        this.nodoOrigen = nodoOrigen;
    }

    public Nodo getNodoDestino() {
        return nodoDestino;
    }

    public void setNodoDestino(Nodo nodoDestino) {
        this.nodoDestino = nodoDestino;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }
    
    
}
