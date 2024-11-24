import java.util.*;
public class Jeu {
    int score_j1;
    int score_j2;
    int joueur;
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
        int graine_bleu=this.i.holes[ic.i][0];
        int graine_rouge=this.i.holes[ic.i][1];
        int count=ic.i;
        int jp=1+(j%2);
        int dernier_t;
        if (ic.c=='b'){
            while (graine_bleu!=0) {
                graine_bleu -= 1;
                count = (count + 2) % (14 + j);
                this.i.holes[count - 1][0] += 1;
            }
            dernier_t=count-1;
        }

        else{
            count-=2;
            while (graine_rouge!=0){
                graine_rouge-=1;
                count=(count+2)%(14+jp);
                this.i.holes[count][0]+=1;
            }
            dernier_t=count;
        }
        return dernier_t;
    }
    public void capture(int j,int trou){
        int gains=0;
        while (((this.i.holes[trou][0]+this.i.holes[trou][1]==2) || (this.i.holes[trou][0]+this.i.holes[trou][1]==3)) && trou!=0){
            gains+=this.i.holes[trou][0]+this.i.holes[trou][1];
            this.i.holes[trou][0]=0;
            this.i.holes[trou][1]=0;
            trou-=1;
            if (trou==0){
                trou=16;
            }
        }
        if(j==1){
            score_j1+=gains;
        }
        else{
            score_j2+=gains;
        }
    }
    public void semer(int j) {
        Int_char ic=choix_trou(j);
        int dernier_t=distribution(ic,j);
        capture(j,dernier_t);
        i.affiche_interface();
    }
}




