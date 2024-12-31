public class Main {
    public static void main(String[] args) {
        Interface i=new Interface();
        i.affiche_interface();
        int partie=1;
        Jeu j=new Jeu(i);
        j.play();
    }
}
