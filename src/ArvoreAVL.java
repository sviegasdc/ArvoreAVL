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
            checaRotacao(novoNo);
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
            checaRotacao(novoNo);
            return novoNo;
        } else {
            System.out.println("A chave já foi adicionada na árvore");
            return null;
        }
    }

    private void checaRotacao(No novoNo) throws InvalidNoException {
        No pai = novoNo.getPai();
        // evitando o NullPointerException
        if(pai.getPai() != null){
            No avo = pai.getPai();
            // ROTAÇÃO DIREITA
            // checar se é uma rotação simples para a direita (sinal do avô(2) e do pai são iguais)
            if(avo.getFB() > 1 && pai.getFB() >= 0 ){
                rotacaoSimplesDireita(pai);
                atualizaFBRotacaoDireita(novoNo.getPai());
                // atualizar fb
            }
            // checar se é uma rotação dupla para a direita (sinal do avô(2) e do pai são diferentes)
            else if (avo.getFB() > 1 && pai.getFB() < 0) {
                // primeiro uma rotação para esquerda
                rotacaoSimplesEsquerda(pai);
                atualizaFBRotacaoEsquerda(novoNo.getPai());
                // depois uma para direita
                rotacaoSimplesDireita(novoNo.getPai());
                atualizaFBRotacaoDireita(novoNo);
            }
            // ROTAÇÃO ESQUERDA
            // checar se é uma rotação simples para a esquerda (sinal do avô(-2) e do pai são iguais)
            else if (avo.getFB() < -1 && pai.getFB() <= 0) {
                rotacaoSimplesEsquerda(pai);
                atualizaFBRotacaoEsquerda(novoNo.getPai());
            } else if (avo.getFB() < -1 && pai.getFB() > 0) {
                // primeiro uma rotação para a direita
                rotacaoSimplesDireita(pai);
                atualizaFBRotacaoDireita(novoNo.getPai());
                // depois uma rotação a esquerda
                rotacaoSimplesEsquerda(novoNo.getPai());
                atualizaFBRotacaoEsquerda(novoNo);
            }
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
               // setar o nó como null
           }else{
               node.getPai().setFilhoEsquerdo(null);
           }
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
            return node.getChave();
        }
        if(temDoisFilhos(node)){
            Object temp = node.getChave();
            No sucessor = sucessor(node.getFilhoDireito());
            removeChave(sucessor);
            node.setChave(sucessor.getChave());
            return temp;
        }
        return null;
    }

    private No sucessor(No node){
        if(node.getFilhoEsquerdo() == null){
            return node;
        }
        else{
            return sucessor(node.getFilhoEsquerdo());
        }
    }

    //checar rotações
    // if Pai = 2 filhoE >= 0 (positivo) Simples Direita else dupla Direita (primeiro a esquerda e depois a direita)
    // if Pai = -2 filhoD <= 0 (negativo) Simples Esquerda else dupla Esquerda (primeiro a direita e depois a esquerda)
    // se rotação dupla nada mais é que duas simples pq não chamar as simples em ordem? (testar)

    private No rotacaoSimplesDireita(No node) throws InvalidNoException {
        No novoPai = node.getFilhoEsquerdo();
        No filhoDireitoDoFilhoEsquerdo;
        int comparacao;
        // para caso haja 'colisão' dos nós do lado direito
        if(novoPai.getFilhoDireito() != null ){
            filhoDireitoDoFilhoEsquerdo = novoPai.getFilhoDireito();
            // comparação para ver se o node pega o lugar do filho ou do neto do novoPai
            comparacao = compareChaves((novoPai.getFilhoDireito()).getChave(), node.getChave());
            if(comparacao < 0 ){
                filhoDireitoDoFilhoEsquerdo.setFilhoDireito(node);
                node.setPai(filhoDireitoDoFilhoEsquerdo);
            }
            if(comparacao > 0){
                node.setFilhoDireito(filhoDireitoDoFilhoEsquerdo);
                filhoDireitoDoFilhoEsquerdo.setPai(node);
            }
            else{
                // isso provavelmente nunca vai acontecer mais é melhor prevenir
                throw new InvalidNoException("As chaves são iguais");
            }
        }
        // caso não haja essa 'colisão'
        else{
            // fazendo as novas ligações
            (node.getPai()).setFilhoDireito(novoPai);
            novoPai.setPai(node.getPai());
            novoPai.setFilhoDireito(node);
            node.setPai(novoPai);
            // tirando o filho do nó passado
            node.setFilhoEsquerdo(null);
        }
        return node;
    }

    private No rotacaoSimplesEsquerda(No node) throws InvalidNoException {
        No novoPai = node.getFilhoDireito();
        // para caso haja 'colisão' dos nós ao lado esquerdo
        No filhoEsquerdoDoFilhoEsquerdo;
        int comparacao;
        if(novoPai.getFilhoEsquerdo() != null){
            filhoEsquerdoDoFilhoEsquerdo = novoPai.getFilhoEsquerdo();
            // comparação para ver se o node pega o lugar do filho ou do neto do novoPai
            comparacao = compareChaves(novoPai.getFilhoEsquerdo().getChave(), node.getChave());
            if(comparacao < 0){
                // filho esquerdo menor que node então node vai ser seu pai
                node.setFilhoEsquerdo(filhoEsquerdoDoFilhoEsquerdo);
                filhoEsquerdoDoFilhoEsquerdo.setPai(node);
            }
            if(comparacao > 0){
                // filho esquerdo maior que node então node vai ser seu filho
                filhoEsquerdoDoFilhoEsquerdo.setFilhoEsquerdo(node);
                node.setPai(filhoEsquerdoDoFilhoEsquerdo);
            }
            else{
                // isso provavelmente nunca vai acontecer mais é melhor prevenir
                throw new InvalidNoException("As chaves são iguais");
            }
        }
        // caso não haja 'colisão'
        else {
            // fazendo as novas ligações
            (node.getPai()).setFilhoEsquerdo(novoPai);
            novoPai.setPai(node.getPai());
            node.setPai(novoPai);
            novoPai.setFilhoEsquerdo(node);
            node.setFilhoDireito(null);
        }
        return node;
    }

    // método de atualizar o fator de balanceamento depois de uma inserção
    private void atualizaFBInsercao(No node){
        No pai = node.getPai();
        int alturaDireita;
        int alturaEsquerda;
        if(pai.getFilhoEsquerdo() == null){
            alturaEsquerda = 0;
        }
        else{
            alturaEsquerda = altura(pai.getFilhoEsquerdo());
            if(alturaEsquerda == 0){
                alturaEsquerda = 1;
            }else{
                alturaEsquerda = alturaEsquerda + 1;
            }
        }
        if(pai.getFilhoDireito() == null){
            alturaDireita = 0;
        }
        else{
            alturaDireita = altura(pai.getFilhoDireito());
            if(alturaDireita == 0){
                alturaDireita = 1;
            }else{
                alturaDireita = alturaDireita +1;
            }
        }
        // atualizar o fb
        int fb = alturaEsquerda - alturaDireita;
        pai.setFB(fb);
        // checar se tem outro filho além do que acabou de ser inserido
        if(pai.getFB() !=0 && pai.getPai() != null){
            atualizaFBInsercao(pai);
        }
    }

    // método de atualizar o fator de balanceamento depois de uma remoção
    private void atulizaFBRemocao(No node){
        No pai = node.getPai();
        int alturaDireita;
        int alturaEsquerda;
        // checar se tem outro filho além do que acabou de ser inserido
        if(pai.getFilhoEsquerdo() == null){
            alturaEsquerda = 0;
        }
        else{
            alturaEsquerda = altura(node.getFilhoEsquerdo());
        }
        if(pai.getFilhoDireito() == null){
            alturaDireita = 0;
        }
        else{
            alturaDireita = altura(node.getFilhoDireito());
        }
        // atualizar o fb
        int fb = alturaEsquerda - alturaDireita;
        pai.setFB(fb);
        while (pai!=null){
            if(ehFilhoEsquerdo(node)){
                pai.setFB(pai.getFB() -1);
            }
            // se node for filho direito
            else{
                pai.setFB(pai.getFB() +1);
            }

            // checagem de parada
            if(pai.getFB() != 0){
                break;
            }
            // atualizando variáveis para o while
            node = pai;
            pai = node.getPai();
        }
    }

    // método de atualizar o fator de balanceamento depois da rotação direita
    private void atualizaFBRotacaoEsquerda(No node){
        // trocar o fb do node e de seu filho esquerdo
        No pai = node;
        No filhoEsquerdoDoNode = pai.getFilhoEsquerdo();
        // calculando os novos fatores de balanceamento
        int novoFbPai = pai.getFB() - 1 - Math.max(filhoEsquerdoDoNode.getFB(), 0);
        int novoFbFilho = filhoEsquerdoDoNode.getFB() - 1 + Math.min(novoFbPai, 0);
        // setando esse valores nos nós
        pai.setFB(novoFbPai);
        filhoEsquerdoDoNode.setFB(novoFbFilho);
    }

    // método de atualizar o fator de balanceamento depois da rotação esquerda
    private void atualizaFBRotacaoDireita(No node){
        // trocar o fb do node e de seu filho direito
        No pai = node;
        No filhoDireitoDoNode = node.getFilhoDireito();
        // calculando os novos fatores de balanceamento
        int novoFbPai = pai.getFB() + 1 - Math.min(filhoDireitoDoNode.getFB(), 0);
        int novoFbFilho = filhoDireitoDoNode.getFB() + 1 + Math.max(novoFbPai,0);
        // setando esse valores nos nós
        pai.setFB(novoFbPai);
        filhoDireitoDoNode.setFB(novoFbFilho);
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
