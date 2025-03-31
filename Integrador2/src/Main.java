

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.Date;
import java.util.List;

public class Main {
    public static  void main(String[] args) {
        Repositoryfactory RepositoryFactory = Repositoryfactory.getRepositoryFactory(1);

        EstudianteRepositoryImpl estudianteRepository = RepositoryFactory.getEstudianteRepository();
        CarreraRepositoryImpl carreraRepository = RepositoryFactory.getCarreraRepository();
        Estudiante_CarreraRepositoryImpl estudiante_carreraRepository = RepositoryFactory.getEstudiante_CarreraRepository();

        // Cargar Estudiantes desde CSV
        cargarEstudiantesDesdeCSV(estudianteRepository, "src/main/java/org/example/datasets/estudiantes.csv");
        // Cargar Carreras desde CSV
        cargarCarrerasDesdeCSV(carreraRepository, "src/main/java/org/example/datasets/carreras.csv");

        //Nuevo estudiante
        Estudiante e = new Estudiante();
        e.setDni("29.547.666");
        e.setNombres("Micaela");
        e.setApellido("Dominguez");
        e.setEdad(20);
        e.setGenero("femenino");
        e.setNumeroLibretaUniversitaria("47666");
        e.setCiudadResidencia("Tandil");

        // Punto a) ALTA Estudiante
        estudianteRepository.save(e);

        //Punto b) Matricular Estudiante en Carrera
        Date fechaInscripcion = new Date();
        estudiante_carreraRepository.matricularEstudiante(e.getDni(), 1, fechaInscripcion);
        estudiante_carreraRepository.matricularEstudiante("30.243.986", 1, fechaInscripcion);

        // Punto c)
        // Ordenar por apellido
        List<EstudianteDTO> estudiantesOrdenadosPorApellido = estudianteRepository.listarEstudiantesOrdenados("apellido");
        System.out.println("Estudiantes ordenados por apellido:");
        for (EstudianteDTO estudiante : estudiantesOrdenadosPorApellido) {
            System.out.println(estudiante);
        }

        // Ordenar por nÃºmero de libreta
        List<EstudianteDTO> estudiantesOrdenadosPorLibreta = estudianteRepository.listarEstudiantesOrdenados("libreta");
        System.out.println("Estudiantes ordenados por nÃºmero de libreta:");
        for (EstudianteDTO estudiante : estudiantesOrdenadosPorLibreta) {
            System.out.println(estudiante  + "Nro Libreta :" + estudiante.getNumeroLibretaUniversitaria());
        }

        //Punto d) Obtener Estudiante por NÃºmero de Libreta
       EstudianteDTO estudianteDTO = estudianteRepository.findByNumeroLibreta(e.getNumeroLibretaUniversitaria());
       if (estudianteDTO != null) {
            System.out.println("Estudiante encontrado: " + estudianteDTO);
       } else {
            System.out.println("Estudiante no encontrado.");
       }

       //Punto e) Listar estudiantes por gÃ©nero
        String generoBuscado = "masculino";
        List<EstudianteDTO> estudiantesPorGenero = estudianteRepository.findByGenero(generoBuscado);
        System.out.println("Listado estudiantes de gÃ©nero " + generoBuscado + ":");
        for (EstudianteDTO estudiante : estudiantesPorGenero) {
            System.out.println(estudiante);
        }

        //Punto f) carreras con estudiantes inscriptos
        List<EstudiantesCarreraDTO> carrerasConEstudiantes = estudiante_carreraRepository.obtenerCarrerasConEstudiantesInscritos();

        System.out.println("Carreras con estudiantes inscritos, ordenadas por cantidad:");
        for (EstudiantesCarreraDTO dto : carrerasConEstudiantes) {
            System.out.println(dto);
        }

        //Punto g) recuperar los estudiantes de una determinada carrera, filtrado por ciudad de residencia
        int idCarrera = 1;
        String ciudad = "Tandil";
        List<EstudianteDTO> estudiantes = estudiante_carreraRepository.obtenerEstudiantesPorCarreraYCiudad(idCarrera, ciudad);

        System.out.println("Estudiantes en la carrera con ID " + idCarrera + " y ciudad " + ciudad + ":");
        for (EstudianteDTO estudiante : estudiantes) {
            System.out.println(estudiante);
        }

        //PUNTO 3
        List<ReporteDTO> reporteCarreras = estudiante_carreraRepository.generarReporteCarreras();

        System.out.println("Reporte de Carreras:");
        for (ReporteDTO reporte : reporteCarreras) {
            System.out.println(reporte);
        }


        estudianteRepository.close();
        carreraRepository.close();
        estudiante_carreraRepository.close();
    }





    private static void cargarEstudiantesDesdeCSV(EstudianteRepositoryImpl estudianteRepository, String archivoCsv) {
        try (CSVReader csvReader = new CSVReader(new FileReader(archivoCsv))) {
            String[] fila;
            while ((fila = csvReader.readNext()) != null) {
                Estudiante estudiante = new Estudiante();
                estudiante.setDni(fila[0]);
                estudiante.setNombres(fila[1]);
                estudiante.setApellido(fila[2]);
                estudiante.setEdad(Integer.parseInt(fila[3]));
                estudiante.setGenero(fila[4]);
                estudiante.setNumeroLibretaUniversitaria(fila[5]);
                estudiante.setCiudadResidencia(fila[6]);

                estudianteRepository.save(estudiante);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cargarCarrerasDesdeCSV(CarreraRepositoryImpl carreraRepository, String archivoCsv) {
        try (CSVReader csvReader = new CSVReader(new FileReader(archivoCsv))) {
            String[] fila;
            while ((fila = csvReader.readNext()) != null) {
                Carrera carrera = new Carrera();
                carrera.setNombre(fila[1]);

                carreraRepository.save(carrera);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
