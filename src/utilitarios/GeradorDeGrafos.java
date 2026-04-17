package utilitarios;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import modelos.Grafo;

/**
 * Gerador de grafos aleatórios para testes.
 * 
 */
public class GeradorDeGrafos {
    
    private final Random random;
    
    public GeradorDeGrafos() {
        this.random = new Random();
    }
    
    public GeradorDeGrafos(long seed) {
        this.random = new Random(seed);
    }
    
    /**
     * Gera um grafo aleatório com o número especificado de vértices e arestas.
     * 
     * @param numVertices número de vértices
     * @param numArestas  número de arestas
     * @param pesoMin     peso mínimo
     * @param pesoMax     peso máximo
     * @return grafo gerado
     */
    public Grafo gerarGrafoAleatorio(int numVertices, int numArestas, 
                                      int pesoMin, int pesoMax) {
        
        Grafo grafo = new Grafo(numVertices);
        Set<String> arestasExistentes = new HashSet<>();
        
        int maxArestas = numVertices * (numVertices - 1) / 2;
        if (numArestas > maxArestas) {
            numArestas = maxArestas;
        }
        
        int arestasAdicionadas = 0;
        
        while (arestasAdicionadas < numArestas) {
            int u = random.nextInt(numVertices);
            int v = random.nextInt(numVertices);
            
            if (u == v) continue;  // sem laços
            
            String chave = u < v ? u + "," + v : v + "," + u;
            if (arestasExistentes.contains(chave)) continue;
            
            int peso = pesoMin + random.nextInt(pesoMax - pesoMin + 1);
            grafo.adicionarAresta(u, v, peso);
            arestasExistentes.add(chave);
            arestasAdicionadas++;
        }
        
        return grafo;
    }
    
    /**
     * Gera um grafo para o pior caso do DSU ingênuo.
     * As arestas são geradas em uma ordem que força a árvore a degenerar.
     * 
     * @param numVertices número de vértices
     * @param pesoMin     peso mínimo
     * @param pesoMax     peso máximo
     * @return grafo para pior caso
     */
    public Grafo gerarPiorCaso(int numVertices, int pesoMin, int pesoMax) {
        Grafo grafo = new Grafo(numVertices);
        
        // Adiciona arestas na ordem que força a degeneração
        // Union(0,1), Union(1,2), Union(2,3), ...
        for (int i = 0; i < numVertices - 1; i++) {
            int peso = pesoMin + random.nextInt(pesoMax - pesoMin + 1);
            grafo.adicionarAresta(i, i + 1, peso);
        }
        
        return grafo;
    }
}