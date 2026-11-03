package test;

import ml.LogisticRegression;

public class LogisticRegressionValidationTest {

    // =====================================================
    // VERIFY THAT INVALID INPUT THROWS EXPECTED EXCEPTION
    // =====================================================

    private static void assertThrows(
            String testName,
            Class<? extends RuntimeException> expectedException,
            Runnable action
    ) {

        try {

            action.run();

        } catch (RuntimeException e) {

            if (expectedException.isInstance(e)) {

                System.out.println("PASSED: " + testName);
                return;
            }

            throw new AssertionError(
                    testName + " FAILED. Unexpected exception: "
                            + e.getClass().getSimpleName()
            );
        }

        throw new AssertionError(
                testName + " FAILED. Expected exception was not thrown."
        );
    }


    // =====================================================
    // MAIN TEST METHOD
    // =====================================================

    public static void main(String[] args) {

        System.out.println();
        System.out.println("========================================");
        System.out.println(" LOGISTIC REGRESSION VALIDATION TESTS");
        System.out.println("========================================");


        // =====================================================
        // TEST INVALID CONSTRUCTOR PARAMETERS
        // =====================================================

        System.out.println();
        System.out.println("CONSTRUCTOR VALIDATION TESTS");
        System.out.println("----------------------------");

        assertThrows(
                "Zero Feature Count",
                IllegalArgumentException.class,
                () -> new LogisticRegression(0, 3, 0.1, 100)
        );

        assertThrows(
                "Invalid Class Count",
                IllegalArgumentException.class,
                () -> new LogisticRegression(2, 1, 0.1, 100)
        );

        assertThrows(
                "Negative Learning Rate",
                IllegalArgumentException.class,
                () -> new LogisticRegression(2, 3, -0.1, 100)
        );

        assertThrows(
                "Zero Learning Rate",
                IllegalArgumentException.class,
                () -> new LogisticRegression(2, 3, 0.0, 100)
        );

        assertThrows(
                "Zero Epochs",
                IllegalArgumentException.class,
                () -> new LogisticRegression(2, 3, 0.1, 0)
        );


        // =====================================================
        // CREATE MODEL
        // =====================================================

        LogisticRegression model =
                new LogisticRegression(2, 3, 0.1, 100);


        // =====================================================
        // TEST PREDICTION BEFORE TRAINING
        // =====================================================

        System.out.println();
        System.out.println("UNTRAINED MODEL TESTS");
        System.out.println("---------------------");

        assertThrows(
                "Prediction Before Training",
                IllegalStateException.class,
                () -> model.predict(new double[]{1.0, 2.0})
        );

        assertThrows(
                "Probability Before Training",
                IllegalStateException.class,
                () -> model.predictProba(new double[]{1.0, 2.0})
        );

        assertThrows(
                "Weight Access Before Training",
                IllegalStateException.class,
                () -> model.getWeight(0, 0)
        );


        // =====================================================
        // TEST INVALID TRAINING DATA
        // =====================================================

        System.out.println();
        System.out.println("TRAINING DATA VALIDATION TESTS");
        System.out.println("------------------------------");

        assertThrows(
                "Empty Training Dataset",
                IllegalArgumentException.class,
                () -> model.fit(new double[0][0], new int[0])
        );

        assertThrows(
                "Mismatched Features and Labels",
                IllegalArgumentException.class,
                () -> model.fit(
                        new double[][]{{1.0, 2.0}},
                        new int[0]
                )
        );

        assertThrows(
                "Incorrect Feature Count",
                IllegalArgumentException.class,
                () -> model.fit(
                        new double[][]{{1.0}},
                        new int[]{0}
                )
        );

        assertThrows(
                "Invalid Target Label",
                IllegalArgumentException.class,
                () -> model.fit(
                        new double[][]{{1.0, 2.0}},
                        new int[]{3}
                )
        );

        assertThrows(
                "NaN Training Feature",
                IllegalArgumentException.class,
                () -> model.fit(
                        new double[][]{{Double.NaN, 2.0}},
                        new int[]{0}
                )
        );

        assertThrows(
                "Infinite Training Feature",
                IllegalArgumentException.class,
                () -> model.fit(
                        new double[][]{{Double.POSITIVE_INFINITY, 2.0}},
                        new int[]{0}
                )
        );


        // =====================================================
        // TRAIN MODEL USING VALID DATA
        // =====================================================

        System.out.println();
        System.out.println("TRAINING WITH VALID DATA");
        System.out.println("------------------------");

        double[][] X = {
                {2.0, 0.0},
                {0.0, 2.0},
                {-2.0, -2.0}
        };

        int[] y = {0, 1, 2};

        model.fit(X, y);


        // =====================================================
        // TEST INVALID PREDICTION INPUTS
        // =====================================================

        System.out.println();
        System.out.println("PREDICTION VALIDATION TESTS");
        System.out.println("---------------------------");

        assertThrows(
                "Null Prediction Features",
                IllegalArgumentException.class,
                () -> model.predict(null)
        );

        assertThrows(
                "Incorrect Prediction Feature Count",
                IllegalArgumentException.class,
                () -> model.predict(new double[]{1.0})
        );

        assertThrows(
                "NaN Prediction Feature",
                IllegalArgumentException.class,
                () -> model.predict(
                        new double[]{Double.NaN, 1.0}
                )
        );

        assertThrows(
                "Infinite Prediction Feature",
                IllegalArgumentException.class,
                () -> model.predict(
                        new double[]{1.0, Double.POSITIVE_INFINITY}
                )
        );


        // =====================================================
        // FINAL RESULT
        // =====================================================

        System.out.println();
        System.out.println("========================================");
        System.out.println(" ALL INPUT VALIDATION TESTS PASSED");
        System.out.println("========================================");
    }
}