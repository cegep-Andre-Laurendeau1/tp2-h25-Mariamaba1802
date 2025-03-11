package ca.cal.tp2.utilis

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

object HibernateUtil {
    // ✅ La méthode doit être static
    val sessionFactory: SessionFactory? = buildSessionFactory() // ✅ Correct

    private fun buildSessionFactory(): SessionFactory {
        try {
            return Configuration().configure("hibernate.cfg.xml").buildSessionFactory()
        } catch (ex: Throwable) {
            System.err.println("❌ Erreur Hibernate : $ex")
            throw ExceptionInInitializerError(ex)
        }
    }

    fun shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close()
        }
    }
}