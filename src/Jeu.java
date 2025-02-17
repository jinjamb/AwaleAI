import java.util.*;

public class Jeu {
    int score_j1;
    int score_j2;
    Interface i;

    Jeu(Interface i) {
        this.i = i;
    }
    public void affiche_option(int j,int o) {
        /* Cette fonction affiche lorsque c'est autour d'un des 2 joueurs, quelle choix de trou a-t-il. */
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
    public int famine(int j,int s1,int s2){
        int nb=0;
        if(j==1 && s1<33){
            for (int k = 0; k < 15; k+=2) {
                for (int l = 0; l < 2; l++) {
                    nb+=this.i.holes[k][l];
                }
            }
            return nb;
        } else if (j==2 && s2<33) {
            for (int k = 1; k < 16; k+=2) {
                for (int l = 0; l < 2; l++) {
                    nb+=this.i.holes[k][l];
                }
            }
            return nb;
        }
        return 1;
    }
    public void semer(int j) {
        Int_char ic=choix_trou(j);
        int dernier_t=distribution(ic,j);
        capture(j,dernier_t);
        //i.affiche_interface();
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

    public int nb_graines_trou(){//
         /* nombre de graines dans les trous de l'adversaire(sert uniquement à l'ia) */
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
        /* Cette fonction évalue le nombre de graine qu'on peut capturer à une phase de la partie */
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

    public ArrayList<Int_char> position_possible(int j){
        /* Cette fonction énumère les positions possibles jouables en fonction de l'état du plateau */
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

    public int phaseDeLaPartie(int turnCount) {
        /* ici turncount représente le nombre de coup divisé par 2 (un coup par joueur).*/
        if (turnCount < 10) {
            return 1;
        } else if (turnCount < 30) {
            return 2;
        } else {
            return 3;
        }
    }

    public int evaluation(Jeu j,int joueur, int turnCount){
        /* fonction utiliser dans notre alpha_beta permettant de connaitre le "meilleur" coup à jouer en fonction
        du score renvoyé par l'algorithme. Ce score dépend de plusieurs paramètres tels que :
        -si on a une différence score_j2-score_j1 > 0.
        -le nombre de graine qu'on a.
        -si on arrive à capturer plusieurs graines pendant cette phase de jeu.
        -si on arrive à affamer l'adversaire.
         */
        int score=j.score_j2-j.score_j1;
        int nb_graine=nb_graines_trou();
        int Estfamine=0;
        int capturePotentiel = 0;
        int phase = phaseDeLaPartie(turnCount);

        for (Int_char position : j.position_possible(joueur)) {
            Jeu cloneJeu = j.cloneJeu();
            cloneJeu.semer_ai(joueur, position);
            capturePotentiel += cloneJeu.capture_eval(cloneJeu.i);
        }
        //int capture=capture_eval(j.i,ic.i);
        if(famine(joueur%2+1,j.score_j1,j.score_j2)==0){
            Estfamine=100;
        }

        return switch (phase) {
            case 1 -> score * 20 + 2 * nb_graine + 70 * Estfamine + 30 * capturePotentiel;
            case 2 -> score * 10 + 2 * nb_graine + 50 * Estfamine + 50 * capturePotentiel;
            default -> score * 10 + 2 * nb_graine + 50 * Estfamine + 60 * capturePotentiel;
        };
    }


    public Interface clone_interface(){
        Interface  a=new Interface();
        a=this.i;
        return a;
    }


    public int nb_trou_c(int joueur) {
        int nb = 0;
        if (joueur == 2) {
            for (int k = 1; k < 16; k += 2) {
                if(this.i.holes[k][0]>0||this.i.holes[k][1]>0){
                    nb++;
                };
            }
        } else {
            for (int k = 0; k < 15; k += 2) {
                if(this.i.holes[k][0]>0||this.i.holes[k][1]>0){
                    nb++;
                };
            }
        }
        return nb;
    }

    public int evaluation2(Jeu j,int joueur){
        /*deuxième version de notre fonction d'evaluation utilisé dans notre deuxième alpha_beta

         */
        int score;
        if(joueur==1){
            score=j.score_j1-j.score_j2;
        }
        else{
            score=j.score_j2-j.score_j1;
        }

        int nb_graine=nb_graines_trou();
        int Estfamine=0;
        //int capture=capture_eval(j.i);
        if(famine(joueur%2+1,j.score_j1,j.score_j2)==0){
            Estfamine=100000;
        }


        return score*10+2*nb_graine+50*Estfamine;
    }

    public Jeu cloneJeu() {
        // Créer une copie du jeu actuel
        Jeu clone = new Jeu(new Interface());

        // Copier les scores
        clone.score_j1 = this.score_j1;
        clone.score_j2 = this.score_j2;

        for (int i = 0; i < this.i.holes.length; i++) {
            clone.i.holes[i] = this.i.holes[i].clone();
        }

        return clone;
    }
    public void parcourirPositions(ArrayList<Int_char> positionsPossibles) {
        /* fonction beaucoup utiliser pour le debogage pour savoir quelles positions sont
        prises par notre alpha_beta
         */
        if (positionsPossibles.isEmpty()) {
            System.out.println("La liste des positions possibles est vide.");
            return;
        }

        for (Int_char position : positionsPossibles) {
            System.out.println("Position: " + position.i + ", Couleur: " + position.c);
        }
    }

    public int nb_graines_trou_adversaire(int joueur) {
        int nb = 0;
        if (joueur == 1) {
            for (int k = 1; k < 16; k += 2) {
                for (int l = 0; l < 2; l++) {
                    nb += this.i.holes[k][l];
                }
            }
        } else {
            for (int k = 0; k < 15; k += 2) {
                for (int l = 0; l < 2; l++) {
                    nb += this.i.holes[k][l];
                }
            }
        }
        return nb;
    }
    public Int_int_char alpha_beta(Jeu j, int joueur, int profondeur, boolean maxi, int alpha, int beta, int turnCount) {
            /* première version de notre alpha beta fonctionnant avec la première fonction d'evaluation
            le but est de prendre en paramètre des valeurs alpha = -l'infini, beta=+l'infini puis nous
            modifions alpha et beta dans notre code
             */
        if (joueur==1){
            maxi=false;
        }
        else{
            maxi=true;
        }

        if (profondeur == 0) {
            int eval = evaluation(j, joueur, turnCount);
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

            Int_int_char resultat = alpha_beta(cloneJeu, (joueur % 2) + 1, profondeur - 1, !maxi, alpha, beta, turnCount);

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

    public Int_int_char alpha_beta2(Jeu j, int joueur, int profondeur, boolean maxi, int alpha, int beta) {
        /*deuxième version de notre alph beta prenant utilisant une autre fonction d'évaluation*/

        // Si la profondeur est 0, évaluer l'état actuel et retourner une valeur
        if (profondeur == 0||!((j.score_j1 < 33 && j.score_j2 < 33) && nb_graines() >= 8 && famine(joueur,j.score_j1,j.score_j2) > 0)) {
            int eval = evaluation2(j, joueur);
            Int_char a = new Int_char(0, 'o');
            Int_int_char b = new Int_int_char(eval, a);
            return b;
        }

        ArrayList<Int_char> positionsPossibles = j.position_possible(joueur);
        if(positionsPossibles.isEmpty()){
            int eval = evaluation2(j, joueur);
            Int_char a = new Int_char(0, 'o');
            Int_int_char b = new Int_int_char(eval, a);
            return b;
        }
        Int_char meilleurCoup = null;
        int meilleureValeur = maxi ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Int_char position : positionsPossibles) {
            Jeu cloneJeu = j.cloneJeu();
            cloneJeu.semer_ai(joueur, position);

            Int_int_char resultat = alpha_beta2(
                    cloneJeu,
                    (joueur % 2) + 1,
                    profondeur - 1,
                    !maxi,
                    alpha,
                    beta
            );

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

            // Coupure Alpha-Beta
            if (alpha >= beta) {
                break;
            }
        }

        return new Int_int_char(meilleureValeur, meilleurCoup);
    }



    public void playAgainstAI(int joueur) {//une fonction nous permettant de jouer contre notre ia
        //et prenant en paramètre quel joueur souhaite jouer l'utilisateur
        Scanner scanner = new Scanner(System.in);
        int nb_coup=0;
        int j=1;
        int p=7;
        int t=0;
        while ((score_j1 < 33 && score_j2 < 33) && nb_graines() >= 8 && famine(j,score_j1,score_j2) > 0) {
            System.out.println("\nScore joueur 1 = " + score_j1);
            System.out.println("Score joueur 2 = " + score_j2);
            System.out.println(nb_coup);
            nb_coup+=1;
            if(nb_coup>10){
                p=8;
            }
            else if(nb_coup>30){
                p=9;
            }
            if(nb_coup>50){
                p=10;
            }
            if (j == joueur) { // Tour du joueur humain
                System.out.println("\nC'est votre tour !");
                semer(j);
            } else { // Tour de l'IA
                System.out.println("\nTour de l'IA...");
                Interface cloneInterface = clone_interface();
                Jeu cloneJeu = new Jeu(cloneInterface);
                cloneJeu.score_j1 = this.score_j1;
                cloneJeu.score_j2 = this.score_j2;
                Int_int_char bestMove=new Int_int_char(0,new Int_char(0,'o'));
                if(j==1) {
                    bestMove = alpha_beta2(cloneJeu, j, p, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    t++;
                }
                else if(j==2) {
                    bestMove = alpha_beta2(cloneJeu, j, p, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    t++;
                }
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
        if(famine(j,score_j1,score_j2)==0){
            if(j==joueur) {
                System.out.println("L'IA a gagné avec un score de ");
            }
            else{
                System.out.println("Félicitations ! Vous avez gagné avec un score de ");
            }
        }
        if (score_j1 > score_j2) {
            if(joueur==1) {
                System.out.println("Félicitations ! Vous avez gagné avec un score de " + score_j1);
            }
            else{
                System.out.println("L'IA a gagné avec un score de " + score_j1);
            }
        } else if (score_j2 > score_j1) {
            if(joueur==2) {
                System.out.println("Félicitations ! Vous avez gagné avec un score de " + score_j2);
            }
            else{
                System.out.println("L'IA a gagné avec un score de " + score_j2);
            }
        } else {
            System.out.println("Match nul !");
        }
    }

    public void AIvsAI(int p1,int p2) {
        int j = 1; // Début avec le joueur 1
        int t=0;
        int nb_coup=0;
        int p=p1;
        while ((score_j1 < 33 && score_j2 < 33) && nb_graines() >= 8 && famine(j,score_j1,score_j2) > 0) {
            nb_coup+=1;
            if(nb_coup==30) {
                p=9;
            }
            else if(nb_coup==60){
                p=10;
            }
            System.out.println("\nScore joueur 1 = " + score_j1);
            System.out.println("Score joueur 2 = " + score_j2);
            System.out.println("\n coup"+nb_coup+" de l'IA " + (j == 1 ? "Joueur 1" : "Joueur 2") + "...");

            // Cloner l'état actuel du jeu
            Interface cloneInterface = clone_interface();
            Jeu cloneJeu = new Jeu(cloneInterface);
            cloneJeu.score_j1 = this.score_j1;
            cloneJeu.score_j2 = this.score_j2;
            Int_int_char bestMove=new Int_int_char(0,new Int_char(0,'o'));
            if(j==2) {
                bestMove = alpha_beta(cloneJeu,j,p1,true, Integer.MIN_VALUE, Integer.MAX_VALUE,t);
                t++;
            }
            else if(j==1) {
                bestMove = alpha_beta2(cloneJeu, j, p2, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
            if (bestMove != null && bestMove.b!=null) {
                System.out.println("Coup choisi"+nb_coup+ " par l'IA " + (j == 1 ? "Joueur 1" : "Joueur 2") + ": " + bestMove.b.i + bestMove.b.c);
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
        if(famine(j,score_j1,score_j2)==0){
            if(j==1) {
                System.out.println("L'IA 2 a gagné");
            }
            else{
                System.out.println("L'IA 1 a gagné");
            }
        }
        else if(famine((j%2)+1,score_j1,score_j2)==0){
            if(j==1) {
                System.out.println("L'IA 1 a gagné");
            }
            else{
                System.out.println("L'IA 2 a gagné");
            }
        }
        else if (score_j1 > score_j2) {
            System.out.println("L'IA Joueur 1 a gagné avec un score de " + score_j1);
        } else if (score_j2 > score_j1) {
            System.out.println("L'IA Joueur 2 a gagné avec un score de " + score_j2);
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
                (famine(j,score_j1,score_j2) > 0)){
            semer(j);
            System.out.println("\nScore joueur 1 ="+score_j1);
            System.out.println("Score joueur 2 ="+score_j2);
            j=1+(j%2);
        }
        if(famine(j,score_j1,score_j2)==0){
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
}




