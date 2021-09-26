package org.nalexx6;

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

        ComputationManager manager = null;
        try {
            manager = new ComputationManager(inputValue);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Computation manager started");
        manager.startComputing();
        System.out.println(manager.getResult());
    }

}
