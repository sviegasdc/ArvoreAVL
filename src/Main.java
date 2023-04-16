import java.util.Comparator;

public class Main {
    public static void main(String[] args) throws InvalidNoException {
        ArvoreBinariaPesquisa abp = new ArvoreBinariaPesquisa(1);
        No a = abp.addChave(3);
        No b = abp.addChave(10);
        No c = abp.addChave(7);
        No v = abp.addChave(0);
        No ab = abp.addChave(2);
        abp.mostrarArvore();
        System.out.println("---------------------");
        abp.removeChave(3);
        abp.removeChave(10);
        abp.removeChave(7);
        abp.mostrarArvore();

    }
}
