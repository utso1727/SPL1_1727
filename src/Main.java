import data.CSVReader;
import model.Student;
import statistics.Statistics;

import java.text.DecimalFormat;
import java.util.ArrayList;
import preprocessing.FeatureSelector;
import preprocessing.DataSplitter;
import preprocessing.Encoder;
import preprocessing.StandardScaler;
import ml.LogisticRegression;
import ml.ModelEvaluator;

public class Main {
    private static int encodeTarget(String target) {

        switch (target) {

            case "Dropout":
                return 0;

            case "Enrolled":
                return 1;

            case "Graduate":
                return 2;

            default:
                throw new IllegalArgumentException(
                        "Unknown target: " + target
                );
        }
    }

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
        // =====================================================
// DATA SPLITTING
// =====================================================

        System.out.println();
        System.out.println("DATA SPLITTING");
        System.out.println("-------------------");

        DataSplitter splitter =
                new DataSplitter(students, 0.80, 42);

        ArrayList<Student> trainingData =
                splitter.getTrainingData();

        ArrayList<Student> testingData =
                splitter.getTestingData();

        System.out.println(
                "Training Students: " + trainingData.size()
        );

        System.out.println(
                "Testing Students : " + testingData.size()
        );

        System.out.println(
                "Total Students   : "
                        + (trainingData.size() + testingData.size())
        );


// =====================================================
// TRAINING CLASS DISTRIBUTION
// =====================================================

        System.out.println();
        System.out.println("TRAINING CLASS DISTRIBUTION");
        System.out.println("---------------------------");

        System.out.println(
                "Dropout  : " +
                        statistics.countDropoutStudents(trainingData)
        );

        System.out.println(
                "Enrolled : " +
                        statistics.countEnrolledStudents(trainingData)
        );

        System.out.println(
                "Graduate : " +
                        statistics.countGraduateStudents(trainingData)
        );


// =====================================================
// TESTING CLASS DISTRIBUTION
// =====================================================

        System.out.println();
        System.out.println("TESTING CLASS DISTRIBUTION");
        System.out.println("--------------------------");

        System.out.println(
                "Dropout  : " +
                        statistics.countDropoutStudents(testingData)
        );

        System.out.println(
                "Enrolled : " +
                        statistics.countEnrolledStudents(testingData)
        );

        System.out.println(
                "Graduate : " +
                        statistics.countGraduateStudents(testingData)
        );

        // =====================================================
// CATEGORICAL ENCODING
// =====================================================

        System.out.println();
        System.out.println("CATEGORICAL ENCODING");
        System.out.println("--------------------");

// Create FeatureSelector
// Reuse your existing featureSelector variable

// Extract features from training data
        double[][] trainingFeatures =
                featureSelector.selectAllFeatures(trainingData);

// Extract features from testing data
        double[][] testingFeatures =
                featureSelector.selectAllFeatures(testingData);


// =====================================================
// CREATE AND FIT ENCODER
// =====================================================

        Encoder encoder = new Encoder();

// Fit only on training data
        encoder.fit(trainingFeatures);


// =====================================================
// TRANSFORM TRAINING AND TESTING FEATURES
// =====================================================

        double[][] encodedTraining =
                encoder.transform(trainingFeatures);

        double[][] encodedTesting =
                encoder.transform(testingFeatures);


// =====================================================
// PRINT ENCODING INFORMATION
// =====================================================

        System.out.println(
                "Original Features: "
                        + FeatureSelector.FEATURE_COUNT
        );

        System.out.println(
                "Course Categories: "
                        + encoder.getCourseCategoryCount()
        );

        System.out.println(
                "Encoded Features: "
                        + encoder.getEncodedFeatureCount()
        );

        System.out.println(
                "Training Rows: "
                        + encodedTraining.length
        );

        System.out.println(
                "Testing Rows: "
                        + encodedTesting.length
        );

        System.out.println(
                "Training Feature Columns: "
                        + encodedTraining[0].length
        );

        System.out.println(
                "Testing Feature Columns: "
                        + encodedTesting[0].length
        );


// =====================================================
// DISPLAY COURSE CATEGORIES
// =====================================================

        System.out.println();
        System.out.println("COURSE CATEGORIES");
        System.out.println("-----------------");

        System.out.println(
                encoder.getCourseCategories()
        );


// =====================================================
// VERIFY FIRST TRAINING STUDENT
// =====================================================

        System.out.println();
        System.out.println("FIRST TRAINING STUDENT");
        System.out.println("----------------------");

        System.out.println(
                "Original Course Code: "
                        + trainingFeatures[0][1]
        );

        System.out.println(
                "Original Feature Count: "
                        + trainingFeatures[0].length
        );

        System.out.println(
                "Encoded Feature Count: "
                        + encodedTraining[0].length
        );


// =====================================================
// VERIFY ONE-HOT ENCODING
// =====================================================

        int courseCount = encoder.getCourseCategoryCount();

        int activeCourseColumns = 0;

        for (int i = 1; i <= courseCount; i++) {

            if (encodedTraining[0][i] == 1.0) {
                activeCourseColumns++;
            }
        }

        System.out.println(
                "Active Course Columns: "
                        + activeCourseColumns
        );

        if (activeCourseColumns != 1) {

            throw new IllegalStateException(
                    "One-hot encoding validation failed."
            );
        }

        System.out.println(
                "One-Hot Encoding Validation: PASSED"
        );
        // =====================================================
// FEATURE STANDARDIZATION
// =====================================================

        System.out.println();
        System.out.println("FEATURE STANDARDIZATION");
        System.out.println("-----------------------");

// Create StandardScaler
        StandardScaler scaler = new StandardScaler();


// =====================================================
// FIT SCALER USING TRAINING DATA ONLY
// =====================================================

        scaler.fit(encodedTraining);


// =====================================================
// TRANSFORM TRAINING AND TESTING DATA
// =====================================================

        double[][] scaledTraining =
                scaler.transform(encodedTraining);

        double[][] scaledTesting =
                scaler.transform(encodedTesting);


// =====================================================
// PRINT DATASET INFORMATION
// =====================================================

        System.out.println(
                "Training Rows: " + scaledTraining.length
        );

        System.out.println(
                "Testing Rows: " + scaledTesting.length
        );

        System.out.println(
                "Training Columns: " + scaledTraining[0].length
        );

        System.out.println(
                "Testing Columns: " + scaledTesting[0].length
        );


// =====================================================
// COMPARE ORIGINAL AND STANDARDIZED VALUES
// =====================================================

        System.out.println();
        System.out.println("FIRST TRAINING STUDENT");
        System.out.println("----------------------");

        System.out.println(
                "Original Admission Grade: "
                        + encodedTraining[0][20]
        );

        System.out.println(
                "Scaled Admission Grade: "
                        + df.format(scaledTraining[0][20])
        );

        System.out.println(
                "Original Age: "
                        + encodedTraining[0][24]
        );

        System.out.println(
                "Scaled Age: "
                        + df.format(scaledTraining[0][24])
        );


// =====================================================
// DISPLAY TRAINING STATISTICS
// =====================================================

        System.out.println();
        System.out.println("TRAINING SCALER PARAMETERS");
        System.out.println("--------------------------");

        System.out.println(
                "Admission Grade Mean: "
                        + df.format(scaler.getMean(20))
        );

        System.out.println(
                "Admission Grade Std Dev: "
                        + df.format(scaler.getStandardDeviation(20))
        );

        System.out.println(
                "Age Mean: "
                        + df.format(scaler.getMean(24))
        );

        System.out.println(
                "Age Std Dev: "
                        + df.format(scaler.getStandardDeviation(24))
        );


// =====================================================
// VERIFY BINARY FEATURES REMAIN UNCHANGED
// =====================================================

        boolean binaryUnchanged = true;

        int[] binaryColumns = {18, 21, 22, 23};

        for (int i = 0; i < encodedTraining.length; i++) {

            for (int column : binaryColumns) {

                if (encodedTraining[i][column]
                        != scaledTraining[i][column]) {

                    binaryUnchanged = false;
                }
            }
        }

        System.out.println();
        System.out.println(
                "Binary Features Unchanged: "
                        + binaryUnchanged
        );


// =====================================================
// VERIFY SCALING
// =====================================================

        if (scaledTraining.length != 3539 ||
                scaledTesting.length != 885) {

            throw new IllegalStateException(
                    "Data size validation failed."
            );
        }

        if (scaledTraining[0].length != 31 ||
                scaledTesting[0].length != 31) {

            throw new IllegalStateException(
                    "Feature count validation failed."
            );
        }

        if (!binaryUnchanged) {

            throw new IllegalStateException(
                    "Binary feature validation failed."
            );
        }

        System.out.println(
                "Standardization Validation: PASSED"
        );

        // =====================================================
// PREPARE TRAINING TARGET LABELS
// =====================================================

        System.out.println();
        System.out.println("TARGET ENCODING");
        System.out.println("--------------------");

        int[] trainingLabels = new int[trainingData.size()];

        for (int i = 0; i < trainingData.size(); i++) {

            trainingLabels[i] =
                    encodeTarget(trainingData.get(i).getTarget());
        }

        System.out.println(
                "Training Labels: " + trainingLabels.length
        );

        System.out.println(
                "First Training Label: " + trainingLabels[0]
        );


// =====================================================
// CREATE LOGISTIC REGRESSION MODEL
// =====================================================

        LogisticRegression model = new LogisticRegression(
                scaledTraining[0].length,
                3,
                0.1,
                500
        );


// =====================================================
// TRAIN THE MODEL
// =====================================================

        model.fit(scaledTraining, trainingLabels);


// =====================================================
// TEST PROBABILITY PREDICTION
// =====================================================

        System.out.println();
        System.out.println("TESTING FIRST STUDENT");
        System.out.println("---------------------");

        double[] probabilities =
                model.predictProba(scaledTesting[0]);

        int predictedClass =
                model.predict(scaledTesting[0]);

        String[] classNames = {
                "Dropout",
                "Enrolled",
                "Graduate"
        };

        System.out.println(
                "Dropout Probability: "
                        + df.format(probabilities[0] * 100) + "%"
        );

        System.out.println(
                "Enrolled Probability: "
                        + df.format(probabilities[1] * 100) + "%"
        );

        System.out.println(
                "Graduate Probability: "
                        + df.format(probabilities[2] * 100) + "%"
        );

        System.out.println(
                "Predicted Outcome: "
                        + classNames[predictedClass]
        );


// =====================================================
// VERIFY PROBABILITIES
// =====================================================

        double probabilitySum = 0;

        for (double probability : probabilities) {

            probabilitySum += probability;
        }

        System.out.println(
                "Probability Sum: " + probabilitySum
        );

        if (Math.abs(probabilitySum - 1.0) > 1e-9) {

            throw new IllegalStateException(
                    "Probability validation failed."
            );
        }

        System.out.println(
                "Logistic Regression Prediction: PASSED"
        );
        // =====================================================
// PREPARE TESTING TARGET LABELS
// =====================================================

        System.out.println();
        System.out.println("TESTING TARGET ENCODING");
        System.out.println("-----------------------");

        int[] testingLabels = new int[testingData.size()];

        for (int i = 0; i < testingData.size(); i++) {

            testingLabels[i] =
                    encodeTarget(testingData.get(i).getTarget());
        }

        System.out.println(
                "Testing Labels: " + testingLabels.length
        );

        // =====================================================
// MODEL EVALUATION - ACCURACY
// =====================================================

        System.out.println();
        System.out.println("MODEL EVALUATION");
        System.out.println("-----------------------");

// Create evaluator
        ModelEvaluator evaluator = new ModelEvaluator();

// Calculate accuracy using testing data
        double accuracy = evaluator.calculateAccuracy(
                model,
                scaledTesting,
                testingLabels
        );

// Display result
        System.out.println(
                "Total Testing Students: " + scaledTesting.length
        );

        System.out.printf(
                "Testing Accuracy: %.2f%%%n",
                accuracy * 100
        );

// Validate accuracy range
        if (!Double.isFinite(accuracy) ||
                accuracy < 0 || accuracy > 1) {

            throw new IllegalStateException(
                    "Invalid model accuracy."
            );
        }

        System.out.println(
                "Accuracy Validation: PASSED"
        );
        // =====================================================
// CONFUSION MATRIX
// =====================================================

        System.out.println();
        System.out.println("CONFUSION MATRIX");
        System.out.println("----------------------------");

// Calculate confusion matrix
        int[][] confusionMatrix =
                evaluator.calculateConfusionMatrix(
                        model,
                        scaledTesting,
                        testingLabels
                );


// =====================================================
// DISPLAY CONFUSION MATRIX
// =====================================================

        System.out.println(
                "Rows = Actual, Columns = Predicted"
        );

        System.out.printf(
                "%-12s %10s %10s %10s%n",
                "",
                "Dropout",
                "Enrolled",
                "Graduate"
        );

        for (int i = 0; i < 3; i++) {

            System.out.printf(
                    "%-12s",
                    classNames[i]
            );

            for (int j = 0; j < 3; j++) {

                System.out.printf(
                        "%10d",
                        confusionMatrix[i][j]
                );
            }

            System.out.println();
        }


// =====================================================
// CONFUSION MATRIX VALIDATION
// =====================================================

        int matrixTotal = 0;
        int correctFromMatrix = 0;

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

                matrixTotal += confusionMatrix[i][j];

                if (i == j) {
                    correctFromMatrix += confusionMatrix[i][j];
                }
            }
        }

        System.out.println();
        System.out.println(
                "Confusion Matrix Total: " + matrixTotal
        );

        if (matrixTotal != testingData.size()) {

            throw new IllegalStateException(
                    "Confusion matrix total does not match testing data."
            );
        }

        double matrixAccuracy =
                (double) correctFromMatrix / matrixTotal;

        if (Math.abs(matrixAccuracy - accuracy) > 1e-9) {

            throw new IllegalStateException(
                    "Confusion matrix accuracy does not match model accuracy."
            );
        }

        System.out.println(
                "Confusion Matrix Validation: PASSED"
        );
    }
}