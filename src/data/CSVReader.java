package data;

import model.Student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class CSVReader {

    public ArrayList<Student> readStudents(String fileName) {

        ArrayList<Student> students = new ArrayList<>();

        try {

            BufferedReader br =
                    new BufferedReader(new FileReader(fileName));

            String line;

            // Skip header
            br.readLine();

            int id = 1;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(";");

                // Make sure the row has all expected columns
                if (data.length < 37) {
                    continue;
                }

                double previousQualificationGrade =
                        Double.parseDouble(data[6]);

                double admissionGrade =
                        Double.parseDouble(data[12]);

                int debtor =
                        Integer.parseInt(data[15]);

                int tuitionFeesUpToDate =
                        Integer.parseInt(data[16]);

                int scholarshipHolder =
                        Integer.parseInt(data[18]);

                int ageAtEnrollment =
                        Integer.parseInt(data[19]);

                int firstSemEnrolled =
                        Integer.parseInt(data[22]);

                int firstSemEvaluations =
                        Integer.parseInt(data[23]);

                int firstSemApproved =
                        Integer.parseInt(data[24]);

                double firstSemGrade =
                        Double.parseDouble(data[25]);

                int secondSemEnrolled =
                        Integer.parseInt(data[28]);

                int secondSemEvaluations =
                        Integer.parseInt(data[29]);

                int secondSemApproved =
                        Integer.parseInt(data[30]);

                double secondSemGrade =
                        Double.parseDouble(data[31]);


                /*
                 * Original target:
                 *
                 * Dropout
                 * Graduate
                 * Enrolled
                 *
                 * For our binary dropout prediction:
                 *
                 * Dropout  = 1
                 * Others   = 0
                 */

                int dropout;

                if (data[36].trim().equalsIgnoreCase("Dropout")) {
                    dropout = 1;
                } else {
                    dropout = 0;
                }


                Student student = new Student(
                        id,
                        previousQualificationGrade,
                        admissionGrade,
                        debtor,
                        tuitionFeesUpToDate,
                        scholarshipHolder,
                        ageAtEnrollment,
                        firstSemEnrolled,
                        firstSemEvaluations,
                        firstSemApproved,
                        firstSemGrade,
                        secondSemEnrolled,
                        secondSemEvaluations,
                        secondSemApproved,
                        secondSemGrade,
                        dropout
                );

                students.add(student);

                id++;
            }

            br.close();

        } catch (Exception e) {

            System.out.println("Error reading dataset: "
                    + e.getMessage());
        }

        return students;
    }
}