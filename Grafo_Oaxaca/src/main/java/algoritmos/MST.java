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
import java.util.PriorityQueue;
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

    public boolean detener() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
            return true; 
        }
        return false;
    }
    
    private void mostrarReporte(JPanel panel, String titulo, String contenido) {
        JTextArea textArea = new JTextArea(contenido);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 250));
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(panel), scrollPane, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mstKruskal(Grafo grafo, JPanel panel) {
        detener();
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

        StringBuilder reporte = new StringBuilder("Aristas seleccionadas (Kruskal):\n\n");
        double[] pesoTotal = {0.0}; 

        timer = new Timer(800, e -> {
            if (!colaMst.isEmpty()) {
                Arista a = colaMst.poll();
                a.setResaltada(true); 
                a.nodoOrigen.setColor(Color.MAGENTA);
                a.nodoDestino.setColor(Color.MAGENTA);
                
                pesoTotal[0] += a.getPeso();
                reporte.append(a.nodoOrigen.getMunicipio()).append(" -- ")
                       .append(a.nodoDestino.getMunicipio())
                       .append(" (Peso: ").append(a.getPeso()).append(")\n");
                
                panel.repaint();
            } else {
                ((Timer)e.getSource()).stop();
                reporte.append("\n===========================\n");
                reporte.append("PESO TOTAL DEL ÁRBOL: ").append(String.format("%.2f", pesoTotal[0]));
                mostrarReporte(panel, "¡Árbol de Kruskal finalizado!", reporte.toString());
            }
        });
        timer.start();
    }
    
    public void mstPrim(String nombreRaiz, Grafo grafo, JPanel panel) {
        detener();
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

        for (Arista a : grafo.getAristas()) {
            if (a.nodoOrigen.equals(raiz) || a.nodoDestino.equals(raiz)) {
                pq.add(a);
            }
        }

        while (!pq.isEmpty() && visitados.size() < grafo.getVertices().size()) {
            Arista aristaMasBarata = pq.poll();

            boolean origenVisitado = visitados.contains(aristaMasBarata.nodoOrigen);
            boolean destinoVisitado = visitados.contains(aristaMasBarata.nodoDestino);

            if (origenVisitado && destinoVisitado) continue;

            Nodo nuevoNodo = origenVisitado ? aristaMasBarata.nodoDestino : aristaMasBarata.nodoOrigen;

            visitados.add(nuevoNodo);
            aristasParaAnimar.add(aristaMasBarata);

            for (Arista proxima : grafo.getAristas()) {
                if (proxima.nodoOrigen.equals(nuevoNodo) || proxima.nodoDestino.equals(nuevoNodo)) {
                    Nodo otroExtremo = proxima.nodoOrigen.equals(nuevoNodo) ? proxima.nodoDestino : proxima.nodoOrigen;
                    if (!visitados.contains(otroExtremo)) {
                        pq.add(proxima);
                    }
                }
            }
        }

        StringBuilder reporte = new StringBuilder("Aristas seleccionadas (Prim):\n\n");
        double[] pesoTotal = {0.0}; 

        timer = new Timer(800, e -> {
            if (!aristasParaAnimar.isEmpty()) {
                Arista a = aristasParaAnimar.poll();
                a.setResaltada(true);
                a.nodoOrigen.setColor(Color.CYAN);
                a.nodoDestino.setColor(Color.CYAN);
                
                pesoTotal[0] += a.getPeso();
                reporte.append(a.nodoOrigen.getMunicipio()).append(" -- ")
                       .append(a.nodoDestino.getMunicipio())
                       .append(" (Peso: ").append(a.getPeso()).append(")\n");
                
                panel.repaint();
            } else {
                ((Timer)e.getSource()).stop();
                reporte.append("\n===========================\n");
                reporte.append("PESO TOTAL DEL ÁRBOL: ").append(String.format("%.2f", pesoTotal[0]));
                mostrarReporte(panel, "¡Árbol de Prim finalizado!", reporte.toString());
            }
        });
        timer.start();
    }
}