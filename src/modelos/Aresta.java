package modelos;

/**
 * Representa uma aresta em um grafo ponderado.
 * Implementa Comparable para ordenação por peso (essencial para Kruskal).
 * 
 */
public class Aresta implements Comparable<Aresta> {
    
    private int origem;
    private int destino;
    private int peso;
    
    /**
     * Construtor.
     * @param origem  vértice de origem
     * @param destino vértice de destino
     * @param peso    peso da aresta
     */
    public Aresta(int origem, int destino, int peso) {
        this.origem = origem;
        this.destino = destino;
        this.peso = peso;
    }
    
    public int getOrigem() { 
        return origem; 
    }
    
    public int getDestino() { 
        return destino; 
    }
    
    public int getPeso() { 
        return peso; 
    }
    
    @Override
    public int compareTo(Aresta outra) {
        return Integer.compare(this.peso, outra.peso);
    }
    
    @Override
    public String toString() {
        return "(" + origem + " -> " + destino + ", peso: " + peso + ")";
    }
}