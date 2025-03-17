package filemanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import main.Student;

/**
 *
 * @author Angelo
 */
public class StudentManager {

    public static File register = new File(System.getProperty("user.dir") + File.separator + "registro.txt");
    public static BufferedReader brProgram = new BufferedReader(new InputStreamReader(System.in));
    public static ArrayList<Student> students = new ArrayList<>();

    public static void createFile() {
        if (!register.exists()) {
            try {
                register.createNewFile();
            } catch (IOException ex) {
                StudentManager.showError(ex);
            }
        }
    }

    public static boolean isEmpty() {
        try {
            FileReader fr = new FileReader(register);
            BufferedReader brFile = new BufferedReader(fr);

            for (String line = brFile.readLine(); line != null; line = brFile.readLine()) {
                return false;
            }

        } catch (IOException ex) {
            showError(ex);
        }
        return true;
    }

    public static void initializeInformationStudent() {
        try {
            FileReader fr = new FileReader(register);
            BufferedReader brFile = new BufferedReader(fr);

            try {
                for (String line = brFile.readLine(); line != null; line = brFile.readLine()) {
                    String[] array = line.split(";");

                    Student student = new Student(array[0], array[1], Integer.parseInt(array[2]), array[3], array[4]);
                    students.add(student);
                }
            } catch (IOException ex) {
                showError(ex);
            }
        } catch (FileNotFoundException ex) {
            showError(ex);
        }
    }

    public static void newStudent(String name, String surname, int age, String course, String dni) {
        boolean correctDNI = true;

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "LOG ERROR: No puedes dejar el nombre vacío", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (surname.isEmpty()) {
            JOptionPane.showMessageDialog(null, "LOG ERROR: No puedes dejar el apellido vacío", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;

        }
        if (course.isEmpty()) {
            JOptionPane.showMessageDialog(null, "LOG ERROR: No puedes dejar el curso vacío", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;

        }
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(null, "LOG ERROR: No puedes dejar el DNI vacío", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getDni().equals(dni)) {
                JOptionPane.showMessageDialog(null, "LOG ERROR: El DNI del Alumno indicado está repetido", "ERROR", JOptionPane.ERROR_MESSAGE);
                correctDNI = false;
                break;
            }
        }

        if (correctDNI) {
            Student student = new Student(name, surname, age, course, dni);
            System.out.println("LOG: Alumno creado correctamente.\n");
            students.add(student);

            overWriteRegister();
        }
    }

    public static void overWriteRegister() {
        try {
            FileWriter fw = new FileWriter(register, false);
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i = 0; i < students.size(); i++) {
                bw.write(students.get(i).toString() + "\n");
            }

            bw.flush();
            bw.close();
        } catch (IOException ex) {
            showError(ex);
        }
    }

    public static void removeStudent(String dni) {
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(null, "LOG ERROR: El DNI no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean encontrado = false;

        for (int i = students.size() - 1; i >= 0; i--) {
            if (students.get(i).getDni().equalsIgnoreCase(dni)) {
                students.remove(i);
                encontrado = true;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "LOG ERROR: El DNI iniciado no se encuentra.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        overWriteRegister();
    }

    public static String showRegister() {
        if (!isEmpty()) {
            String linea = "<html>\n";
            linea += "<div>\n";
            for (Student s : students) {
                linea += "<p>Nombre: " + s.getName() + " " + s.getSurname() + "</p>\n";
                linea += "<p>Edad: " + s.getAge() + "</p>\n";
                linea += "<p>DNI: " + s.getDni() + "</p>\n";
                linea += "<p>Curso: " + s.getGrade() + "</p>\n\n";
            }
            linea += "</div>";
            linea += "\n</html>";

            return linea;
        } else {
            return null;
        }
    }

    public static String showStudentByDNI(String dni) {
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(null, "LOG ERROR: No puedes dejar el DNI vacío", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        boolean correctDNI = true;

        for (int i = 0; i < students.size(); i++) {
            if (!students.get(i).getDni().equals(dni)) {
                JOptionPane.showMessageDialog(null, "LOG ERROR: No hay ningún Alumno con ese DNI", "ERROR", JOptionPane.ERROR_MESSAGE);
                correctDNI = false;
                break;
            }
        }

        if (!isEmpty() && correctDNI) {
            try {
                FileReader fr = new FileReader(register);
                BufferedReader brFile = new BufferedReader(fr);

                for (String line = brFile.readLine(); line != null; line = brFile.readLine()) {
                    if (line.contains(dni)) {
                        return line + "\n";
                    }
                }

            } catch (IOException ex) {
                showError(ex);
            }
        }

        if (isEmpty()) {
            JOptionPane.showMessageDialog(null, "LOG ERROR: No hay ningún alumno en el registro.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static void showError(Exception ex) {
        System.err.println("Ha ocurrido un error: " + ex.getMessage() + "\n");
    }
}
