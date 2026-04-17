package nucleo;

/**
 * Implementação completa do DSU com as otimizações de Tarjan.
 * Union by Rank + Path Compression.
 * 
 * Complexidade amortizada: O(α(n)), onde α é a inversa da função de Ackermann.
 * Na prática, é considerada constante.
 * 
 */
public class DSUTarjan extends DSU {
    
    public DSUTarjan(int n) {
        super(n);
    }

    public DSUTarjan(){
        super();
    }
    
    @Override
    public int find(int i) {
        validarIndice(i);
        // Path Compression: cada elemento aponta diretamente para a raiz
        if (pai[i] != i) {
            pai[i] = find(pai[i]);
        }
        return pai[i];
    }
    
    @Override
    public void union(int i, int j) {
        int raizI = find(i);
        int raizJ = find(j);
        
        if (raizI != raizJ) {
            if (rank[raizI] < rank[raizJ]) {
                pai[raizI] = raizJ;
            } else if (rank[raizI] > rank[raizJ]) {
                pai[raizJ] = raizI;
            } else {
                pai[raizJ] = raizI;
                rank[raizI]++;
            }
        }
    }
}