package ml;

public class LogisticRegression {

    private final int featureCount;
    private final int classCount;

    private final double learningRate;
    private final int epochs;

    // Weights for each class and feature
    private final double[][] weights;

    // Bias for each class
    private final double[] biases;

    private boolean trained = false;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public LogisticRegression(
            int featureCount,
            int classCount,
            double learningRate,
            int epochs
    ) {

        if (featureCount <= 0 || classCount < 2) {
            throw new IllegalArgumentException(
                    "Invalid feature or class count."
            );
        }

        if (!Double.isFinite(learningRate)
                || learningRate <= 0 || epochs <= 0) {

            throw new IllegalArgumentException(
                    "Learning rate and epochs must be positive."
            );
        }

        this.featureCount = featureCount;
        this.classCount = classCount;
        this.learningRate = learningRate;
        this.epochs = epochs;

        weights = new double[classCount][featureCount];
        biases = new double[classCount];

        // Java initializes array values to 0.0
    }


    // =====================================================
    // CALCULATE LINEAR SCORES
    // =====================================================

    private double[] calculateScores(double[] features) {

        double[] scores = new double[classCount];

        for (int k = 0; k < classCount; k++) {

            double score = biases[k];

            for (int j = 0; j < featureCount; j++) {

                score += weights[k][j] * features[j];
            }

            scores[k] = score;
        }

        return scores;
    }


    // =====================================================
    // NUMERICALLY STABLE SOFTMAX
    // =====================================================

    private double[] softmax(double[] scores) {

        double maxScore = scores[0];

        for (int k = 1; k < classCount; k++) {

            if (scores[k] > maxScore) {
                maxScore = scores[k];
            }
        }

        double[] probabilities = new double[classCount];

        double sum = 0;

        for (int k = 0; k < classCount; k++) {

            probabilities[k] =
                    Math.exp(scores[k] - maxScore);

            sum += probabilities[k];
        }

        for (int k = 0; k < classCount; k++) {

            probabilities[k] /= sum;
        }

        return probabilities;
    }


    // =====================================================
    // TRAIN LOGISTIC REGRESSION
    // =====================================================

    public void fit(double[][] X, int[] y) {

        validateTrainingData(X, y);

        trained = false;

        int sampleCount = X.length;

        System.out.println();
        System.out.println("LOGISTIC REGRESSION TRAINING");
        System.out.println("----------------------------");

        System.out.println("Training Samples: " + sampleCount);
        System.out.println("Features: " + featureCount);
        System.out.println("Classes: " + classCount);
        System.out.println("Learning Rate: " + learningRate);
        System.out.println("Epochs: " + epochs);

        // Start each training run from zero weights
        for (int k = 0; k < classCount; k++) {

            biases[k] = 0.0;

            for (int j = 0; j < featureCount; j++) {
                weights[k][j] = 0.0;
            }
        }

        // Repeat training for multiple epochs
        for (int epoch = 1; epoch <= epochs; epoch++) {

            double[][] weightGradients =
                    new double[classCount][featureCount];

            double[] biasGradients =
                    new double[classCount];

            double totalLoss = 0;


            // Process every training student
            for (int i = 0; i < sampleCount; i++) {

                double[] scores = calculateScores(X[i]);

                double[] probabilities = softmax(scores);

                // Stable cross-entropy calculation
                double maxScore = scores[0];

                for (int k = 1; k < classCount; k++) {

                    if (scores[k] > maxScore) {
                        maxScore = scores[k];
                    }
                }

                double exponentialSum = 0;

                for (int k = 0; k < classCount; k++) {

                    exponentialSum +=
                            Math.exp(scores[k] - maxScore);
                }

                totalLoss +=
                        maxScore
                                + Math.log(exponentialSum)
                                - scores[y[i]];


                // Calculate gradients
                for (int k = 0; k < classCount; k++) {

                    double actual =
                            (y[i] == k) ? 1.0 : 0.0;

                    double error =
                            probabilities[k] - actual;

                    biasGradients[k] += error;

                    for (int j = 0; j < featureCount; j++) {

                        weightGradients[k][j] +=
                                error * X[i][j];
                    }
                }
            }


            // Calculate average training loss
            double averageLoss = totalLoss / sampleCount;

            if (!Double.isFinite(averageLoss)) {
                throw new IllegalStateException(
                        "Training loss became invalid."
                );
            }


            // Update weights and biases
            for (int k = 0; k < classCount; k++) {

                biases[k] -= learningRate
                        * biasGradients[k] / sampleCount;

                for (int j = 0; j < featureCount; j++) {

                    weights[k][j] -= learningRate
                            * weightGradients[k][j]
                            / sampleCount;

                    if (!Double.isFinite(weights[k][j])) {
                        throw new IllegalStateException(
                                "Invalid weight during training."
                        );
                    }
                }
            }


            // Display training progress
            if (epoch == 1 || epoch % 50 == 0
                    || epoch == epochs) {

                System.out.printf(
                        "Epoch %d | Loss: %.6f%n",
                        epoch,
                        averageLoss
                );
            }
        }

        trained = true;

        System.out.println();
        System.out.println("Training Completed.");
    }


    // =====================================================
    // PREDICT CLASS PROBABILITIES
    // =====================================================

    public double[] predictProba(double[] features) {

        if (!trained) {
            throw new IllegalStateException(
                    "Model must be trained first."
            );
        }

        validateFeatures(features);

        double[] scores = calculateScores(features);

        return softmax(scores);
    }


    // =====================================================
    // PREDICT CLASS
    // =====================================================

    public int predict(double[] features) {

        double[] probabilities = predictProba(features);

        int predictedClass = 0;

        double highestProbability = probabilities[0];

        for (int k = 1; k < classCount; k++) {

            if (probabilities[k] > highestProbability) {

                highestProbability = probabilities[k];

                predictedClass = k;
            }
        }

        return predictedClass;
    }

    // =====================================================
// RETURN A LEARNED WEIGHT
// =====================================================

    public double getWeight(int classIndex, int featureIndex) {

        if (!trained) {
            throw new IllegalStateException(
                    "Model must be trained first."
            );
        }

        if (classIndex < 0 || classIndex >= classCount ||
                featureIndex < 0 || featureIndex >= featureCount) {

            throw new IllegalArgumentException(
                    "Invalid weight index."
            );
        }

        return weights[classIndex][featureIndex];
    }


// =====================================================
// RETURN A LEARNED BIAS
// =====================================================

    public double getBias(int classIndex) {

        if (!trained) {
            throw new IllegalStateException(
                    "Model must be trained first."
            );
        }

        if (classIndex < 0 || classIndex >= classCount) {

            throw new IllegalArgumentException(
                    "Invalid class index."
            );
        }

        return biases[classIndex];
    }


    // =====================================================
    // VALIDATE TRAINING DATA
    // =====================================================

    private void validateTrainingData(
            double[][] X,
            int[] y
    ) {

        if (X == null || y == null ||
                X.length == 0 || X.length != y.length) {

            throw new IllegalArgumentException(
                    "Invalid training dataset."
            );
        }

        for (int i = 0; i < X.length; i++) {

            validateFeatures(X[i]);

            if (y[i] < 0 || y[i] >= classCount) {

                throw new IllegalArgumentException(
                        "Invalid target at row " + i
                );
            }
        }
    }


    // =====================================================
    // VALIDATE FEATURE VECTOR
    // =====================================================

    private void validateFeatures(double[] features) {

        if (features == null ||
                features.length != featureCount) {

            throw new IllegalArgumentException(
                    "Expected " + featureCount + " features."
            );
        }

        for (double value : features) {

            if (!Double.isFinite(value)) {

                throw new IllegalArgumentException(
                        "Feature values must be finite."
                );
            }
        }
    }
}