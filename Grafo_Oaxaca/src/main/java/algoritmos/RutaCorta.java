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
        StringBuilder tablaEvolucion = new StringBuilder("Evolución de Distancias:\n");

        PriorityQueue<Nodo> cola = new PriorityQueue<>(Comparator.comparingDouble(n -> distancias.get(n)));
        Set<Nodo> visitados = new HashSet<>();

        grafo.getVertices().forEach(v -> distancias.put(v, Float.MAX_VALUE));
        distancias.put(nodoOrigen, 0.0f);
        cola.add(nodoOrigen);

        final boolean[] terminado = {false}; 

        timer = new Timer(500, e -> {
            if (!cola.isEmpty()) {
                Nodo actual = cola.poll();

                if (visitados.contains(actual)) return;
                visitados.add(actual);

                actual.setColor(new Color(0, 150, 0)); 

                tablaEvolucion.append(String.format(" • %-22s | Distancia Acumulada: %.2f km\n", 
                                      actual.getMunicipio(), distancias.get(actual)));

                grafo.getAristas().forEach(a -> a.setEnCorte(false));

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
                
                aristasCamino.values().forEach(a -> a.setEnCorte(true));
                
                if (actual.equals(nodoDestino)) {
                    cola.clear(); 
                }
                panel.repaint();
                
            } else if (!terminado[0]) {
                terminado[0] = true;
                ((Timer)e.getSource()).stop();

                resaltarCaminoFinal(nodoOrigen, nodoDestino, aristasCamino, padres, panel, grafo);

                // Se invoca compartiendo la misma firma limpia
                String reporte = generarReporteFinal(nodoOrigen, nodoDestino, distancias, padres, tablaEvolucion.toString());
                mostrarReporte(panel, "Resultado Dijkstra", reporte);
            }
        });
        timer.start();
    }

    /**
     * Algoritmo que calcula la ruta más corta usando el método de Bellman-Ford
     * Adaptado para visualización Arista por Arista.
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
        
        StringBuilder registroIteraciones = new StringBuilder("Evolución por Iteración:\n");

        grafo.getVertices().forEach(v -> distancias.put(v, Float.MAX_VALUE));
        distancias.put(nodoOrigen, 0.0f);
        nodoOrigen.setColor(Color.BLACK);

        final int[] iteracion = {0};
        final int[] aristaIndex = {0};
        final boolean[] huboCambio = {false};
        final List<Arista> todasAristas = grafo.getAristas();
        final boolean[] terminado = {false}; 

        timer = new Timer(150, e -> {
            if (iteracion[0] < grafo.getVertices().size() - 1) {
                
                if (aristaIndex[0] < todasAristas.size()) {
                    todasAristas.forEach(ar -> ar.setResaltada(false));
                    
                    Arista a = todasAristas.get(aristaIndex[0]);
                    a.setResaltada(true); 
                    
                    boolean cambio1 = relax(a.nodoOrigen, a.nodoDestino, a, distancias, padres, aristasCamino);
                    boolean cambio2 = relax(a.nodoDestino, a.nodoOrigen, a, distancias, padres, aristasCamino);
                    
                    if (cambio1 || cambio2) {
                        huboCambio[0] = true;
                        todasAristas.forEach(ar -> ar.setEnCorte(false));
                        aristasCamino.values().forEach(ar -> ar.setEnCorte(true));
                        
                        for (Nodo n : grafo.getVertices()) {
                            if (distancias.get(n) != Float.MAX_VALUE && n != nodoOrigen) {
                                n.setColor(Color.YELLOW); 
                            }
                        }
                    }
                    
                    aristaIndex[0]++;
                    panel.repaint();
                    
                } else {
                    long nodosAlcanzados = distancias.values().stream().filter(d -> d != Float.MAX_VALUE).count();
                    registroIteraciones.append(" • Iteración ").append(iteracion[0] + 1)
                                      .append(": ").append(nodosAlcanzados).append(" municipios evaluados.");
                    if (!huboCambio[0]) {
                        registroIteraciones.append(" (El grafo convergió, sin más cambios).");
                    }
                    registroIteraciones.append("\n");

                    iteracion[0]++;
                    aristaIndex[0] = 0; 
                    
                    if (!huboCambio[0]) {
                        iteracion[0] = grafo.getVertices().size();
                    }
                    huboCambio[0] = false; 
                }

            } else if (!terminado[0]) {
                terminado[0] = true;
                ((Timer)e.getSource()).stop();
                
                todasAristas.forEach(ar -> {
                    ar.setResaltada(false);
                    ar.setEnCorte(false);
                });
                
                resaltarCaminoFinal(nodoOrigen, nodoDestino, aristasCamino, padres, panel, grafo);
                
                String reporte = generarReporteFinal(nodoOrigen, nodoDestino, distancias, padres, registroIteraciones.toString());
                mostrarReporte(panel, "Resultado Bellman-Ford", reporte);
            }
        });
        timer.start();
    }

    private boolean relax(Nodo u, Nodo v, Arista a, Map<Nodo, Float> d, Map<Nodo, Nodo> p, Map<Nodo, Arista> ac) {
        if (d.get(u) != Float.MAX_VALUE && d.get(u) + a.getPeso() < d.get(v)) {
            d.put(v, d.get(u) + a.getPeso());
            p.put(v, u);
            ac.put(v, a);
            return true;
        }
        return false;
    }

    private void resaltarCaminoFinal(Nodo origen, Nodo destino, Map<Nodo, Arista> ac, Map<Nodo, Nodo> p, JPanel panel, Grafo grafo) {
        grafo.getAristas().forEach(a -> {
            a.setResaltada(false);
            a.setEnCorte(false);
        });
        grafo.getVertices().forEach(v -> v.setColor(Color.WHITE));

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

    /**
     * Reporte unificado y formateado para estructuras Monospaced de JTextArea
     */
    private String generarReporteFinal(Nodo ori, Nodo des, Map<Nodo, Float> dists, Map<Nodo, Nodo> pads, String tabla) {
        float distTotal = dists.get(des);
        StringBuilder sb = new StringBuilder();
        
        sb.append("=========================================================\n");
        sb.append("                 REPORTES DE RUTA ÓPTIMA                 \n");
        sb.append("=========================================================\n\n");
        
        sb.append("[1] RESUMEN DE TRAYECTO\n");
        sb.append("---------------------------------------------------------\n");
        sb.append(" • Origen : ").append(ori.getMunicipio()).append("\n");
        sb.append(" • Destino: ").append(des.getMunicipio()).append("\n");
        
        if (distTotal == Float.MAX_VALUE) {
            sb.append(" • Estado : ❌ No existe conexión terrestre entre ciudades.\n");
        } else {
            List<String> camino = new ArrayList<>();
            Nodo t = des;
            while (t != null) {
                camino.add(0, t.getMunicipio());
                t = pads.get(t);
            }
            sb.append(" • Ruta   : ").append(String.join(" -> ", camino)).append("\n");
            sb.append(" • Costo  : ").append(String.format("%.2f km", distTotal)).append("\n");
        }
        
        sb.append("\n[2] TRAZA Y LOG DE EVOLUCIÓN\n");
        sb.append("---------------------------------------------------------\n");
        sb.append(tabla.trim()).append("\n");
        
        sb.append("\n[3] MATRIZ DE DISTANCIAS FINALES DESDE EL ORIGEN\n");
        sb.append("---------------------------------------------------------\n");
        sb.append(String.format(" %-22s | %-14s | %-15s\n", "Municipio", "Costo Mínimo", "Predecesor"));
        sb.append("---------------------------------------------------------\n");
        
        List<Nodo> listaNodos = new ArrayList<>(dists.keySet());
        listaNodos.sort((n1, n2) -> n1.getMunicipio().compareTo(n2.getMunicipio()));
        
        for (Nodo n : listaNodos) {
            String nombre = n.getMunicipio();
            float d = dists.get(n);
            String distStr = (d == Float.MAX_VALUE) ? "Infinito" : String.format("%.2f km", d);
            
            Nodo padre = pads.get(n);
            String padreStr = (padre == null) ? "Ninguno (Origen)" : padre.getMunicipio();
            
            sb.append(String.format(" %-22s | %-14s | %-15s\n", nombre, distStr, padreStr));
        }
        sb.append("=========================================================\n");
        
        return sb.toString();
    }

    private void resetGrafo(Grafo g) {
        g.getAristas().forEach(a -> {
            a.setResaltada(false);
            a.setEnCorte(false);
        });
        g.resetearColores();
    }
    
    private Nodo obtenerVecino(Arista a, Nodo actual) {
        if (a.nodoOrigen.equals(actual)) return a.nodoDestino;
        if (a.nodoDestino.equals(actual)) return a.nodoOrigen;
        return null;
    }
    
    private void mostrarReporte(JPanel panel, String titulo, String contenido) {
        JTextArea area = new JTextArea(contenido);
        area.setEditable(false);
        area.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(550, 450)); // Ampliado ligeramente para evitar scroll horizontal molesto
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(panel), scroll, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}