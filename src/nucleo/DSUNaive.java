package nucleo;

/**
 * Implementação Ingênua (Naive) do DSU.
 * Sem heurísticas de balanceamento (Union by Rank) ou compressão de caminho.
 * 
 * Complexidade:
 * - Find: O(n) no pior caso
 * - Union: O(n) no pior caso
 * 
 */
public class DSUNaive extends DSU {

    public DSUNaive(int n) {
        super(n);
    }

    public DSUNaive() {
        super();
    }

        
    @Override
    public int find(int i){
        validarIndice(i);
        while (pai[i] != i) {
            i = pai[i];
        }
        return i;
    }

    @Override
    public void union(int i, int j){
        int raizI = find(i);
        int raizJ = find(j);
        if (raizI != raizJ) {
            pai[raizJ] = raizI; // Une j em i (sem heurística)
        }

    }
}
