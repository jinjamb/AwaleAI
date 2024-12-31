import javax.swing.*;
import java.util.*;

import static java.lang.Integer.max;

public class Jeu {
    int score_j1;
    int score_j2;
    Interface i;

    Jeu(Interface i) {
        this.i = i;
    }
    public void affiche_option(int j,int o) {
        if (j==1 && o==0){
            System.out.print("\nVous pouvez choisir un trou impair entre 1 et 15 et un couleur " +
                    "sous la forme NC, où :\n" +
                    "\n" +
                    "N est le numero du trou.\n" +
                    "C est la couleur des graines jouees.:");}
        else if (j==1 && o==1) {
            System.out.print("Vous devez entrer un nombre impair entre 1 et 15");
        } else if(j==2 && o==0){
            System.out.print("\nVous pouvez choisir un trou pair entre 2 et 16 et une couleur " +
                    "sous la forme NC, où :\n" +
                    "\n" +
                    "N est le numero du trou.\n" +
                    "C est la couleur des graines jouees.:");}
        else {
            System.out.print("Vous devez entrer un nombre pair entre 2 et 16");
        }
    }
    public Int_char choix_trou(int j){
        Int_char ic=new Int_char(0,' ');
        Scanner choix = new Scanner(System.in);
        int boucle1 = 1;
        while (boucle1 != 0) {
            affiche_option(j,0);
            String option2 = choix.nextLine();
            int trou_choisi;
            boucle1 = 1;
            for (int i = 0; i < option2.length(); i++) {
                int boucle2 = 1;
                if (!Character.isDigit(option2.charAt(i))) {
                    trou_choisi = Integer.valueOf(option2.substring(0, i));
                    while (boucle2 == 1) {
                        if (((option2.charAt(i) == 'b') || (option2.charAt(i) == 'r')) && (trou_choisi >= j && trou_choisi <= 14+j && ((j==1)||(j == 2)))) {
                            ic.c = option2.charAt(i);
                            ic.i=trou_choisi;
                            boucle2 = 0;
                            boucle1 = 0;
                        }
                        else if (!(trou_choisi >= j && trou_choisi <= 14+j && trou_choisi % 2 == j)) {
                            affiche_option(j,1);
                            break;
                        } else if (!(option2.charAt(i) == 'b') || (option2.charAt(i) == 'r')) {
                            System.out.print("Vous devez choisir une couleur entre le bleu et le rouge");
                            break;
                        } else {
                            affiche_option(j,0);
                            break;
                        }
                    }
                }
            }
        }
        return ic;
    }
    public int distribution(Int_char ic,int j){
        int graine_bleu=this.i.holes[ic.i-1][0];
        int graine_rouge=this.i.holes[ic.i-1][1];
        int count=ic.i;
        int jp=1+(j%2);
        int dernier_t;
        if (ic.c=='b'){
            this.i.holes[count-1][0]=0;
            while (graine_bleu!=0) {
                graine_bleu -= 1;
                count = (count +1);
                if(count ==17){
                    count=1;
                }
                this.i.holes[count-1][0] += 1;
            }
            dernier_t=count-1;
        }

        else{
            System.out.println(graine_rouge);
            this.i.holes[count-1][1]=0;
            count++;
            this.i.holes[count-1][1] += 1;
            graine_rouge -= 1;
            while (graine_rouge!=0) {
                count-=1;
                count = (count +2)%16;
                count+=1;
                this.i.holes[count-1][1] += 1;
                graine_rouge -= 1;

            }
            dernier_t=count;
        }
        return dernier_t;
    }
    public void capture(int j,int trou){
        int gains=0;
        while ((this.i.holes[trou][0]+this.i.holes[trou][1]==2) || (this.i.holes[trou][0]+this.i.holes[trou][1]==3)){
            gains+=this.i.holes[trou][0]+this.i.holes[trou][1];
            this.i.holes[trou][0]=0;
            this.i.holes[trou][1]=0;
            trou-=1;
            if (trou==-1){
                trou=15;
            }
        }
        if(j==1){
            score_j1+=gains;
        }
        else{
            score_j2+=gains;
        }
    }
    public int famine(int j){
        int nb=0;
        if(j==1){
            for (int k = 0; k < 15; k++) {
                for (int l = 0; l < 2; l++) {
                    nb+=this.i.holes[k][l];
                }
            }
        }
        else{
            for (int k = 1; k < 16; k++) {
                for (int l = 0; l < 2; l++) {
                    nb+=this.i.holes[k][l];
                }
            }
        }
        return nb;
    }
    public void semer(int j) {
        Int_char ic=choix_trou(j);
        System.out.println("voilà le trou choisi"+ic.i+ic.c);
        int dernier_t=distribution(ic,j);
        System.out.println("voilà le dernier trou"+dernier_t);
        capture(j,dernier_t);
        i.affiche_interface();
    }

    public int nb_graines(){
        int nb=0;
        for (int k = 0; k < 16; k++) {
            for (int l = 0; l < 2; l++) {
                nb+=this.i.holes[k][l];
            }
        }
        return nb;
    }

    public int nb_graines_trou(){//nombre de graines dans les trous de l'adversaire(sert uniquement à l'ia)
        int nb=0;
        for (int k = 0; k < 15; k+=2) {
            for (int l = 0; l < 2; l++) {
                nb+=this.i.holes[k][l];
            }
        }
        for (int j = 1; j < 16; j+=2) {
            for (int l = 0; l < 2; l++) {
                nb-=this.i.holes[j][l];
            }
        }
        return nb;
    }

    public int capture_eval(Interface i,int trou){//la fonction capture nous sert dans notre fonction d'evaluation a évalué le nombre de graine qu'on peut capturer à une position donnée
        int gains=0;
        while ((this.i.holes[trou][0]+this.i.holes[trou][1]==2) || (this.i.holes[trou][0]+this.i.holes[trou][1]==3)){
            gains+=this.i.holes[trou][0]+this.i.holes[trou][1];
            this.i.holes[trou][0]=0;
            this.i.holes[trou][1]=0;
            trou-=1;
            if (trou==-1){
                trou=15;
            }
        }
        return gains;

    }

    public int nb_graines_capture(){
        int nb=0;
        for (int k = 1; k < 16; k+=2) {
            for (int l = 0; l < 2; l++) {
                nb+=this.i.holes[k][l];
            }
        }
        return nb;
    }

    public ArrayList<Int_char> position_possible(){
        ArrayList<Int_char> liste_position = new ArrayList<>();
        int nb_r;
        int nb_b;
        for (int k = 1; k < 16; k+=2) {
            nb_r=this.i.holes[k][0];
            nb_b=this.i.holes[k][1];
            if(nb_r!=0){
                liste_position.add(new Int_char(k,'r'));
                nb_r=0;
            }
            if(nb_b!=0){
                liste_position.add(new Int_char(k,'b'));
                nb_b=0;
            }
        }
        return liste_position;
    }

    public void affiche_liste(){
        ArrayList<Int_char> a=new ArrayList<>();
        a=position_possible();
        for(int i=0;i<a.size();i++){
            System.out.println(a.get(i).i);
            System.out.println(a.get(i).c);
        }
    }

    public Interface clone_interface(){
        Interface  a=new Interface();
        a=this.i;
        return a;
    }

    public int evaluation(Int_char ic){
        int score=score_j2-score_j1;
        int nb_graine=nb_graines_trou();
        int famine=0;
        Interface a=clone_interface();
        int capture=capture_eval(a,ic.i);
        if(nb_graines()<=8){
            famine=100;
        }
        return score*10+capture*5+2*nb_graine+50*famine;
    }

    public Int_char min_max(int noeud, int profondeur, boolean maxi) {
        if (profondeur == 0) {
            // Évaluer la position actuelle et retourner une évaluation
            return new Int_char(noeud, (char) (evaluation(new Int_char(noeud, ' ')) + '0'));
        }


        ArrayList<Int_char> positionsPossibles = position_possible();
        Int_char meilleurCoup = null;
        int meilleureValeur = maxi ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Int_char position : positionsPossibles) {
            // Cloner l'interface pour simuler le coup
            Interface cloneInterface = clone_interface();
            Jeu cloneJeu = new Jeu(cloneInterface);
            cloneJeu.score_j1 = this.score_j1;
            cloneJeu.score_j2 = this.score_j2;

            // Simuler le coup
            cloneJeu.distribution(position, noeud % 2 + 1);

            // Appeler récursivement min_max
            Int_char resultat = cloneJeu.min_max(position.i, profondeur - 1, !maxi);

            // Mettre à jour la meilleure valeur et le meilleur coup
            if (maxi) {
                if (resultat.i > meilleureValeur) {
                    meilleureValeur = resultat.i;
                    meilleurCoup = position;
                }
            } else {
                if (resultat.i < meilleureValeur) {
                    meilleureValeur = resultat.i;
                    meilleurCoup = position;
                }
            }
        }

        if (meilleurCoup == null) {
            meilleurCoup = new Int_char(0, ' '); // Si aucun coup n'est trouvé
        }

        meilleurCoup.i = meilleureValeur; // Ajouter l'évaluation au meilleur coup
        return meilleurCoup;
    }

    public void playAgainstAI() {
        int currentPlayer = 1; // 1 pour le joueur humain, 2 pour l'IA
        Scanner scanner = new Scanner(System.in);

        while ((score_j1 < 33 && score_j2 < 33) && nb_graines() > 8 && famine(currentPlayer) > 0) {
            System.out.println("\nScore joueur 1 = " + score_j1);
            System.out.println("Score joueur 2 = " + score_j2);

            if (currentPlayer == 1) { // Tour du joueur humain
                System.out.println("\nC'est votre tour !");
                semer(currentPlayer);
            } else { // Tour de l'IA
                System.out.println("\nTour de l'IA...");
                Int_char bestMove = min_max(currentPlayer, 5, true); // Profondeur de recherche = 5
                if (bestMove != null && bestMove.c != 0) {
                    System.out.println("L'IA choisit le trou " + bestMove.i + " avec la couleur " + bestMove.c);
                    distribution(bestMove, currentPlayer);
                } else {
                    System.out.println("L'IA n'a pas pu jouer de coup valide.");
                    break; // Sortir de la boucle si l'IA ne peut pas jouer
                }
            }

            i.affiche_interface(); // Mettre à jour et afficher l'interface

            // Passer au joueur suivant
            currentPlayer = 1 + (currentPlayer % 2);
        }

        // Fin du jeu : déterminer le gagnant
        System.out.println("\nFin du jeu !");
        if (score_j1 > score_j2) {
            System.out.println("Félicitations ! Vous avez gagné avec un score de " + score_j1);
        } else if (score_j2 > score_j1) {
            System.out.println("L'IA a gagné avec un score de " + score_j2);
        } else {
            System.out.println("Match nul !");
        }
    }

    public int evaluation(Int_char ic) {
        int score = score_j2 - score_j1;
        int nb_graine = nb_graines_trou();
        int famine = 0;
        Interface a = clone_interface();
        int capture = capture_eval(a, ic.i);
        if (nb_graines() <= 8) {
            famine = 100;
        }
        return score * 10 + capture * 5 + 2 * nb_graine + 50 * famine;
    }

    public void play(){
        int j=1;
        while((score_j1!=33 || score_j2!=33)||(score_j1!=32 && score_j2!=32)||(nb_graines()>8)||(famine(j)>0)){
            System.out.println("\nScore joueur 1 ="+score_j1);
            System.out.println("Score joueur 2 ="+score_j2);
            semer(j);
            j=1+(j%2);
        }
        if(score_j1>score_j2){
            System.out.println("Le joueur 1 vient de gagner !");
        }
        else if(score_j1==score_j2){
            System.out.println("Match nul ! ");
        }
        else if(famine(j)==0){
            System.out.println("Le joueur %d vient de gagner !".formatted(1+(j%2)));
        }
        else{
            System.out.println("Le joueur 2 vient de gagner !");
        }

    }
}










