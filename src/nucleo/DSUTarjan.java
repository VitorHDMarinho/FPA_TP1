package nucleo;

public class DSUTarjan extends DSU {
    
    public DSUTarjan(int n) { super(n); }
    public DSUTarjan() { super(); }
    
    @Override
    public int find(int i) {
        validarIndice(i);
        if (coletor != null) coletor.registrarFind();
        
        if (pai[i] != i) {
            if (coletor != null) coletor.registrarLeituraPai();
            pai[i] = find(pai[i]); // Compressão
            if (coletor != null) coletor.registrarEscritaPai();
        }
        return pai[i];
    }
    
    @Override
    public void union(int i, int j) {
        int raizI = find(i);
        int raizJ = find(j);
        
        if (raizI != raizJ) {
            if (coletor != null) {
                coletor.registrarUnion();
                coletor.registrarLeituraRank(); 
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