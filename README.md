
# Jeu Awele avec IA 

### Les différentes commandes pour utiliser notre programme (dans le main)

0) La base pour chaque commande :

        Interface i=new Interface();
        i.affiche_interface();
        Jeu j=new Jeu(i);

1) Jouer contre l'IA :

        int joueur = 1 //ou 2
        j.playAgainstAI(joueur);//ici nous sommes le joueur 1 et 
                                //l'IA le joueur 2

2) Tester une IA contre une autre IA :

        j.AIvsAI(p1,p2)//p1 et p2 correspondent à la profondeur 
                        //qu'on souhaite dans notre alpha_beta
