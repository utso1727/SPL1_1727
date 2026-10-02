package statistics;
import java.text.DecimalFormat;


import model.Student;

import java.util.ArrayList;


public class Statistics {
    static DecimalFormat df = new DecimalFormat("0.00");


    public double calculateMeanGPA(ArrayList<Student> students){

        double sum = 0;


        for(Student s: students){

            sum += s.getGpa();

        }


        return sum / students.size();

    }



    public double calculateMeanAttendance(ArrayList<Student> students){

        double sum = 0;


        for(Student s: students){

            sum += s.getAttendance();

        }


        return sum / students.size();

    }



    public double standardDeviationGPA(ArrayList<Student> students){


        double mean = calculateMeanGPA(students);

        double sum = 0;


        for(Student s: students){

            sum += Math.pow(s.getGpa()-mean,2);

        }


        return Math.sqrt(sum/students.size());

    }



    public void dropoutComparison(ArrayList<Student> students){


        double dropoutGPA = 0;
        double normalGPA = 0;

        int dropoutCount = 0;
        int normalCount = 0;



        for(Student s: students){


            if(s.getDropout()==1){

                dropoutGPA += s.getGpa();
                dropoutCount++;

            }

            else{

                normalGPA += s.getGpa();
                normalCount++;

            }

        }


        System.out.println("Dropout Student Average GPA: "
                + df.format(dropoutGPA/dropoutCount));


        System.out.println("Non-Dropout Student Average GPA: "
                + df.format(normalGPA/normalCount));


    }

}