

import java.util.List;

public interface CarreraRepository {
    void save(Carrera carrera);
    List<Carrera> findAll();
    Carrera findById(int id);
    void delete(Carrera carrera);
    void close();
}
