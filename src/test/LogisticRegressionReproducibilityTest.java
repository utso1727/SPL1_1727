package test;

import ml.LogisticRegression;

public class LogisticRegressionReproducibilityTest {

    private static final double TOLERANCE = 1e-12;

    // =====================================================
    // COMPARE TWO NUMERICAL VALUES
    // =====================================================

    private static void assertClose(
            String testName,
            double expected,
            double actual
    ) {

        if (!Double.isFinite(expected) ||
                !Double.isFinite(actual) ||
                Math.abs(expected - actual) > TOLERANCE) {

            throw new AssertionError(
                    testName + " FAILED. Expected: "
                            + expected + ", Actual: " + actual
            );
        }
    }


    // =====================================================
    // COMPARE TWO TRAINED MODELS
    // =====================================================

    private static void compareModels(
            LogisticRegression model1,
            LogisticRegression model2,
            int featureCount,
            int classCount,
            double[] testStudent
    ) {

        // Compare every learned weight
        for (int k = 0; k < classCount; k++) {

            for (int j = 0; j < featureCount; j++) {

                assertClose(
                        "Weight " + k + "," + j,
                        model1.getWeight(k, j),
                        model2.getWeight(k, j)
                );
            }
        }

        System.out.println(
                "PASSED: Identical Model Weights"
        );


        // Compare every learned bias
        for (int k = 0; k < classCount; k++) {

            assertClose(
                    "Bias " + k,
                    model1.getBias(k),
                    model2.getBias(k)
            );
        }

        System.out.println(
                "PASSED: Identical Model Biases"
        );


        // Compare predicted probabilities
        double[] probabilities1 =
                model1.predictProba(testStudent);

        double[] probabilities2 =
                model2.predictProba(testStudent);

        for (int k = 0; k < classCount; k++) {

            assertClose(
                    "Probability " + k,
                    probabilities1[k],
                    probabilities2[k]
            );
        }

        System.out.println(
                "PASSED: Identical Predictions"
        );
    }


    // =====================================================
    // MAIN TEST METHOD
    // =====================================================

    public static void main(String[] args) {

        System.out.println();
        System.out.println("====================================");
        System.out.println(" LOGISTIC REGRESSION REPRODUCIBILITY");
        System.out.println("====================================");


        // Small controlled training dataset
        double[][] X = {

                {2, 0},
                {3, 0},
                {2, 1},

                {0, 2},
                {0, 3},
                {1, 2},

                {-2, -2},
                {-3, -2},
                {-2, -3}
        };

        int[] y = {
                0, 0, 0,
                1, 1, 1,
                2, 2, 2
        };


        int featureCount = 2;
        int classCount = 3;

        double learningRate = 0.1;
        int epochs = 100;


        // =====================================================
        // CREATE TWO INDEPENDENT MODELS
        // =====================================================

        LogisticRegression model1 =
                new LogisticRegression(
                        featureCount,
                        classCount,
                        learningRate,
                        epochs
                );

        LogisticRegression model2 =
                new LogisticRegression(
                        featureCount,
                        classCount,
                        learningRate,
                        epochs
                );


        // =====================================================
        // TRAIN BOTH MODELS
        // =====================================================

        System.out.println();
        System.out.println("TRAINING MODEL 1");

        model1.fit(X, y);

        System.out.println();
        System.out.println("TRAINING MODEL 2");

        model2.fit(X, y);


        // =====================================================
        // COMPARE TRAINED MODELS
        // =====================================================

        System.out.println();
        System.out.println("REPRODUCIBILITY TEST");
        System.out.println("--------------------");

        double[] testStudent = {2.5, 0.2};

        compareModels(
                model1,
                model2,
                featureCount,
                classCount,
                testStudent
        );


        // =====================================================
        // VERIFY REPEATED TRAINING
        // =====================================================

        System.out.println();
        System.out.println("REPEATED TRAINING TEST");
        System.out.println("----------------------");

        // Retrain model1 using identical data
        model1.fit(X, y);

        // Compare retrained model1 with model2
        compareModels(
                model1,
                model2,
                featureCount,
                classCount,
                testStudent
        );


        // =====================================================
        // FINAL RESULT
        // =====================================================

        System.out.println();
        System.out.println("====================================");
        System.out.println(" ALL REPRODUCIBILITY TESTS PASSED");
        System.out.println("====================================");
    }
}