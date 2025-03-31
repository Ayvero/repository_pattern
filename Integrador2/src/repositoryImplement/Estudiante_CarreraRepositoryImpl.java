
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Estudiante_CarreraRepositoryImpl {
    private EntityManagerFactory emf;

    private static Estudiante_CarreraRepositoryImpl estCarRepo= null;

    private Estudiante_CarreraRepositoryImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }
      //singleton
    public static Estudiante_CarreraRepositoryImpl newEstudiante_CarreraRepositoryImpl(EntityManagerFactory emf){
        if(estCarRepo==null){
            estCarRepo = new Estudiante_CarreraRepositoryImpl(emf);
        }
        return estCarRepo;
    }


    //Matricula un estudiante a una carrera
    public void matricularEstudiante(String dniEstudiante, int idCarrera, Date fechaInscripcion) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            Estudiante estudiante = em.find(Estudiante.class, dniEstudiante);
            if (estudiante == null) {
                throw new IllegalArgumentException("Estudiante no encontrado con DNI: " + dniEstudiante);
            }
            Carrera carrera = em.find(Carrera.class, idCarrera);
            if (carrera == null) {
                throw new IllegalArgumentException("Carrera no encontrada con ID: " + idCarrera);
            }

            Estudiante_Carrera estudianteCarrera = new Estudiante_Carrera();
            estudianteCarrera.setEstudiante(estudiante);
            estudianteCarrera.setCarrera(carrera);
            estudianteCarrera.setFechaInscripcion(fechaInscripcion);

            em.persist(estudianteCarrera);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstudiantesCarreraDTO> obtenerCarrerasConEstudiantesInscritos() {
        EntityManager em = emf.createEntityManager();

        String jpql = "SELECT new org.example.DTO.EstudiantesCarreraDTO(c.nombre, COUNT(ec.estudiante)) " +
                "FROM Carrera c JOIN Estudiante_Carrera ec ON c.id = ec.carrera.id " +
                "GROUP BY c.id, c.nombre " +
                "ORDER BY COUNT(ec.estudiante) DESC";

        TypedQuery<EstudiantesCarreraDTO> query = em.createQuery(jpql, EstudiantesCarreraDTO.class);
        List<EstudiantesCarreraDTO> resultados = query.getResultList();

        em.close();
        return resultados;
    }

    public List<EstudianteDTO> obtenerEstudiantesPorCarreraYCiudad(int idCarrera, String ciudad) {
        EntityManager em = emf.createEntityManager();

        String jpql = "SELECT e " +
                "FROM Estudiante_Carrera ec JOIN ec.estudiante e " +
                "WHERE ec.carrera.id = :idCarrera AND e.ciudadResidencia = :ciudad";

        TypedQuery<Estudiante> query = em.createQuery(jpql, Estudiante.class);
        query.setParameter("idCarrera", idCarrera);
        query.setParameter("ciudad", ciudad);

        List<Estudiante> estudiantes = query.getResultList();
        em.close();

       return estudiantes.stream()
                .map(e -> new EstudianteDTO(
                        e.getDni(),
                        e.getNombres(),
                        e.getApellido(),
                        e.getGenero(),
                        e.getCiudadResidencia(),
                        e.getNumeroLibretaUniversitaria()))
                .collect(Collectors.toList());
    }

    public List<ReporteDTO> generarReporteCarreras() {
        EntityManager em = emf.createEntityManager();

        // Obtener las carreras
        String jpqlCarreras = "SELECT c FROM Carrera c ORDER BY c.nombre";
        TypedQuery<Carrera> queryCarreras = em.createQuery(jpqlCarreras, Carrera.class);
        List<Carrera> carreras = queryCarreras.getResultList();

        List<ReporteDTO> reportes = new ArrayList<>();

        for (Carrera carrera : carreras) {
            // Obtener inscriptos por aÃ±o
            Map<Integer, Long> inscriptosPorAno = em.createQuery(
                            "SELECT YEAR(ec.fechaInscripcion), COUNT(ec) " +
                                    "FROM Estudiante_Carrera ec " +
                                    "WHERE ec.carrera.id = :idCarrera " +
                                    "GROUP BY YEAR(ec.fechaInscripcion)",
                            Object[].class)
                    .setParameter("idCarrera", carrera.getId())
                    .getResultList()
                    .stream()
                    .collect(Collectors.toMap(
                            e -> (Integer) e[0],
                            e -> (Long) e[1]
                    ));

           Map<Integer, Long> egresadosPorAno = em.createQuery(
                            "SELECT YEAR(ec.fechaInscripcion), COUNT(ec) " +
                                    "FROM Estudiante_Carrera ec " +
                                    "WHERE ec.carrera.id = :idCarrera AND ec.isGraduado = true " +
                                    "GROUP BY YEAR(ec.fechaInscripcion)",
                            Object[].class)
                    .setParameter("idCarrera", carrera.getId())
                    .getResultList()
                    .stream()
                    .collect(Collectors.toMap(
                            e -> (Integer) e[0],
                            e -> (Long) e[1]
                    ));

            reportes.add(new ReporteDTO(carrera.getNombre(), inscriptosPorAno, egresadosPorAno));
        }

        em.close();
        return reportes;
    }

    // Cerrar el EntityManagerFactory al finalizar
    public void close() {
        if (emf != null) {
            emf.close();
        }
    }
}