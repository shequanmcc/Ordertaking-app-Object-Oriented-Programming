import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class Customer{
    
    private String name;
    private int orderNum;
    private static int orderNumber;
    private List<String> orderItems;

    public Customer(String name, int orderNum, List<String> orderItems){
        this.name  = name;
        this.orderNum = orderNum;
        this.orderItems = orderItems;
    }
    
    public Customer(String name){
        this.name = name;
        orderNumber ++;
        orderNum = orderNumber;
        orderItems = new ArrayList<String>();
        
    }

    public String getName()
    {
        return name;
    }

    public int getCustomerNumber(){
        return orderNum;
    }

    public List<String> getOrderItems(){
        return orderItems;
    }

    public void setOrderItems(List<String> orderItems){
        this.orderItems = orderItems;
    }

    public int compareTo(Customer other) {
        return Integer.compare(this.orderNum, other.orderNum);
    }
    

}