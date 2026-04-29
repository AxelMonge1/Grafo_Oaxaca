/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package elementos;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author axelm
 */
public class Nodo {
    private String municipio;
    private int x,y;
    private List<Arista> aristas = new ArrayList<>();
    private Color color = new Color(0, 110, 220); //Para los metodos que usan colores en los nodos

    public Nodo() {
    }

    public Nodo(String municipio, int x, int y) {
        this.municipio = municipio;
        this.x = x;
        this.y = y;
        this.aristas = new ArrayList<>();
    }

    public void setAristas(List<Arista> aristas) {
        this.aristas = aristas;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public void addArista(Arista arista) {
        this.aristas.add(arista);
    }

    public Color getColor() {
        return color;
    }

    public String getMunicipio() {
        return municipio;
    }

    public List<Arista> getAristas() {
        return aristas;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
