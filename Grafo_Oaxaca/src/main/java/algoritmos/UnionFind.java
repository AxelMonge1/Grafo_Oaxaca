/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos;

import elementos.Nodo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author axelm
 */
public class UnionFind {
    private Map<String, String> padre = new HashMap<>();

    public UnionFind(List<Nodo> vertices) {
        for (Nodo n : vertices) {
            padre.put(n.getMunicipio(), n.getMunicipio());
        }
    }

    public String buscar(String i) {
        if (padre.get(i).equals(i)) return i;
        return buscar(padre.get(i));
    }

    public void union(String i, String j) {
        String raizI = buscar(i);
        String raizJ = buscar(j);
        padre.put(raizI, raizJ);
    }
}
