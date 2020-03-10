package main;

import java.util.HashSet;

public class RegenerationThread extends Thread {

	private HashSet<Joueur> joueurs;
	private static int regenerationParSeconde = 1;

	public RegenerationThread(HashSet<Joueur> joueurs) {
		this.joueurs = joueurs;
		this.setDaemon(true);
	}

	public void run() {
		while (true) {
			for (Joueur j : joueurs) {
				Personnage p = j.getPersonnage();
				if (p.isAlive()) {
					int santeAvantRegen = p.getSante();
					p.setSante(santeAvantRegen + RegenerationThread.regenerationParSeconde);
					if (santeAvantRegen != p.getSante()) {
						System.out
								.println("Santé de " + p + " régénéré de " + RegenerationThread.regenerationParSeconde);
					}
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println("Erreur lors de l'attente du Thread de regeneration.");
			}
		}
	}

}
