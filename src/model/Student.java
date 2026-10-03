package model;

public class Student {

    private int id;

    private double previousQualificationGrade;
    private double admissionGrade;

    private int debtor;
    private int tuitionFeesUpToDate;
    private int scholarshipHolder;

    private int ageAtEnrollment;

    private int firstSemEnrolled;
    private int firstSemEvaluations;
    private int firstSemApproved;
    private double firstSemGrade;

    private int secondSemEnrolled;
    private int secondSemEvaluations;
    private int secondSemApproved;
    private double secondSemGrade;

    private int dropout;


    public Student(
            int id,
            double previousQualificationGrade,
            double admissionGrade,
            int debtor,
            int tuitionFeesUpToDate,
            int scholarshipHolder,
            int ageAtEnrollment,
            int firstSemEnrolled,
            int firstSemEvaluations,
            int firstSemApproved,
            double firstSemGrade,
            int secondSemEnrolled,
            int secondSemEvaluations,
            int secondSemApproved,
            double secondSemGrade,
            int dropout) {

        this.id = id;

        this.previousQualificationGrade = previousQualificationGrade;
        this.admissionGrade = admissionGrade;

        this.debtor = debtor;
        this.tuitionFeesUpToDate = tuitionFeesUpToDate;
        this.scholarshipHolder = scholarshipHolder;

        this.ageAtEnrollment = ageAtEnrollment;

        this.firstSemEnrolled = firstSemEnrolled;
        this.firstSemEvaluations = firstSemEvaluations;
        this.firstSemApproved = firstSemApproved;
        this.firstSemGrade = firstSemGrade;

        this.secondSemEnrolled = secondSemEnrolled;
        this.secondSemEvaluations = secondSemEvaluations;
        this.secondSemApproved = secondSemApproved;
        this.secondSemGrade = secondSemGrade;

        this.dropout = dropout;
    }


    public int getId() {
        return id;
    }


    public double getPreviousQualificationGrade() {
        return previousQualificationGrade;
    }


    public double getAdmissionGrade() {
        return admissionGrade;
    }


    public int getDebtor() {
        return debtor;
    }


    public int getTuitionFeesUpToDate() {
        return tuitionFeesUpToDate;
    }


    public int getScholarshipHolder() {
        return scholarshipHolder;
    }


    public int getAgeAtEnrollment() {
        return ageAtEnrollment;
    }


    public int getFirstSemEnrolled() {
        return firstSemEnrolled;
    }


    public int getFirstSemEvaluations() {
        return firstSemEvaluations;
    }


    public int getFirstSemApproved() {
        return firstSemApproved;
    }


    public double getFirstSemGrade() {
        return firstSemGrade;
    }


    public int getSecondSemEnrolled() {
        return secondSemEnrolled;
    }


    public int getSecondSemEvaluations() {
        return secondSemEvaluations;
    }


    public int getSecondSemApproved() {
        return secondSemApproved;
    }


    public double getSecondSemGrade() {
        return secondSemGrade;
    }


    public int getDropout() {
        return dropout;
    }
}