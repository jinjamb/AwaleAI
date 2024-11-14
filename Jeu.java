import java.util.*;
public class Jeu {
    int score_j1;
    int score_j2;
    int joueur;
    Interface i;
    Jeu(Interface i){
        this.i=i;
    }
    public void Semer(int joueur) {
        Scanner choix = new Scanner(System.in);
        int graine_bleu = 0;
        int graine_rouge = 0;

        // Pour le joueur 1
        if (joueur == 1) {
            System.out.println("Vous pouvez choisir un trou impair entre 1 et 15:");
            int trou = choix.nextInt();

            // Validation du choix (impair et dans la plage valide)
            if (trou % 2 == 1 && trou >= 1 && trou <= 15) {
                graine_bleu = this.i.holes[trou][0];
                graine_rouge = this.i.holes[trou][1];
                this.i.holes[trou][0] = 0;
                this.i.holes[trou][1] = 0;

                // Distribution des graines bleues dans les trous impairs
                int trou_actuel = trou;
                for (int i = 0; i < graine_bleu; i++) {
                    trou_actuel = (trou_actuel + 2) % 16;  // Passe au prochain trou impair
                    if (trou_actuel == 0) trou_actuel = 1;  // Réinitialiser à 1 si on dépasse 16

                    System.out.print("Combien de graines bleues voulez-vous placer dans le trou " + trou_actuel + "? ");
                    int graines_ajoutees = choix.nextInt();
                    this.i.holes[trou_actuel][0] += graines_ajoutees;
                }

                // Distribution des graines rouges dans les trous pairs
                trou_actuel = trou + 1;  // Commencer par le trou pair suivant
                for (int i = 0; i < graine_rouge; i++) {
                    if (trou_actuel > 16) trou_actuel = 2;  // Repartir du premier trou pair si on dépasse 16
                    this.i.holes[trou_actuel][1] += 1;
                    trou_actuel = (trou_actuel + 2) % 16;  // Passe au prochain trou pair
                    if (trou_actuel == 0) trou_actuel = 2;  // Réinitialiser à 2 si on dépasse 16
                }
            } else {
                System.out.println("Choix invalide. Veuillez choisir un trou impair entre 1 et 15.");
            }

            // Pour le joueur 2
        } else {
            System.out.println("Vous pouvez choisir un trou pair entre 2 et 16:");
            int trou = choix.nextInt();

            // Validation du choix (pair et dans la plage valide)
            if (trou % 2 == 0 && trou >= 2 && trou <= 16) {
                graine_bleu = this.i.holes[trou][0];
                graine_rouge = this.i.holes[trou][1];
                this.i.holes[trou][0] = 0;
                this.i.holes[trou][1] = 0;

                // Distribution des graines bleues dans les trous pairs
                int trou_actuel = trou;
                for (int i = 0; i < graine_bleu; i++) {
                    trou_actuel = (trou_actuel + 2) % 16;  // Passe au prochain trou pair
                    if (trou_actuel == 0) trou_actuel = 2;  // Réinitialiser à 2 si on dépasse 16

                    System.out.print("Combien de graines bleues voulez-vous placer dans le trou " + trou_actuel + "? ");
                    int graines_ajoutees = choix.nextInt();
                    this.i.holes[trou_actuel][0] += graines_ajoutees;
                }

                // Distribution des graines rouges dans les trous impairs
                trou_actuel = trou - 1;  // Commencer par le trou impair précédent
                for (int i = 0; i < graine_rouge; i++) {
                    if (trou_actuel < 1) trou_actuel = 15;  // Repartir du dernier trou impair si on est avant 1
                    this.i.holes[trou_actuel][1] += 1;
                    trou_actuel = (trou_actuel - 2 + 16) % 16;  // Passe au prochain trou impair en reculant
                    if (trou_actuel == 0) trou_actuel = 15;  // Réinitialiser à 15 si on atteint 0
                }
            } else {
                System.out.println("Choix invalide. Veuillez choisir un trou pair entre 2 et 16.");
            }
        }
    }

}
