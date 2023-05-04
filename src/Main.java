public class Main {
    public static void main(String[] args) throws InvalidNoException {
        ArvoreAVL abp = new ArvoreAVL(10);
        No u = abp.addChave(9);
        abp.mostrarArvore();
        System.out.println("---------------------");
        No bu = abp.addChave(6);
        abp.mostrarArvore();
        System.out.println("---------------------");
        No bu3 = abp.addChave(4);
        abp.mostrarArvore();
        System.out.println("---------------------");
        abp.removeChave(9);
        abp.mostrarArvore();
        System.out.println("---------------------");
//        No buqw = abp.addChave(6);
//        abp.mostrarArvore();
//        System.out.println("---------------------");
    }
}
