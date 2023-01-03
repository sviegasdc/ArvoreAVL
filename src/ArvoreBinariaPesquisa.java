import java.util.ArrayList;
import java.util.Comparator;

public class ArvoreBinariaPesquisa {
    // pegar tudo da árvore simples porem remoção mais dificil
    // inserção com casos diferentes
    // ordenada, esquerda são as menores e direita são as maiores

    No raiz;
    int tamanho;

    private ArrayList<No> ArrayNos;

    private Comparator<Object> comparadorDechaves;

    public ArvoreBinariaPesquisa(Object chave){
        this.comparadorDechaves = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };
        this.ArrayNos = new ArrayList<No>();
        raiz = new No(chave);
        // já inicializa os filhos como nós externos
        tamanho = 1;
        // então não é necessário setar os filhos como "null"
        // raiz.setFilhoEsquerdo(null); raiz.setFilhoDireito(null);
    }


        public int compareChaves (Object chave1, Object chave2){
        return comparadorDechaves.compare(chave1, chave2);
        // se a primeira chave é menor que a segunda, o método retorna um número negativo,"-1"
        // se a primeira chave é igual à segunda, o método retorna "0"
        // se a primeira chave é maior que a segunda, o método retorna um número positivo,"1"
    }


    public No pesquisar(No node, Object chave){
        if(ehExterno(node)){
            return node;
        }
        // passar pelo método de comparação onde pode retornar (1,-1 ou 0)
        int comparacao = compareChaves(chave, node.getChave());
        if (comparacao < 0){
            return pesquisar(node.getFilhoEsquerdo(), chave);
            // pesquisar recursivamente passando o filho esquerdo do nó e a chave como parâmetro
        } else if (comparacao == 0) {
            return node;
            // se a comparação der igual, retorna o nó passado inicalmente no parãmetro
        }
        else {
            return pesquisar(node.getFilhoDireito(), chave);
            // pesquisar recursivamente passando o filho direito do nó e a chave como parâmetro
        }
    }

    public void listaArvore(No node) {
        ArrayNos.add(node);
        System.out.println(ArrayNos);
    }

    public void emOrdem(No node){
        // filho esquerdo, nó pai e depois filho direito
        if(ehInterno(node)){
            // filho esquerdo
            emOrdem(node.getFilhoEsquerdo());
            // nó pai
            listaArvore(node);
            // filho direito
            emOrdem(node.getFilhoDireito());
        }
    }

    public void preOrdem(No node){
        // nó pai, filho esquerdo e depois filho direito
        if(ehInterno(node)){
            // nó pai
            listaArvore(node);
            // filho esquerdo
            preOrdem(node.getFilhoEsquerdo());
            // filho direito
            preOrdem(node.getFilhoDireito());
        }
    }

    public void posOrdem(No node){
        // filho esquerdo, filho direito e depois nó pai
        if(ehInterno(node)){
            // filho esquerdo
            preOrdem(node.getFilhoEsquerdo());
            // filho direito
            preOrdem(node.getFilhoDireito());
            // nó pai
            listaArvore(node);
        }
    }

    public int altura(No node) {
        if(node == getRaiz()){
            return 0;
        }
        int alturaEsquerda = altura(node.getFilhoEsquerdo());
        int alturaDireita = altura(node.getFilhoDireito());
        return Math.max(alturaEsquerda, alturaDireita);
    }


    public boolean ehExterno(No node){
        // para ser externo/folha ele não pode ter nenhum filho
        return node.getFilhoDireito() == null || node.getFilhoEsquerdo() == null;
    }

    public boolean ehInterno(No node){
        // para ser interno ele deve possuir pelo menos um filho
        return node.getFilhoDireito() != null && node.getFilhoEsquerdo() != null;
    }

    /////////////////////////////

    public void mostrarElementos(){
        System.out.print("[");
        for (No node:ArrayNos) {
            // para cada nó no array,imprima
            System.out.print(node.getChave() + " ");
        }
        System.out.print("]");
        System.out.println("");
    }

    public void mostrarNos(){
        System.out.print("[");
        for (No node:ArrayNos) {
            // para cada nó no array,imprima
            System.out.print(node + " ");
        }
        System.out.print("]");
        System.out.println("");
    }


    public No addChave(Object chave) {
        No noApesquisar = pesquisar(raiz, chave);
        // pesquisa para saber onde inserir o novo nó
        if(ehExterno(noApesquisar)){
            // se o nó pesquisado for externo(folha) adicionamos o novo nó como filho
            // o noApesquisar nunca vai ser interno (nuca vai ter um filho), pois ele sempre é o último nó
            No novoNo = new No(chave);
            // adicionar novo nó no array
            ArrayNos.add(novoNo);
            // criar o novo nó e guardar a chave passada nele
            int comparacao = compareChaves(chave, noApesquisar.getChave());
            // fazendo a comparação aqui porque no 'if' não é possível comparar inteiros
                if( comparacao < 0){
                 // Se a primeira chave é menor que a segunda vai setar o novo nó como filho esquerdo
                noApesquisar.setFilhoEsquerdo(novoNo);
            }else{
                noApesquisar.setFilhoDireito(novoNo);
            }
            tamanho++;
            return novoNo;
        }
        else {
            System.out.println("A chave já foi adicionada na árvore");
            return null;
        }

    }

    public Object removeChave(Object chave) throws InvalidNoException {
        // se o nó for folha
        No node = pesquisar(raiz, chave);
        if(node == null){
            throw new InvalidNoException("Não foi possível encontrar essa chave na árvore");
        }
        if(ehExterno(node)){
            // se é folha (não tem filhos)
           Object temp = node.getChave();
            // guarda o elemento da chave para poder retornar
           if(ehFilhoDireito(node)){
               node.getPai().setFilhoDireito(null);
               // setar o nó como
           }else{
               node.getPai().setFilhoEsquerdo(null);
           }
           return temp;
        }
        // se o nó tem apenas um filho
        if(temUmFilho(node)){
            No filho = new No(chave);
            if(node.getFilhoEsquerdo() != null){
                filho = node.getFilhoEsquerdo();
            }
            else{
                filho = node.getFilhoDireito();
            }
            if(ehFilhoDireito(node)){
                node.getPai().setFilhoDireito(filho);
            }
            else{
                node.getPai().setFilhoEsquerdo(filho);
            }
            filho.setPai(node.getPai());
            Object temp = node.getChave();
            node.setPai(null);
            node.setFilhoEsquerdo(null);
            node.setFilhoDireito(null);
            return temp;

        }
        if(temDoisFilhos(node)){
            Object temp = node.getChave();
            // VER ISSO AQUI
            No sucessor = sucessor(node.getFilhoDireito());
            removeChave(node.getFilhoDireito());
            node.setChave(sucessor.getChave());
            return temp;
        }
        // VER ISSO AQUI
        return node;

    }



    public void mostrarArvore(){

    }

    /////////////////////////////

    public boolean temUmFilho(No node){
        if(node.getFilhoEsquerdo() != null && node.getFilhoDireito() == null){
            // se o filho esquerdo diferir de nulo e filho direiro for nulo
            return true;
        }
        if(node.getFilhoEsquerdo() == null && node.getFilhoDireito() != null){
            // se o filho esquerdo for nulo e filho direiro diferir de nulo
            return true;
        }
        else{
            return false;
            // se tiver mais de um filho ou tiver dois filhos
        }
    }
    public boolean temDoisFilhos(No node){
        if(node.getFilhoEsquerdo() != null && node.getFilhoDireito() != null){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean ehFilhoDireito(No node) {
        // pegar o pai desse nó
        No paiDoNo = node.getPai();
        // checar se é nulo
        if (paiDoNo == null) {
            return false;
        }
        // se for o filho direito = true
        if (paiDoNo.getFilhoDireito() == node) {
            return true;
        }
        return false;
    }

    public boolean ehFilhoEsquerdo(No node) {
        // pegar o pai desse nó
        No paiDoNo = node.getPai();
        // checar se é nulo
        if (paiDoNo == null) {
            return false;
        }
        // se for o filho esquerdo = true
        if (paiDoNo.getFilhoEsquerdo() == node) {
            return true;
        }
        return false;
    }

    public No getRaiz(){
        // retornar raiz da arvore
        return raiz;
    }
     public No setRaiz(No node){
        return raiz;
     }

     public boolean temFilhoEquerdo(No node){
        System.out.println("Nó '"+ node + "' tem filho esquerdo? ");
        return node.getFilhoEsquerdo()!= null;

     }

    public boolean temFilhoDireito(No node){
        System.out.println("Nó '"+ node + "' tem filho direito? ");
        return node.getFilhoDireito()!= null;
    }


    public int profundidade(No node){
        if (node == raiz) {
            return 0;
        }
        else{
            return 1 + profundidade(node.getPai());
        }
    }

    public int size(){
        return tamanho;
    }

    public boolean isEmpty(){
        return tamanho == 0;
    }


}
