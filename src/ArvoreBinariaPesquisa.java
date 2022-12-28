import java.util.ArrayList;

public class ArvoreBinariaPesquisa {
    // pegar tudo da árvore simples porem remoção mais dificil
    // inserção com casos diferentes
    // ordenada, esquerda são as menores e direita são as maiores

    No raiz;
    int tamanho;

    private ArrayList<Object> ArrayNos;

    public ArvoreBinariaPesquisa(Object chave){
        raiz = new No(chave);
        tamanho = 1;
    }



    public No addChave() {
        // "incluir" do professor
        return null;
    }

    public void removeChave() {
    }


    public No pesquisar(No node, Object chave){
        if(ehExterno(node)){
            return node;
        }
        return null;
    }
    //    Algoritmo pesquise(v,k)
//    se v.isExternal ()
//    retorne v
//    se k < v.getChave()
//    retorne pesquise(v.filhoEsquerdo(),k)
//    senão se k = v.getChave()
//    retorne v
//    senão { k > v.getChave() }
//    retorne pesquise(v.filhoDireito(),k)
    void emOrdem(No node){

    }

    void preOrdem(No node){

    }

    void posOrdem(No node){

    }

    public int altura() {
        return 0;
    }

    public void mostrarArvore(){

    }

    public void mostrarElementos(){
        // public Iterator<Object>
    }

    public void mostrarNos(){
        // public Iterator<Object>
    }

    public boolean ehExterno(No node){
        // para ser externo/folha ele não pode ter nenhum filho
        boolean checagem = true;
        if(node.getFilhoDireito() != null && node.getFilhoEsquerdo() != null){
            checagem = false;
        }
        return checagem;
    }

    public boolean ehInterno(No node){
        // para ser interno ele deve possuir pelo menos um filho
        boolean checagem = true;
        if(node.getFilhoDireito() == null || node.getFilhoEsquerdo() == null){
            checagem = false;
        }
        return checagem;
    }

    /////////////////////////////

    public No getRaiz(){
        // retornar raiz da arvore
        return raiz;
    }
     public No setRaiz(No node){
        return this.raiz = raiz;
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
