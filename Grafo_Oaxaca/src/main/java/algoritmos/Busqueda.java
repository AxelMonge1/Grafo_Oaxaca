package algoritmos;

import elementos.Arista;
import elementos.Grafo;
import elementos.Nodo;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Busqueda {
    private Timer timer;

    public boolean detener() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
            return true;
        }
        return false;
    }
    
    private void mostrarReporte(JPanel panel, String titulo, String contenido) {
        JTextArea textArea = new JTextArea(contenido);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(panel), scrollPane, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void busquedaAnchura(String municipio, Grafo grafo, JPanel panel){
        detener(); 
        grafo.resetearColores();
        grafo.getAristas().forEach(a -> a.setResaltada(false));

        Nodo raiz = grafo.getVertices().stream()
                .filter(n -> n.getMunicipio().equals(municipio))
                .findFirst().orElse(null);

        if (raiz == null) return;

        Queue<Nodo> cola = new LinkedList<>();
        Set<Nodo> visitados = new HashSet<>();
        Map<Nodo, Arista> aristaDescubrimiento = new HashMap<>();
        
        StringBuilder secuencia = new StringBuilder("Secuencia de visita (Nivel por Nivel):\n");

        cola.add(raiz);
        visitados.add(raiz);
        raiz.setColor(Color.YELLOW); 

        timer = new Timer(600, null);
        timer.addActionListener(e -> {
            if (!cola.isEmpty()) {
                Nodo actual = cola.poll();
                actual.setColor(new Color(255, 128, 0)); 
                
                secuencia.append(actual.getMunicipio()).append(" -> ");

                Arista aristaPadre = aristaDescubrimiento.get(actual);
                if (aristaPadre != null) {
                    aristaPadre.setResaltada(true);
                }

                for (Arista a : grafo.getAristas()) {
                    Nodo vecino = null;
                    if (a.nodoOrigen.equals(actual)) vecino = a.nodoDestino;
                    else if (a.nodoDestino.equals(actual)) vecino = a.nodoOrigen;

                    if (vecino != null && !visitados.contains(vecino)) {
                        visitados.add(vecino);
                        cola.add(vecino);
                        aristaDescubrimiento.put(vecino, a);
                        vecino.setColor(Color.YELLOW); 
                    }
                }
                panel.repaint(); 
            } else {
                timer.stop();
                String resultadoFinal = secuencia.substring(0, secuencia.length() - 4);
                mostrarReporte(panel, "¡Búsqueda en Anchura (BFS) finalizada!", resultadoFinal);
            }
        });
        timer.start();
    }

    public void busquedaProfundidad(String municipio, Grafo grafo, JPanel panel) {
        detener(); 
        grafo.resetearColores();
        grafo.getAristas().forEach(a -> a.setResaltada(false));

        Nodo raiz = grafo.getVertices().stream()
                .filter(n -> n.getMunicipio().equals(municipio))
                .findFirst().orElse(null);

        if (raiz == null) return;

        Stack<Nodo> pila = new Stack<>();
        Set<Nodo> visitados = new HashSet<>();
        Map<Nodo, Arista> aristaDescubrimiento = new HashMap<>();
        
        StringBuilder secuencia = new StringBuilder("Lista de nodos en orden de descubrimiento:\n");

        pila.push(raiz);
        
        timer = new Timer(600, e -> {
            if (!pila.isEmpty()) {
                Nodo actual = pila.pop();

                if (!visitados.contains(actual)) {
                    visitados.add(actual);
                    actual.setColor(new Color(255, 128, 0)); 
                    
                    secuencia.append(actual.getMunicipio()).append(" -> ");
                    
                    Arista aristaPadre = aristaDescubrimiento.get(actual);
                    if (aristaPadre != null) {
                        aristaPadre.setResaltada(true);
                    }

                    for (Arista a : grafo.getAristas()) {
                        Nodo vecino = null;
                        if (a.nodoOrigen.equals(actual)) vecino = a.nodoDestino;
                        else if (a.nodoDestino.equals(actual)) vecino = a.nodoOrigen;

                        if (vecino != null && !visitados.contains(vecino)) {
                            pila.push(vecino);
                            aristaDescubrimiento.put(vecino, a);
                            vecino.setColor(Color.YELLOW); 
                        }
                    }
                    panel.repaint();
                }
            } else {
                ((Timer)e.getSource()).stop();
                String resultadoFinal = secuencia.substring(0, secuencia.length() - 4);
                mostrarReporte(panel, "¡Búsqueda en Profundidad (DFS) finalizada!", resultadoFinal);
            }
        });
        timer.start();
    }
}