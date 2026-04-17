## Índice
- [O que é o Algoritmo de Kruskal?](#o-que-é-o-algoritmo-de-kruskal)
- [Conceitos Fundamentais](#conceitos-fundamentais)
- [Lógica do Algoritmo](#lógica-do-algoritmo)
- [Implementação](#Implementação)

---

## O que é o Algoritmo de Kruskal?

O **Algoritmo de Kruskal** é um algoritmo guloso (greedy) utilizado para encontrar a **Árvore Geradora Mínima (MST - Minimum Spanning Tree)** de um grafo ponderado e conexo.

Foi desenvolvido por **Joseph Kruskal** em 1956 e é amplamente utilizado em problemas de otimização de redes, como:
- Conexão de cidades por redes de fibra óptica com custo mínimo
- Projeto de redes elétricas
- Roteamento em redes de computadores
- Agrupamento de dados (clusterização)

### Analogia do Mundo Real

Imagine que você precisa conectar `n` cidades com cabos de fibra óptica. Existem várias rotas possíveis entre as cidades, cada uma com um custo diferente. O objetivo é:

1. **Conectar todas as cidades** (todos os vértices)
2. **Sem criar ciclos** (sem rotas redundantes)
3. **Minimizando o custo total** (soma dos pesos das rotas escolhidas)

O Algoritmo de Kruskal encontra exatamente essa solução ótima!

---

## Conceitos Fundamentais

### O que é uma Árvore Geradora (Spanning Tree)?

Um subconjunto de arestas que:
- Conecta **todos os vértices** do grafo
- Não forma **ciclos**
- Para `n` vértices, contém exatamente `n-1` arestas

### O que é uma Árvore Geradora Mínima (MST)?

É a árvore geradora com a **menor soma possível** dos pesos das arestas.

```
Exemplo: Grafo com 4 vértices

Possível árvore geradora 1:          Possível árvore geradora 2:
   0 ----- 1                            0 ----- 1
   |        |                            \
   |        |                             \
   2 ----- 3                              2 ----- 3
Peso total: 10 + 6 + 4 = 20             Peso total: 5 + 6 + 4 = 15 (MÍNIMA!)
```

---

## Lógica do Algoritmo

O algoritmo segue uma estratégia **gulosa**: sempre escolher a aresta de menor peso que não forma ciclo.

### Passo a Passo

```
1. Ordene todas as arestas em ordem crescente de peso
2. Inicialize cada vértice como um componente separado
3. Para cada aresta (u, v) na ordem ordenada:
      Se u e v estão em componentes diferentes:
          Adicione a aresta à MST
          Una os componentes de u e v
4. Repita até ter (n-1) arestas na MST
```

### Por que funciona?

O algoritmo é guloso porque:
- **Sempre escolhe a aresta de menor peso disponível**
- **Nunca desfaz uma escolha** (uma vez adicionada, a aresta permanece)
- A propriedade do corte (cut property) garante que essa estratégia leva à solução ótima global

---

## Exemplo Visual

### Grafo Inicial

Considere um grafo com 4 vértices e 5 arestas:

```
    0 ------10------ 1
    |                |
    6                15
    |                |
    2 ------4------- 3
```

**Arestas:**
- (0,1) com peso 10
- (0,2) com peso 6
- (0,3) com peso 5
- (1,3) com peso 15
- (2,3) com peso 4

### Execução Passo a Passo

#### Passo 0: Ordenar arestas por peso

| Ordem | Aresta | Peso |
|-------|--------|------|
| 1ª | (2,3) | 4 |
| 2ª | (0,3) | 5 |
| 3ª | (0,2) | 6 |
| 4ª | (0,1) | 10 |
| 5ª | (1,3) | 15 |

#### Passo 1: Processar (2,3) - peso 4

```
Componentes: {0} {1} {2} {3}
2 e 3 estão em componentes diferentes? SIM!
Adicionar (2,3) à MST
Unir componentes {2} e {3} → {2,3}

MST atual: [(2,3,4)]
Custo atual: 4
```

#### Passo 2: Processar (0,3) - peso 5

```
Componentes: {0} {1} {2,3}
0 e 3 estão em componentes diferentes? SIM (0 vs 2,3)
Adicionar (0,3) à MST
Unir {0} e {2,3} → {0,2,3}

MST atual: [(2,3,4), (0,3,5)]
Custo atual: 9
```

#### Passo 3: Processar (0,2) - peso 6

```
Componentes: {0,2,3} {1}
0 e 2 estão em componentes diferentes? NÃO! (ambos em {0,2,3})
Pular (formaria ciclo: 0-3-2-0)

MST atual: [(2,3,4), (0,3,5)]
Custo atual: 9
```

#### Passo 4: Processar (0,1) - peso 10

```
Componentes: {0,2,3} {1}
0 e 1 estão em componentes diferentes? SIM!
Adicionar (0,1) à MST
Unir {0,2,3} e {1} → {0,1,2,3}

MST atual: [(2,3,4), (0,3,5), (0,1,10)]
Custo atual: 19
```

#### Passo 5: Verificar conclusão

```
Arestas na MST: 3
Vértices no grafo: 4
MST completa? SIM (n-1 = 3 arestas)
Parar!
```

### Resultado Final

```
Árvore Geradora Mínima encontrada:

    0 -----10------ 1
    |                
    5                
    |                
    2 ------4------- 3

Arestas selecionadas:
- (2,3) com peso 4
- (0,3) com peso 5
- (0,1) com peso 10

Custo total: 4 + 5 + 10 = 19
```

---

## Implementação

### Estrutura do Código

O código implementa o Algoritmo de Kruskal de forma **modular** e **flexível**, permitindo testar diferentes implementações da estrutura Union-Find (DSU).

```java
package algoritmos;

import java.util.List;
import modelos.Aresta;
import modelos.Grafo;
import nucleo.DSU;

public class AlgoritmoKruskal {
    
    public static int encontrarMST(Grafo grafo, DSU dsu) {
        // 1. Validação de entrada
        if (grafo == null || dsu == null) {
            throw new IllegalArgumentException("Parâmetros não podem ser nulos");
        }
        
        int numVertices = grafo.getNumVertices();
        
        // 2. Caso trivial: grafo com 0 ou 1 vértice
        if (numVertices <= 1) {
            return 0;
        }
        
        // 3. Inicializa o DSU (cada vértice é um componente)
        dsu.reset(numVertices);
        
        // 4. Obtém arestas ordenadas por peso (crescente)
        List<Aresta> arestasOrdenadas = grafo.getArestasOrdenadas();
        
        int custoTotal = 0;
        int arestasNaMST = 0;
        
        // 5. Processa cada aresta na ordem ordenada
        for (Aresta aresta : arestasOrdenadas) {
            int u = aresta.getOrigem();
            int v = aresta.getDestino();
            int peso = aresta.getPeso();
            
            // Verifica se u e v estão em componentes diferentes
            if (dsu.find(u) != dsu.find(v)) {
                // Adiciona aresta à MST
                dsu.union(u, v);
                custoTotal += peso;
                arestasNaMST++;
                
                // Otimização: MST completa (n-1 arestas)
                if (arestasNaMST == numVertices - 1) {
                    break;
                }
            }
        }
        
        // 6. Verifica se o grafo é conexo
        if (arestasNaMST != numVertices - 1) {
            System.err.println("Aviso: Grafo não é conexo!");
        }
        
        return custoTotal;
    }
}
```

### Explicação do Código

| Parte | O que faz | Por que é importante |
|-------|-----------|---------------------|
| **Linha 10** | Método recebe `Grafo` e `DSU` | Permite testar diferentes DSUs sem mudar o algoritmo |
| **Linhas 12-14** | Valida parâmetros | Evita erros em tempo de execução |
| **Linhas 17-19** | Caso trivial | Grafo pequeno não precisa processar |
| **Linha 22** | `dsu.reset()` | Cada vértice começa isolado |
| **Linha 25** | `getArestasOrdenadas()` | Kruskal exige arestas em ordem crescente |
| **Linha 30** | `dsu.find(u) != dsu.find(v)` | Verifica se aresta forma ciclo |
| **Linhas 33-34** | `dsu.union()` + soma peso | Adiciona aresta à MST |
| **Linha 37** | `break` antecipado | Otimização: para quando MST está completa |
| **Linhas 41-43** | Verificação de conexidade | Útil para depuração |

### Por que o DSU é necessário?

O DSU (Union-Find) é a estrutura de dados perfeita para o Kruskal porque:

1. **`find(u)`** - Descobre qual componente `u` pertence em O(α(n))
2. **`union(u, v)`** - Une dois componentes em O(α(n))
3. **Verificação de ciclo** - Se `find(u) == find(v)`, adicionar a aresta formaria ciclo

Sem o DSU, verificar se dois vértices estão conectados exigiria percorrer toda a MST atual a cada iteração, resultando em complexidade O(n²)!