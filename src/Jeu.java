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
                    System.out.println("voici le trou chosi"+ trou_choisi);
                    while (boucle2 == 1) {
                        if (((option2.charAt(i) == 'b') || (option2.charAt(i) == 'r')) && (trou_choisi >= j && trou_choisi <= 14+j && ((j==1)||(j == 2)))&&(trou_choisi % 2 == Math.abs(j-2))&&(this.i.holes[(trou_choisi-1)%16][0]+this.i.holes[(trou_choisi-1)%16][1]>0)){
                            ic.c = option2.charAt(i);
                            ic.i=trou_choisi;
                            boucle2 = 0;
                            boucle1 = 0;
                        }
                        else if (!(trou_choisi >= j && trou_choisi <= 14+j && trou_choisi % 2 == j)&&(this.i.holes[(trou_choisi-1)%16][0]+this.i.holes[(trou_choisi-1)%16][1]==0)) {
                            affiche_option(j,1);
                            break;
                        } else if (!(option2.charAt(i) == 'b') || (option2.charAt(i) == 'r')) {
                            System.out.print("Vous devez choisir une couleur entre le bleu et le rouge");
                            break;
                        }
                        else {
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
        int count=ic.i-1;
        int jp=1+(j%2);
        int dernier_t;
        if (ic.c=='b'){
            this.i.holes[count][0]=0;
            while (graine_bleu!=0) {
                graine_bleu -= 1;
                count +=1 ;
                if(count ==16){
                    count=0;
                }
                this.i.holes[count][0] += 1;
            }
            dernier_t=count;
        }

        else{
            if(graine_rouge!=0) {
                this.i.holes[count][1] = 0;
                count++;
                if(count ==16){
                    count=0;
                }
                this.i.holes[count][1] += 1;
                graine_rouge -= 1;
            }
            while (graine_rouge!=0) {
                count =count+2;
                if(count==17 && j==1){
                    count=1;
                }
                if(count==16 && j==2){
                    count=0;
                }
                this.i.holes[count][1] += 1;
                graine_rouge -= 1;

            }
            dernier_t=count;
        }
        return dernier_t;
    }

    public Int_int_char alpha_beta(Jeu j, int joueur, int profondeur, boolean maxi, int alpha, int beta) {

        if (joueur==1){
            maxi=false;
        }
        else{
            maxi=true;
        }

        if (profondeur == 0) {
            int eval = evaluation(j, joueur);
            Int_char a = new Int_char(0, 'o');
            Int_int_char b = new Int_int_char(eval, a);
            return b;
        }

        ArrayList<Int_char> positionsPossibles = j.position_possible(joueur);
        Int_char meilleurCoup = null;
        int meilleureValeur = maxi ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Int_char position : positionsPossibles) {
            Jeu cloneJeu = j.cloneJeu();
            cloneJeu.semer_ai(joueur, position);

            Int_int_char resultat = alpha_beta(cloneJeu, (joueur % 2) + 1, profondeur - 1, !maxi, alpha, beta);

            if (maxi) {
                if (resultat.a > meilleureValeur) {
                    meilleureValeur = resultat.a;
                    meilleurCoup = position;
                }
                alpha = Math.max(alpha, meilleureValeur);
            } else {
                if (resultat.a < meilleureValeur) {
                    meilleureValeur = resultat.a;
                    meilleurCoup = position;
                }
                beta = Math.min(beta, meilleureValeur);
            }

            if (alpha >= beta) {
                break;
            }
        }

        return new Int_int_char(meilleureValeur, meilleurCoup);


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
            for (int k = 0; k < 15; k+=2) {
                for (int l = 0; l < 2; l++) {
                    nb+=this.i.holes[k][l];
                }
            }
        }
        else{
            for (int k = 1; k < 16; k+=2) {
                for (int l = 0; l < 2; l++) {
                    nb+=this.i.holes[k][l];
                }
            }
        }
        return nb;
    }
    public void semer(int j) {
        Int_char ic=choix_trou(j);
        int dernier_t=distribution(ic,j);
        capture(j,dernier_t);
        i.affiche_interface();
    }
    public void semer_ai(int j,Int_char ic) {
        int dernier_t=distribution(ic,j);
        capture(j,dernier_t);
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

    public int capture_eval(Interface i) {
        int maxGains = 0;
        for (int trou = 0; trou < 16; trou++) {
            int gains = 0;
            int currentTrou = trou;
            while (currentTrou >= 0 && currentTrou < 16 && ((i.holes[currentTrou][0] + i.holes[currentTrou][1] == 2) || (i.holes[currentTrou][0] + i.holes[currentTrou][1] == 3))) {
                gains += i.holes[currentTrou][0] + i.holes[currentTrou][1];
                i.holes[currentTrou][0] = 0;
                i.holes[currentTrou][1] = 0;
                currentTrou -= 1;
                if (currentTrou == -1) {
                    currentTrou = 15;
                }
            }
            if (gains > maxGains) {
                maxGains = gains;
            }
        }
        return maxGains;
    }
/*
    public int capture_eval(Interface i,int trou){//la fonction capture nous sert dans notre fonction d'evaluation
        // a évalué le nombre de graine qu'on peut capturer à une position donnée
        int gains=0;
        while (trou >= 0 && trou<16 &&((this.i.holes[trou][0]+this.i.holes[trou][1]==2) || (this.i.holes[trou][0]+this.i.holes[trou][1]==3))){
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
*/
    public int nb_graines_capture(){
        int nb=0;
        for (int k = 1; k < 16; k+=2) {
            for (int l = 0; l < 2; l++) {
                nb+=this.i.holes[k][l];
            }
        }
        return nb;
    }

    public ArrayList<Int_char> position_possible(int j){
        ArrayList<Int_char> liste_position = new ArrayList<>();
        int nb_r;
        int nb_b;
        if (j==1){
            for (int k = 0; k < 15; k+=2) {
                nb_b=this.i.holes[k][0];
                nb_r=this.i.holes[k][1];
                if(nb_r!=0){
                    liste_position.add(new Int_char(k+1,'r'));
                    nb_r=0;
                }
                if(nb_b!=0){
                    liste_position.add(new Int_char(k+1,'b'));
                    nb_b=0;
                }
            }
        }
        else {
            for (int k = 1; k <16; k += 2) {
                nb_b = this.i.holes[k][0];
                nb_r = this.i.holes[k][1];
                if (nb_r != 0) {
                    liste_position.add(new Int_char(k+1, 'r'));
                    nb_r = 0;
                }
                if (nb_b != 0) {
                    liste_position.add(new Int_char(k+1, 'b'));
                    nb_b = 0;
                }
            }
        }
        return liste_position;
    }

    public Interface clone_interface(){
        Interface  a=new Interface();
        a=this.i;
        return a;
    }

    public int evaluation(Jeu j,int joueur){
        int score=j.score_j2-j.score_j1;
        int nb_graine=nb_graines_trou();
        int Estfamine=0;
        int capturePotentiel = 0;

        for (Int_char position : j.position_possible(joueur)) {
            Jeu cloneJeu = j.cloneJeu();
            cloneJeu.semer_ai(joueur, position);
            capturePotentiel += cloneJeu.capture_eval(cloneJeu.i);
        }
        //int capture=capture_eval(j.i,ic.i);
        if(famine(joueur%2+1)==0){
            Estfamine=100;
        }
        return score*10+2*nb_graine+50*Estfamine + capturePotentiel;
    }

    public Jeu cloneJeu() {
        // Créer une copie du jeu actuel
        Jeu clone = new Jeu(new Interface());

        // Copier les scores
        clone.score_j1 = this.score_j1;
        clone.score_j2 = this.score_j2;

        // Copier l'état du plateau
        for (int i = 0; i < this.i.holes.length; i++) {
            clone.i.holes[i] = this.i.holes[i].clone();
        }

        // Retourner la copie
        return clone;
    }
    public void parcourirPositions(ArrayList<Int_char> positionsPossibles) {
        // Vérification si la liste est vide
        if (positionsPossibles.isEmpty()) {
            System.out.println("La liste des positions possibles est vide.");
            return;
        }

        // Parcours de la liste avec une boucle for
        for (Int_char position : positionsPossibles) {
            // Affichage des informations de chaque Int_char dans la liste
        }
    }
    public Int_int_char min_max(Jeu j,int joueur, int profondeur, boolean maxi) {
        // Si la profondeur est 0, évaluer l'état actuel et retourner une valeur
        if (joueur==1){
            maxi=false;
        }
        else{
            maxi=true;
        }

        if (profondeur == 0) {
            int eval = evaluation(j,joueur);
            Int_char a=new Int_char(0,'o');
            Int_int_char b=new Int_int_char(eval,a);
            return b;
        }

        ArrayList<Int_char> positionsPossibles = j.position_possible(joueur);
        parcourirPositions(positionsPossibles);
        Int_char meilleurCoup = null;
        int meilleureValeur = maxi ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        if (maxi==true){
            for (Int_char position : positionsPossibles) {
                Jeu cloneJeu = j.cloneJeu();
                cloneJeu.semer_ai(joueur,position);
                maxi=false;
                Int_int_char resultat = min_max(cloneJeu,(joueur % 2) + 1, profondeur - 1, maxi);
                if (resultat.a > meilleureValeur) {
                    meilleureValeur = resultat.a;
                    meilleurCoup = position;
                }
            }
        } if(maxi==false) {
            for (Int_char position : positionsPossibles) {
                // Cloner l'état du jeu
                Jeu cloneJeu = j.cloneJeu();
                maxi=true;
                // Simuler le coup
                cloneJeu.semer_ai(joueur,position);
                // Appeler récursivement min_max
                Int_int_char resultat = min_max(cloneJeu,(joueur % 2) + 1, profondeur - 1, maxi);
                if (resultat.a < meilleureValeur) {
                    meilleureValeur = resultat.a;
                    meilleurCoup = position;
                }
            }
        }
        Int_int_char a=new Int_int_char(meilleureValeur,meilleurCoup);
        return a;
    }


    public void playAgainstAI(int joueur) {
        Scanner scanner = new Scanner(System.in);
        int j=1;
        while ((score_j1 < 33 && score_j2 < 33) && nb_graines() >= 8 && famine(j) > 0) {
            System.out.println("\nScore joueur 1 = " + score_j1);
            System.out.println("Score joueur 2 = " + score_j2);

            if (j == joueur) { // Tour du joueur humain
                System.out.println("\nC'est votre tour !");
                semer(j);
            } else { // Tour de l'IA
                System.out.println("\nTour de l'IA...");
                Interface cloneInterface = clone_interface();
                Jeu cloneJeu = new Jeu(cloneInterface);
                cloneJeu.score_j1 = this.score_j1;
                cloneJeu.score_j2 = this.score_j2;
                Int_int_char bestMove = min_max(cloneJeu, j, 6,true);
                //Int_int_char bestMove = alpha_beta(cloneJeu, j, 6, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
                System.out.println("Voici le coup de l'ia :"+ bestMove.b.i + bestMove.b.c);
                if (bestMove != null) {
                    semer_ai(j,bestMove.b);
                } else {
                    System.out.println("L'IA n'a pas pu jouer de coup valide.");
                    break; // Sortir de la boucle si l'IA ne peut pas jouer
                }
            }

            i.affiche_interface(); // Mettre à jour et afficher l'interface

            // Passer au joueur suivant
            j = 1 + (j % 2);
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



    public void play(){
        int j=1;
        System.out.println("\nScore joueur 1 ="+score_j1);
        System.out.println("Score joueur 2 ="+score_j2);
        while (((score_j1 != 33) && (score_j2 != 33)) &&
                (nb_graines() >= 8) &&
                (famine(j) > 0)){
            semer(j);
            System.out.println("\nScore joueur 1 ="+score_j1);
            System.out.println("Score joueur 2 ="+score_j2);
            j=1+(j%2);
        }
        if(famine(j)==0){
            System.out.println("Le joueur %d vient de gagner !".formatted(1+(j%2)));
        }
        else if(score_j1>score_j2){
            System.out.println("Le joueur 1 vient de gagner !");
        }
        else if(score_j1==score_j2){
            System.out.println("Match nul ! ");
        }
        else{
            System.out.println("Le joueur 2 vient de gagner !");
        }

    }

    public void AIvsAI(int p1,int p2) {
        int j = 1; // Début avec le joueur 1
        int p;
        while ((score_j1 < 33 && score_j2 < 33) && nb_graines() >= 8 && famine(j) > 0) {
            if(j==1){
                p=p1;
            }
            else{
                p=p2;
            }
            System.out.println("\nScore joueur 1 = " + score_j1);
            System.out.println("Score joueur 2 = " + score_j2);
            System.out.println("\nTour de l'IA " + (j == 1 ? "Joueur 1" : "Joueur 2") + "...");

            // Cloner l'état actuel du jeu
            Interface cloneInterface = clone_interface();
            Jeu cloneJeu = new Jeu(cloneInterface);
            cloneJeu.score_j1 = this.score_j1;
            cloneJeu.score_j2 = this.score_j2;

            // Calculer le meilleur coup pour l'IA actuelle
            Int_int_char bestMove = alpha_beta(cloneJeu, j, p, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
            System.out.println("Coup choisi par l'IA " + (j == 1 ? "Joueur 1" : "Joueur 2") + ": " + bestMove.b.i + bestMove.b.c);

            if (bestMove != null) {
                semer_ai(j, bestMove.b);
            } else {
                System.out.println("L'IA n'a pas pu jouer de coup valide.");
                break; // Sortir de la boucle si l'IA ne peut pas jouer
            }

            i.affiche_interface(); // Mettre à jour et afficher l'interface

            // Passer au joueur suivant
            j = 1 + (j % 2);
        }

        // Fin du jeu : déterminer le gagnant
        System.out.println("\nFin du jeu !");
        if (score_j1 > score_j2) {
            System.out.println("L'IA Joueur 1 a gagné avec un score de " + score_j1);
        } else if (score_j2 > score_j1) {
            System.out.println("L'IA Joueur 2 a gagné avec un score de " + score_j2);
        } else {
            System.out.println("Match nul !");
        }
    }
}



