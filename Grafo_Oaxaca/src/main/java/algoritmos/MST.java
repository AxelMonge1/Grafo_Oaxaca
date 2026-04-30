package algoritmos;

import elementos.Arista;
import elementos.Grafo;
import elementos.Nodo;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class MST {
    private Timer timer;

    //Método que detiene el timer anterior si es que se habia inicializado uno
    public boolean detener() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
            return true; 
        }
        return false;
    }
    
    //Muestra en texto el resultado del mst
    private void mostrarReporte(JPanel panel, String titulo, String contenido) {
        JTextArea textArea = new JTextArea(contenido);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 250));
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(panel), scrollPane, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Método que obtiene el MST usando el método de Kruskal.
     * Obtiene el MST in usar una raíz.
     * @param grafo
     * @param panel
     */
    public void mstKruskal(Grafo grafo, JPanel panel) {
        detener();
        grafo.getVertices().forEach(v -> v.setColor(Color.WHITE));
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

        StringBuilder reporte = new StringBuilder("Aristas seleccionadas (Kruskal):\n\n");
        double[] pesoTotal = {0.0}; 

        timer = new Timer(800, e -> {
            if (!colaMst.isEmpty()) {
                Arista a = colaMst.poll();
                a.setResaltada(true); 
                a.nodoOrigen.setColor(Color.BLACK);
                a.nodoDestino.setColor(Color.BLACK);
                
                pesoTotal[0] += a.getPeso();
                reporte.append(a.nodoOrigen.getMunicipio()).append(" -- ")
                       .append(a.nodoDestino.getMunicipio())
                       .append(" (Peso: ").append(a.getPeso()).append(" km)\n"); 
                
                panel.repaint();
            } else {
                ((Timer)e.getSource()).stop();
                reporte.append("\n===========================\n");
                reporte.append("PESO TOTAL DEL ÁRBOL: ").append(String.format("%.2f", pesoTotal[0])).append(" km");
                mostrarReporte(panel, "Árbol de Kruskal finalizado", reporte.toString());
            }
        });
        timer.start();
    }
    
    /**
     * Método que obtiene el MST con el método de Prim.
     * Usa un vertice raíz para obtener el MST partiendo de ahí.
     * @param nombreRaiz
     * @param grafo
     * @param panel
     */
    public void mstPrim(String nombreRaiz, Grafo grafo, JPanel panel) {
        detener();
        grafo.getVertices().forEach(v -> v.setColor(Color.WHITE));
        grafo.getAristas().forEach(a -> a.setResaltada(false));
        panel.repaint();

        Nodo raiz = grafo.getVertices().stream()
                .filter(n -> n.getMunicipio().equals(nombreRaiz))
                .findFirst().orElse(null);

        if (raiz == null) return;

        Set<Nodo> conjuntoS = new HashSet<>();
        conjuntoS.add(raiz);

        StringBuilder reporte = new StringBuilder("Aristas seleccionadas (Prim):\n\n");
        double[] pesoTotal = {0.0}; 
        final boolean[] raizFlash = {true};

        timer = new Timer(800, e -> {
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
                if (nodoCruzado != raiz) nodoCruzado.setColor(Color.BLACK); 
                
                pesoTotal[0] += aristaLigera.getPeso();
                reporte.append(aristaLigera.nodoOrigen.getMunicipio()).append(" -- ")
                       .append(aristaLigera.nodoDestino.getMunicipio())
                       .append(" (Peso: ").append(aristaLigera.getPeso()).append(" km)\n"); 
                
                panel.repaint();
            } else {
                ((Timer)e.getSource()).stop();
                raiz.setColor(Color.BLACK); 
                panel.repaint();
                reporte.append("\n===========================\n");
                reporte.append("PESO TOTAL DEL ÁRBOL: ").append(String.format("%.2f", pesoTotal[0])).append(" km");
                mostrarReporte(panel, "Árbol de Prim finalizado", reporte.toString());
            }
        });
        timer.start();
    }
}