package preprocessing;

public class StandardScaler {

    // Numerical feature indexes after encoding
    private static final int[] NUMERIC_COLUMNS = {
            0, 19, 20, 24, 25, 26, 27, 28, 29, 30
    };

    private double[] means;
    private double[] standardDeviations;

    private boolean fitted = false;


    // =====================================================
    // CALCULATE MEAN AND STANDARD DEVIATION
    // USING TRAINING DATA ONLY
    // =====================================================

    public void fit(double[][] trainingData) {

        validateData(trainingData, false);

        fitted = false;

        int rows = trainingData.length;
        int columns = trainingData[0].length;

        if (columns != 31) {
            throw new IllegalArgumentException(
                    "Expected 31 encoded features, found " + columns
            );
        }

        means = new double[columns];
        standardDeviations = new double[columns];


        // Calculate mean for numerical columns
        for (int column : NUMERIC_COLUMNS) {

            double sum = 0;

            for (int i = 0; i < rows; i++) {

                sum += trainingData[i][column];
            }

            means[column] = sum / rows;
        }


        // Calculate standard deviation
        for (int column : NUMERIC_COLUMNS) {

            double sumSquaredDifference = 0;

            for (int i = 0; i < rows; i++) {

                double difference =
                        trainingData[i][column] - means[column];

                sumSquaredDifference +=
                        difference * difference;
            }

            double variance =
                    sumSquaredDifference / rows;

            standardDeviations[column] =
                    Math.sqrt(variance);

            // Prevent division by zero
            if (standardDeviations[column] == 0) {

                standardDeviations[column] = 1.0;
            }
        }

        fitted = true;
    }


    // =====================================================
    // STANDARDIZE FEATURES
    // =====================================================

    public double[][] transform(double[][] data) {

        if (!fitted) {
            throw new IllegalStateException(
                    "StandardScaler must be fitted first."
            );
        }

        validateData(data, true);

        double[][] scaled =
                new double[data.length][means.length];

        // Copy original values
        for (int i = 0; i < data.length; i++) {

            System.arraycopy(
                    data[i],
                    0,
                    scaled[i],
                    0,
                    means.length
            );
        }


        // Standardize numerical columns only
        for (int i = 0; i < scaled.length; i++) {

            for (int column : NUMERIC_COLUMNS) {

                scaled[i][column] =
                        (scaled[i][column] - means[column])
                                / standardDeviations[column];

                if (!Double.isFinite(scaled[i][column])) {

                    throw new IllegalStateException(
                            "Invalid scaled value at row "
                                    + i + ", column " + column
                    );
                }
            }
        }

        return scaled;
    }


    // =====================================================
    // VALIDATE INPUT DATA
    // =====================================================

    private void validateData(
            double[][] data,
            boolean useFittedColumnCount
    ) {

        if (data == null || data.length == 0) {

            throw new IllegalArgumentException(
                    "Dataset cannot be empty."
            );
        }

        int expectedColumns = useFittedColumnCount
                ? means.length
                : data[0] == null ? -1 : data[0].length;

        if (expectedColumns <= 0) {

            throw new IllegalArgumentException(
                    "Dataset must contain feature columns."
            );
        }

        for (int i = 0; i < data.length; i++) {

            if (data[i] == null ||
                    data[i].length != expectedColumns) {

                throw new IllegalArgumentException(
                        "Invalid feature count at row " + i
                );
            }

            for (double value : data[i]) {

                if (!Double.isFinite(value)) {

                    throw new IllegalArgumentException(
                            "Feature values must be finite."
                    );
                }
            }
        }
    }


    // =====================================================
    // RETURN MEAN OF A FEATURE
    // =====================================================

    public double getMean(int column) {

        if (!fitted) {
            throw new IllegalStateException(
                    "StandardScaler has not been fitted."
            );
        }

        return means[column];
    }


    // =====================================================
    // RETURN STANDARD DEVIATION OF A FEATURE
    // =====================================================

    public double getStandardDeviation(int column) {

        if (!fitted) {
            throw new IllegalStateException(
                    "StandardScaler has not been fitted."
            );
        }

        return standardDeviations[column];
    }
}