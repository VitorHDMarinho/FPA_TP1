package utilitarios;

/**
 * Coletor de métricas para medir tempo de execução e acessos à memória.
 * 
 * As métricas coletadas são:
 * - Tempo total de execução (nanossegundos e milissegundos)
 * - Número de operações find realizadas
 * - Número de operações union realizadas
 * - Número total de acessos ao array de pais
 * 
 * @author FPAA - Trabalho Prático 1
 */
public class ColetorMetricas {
    
    private long operacoesFind;
    private long operacoesUnion;
    private long acessosPai;      
    private long acessosRank;     
    
    private long tempoInicio;
    private long tempoFim;
    
    /**
     * Construtor padrão - zera todas as métricas.
     */
    public ColetorMetricas() {
        reset();
    }
    
    /**
     * Reinicia todos os contadores.
     */
    public void reset() {
        operacoesFind = 0;
        operacoesUnion = 0;
        acessosPai = 0;
        acessosRank = 0;
        tempoInicio = 0;
        tempoFim = 0;
    }
    
    /**
     * Inicia a contagem de tempo.
     */
    public void iniciarTempo() {
        tempoInicio = System.nanoTime();
    }
    
    /**
     * Para a contagem de tempo.
     */
    public void pararTempo() {
        tempoFim = System.nanoTime();
    }
    
    /**
     * Registra um acesso de leitura ao array pai.
     */
    public void registrarLeituraPai() {
        acessosPai++;
    }
    
    /**
     * Registra um acesso de escrita ao array pai.
     */
    public void registrarEscritaPai() {
        acessosPai++;
    }
    
    /**
     * Registra um acesso de leitura ao array rank.
     */
    public void registrarLeituraRank() {
        acessosRank++;
    }
    
    /**
     * Registra um acesso de escrita ao array rank.
     */
    public void registrarEscritaRank() {
        acessosRank++;
    }
    
    /**
     * Registra uma operação de find.
     */
    public void registrarFind() {
        operacoesFind++;
    }
    
    /**
     * Registra uma operação de union.
     */
    public void registrarUnion() {
        operacoesUnion++;
    }
    
    /**
     * Retorna o número de operações find realizadas.
     */
    public long getOperacoesFind() {
        return operacoesFind;
    }
    
    /**
     * Retorna o número de operações union realizadas.
     */
    public long getOperacoesUnion() {
        return operacoesUnion;
    }
    
    /**
     * Retorna o total de operações (find + union).
     */
    public long getTotalOperacoes() {
        return operacoesFind + operacoesUnion;
    }
    
    /**
     * Retorna o total de acessos ao array pai.
     */
    public long getAcessosPai() {
        return acessosPai;
    }
    
    /**
     * Retorna o total de acessos ao array rank.
     */
    public long getAcessosRank() {
        return acessosRank;
    }
    
    /**
     * Retorna o total de acessos à memória (pai + rank).
     */
    public long getTotalAcessos() {
        return acessosPai + acessosRank;
    }
    
    /**
     * Retorna o tempo de execução em nanossegundos.
     */
    public long getTempoNanosegundos() {
        return tempoFim - tempoInicio;
    }
    
    /**
     * Retorna o tempo de execução em milissegundos.
     */
    public double getTempoMilissegundos() {
        return (tempoFim - tempoInicio) / 1_000_000.0;
    }
    
    /**
     * Retorna um resumo formatado das métricas coletadas.
     */
    public String gerarRelatorio() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== RELATÓRIO DE MÉTRICAS ==========\n");
        sb.append(String.format("Tempo: %.3f ms (%d ns)\n", 
                getTempoMilissegundos(), getTempoNanosegundos()));
        sb.append(String.format("Operações find: %d\n", operacoesFind));
        sb.append(String.format("Operações union: %d\n", operacoesUnion));
        sb.append(String.format("Total de operações: %d\n", getTotalOperacoes()));
        sb.append(String.format("Acessos ao array pai: %d\n", acessosPai));
        sb.append(String.format("Acessos ao array rank: %d\n", acessosRank));
        sb.append(String.format("Total de acessos à memória: %d\n", getTotalAcessos()));
        sb.append("===========================================");
        return sb.toString();
    }
    
    /**
     * Retorna os dados em formato CSV (para salvar nos resultados).
     * 
     * @return linha CSV com as métricas
     */
    public String toCSV() {
        return String.format("%d,%d,%d,%d,%d,%.3f",
                operacoesFind,
                operacoesUnion,
                getTotalOperacoes(),
                getTotalAcessos(),
                getTempoNanosegundos(),
                getTempoMilissegundos());
    }
    
    /**
     * Retorna o cabeçalho do CSV.
     */
    public static String getCSVCabecalho() {
        return "operacoes_find,operacoes_union,total_operacoes,total_acessos,tempo_ns,tempo_ms";
    }
}