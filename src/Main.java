public class Main {
    public static void main(String[] args) throws InvalidNoException {
        ArvoreAVL abp = new ArvoreAVL(1);
        No a = abp.addChave(3);
        No b = abp.addChave(0);
        abp.mostrarArvore();
        System.out.println("---------------------");

    }
}
