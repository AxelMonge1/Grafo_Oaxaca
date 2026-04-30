package algoritmos;

import elementos.Arista;
import elementos.Grafo;
import elementos.Nodo;
import java.awt.Color;
import java.awt.Dimension;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class RutaCorta {
    private Timer timer;

    public void detener() {
        if (timer != null && timer.isRunning()) timer.stop();
    }

    /**
     * Algoritmo que calcula la ruta más corta usando el método de Dijkstra
     * No funciona con aristas con peso negativo
     * @param origen
     * @param destino
     * @param grafo
     * @param panel
     */
    public void dijkstra(String origen, String destino, Grafo grafo, JPanel panel) {
        detener();
        resetGrafo(grafo);

        Nodo nodoOrigen = grafo.buscarNodo(origen);
        Nodo nodoDestino = grafo.buscarNodo(destino);

        if (nodoOrigen == null || nodoDestino == null) {
            JOptionPane.showMessageDialog(panel, "Una o ambas ciudades no existen en el mapa.");
            return;
        }
        
        Map<Nodo, Float> distancias = new HashMap<>();
        Map<Nodo, Nodo> padres = new HashMap<>();
        Map<Nodo, Arista> aristasCamino = new HashMap<>();
        StringBuilder tablaEvolucion = new StringBuilder("Evolución de Distancias (Dijkstra):\n");

        PriorityQueue<Nodo> cola = new PriorityQueue<>(Comparator.comparingDouble(n -> distancias.get(n)));
        Set<Nodo> visitados = new HashSet<>();

        grafo.getVertices().forEach(v -> distancias.put(v, Float.MAX_VALUE));
        distancias.put(nodoOrigen, 0.0f);
        cola.add(nodoOrigen);

        timer = new Timer(400, e -> {
            if (!cola.isEmpty()) {
                Nodo actual = cola.poll();

                if (visitados.contains(actual)) return;
                visitados.add(actual);

                actual.setColor(new Color(0, 150, 0));

                tablaEvolucion.append(String.format("Ciudad: %-20s | Distancia Acumulada: %.2f km\n", 
                                      actual.getMunicipio(), distancias.get(actual)));

                for (Arista a : grafo.getAristas()) {
                    Nodo vecino = obtenerVecino(a, actual);

                    if (vecino != null && !visitados.contains(vecino)) {
                        float nuevaDist = distancias.get(actual) + a.getPeso();

                        if (nuevaDist < distancias.get(vecino)) {
                            cola.remove(vecino);

                            distancias.put(vecino, nuevaDist);
                            padres.put(vecino, actual);
                            aristasCamino.put(vecino, a);

                            cola.add(vecino);

                            vecino.setColor(Color.LIGHT_GRAY);
                        }
                    }
                }
                panel.repaint();
            } else {
                ((Timer)e.getSource()).stop();

                resaltarCaminoFinal(nodoDestino, aristasCamino, padres, panel);

                String reporte = generarReporteFinal(nodoOrigen, nodoDestino, distancias, padres, tablaEvolucion.toString());
                mostrarReporte(panel, "Resultado Dijkstra", reporte);
            }
        });
        timer.start();
    }

    /**
     * Algoritmo que calcula la ruta más corta usando el método de Bellman-Ford
     * Acepta aristas con peso negativo
     * @param origen
     * @param destino
     * @param grafo
     * @param panel
     */
    public void bellmanFord(String origen, String destino, Grafo grafo, JPanel panel) {
        detener();
        resetGrafo(grafo);

        Nodo nodoOrigen = grafo.buscarNodo(origen);
        Nodo nodoDestino = grafo.buscarNodo(destino);
        if (nodoOrigen == null || nodoDestino == null) return;

        Map<Nodo, Float> distancias = new HashMap<>();
        Map<Nodo, Nodo> padres = new HashMap<>();
        Map<Nodo, Arista> aristasCamino = new HashMap<>();
        StringBuilder tablaIteraciones = new StringBuilder("Tabla de Distancias por Iteración:\n");

        grafo.getVertices().forEach(v -> distancias.put(v, Float.MAX_VALUE));
        distancias.put(nodoOrigen, 0.0f);

        final int[] i = {0};
        final List<Arista> todasAristas = grafo.getAristas();

        timer = new Timer(100, e -> {
            if (i[0] < grafo.getVertices().size() - 1) {
                boolean huboCambio = false;
                for (Arista a : todasAristas) {
                    if (relax(a.nodoOrigen, a.nodoDestino, a, distancias, padres, aristasCamino)) huboCambio = true;
                    if (relax(a.nodoDestino, a.nodoOrigen, a, distancias, padres, aristasCamino)) huboCambio = true;
                }
                
                tablaIteraciones.append("Iteración ").append(i[0]+1).append(" completada.\n");
                i[0]++;
                if (huboCambio) panel.repaint();
            } else {
                ((Timer)e.getSource()).stop();
                resaltarCaminoFinal(nodoDestino, aristasCamino, padres, panel);
                String reporte = generarReporteFinal(nodoOrigen, nodoDestino, distancias, padres, tablaIteraciones.toString());
                mostrarReporte(panel, "Resultado Bellman-Ford", reporte);
            }
        });
        timer.start();
    }

    // Método relajar que revisa si el nuevo camino es más corto que el camino que ya tenia registrado
    private boolean relax(Nodo u, Nodo v, Arista a, Map<Nodo, Float> d, Map<Nodo, Nodo> p, Map<Nodo, Arista> ac) {
        if (d.get(u) != Float.MAX_VALUE && d.get(u) + a.getPeso() < d.get(v)) {
            d.put(v, d.get(u) + a.getPeso());
            p.put(v, u);
            ac.put(v, a);
            v.setColor(new Color(255, 140, 0));
            return true;
        }
        return false;
    }

    //Resalta el camino que queda al final
    private void resaltarCaminoFinal(Nodo destino, Map<Nodo, Arista> ac, Map<Nodo, Nodo> p, JPanel panel) {
        ac.values().forEach(a -> a.setResaltada(false));
        Nodo temp = destino;
        while (p.containsKey(temp)) {
            Arista camino = ac.get(temp);
            if (camino != null) camino.setResaltada(true);
            temp.setColor(Color.CYAN);
            temp = p.get(temp);
        }
        if (temp != null) temp.setColor(Color.CYAN);
        panel.repaint();
    }

    //Texto para mostrarle al usuario el camino y las distancias en texto
    private String generarReporteFinal(Nodo ori, Nodo des, Map<Nodo, Float> dists, Map<Nodo, Nodo> pads, String tabla) {
        float distTotal = dists.get(des);
        StringBuilder sb = new StringBuilder();
        sb.append("RUTA ÓPTIMA:\n");
        
        if (distTotal == Float.MAX_VALUE) {
            sb.append("No existe camino entre las ciudades.\n");
        } else {
            List<String> camino = new ArrayList<>();
            Nodo t = des;
            while (t != null) {
                camino.add(0, t.getMunicipio());
                t = pads.get(t);
            }
            sb.append(String.join(" -> ", camino)).append("\n");
            sb.append("Distancia Total: ").append(String.format("%.2f km", distTotal)).append("\n");
        }
        
        sb.append("\n----------------------------------\n");
        sb.append(tabla);
        return sb.toString();
    }

    //Reinicia los colores de los vertiecs y si las aristas están resaltadas o no
    private void resetGrafo(Grafo g) {
        g.getAristas().forEach(a -> a.setResaltada(false));
        g.resetearColores();
    }
    
    //Obtiene el vecino de un vertice
    private Nodo obtenerVecino(Arista a, Nodo actual) {
        if (a.nodoOrigen.equals(actual)) return a.nodoDestino;
        if (a.nodoDestino.equals(actual)) return a.nodoOrigen;
        return null;
    }
    
    //Muestra el reporte que se genera en cada iteración
    private void mostrarReporte(JPanel panel, String titulo, String contenido) {
        JTextArea area = new JTextArea(contenido);
        area.setEditable(false);
        area.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 400));
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(panel), scroll, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}