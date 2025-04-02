package ca.cal.tp2.Dao;

public interface repository_parent<T> {
   void ajouter(T entity);
   void supprimer(T entity);
}
