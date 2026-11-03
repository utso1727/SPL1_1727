package test;

import data.CSVReader;
import model.Student;
import preprocessing.DataSplitter;
import preprocessing.FeatureSelector;
import preprocessing.Encoder;
import preprocessing.StandardScaler;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public class PreprocessingPipelineTest {

    private static final double TOLERANCE = 1e-9;


    // =====================================================
    // ASSERT CONDITION
    // =====================================================

    private static void check(
            boolean condition,
            String message
    ) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }


    // =====================================================
    // VERIFY SELECTED FEATURE VALUES AND ORDER
    // =====================================================

    private static void verifyFeatures(
            ArrayList<Student> students,
            double[][] features
    ) {

        check(
                students.size() == features.length,
                "Feature rows do not match student count."
        );

        for (int i = 0; i < students.size(); i++) {

            Student s = students.get(i);

            double[] expected = {
                    s.getApplicationOrder(),
                    s.getCourse(),
                    s.getDaytimeEveningAttendance(),
                    s.getPreviousQualificationGrade(),
                    s.getAdmissionGrade(),
                    s.getDebtor(),
                    s.getTuitionFeesUpToDate(),
                    s.getScholarshipHolder(),
                    s.getAgeAtEnrollment(),
                    s.getCurricularUnits1stSemCredited(),
                    s.getCurricularUnits1stSemEnrolled(),
                    s.getCurricularUnits1stSemEvaluations(),
                    s.getCurricularUnits1stSemApproved(),
                    s.getCurricularUnits1stSemGrade(),
                    s.getCurricularUnits1stSemWithoutEvaluations()
            };

            check(
                    features[i] != null &&
                            features[i].length == expected.length,
                    "Invalid feature count at row " + i
            );

            for (int j = 0; j < expected.length; j++) {

                check(
                        Double.compare(
                                features[i][j],
                                expected[j]
                        ) == 0,
                        "Feature mismatch at row " + i +
                                ", column " + j
                );
            }
        }
    }


    // =====================================================
    // VERIFY COURSE ONE-HOT ENCODING
    // =====================================================

    private static void verifyEncoding(
            double[][] original,
            double[][] encoded,
            List<Integer> courses
    ) {

        check(
                original.length == encoded.length,
                "Encoding changed the row count."
        );

        for (int i = 0; i < original.length; i++) {

            int course = (int) original[i][1];
            int expectedPosition = courses.indexOf(course);

            check(
                    expectedPosition >= 0,
                    "Unknown course at row " + i
            );

            int active = 0;

            for (int j = 0; j < courses.size(); j++) {

                double value = encoded[i][j + 1];

                check(
                        value == 0.0 || value == 1.0,
                        "Invalid one-hot value."
                );

                if (value == 1.0) {

                    active++;

                    check(
                            j == expectedPosition,
                            "Incorrect course mapping."
                    );
                }
            }

            check(
                    active == 1,
                    "Expected exactly one active course."
            );

            // Verify all remaining features
            for (int j = 2; j < original[i].length; j++) {

                int encodedIndex = courses.size() + j - 1;

                check(
                        Double.compare(
                                original[i][j],
                                encoded[i][encodedIndex]
                        ) == 0,
                        "Other feature changed during encoding."
                );
            }
        }
    }


    // =====================================================
    // MAIN INTEGRATION TEST
    // =====================================================

    public static void main(String[] args) {

        System.out.println();
        System.out.println("PREPROCESSING PIPELINE TEST");
        System.out.println("---------------------------");


        // =====================================================
        // TEST 1: LOAD DATASET
        // =====================================================

        ArrayList<Student> students =
                CSVReader.readStudents("data/students.csv");

        check(
                students.size() == 4424,
                "Expected 4424 students."
        );

        System.out.println("PASSED: Dataset Loading");


        // =====================================================
        // TEST 2: TRAIN/TEST SPLITTING
        // =====================================================

        DataSplitter splitter =
                new DataSplitter(students, 0.80, 42);

        ArrayList<Student> training =
                splitter.getTrainingData();

        ArrayList<Student> testing =
                splitter.getTestingData();

        check(
                training.size() == 3539 &&
                        testing.size() == 885,
                "Unexpected training/testing sizes."
        );

        // Identity-based checks prevent counting equal-valued
        // but distinct Student objects as the same object.

        IdentityHashMap<Student, Boolean> original =
                new IdentityHashMap<>();

        for (Student s : students) {
            check(
                    original.put(s, true) == null,
                    "Duplicate object in source dataset."
            );
        }

        IdentityHashMap<Student, Boolean> seen =
                new IdentityHashMap<>();

        for (Student s : training) {

            check(original.containsKey(s),
                    "Unknown training student.");

            check(seen.put(s, true) == null,
                    "Duplicate training student.");
        }

        for (Student s : testing) {

            check(original.containsKey(s),
                    "Unknown testing student.");

            check(seen.put(s, true) == null,
                    "Student repeated across partitions.");
        }

        check(
                seen.size() == original.size(),
                "Not all students were assigned."
        );

        System.out.println("PASSED: No Train/Test Overlap");


        // =====================================================
        // TEST 3: FEATURE SELECTION
        // =====================================================

        FeatureSelector selector = new FeatureSelector();

        double[][] trainingFeatures =
                selector.selectAllFeatures(training);

        double[][] testingFeatures =
                selector.selectAllFeatures(testing);

        verifyFeatures(training, trainingFeatures);
        verifyFeatures(testing, testingFeatures);

        System.out.println("PASSED: Feature Selection");
        System.out.println("PASSED: Feature Order and Alignment");


        // =====================================================
        // TEST 4: CATEGORICAL ENCODING
        // =====================================================

        Encoder encoder = new Encoder();

        // Learn categories from training data only
        encoder.fit(trainingFeatures);

        double[][] encodedTraining =
                encoder.transform(trainingFeatures);

        double[][] encodedTesting =
                encoder.transform(testingFeatures);

        List<Integer> courses =
                encoder.getCourseCategories();

        verifyEncoding(
                trainingFeatures,
                encodedTraining,
                courses
        );

        verifyEncoding(
                testingFeatures,
                encodedTesting,
                courses
        );

        check(
                encodedTraining[0].length == 31 &&
                        encodedTesting[0].length == 31,
                "Expected 31 encoded features."
        );

        System.out.println("PASSED: Course Encoding");
        System.out.println("PASSED: Encoded Feature Count");


        // =====================================================
        // TEST 5: STANDARDIZATION
        // =====================================================

        StandardScaler scaler = new StandardScaler();

        // Fit using training data only
        scaler.fit(encodedTraining);

        double[][] scaledTraining =
                scaler.transform(encodedTraining);

        double[][] scaledTesting =
                scaler.transform(encodedTesting);

        check(
                scaledTraining.length == training.size() &&
                        scaledTesting.length == testing.size(),
                "Scaling changed the row count."
        );

        // Verify Admission Grade mean
        double sum = 0;

        for (double[] row : encodedTraining) {
            sum += row[20];
        }

        double expectedMean =
                sum / encodedTraining.length;

        check(
                Math.abs(
                        expectedMean - scaler.getMean(20)
                ) < TOLERANCE,
                "Scaler mean does not match training data."
        );

        System.out.println("PASSED: Training-Based Scaling");


        // =====================================================
        // TEST 6: BINARY AND ONE-HOT FEATURES
        // =====================================================

        int[] binaryColumns = {18, 21, 22, 23};

        for (int i = 0; i < encodedTraining.length; i++) {

            // Verify course one-hot columns remain unchanged
            for (int j = 1; j <= courses.size(); j++) {

                check(
                        encodedTraining[i][j] ==
                                scaledTraining[i][j],
                        "One-hot column was modified."
                );
            }

            // Verify original binary features
            for (int column : binaryColumns) {

                check(
                        encodedTraining[i][column] ==
                                scaledTraining[i][column],
                        "Binary feature was modified."
                );
            }
        }

        System.out.println("PASSED: Binary Features Unchanged");
        System.out.println("PASSED: One-Hot Features Unchanged");


        // =====================================================
        // FINAL RESULT
        // =====================================================

        System.out.println();
        System.out.println("====================================");
        System.out.println(" ALL PREPROCESSING TESTS PASSED");
        System.out.println("====================================");
    }
}