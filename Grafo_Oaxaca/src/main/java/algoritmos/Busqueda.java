/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos;

import elementos.Grafo;
import elementos.Nodo;
import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author axelm
 */
public class Busqueda {
    private Timer timer;
    
    public void busquedaAnchura(String municipio, Grafo grafo, JPanel panel){
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        grafo.resetearColores();
        Nodo raiz = grafo.getVertices().stream()
                .filter(n -> n.getMunicipio().equals(municipio))
                .findFirst().orElse(null);

        if (raiz == null) return;

        Queue<Nodo> cola = new LinkedList<>();
        Set<Nodo> visitados = new HashSet<>();

        cola.add(raiz);
        visitados.add(raiz);
        raiz.setColor(Color.YELLOW); 

        timer = new Timer(600, null);
        timer.addActionListener(e -> {
            if (!cola.isEmpty()) {
                Nodo actual = cola.poll();
                actual.setColor(new Color(255, 128, 0)); // Naranja: Procesado

                for (Nodo vecino : grafo.getNodosAdyacentesDe(actual)) {
                    if (!visitados.contains(vecino)) {
                        visitados.add(vecino);
                        cola.add(vecino);
                        vecino.setColor(Color.YELLOW); // Amarillo: En cola
                    }
                }
                panel.repaint(); 
            } else {
                timer.stop();
            }
        });
        timer.start();
    }
    public void busquedaProfundidad(String municipio, Grafo grafo, JPanel panel) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        grafo.resetearColores();

        Nodo raiz = grafo.getVertices().stream()
                .filter(n -> n.getMunicipio().equals(municipio))
                .findFirst().orElse(null);

        if (raiz == null) return;

        Stack<Nodo> pila = new Stack<>();
        Set<Nodo> visitados = new HashSet<>();

        pila.push(raiz);
        timer = new Timer(600, e -> {
            if (!pila.isEmpty()) {
                Nodo actual = pila.pop();

                if (!visitados.contains(actual)) {
                    visitados.add(actual);
                    actual.setColor(new Color(255, 128, 0)); // Naranja: Procesado

                    for (Nodo vecino : grafo.getNodosAdyacentesDe(actual)) {
                        if (!visitados.contains(vecino)) {
                            pila.push(vecino);
                            vecino.setColor(Color.YELLOW); // Amarillo: En pila
                        }
                    }
                    panel.repaint();
                }
            } else {
                ((Timer)e.getSource()).stop();
            }
        });
        timer.start();
    }
}