package preprocessing;

import model.Student;

import java.util.ArrayList;

public class FeatureSelector {

    // Total number of selected original features
    public static final int FEATURE_COUNT = 15;


    // ---------------------------------------------------------
    // Extract Selected Features From One Student
    // ---------------------------------------------------------

    public double[] selectFeatures(Student student) {

        double[] features = new double[FEATURE_COUNT];

        // Academic Background
        features[0] = student.getApplicationOrder();
        features[1] = student.getCourse();
        features[2] = student.getDaytimeEveningAttendance();
        features[3] = student.getPreviousQualificationGrade();
        features[4] = student.getAdmissionGrade();

        // Financial and Personal Information
        features[5] = student.getDebtor();
        features[6] = student.getTuitionFeesUpToDate();
        features[7] = student.getScholarshipHolder();
        features[8] = student.getAgeAtEnrollment();

        // First Semester Academic Performance
        features[9] = student.getCurricularUnits1stSemCredited();
        features[10] = student.getCurricularUnits1stSemEnrolled();
        features[11] = student.getCurricularUnits1stSemEvaluations();
        features[12] = student.getCurricularUnits1stSemApproved();
        features[13] = student.getCurricularUnits1stSemGrade();
        features[14] = student.getCurricularUnits1stSemWithoutEvaluations();

        return features;
    }


    // ---------------------------------------------------------
    // Extract Features From All Students
    // ---------------------------------------------------------

    public double[][] selectAllFeatures(
            ArrayList<Student> students) {

        double[][] allFeatures =
                new double[students.size()][FEATURE_COUNT];

        for (int i = 0; i < students.size(); i++) {

            allFeatures[i] =
                    selectFeatures(students.get(i));
        }

        return allFeatures;
    }


    // ---------------------------------------------------------
    // Return Selected Feature Names
    // ---------------------------------------------------------

    public String[] getFeatureNames() {

        return new String[]{

                "Application Order",
                "Course",
                "Daytime/Evening Attendance",
                "Previous Qualification Grade",
                "Admission Grade",
                "Debtor",
                "Tuition Fees Up to Date",
                "Scholarship Holder",
                "Age at Enrollment",
                "1st Semester Credited Units",
                "1st Semester Enrolled Units",
                "1st Semester Evaluations",
                "1st Semester Approved Units",
                "1st Semester Grade",
                "1st Semester Without Evaluations"
        };
    }
}