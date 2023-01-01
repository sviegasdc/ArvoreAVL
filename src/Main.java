import java.util.Comparator;

public class Main {
    public static void main(String[] args) throws InvalidNoException {
        ArvoreBinariaPesquisa abp = new ArvoreBinariaPesquisa(1);
        No a = abp.addChave(2);
        No b = abp.addChave(10);
        No c = abp.addChave(7);
        System.out.println(abp.temFilhoDireito(a));
        abp.mostrarElementos();
        abp.mostrarNos();

    }
}
