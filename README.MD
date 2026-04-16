
---

## Descrição dos Arquivos

### `dados/entradas/`
Armazena grafos gerados aleatoriamente (formato: `n` vértices, lista de arestas `u v peso`).  
Exemplo de nome: `grafo_n1000_m20000.txt`

### `dados/resultados/`
Guarda arquivos `.csv` com métricas coletadas:  
- tempo total de execução  
- número de operações `find` e `union`  
- número de acessos a ponteiros/arrays (memória)  

### `docs/`
- **artigo-sbc.pdf** – relatório final seguindo o template da SBC.  
- **ESTRUTURA_MODULARIZACAO.md** – detalha como os pacotes foram organizados e por quê.  
- **JUSTIFICATIVA_ESTRUTURA.md** – defesa das decisões de design (ex: interface DSU, separação de métricas).

### `src/algoritmos/AlgoritmoKruskal.java`
Implementa o algoritmo de Kruskal, recebendo uma implementação de DSU por injeção de dependência.  
Retorna o custo da MST e coleta métricas via `ColetorMetricas`.

### `src/modelos/`
- **Aresta.java** – `int u, v, peso` + `Comparable` para ordenação.  
- **Grafo.java** – lista de arestas + número de vértices.

### `src/nucleo/`
- **DSU.java** – interface com `find(i)`, `union(i, j)`, `reset(n)`.  
- **DSUIngenuo.java** – array `pai[]`, `find` recursivo/iterativo sem compressão, `union` sem rank.  
- **DSURank.java** – array `rank[]`, `union` pelo maior rank.  
- **DSUTarjan.java** – `find` com compressão de caminho (iterativa ou recursiva) + union by rank.

### `src/utilitarios/`
- **ColetorMetricas.java** – encapsula `System.nanoTime()` e contadores de acesso. Permite iniciar/parar medição e gerar relatório.  
- **GeradorDeGrafos.java** – gera grafos conexos ou desconexos com densidade controlada.

### `src/main.java`
Loop principal que:
1. Define parâmetros (\(n\), \(m\), densidade).  
2. Gera ou carrega grafos.  
3. Para cada variante DSU (Ingênuo, Rank, Tarjan), executa Kruskal e coleta métricas.  
4. Salva os resultados em CSV.