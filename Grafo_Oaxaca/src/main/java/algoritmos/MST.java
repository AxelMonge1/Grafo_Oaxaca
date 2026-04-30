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

public class MST {
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
     * Algoritmo que calcula MST usando el método de Kruskal
     */
    public void mstKruskal(Grafo grafo, JPanel panel) {
        detener();
        
        grafo.getVertices().forEach(v -> {
            v.setColor(Color.WHITE);
            v.setEnConjuntoS(false); 
        });
        grafo.getAristas().forEach(a -> {
            a.setResaltada(false);
            a.setEnCorte(false);
        });
        panel.repaint();

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

        StringBuilder reporte = new StringBuilder("ÁRBOL DE EXPANSIÓN MÍNIMA (Kruskal):\n");
        reporte.append("--------------------------------------------------\n");
        double[] pesoTotal = {0.0}; 
        final boolean[] terminado = {false}; 

        timer = new Timer(800, e -> {
            if (!colaMst.isEmpty()) {
                Arista a = colaMst.poll();
                a.setResaltada(true); 
                a.nodoOrigen.setColor(Color.BLACK);
                a.nodoDestino.setColor(Color.BLACK);
                
                pesoTotal[0] += a.getPeso();
                reporte.append(String.format("%-15s -- %-15s | %.2f km\n", 
                        a.nodoOrigen.getMunicipio(), a.nodoDestino.getMunicipio(), a.getPeso()));
                
                panel.repaint();
            } else if (!terminado[0]) {
                terminado[0] = true;
                ((Timer)e.getSource()).stop();
                reporte.append("--------------------------------------------------\n");
                reporte.append("PESO TOTAL DEL ÁRBOL: ").append(String.format("%.2f", pesoTotal[0])).append(" km");
                mostrarReporte(panel, "Árbol de Kruskal finalizado", reporte.toString());
            }
        });
        timer.start();
    }
    
    /**
     * Algoritmo que calcula MST usando el método de Prim
     */
    public void mstPrim(String nombreRaiz, Grafo grafo, JPanel panel) {
        detener();

        grafo.getVertices().forEach(v -> {
            v.setColor(Color.WHITE);
            v.setEnConjuntoS(false); 
        });
        grafo.getAristas().forEach(a -> {
            a.setResaltada(false);
            a.setEnCorte(false);
        });
        panel.repaint();

        Nodo raiz = grafo.getVertices().stream()
                .filter(n -> n.getMunicipio().equals(nombreRaiz))
                .findFirst().orElse(null);

        if (raiz == null) return;

        Set<Nodo> conjuntoS = new HashSet<>();
        conjuntoS.add(raiz);
        raiz.setEnConjuntoS(true); 

        StringBuilder reporte = new StringBuilder("ÁRBOL DE EXPANSIÓN MÍNIMA (Prim):\n");
        reporte.append("--------------------------------------------------\n");
        double[] pesoTotal = {0.0}; 
        final boolean[] raizFlash = {true};
        final boolean[] terminado = {false};

        timer = new Timer(1000, e -> {
            raiz.setColor(raizFlash[0] ? Color.CYAN : Color.YELLOW);
            raizFlash[0] = !raizFlash[0];

            Arista aristaLigera = null;
            double pesoMinimo = Double.MAX_VALUE;

            for (Arista a : grafo.getAristas()) {
                boolean origenEnS = conjuntoS.contains(a.nodoOrigen);
                boolean destinoEnS = conjuntoS.contains(a.nodoDestino);

                if (origenEnS ^ destinoEnS) { 
                    if (a.getPeso() < pesoMinimo) {
                        pesoMinimo = a.getPeso();
                        aristaLigera = a;
                    }
                }
            }

            if (aristaLigera != null) {
                aristaLigera.setResaltada(true);
                
                Nodo nodoCruzado = conjuntoS.contains(aristaLigera.nodoOrigen) ? aristaLigera.nodoDestino : aristaLigera.nodoOrigen;
                conjuntoS.add(nodoCruzado);
                
                nodoCruzado.setEnConjuntoS(true); 
                if (nodoCruzado != raiz) nodoCruzado.setColor(Color.BLACK); 
                
                pesoTotal[0] += aristaLigera.getPeso();
                reporte.append(String.format("%-15s -- %-15s | %.2f km\n", 
                        aristaLigera.nodoOrigen.getMunicipio(), aristaLigera.nodoDestino.getMunicipio(), aristaLigera.getPeso()));
                
                panel.repaint();
            } else if (!terminado[0]) {
                terminado[0] = true; 
                ((Timer)e.getSource()).stop();
                raiz.setColor(Color.BLACK); 
                
                grafo.getVertices().forEach(v -> v.setEnConjuntoS(false)); 
                panel.repaint();
                
                reporte.append("--------------------------------------------------\n");
                reporte.append("PESO TOTAL DEL ÁRBOL: ").append(String.format("%.2f", pesoTotal[0])).append(" km");
                mostrarReporte(panel, "Árbol de Prim finalizado", reporte.toString());
            }
        });
        timer.start();
    }
}