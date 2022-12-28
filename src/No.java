public class No {
    private Object chave;

    private No pai;

    private No filhoEsquerdo;

    private No filhoDireito;


    public No(Object newChave) {
        this.chave = newChave;
    }

    public void setPai(No pai) {
        this.pai = pai;
    }
    public No getPai(){
        return pai;
    }

    public Object getChave(){
        return chave;
    }
    public void setChave(Object chave) {
        this.chave = chave;
    }

    public void setFilhoEsquerdo(No filhoEsquerdo){
        this.filhoEsquerdo = filhoEsquerdo;
    }

    public No getFilhoEsquerdo(){
        return filhoEsquerdo;
    }

    public No getFilhoDireito(){
        return filhoDireito;
    }

}