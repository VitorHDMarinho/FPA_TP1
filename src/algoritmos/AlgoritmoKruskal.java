package algoritmos;

import java.util.List;
import modelos.Aresta;
import modelos.Grafo;
import nucleo.DSU;

/**
 * Implementação do algoritmo de Kruskal para encontrar a Árvore Geradora Mínima (MST).
 * 
 * O algoritmo funciona da seguinte forma:
 * 1. Ordena todas as arestas do grafo em ordem crescente de peso
 * 2. Para cada aresta (na ordem ordenada):
 *    - Se os vértices da aresta pertencem a componentes diferentes (find(u) != find(v))
 *    - Adiciona a aresta à MST e une os componentes (union(u, v))
 * 3. Repete até que todos os vértices estejam conectados (n-1 arestas na MST)
 * 
 * A estrutura DSU (Union-Find) é responsável por gerenciar os componentes conectados
 * e pode ser injetada em diferentes versões (Ingênua, Rank, Tarjan) para comparação.
 * 
 */

public class AlgoritmoKruskal {
    
    /**
     * Executa o algoritmo de Kruskal para encontrar o custo da MST.
     * 
     * @param grafo Grafo contendo vértices e arestas
     * @param dsu   Estrutura DSU (Union-Find) para gerenciar componentes
     * @return      Custo total da Árvore Geradora Mínima
     * 
     * @throws IllegalArgumentException se o grafo for nulo ou o DSU for nulo
     */
    public static int encontrarMST(Grafo grafo, DSU dsu) {
        if (grafo == null) {
            throw new IllegalArgumentException("Grafo não pode ser nulo");
        }
        if (dsu == null) {
            throw new IllegalArgumentException("DSU não pode ser nulo");
        }
        
        int numVertices = grafo.getNumVertices();
        
        if (numVertices <= 1) {
            return 0;
        }
        
        dsu.reset(numVertices);
        
        List<Aresta> arestasOrdenadas = grafo.getArestasOrdenadas();
        
        int custoTotal = 0;
        int arestasNaMST = 0;
        
        for (Aresta aresta : arestasOrdenadas) {
            int u = aresta.getOrigem();
            int v = aresta.getDestino();
            int peso = aresta.getPeso();
            
            // Verifica se os vértices estão em componentes diferentes
            if (dsu.find(u) != dsu.find(v)) {
                // Adiciona aresta à MST
                dsu.union(u, v);
                custoTotal += peso;
                arestasNaMST++;
                
                if (arestasNaMST == numVertices - 1) {
                    break;
                }
            }
        }
        
        // Verifica se o grafo é conexo (se encontrou MST completa)
        if (arestasNaMST != numVertices - 1) {
            System.err.println("Aviso: Grafo não é conexo! MST encontrada com " 
                    + arestasNaMST + " arestas de " + (numVertices - 1) + " necessárias.");
        }
        
        return custoTotal;
    }
    
    /**
     * Executa o algoritmo de Kruskal e retorna o custo da MST.
     * Versão que não faz reset do DSU (útil quando o DSU já está configurado).
     * 
     * @param grafo Grafo contendo vértices e arestas
     * @param dsu   Estrutura DSU já inicializada
     * @param reset Se true, reinicializa o DSU antes da execução
     * @return      Custo total da Árvore Geradora Mínima
     */
    public static int encontrarMST(Grafo grafo, DSU dsu, boolean reset) {
        if (reset) {
            dsu.reset(grafo.getNumVertices());
        }
        return encontrarMST(grafo, dsu);
    }
}