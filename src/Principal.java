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
 * Os resultados são salvos em arquivos CSV para análise e geração de gráficos.
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
        resultadosCSV.add("tipo_dsu,n_vertices,n_arestas,repeticao,tempo_ms,operacoes_find,operacoes_union,total_acessos");
        
        // Para cada tamanho de grafo
        for (int n : TAMANHOS_VERTICES) {
            int m = Math.min(n * ARESTAS_POR_VERTICE, n * (n - 1) / 2);
            
            System.out.println("=========================================");
            System.out.println("Testando com n = " + n + " vértices, m = " + m + " arestas");
            System.out.println("=========================================\n");
            
            // Gera o grafo uma vez para ser usado em todas as repetições
            Grafo grafo = gerador.gerarGrafoAleatorio(n, m, PESO_MIN, PESO_MAX);
            
            // Testa as três implementações
            testarImplementacao(grafo, new DSUNaive(), "naive", resultadosCSV);
            testarImplementacao(grafo, new DSURank(), "rank", resultadosCSV);
            testarImplementacao(grafo, new DSUTarjan(), "tarjan", resultadosCSV);
        }
        
        // Salvar resultados em arquivo CSV
        salvarResultados(resultadosCSV, "dados/resultados/experimento.csv");
        
        System.out.println("\n=========================================");
        System.out.println("Experimento concluído!");
        System.out.println("Resultados salvos em: dados/resultados/experimento.csv");
        System.out.println("=========================================");
    }
    
    /**
     * Testa uma implementação específica do DSU com o grafo fornecido.
     * 
     * @param grafo      grafo a ser processado
     * @param dsu        implementação do DSU
     * @param nome       nome da implementação (naive, rank, tarjan)
     * @param resultados lista para acumular os resultados
     */
    private static void testarImplementacao(Grafo grafo, DSU dsu, String nome, List<String> resultados) {
        int n = grafo.getNumVertices();
        int m = grafo.getNumArestas();
        
        System.out.println("Testando DSU " + nome.toUpperCase() + "...");
        
        for (int rep = 1; rep <= REPETICOES; rep++) {
            // Cria um novo coletor de métricas para cada repetição
            ColetorMetricas coletor = new ColetorMetricas();
            
            // Medição de tempo
            coletor.iniciarTempo();
            
            // Executa Kruskal com o DSU (que não coleta métricas diretamente)
            // Por enquanto, apenas o tempo é medido
            int custoMST = AlgoritmoKruskal.encontrarMST(grafo, dsu);
            
            coletor.pararTempo();
            
            // Registra resultado (como não temos métricas do DSU, colocamos 0)
            // Para ter métricas completas, seria necessário um DSU com instrumentação
            String linha = String.format("%s,%d,%d,%d,%.3f,%d,%d,%d",
                    nome, n, m, rep,
                    coletor.getTempoMilissegundos(),
                    0, 0, 0);  // Placeholder para métricas do DSU
            
            resultados.add(linha);
            
            System.out.printf("  Repetição %d: tempo = %.3f ms, custo MST = %d\n", 
                    rep, coletor.getTempoMilissegundos(), custoMST);
        }
        
        System.out.println();
    }
    
    /**
     * Testa uma implementação específica do DSU com o grafo de pior caso.
     */
    private static void testarPiorCaso(GeradorDeGrafos gerador, DSU dsu, String nome, 
                                        int n, List<String> resultados) {
        Grafo grafo = gerador.gerarPiorCaso(n, PESO_MIN, PESO_MAX);
        int m = grafo.getNumArestas();
        
        System.out.println("Testando DSU " + nome.toUpperCase() + " (pior caso)...");
        
        for (int rep = 1; rep <= REPETICOES; rep++) {
            ColetorMetricas coletor = new ColetorMetricas();
            
            coletor.iniciarTempo();
            int custoMST = AlgoritmoKruskal.encontrarMST(grafo, dsu);
            coletor.pararTempo();
            
            String linha = String.format("%s_pior,%d,%d,%d,%.3f,%d,%d,%d",
                    nome, n, m, rep,
                    coletor.getTempoMilissegundos(),
                    0, 0, 0);
            
            resultados.add(linha);
            
            System.out.printf("  Repetição %d: tempo = %.3f ms, custo MST = %d\n", 
                    rep, coletor.getTempoMilissegundos(), custoMST);
        }
        
        System.out.println();
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