package test;

import ml.LogisticRegression;

public class LogisticRegressionGradientTest {

    private static final double EPSILON = 1e-5;
    private static final double TOLERANCE = 1e-6;
    private static final double LEARNING_RATE = 0.1;

    // =====================================================
    // CALCULATE AVERAGE CROSS-ENTROPY LOSS
    // =====================================================

    private static double calculateLoss(
            double[][] X,
            int[] y,
            double[][] weights,
            double[] biases
    ) {

        int sampleCount = X.length;
        int classCount = biases.length;

        double totalLoss = 0.0;

        for (int i = 0; i < sampleCount; i++) {

            double[] scores = new double[classCount];

            for (int k = 0; k < classCount; k++) {

                double score = biases[k];

                for (int j = 0; j < X[i].length; j++) {

                    score += weights[k][j] * X[i][j];
                }

                scores[k] = score;
            }

            double maxScore = scores[0];

            for (int k = 1; k < classCount; k++) {

                maxScore = Math.max(maxScore, scores[k]);
            }

            double expSum = 0.0;

            for (int k = 0; k < classCount; k++) {

                expSum += Math.exp(scores[k] - maxScore);
            }

            totalLoss += maxScore
                    + Math.log(expSum)
                    - scores[y[i]];
        }

        return totalLoss / sampleCount;
    }


    // =====================================================
    // COMPARE GRADIENT VALUES
    // =====================================================

    private static void checkGradient(
            String name,
            double numericalGradient,
            double updateGradient
    ) {

        double difference =
                Math.abs(numericalGradient - updateGradient);

        if (!Double.isFinite(difference)
                || difference > TOLERANCE) {

            throw new AssertionError(
                    name + " FAILED. Numerical: "
                            + numericalGradient
                            + ", Update: " + updateGradient
            );
        }

        System.out.printf(
                "PASSED: %-12s Difference: %.10f%n",
                name,
                difference
        );
    }


    // =====================================================
    // MAIN TEST
    // =====================================================

    public static void main(String[] args) {

        System.out.println();
        System.out.println("====================================");
        System.out.println(" LOGISTIC REGRESSION GRADIENT TEST");
        System.out.println("====================================");


        // Small controlled dataset
        double[][] X = {
                {1.0, 0.5},
                {-0.8, 1.2},
                {0.2, -1.5},
                {2.0, -0.5},
                {-1.1, -0.3}
        };

        int[] y = {
                0, 1, 2, 0, 1
        };

        int featureCount = 2;
        int classCount = 3;


        // =====================================================
        // TRAIN FOR EXACTLY ONE EPOCH
        // =====================================================

        LogisticRegression model =
                new LogisticRegression(
                        featureCount,
                        classCount,
                        LEARNING_RATE,
                        1
                );

        model.fit(X, y);


        // =====================================================
        // INITIAL PARAMETERS ARE ALL ZERO
        // =====================================================

        double[][] weights =
                new double[classCount][featureCount];

        double[] biases =
                new double[classCount];


        // =====================================================
        // CHECK WEIGHT GRADIENTS
        // =====================================================

        System.out.println();
        System.out.println("WEIGHT GRADIENT TESTS");
        System.out.println("---------------------");

        for (int k = 0; k < classCount; k++) {

            for (int j = 0; j < featureCount; j++) {

                // Positive perturbation
                weights[k][j] = EPSILON;

                double lossPlus =
                        calculateLoss(X, y, weights, biases);

                // Negative perturbation
                weights[k][j] = -EPSILON;

                double lossMinus =
                        calculateLoss(X, y, weights, biases);

                // Restore original weight
                weights[k][j] = 0.0;

                // Numerical gradient
                double numericalGradient =
                        (lossPlus - lossMinus)
                                / (2 * EPSILON);

                // Gradient inferred from model update
                double updateGradient =
                        -model.getWeight(k, j)
                                / LEARNING_RATE;

                checkGradient(
                        "Weight " + k + "," + j,
                        numericalGradient,
                        updateGradient
                );
            }
        }


        // =====================================================
        // CHECK BIAS GRADIENTS
        // =====================================================

        System.out.println();
        System.out.println("BIAS GRADIENT TESTS");
        System.out.println("-------------------");

        for (int k = 0; k < classCount; k++) {

            // Positive perturbation
            biases[k] = EPSILON;

            double lossPlus =
                    calculateLoss(X, y, weights, biases);

            // Negative perturbation
            biases[k] = -EPSILON;

            double lossMinus =
                    calculateLoss(X, y, weights, biases);

            // Restore original bias
            biases[k] = 0.0;

            // Numerical gradient
            double numericalGradient =
                    (lossPlus - lossMinus)
                            / (2 * EPSILON);

            // Gradient inferred from model update
            double updateGradient =
                    -model.getBias(k)
                            / LEARNING_RATE;

            checkGradient(
                    "Bias " + k,
                    numericalGradient,
                    updateGradient
            );
        }


        // =====================================================
        // FINAL RESULT
        // =====================================================

        System.out.println();
        System.out.println("====================================");
        System.out.println(" ALL GRADIENT CHECKS PASSED");
        System.out.println("====================================");
    }
}