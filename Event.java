package mycalendartester;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

/**
 * Represents an Event object. Stores the name, date, start, and end times to be used within
 * the Calendar. 
 * @author Alejandro Lopez
 */
public class Event {
    private String name;
    private GregorianCalendar date = new GregorianCalendar();
    private GregorianCalendar endTime = new GregorianCalendar();
    private Scanner sc = new Scanner(System.in);
    private boolean hasEnd = false;
    
    /**
     * Default Constructor: Initializes name to an empty string to prevent null exceptions
     */
    Event()
    {
        name = new String("");
        hasEnd = false;
    }
    
    /**
     * Constructor: Constructs an Event object with no end time, sets up the event date and 
     * time with the proper format. It parses the input to be accepted as a date object
     * @param in the date input to be parsed and used as the concrete date/time 
     * @param name the title of the event
     * Precondition: in should be in proper date format and name shouldn't be a null string
     * Postcondition: an Event object is created and ready to be stored in the Calendar
     */
    Event(String in, String name)
    {
        try {
            Date aDate = new SimpleDateFormat("EEEEEEEEEE, d MMMMMMMMM yyyy HH:mm").parse(in);
            date.setTime(aDate);
        } catch (ParseException ex) {
            System.err.println(ex);
        }
        this.name = name;
    }
    
    /**
     * Constructor: Constructs and Event object with a given end time. This method sets up
     * the event date and time with proper format. It parses the input to be accepted as a
     * date object
     * @param in the date input to be parsed and used as the concrete date/time 
     * @param end the date input to be parsed and used as the concrete time for endTime
     * @param name the title of the event
     * Precondition: in and end should be in proper format and name shouldn't be a null string
     * Postcondition: an Event object with proper start and end time is created and ready to be 
     * stored in the Calendar
     */
    Event(String in, String end, String name)
    {
        try {
            Date aDate = new SimpleDateFormat("EEEEEEEEEE, d MMMMMMMMM yyyy HH:mm").parse(in);
            date.setTime(aDate);

            updateEndTime(end);

        } catch (ParseException ex) {
            System.err.println(ex);
        }
        this.name = name;
    }
    
    /**
     * Creates an event object to be stored in the Calendar using the User's inputs. It guides the user with
     * the proper input format.
     * Precondition: User enters the proper input for the title, date, and time
     * Postcondition: Event object is created and added to the calendar.
     */
    public void makeEvent()
    {
        String input;
        boolean valid = false;
        System.out.println("Please enter the title of your event:");
        do{
            input = sc.nextLine();
            if (input == null || input == "")
                System.out.println("Please enter a valid title.");
            else
            {
                valid = true;
                name = input;
            }
        }while(!valid);
        
        valid = false;
        
        System.out.println("Please enter the date of your event (MM/DD/YYYY):");
        do{
            input = sc.nextLine();
            if (input == null || input == "")
                System.out.println("Please enter a valid date.");
            else
            {
                valid = true;
                updateDate(input);
            }
        }while(!valid);
        
        valid = false;
        
        System.out.println("Please enter the time of your event (24-hr clock (06:00 for 6:00am 13:00 for 1:00pm)");
        System.out.println("NOTE: To enter an end time just put a dash in between the two times (i.e., 12:00 - 16:00)");
        do{
            input = sc.nextLine();
            if (input == null || input == "")
                System.out.println("Please enter a valid time.");
            else
            {
                valid = true;
                updateTime(input);
            }
        }while(!valid);

    }
    
    /**
     * Updates the date specified by the program using the date-formatted String given by the User
     * @param in user's date-formatted input
     * Precondition: The user's input is formatted correctly (MM/DD/YYYY)
     * Postcondition: The Event's date is updated
     */
    public void updateDate(String in)
    {
        String [] split = in.split("/");
        date.set(Integer.parseInt(split[2]), Integer.parseInt(split[0])-1, Integer.parseInt(split[1]));
    }
    
    /**
     * Updates the start time specified by the User. Takes the formatted String input and adds the time 
     * to the GregorianCalendar instance variable holding the date of the Event
     * @param in user's time-formatted input (HH:mm)
     * Precondition: in is formatted properly
     * Postcondition: Date (GregorianCalendar instance variable) gets updated with the proper start time
     */
    public void updateTime(String in)
    {
        Date someDate = new Date();
        in = in.replaceAll("\\s", "");
        String [] split = in.split(":|-");
        
        date.set(Calendar.HOUR_OF_DAY, Integer.valueOf(split[0]));
        date.set(Calendar.MINUTE, Integer.valueOf(split[1]));

        if (split.length >= 3)
        {
            endTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(split[2]));
            endTime.set(Calendar.MINUTE, Integer.valueOf(split[3]));
            hasEnd = true;
        }
        else 
        {
            endTime = date;
            hasEnd = false;
        }
    }
    
    /**
     * Updates the end time (if given) of the Event object.
     * @param in user's time-formatted input (HH:mm)
     * Precondition: in is formatted correctly 
     * Postcondition: Event has endTime updated with the user's input
     */
    public void updateEndTime(String in)
    {
        String [] split = in.split(":");
        endTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(split[0]));
        endTime.set(Calendar.MINUTE, Integer.valueOf(split[1]));
        hasEnd = true;
    }
    
    /**
     * Checks if there is a time conflict for this event
     * @param e the event to be compared to this event
     * @return true if there is a time conflict
     */
    public boolean eventConflicts(Event e)
    {
        if (this.getDate().get(Calendar.YEAR) == (e.getDate().get(Calendar.YEAR)))
        {
            if (this.getDate().get(Calendar.MONTH) == (e.getDate().get(Calendar.MONTH)))
            {
                if (this.getDate().get(Calendar.DATE) == (e.getDate().get(Calendar.DATE)))
                {
                    if(timeConflicts(e))
                        return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Checks for any time conflicts between Events and the Event to be added
     * @param e the event who's being checked 
     * @return true if there is a time conflict
     */
    public boolean timeConflicts(Event e)
    {
        if(this.getDate().get(Calendar.HOUR_OF_DAY) < e.getDate().get(Calendar.HOUR_OF_DAY) && this.getEndTime().get(Calendar.HOUR_OF_DAY) > e.getDate().get(Calendar.HOUR_OF_DAY))
        {
            return true;
        }
        else if (this.getEndTime().get(Calendar.HOUR_OF_DAY) == e.getDate().get(Calendar.HOUR_OF_DAY))
        {
            if(this.getEndTime().get(Calendar.MINUTE) < e.getDate().get(Calendar.MINUTE))
                return false;
        }
        else if(this.getDate().get(Calendar.HOUR_OF_DAY) == e.getDate().get(Calendar.HOUR_OF_DAY))
        {
            if(this.getDate().get(Calendar.MINUTE) <= e.getDate().get(Calendar.MINUTE))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * To get the Event's title
     * @return Event name
     */
    public String getName(){return name;}
    
    /**
     * To get the Event's Date
     * @return Start date/time
     */
    public GregorianCalendar getDate(){return date;}
    
    /**
     * To get the Event's end time
     * @return End time
     */
    public GregorianCalendar getEndTime(){return endTime;}
    
    /**
     * To check if the Event has an end time specified
     * @return true if Event has an end time
     */
    public boolean hasEndTime(){return hasEnd;}
    
}
