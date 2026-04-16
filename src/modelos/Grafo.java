package modelos;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Collections;

/**
 * Representa um grafo
 * Armazena Vértices e Arestas para o uso no algoritmo de Kruskal
 */
public class Grafo {
    
    private final int numVertices;
    private final List<Aresta> arestas;

    /**
     * Construtor.
     * @param numVertices número de vértices do grafo
     */
    public Grafo(int numVertices) {
        if(numVertices <= 0) {
            throw new IllegalArgumentException("Número de vértices deve ser não negativo");
        }
        this.numVertices = numVertices;
        this.arestas = new ArrayList<>();
    }

    /**
     * Adiciona uma aresta ao grafo
     * @param origem vértice de origem
     * @param destino vértice de destino
     * @param peso peso da aresta
     */
    public void adicionarAresta(int origem, int destino, int peso) {
        if(origem < 0 || origem >= numVertices){
            throw new IllegalArgumentException("Vértice de origem inválido: " + origem);
        }
        if(destino < 0 || destino >= numVertices){
            throw new IllegalArgumentException("Vértice de destino inválido: " + destino);
        }
        if(peso < 0){
            throw new IllegalArgumentException("Peso da aresta deve ser não negativo: " + peso);
        }
        arestas.add(new Aresta(origem, destino, peso));
    }

    /**
    * Retorna a lista de arestas do grafo
    * @return lista de arestas
    */
    public int getNumVertices() {
        return numVertices;
    }

    /**
     * Retorna o numero de arestas do grafo
     */
    public int getNumArestas() {
        return arestas.size();
    }

    /**
     * Retorna a lista de arestas do grafo
     * @return lista de arestas
     */
    public List<Aresta> getArestas() {
        return arestas;
    }    

    /**
     * Retorna a lista de arestas ordenadas por peso (crescente)
     * @return lista de arestas ordenadas
     */
    public List<Aresta> getArestasOrdenadas() {
        List<Aresta> copia = new ArrayList<>(arestas);
        Collections.sort(copia);
        return copia;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Grafo com ").append(numVertices).append(" vértices e ")
          .append(arestas.size()).append(" arestas:\n");
        
        for (Aresta a : arestas) {
            sb.append("  ").append(a).append("\n");
        }
        
        return sb.toString();
    }        

}
