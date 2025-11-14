import java.util.Comparator;

public class NameSort implements Comparator<Customer>
{
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor for objects of class NameSort
     */
    public NameSort()
    {
    }

    public int compare(Customer c1, Customer c2)
    {
        return Character.toString(c1.getName().charAt(0)).compareToIgnoreCase(Character.toString(c2.getName().charAt(0))); 
        //return c1.getName().charAt(0)-c2.getName().charAt(0);
    }
}
