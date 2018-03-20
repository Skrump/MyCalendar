package mycalendartester;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

enum MONTHS
{
	January, February, March, April, May, June, July, August, September, October, November, December;
}
enum DAYS
{
	Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday ;
}

/**
 * Represent a Calendar Object. Used to display a calendar and allow a user to browse through the days, months,
 * and years. It also allows users to create and store events within the calendar. Events and the current date
 * are highlighted. 
 * @author Alejandro Lopez
 */
public class MyCalendar {
    private ArrayList<Event> myEvents = new ArrayList();
    private GregorianCalendar currentCal = new GregorianCalendar();
    private MONTHS[] monthArray = MONTHS.values();
    private DAYS[] dayArray = DAYS.values();
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEEEEEEEE, d MMMMMMMMM yyyy HH:mm");
    
    /**
     * Used to add an event to the Calendar. Checks if the ArrayList of Events is empty or if it has any 
     * conflicts. It then calls on sort after the Event is added.
     * @param e Event object to be stored in Calendar
     * Precondition: e is a valid (non-null) Event object
     * Postcondition: the Event will be added to the Calendar
     */
    public void add(Event e)
    {
        int hits = 0;
        if(myEvents.size() == 0)
            myEvents.add(e);
        else
        {
            for (int i = 0; i < myEvents.size(); i++)
            {
                if (myEvents.get(i).eventConflicts(e))
                    hits++;
            }
            if(hits == 0)
                myEvents.add(e);
            else 
                System.out.println("There is already an event stored in the Calendar during that time.");
        }

        Collections.sort(myEvents,eventComparator);
    }
    
    /**
     * Comparator for ArrayList of Event objects, used to sort the list
     */
    public static Comparator<Event> eventComparator = new Comparator<Event>()
    {
        /**
         * Compare method for each Event in the ArrayList data structure
         * @param e1 first event to be compared
         * @param e2 second event to be compared
         * @return negative value if e1's date comes before e2, otherwise returns e2
         * Precondition: There is at least one event object to be compared
         * Postcondition: The ArrayList will be able to be sorted 
         */
        @Override
        public int compare(Event e1, Event e2)
        {            
            return e1.getDate().getTime().compareTo(e2.getDate().getTime());
        }
    };

    /**
     * Reads the text file and parses any data within it and adds events with the given
     * input to the ArrayList of Events. After it stores all of the elements, it calls 
     * Collections' sort method to sort the ArrayList
     */
    public void loadFromFile()
    {
        String fromFile;
        try
        {
            File file = new File("events.txt");
            Scanner in = new Scanner(file);
            while(in.hasNextLine())
            {
                fromFile = in.nextLine();
                if (fromFile != null)
                {
                    String [] split = fromFile.split("-|;");
                    split[0] = split[0].trim();
                    split[1] = split[1].trim();

                    if (split.length == 3)
                    {
                        split[2] = split[2].trim();
                        myEvents.add(new Event(split[0],split[1],split[2]));
                    }
                    else
                        myEvents.add(new Event(split[0],split[1]));
                }
            }
        }
        catch(FileNotFoundException e)
        {
            System.err.println(e);
            System.out.println("There is no existing file.");
        }
        if(myEvents.isEmpty())
            System.out.println("There are no events stored in this text file.");
        Collections.sort(myEvents,eventComparator);
    }
    
    /**
     * Removes the selected Event from the Calendar.
     * @param gc the date that the event is held on
     * @param inp the title of the event to be deleted
     * Precondition: the event exists and the user inputs the arguments correctly
     * Postcondition: the selected event is removed from the Calendar
     */
    public void removeSelected(GregorianCalendar gc, String inp)
    {
        for (int i = 0; i < myEvents.size(); i++)
        {
            if(myEvents.get(i).getDate().get(Calendar.DATE) == gc.get(Calendar.DATE) && myEvents.get(i).getName().equalsIgnoreCase(inp))
                myEvents.remove(i);
        }
        System.out.println("Slected event was removed from that day.");
    }
    
    /**
     * Removes all Events from the given day
     * @param gc the date to have Events cleared from
     * Precondition: the day has Events
     * Postcondition: Events on that day are cleared
     */
    public void removeAll(GregorianCalendar gc)
    {
        for (int i = myEvents.size() - 1; i >= 0; i--)
        {
            if(myEvents.get(i).getDate().get(Calendar.DATE) == gc.get(Calendar.DATE))
                myEvents.remove(i);
        }
        System.out.println("All events were removed from that day.");
    }
    
    
    /**
     * Prints all Events on a specific day (defined by the user)
     * @param gc date of Events to be searched
     * Precondition: There is at least one Event on the given day
     * Postcondition: All events for the defined day are printed to the screen
     */
    public void getSelectedEvent(GregorianCalendar gc)
    {
        Date tempDate = new Date();
        int counter = 0;
        for (int i = 0; i < myEvents.size(); i++)
        {
            tempDate = myEvents.get(i).getDate().getTime();
            if(gc.get(Calendar.YEAR) == myEvents.get(i).getDate().get(Calendar.YEAR))
                if(gc.get(Calendar.MONTH) == myEvents.get(i).getDate().get(Calendar.MONTH))
                    if(gc.get(Calendar.DATE) == myEvents.get(i).getDate().get(Calendar.DATE))
                    {
                        System.out.print(sdf.format(myEvents.get(i).getDate().getTime()));
                        if(myEvents.get(i).hasEndTime())
                        {
                            sdf = new SimpleDateFormat("HH:mm");
                            tempDate = myEvents.get(i).getEndTime().getTime();
                            System.out.println(" - " + sdf.format(tempDate.getTime()) + ";" + myEvents.get(i).getName());
                            sdf = new SimpleDateFormat("EEEEEEEEEE, d MMMMMMMMM yyyy HH:mm");
                        }
                        else
                            System.out.println(" " + myEvents.get(i).getName() + "\n");
                        counter++;
                    }
        }
        if (counter == 0)
            System.out.println("There are no events on that day.\n");
    }
        
    /**
     * Prints the current month's calendar with the current day highlighted.
     */
    public void printCalendar()
    {
        String frontSpace = "";
        GregorianCalendar today = new GregorianCalendar();
        GregorianCalendar temp = new GregorianCalendar(currentCal.get(Calendar.YEAR), currentCal.get(Calendar.MONTH), 1);
        
        for (int i = 0; i < temp.get(Calendar.DAY_OF_WEEK)-1; i++)
            frontSpace += "     ";
        
        System.out.printf("\t%10s %s\n   Su   Mo   Tu   We   Th   Fr   Sa\n%s",monthArray[currentCal.get(Calendar.MONTH)], currentCal.get(Calendar.YEAR),frontSpace);

        for (int i = 0; i < currentCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++)
        {
            if(today.get(Calendar.DATE) == i+1)
            {
                System.out.printf("%2s%2d]","[", 1 + i);
                temp.add(Calendar.DATE,1);
            }
            else 
            {
                System.out.printf("%5d", 1 + i);
                temp.add(Calendar.DATE,1);
            }
            if(temp.get(Calendar.DAY_OF_WEEK) == 1)
                System.out.print("\n");
        }
        System.out.println();
    }
    
    /**
     * Prints Calendar while highlighting the current day and all events held in the current month.
     * Precondition: There are Events in the given month
     * Postcondition: All days in month with an Event are highlighted
     */
    public void printEventCalendar()
    {
        String frontSpace = "";
        GregorianCalendar today = new GregorianCalendar();
        GregorianCalendar temp = new GregorianCalendar(currentCal.get(Calendar.YEAR), currentCal.get(Calendar.MONTH), 1);
        
        for (int i = 0; i < temp.get(Calendar.DAY_OF_WEEK)-1; i++)
            frontSpace += "     ";
        
        System.out.printf("\t%10s %s\n   Su   Mo   Tu   We   Th   Fr   Sa\n%s",monthArray[currentCal.get(Calendar.MONTH)], currentCal.get(Calendar.YEAR),frontSpace);

        for (int i = 0; i < currentCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++)
        {
            if(hasEvent(i+1))
            {
                System.out.printf("%2s%2d}","{", 1 + i);
                temp.add(Calendar.DATE,1);
            }
            else 
            {
                System.out.printf("%5d", 1 + i);
                temp.add(Calendar.DATE,1);
            }
            if(temp.get(Calendar.DAY_OF_WEEK) == 1)
                System.out.print("\n");
        }
        System.out.println();
    }
    
    /**
     * Checks if a given day has an Event.
     * @param iter the iterator going through each day of the month
     * @return true if the given day has an Event
     * Precondition: There are Events in that month
     * Postcondition: Event(s) will be highlighted in month view
     */
    private boolean hasEvent(int iter)
    {
        for (int i = 0; i < myEvents.size(); i++)
        {
            if(currentCal.get(Calendar.MONTH) ==  myEvents.get(i).getDate().get(Calendar.MONTH))
            {
                if (myEvents.get(i).getDate().get(Calendar.DATE) == iter)
                    return true;
            }
        }
        return false;
    }
    
    /**
     * Prints all Events on the current day.
     * Precondition: There are Events on that day
     * Postcondition: The User sees all Events on that day
     */
    public void printDayCalendar()
    {
        System.out.println("Day view");
        sdf = new SimpleDateFormat("EEEEEEEEEE, d MMMMMMMMM yyyy");
        System.out.println("Current date: " + sdf.format(currentCal.getTime()));
        
        Date tempDate = new Date();
        
        
        for (int i = 0; i < myEvents.size(); i++)
        {
            if (myEvents.get(i).getDate().get(Calendar.DATE) == currentCal.get(Calendar.DATE))
            {
                sdf = new SimpleDateFormat("HH:mm");
                
                System.out.print(myEvents.get(i).getName() + " ");
                System.out.print(sdf.format(myEvents.get(i).getDate().getTime()));
                tempDate = myEvents.get(i).getDate().getTime();
                if (myEvents.get(i).hasEndTime())
                {
                    tempDate = myEvents.get(i).getEndTime().getTime();
                    System.out.println(" - " + sdf.format(tempDate.getTime()));
                }
                else
                    System.out.println();
            }
        }
        sdf = new SimpleDateFormat("EEEEEEEEEE, d MMMMMMMMM yyyy HH:mm");
    }
    
    /**
     * Prints every Event stored in the Calendar to the console.
     * Precondition: there are Events stored in the Calendar
     * Postcondition: User will see all stored Events
     */
    public void printAll()
    {
        if (myEvents.size() != 0)
        {
            Date tempDate = new Date();
            System.out.println();
            for(int i = 0; i < myEvents.size(); i++)
            {
                tempDate = myEvents.get(i).getDate().getTime();
                System.out.print(sdf.format(myEvents.get(i).getDate().getTime()));
                if (myEvents.get(i).hasEndTime())
                {
                    sdf = new SimpleDateFormat("HH:mm");
                    tempDate = myEvents.get(i).getEndTime().getTime();
                    System.out.println(" - " + sdf.format(tempDate.getTime()) + ";" + myEvents.get(i).getName());
                    sdf = new SimpleDateFormat("EEEEEEEEEE, d MMMMMMMMM yyyy HH:mm");
                }
                else
                    System.out.println("; " + myEvents.get(i).getName());
            }
            System.out.println();
        }
        else
            System.out.println("There are no events to display.");
    }
    
    /**
     * Changes current month to the previous month (used by user to navigate the Calendar month view)
     */
    public void prevMonth()
    {
        currentCal.add(Calendar.MONTH,-1);
    }
    
    /**
     * Changes current month to the following month (used by user to navigate the Calendar month view)
     */
    public void nextMonth()
    {
        currentCal.add(Calendar.MONTH,1);
    }
    
    /**
     * Changes current day to the previous day (used by user to navigate the Calendar day view)
     */
    public void prevDay()
    {
        currentCal.add(Calendar.DATE,-1);
    }
    
    /**
     * Changes current day to the following day (used by user to navigate the Calendar day view)
     */
    public void nextDay()
    {
        currentCal.add(Calendar.DATE,1);
    }
    
    /**
     * Prints all events to a text file to be stored for later use
     * Precondition: there are Events stored in the Calendar
     * Postcondition: text file is populated with the Events
     */
    public void printToFile()
    {
        FileOutputStream fout;
        try{
            fout = new FileOutputStream("events.txt");
        }
        catch(FileNotFoundException e)
        {
            System.err.println(e);
            System.out.println("Error creating output text file.");
            return;
        }

        PrintWriter prntWriter = new PrintWriter(fout, true);
        
        for (int i = 0; i < myEvents.size(); i++)
        {
            Date tempDate = new Date();
            
            tempDate = myEvents.get(i).getDate().getTime();
            sdf.setCalendar(currentCal);
            
            prntWriter.print(sdf.format(tempDate.getTime()));
            
            if (myEvents.get(i).hasEndTime())
            {
                sdf = new SimpleDateFormat("HH:mm");
                tempDate = myEvents.get(i).getEndTime().getTime();
                prntWriter.println(" - " + sdf.format(tempDate.getTime()) + ";" + myEvents.get(i).getName());
                sdf = new SimpleDateFormat("EEEEEEEEEE, d MMMMMMMMM yyyy HH:mm");
            }
            else
                prntWriter.println(";" + myEvents.get(i).getName());
        }
        
        prntWriter.close();
    }
}
