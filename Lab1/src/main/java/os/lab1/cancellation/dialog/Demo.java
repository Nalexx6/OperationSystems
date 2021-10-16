package os.lab1.cancellation.dialog;

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
            Integer fStatus = manager.getFStatus();
            Integer gStatus = manager.getGStatus();
            Boolean cancelStatus = manager.getCancellationStatus();

            if(cancelStatus){
                System.out.println("Computation cancelled");
                if(fStatus > 0){
                    System.out.println("f-function did not finished. Number of soft fails: " + (fStatus - 1));
                }
                if(gStatus > 0){
                    System.out.println("g-function did not finished. Number of soft fails: " + (fStatus - 1));
                }
            }

            if(fStatus + gStatus == -2) {
                System.out.println("Expression value: failed");
            } else if(fStatus == 0 && gStatus == 0) {
                System.out.println("Expression value: " + manager.getResult());
            } else {

                System.out.println("Expression value: undetermined");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
