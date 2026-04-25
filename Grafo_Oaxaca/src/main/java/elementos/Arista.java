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
}
