


public class EstudiantesCarreraDTO {
    private String nombreCarrera;
    private long cantidadEstudiantes;

    // Constructor
    public EstudiantesCarreraDTO(String nombreCarrera, long cantidadEstudiantes) {
        this.nombreCarrera = nombreCarrera;
        this.cantidadEstudiantes = cantidadEstudiantes;
    }


    public String getNombreCarrera() {
        return nombreCarrera;
    }

    public long getCantidadEstudiantes() {
        return cantidadEstudiantes;
    }

    @Override
    public String toString() {
        return "Carrera: " + nombreCarrera + ", Inscritos: " + cantidadEstudiantes;
    }
}
