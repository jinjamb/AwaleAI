public class Interface {
    int[] color;
    int[][] holes;
    Interface(){
        color = new int[2];
        holes = new int[16][2];  // Initialisez un tableau 2D de 16 trous, chacun ayant 2 cases

        color[0] = 2;
        color[1] = 2;

        for (int i = 0; i < 16; i++) {
            holes[i] = new int[]{color[0], color[1]}; // Copie des valeurs de color dans chaque trou
        }
    }

    public void affiche_interface() {
        // Première rangée (de 1 à 8)
        for (int i = 0; i < 8; i++) {
            System.out.print(String.format("Trou %2d [%db][%dr]  ", i + 1,this.holes[i][0],this.holes[i][1]));
        }
        System.out.print("\n");

        // Deuxième rangée (de 16 à 9, dans l'ordre inverse)
        for (int i = 15; i > 7; i--) {
            System.out.print(String.format("Trou %2d [%db][%dr]  ", i + 1,this.holes[i][0],this.holes[i][1]));
        }
    }

}
