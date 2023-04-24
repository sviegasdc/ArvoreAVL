public class Main {
    public static void main(String[] args) throws InvalidNoException {
        ArvoreAVL abp = new ArvoreAVL(10);
        No a = abp.addChave(11);
        No b = abp.addChave(8);
        No c = abp.addChave(7);
        abp.mostrarArvore();
        System.out.println("---------------------");
    }
}
