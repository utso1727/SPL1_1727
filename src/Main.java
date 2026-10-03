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



        Statistics statistics = new Statistics();

        System.out.println();
        System.out.println("STATISTICAL ANALYSIS");
        System.out.println("------------------------------");

        System.out.println(
                "Total Students: "
                        + students.size()
        );

        System.out.println(
                "Dropout Students: "
                        + statistics.countDropoutStudents(students)
        );

        System.out.println(
                "Non-Dropout Students: "
                        + statistics.countNonDropoutStudents(students)
        );

        System.out.println();

        System.out.println(
                "Average Previous Qualification Grade: "
                        + statistics.calculateMeanPreviousQualificationGrade(students)
        );

        System.out.println(
                "Average Admission Grade: "
                        + statistics.calculateMeanAdmissionGrade(students)
        );

        System.out.println(
                "Average 1st Semester Grade: "
                        + statistics.calculateMeanFirstSemesterGrade(students)
        );

        System.out.println(
                "Average 2nd Semester Grade: "
                        + statistics.calculateMeanSecondSemesterGrade(students)
        );

        System.out.println();

        System.out.println(
                "1st Semester Grade Standard Deviation: "
                        + statistics.standardDeviationFirstSemesterGrade(students)
        );

        System.out.println(
                "2nd Semester Grade Standard Deviation: "
                        + statistics.standardDeviationSecondSemesterGrade(students)
        );

        System.out.println();

        System.out.println("Comparison:");
        statistics.dropoutComparison(students);



    }

}