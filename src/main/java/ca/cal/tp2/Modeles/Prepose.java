package ca.cal.tp2.Modeles;

import jakarta.persistence.*;

@Entity
@Table(name = "Prepose")
@PrimaryKeyJoinColumn(name = "id")
public class Prepose extends Utilisateur {

    public Prepose() {}


    public Prepose(Long id, String nom, String prenom) {
        super(id, nom, prenom);
    }



}
