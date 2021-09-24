package org.nalexx6;

import java.util.Scanner;

public class Demo {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int variable = 0;
        boolean correctResponse = false;
        while (!correctResponse) {
            System.out.println("Type x parameter value:");
            correctResponse = true;
            if (sc.hasNextInt()) {
                variable = sc.nextInt();
            } else {
                correctResponse = false;
            }
            sc.nextLine();
        }
    }
}
