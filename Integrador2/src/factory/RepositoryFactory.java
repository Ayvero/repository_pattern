
public abstract class Repositoryfactory {
    public static final int MYSQL_JDBC = 1;
    public static final int DERBY_JDBC = 2;


    public abstract EstudianteRepositoryImpl getEstudianteRepository();
    public abstract CarreraRepositoryImpl getCarreraRepository();
    public abstract Estudiante_CarreraRepositoryImpl getEstudiante_CarreraRepository();


    public static Repositoryfactory getRepositoryFactory(int whichFactory) {
        switch (whichFactory) {
            case MYSQL_JDBC:
                return new MySQLRepositoryFactory();
            case DERBY_JDBC:
                return new DerbyRepositoryFactory();
            default:
                return null;
        }
    }
}