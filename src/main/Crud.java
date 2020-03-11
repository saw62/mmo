package main;

import java.sql.*;

public class Crud {

    public static int CreateJoueur(Connection cnx, String name) throws SQLException {
        Statement stmt = cnx.createStatement();
        String sql = " INSERT INTO joueur(pseudo) VALUES" +"("+ "'"+name+"'" +")";
        System.out.println(sql);
        int statut = stmt.executeUpdate(sql);
        return statut;
    }

    public static int createPerso(Connection cnx, Personnage p, Integer idJ) throws SQLException {
        PreparedStatement createp = cnx.prepareStatement (
                "INSERT INTO personnage (nomPerso, niveau, sante, santeMax, niveauExp, idJoueur) VALUES (?,?,?,?,?,?)"
        );
        createp.setString(1, String.valueOf(p.getClass()));
        createp.setInt(2, p.getNiveau());
        createp.setInt(3, p.getSante());
        createp.setInt(4, p.getSanteMax());
        createp.setInt(5, p.getProchainNiveau());
        createp.setInt(6, idJ);
        int statut = createp.executeUpdate();

        return statut;
    }

    public static Integer getIdByPseudo( Connection cnx ,String pseudo) throws SQLException {

        PreparedStatement resultat = cnx.prepareStatement("SELECT idJoueur FROM joueur WHERE pseudo = ? ");
        resultat.setString(1,pseudo);
        ResultSet  result = resultat.executeQuery();
        Integer resultId = null;
        while(result.next()){
            resultId=result.getInt(1);
        }

        return resultId;
    }

}
