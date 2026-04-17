import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import modelos.Grafo;
import nucleo.DSU;
import nucleo.DSUNaive;
import nucleo.DSURank;
import nucleo.DSUTarjan;
import utilitarios.ColetorMetricas;
import utilitarios.GeradorDeGrafos;
import algoritmos.AlgoritmoKruskal;

/**
 * Classe principal para execução dos experimentos.
 * 
 * Realiza testes comparativos entre as três implementações de DSU:
 * - Ingênua (Naive)
 * - Union by Rank
 * - Tarjan (Rank + Path Compression)
 * 
 * Os testes são realizados com:
 * - Grafos conexos
 * - Grafos desconexos
 * 
 * Os resultados são salvos em arquivos CSV para análise e geração de gráficos.
 * As entradas (grafos gerados) também são salvas para reprodutibilidade.
 * 
 * @author FPAA - Trabalho Prático 1
 */
public class Principal {
    
    // Configurações dos experimentos
    private static final int[] TAMANHOS_VERTICES = {100, 500, 1000, 2000, 5000, 10000};
    private static final int ARESTAS_POR_VERTICE = 5;  // ~5n arestas (grafo esparso)
    private static final int PESO_MIN = 1;
    private static final int PESO_MAX = 100;
    private static final int REPETICOES = 3;  // número de repetições para cada configuração
    
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("Experimento DSU x Kruskal - MST");
        System.out.println("=========================================\n");
        
        GeradorDeGrafos gerador = new GeradorDeGrafos(42);  // semente fixa para reprodutibilidade
        
        // Listas para armazenar os resultados
        List<String> resultadosCSV = new ArrayList<>();
        
        // Cabeçalho do CSV
        resultadosCSV.add("tipo_grafo,tipo_dsu,n_vertices,n_arestas,repeticao,tempo_ms,custo_mst");
        
        // Para cada tamanho de grafo
        for (int n : TAMANHOS_VERTICES) {
            int m = Math.min(n * ARESTAS_POR_VERTICE, n * (n - 1) / 2);
            
            // ======================================================
            // TESTE COM GRAFO CONEXO
            // ======================================================
            System.out.println("=========================================");
            System.out.println(">>> GRAFO CONEXO: n = " + n + ", m = " + m);
            System.out.println("=========================================\n");
            
            Grafo grafoConexo = gerador.gerarGrafoConexo(n, m, PESO_MIN, PESO_MAX);
            
            // Salva o grafo conexo gerado
            salvarGrafo(grafoConexo, n, m, "conexo");
            
            // Testa as três implementações no grafo conexo
            testarImplementacao(grafoConexo, "conexo", resultadosCSV);
            
            // ======================================================
            // TESTE COM GRAFO DESCONEXO
            // ======================================================
            System.out.println("=========================================");
            System.out.println(">>> GRAFO DESCONEXO: n = " + n + ", m = " + m);
            System.out.println("=========================================\n");
            
            Grafo grafoDesconexo = gerador.gerarGrafoDesconexo(n, m, PESO_MIN, PESO_MAX);
            
            // Salva o grafo desconexo gerado
            salvarGrafo(grafoDesconexo, n, m, "desconexo");
            
            // Testa as três implementações no grafo desconexo
            testarImplementacao(grafoDesconexo, "desconexo", resultadosCSV);
        }
        
        // Salvar resultados em arquivo CSV
        salvarResultados(resultadosCSV, "dados/resultados/experimento.csv");
        
        System.out.println("\n=========================================");
        System.out.println("Experimento concluído!");
        System.out.println("Resultados salvos em: dados/resultados/experimento.csv");
        System.out.println("Grafos salvos em: dados/entradas/");
        System.out.println("=========================================");
    }
    
    /**
     * Testa as três implementações de DSU com um grafo.
     * 
     * @param grafo      grafo a ser processado
     * @param tipoGrafo  "conexo" ou "desconexo"
     * @param resultados lista para acumular os resultados
     */
    private static void testarImplementacao(Grafo grafo, String tipoGrafo, List<String> resultados) {
        int n = grafo.getNumVertices();
        int m = grafo.getNumArestas();
        
        // Testa DSU Naive
        System.out.println("  Testando DSU NAIVE...");
        for (int rep = 1; rep <= REPETICOES; rep++) {
            DSUNaive dsu = new DSUNaive();
            dsu.reset(n);  // Inicializa com n elementos
            
            ColetorMetricas coletor = new ColetorMetricas();
            
            coletor.iniciarTempo();
            int custoMST = AlgoritmoKruskal.encontrarMST(grafo, dsu);
            coletor.pararTempo();
            
            String linha = String.format("%s,naive,%d,%d,%d,%.3f,%d",
                    tipoGrafo, n, m, rep,
                    coletor.getTempoMilissegundos(),
                    custoMST);
            
            resultados.add(linha);
            System.out.printf("    Rep %d: tempo = %.3f ms, custo = %d\n", 
                    rep, coletor.getTempoMilissegundos(), custoMST);
        }
        
        // Testa DSU Rank
        System.out.println("  Testando DSU RANK...");
        for (int rep = 1; rep <= REPETICOES; rep++) {
            DSURank dsu = new DSURank();
            dsu.reset(n);
            
            ColetorMetricas coletor = new ColetorMetricas();
            
            coletor.iniciarTempo();
            int custoMST = AlgoritmoKruskal.encontrarMST(grafo, dsu);
            coletor.pararTempo();
            
            String linha = String.format("%s,rank,%d,%d,%d,%.3f,%d",
                    tipoGrafo, n, m, rep,
                    coletor.getTempoMilissegundos(),
                    custoMST);
            
            resultados.add(linha);
            System.out.printf("    Rep %d: tempo = %.3f ms, custo = %d\n", 
                    rep, coletor.getTempoMilissegundos(), custoMST);
        }
        
        // Testa DSU Tarjan
        System.out.println("  Testando DSU TARJAN...");
        for (int rep = 1; rep <= REPETICOES; rep++) {
            DSUTarjan dsu = new DSUTarjan();
            dsu.reset(n);
            
            ColetorMetricas coletor = new ColetorMetricas();
            
            coletor.iniciarTempo();
            int custoMST = AlgoritmoKruskal.encontrarMST(grafo, dsu);
            coletor.pararTempo();
            
            String linha = String.format("%s,tarjan,%d,%d,%d,%.3f,%d",
                    tipoGrafo, n, m, rep,
                    coletor.getTempoMilissegundos(),
                    custoMST);
            
            resultados.add(linha);
            System.out.printf("    Rep %d: tempo = %.3f ms, custo = %d\n", 
                    rep, coletor.getTempoMilissegundos(), custoMST);
        }
        
        System.out.println();
    }
    
    /**
     * Salva um grafo em arquivo no formato:
     * n m
     * u v peso
     * u v peso
     * ...
     * 
     * @param grafo     grafo a ser salvo
     * @param n         número de vértices
     * @param m         número de arestas
     * @param tipo      "conexo" ou "desconexo"
     */
    private static void salvarGrafo(Grafo grafo, int n, int m, String tipo) {
        String caminho = String.format("dados/entradas/grafo_%s_n%d_m%d.txt", tipo, n, m);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminho))) {
            // Escreve cabeçalho com número de vértices e arestas
            writer.printf("%d %d\n", n, m);
            
            // Escreve cada aresta
            for (int i = 0; i < grafo.getNumArestas(); i++) {
                var aresta = grafo.getArestas().get(i);
                writer.printf("%d %d %d\n", 
                        aresta.getOrigem(), 
                        aresta.getDestino(), 
                        aresta.getPeso());
            }
            
            System.out.println("  Grafo salvo em: " + caminho);
        } catch (Exception e) {
            System.err.println("  Erro ao salvar grafo: " + e.getMessage());
        }
    }
    
    /**
     * Salva os resultados em um arquivo CSV.
     * 
     * @param resultados lista de linhas do CSV
     * @param caminho    caminho do arquivo
     */
    private static void salvarResultados(List<String> resultados, String caminho) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminho))) {
            for (String linha : resultados) {
                writer.println(linha);
            }
            System.out.println("Resultados salvos com sucesso em: " + caminho);
        } catch (Exception e) {
            System.err.println("Erro ao salvar resultados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}