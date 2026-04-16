# Documentação das Classes Aresta e Grafo

## Conceitos Fundamentais

### O que é uma Aresta?

Em teoria dos grafos, uma **aresta** é o elemento que conecta dois vértices (nós) em um grafo. No contexto deste trabalho, trabalhamos com:

- **Grafos não-direcionados**: A aresta pode ser percorrida em ambas as direções
- **Grafos ponderados**: Cada aresta possui um peso (custo) associado
- **Arestas simples**: Apenas uma aresta conectando o mesmo par de vértices

**Representação matemática:**  
Uma aresta `e` é um par ordenado `(u, v, w)` onde:
- `u`: vértice de origem
- `v`: vértice de destino  
- `w`: peso da aresta (valor numérico não-negativo)

**Exemplo:** A aresta `(2, 5, 10)` conecta os vértices 2 e 5 com custo 10.

### O que é um Grafo?

Um **grafo** é uma estrutura de dados composta por:
- Um conjunto finito de **vértices** (nós)
- Um conjunto de **arestas** que conectam pares de vértices

**No nosso contexto:**
- Trabalhamos com **grafos não-direcionados ponderados**
- Vértices são numerados de `0` a `n-1` (0-indexado)
- O grafo pode ser **conexo** ou **desconexo** (para testes comparativos)

**Exemplo visual:**
```
Grafo com 4 vértices:

    0 ------10------ 1
    |                |
    6                15
    |                |
    2 ------4------- 3

Vértices: 0, 1, 2, 3
Arestas: (0,1,10), (0,2,6), (2,3,4), (1,3,15)
```

---

## Classe Aresta

### Propósito

Representar uma conexão entre dois vértices com um peso associado. A classe é projetada para ser **imutável** e **comparável**, características essenciais para o algoritmo de Kruskal.

### Estrutura

```java
public class Aresta implements Comparable<Aresta> {
    private int origem;   // vértice de origem
    private int destino;  // vértice de destino
    private int peso;     // peso da aresta
}
```

### Métodos Principais

| Método | Retorno | Descrição |
|--------|---------|-----------|
| `getOrigem()` | `int` | Retorna o vértice de origem |
| `getDestino()` | `int` | Retorna o vértice de destino |
| `getPeso()` | `int` | Retorna o peso da aresta |
| `compareTo(Aresta outra)` | `int` | Compara duas arestas pelo peso (para ordenação) |
| `toString()` | `String` | Representação textual da aresta |

### Exemplo de Uso

```java
// Criando uma aresta
Aresta a = new Aresta(2, 5, 10);

// Acessando os valores
int u = a.getOrigem();    // 2
int v = a.getDestino();   // 5
int w = a.getPeso();      // 10

// Ordenando arestas por peso
List<Aresta> arestas = Arrays.asList(
    new Aresta(0, 1, 15),
    new Aresta(0, 2, 5),
    new Aresta(1, 2, 10)
);
Collections.sort(arestas);
// Resultado: [(0,2,5), (1,2,10), (0,1,15)]

// Impressão
System.out.println(a);  // (2 -> 5, peso: 10)
```

### Por que implementar `Comparable`?

O algoritmo de Kruskal precisa processar as arestas em **ordem crescente de peso**. A implementação de `Comparable` permite usar `Collections.sort()` diretamente, garantindo:

- **Eficiência**: Ordenação O(m log m)
- **Simplicidade**: Código limpo e legível
- **Corretude**: Ordenação consistente e previsível


## Classe Grafo

### Propósito

Agrupar um conjunto de vértices e arestas, fornecendo métodos para:
- Adicionar arestas ao grafo
- Acessar arestas ordenadas por peso
- Validar a integridade dos dados

### Estrutura

```java
public class Grafo {
    private final int numVertices;    // quantidade de vértices
    private final List<Aresta> arestas;  // lista de arestas
}
```

### Por que `List<Aresta>` e não `ArrayList<Aresta>`?

- **Flexibilidade**: `ArrayList` é a implementação concreta, mas o tipo declarado é `List`
- **Facilidade**: Permite usar `Collections.sort()` sem acoplamento

### Métodos Principais

| Método | Retorno | Descrição |
|--------|---------|-----------|
| `Grafo(int numVertices)` | `void` | Construtor (cria grafo vazio) |
| `adicionarAresta(int u, int v, int w)` | `void` | Adiciona aresta ao grafo com validação |
| `getNumVertices()` | `int` | Retorna quantidade de vértices |
| `getNumArestas()` | `int` | Retorna quantidade de arestas |
| `getArestas()` | `List<Aresta>` | Retorna lista original (não ordenada) |
| `getArestasOrdenadas()` | `List<Aresta>` | Retorna **cópia** ordenada por peso |
| `toString()` | `String` | Representação textual do grafo |

### Exemplo de Uso

```java
// Criando um grafo com 5 vértices
Grafo grafo = new Grafo(5);

// Adicionando arestas
grafo.adicionarAresta(0, 1, 4);
grafo.adicionarAresta(0, 2, 3);
grafo.adicionarAresta(1, 2, 1);
grafo.adicionarAresta(1, 3, 2);
grafo.adicionarAresta(2, 3, 5);
grafo.adicionarAresta(3, 4, 6);

// Informações básicas
System.out.println("Vértices: " + grafo.getNumVertices());  // 5
System.out.println("Arestas: " + grafo.getNumArestas());    // 6

// Obtendo arestas ordenadas para Kruskal
List<Aresta> ordenadas = grafo.getArestasOrdenadas();
for (Aresta a : ordenadas) {
    System.out.println(a);
}
// Saída ordenada por peso:
// (1 -> 2, peso: 1)
// (1 -> 3, peso: 2)
// (0 -> 2, peso: 3)
// (0 -> 1, peso: 4)
// (2 -> 3, peso: 5)
// (3 -> 4, peso: 6)

// Impressão completa do grafo
System.out.println(grafo);