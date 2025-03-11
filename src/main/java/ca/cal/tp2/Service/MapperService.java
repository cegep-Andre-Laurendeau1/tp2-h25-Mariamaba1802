package ca.cal.tp2.Service;

import ca.cal.tp2.DTOs.*;
import ca.cal.tp2.Modeles.*;

import java.util.ArrayList;
import java.util.List;

public class MapperService {

    // ***************** DOCUMENT et sous-classes *****************

    public static DocumentDTO versDocumentDTO(Document document) {
        if (document == null) return null;
        if (document instanceof Livre) {
            return versLivreDTO((Livre) document);
        } else if (document instanceof CD) {
            return versCDDTO((CD) document);
        } else {
            return versDVDDTO((DVD) document);
        }
    }

    public static Document versDocumentEntite(DocumentDTO dto) {
        if (dto == null) return null;

        if (dto instanceof LivreDTO) {
            return versLivreEntite((LivreDTO) dto);
        } else if (dto instanceof CdDTO) {
            return versCDEntite((CdDTO) dto);
        } else {
            return versDVDEntite((DvdDTO) dto);
        }
    }

    public static LivreDTO versLivreDTO(Livre livre) {
        if (livre == null) {
            return null;
        }
        return new LivreDTO(
                livre.getTitre(),
                livre.getDateParution(),
                livre.getNbExemplaire(),
                livre.getDureeEmpruntAutorisee(),
                livre.getAuteur(),
                livre.getEditeur(),
                livre.getNombreDePages()
        );
    }

    public static Livre versLivreEntite(LivreDTO livreDTO) {
        if (livreDTO == null) {
            return null;
        }
        return new Livre(
                null,
                livreDTO.getTitre(),
                livreDTO.getDateParution(),
                livreDTO.getNbExemplaire(),
                livreDTO.getAuteur(),
                livreDTO.getEditeur(),
                livreDTO.getNombreDePages()
        );
    }

    public static CdDTO versCDDTO(CD cd) {
        if (cd == null) {
            return null;
        }
        return new CdDTO(
                cd.getTitre(),
                cd.getDateParution(),
                cd.getNbExemplaire(),
                cd.getDureeEmpruntAutorisee(),
                cd.getArtiste(),
                cd.getDuree()
        );
    }

    public static CD versCDEntite(CdDTO cdDTO) {
        if (cdDTO == null) {
            return null;
        }
        return new CD(
                null,
                cdDTO.getTitre(),
                cdDTO.getDateParution(),
                cdDTO.getNbExemplaire(),
                cdDTO.getArtiste(),
                cdDTO.getDuree()
        );
    }

    public static DvdDTO versDVDDTO(DVD dvd) {
        if (dvd == null) {
            return null;
        }
        return new DvdDTO(
                dvd.getTitre(),
                dvd.getDateParution(),
                dvd.getNbExemplaire(),
                dvd.getDureeEmpruntAutorisee(),
                dvd.getRealisateur(),
                dvd.getDuree()
        );
    }

    public static DVD versDVDEntite(DvdDTO dvdDTO) {
        if (dvdDTO == null) {
            return null;
        }
        return new DVD(
                null,
                dvdDTO.getTitre(),
                dvdDTO.getDateParution(),
                dvdDTO.getNbExemplaire(),
                dvdDTO.getRealisateur(),
                dvdDTO.getDuree()
        );
    }

    // ***************** Utilisateurs et sous-classes *****************

    public static EmprunteurDTO versEmprunteurDTO(Emprunteur emprunteur) {
        if (emprunteur == null) {
            return null;
        }
        return new EmprunteurDTO(
                emprunteur.getNom(),
                emprunteur.getPrenom(),
                versListeEmpruntDTO(emprunteur.getEmprunts()),
                versListeAmendeDTO(emprunteur.getAmendes())
        );
    }

    public static Emprunteur versEmprunteurEntite(EmprunteurDTO emprunteurDTO) {
        if (emprunteurDTO == null) {
            return null;
        }
        return new Emprunteur(
                null,
                emprunteurDTO.getNom(),
                emprunteurDTO.getPrenom()
        );
    }


    public static PreposeDTO versPreposeDTO(Prepose prepose) {
        if (prepose == null) {
            return null;
        }
        return new PreposeDTO(
                prepose.getNom(),
                prepose.getPrenom()
        );
    }

    public static Prepose versPreposeEntite(PreposeDTO preposeDTO) {
        if (preposeDTO == null) {
            return null;
        }
        return new Prepose(
                null,
                preposeDTO.getNom(),
                preposeDTO.getPrenom()
        );
    }


// ***************** EMPRUNT *****************

    public static EmpruntDTO versEmpruntDTO(Emprunt emprunt) {
        if (emprunt == null) {
            return null;
        }
        return new EmpruntDTO(
                emprunt.getDateEmprunt(),
                versEmprunteurDTO(emprunt.getEmprunteur()),
                versListeLigneEmpruntDTO(emprunt.getLignesEmprunt())
        );
    }

    public static Emprunt versEmpruntEntite(EmpruntDTO empruntDTO) {
        if (empruntDTO == null) {
            return null;
        }
        return new Emprunt(
                versEmprunteurEntite(empruntDTO.getEmprunteur())
        );
    }

    public static List<EmpruntDTO> versListeEmpruntDTO(List<Emprunt> emprunts) {
        List<EmpruntDTO> listeDTO = new ArrayList<>();
        if (emprunts != null) {
            for (Emprunt emprunt : emprunts) {
                listeDTO.add(versEmpruntDTO(emprunt));
            }
        }
        return listeDTO;
    }

    public static List<Emprunt> versListeEmpruntEntite(List<EmpruntDTO> empruntsDTO) {
        List<Emprunt> listeEntite = new ArrayList<>();
        if (empruntsDTO != null) {
            for (EmpruntDTO empruntDTO : empruntsDTO) {
                listeEntite.add(versEmpruntEntite(empruntDTO));
            }
        }
        return listeEntite;
    }


    // ***************** LIGNE EMPREUNT *****************


    public static LigneEmpruntDTO versLigneEmpruntDTO(LigneEmprunt ligneEmprunt) {
        if (ligneEmprunt == null) {
            return null;
        }
        return new LigneEmpruntDTO(
                versDocumentDTO(ligneEmprunt.getDocument()),
                ligneEmprunt.getDateRetour(),
                ligneEmprunt.getDateRetourEffectif()
        );
    }

    public static List<LigneEmpruntDTO> versListeLigneEmpruntDTO(List<LigneEmprunt> lignesEmprunt) {
        List<LigneEmpruntDTO> listeDTO = new ArrayList<>();
        if (lignesEmprunt != null) {
            for (LigneEmprunt ligne : lignesEmprunt) {
                listeDTO.add(versLigneEmpruntDTO(ligne));
            }
        }
        return listeDTO;
    }


    // ***************** AMENDE *****************

    public static AmendeDTO versAmendeDTO(Amende amende) {
        if (amende == null) {
            return null;
        }
        return new AmendeDTO(
                amende.getMontant(),
                amende.getDateGeneration(),
                amende.EstPayee(),
                versEmprunteurDTO(amende.getEmprunteur())
        );
    }

    public static Amende versAmendeEntite(AmendeDTO amendeDTO) {
        if (amendeDTO == null) {
            return null;
        }
        return new Amende(
                amendeDTO.getMontant(),
                amendeDTO.getDateGeneration(),
                versEmprunteurEntite(amendeDTO.getEmprunteur())
        );
    }

    public static List<AmendeDTO> versListeAmendeDTO(List<Amende> amendes) {
        List<AmendeDTO> listeDTO = new ArrayList<>();
        if (amendes != null) {
            for (Amende amende : amendes) {
                listeDTO.add(versAmendeDTO(amende));
            }
        }
        return listeDTO;
    }
}
