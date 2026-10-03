package statistics;

import model.Student;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Statistics {

    private static final DecimalFormat df =
            new DecimalFormat("0.00");


    // Average previous qualification grade
    public double calculateMeanPreviousQualificationGrade(
            ArrayList<Student> students) {

        if (students.isEmpty()) {
            return 0;
        }

        double sum = 0;

        for (Student s : students) {
            sum += s.getPreviousQualificationGrade();
        }

        return sum / students.size();
    }


    // Average admission grade
    public double calculateMeanAdmissionGrade(
            ArrayList<Student> students) {

        if (students.isEmpty()) {
            return 0;
        }

        double sum = 0;

        for (Student s : students) {
            sum += s.getAdmissionGrade();
        }

        return sum / students.size();
    }


    // Average first semester grade
    public double calculateMeanFirstSemesterGrade(
            ArrayList<Student> students) {

        if (students.isEmpty()) {
            return 0;
        }

        double sum = 0;

        for (Student s : students) {
            sum += s.getFirstSemGrade();
        }

        return sum / students.size();
    }


    // Average second semester grade
    public double calculateMeanSecondSemesterGrade(
            ArrayList<Student> students) {

        if (students.isEmpty()) {
            return 0;
        }

        double sum = 0;

        for (Student s : students) {
            sum += s.getSecondSemGrade();
        }

        return sum / students.size();
    }


    // Standard deviation of first semester grade
    public double standardDeviationFirstSemesterGrade(
            ArrayList<Student> students) {

        if (students.isEmpty()) {
            return 0;
        }

        double mean =
                calculateMeanFirstSemesterGrade(students);

        double sum = 0;

        for (Student s : students) {

            sum += Math.pow(
                    s.getFirstSemGrade() - mean,
                    2
            );
        }

        return Math.sqrt(sum / students.size());
    }


    // Standard deviation of second semester grade
    public double standardDeviationSecondSemesterGrade(
            ArrayList<Student> students) {

        if (students.isEmpty()) {
            return 0;
        }

        double mean =
                calculateMeanSecondSemesterGrade(students);

        double sum = 0;

        for (Student s : students) {

            sum += Math.pow(
                    s.getSecondSemGrade() - mean,
                    2
            );
        }

        return Math.sqrt(sum / students.size());
    }


    // Count dropout students
    public int countDropoutStudents(
            ArrayList<Student> students) {

        int count = 0;

        for (Student s : students) {

            if (s.getDropout() == 1) {
                count++;
            }
        }

        return count;
    }


    // Count non-dropout students
    public int countNonDropoutStudents(
            ArrayList<Student> students) {

        int count = 0;

        for (Student s : students) {

            if (s.getDropout() == 0) {
                count++;
            }
        }

        return count;
    }


    // Compare dropout and non-dropout students
    // using first semester grade
    public void dropoutComparison(
            ArrayList<Student> students) {

        double dropoutGrade = 0;
        double nonDropoutGrade = 0;

        int dropoutCount = 0;
        int nonDropoutCount = 0;


        for (Student s : students) {

            if (s.getDropout() == 1) {

                dropoutGrade +=
                        s.getFirstSemGrade();

                dropoutCount++;

            } else {

                nonDropoutGrade +=
                        s.getFirstSemGrade();

                nonDropoutCount++;
            }
        }


        System.out.println(
                "Dropout Student Average 1st Semester Grade: "
                        + df.format(
                        dropoutGrade / dropoutCount
                )
        );


        System.out.println(
                "Non-Dropout Student Average 1st Semester Grade: "
                        + df.format(
                        nonDropoutGrade / nonDropoutCount
                )
        );
    }
}