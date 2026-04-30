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
        JTextArea area = new JTextArea(contenido);
        area.setEditable(false);
        area.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 400));
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(panel), scroll, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Método que visita todos los vertices de un grafo por anchura (BFS).
     */
    public void busquedaAnchura(String municipio, Grafo grafo, JPanel panel){
        detener(); 
        
        grafo.getAristas().forEach(a -> {
            a.setResaltada(false);
            a.setEnCorte(false);
        });
        grafo.getVertices().forEach(v -> {
            v.setColor(Color.WHITE);
            v.setEnConjuntoS(false);
        });

        Nodo raiz = grafo.getVertices().stream()
                .filter(n -> n.getMunicipio().equals(municipio))
                .findFirst().orElse(null);

        if (raiz == null) return;

        Queue<Nodo> cola = new LinkedList<>();
        Set<Nodo> visitados = new HashSet<>();
        Map<Nodo, Arista> aristaDescubrimiento = new HashMap<>();
        
        StringBuilder secuencia = new StringBuilder("BÚSQUEDA EN ANCHURA (Nivel por Nivel):\n");
        secuencia.append("--------------------------------------------------\n");

        cola.add(raiz);
        visitados.add(raiz);

        final boolean[] raizFlash = {true};
        final boolean[] terminado = {false}; 

        timer = new Timer(600, null);
        timer.addActionListener(e -> {
            raiz.setColor(raizFlash[0] ? Color.CYAN : Color.YELLOW);
            raizFlash[0] = !raizFlash[0];

            if (!cola.isEmpty()) {
                Nodo actual = cola.poll();
                
                if (actual != raiz) actual.setColor(new Color(255, 128, 0)); 
                
                secuencia.append("-> ").append(actual.getMunicipio()).append("\n");

                grafo.getAristas().forEach(a -> a.setEnCorte(false));

                for (Arista a : grafo.getAristas()) {
                    Nodo vecino = null;
                    if (a.nodoOrigen.equals(actual)) vecino = a.nodoDestino;
                    else if (a.nodoDestino.equals(actual)) vecino = a.nodoOrigen;

                    if (vecino != null && !visitados.contains(vecino)) {
                        visitados.add(vecino);
                        cola.add(vecino);
                        aristaDescubrimiento.put(vecino, a);
                        if (vecino != raiz) vecino.setColor(Color.GRAY); 
                    }
                }
                
                aristaDescubrimiento.values().forEach(a -> a.setEnCorte(true));
                
                if (actual != raiz) actual.setColor(Color.BLACK); 
                panel.repaint(); 
                
            } else if (!terminado[0]) {
                terminado[0] = true; 
                timer.stop();
                raiz.setColor(Color.BLACK); 
                
                grafo.getAristas().forEach(a -> a.setEnCorte(false));
                aristaDescubrimiento.values().forEach(a -> a.setResaltada(true));
                
                panel.repaint();
                mostrarReporte(panel, "Resultado de Búsqueda (BFS)", secuencia.toString());
            }
        });
        timer.start();
    }

    /**
     * Método que recorre todos los vertices de un grafo por profundidad (DFS).
     * Incluye tiempos de descubrimiento y finalización visuales en los nodos.
     */
    public void busquedaProfundidad(String municipio, Grafo grafo, JPanel panel) {
        detener(); 
        
        grafo.getAristas().forEach(a -> {
            a.setResaltada(false);
            a.setEnCorte(false);
        });
        
        grafo.resetearColores();

        Nodo raiz = grafo.getVertices().stream()
                .filter(n -> n.getMunicipio().equals(municipio))
                .findFirst().orElse(null);

        if (raiz == null) return;

        Stack<Nodo> pila = new Stack<>();
        Set<Nodo> descubiertos = new HashSet<>();
        Set<Nodo> terminados = new HashSet<>();
        Map<Nodo, Arista> aristaDescubrimiento = new HashMap<>();
        
        int[] tiempo = {0};
        
        StringBuilder secuencia = new StringBuilder("BÚSQUEDA EN PROFUNDIDAD (Tiempos d / f):\n");
        secuencia.append("--------------------------------------------------\n");
        secuencia.append(String.format("%-20s | %-12s | %-12s\n", "Ciudad", "Descubierto", "Finalizado"));
        secuencia.append("--------------------------------------------------\n");

        pila.push(raiz);
        final boolean[] raizFlash = {true};
        final boolean[] terminado = {false}; 
        
        timer = new Timer(800, e -> { 
            raiz.setColor(raizFlash[0] ? Color.CYAN : Color.YELLOW);
            raizFlash[0] = !raizFlash[0];

            if (!pila.isEmpty()) {
                Nodo actual = pila.peek();

                if (!descubiertos.contains(actual)) {
                    descubiertos.add(actual);
                    tiempo[0]++;
                    actual.setTiempoDescubrimiento(tiempo[0]);
                    if (actual != raiz) actual.setColor(Color.GRAY); 
                }

                Nodo siguienteVecino = null;
                Arista aristaHaciaVecino = null;

                for (Arista a : grafo.getAristas()) {
                    Nodo vecino = null;
                    if (a.nodoOrigen.equals(actual)) vecino = a.nodoDestino;
                    else if (a.nodoDestino.equals(actual)) vecino = a.nodoOrigen;

                    if (vecino != null && !descubiertos.contains(vecino)) {
                        siguienteVecino = vecino;
                        aristaHaciaVecino = a;
                        break; 
                    }
                }

                if (siguienteVecino != null) {
                    pila.push(siguienteVecino);
                    aristaDescubrimiento.put(siguienteVecino, aristaHaciaVecino);
                } else {
                    
                    pila.pop();
                    if (!terminados.contains(actual)) {
                        terminados.add(actual);
                        tiempo[0]++;
                        actual.setTiempoFinalizacion(tiempo[0]); 
                        if (actual != raiz) actual.setColor(Color.BLACK); 
                        
                        secuencia.append(String.format("%-20s | %-12d | %-12d\n", 
                                actual.getMunicipio(), 
                                actual.getTiempoDescubrimiento(), 
                                actual.getTiempoFinalizacion()));
                    }
                }

                grafo.getAristas().forEach(a -> a.setEnCorte(false));
                aristaDescubrimiento.values().forEach(a -> a.setEnCorte(true));
                
                panel.repaint();
                
            } else if (!terminado[0]) {
                terminado[0] = true; 
                ((Timer)e.getSource()).stop();
                raiz.setColor(Color.BLACK);
                
                grafo.getAristas().forEach(a -> a.setEnCorte(false));
                aristaDescubrimiento.values().forEach(a -> a.setResaltada(true));
                
                panel.repaint();
                mostrarReporte(panel, "Resultado de Búsqueda (DFS)", secuencia.toString());
            }
        });
        timer.start();
    }
}