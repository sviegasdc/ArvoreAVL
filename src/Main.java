public class Main {
    public static void main(String[] args) throws InvalidNoException {
        ArvoreAVL abp = new ArvoreAVL(1);
        No u = abp.addChave(2);
        abp.mostrarArvore();
        System.out.println("---------------------");
        No bu = abp.addChave(3);
        abp.mostrarArvore();
        System.out.println("---------------------");
        No bu3 = abp.addChave(4);
        abp.mostrarArvore();
        System.out.println("---------------------");
        No buq = abp.addChave(5);
        abp.mostrarArvore();
        System.out.println("---------------------");
        No buqw = abp.addChave(6);
        abp.mostrarArvore();
        System.out.println("---------------------");
    }
}
