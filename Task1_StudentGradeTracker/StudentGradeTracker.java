import java.util.*;
public class StudentGradeTracker {
    static class Student {
        String name;
        double grade;

        Student(String name, double grade) {
            this.name = name;
            this.grade = grade;
        }
    }

    static ArrayList<Student> students = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== STUDENT GRADE TRACKER =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Show Statistics");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> displayStudents();
                case 3 -> showStatistics();
                case 4 -> {
                    System.out.println("Thank you!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void addStudent() {
        System.out.print("Enter student name: ");
        String name = sc.nextLine();

        double grade;
        while (true) {
            System.out.print("Enter grade (0-100): ");
            grade = readDouble();
            if (grade >= 0 && grade <= 100) break;
            System.out.println("Grade must be between 0 and 100.");
        }

        students.add(new Student(name, grade));
        System.out.println("Student added successfully.");
    }

    static void displayStudents() {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        System.out.println("\n----- STUDENT REPORT -----");
        System.out.printf("%-5s %-25s %-10s%n", "No.", "Name", "Grade");

        int i = 1;
        for (Student s : students) {
            System.out.printf("%-5d %-25s %-10.2f%n", i++, s.name, s.grade);
        }
    }

    static void showStatistics() {
        if (students.isEmpty()) {
            System.out.println("No grades available.");
            return;
        }

        double total = 0;
        double highest = students.get(0).grade;
        double lowest = students.get(0).grade;
        String highName = students.get(0).name;
        String lowName = students.get(0).name;

        for (Student s : students) {
            total += s.grade;

            if (s.grade > highest) {
                highest = s.grade;
                highName = s.name;
            }

            if (s.grade < lowest) {
                lowest = s.grade;
                lowName = s.name;
            }
        }

        double average = total / students.size();

        System.out.println("\n----- STATISTICS -----");
        System.out.printf("Average Score : %.2f%n", average);
        System.out.printf("Highest Score : %.2f (%s)%n", highest, highName);
        System.out.printf("Lowest Score  : %.2f (%s)%n", lowest, lowName);
    }

    static int readInt() {
        while (true) {
            try {
                int value = Integer.parseInt(sc.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    static double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid grade: ");
            }
        }
    }
}
