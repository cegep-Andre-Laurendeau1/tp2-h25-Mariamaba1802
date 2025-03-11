package ca.cal.tp2.Dao;

public interface DAO_hibernate<T> {
   void ajouter(T entity);
   void supprimer(T entity);
}
