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
}