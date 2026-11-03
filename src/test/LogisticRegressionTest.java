package test;

import ml.LogisticRegression;

public class LogisticRegressionTest {

    private static final double TOLERANCE = 1e-9;


    // =====================================================
    // TEST PROBABILITY DISTRIBUTION
    // =====================================================

    private static void testProbabilities(double[] probabilities) {

        if (probabilities.length != 3) {
            throw new AssertionError(
                    "Expected probabilities for 3 classes."
            );
        }

        double sum = 0;

        for (double probability : probabilities) {

            if (!Double.isFinite(probability)
                    || probability < 0
                    || probability > 1) {

                throw new AssertionError(
                        "Invalid Softmax probability."
                );
            }

            sum += probability;
        }

        if (Math.abs(sum - 1.0) > TOLERANCE) {

            throw new AssertionError(
                    "Probabilities do not sum to 1."
            );
        }

        System.out.println(
                "PASSED: Softmax Probability Validation"
        );
    }


    // =====================================================
    // TEST EXPECTED PREDICTION
    // =====================================================

    private static void testPrediction(
            LogisticRegression model,
            double[] features,
            int expectedClass
    ) {

        int predictedClass = model.predict(features);

        if (predictedClass != expectedClass) {

            throw new AssertionError(
                    "Expected Class: " + expectedClass
                            + ", Predicted Class: " + predictedClass
            );
        }

        System.out.println(
                "PASSED: Prediction for Class " + expectedClass
        );
    }


    // =====================================================
    // MAIN TEST METHOD
    // =====================================================

    public static void main(String[] args) {

        System.out.println();
        System.out.println("====================================");
        System.out.println(" LOGISTIC REGRESSION UNIT TESTS");
        System.out.println("====================================");


        // =====================================================
        // CREATE A SMALL TRAINING DATASET
        // =====================================================

        double[][] X = {

                // Class 0
                {2, 0},
                {3, 0},
                {2, 1},

                // Class 1
                {0, 2},
                {0, 3},
                {1, 2},

                // Class 2
                {-2, -2},
                {-3, -2},
                {-2, -3}
        };

        int[] y = {
                0, 0, 0,
                1, 1, 1,
                2, 2, 2
        };


        // =====================================================
        // CREATE LOGISTIC REGRESSION MODEL
        // =====================================================

        LogisticRegression model =
                new LogisticRegression(
                        2,      // Features
                        3,      // Classes
                        0.1,    // Learning rate
                        300     // Epochs
                );


        // =====================================================
        // TRAIN MODEL
        // =====================================================

        model.fit(X, y);


        // =====================================================
        // TEST THREE-CLASS PREDICTIONS
        // =====================================================

        System.out.println();
        System.out.println("THREE-CLASS PREDICTION TESTS");
        System.out.println("----------------------------");

        testPrediction(
                model,
                new double[]{2.5, 0.2},
                0
        );

        testPrediction(
                model,
                new double[]{0.2, 2.5},
                1
        );

        testPrediction(
                model,
                new double[]{-2.5, -2.5},
                2
        );


        // =====================================================
        // TEST SOFTMAX PROBABILITIES
        // =====================================================

        System.out.println();
        System.out.println("SOFTMAX TESTS");
        System.out.println("----------------------------");

        double[] probabilities =
                model.predictProba(
                        new double[]{2.5, 0.2}
                );

        testProbabilities(probabilities);


        // =====================================================
        // TEST NUMERICAL STABILITY
        // =====================================================

        System.out.println();
        System.out.println("NUMERICAL STABILITY TEST");
        System.out.println("----------------------------");

        double[] largeInputProbabilities =
                model.predictProba(
                        new double[]{1000000.0, 0.0}
                );

        testProbabilities(largeInputProbabilities);


        // =====================================================
        // FINAL RESULT
        // =====================================================

        System.out.println();
        System.out.println("====================================");
        System.out.println(" ALL LOGISTIC REGRESSION TESTS PASSED");
        System.out.println("====================================");
    }
}