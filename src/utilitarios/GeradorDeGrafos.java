package utilitarios;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import modelos.Grafo;

public class GeradorDeGrafos {
    
    private final Random random;
    
    public GeradorDeGrafos() {
        this.random = new Random();
    }
    
    public GeradorDeGrafos(long seed) {
        this.random = new Random(seed);
    }
    
    /**
     * Gera um grafo CONEXO.
     */
    public Grafo gerarGrafoConexo(int numVertices, int numArestas,
                                   int pesoMin, int pesoMax) {
        
        if (numVertices < 1) {
            throw new IllegalArgumentException("Número de vértices deve ser pelo menos 1");
        }
        
        int maxArestas = numVertices * (numVertices - 1) / 2;
        if (numArestas > maxArestas) {
            numArestas = maxArestas;
        }
        
        if (numArestas < numVertices - 1) {
            numArestas = numVertices - 1;  // Ajusta para o mínimo necessário
        }
        
        Grafo grafo = new Grafo(numVertices);
        Set<String> arestasExistentes = new HashSet<>();
        
        // Passo 1: Árvore geradora (garante conexidade)
        List<Integer> vertices = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            vertices.add(i);
        }
        
        Collections.shuffle(vertices, random);
        
        for (int i = 1; i < numVertices; i++) {
            int u = vertices.get(i);
            int indiceAnterior = random.nextInt(i);
            int v = vertices.get(indiceAnterior);
            
            int peso = pesoMin + random.nextInt(pesoMax - pesoMin + 1);
            grafo.adicionarAresta(u, v, peso);
            
            String chave = u < v ? u + "," + v : v + "," + u;
            arestasExistentes.add(chave);
        }
        
        // Passo 2: Arestas extras
        int arestasExtras = numArestas - (numVertices - 1);
        int arestasAdicionadas = 0;
        
        while (arestasAdicionadas < arestasExtras) {
            int u = random.nextInt(numVertices);
            int v = random.nextInt(numVertices);
            
            if (u == v) continue;
            
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
     * Gera um grafo DESCONEXO.
     * Cria dois componentes separados.
     */
    public Grafo gerarGrafoDesconexo(int numVertices, int numArestas,
                                      int pesoMin, int pesoMax) {
        
        if (numVertices < 2) {
            throw new IllegalArgumentException("Grafo desconexo precisa de pelo menos 2 vértices");
        }
        
        Grafo grafo = new Grafo(numVertices);
        Set<String> arestasExistentes = new HashSet<>();
        
        // Divide os vértices em dois grupos
        int tamanhoGrupo1 = numVertices / 2;
        int tamanhoGrupo2 = numVertices - tamanhoGrupo1;
        
        // Grupo 1: vértices 0..tamanhoGrupo1-1
        // Grupo 2: vértices tamanhoGrupo1..numVertices-1
        
        int arestasAdicionadas = 0;
        int maxArestas = (tamanhoGrupo1 * (tamanhoGrupo1 - 1) / 2) + 
                         (tamanhoGrupo2 * (tamanhoGrupo2 - 1) / 2);
        
        if (numArestas > maxArestas) {
            numArestas = maxArestas;
        }
        
        while (arestasAdicionadas < numArestas) {
            // Decide aleatoriamente em qual grupo adicionar a aresta
            boolean grupo1 = random.nextBoolean();
            
            int u, v;
            if (grupo1) {
                u = random.nextInt(tamanhoGrupo1);
                v = random.nextInt(tamanhoGrupo1);
            } else {
                u = tamanhoGrupo1 + random.nextInt(tamanhoGrupo2);
                v = tamanhoGrupo1 + random.nextInt(tamanhoGrupo2);
            }
            
            if (u == v) continue;
            
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
     * Gera um grafo para o pior caso (cadeia).
     */
    public Grafo gerarPiorCaso(int numVertices, int pesoMin, int pesoMax) {
        Grafo grafo = new Grafo(numVertices);
        
        for (int i = 0; i < numVertices - 1; i++) {
            int peso = pesoMin + random.nextInt(pesoMax - pesoMin + 1);
            grafo.adicionarAresta(i, i + 1, peso);
        }
        
        return grafo;
    }
}