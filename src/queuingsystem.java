import java.util.*;

class Student implements Comparable<Student> {
    private int id;
    private String firstName;
    private String lastName;
    private double grade;

    public Student(int id, String firstName, String lastName, double grade) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public double getGrade() { return grade; }

    public void setGrade(double grade) { this.grade = grade; }

    @Override
    public String toString() {
        return String.format("%-5d %-15s %-15s %.2f", id, lastName, firstName, grade);
    }

    @Override
    public int compareTo(Student other) {
        return this.lastName.compareTo(other.lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

public class StudentManagementSystem {
    private List<Student> students;
    private Scanner scanner;

    public StudentManagementSystem() {
        students = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("\n=== Система управления студентами ===");
            System.out.println("1. Добавить студента");
            System.out.println("2. Удалить студента");
            System.out.println("3. Найти студента по имени");
            System.out.println("4. Показать всех студентов");
            System.out.println("5. Сортировать по фамилии");
            System.out.println("6. Найти студентов с оценкой выше заданной");
            System.out.println("7. Выйти");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> removeStudent();
                case 3 -> findStudentByName();
                case 4 -> displayAllStudents();
                case 5 -> sortStudents();
                case 6 -> findStudentsByGrade();
                case 7 -> {
                    System.out.println("Выход из системы...");
                    return;
                }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private void addStudent() {
        System.out.print("Введите ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Введите имя: ");
        String firstName = scanner.nextLine();

        System.out.print("Введите фамилию: ");
        String lastName = scanner.nextLine();

        System.out.print("Введите оценку: ");
        double grade = scanner.nextDouble();

        Student student = new Student(id, firstName, lastName, grade);
        students.add(student);
        System.out.println("Студент добавлен!");
    }

    private void removeStudent() {
        System.out.print("Введите ID студента для удаления: ");
        int id = scanner.nextInt();

        boolean removed = students.removeIf(s -> s.getId() == id);
        if (removed) {
            System.out.println("Студент удален!");
        } else {
            System.out.println("Студент не найден!");
        }
    }

    private void findStudentByName() {
        System.out.print("Введите имя для поиска: ");
        String name = scanner.nextLine().toLowerCase();

        List<Student> found = students.stream()
                .filter(s -> s.getFirstName().toLowerCase().contains(name) ||
                        s.getLastName().toLowerCase().contains(name))
                .toList();

        if (found.isEmpty()) {
            System.out.println("Студенты не найдены!");
        } else {
            System.out.println("\nНайденные студенты:");
            System.out.println("ID    Фамилия         Имя             Оценка");
            found.forEach(System.out::println);
        }
    }

    private void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Список студентов пуст!");
            return;
        }

        System.out.println("\nСписок всех студентов:");
        System.out.println("ID    Фамилия         Имя             Оценка");
        students.forEach(System.out::println);
    }

    private void sortStudents() {
        Collections.sort(students);
        System.out.println("Студенты отсортированы по фамилии!");
        displayAllStudents();
    }

    private void findStudentsByGrade() {
        System.out.print("Введите минимальную оценку: ");
        double minGrade = scanner.nextDouble();

        List<Student> found = students.stream()
                .filter(s -> s.getGrade() > minGrade)
                .sorted(Comparator.comparingDouble(Student::getGrade).reversed())
                .toList();

        if (found.isEmpty()) {
            System.out.println("Студенты не найдены!");
        } else {
            System.out.println("\nСтуденты с оценкой выше " + minGrade + ":");
            System.out.println("ID    Фамилия         Имя             Оценка");
            found.forEach(System.out::println);
        }
    }

    public static void main(String[] args) {
        StudentManagementSystem system = new StudentManagementSystem();
        system.run();
    }
}
