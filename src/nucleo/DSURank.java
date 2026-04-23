package nucleo;

public class DSURank extends DSU {
    
    public DSURank(int n) { super(n); }
    public DSURank() { super(); }
    
    @Override
    public int find(int i) {
        validarIndice(i);
        if (coletor != null) coletor.registrarFind();
        
        int curr = i;
        while (pai[curr] != curr) {
            if (coletor != null) coletor.registrarLeituraPai();
            curr = pai[curr];
        }
        return curr;
    }
    
    @Override
    public void union(int i, int j) {
        int raizI = find(i);
        int raizJ = find(j);
        
        if (raizI != raizJ) {
            if (coletor != null) {
                coletor.registrarUnion();
                coletor.registrarLeituraRank(); // Lendo ranks para comparar
                coletor.registrarLeituraRank();
            }
            
            if (rank[raizI] < rank[raizJ]) {
                pai[raizI] = raizJ;
                if (coletor != null) coletor.registrarEscritaPai();
            } else if (rank[raizI] > rank[raizJ]) {
                pai[raizJ] = raizI;
                if (coletor != null) coletor.registrarEscritaPai();
            } else {
                pai[raizJ] = raizI;
                rank[raizI]++;
                if (coletor != null) {
                    coletor.registrarEscritaPai();
                    coletor.registrarEscritaRank();
                }
            }
        }
    }
}