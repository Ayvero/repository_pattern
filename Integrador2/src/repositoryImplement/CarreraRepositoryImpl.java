
import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

public class CarreraRepositoryImpl implements CarreraRepository {
    private EntityManagerFactory emf;

    private static CarreraRepositoryImpl carRepo= null;

    private CarreraRepositoryImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }
        //singleton
    public static CarreraRepositoryImpl newCarreraRepositoryImpl(EntityManagerFactory emf){
        if(carRepo==null){
            carRepo = new CarreraRepositoryImpl(emf);
        }
        return carRepo;
    }

    // Guarda una nueva carrera
    public void save(Carrera carrera) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (em.find(Carrera.class, carrera.getId()) == null) {
                em.persist(carrera);
            } else {
                em.merge(carrera);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Encuentra todas las carreras
    public List<Carrera> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT c FROM Carrera c";
            TypedQuery<Carrera> query = em.createQuery(jpql, Carrera.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Encuentra carrera por ID
    public Carrera findById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Carrera.class, id);
        } finally {
            em.close();
        }
    }

    // Elimina carrera
    public void delete(Carrera carrera) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Carrera mergeCarrera = em.merge(carrera);
            em.remove(mergeCarrera);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }




    // Cerrar el EntityManagerFactory al finalizar
    public void close() {
        if (emf != null) {
            emf.close();
        }
    }
}
