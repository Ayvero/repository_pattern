
import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

public class EstudianteRepositoryImpl implements EstudianteRepository {
    private EntityManagerFactory emf;

    public EstudianteRepositoryImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    //Da de alta un nuevo estudiante
    public void save(Estudiante estudiante) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (em.find(Estudiante.class, estudiante.getDni()) == null) {
                em.persist(estudiante);
            } else {
                em.merge(estudiante);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    //Lista todos los estudiantes
    public List<Estudiante> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT e FROM Estudiante e";
            TypedQuery<Estudiante> query = em.createQuery(jpql, Estudiante.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    //Recupera estudiante por PK
    public Estudiante findById(String dni) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Estudiante.class, dni);
        } finally {

            em.close();
        }
    }

    // Elimina estudiante
    public void delete(Estudiante estudiante) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Estudiante mergeEstudiante = em.merge(estudiante);
            em.remove(mergeEstudiante);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    //Lista Estudiantes ORDENADOS por algun criterio
    public List<EstudianteDTO> listarEstudiantesOrdenados(String ordenPor) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT e FROM Estudiante e";

            if (ordenPor != null && !ordenPor.isEmpty()) {
                jpql += " ORDER BY ";
                switch (ordenPor.toLowerCase()) {
                    case "apellido":
                        jpql += "e.apellido";
                        break;
                    case "libreta":
                        jpql += "e.numeroLibretaUniversitaria";
                        break;
                    default:
                        throw new IllegalArgumentException("Criterio de ordenamiento no vÃ¡lido.");
                }
            }

            TypedQuery<Estudiante> query = em.createQuery(jpql, Estudiante.class);
            List<Estudiante> estudiantes = query.getResultList();
           return estudiantes.stream()
                    .map(e -> new EstudianteDTO(
                            e.getDni(),
                            e.getNombres(),
                            e.getApellido(),
                            e.getGenero(),
                            e.getCiudadResidencia(),
                            e.getNumeroLibretaUniversitaria()))
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    //Recupera estudiante por numero de libreta
    public EstudianteDTO findByNumeroLibreta(String numeroLibreta) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT e FROM Estudiante e WHERE e.numeroLibretaUniversitaria = :numeroLibreta";
            TypedQuery<Estudiante> query = em.createQuery(jpql, Estudiante.class);
            query.setParameter("numeroLibreta", numeroLibreta);

            Estudiante estudiante = query.getSingleResult();
            if (estudiante != null) {
                return new EstudianteDTO(estudiante.getDni(), estudiante.getNombres(),
                        estudiante.getApellido(), estudiante.getGenero(),
                        estudiante.getCiudadResidencia(),
                        estudiante.getNumeroLibretaUniversitaria());
            }
            return null;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    //Recupera todos los estudiantes en base a su genero
    public List<EstudianteDTO> findByGenero(String genero) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT e FROM Estudiante e WHERE e.genero = :genero";
            TypedQuery<Estudiante> query = em.createQuery(jpql, Estudiante.class);
            query.setParameter("genero", genero);
            List<Estudiante> estudiantes = query.getResultList();
            return estudiantes.stream()
                    .map(e -> new EstudianteDTO(
                            e.getDni(),
                            e.getNombres(),
                            e.getApellido(),
                            e.getGenero(),
                            e.getCiudadResidencia(),
                            e.getNumeroLibretaUniversitaria()))
                    .collect(Collectors.toList());
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
