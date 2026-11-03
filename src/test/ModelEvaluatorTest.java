package test;

import ml.ModelEvaluator;

public class ModelEvaluatorTest {

    private static final double TOLERANCE = 1e-10;

    // =====================================================
    // VERIFY CALCULATED VALUE
    // =====================================================

    private static void assertClose(
            String testName,
            double expected,
            double actual
    ) {

        if (!Double.isFinite(actual) ||
                Math.abs(expected - actual) > TOLERANCE) {

            throw new AssertionError(
                    testName + " FAILED. Expected: "
                            + expected + ", Actual: " + actual
            );
        }

        System.out.println("PASSED: " + testName);
    }


    // =====================================================
    // VERIFY INVALID INPUT IS REJECTED
    // =====================================================

    private static void assertRejects(
            String testName,
            Runnable action
    ) {

        try {

            action.run();

        } catch (IllegalArgumentException e) {

            System.out.println("PASSED: " + testName);
            return;
        }

        throw new AssertionError(
                testName + " FAILED. Invalid input was accepted."
        );
    }


    // =====================================================
    // MAIN TEST METHOD
    // =====================================================

    public static void main(String[] args) {

        ModelEvaluator evaluator = new ModelEvaluator();


        // =====================================================
        // CREATE A KNOWN CONFUSION MATRIX
        // =====================================================

        int[][] matrix = {

                {4, 1, 1},
                {2, 3, 1},
                {1, 1, 6}

        };

        /*
         * Rows = Actual
         * Columns = Predicted
         *
         *             Dropout Enrolled Graduate
         * Dropout        4       1       1
         * Enrolled       2       3       1
         * Graduate       1       1       6
         *
         * Total Students = 20
         * Correct Predictions = 13
         */


        // =====================================================
        // TEST PRECISION
        // =====================================================

        System.out.println();
        System.out.println("PRECISION TESTS");
        System.out.println("--------------------");

        double[] expectedPrecision = {
                4.0 / 7.0,
                3.0 / 5.0,
                6.0 / 8.0
        };

        double[] calculatedPrecision = new double[3];

        for (int i = 0; i < 3; i++) {

            calculatedPrecision[i] =
                    evaluator.calculatePrecision(matrix, i);

            assertClose(
                    "Precision Class " + i,
                    expectedPrecision[i],
                    calculatedPrecision[i]
            );
        }


        // =====================================================
        // TEST RECALL
        // =====================================================

        System.out.println();
        System.out.println("RECALL TESTS");
        System.out.println("--------------------");

        double[] expectedRecall = {
                4.0 / 6.0,
                3.0 / 6.0,
                6.0 / 8.0
        };

        double[] calculatedRecall = new double[3];

        for (int i = 0; i < 3; i++) {

            calculatedRecall[i] =
                    evaluator.calculateRecall(matrix, i);

            assertClose(
                    "Recall Class " + i,
                    expectedRecall[i],
                    calculatedRecall[i]
            );
        }


        // =====================================================
        // TEST F1-SCORE
        // =====================================================

        System.out.println();
        System.out.println("F1-SCORE TESTS");
        System.out.println("--------------------");

        double[] expectedF1 = {
                8.0 / 13.0,
                6.0 / 11.0,
                12.0 / 16.0
        };

        double[] calculatedF1 = new double[3];

        for (int i = 0; i < 3; i++) {

            calculatedF1[i] =
                    evaluator.calculateF1Score(
                            calculatedPrecision[i],
                            calculatedRecall[i]
                    );

            assertClose(
                    "F1-Score Class " + i,
                    expectedF1[i],
                    calculatedF1[i]
            );
        }


        // =====================================================
        // TEST MACRO-AVERAGES
        // =====================================================

        System.out.println();
        System.out.println("MACRO-AVERAGE TESTS");
        System.out.println("--------------------");

        double expectedMacroPrecision =
                (4.0 / 7.0 + 3.0 / 5.0 + 6.0 / 8.0) / 3;

        double expectedMacroRecall =
                (4.0 / 6.0 + 3.0 / 6.0 + 6.0 / 8.0) / 3;

        double expectedMacroF1 =
                (8.0 / 13.0 + 6.0 / 11.0 + 12.0 / 16.0) / 3;

        assertClose(
                "Macro Precision",
                expectedMacroPrecision,
                evaluator.calculateMacroAverage(calculatedPrecision)
        );

        assertClose(
                "Macro Recall",
                expectedMacroRecall,
                evaluator.calculateMacroAverage(calculatedRecall)
        );

        assertClose(
                "Macro F1-Score",
                expectedMacroF1,
                evaluator.calculateMacroAverage(calculatedF1)
        );


        // =====================================================
        // TEST DIVISION BY ZERO HANDLING
        // =====================================================

        System.out.println();
        System.out.println("ZERO DIVISION TESTS");
        System.out.println("--------------------");

        int[][] emptyMatrix = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        assertClose(
                "Zero Precision",
                0.0,
                evaluator.calculatePrecision(emptyMatrix, 0)
        );

        assertClose(
                "Zero Recall",
                0.0,
                evaluator.calculateRecall(emptyMatrix, 0)
        );

        assertClose(
                "Zero F1-Score",
                0.0,
                evaluator.calculateF1Score(0.0, 0.0)
        );


        // =====================================================
        // TEST INVALID INPUT HANDLING
        // =====================================================

        System.out.println();
        System.out.println("INPUT VALIDATION TESTS");
        System.out.println("--------------------");

        assertRejects(
                "Invalid Class Index",
                () -> evaluator.calculatePrecision(matrix, 3)
        );

        assertRejects(
                "Invalid Recall Index",
                () -> evaluator.calculateRecall(matrix, -1)
        );

        assertRejects(
                "Invalid F1 Precision",
                () -> evaluator.calculateF1Score(1.5, 0.5)
        );

        assertRejects(
                "Empty Macro Scores",
                () -> evaluator.calculateMacroAverage(new double[0])
        );


        // =====================================================
        // FINAL TEST RESULT
        // =====================================================

        System.out.println();
        System.out.println("================================");
        System.out.println(" ALL EVALUATION TESTS PASSED");
        System.out.println("================================");
    }
}