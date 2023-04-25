public class Main {
    public static void main(String[] args) throws InvalidNoException {
        ArvoreAVL abp = new ArvoreAVL(10);
        No a = abp.addChave(8);
        abp.mostrarArvore();
        System.out.println("---------------------");
        No b = abp.addChave(7);
        abp.mostrarArvore();
        System.out.println("---------------------");
        No c = abp.addChave(12);
        abp.mostrarArvore();
        System.out.println("---------------------");
        No cu = abp.addChave(11);
        abp.mostrarArvore();
        System.out.println("---------------------");
    }
}
