package elementos;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Nodo {
    private String municipio;
    private int x, y;
    private List<Arista> aristas = new ArrayList<>();
    private Color color = new Color(0, 110, 220); 
    private boolean enConjuntoS = false;
   
    private int tiempoDescubrimiento = 0;
    private int tiempoFinalizacion = 0;

    public Nodo() {}

    public Nodo(String municipio, int x, int y) {
        this.municipio = municipio;
        this.x = x;
        this.y = y;
        this.aristas = new ArrayList<>();
    }

    // Getters y Setters
    public int getTiempoDescubrimiento() { return tiempoDescubrimiento; }
    public void setTiempoDescubrimiento(int tiempoDescubrimiento) { this.tiempoDescubrimiento = tiempoDescubrimiento; }
    public int getTiempoFinalizacion() { return tiempoFinalizacion; }
    public void setTiempoFinalizacion(int tiempoFinalizacion) { this.tiempoFinalizacion = tiempoFinalizacion; }

    public void setAristas(List<Arista> aristas) { this.aristas = aristas; }
    public void setColor(Color color) { this.color = color; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }
    public void addArista(Arista arista) { this.aristas.add(arista); }
    public Color getColor() { return color; }
    public String getMunicipio() { return municipio; }
    public List<Arista> getAristas() { return aristas; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public boolean isEnConjuntoS() { return enConjuntoS; }
    public void setEnConjuntoS(boolean enConjuntoS) { this.enConjuntoS = enConjuntoS; }
    
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