import java.util.ArrayList;
import java.util.Comparator;

public class ArvoreAVL {
    // ordenada, esquerda são as menores e direita são as maiores
    No raiz;
    int tamanho;
    No filho;

    private Comparator<Object> comparadorDechaves;

    public ArvoreAVL(Object chave){
        this.comparadorDechaves = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                // para evitar erro de nó sendo passado
                if (o1 instanceof No) {
                    o1 = ((No) o1).getChave();
                }
                if (o2 instanceof No) {
                    o2 = ((No) o2).getChave();
                }
                int chave1 = (int) o1;
                int chave2 = (int) o2;
                if(chave1 < chave2){
                    return -1;
                }
                if(chave1 > chave2){
                    return 1;
                }
                else{
                    return 0;
                }
            }
        };
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

    public int altura(No node) {
        int alturaEsquerda = 0;
        int alturaDireita = 0;
        if(
                ehExterno(node)){
            return 0;
        }
        if(node.getFilhoEsquerdo() != null){
            alturaEsquerda = altura(node.getFilhoEsquerdo()) +1;
        }
        if(node.getFilhoDireito() != null){
            alturaDireita = altura(node.getFilhoDireito()) +1;
        }
        return Math.max(alturaEsquerda, alturaDireita);
    }


    public boolean ehExterno(No node){
        // para ser externo/folha ele não pode ter nenhum filho
        return node.getFilhoDireito() == null && node.getFilhoEsquerdo() == null;
    }

    public boolean ehInterno(No node){
        // para ser interno ele deve possuir pelo menos um filho
        return node.getFilhoDireito() != null || node.getFilhoEsquerdo() != null;
    }

    public No pesquisar(No node, Object chave){
        if(ehExterno(node)){
            return node;
        }
        // passar pelo método de comparação onde pode retornar (1,-1 ou 0)
        int comparacao = compareChaves(node.getChave(),chave);
        if (comparacao > 0){
            if(node.getFilhoEsquerdo() !=null){
                return pesquisar(node.getFilhoEsquerdo(), chave);
            }
            else{
                return node;
            }
            // pesquisar recursivamente passando o filho esquerdo do nó e a chave como parâmetro
        } else if (comparacao == 0) {
            return node;
            // se a comparação der igual, retorna o nó passado inicialmente no parâmetro
        }
        else {
            if(node.getFilhoDireito() !=null){
                return pesquisar(node.getFilhoDireito(), chave);
            }
            else{
                return node;
            }
        }
    }

    public No addChave(Object chave) throws InvalidNoException {
        No noApesquisar = pesquisar(raiz, chave);
        // pesquisa para saber onde inserir o novo nó
        if (ehExterno(noApesquisar)) {
            // se o nó pesquisado for externo (folha) adicionamos o novo nó como filho
            No novoNo = new No(chave);
            novoNo.setPai(noApesquisar);
            int comparacao = compareChaves(chave, noApesquisar.getChave());
            if (comparacao < 0) {
                // Se a primeira chave é menor que a segunda vai setar o novo nó como filho esquerdo
                noApesquisar.setFilhoEsquerdo(novoNo);
            } else {
                noApesquisar.setFilhoDireito(novoNo);
            }
            tamanho++;
            // atualizar os fatores de balanceamento
            atualizaFBInsercao(novoNo);
            // verificar se precisa fazer rotação
            return novoNo;
        } else if (noApesquisar.getFilhoEsquerdo() == null || noApesquisar.getFilhoDireito() == null) {
            // se o nó pesquisado é interno e tem pelo menos um filho vazio, adicionamos o novo nó nesse filho vazio
            No novoNo = new No(chave);
            novoNo.setPai(noApesquisar);
            if (noApesquisar.getFilhoEsquerdo() == null) {
                noApesquisar.setFilhoEsquerdo(novoNo);
            } else {
                noApesquisar.setFilhoDireito(novoNo);
            }
            tamanho++;
            // atualizar os fatores de balanceamento
            atualizaFBInsercao(novoNo);
            // verificar se precisa fazer rotação
            return novoNo;
        } else {
            System.out.println("A chave já foi adicionada na árvore");
            return null;
        }
    }

    // método de atualizar o fator de balanceamento depois de uma inserção
    private void atualizaFBInsercao(No node) throws InvalidNoException {
        No atual = node;
        boolean pare = false;
        while(!pare && atual.getPai() !=null){
            No pai = atual.getPai();
            int comparacao = compareChaves(atual.getChave(), pai.getChave());
            if(comparacao < 0){ // pai é maior
                pai.setFB((pai.getFB()) + 1);
            }
            if(comparacao > 0){ // atual maior
                pai.setFB(pai.getFB()+(-1));
            }
            checaRotacao(atual.getPai());
//           pai = atual.getPai();
            if(atual.getPai() == null ){
                pare = true;
            }
            if(pai.getFB() == 00){
                pare = true;
            }
            atual = atual.getPai();
        }
    }

    private void checaRotacao(No novoNo) throws InvalidNoException {
        if(novoNo != null){
            if (novoNo.getFB() > 1 && (novoNo.getFilhoEsquerdo()).getFB() >= 0) {
                    rotacaoSimplesDireita(novoNo);
                    // atualizar fb
            }
            // checar se é uma rotação dupla para a direita (sinal do avô(2) e do pai são diferentes)
            else if (novoNo.getFB() > 1 && (novoNo.getFilhoEsquerdo()).getFB() < 0) {
                    // primeiro uma rotação para esquerda
                    rotacaoSimplesEsquerda(novoNo.getFilhoEsquerdo());
                    // depois uma para direita
                    rotacaoSimplesDireita(novoNo);
            }
            // ROTAÇÃO ESQUERDA
            // checar se é uma rotação simples para a esquerda (sinal do avô(-2) e do pai são iguais)
            else if (novoNo.getFB() < -1 && (novoNo.getFilhoDireito()).getFB() <= 0) {
                rotacaoSimplesEsquerda(novoNo);
            } else if (novoNo.getFB() < -1 && (novoNo.getFilhoDireito()).getFB() > 0) {
                // primeiro uma rotação para a direita
                rotacaoSimplesDireita(novoNo.getFilhoDireito());
                // depois uma rotação a esquerda
                rotacaoSimplesEsquerda(novoNo);
            }
        }
    }

    private No rotacaoSimplesDireita(No node) throws InvalidNoException {
        No antigoPaidoNode = node.getPai();
        // colisão
        No filhoDireito = null;
        No filhoEsquerdo = node.getFilhoEsquerdo();
        if((node.getFilhoEsquerdo()).getFilhoDireito() != null){
            filhoDireito = (node.getFilhoEsquerdo()).getFilhoDireito();
            filhoDireito.setPai(node);
        }
        node.setFilhoEsquerdo(filhoDireito);
        filhoEsquerdo.setFilhoDireito(node);
        filhoEsquerdo.setPai(antigoPaidoNode);
        if(node.getPai() != null){
            if(ehFilhoEsquerdo(node)){
                (node.getPai()).setFilhoEsquerdo(filhoEsquerdo);
            }else{
                (node.getPai()).setFilhoDireito(filhoEsquerdo);
            }
        }else{
            filhoEsquerdo.setPai(null);
            raiz = filhoEsquerdo;
        }
        node.setPai(filhoEsquerdo);
//        FB_B_novo= FB_B - 1 - max(FB_A, 0);
//        FB_A_novo= FB_A - 1 + min(FB_B_novo, 0);
        int novoFbNode = node.getFB() - 1 - Math.max(filhoEsquerdo.getFB(),0);
        int novoFbNovoPai = filhoEsquerdo.getFB() - 1 + Math.min(novoFbNode,0);
        node.setFB(novoFbNode);
        filhoEsquerdo.setFB(novoFbNovoPai);
        return node;
    }

    private No rotacaoSimplesEsquerda(No node) throws InvalidNoException {
        No antigoPaidoNode = node.getPai();
        // para caso haja 'colisão' dos nós ao lado esquerdo
        No filhoEsquerdo = null;
        No filhoDireito = node.getFilhoDireito();
        if((node.getFilhoDireito()).getFilhoEsquerdo() != null) {
            filhoEsquerdo = (node.getFilhoDireito()).getFilhoEsquerdo();
            // não precisa de comparação pq o node sempre vai ser maior que o filho esquerdo (lógica da árvore)
            filhoEsquerdo.setPai(node);
        }
        node.setFilhoDireito(filhoEsquerdo);
        filhoDireito.setFilhoEsquerdo(node);
        filhoDireito.setPai(antigoPaidoNode);
        if(node.getPai() != null) {
            if (ehFilhoEsquerdo(node)) {
                (node.getPai()).setFilhoEsquerdo(filhoDireito);
            } else {
                // se for filho direito
                (node.getPai()).setFilhoDireito(filhoDireito);
            }
        }else{
            filhoDireito.setPai(null);
            raiz = filhoDireito;
        }
        node.setPai(filhoDireito);

//        NewBalB = B.FB + 1 - Math.min(A.FB, 0);
//        NewBalA = A.FB + 1 + Math.max(NewBalB, 0);
        int novoFbNode = node.getFB() + 1 - Math.min(filhoDireito.getFB(), 0);
        int novoFbNovoPai = filhoDireito.getFB() + 1 + Math.max(novoFbNode,0);
        node.setFB(novoFbNode);
        filhoDireito.setFB(novoFbNovoPai);
        return node;
    }


    public Object removeChave(Object chave) throws InvalidNoException {
        // se o nó for folha
        No node = pesquisar(raiz, chave);
        No noGuardado = node;
        if(node == null){
            throw new InvalidNoException("Não foi possível encontrar essa chave na árvore");
        }
        if(ehExterno(node)){
            if(node == raiz && node.getFilhoEsquerdo() == null && node.getFilhoDireito() == null){
                Object temp = node.getChave();
                node.setChave(null);
                atualizaFBRemocao(noGuardado);
                return temp;
            }
            // se é folha (não tem filhos)
            Object temp = node.getChave();
            // guarda o elemento da chave para poder retornar
            if(ehFilhoDireito(node)){
                node.getPai().setFilhoDireito(null);
                // setar o nó como null
            }else{
                node.getPai().setFilhoEsquerdo(null);
            }
            atualizaFBRemocao(noGuardado);
            return temp;
        }
        // se o nó tem apenas um filho
        if(temUmFilho(node)){
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
            atualizaFBRemocao(noGuardado);
            checaRotacao(noGuardado);
            return node.getChave();
        }
        if(temDoisFilhos(node)){
            Object temp = node.getChave();
            No sucessor = sucessor(node.getFilhoDireito());
            removeChave(sucessor);
            node.setChave(sucessor.getChave());
            atualizaFBRemocao(noGuardado);
            checaRotacao(noGuardado);
            return temp;
        }
        return null;
    }

    // método de atualizar o fator de balanceamento depois de uma remoção
    private void atualizaFBRemocao(No node) throws InvalidNoException {
       No atual = node;
       boolean pare = false;
       while(!pare && atual.getPai() != null){
           No pai = atual.getPai();
           int comparacao = compareChaves(atual.getChave(), pai.getChave());
           if(comparacao < 0){ // pai é maior
               pai.setFB(pai.getFB()+ (-1));
           }
           if(comparacao > 0 ){ // atual é maior
               pai.setFB(pai.getFB() + 1);
           }
           checaRotacao(atual);
           if(atual == raiz){
               pare = true;
           }
           if(pai.getFB() == 00){
               pare = true;
           }
           atual = atual.getPai();
       }
    }

    private No sucessor(No node){
        if(node.getFilhoEsquerdo() == null){
            return node;
        }
        else{
            return sucessor(node.getFilhoEsquerdo());
        }
    }


    ArrayList<No> a = new ArrayList<>();
    // array para armazenar os nós em ordem

    public void emOrdemP(No node){
        // filho esquerdo, nó pai e depois filho direito
        if(node.getFilhoEsquerdo() != null) {
            // filho esquerdo
            emOrdemP(node.getFilhoEsquerdo());
        }
        // nó pai
        a.add(node);
        // filho direito
        if(node.getFilhoDireito() != null){
            emOrdemP(node.getFilhoDireito());
        }
    }

    public void mostrarArvore(){
        a.clear(); // limpando a array para ser possível imprimir novamente
        Object[][] m = new Object[altura(raiz)+1][size()];
        emOrdemP(raiz);
        for(int i=0; i < a.size(); i++ ){
            m[profundidade(a.get(i))] [i]=a.get(i).getChave() + "[" + a.get(i).getFB() + "]";
        }

        for(int l = 0; l < altura(raiz)+1; l++){
            for(int c = 0; c < a.size(); c++){
                System.out.print(m[l][c]==null?"\t":m[l][c]+"\t");
            }
            System.out.println();
        }
    }

    public boolean temUmFilho(No node){
        if(node.getFilhoEsquerdo() != null && node.getFilhoDireito() == null){
            // se o filho esquerdo não for nulo e filho direito for nulo
            return true;
        }
        if(node.getFilhoEsquerdo() == null && node.getFilhoDireito() != null){
            // se o filho esquerdo for nulo e filho direito não for nulo
            return true;
        }
        else{
            return false;
            // se tiver mais de um filho
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

    public boolean temFilhoEsquerdo(No node){
        System.out.println("Nó '"+ node.getChave() + "' tem filho esquerdo? ");
        return node.getFilhoEsquerdo()!= null;
    }

    public boolean temFilhoDireito(No node){
        System.out.println("Nó '"+ node.getChave() + "' tem filho direito? ");
        return node.getFilhoDireito()!= null;
    }

    public int profundidade(No node){
//        if (node == raiz)
        if (node.getPai() == null) {
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
