package ml;

public class ModelEvaluator {

    // =====================================================
    // CALCULATE MODEL ACCURACY
    // =====================================================

    public double calculateAccuracy(
            LogisticRegression model,
            double[][] testingFeatures,
            int[] testingLabels
    ) {

        // Validate input
        if (model == null ||
                testingFeatures == null ||
                testingLabels == null ||
                testingFeatures.length == 0 ||
                testingFeatures.length != testingLabels.length) {

            throw new IllegalArgumentException(
                    "Invalid testing data."
            );
        }

        int correctPredictions = 0;

        // Check every testing student
        for (int i = 0; i < testingFeatures.length; i++) {

            // Get actual outcome
            int actual = testingLabels[i];

            if (actual < 0 || actual >= 3) {
                throw new IllegalArgumentException(
                        "Invalid testing label at row " + i
                );
            }

            // Predict outcome using trained model
            int predicted = model.predict(testingFeatures[i]);

            // Compare actual and predicted outcomes
            if (actual == predicted) {
                correctPredictions++;
            }
        }

        // Calculate accuracy
        double accuracy =
                (double) correctPredictions / testingFeatures.length;

        return accuracy;
    }
    // =====================================================
// CALCULATE CONFUSION MATRIX
// =====================================================

    public int[][] calculateConfusionMatrix(
            LogisticRegression model,
            double[][] testingFeatures,
            int[] testingLabels
    ) {

        // Validate input
        if (model == null ||
                testingFeatures == null ||
                testingLabels == null ||
                testingFeatures.length == 0 ||
                testingFeatures.length != testingLabels.length) {

            throw new IllegalArgumentException(
                    "Invalid testing data."
            );
        }

        // Three target classes
        int classCount = 3;

        // Create 3x3 confusion matrix
        int[][] confusionMatrix =
                new int[classCount][classCount];


        // Process every testing student
        for (int i = 0; i < testingFeatures.length; i++) {

            // Actual outcome
            int actual = testingLabels[i];

            if (actual < 0 || actual >= classCount) {
                throw new IllegalArgumentException(
                        "Invalid actual label at row " + i
                );
            }

            // Predicted outcome
            int predicted =
                    model.predict(testingFeatures[i]);

            if (predicted < 0 || predicted >= classCount) {
                throw new IllegalStateException(
                        "Invalid predicted class at row " + i
                );
            }

            // Update confusion matrix
            confusionMatrix[actual][predicted]++;
        }

        return confusionMatrix;
    }
    // =====================================================
// CALCULATE PRECISION FOR A TARGET CLASS
// =====================================================

    public double calculatePrecision(
            int[][] confusionMatrix,
            int classIndex
    ) {

        if (confusionMatrix == null ||
                confusionMatrix.length != 3) {

            throw new IllegalArgumentException(
                    "Expected a 3x3 confusion matrix."
            );
        }

        if (classIndex < 0 || classIndex >= 3) {

            throw new IllegalArgumentException(
                    "Invalid class index."
            );
        }

        // Validate confusion matrix
        for (int i = 0; i < 3; i++) {

            if (confusionMatrix[i] == null ||
                    confusionMatrix[i].length != 3) {

                throw new IllegalArgumentException(
                        "Invalid confusion matrix dimensions."
                );
            }

            for (int j = 0; j < 3; j++) {

                if (confusionMatrix[i][j] < 0) {

                    throw new IllegalArgumentException(
                            "Confusion matrix cannot contain negative values."
                    );
                }
            }
        }

        // Correct predictions for this class
        int truePositive =
                confusionMatrix[classIndex][classIndex];

        // Total predictions made for this class
        int totalPredicted = 0;

        for (int i = 0; i < 3; i++) {

            totalPredicted += confusionMatrix[i][classIndex];
        }

        // Avoid division by zero
        if (totalPredicted == 0) {
            return 0.0;
        }

        return (double) truePositive / totalPredicted;
    }
    // =====================================================
// CALCULATE RECALL FOR A TARGET CLASS
// =====================================================

    public double calculateRecall(
            int[][] confusionMatrix,
            int classIndex
    ) {

        // Validate confusion matrix
        if (confusionMatrix == null ||
                confusionMatrix.length != 3) {

            throw new IllegalArgumentException(
                    "Expected a 3x3 confusion matrix."
            );
        }

        // Validate class index
        if (classIndex < 0 || classIndex >= 3) {

            throw new IllegalArgumentException(
                    "Invalid class index."
            );
        }

        // Validate matrix dimensions and values
        for (int i = 0; i < 3; i++) {

            if (confusionMatrix[i] == null ||
                    confusionMatrix[i].length != 3) {

                throw new IllegalArgumentException(
                        "Invalid confusion matrix dimensions."
                );
            }

            for (int j = 0; j < 3; j++) {

                if (confusionMatrix[i][j] < 0) {

                    throw new IllegalArgumentException(
                            "Confusion matrix cannot contain negative values."
                    );
                }
            }
        }

        // Correct predictions for this class
        int truePositive =
                confusionMatrix[classIndex][classIndex];

        // Total actual students belonging to this class
        int totalActual = 0;

        for (int j = 0; j < 3; j++) {

            totalActual += confusionMatrix[classIndex][j];
        }

        // Avoid division by zero
        if (totalActual == 0) {
            return 0.0;
        }

        // Calculate recall
        return (double) truePositive / totalActual;
    }
    // =====================================================
// CALCULATE F1-SCORE
// =====================================================

    public double calculateF1Score(
            double precision,
            double recall
    ) {

        // Validate precision
        if (!Double.isFinite(precision) ||
                precision < 0 || precision > 1) {

            throw new IllegalArgumentException(
                    "Precision must be between 0 and 1."
            );
        }

        // Validate recall
        if (!Double.isFinite(recall) ||
                recall < 0 || recall > 1) {

            throw new IllegalArgumentException(
                    "Recall must be between 0 and 1."
            );
        }

        // Avoid division by zero
        if (precision + recall == 0) {
            return 0.0;
        }

        // Calculate F1-score
        double f1Score =
                (2 * precision * recall)
                        / (precision + recall);

        return f1Score;
    }
}