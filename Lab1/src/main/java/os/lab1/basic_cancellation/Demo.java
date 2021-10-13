package os.lab1.basic_cancellation;

import os.lab1.basic_cancellation.ComputationManager;

import java.io.IOException;
import java.util.Scanner;

public class Demo {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int inputValue = 0;
        boolean correctResponse = false;
        while (!correctResponse) {
            System.out.println("Type x parameter value:");
            correctResponse = true;
            if (sc.hasNextInt()) {
                inputValue = sc.nextInt();
            } else {
                correctResponse = false;
            }
            sc.nextLine();
        }

        System.out.println("Value is read");

        try {
            ComputationManager manager = new ComputationManager(inputValue);
            System.out.println("Computation manager started");
            manager.startComputing();
            Boolean fStatus = manager.getFStatus();
            Boolean gStatus = manager.getGStatus();
            Boolean cancelStatus = manager.getCancellationStatus();
            if(fStatus){
                System.out.println("F-function computation failed");
            }

            if(gStatus){
                System.out.println("G-function computation failed");
            }

            if(cancelStatus){
                System.out.println("Computation cancelled");
            }

            if(!(fStatus || gStatus) && !cancelStatus) {
                System.out.println(manager.getResult());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
