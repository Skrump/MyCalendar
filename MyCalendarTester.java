package mycalendartester;

import java.util.GregorianCalendar;
import java.util.Scanner;

/** HW #2 Solution
 * @author Alejandro Lopez
 * This program runs a calendar console application. There are two classes within this program:
 * MyCalendar and Event
 */
public class MyCalendarTester {

    /**
     * To run the main function of the program
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MyCalendar someCal = new MyCalendar();
        initialCalendar(someCal);
    }
    
    /**
     * To Act as a main menu to the main function of the program. It directs the user and receives
     * input and calls the appropriate methods within MyCalendar.
     * @param calendar the main calendar object to be stored and displayed
     * Precondition: User enters the specified input
     * Postcondition: The calendar will manage, create, adjust, and display the proper days and events
     */
    public static void initialCalendar(MyCalendar calendar)
    {
        calendar = new MyCalendar();
        Event someEvent;
        Scanner sc = new Scanner(System.in);
        String input = "";
        boolean quit = false;
        
        calendar.printCalendar();
        
        outter:
        do{
            System.out.println("Select one of the following options:");
            System.out.println("[L]oad   [V]iew by   [C]reate   [G]o to   [E]vent list   [D]elete   [Q]uit");
            
            input = sc.nextLine();
            
            if(input.equalsIgnoreCase("L"))
            {
                calendar.loadFromFile();

            }
            else if(input.equalsIgnoreCase("V"))
            {
                System.out.println("[D]ay view or [M]onth view?");
                input = sc.nextLine();
                if (input.equalsIgnoreCase("D"))
                {
                    do{
                        calendar.printDayCalendar();
                        System.out.println("\n[P]revious [N]ext or [M]ain menu?");
                        input = sc.nextLine();
                        if (input.equalsIgnoreCase("P"))
                            calendar.prevDay();
                        else if (input.equalsIgnoreCase("N"))
                            calendar.nextDay();
                    }while (!input.equalsIgnoreCase("M"));
                }
                else 
                {
                    do{
                    calendar.printEventCalendar();
                    System.out.println("[P]revious [N]ext or [M]ain menu?");
                    input = sc.nextLine();
                    if(input.equalsIgnoreCase("N"))
                        calendar.nextMonth();
                    else if (input.equalsIgnoreCase("P"))
                        calendar.prevMonth();
                    else
                        continue outter;
                    } while(!input.equalsIgnoreCase("M"));
                }
            }
            else if(input.equalsIgnoreCase("C"))
            {
                System.out.println("create");
                someEvent = new Event();
                
                someEvent.makeEvent();
                calendar.add(someEvent);
            }
            else if(input.equalsIgnoreCase("G"))
            {
                GregorianCalendar date = new GregorianCalendar();
                System.out.print("To go to event date, ");
                System.out.println("please enter e date in the format MM/DD/YYYY");
                input = sc.nextLine();
                
                String [] split = input.split("/");
                date.set(Integer.parseInt(split[2]), Integer.parseInt(split[0])-1, Integer.parseInt(split[1]));
        
                calendar.getSelectedEvent(date);
            }
            else if(input.equalsIgnoreCase("E"))
            {
                calendar.printAll();
            }
            else if(input.equalsIgnoreCase("D"))
            {
                GregorianCalendar date = new GregorianCalendar();
                System.out.println("Please enter a specific date in the format MM/DD/YYYY");
                input = sc.nextLine();
                
                String [] split = input.split("/");
                date.set(Integer.parseInt(split[2]), Integer.parseInt(split[0])-1, Integer.parseInt(split[1]));
                
                System.out.println("Please choose one of the following: ");
                System.out.println("[S]elected or [A]ll");
                
                input = sc.nextLine();
                
                if (input.equalsIgnoreCase("A"))
                    calendar.removeAll(date);
                else if(input.equalsIgnoreCase("S"))
                {
                    System.out.println("Please enter the name of the event you would like to remove.");
                    input = sc.nextLine();
                    calendar.removeSelected(date, input);
                }
            }
            else if(input.equalsIgnoreCase("Q"))
            {
                calendar.printToFile();
                quit = true;
            }
        }while(!quit);
            
        
    }
    
}

