import java.util.Comparator;

public class Main {
    public static void main(String[] args) throws InvalidNoException {
        ArvoreBinariaPesquisa abp = new ArvoreBinariaPesquisa(1);
        No a = abp.addChave(2);
        No b = abp.addChave(3);
        No c = abp.addChave(4);
        abp.posOrdem(abp.getRaiz());
        abp.mostrarElementos();
        abp.mostrarNos();

    }

}
