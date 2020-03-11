package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Mmo implements Serializable {

	private HashSet<Joueur> joueurs;
	private ArrayList<Monstre> monstres;
	private Carte carte;

	private static Scanner sc = new Scanner(System.in);

	public Mmo() {
		this.joueurs = new HashSet<Joueur>();
		this.monstres = new ArrayList<Monstre>();
		this.carte = new Carte();
	}

	private boolean ajouterJoueur(Joueur j) {
		if (this.joueurs.add(j)) {
			System.out.println("Le joueur " + j + " a été ajouté");
			return true;
		}
		System.out.println("Le joueur " + j + " n'a pas été ajouté (le pseudo existe déja).");
		return false;
	}

	private void creerJoueur() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		String choixPseudo;
		String choixPersonnage = null;
		Personnage personnage = null;
		Joueur j;
		do {
			System.out.println("Veuillez entrer votre pseudo :");
			choixPseudo = sc.next();

			Crud.CreateJoueur(Conn.connection(), choixPseudo);

			choixPersonnage = null;
			while (choixPersonnage == null) {
				System.out.println("Quelle personnage voulez-vous jouer ?\n\t1 - Archer\n\t2 - Soldat\n\t3 - Sorcier");
				choixPersonnage = sc.next();
				switch (choixPersonnage) {
				case "1":
					personnage = new Archer();
					break;
				case "2":
					personnage = new Soldat();
					break;
				case "3":
					personnage = new Sorcier();
					break;
				default:
					choixPersonnage = null;
					break;
				}
			}
			Crud.createPerso(Conn.connection() , personnage, Crud.getIdByPseudo(Conn.connection(),choixPseudo));
			j = new Joueur(choixPseudo, personnage);
		} while (!ajouterJoueur(j));
	}

	private void creerMonstre() {
		this.monstres.add(new Troll());
	}

	public boolean sauvegarder() {
		File fichier = new File("mmo.save");
		try {
			FileOutputStream fs = new FileOutputStream(fichier);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void jouer() {
		System.out.println("#########################\n");
		System.out.println("LE JEU COMMENCE !");
		Object[] mesJoueurs = this.joueurs.toArray();
		Object[] mesMonstres = this.monstres.toArray();
		Combat combat = new Combat((Monstre) mesMonstres[0], ((Joueur) mesJoueurs[0]).getPersonnage(), 1000);
		combat.lancerCombat();
		this.sauvegarder();
		// combat = new Combat(((Joueur) mesJoueurs[1]).getPersonnage(), ((Joueur)
		// mesJoueurs[0]).getPersonnage(), 2000);
		// combat.lancerCombat();
		// ((Joueur) mesJoueurs[0]).lancerCombat((Joueur) mesJoueurs[1]);
		System.out.println("#########################\n");
		System.out.println("FIN DU JEU !");
	}

	public static Mmo restaurer() {
		File save = new File("mmo.save");
		Mmo result = null;
		FileInputStream fs;
		try {
			fs = new FileInputStream(save);
			ObjectInputStream os = new ObjectInputStream(fs);
			Mmo mmo = (Mmo) os.readObject();
			return mmo;
		}catch (FileNotFoundException e){
			System.out.println("Pas de joueurs créés, création nécessaire.");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		Conn conn = new Conn();
		Crud bdd = new Crud();
		Connection cnx = conn.connection();

		Mmo mmo = new Mmo();
		mmo.creerJoueur();
		/*Mmo mmo = Mmo.restaurer();
		if (mmo == null) { // Si il y a pas de récupération possible
			mmo = new Mmo();
			mmo.creerJoueur();
			mmo.creerMonstre();
		}
		RegenerationThread regen = new RegenerationThread(mmo.joueurs);
		regen.start();
		mmo.jouer();*/
	}
}
