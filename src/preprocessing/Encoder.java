package preprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Encoder {

    // Course is at index 1 in FeatureSelector
    private static final int COURSE_INDEX = 1;

    // Store unique course categories
    private final ArrayList<Integer> courseCategories =
            new ArrayList<>();

    // Map course codes to encoded positions
    private final HashMap<Integer, Integer> courseMapping =
            new HashMap<>();

    // Check whether encoder has been fitted
    private boolean fitted = false;


    // =====================================================
    // LEARN COURSE CATEGORIES FROM TRAINING DATA
    // =====================================================

    public void fit(double[][] trainingFeatures) {

        if (trainingFeatures == null ||
                trainingFeatures.length == 0) {

            throw new IllegalArgumentException(
                    "Training features cannot be empty."
            );
        }

        courseCategories.clear();
        courseMapping.clear();
        fitted = false;

        TreeSet<Integer> uniqueCourses = new TreeSet<>();

        // Find all unique courses
        for (double[] student : trainingFeatures) {

            validateRow(student);

            int course = (int) student[COURSE_INDEX];

            uniqueCourses.add(course);
        }

        // Store sorted course categories
        courseCategories.addAll(uniqueCourses);

        // Assign each course a position
        for (int i = 0; i < courseCategories.size(); i++) {

            courseMapping.put(
                    courseCategories.get(i),
                    i
            );
        }

        fitted = true;
    }


    // =====================================================
    // CONVERT FEATURES USING ONE-HOT ENCODING
    // =====================================================

    public double[][] transform(double[][] features) {

        if (!fitted) {
            throw new IllegalStateException(
                    "Encoder must be fitted before transformation."
            );
        }

        if (features == null) {
            throw new IllegalArgumentException(
                    "Features cannot be null."
            );
        }

        int totalFeatures =
                FeatureSelector.FEATURE_COUNT - 1
                        + courseCategories.size();

        double[][] encoded =
                new double[features.length][totalFeatures];

        for (int i = 0; i < features.length; i++) {

            double[] student = features[i];

            validateRow(student);

            int column = 0;

            // 1. Copy Application Order
            encoded[i][column++] = student[0];

            // 2. One-Hot Encode Course
            int course = (int) student[COURSE_INDEX];

            Integer coursePosition =
                    courseMapping.get(course);

            if (coursePosition == null) {

                throw new IllegalArgumentException(
                        "Unknown course code: " + course
                );
            }

            encoded[i][column + coursePosition] = 1.0;

            column += courseCategories.size();

            // 3. Copy Remaining Features
            for (int j = 2; j < student.length; j++) {

                encoded[i][column++] = student[j];
            }
        }

        return encoded;
    }


    // =====================================================
    // VALIDATE FEATURE VALUES
    // =====================================================

    private void validateRow(double[] student) {

        if (student == null ||
                student.length != FeatureSelector.FEATURE_COUNT) {

            throw new IllegalArgumentException(
                    "Expected 15 original features."
            );
        }

        for (double value : student) {

            if (!Double.isFinite(value)) {

                throw new IllegalArgumentException(
                        "Feature values must be finite."
                );
            }
        }

        double course = student[COURSE_INDEX];

        if (course != Math.rint(course) ||
                course < Integer.MIN_VALUE ||
                course > Integer.MAX_VALUE) {

            throw new IllegalArgumentException(
                    "Course must be a valid integer code."
            );
        }
    }


    // =====================================================
    // RETURN NUMBER OF COURSE CATEGORIES
    // =====================================================

    public int getCourseCategoryCount() {

        if (!fitted) {
            throw new IllegalStateException(
                    "Encoder has not been fitted."
            );
        }

        return courseCategories.size();
    }


    // =====================================================
    // RETURN TOTAL ENCODED FEATURES
    // =====================================================

    public int getEncodedFeatureCount() {

        if (!fitted) {
            throw new IllegalStateException(
                    "Encoder has not been fitted."
            );
        }

        return FeatureSelector.FEATURE_COUNT - 1
                + courseCategories.size();
    }


    // =====================================================
    // RETURN COURSE CATEGORIES
    // =====================================================

    public ArrayList<Integer> getCourseCategories() {

        if (!fitted) {
            throw new IllegalStateException(
                    "Encoder has not been fitted."
            );
        }

        return new ArrayList<>(courseCategories);
    }
}