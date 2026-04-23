package nucleo;

public class DSUNaive extends DSU {

    public DSUNaive(int n) { super(n); }
    public DSUNaive() { super(); }

    @Override
    public int find(int i) {
        validarIndice(i);
        if (coletor != null) coletor.registrarFind();
        
        while (pai[i] != i) {
            if (coletor != null) coletor.registrarLeituraPai();
            i = pai[i];
        }
        return i;
    }

    @Override
    public void union(int i, int j) {
        int raizI = find(i);
        int raizJ = find(j);
        if (raizI != raizJ) {
            if (coletor != null) {
                coletor.registrarUnion();
                coletor.registrarEscritaPai();
            }
            pai[raizJ] = raizI;
        }
    }
}