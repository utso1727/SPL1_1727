package model;

public class Student {

    private int id;
    private int age;
    private double attendance;
    private double gpa;
    private int failedCourses;
    private int studyHours;
    private int tuitionStatus;
    private int scholarship;
    private int financialDifficulty;
    private int dropout;


    public Student(int id, int age, double attendance, double gpa,
                   int failedCourses, int studyHours,
                   int tuitionStatus, int scholarship,
                   int financialDifficulty, int dropout) {

        this.id = id;
        this.age = age;
        this.attendance = attendance;
        this.gpa = gpa;
        this.failedCourses = failedCourses;
        this.studyHours = studyHours;
        this.tuitionStatus = tuitionStatus;
        this.scholarship = scholarship;
        this.financialDifficulty = financialDifficulty;
        this.dropout = dropout;
    }


    public double getAttendance() {
        return attendance;
    }


    public double getGpa() {
        return gpa;
    }


    public int getDropout() {
        return dropout;
    }


    public int getFailedCourses() {
        return failedCourses;
    }
}