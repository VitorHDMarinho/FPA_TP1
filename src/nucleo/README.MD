
## Índice
- [O que é DSU?](#o-que-é-dsu)
- [Conceitos Fundamentais](#conceitos-fundamentais)
- [Operações Básicas](#operações-básicas)
- [Problema da Implementação Ingênua](#problema-da-implementação-ingênua)
- [Otimizações](#otimizações)
- [Nossas Implementações](#nossas-implementações)

---

## O que é DSU?

**Disjoint Set Union (DSU)**, também conhecido como **Union-Find**, é uma estrutura de dados que gerencia uma coleção de **elementos particionados em subconjuntos disjuntos** (sem interseção).

### Analogia do Mundo Real

Imagine uma sala com `n` pessoas, onde inicialmente cada pessoa está em seu próprio grupo (isolada). Conforme as pessoas se conhecem, elas formam grupos de amigos. O DSU responde duas perguntas:

1. **Duas pessoas estão no mesmo grupo?** (operação `find`)
2. **Vamos unir dois grupos diferentes!** (operação `union`)

```
Inicialmente:  {0}  {1}  {2}  {3}  {4}
                     
União(0,1):    {0,1}  {2}  {3}  {4}
União(2,3):    {0,1}  {2,3}  {4}
União(1,2):    {0,1,2,3}  {4}

Find(0) == Find(3)? → Sim! (mesmo grupo)
Find(0) == Find(4)? → Não! (grupos diferentes)
```

### Aplicações

- **Algoritmo de Kruskal** (MST) - nosso caso de uso
- **Conectividade em redes** (redes sociais, redes de computadores)
- **Detecção de ciclos** em grafos
- **Sistemas de permissões** (Unix file system)
- **Processamento de imagens** (segmentação)

---

## Conceitos Fundamentais

### Representação como Floresta de Árvores

O DSU representa cada conjunto como uma **árvore**:
- Cada elemento tem um **pai** (ponteiro)
- A **raiz** da árvore é o **representante** do conjunto
- Elementos no mesmo conjunto compartilham a mesma raiz

```
Conjunto {0,1,2,3} representado como árvore:

        2 (raiz - representante)
       / \
      1   3
     /
    0

Find(0) = 2 (sobe até a raiz)
Find(3) = 2 (mesma raiz → mesmo conjunto)
```

### Arrays de Representação

Usamos dois arrays:
- `pai[i]`: o pai do elemento `i` (se `pai[i] == i`, `i` é raiz)
- `rank[i]`: a altura aproximada da árvore com raiz `i`

```java
pai[]  = [0, 2, 2, 2, 4]  // 0→2, 1→2, 2→2(raiz), 3→2, 4→4(raiz)
rank[] = [0, 0, 2, 0, 0]  // árvore com raiz 2 tem altura 2
```

---

## Operações Básicas

### Find(i)
Retorna o **representante** (raiz) do conjunto que contém `i`.

```java
int find(int i) {
    if (pai[i] == i) return i;  // i é a raiz
    return find(pai[i]);         // Sobe para o pai
}
```

**Exemplo:** `find(0)` na árvore acima:
1. `pai[0] = 1` → não é raiz
2. `pai[1] = 2` → não é raiz
3. `pai[2] = 2` → é raiz! retorna 2

### Union(i, j)
Une os conjuntos que contêm `i` e `j`.

```java
void union(int i, int j) {
    int raizI = find(i);
    int raizJ = find(j);
    
    if (raizI != raizJ) {
        pai[raizI] = raizJ;  // Uma raiz vira filha da outra
    }
}
```

---

## Problema da Implementação Ingênua

### O que acontece sem otimizações?

Uniões mal feitas podem criar uma **árvore degenerada** (cadeia):

```
Union(0,1): 1 → 0
Union(1,2): 2 → 1 → 0
Union(2,3): 3 → 2 → 1 → 0
Union(3,4): 4 → 3 → 2 → 1 → 0

Resultado:
    0 (raiz)
    |
    1
    |
    2
    |
    3
    |
    4

find(4) precisa percorrer 5 elementos! (4 → 3 → 2 → 1 → 0)
```

### Complexidade da versão Ingênua

| Operação | Melhor caso | Pior caso |
|----------|-------------|-----------|
| Find | O(1) | O(n) |
| Union | O(1) | O(n) |

Para `n = 100.000` elementos, um `find` pode custar 100.000 operações!

---

## Otimizações

### 1. Union by Rank

**Ideia:** Sempre anexar a árvore **menor** (em altura) à árvore **maior**.

```java
void union(int i, int j) {
    int raizI = find(i);
    int raizJ = find(j);
    
    if (raizI != raizJ) {
        if (rank[raizI] < rank[raizJ]) {
            pai[raizI] = raizJ;      // Árvore I é menor → vira filha
        } else if (rank[raizI] > rank[raizJ]) {
            pai[raizJ] = raizI;      // Árvore J é menor → vira filha
        } else {
            pai[raizJ] = raizI;      // Ranks iguais: uma vira filha
            rank[raizI]++;           // Altura aumenta em 1
        }
    }
}
```

**Efeito:** A altura máxima da árvore é **O(log n)**.

### 2. Path Compression (Compressão de Caminho)

**Ideia:** Durante `find`, fazer todos os elementos no caminho apontarem diretamente para a raiz.

```java
// Versão recursiva (mais elegante)
int find(int i) {
    if (pai[i] != i) {
        pai[i] = find(pai[i]);  // Compressão!
    }
    return pai[i];
}

// Versão iterativa (evita recursão profunda)
int find(int i) {
    int raiz = i;
    while (pai[raiz] != raiz) {
        raiz = pai[raiz];
    }
    while (pai[i] != i) {
        int proximo = pai[i];
        pai[i] = raiz;
        i = proximo;
    }
    return raiz;
}
```

**Efeito:** Achatamento da árvore → operações quase O(1).

### Visualização da Compressão

```
Antes do find(4):
    0
    |
    1
    |
    2
    |
    3
    |
    4

Após find(4):
    0
    |   |
    1   2
    |   |
    3   4
    (simplificado)
    Todos os elementos no caminho apontam para a raiz!
```

---

## Nossas Implementações

### Estrutura da Classe Abstrata DSU

```java
public abstract class DSU {
    protected int[] pai;    // Array de pais
    protected int[] rank;   // Array de rank (altura)
    protected int n;        // Número de elementos
    
    // Construtores
    public DSU() { ... }
    public DSU(int n) { ... }
    
    // Métodos abstratos (implementados pelas subclasses)
    public abstract int find(int i);
    public abstract void union(int i, int j);
    
    // Métodos concretos (compartilhados)
    public void reset(int n) { ... }
    public boolean mesmoConjunto(int i, int j) { ... }
    public int getNumElementos() { ... }
}
```

### 1. DSUNaive

**Características:** Sem otimizações

```java
public class DSUNaive extends DSU {
    
    public DSUIngenuo() { super(); }
    public DSUIngenuo(int n) { super(n); }
    
    @Override
    public int find(int i) {
        validarIndice(i);
        if (pai[i] != i) {
            return find(pai[i]);  // Sem compressão
        }
        return i;
    }
    
    @Override
    public void union(int i, int j) {
        int raizI = find(i);
        int raizJ = find(j);
        
        if (raizI != raizJ) {
            pai[raizI] = raizJ;  // Sem critério de balanceamento
        }
    }
}
```

**Complexidade:** O(n) para find e union no pior caso.

### 2. DSURank (Apenas Union by Rank)

**Características:** Apenas balanceamento por rank

```java
public class DSURank extends DSU {
    
    public DSURank() { super(); }
    public DSURank(int n) { super(n); }
    
    @Override
    public int find(int i) {
        validarIndice(i);
        if (pai[i] != i) {
            return find(pai[i]);  // Sem compressão
        }
        return i;
    }
    
    @Override
    public void union(int i, int j) {
        int raizI = find(i);
        int raizJ = find(j);
        
        if (raizI != raizJ) {
            // Union by Rank: anexa a menor à maior
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
```

**Complexidade:** O(log n) para find e union.

### 3. DSUTarjan (Rank + Path Compression)

**Características:** Union by Rank + Path Compression

```java
public class DSUTarjan extends DSU {
    
    public DSUTarjan() { super(); }
    public DSUTarjan(int n) { super(n); }
    
    @Override
    public int find(int i) {
        validarIndice(i);
        if (pai[i] != i) {
            pai[i] = find(pai[i]);  // Path Compression!
        }
        return pai[i];
    }
    
    @Override
    public void union(int i, int j) {
        int raizI = find(i);
        int raizJ = find(j);
        
        if (raizI != raizJ) {
            // Union by Rank
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
```