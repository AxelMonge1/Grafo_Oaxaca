/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos;

import elementos.Arista;
import elementos.Grafo;
import elementos.Nodo;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author axelm
 */
public class MST {
    private Timer timer;
    
    public void mstKruskal(Grafo grafo, JPanel panel) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        grafo.resetearColores();
        grafo.getAristas().forEach(a -> a.setResaltada(false));

        List<Arista> aristasOrdenadas = new ArrayList<>(grafo.getAristas());
        aristasOrdenadas.sort(Comparator.comparingDouble(Arista::getPeso));

        UnionFind uf = new UnionFind(grafo.getVertices());

        Queue<Arista> colaMst = new LinkedList<>();

        for (Arista arista : aristasOrdenadas) {
            String rootOrigen = uf.buscar(arista.nodoOrigen.getMunicipio());
            String rootDestino = uf.buscar(arista.nodoDestino.getMunicipio());

            if (!rootOrigen.equals(rootDestino)) {
                uf.union(rootOrigen, rootDestino);
                colaMst.add(arista);
            }
        }

        timer = new Timer(800, e -> {
            if (!colaMst.isEmpty()) {
                Arista a = colaMst.poll();

                a.setResaltada(true); 
                a.nodoOrigen.setColor(Color.MAGENTA);
                a.nodoDestino.setColor(Color.MAGENTA);

                panel.repaint();
            } else {
                ((Timer)e.getSource()).stop();
            }
        });
        timer.start();
    }
    
    public void mstPrim(String nombreRaiz, Grafo grafo, JPanel panel) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        grafo.resetearColores();
        for (Arista a : grafo.getAristas()) {
            a.setResaltada(false);
        }
        panel.repaint();

        Nodo raiz = grafo.getVertices().stream()
                .filter(n -> n.getMunicipio().equals(nombreRaiz))
                .findFirst().orElse(null);

        if (raiz == null) return;

        Set<Nodo> visitados = new HashSet<>();
        PriorityQueue<Arista> pq = new PriorityQueue<>((a1, a2) -> Float.compare(a1.getPeso(), a2.getPeso()));

        Queue<Arista> aristasParaAnimar = new LinkedList<>();

        visitados.add(raiz);
        pq.addAll(grafo.getAristasDe(raiz));

        while (!pq.isEmpty() && visitados.size() < grafo.getVertices().size()) {
            Arista aristaMasBarata = pq.poll();

            Nodo destino = visitados.contains(aristaMasBarata.nodoOrigen) ? 
                            aristaMasBarata.nodoDestino : aristaMasBarata.nodoOrigen;

            if (!visitados.contains(destino)) {
                visitados.add(destino);
                aristasParaAnimar.add(aristaMasBarata);

                for (Arista proxima : grafo.getAristasDe(destino)) {
                    if (!visitados.contains(proxima.nodoOrigen) || !visitados.contains(proxima.nodoDestino)) {
                        pq.add(proxima);
                    }
                }
            }
        }

        timer = new Timer(800, e -> {
            if (!aristasParaAnimar.isEmpty()) {
                Arista a = aristasParaAnimar.poll();

                a.setResaltada(true);
                a.nodoOrigen.setColor(Color.CYAN);
                a.nodoDestino.setColor(Color.CYAN);

                panel.repaint();
            } else {
                ((Timer)e.getSource()).stop();
            }
        });
        timer.start();
    }
}