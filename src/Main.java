import data.CSVReader;
import model.Student;
import statistics.Statistics;
import java.text.DecimalFormat;

import java.util.ArrayList;


public class Main {
    static DecimalFormat df = new DecimalFormat("0.00");


    public static void main(String[] args) {


        CSVReader reader = new CSVReader();


        ArrayList<Student> students =
                reader.readStudents("data/students.csv");



        System.out.println(
                "University Student Dropout Early Warning System"
        );


        System.out.println("--------------------------------");


        System.out.println(
                "Total Students: "
                        + students.size()
        );



        Statistics stats = new Statistics();



        System.out.println(
                "Average GPA: "
                        + df.format(stats.calculateMeanGPA(students))
        );


        System.out.println(
                "Average Attendance: "
                        + df.format(stats.calculateMeanAttendance(students))
        );


        System.out.println(
                "GPA Standard Deviation: "
                        + df.format(stats.standardDeviationGPA(students))
        );



        System.out.println("\nComparison:");

        stats.dropoutComparison(students);



    }

}