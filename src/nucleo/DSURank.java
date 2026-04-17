package nucleo;

/**
 * Implementação do DSU com Union by Rank.
 * Une as árvores pela altura (rank) para manter a árvore balanceada.
 * 
 * Complexidade:
 * - Find: O(log n)
 * - Union: O(log n)
 * 
 */
public class DSURank extends DSU {
    
    public DSURank(int n) {
        super(n);
    }
    
    public DSURank(){
        super();
    }
    
    @Override
    public int find(int i) {
        validarIndice(i);
        if (pai[i] != i) {
            return find(pai[i]);
        }
        return i;
    }
    
    @Override
    public void union(int i, int j) {
        int raizI = find(i);
        int raizJ = find(j);
        
        if (raizI != raizJ) {
            // anexa a árvore menor à maior
            if (rank[raizI] < rank[raizJ]) {
                pai[raizI] = raizJ;
            } else if (rank[raizI] > rank[raizJ]) {
                pai[raizJ] = raizI;
            } else {
                // uma vira filha da outra e rank aumenta
                pai[raizJ] = raizI;
                rank[raizI]++;
            }
        }
    }
}