import dto.HistoryDataDTO.HistogramProperiesDTO;
import dto.HistoryDataDTO.HistoryDataDTO;

import java.util.Scanner;

public class Menu {
    private final int STRING_CHOICE = -1;
    private final int NO_LIMIT = -1;


    public void displayMainMenu() {
        System.out.println("*************************************");
        System.out.println("Please Choose an option (number between 1-5):");
        System.out.println("1 - Read file");
        System.out.println("2 - Display simulation details");
        System.out.println("3 - Run simulation");
        System.out.println("4 - Show history simulations");
        System.out.println("5 - Exit ");
    }
    public int getChoice(){
        Scanner scanObj = new Scanner(System.in);
        int choice = STRING_CHOICE;
        try {
            choice = scanObj.nextInt();
        } catch (Exception e) {
            scanObj.nextLine();
        }
        return choice;
    }

    public int getYesOrNoChoice() {
        boolean validChoice = false;
        int choice;
        do {
         choice = getChoice();
        if(choice == STRING_CHOICE) {
            System.out.println("String was entered!");
            System.out.println("Please choose 1.Yes or 2.No");
        }else  {
            validChoice = isInRangeChoice(choice, 1, 2);
            if(!validChoice) {
                System.out.println("Out of range number was entered!");
                System.out.println("Please choose 1.Yes or 2.No");
            }
        }
    } while (!validChoice);

        return choice;
    }


    public int getMainChoice(){
        boolean validChoice = false;
        int choice;
        do {
            displayMainMenu();
            choice = getChoice();
            if(choice == STRING_CHOICE)
                System.out.println("String was entered!");
            else {
                validChoice = isInRangeChoice(choice, 1, 5);
                if(!validChoice)
                    System.out.println("Out of range number was entered!");
            }
        } while (!validChoice);

        return choice;
    }

    private boolean isInRangeChoice(int choice,int smallest, int biggest) {
        if(biggest != NO_LIMIT)
            return choice >= smallest && choice <= biggest;
        return choice >= smallest;
    }
    public String getFileName(){
        System.out.println("Please enter full path name:");
        Scanner scanObj = new Scanner(System.in);
        return scanObj.nextLine();
    }

    public String getEnvValue() {
        System.out.println("Enter value: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public int getUserChooseToSeeSimulationResults(int size) {
            boolean validChoice = false;
            int choice;
            do {
                choice = getChoice();
                if(choice == STRING_CHOICE)
                    System.out.println("String was entered!");
                else {
                    validChoice = isInRangeChoice(choice, 1, size);
                    if(!validChoice)
                        System.out.println("Out of range number was entered!");
                }
            } while (!validChoice);

            return choice;
        }

    public int getWayToDisplayDetails() {
        boolean validChoice = false;
        int choice;
        System.out.println("Please choose what you want to see:");
        System.out.println("1. Entities population in this simulation.");
        System.out.println("2. Histogram of property.");
        System.out.println("3. Return to main menu.");
        do {
            choice = getChoice();
            if(choice == STRING_CHOICE)
                System.out.println("String was entered!");
            else {
                validChoice = isInRangeChoice(choice, 1, 3);
                if(!validChoice) {
                    System.out.println("Out of range number was entered!");
                    System.out.println("Please choose again: ");
                }
            }
        } while (!validChoice);
        return choice;
    }

    public int getNumberFromUser(int range) {
        boolean validChoice = false;
        int choice;
        do {
            choice = getChoice();
            if(choice == STRING_CHOICE)
                System.out.println("String was entered!");
            else {
                validChoice = isInRangeChoice(choice, 1, range);
                if(!validChoice) {
                    System.out.println("Out of range number was entered!");
                    System.out.println("Please choose again: ");
                }
            }
        } while (!validChoice);
        return choice-1; //return -1 for start from 0
    }

    public int getNumberFromUserWithZeroToFinish(int range) {
        boolean validChoice = false;
        int choice;
        do {
            choice = getChoice();
            if(choice == STRING_CHOICE)
                System.out.println("String was entered!");
            else {
                validChoice = isInRangeChoice(choice, 0, range);
                if(!validChoice) {
                    System.out.println("Out of range number was entered!");
                    System.out.println("Please choose again: ");
                }
            }
        } while (!validChoice);
            return choice;
    }
}
