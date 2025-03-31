

import javax.persistence.*;
import java.util.Date;

@Entity
public class Estudiante_Carrera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean isGraduado;
    private Date fechaInscripcion;
    private int antiguedad;
    @ManyToOne
    private Carrera carrera;
    @ManyToOne
    private Estudiante estudiante;

    public boolean isGraduado() {
        return isGraduado;
    }

    public void setGraduado(boolean graduado) {
        isGraduado = graduado;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public int getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(int antiguedad) {
        this.antiguedad = antiguedad;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
}
