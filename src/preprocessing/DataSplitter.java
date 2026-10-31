package preprocessing;

import model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DataSplitter {

    private final ArrayList<Student> trainingData;
    private final ArrayList<Student> testingData;

    public DataSplitter(
            ArrayList<Student> students,
            double trainingRatio,
            long seed
    ) {

        if (students == null || students.isEmpty()) {
            throw new IllegalArgumentException(
                    "Student dataset cannot be empty."
            );
        }

        if (trainingRatio <= 0 || trainingRatio >= 1) {
            throw new IllegalArgumentException(
                    "Training ratio must be between 0 and 1."
            );
        }

        trainingData = new ArrayList<>();
        testingData = new ArrayList<>();

        // Separate students by target class
        ArrayList<Student> dropout = new ArrayList<>();
        ArrayList<Student> enrolled = new ArrayList<>();
        ArrayList<Student> graduate = new ArrayList<>();

        for (Student student : students) {

            switch (student.getTarget()) {

                case "Dropout":
                    dropout.add(student);
                    break;

                case "Enrolled":
                    enrolled.add(student);
                    break;

                case "Graduate":
                    graduate.add(student);
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Unknown target: " + student.getTarget()
                    );
            }
        }

        Random random = new Random(seed);

        // Shuffle each class separately
        Collections.shuffle(dropout, random);
        Collections.shuffle(enrolled, random);
        Collections.shuffle(graduate, random);

        // Split each class into training and testing
        splitClass(dropout, trainingRatio);
        splitClass(enrolled, trainingRatio);
        splitClass(graduate, trainingRatio);

        // Shuffle combined datasets
        Collections.shuffle(trainingData, random);
        Collections.shuffle(testingData, random);
    }


    // ---------------------------------------------------------
    // Split Students of One Target Class
    // ---------------------------------------------------------

    private void splitClass(
            ArrayList<Student> students,
            double trainingRatio
    ) {

        int trainingSize =
                (int) Math.round(students.size() * trainingRatio);

        for (int i = 0; i < students.size(); i++) {

            if (i < trainingSize) {
                trainingData.add(students.get(i));
            } else {
                testingData.add(students.get(i));
            }
        }
    }


    // ---------------------------------------------------------
    // Get Training Dataset
    // ---------------------------------------------------------

    public ArrayList<Student> getTrainingData() {
        return new ArrayList<>(trainingData);
    }


    // ---------------------------------------------------------
    // Get Testing Dataset
    // ---------------------------------------------------------

    public ArrayList<Student> getTestingData() {
        return new ArrayList<>(testingData);
    }
}