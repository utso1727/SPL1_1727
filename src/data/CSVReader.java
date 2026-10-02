package data;

import model.Student;

import java.io.*;
import java.util.ArrayList;


public class CSVReader {


    public ArrayList<Student> readStudents(String fileName) {

        ArrayList<Student> students = new ArrayList<>();

        try {

            BufferedReader br =
                    new BufferedReader(new FileReader(fileName));


            String line;

            br.readLine(); // skip header


            while((line = br.readLine()) != null) {


                String data[] = line.split(",");


                Student student = new Student(

                        Integer.parseInt(data[0]),
                        Integer.parseInt(data[1]),
                        Double.parseDouble(data[2]),
                        Double.parseDouble(data[3]),
                        Integer.parseInt(data[4]),
                        Integer.parseInt(data[5]),
                        Integer.parseInt(data[6]),
                        Integer.parseInt(data[7]),
                        Integer.parseInt(data[8]),
                        Integer.parseInt(data[9])

                );


                students.add(student);

            }


            br.close();


        } catch(Exception e){

            System.out.println(e.getMessage());

        }


        return students;

    }

}