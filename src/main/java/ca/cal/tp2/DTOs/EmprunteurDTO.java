package ca.cal.tp2.DTOs;

import java.util.ArrayList;
import java.util.List;

public class EmprunteurDTO extends UtilisateurDTO {
    private List<EmpruntDTO> emprunts;
    private List<AmendeDTO> amendes;


    public EmprunteurDTO(String nom, String prenom, List<EmpruntDTO> emprunts, List<AmendeDTO> amendes) {
        super(nom, prenom);
        this.emprunts = emprunts != null ? emprunts : new ArrayList<>();
        this.amendes = amendes != null ? amendes : new ArrayList<>();
    }


}
