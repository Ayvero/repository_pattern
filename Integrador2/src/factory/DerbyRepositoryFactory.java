
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DerbyRepositoryFactory extends Repositoryfactory {
    private EntityManagerFactory emf;

    public DerbyRepositoryFactory() {

        this.emf = Persistence.createEntityManagerFactory("derby_persistence_unit");
    }


    public EstudianteRepositoryImpl getEstudianteRepository() {

        return new EstudianteRepositoryImpl(emf);
    }


    public CarreraRepositoryImpl getCarreraRepository() {
        return CarreraRepositoryImpl.newCarreraRepositoryImpl(emf);
    }

    public Estudiante_CarreraRepositoryImpl getEstudiante_CarreraRepository() {
        return Estudiante_CarreraRepositoryImpl.newEstudiante_CarreraRepositoryImpl(emf);
    }


}
