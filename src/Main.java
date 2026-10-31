import data.CSVReader;
import model.Student;
import statistics.Statistics;

import java.text.DecimalFormat;
import java.util.ArrayList;
import preprocessing.FeatureSelector;

public class Main {

    public static void main(String[] args) {

        String filePath = "data/students.csv";

        ArrayList<Student> students =
                CSVReader.readStudents(filePath);

        DecimalFormat df = new DecimalFormat("0.00");

        Statistics statistics = new Statistics();


        // =====================================================
        // PROJECT TITLE
        // =====================================================

        System.out.println();
        System.out.println("==============================================");
        System.out.println(" UNIVERSITY STUDENT DROPOUT EARLY WARNING SYSTEM");
        System.out.println("==============================================");


        // =====================================================
        // DATASET INFORMATION
        // =====================================================

        System.out.println();
        System.out.println("DATASET INFORMATION");
        System.out.println("-------------------");

        System.out.println(
                "Total Students: " + students.size()
        );


        // =====================================================
        // TARGET DISTRIBUTION
        // =====================================================

        int dropout =
                statistics.countDropoutStudents(students);

        int enrolled =
                statistics.countEnrolledStudents(students);

        int graduate =
                statistics.countGraduateStudents(students);

        System.out.println();
        System.out.println("TARGET DISTRIBUTION");
        System.out.println("-------------------");

        System.out.println("Dropout  : " + dropout);
        System.out.println("Enrolled : " + enrolled);
        System.out.println("Graduate : " + graduate);


        // =====================================================
        // GENERAL STATISTICS
        // =====================================================

        System.out.println();
        System.out.println("GENERAL STATISTICS");
        System.out.println("------------------");

        System.out.println(
                "Average Age at Enrollment: "
                        + df.format(
                        statistics.calculateMeanAge(students)
                )
        );

        System.out.println(
                "Average Admission Grade: "
                        + df.format(
                        statistics.calculateMeanAdmissionGrade(students)
                )
        );


        // =====================================================
        // FIRST SEMESTER
        // =====================================================

        System.out.println();
        System.out.println("FIRST SEMESTER ANALYSIS");
        System.out.println("-----------------------");

        System.out.println(
                "Average Grade: "
                        + df.format(
                        statistics.calculateMeanFirstSemesterGrade(students)
                )
        );

        System.out.println(
                "Grade Standard Deviation: "
                        + df.format(
                        statistics.standardDeviationFirstSemesterGrade(students)
                )
        );

        System.out.println(
                "Average Approved Units: "
                        + df.format(
                        statistics.calculateMeanFirstSemesterApproved(students)
                )
        );


        // =====================================================
        // SECOND SEMESTER
        // =====================================================

        System.out.println();
        System.out.println("SECOND SEMESTER ANALYSIS");
        System.out.println("------------------------");

        System.out.println(
                "Average Grade: "
                        + df.format(
                        statistics.calculateMeanSecondSemesterGrade(students)
                )
        );

        System.out.println(
                "Grade Standard Deviation: "
                        + df.format(
                        statistics.standardDeviationSecondSemesterGrade(students)
                )
        );

        System.out.println(
                "Average Approved Units: "
                        + df.format(
                        statistics.calculateMeanSecondSemesterApproved(students)
                )
        );


        // =====================================================
        // DROPOUT COMPARISON
        // =====================================================

        System.out.println();
        System.out.println("DROPOUT COMPARISON");
        System.out.println("------------------");

        System.out.println(
                "Dropout Average 1st Semester Grade: "
                        + df.format(
                        statistics.dropoutAverageFirstSemesterGrade(students)
                )
        );

        System.out.println(
                "Non-Dropout Average 1st Semester Grade: "
                        + df.format(
                        statistics.nonDropoutAverageFirstSemesterGrade(students)
                )
        );


        // =====================================================
        // COMPLETION
        // =====================================================

        System.out.println();
        System.out.println("==============================================");
        System.out.println(" STATISTICAL ANALYSIS COMPLETED");
        System.out.println("==============================================");
        // =====================================================
// FEATURE SELECTION TEST
// =====================================================

        System.out.println();
        System.out.println("FEATURE SELECTION");
        System.out.println("-----------------");

        FeatureSelector featureSelector = new FeatureSelector();

        double[][] features =
                featureSelector.selectAllFeatures(students);

        String[] featureNames =
                featureSelector.getFeatureNames();

        System.out.println(
                "Total Students: " + features.length
        );

        System.out.println(
                "Selected Features: " + featureNames.length
        );

        System.out.println();
        System.out.println("FIRST STUDENT FEATURE VALUES");
        System.out.println("----------------------------");

        if (features.length > 0) {

            for (int i = 0; i < featureNames.length; i++) {

                System.out.println(
                        featureNames[i] + ": " + features[0][i]
                );
            }
        }
    }
}