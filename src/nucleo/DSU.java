package nucleo;

import utilitarios.ColetorMetricas;

/**
 * Classe abstrata que define o contrato para implementações da estrutura
 * Disjoint Set Union (Union-Find).
 * 
 * As operações fundamentais são:
 * - find(i): encontra o representante (raiz) do conjunto que contém i
 * - union(i, j): une os conjuntos que contêm i e j
 * - reset(n): reinicializa a estrutura com n elementos isolados
 * 
 * As implementações concretas devem fornecer diferentes níveis de otimização:
 * - DSUIngenuo: sem heurísticas (find O(n), union O(n))
 * - DSURank: apenas Union by Rank (find O(log n), union O(log n))
 * - DSUTarjan: Union by Rank + Path Compression (amortizado O(α(n)))
 * 
 */
public abstract class DSU {
    
    
    protected int[] pai;
    
    protected int[] rank;
    
    protected int n;

    protected ColetorMetricas coletor;


        /**
     * Construtor padrão.
     * A estrutura fica vazia. É necessário chamar reset(n) antes do uso.
     */
    public DSU() {
        this.pai = null;
        this.rank = null;
        this.n = 0;
    }
    
    /**
     * Construtor que já inicializa a estrutura com n elementos.
     * Cada elemento é seu próprio representante (raiz).
     * 
     * @param n número de elementos (vértices) na estrutura
     * @throws IllegalArgumentException se n < 1
     */
    public DSU(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Número de elementos deve ser pelo menos 1: " + n);
        }
        this.n = n;
        this.pai = new int[n];
        this.rank = new int[n];
        
        for (int i = 0; i < n; i++) {
            pai[i] = i;
            rank[i] = 0;
        }
    }

    /**
     * Encontra o representante (raiz) do conjunto que contém o elemento i.
     * 
     * @param i elemento a ser buscado (0 <= i < n)
     * @return representante do conjunto de i
     */
    public abstract int find(int i);
    
    /**
     * Une os conjuntos que contêm os elementos i e j.
     * 
     * @param i primeiro elemento
     * @param j segundo elemento
     */
    public abstract void union(int i, int j);

    
    /**
     * Reinicializa a estrutura DSU com n elementos isolados.
     * Cada elemento é seu próprio representante (raiz).
     * 
     * @param n número de elementos (vértices) na estrutura
     * @throws IllegalArgumentException se n < 1
     */
    public void reset(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Número de elementos deve ser pelo menos 1: " + n);
        }
        
        this.n = n;
        this.pai = new int[n];
        this.rank = new int[n];
        
        // Inicialização: cada elemento é seu próprio pai (raiz)
        // e rank inicial é 0
        for (int i = 0; i < n; i++) {
            pai[i] = i;
            rank[i] = 0;
        }
    }
    
    /**
     * Verifica se dois elementos estão no mesmo conjunto.
     * 
     * @param i primeiro elemento
     * @param j segundo elemento
     * @return true se i e j estão no mesmo conjunto, false caso contrário
     */
    public boolean mesmoConjunto(int i, int j) {
        return find(i) == find(j);
    }
    
    /**
     * Retorna o número de elementos na estrutura.
     * 
     * @return quantidade de elementos
     */
    public int getNumElementos() {
        return n;
    }
    
    /**
     * Retorna o array de pais (para debugging e coleta de métricas).
     * 
     * @return cópia do array de pais
     */
    public int[] getArrayPai() {
        return pai.clone();  // Cópia defensiva para evitar modificações externas
    }
    
    /**
     * Retorna o array de rank (para debugging e coleta de métricas).
     * 
     * @return cópia do array de rank
     */
    public int[] getArrayRank() {
        return rank.clone();  // Cópia defensiva
    }
    
    /**
     * Verifica se um índice é válido.
     * 
     * @param i índice a ser verificado
     * @throws IllegalArgumentException se i for inválido
     */
    protected void validarIndice(int i) {
        if (i < 0 || i >= n) {
            throw new IllegalArgumentException("Índice inválido: " + i + 
                    ". Deve estar entre 0 e " + (n - 1));
        }
    }

    public void setColetor(ColetorMetricas coletor) {
    this.coletor = coletor;
}
    
    
    /**
     * Retorna uma representação textual da estrutura DSU.
     * Mostra para cada elemento seu pai e rank.
     * 
     * @return string representando o estado atual do DSU
     */
    @Override
    public String toString() {
        if (pai == null) {
            return "DSU não inicializado. Chame reset(n) primeiro.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("DSU (n=").append(n).append("):\n");
        sb.append("Elemento: ");
        for (int i = 0; i < n; i++) {
            sb.append(String.format("%3d ", i));
        }
        sb.append("\nPai:      ");
        for (int i = 0; i < n; i++) {
            sb.append(String.format("%3d ", pai[i]));
        }
        sb.append("\nRank:     ");
        for (int i = 0; i < n; i++) {
            sb.append(String.format("%3d ", rank[i]));
        }
        return sb.toString();
    }
}