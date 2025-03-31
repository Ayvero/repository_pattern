
import java.util.List;

public interface EstudianteRepository {
    void save(Estudiante estudiante);
    List<Estudiante> findAll();
    Estudiante findById(String dni);
    public List<EstudianteDTO> listarEstudiantesOrdenados(String ordenPor);
    EstudianteDTO findByNumeroLibreta(String numeroLibreta);
    public List<EstudianteDTO> findByGenero(String genero);
    void delete(Estudiante estudiante);
    void close();
}
