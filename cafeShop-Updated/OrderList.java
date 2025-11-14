import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.table.*;
import java.util.Comparator;
import java.util.Collections;


public class OrderList extends JPanel{
    private JButton cmdAddCustomer;
    private JButton cmdSortOrder;
    private JButton cmdNameSort;
    private JButton cmdClose;
    private static List<Customer> customers;
    private JTable Table;
    private static DefaultTableModel tableModel;
    private JPanel pnlCommand;

    public OrderList() {
        pnlCommand = new JPanel();

        setLayout(new BorderLayout());
        setBackground(new Color(255, 182, 193));

        JLabel lblTitle = new JLabel("Cafe Orders", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));  
        lblTitle.setForeground(new Color(199, 21, 133)); 
        add(lblTitle, BorderLayout.NORTH);

        customers = loadCustomers("customers.txt");

        String[] columnNames = {"Customer Name", "Order Number"};
        tableModel = new DefaultTableModel(columnNames, 0);

        for (Customer c : customers) {
            tableModel.addRow(new Object[]{c.getName(), c.getCustomerNumber()});
        }

        Table = new JTable(tableModel);
        Table.setFillsViewportHeight(true);
        Table.setRowHeight(30);
        Table.setBackground(new Color(199, 21, 133));
        Table.setForeground(Color.WHITE);
        Table.setGridColor(Color.WHITE);

        Table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = Table.getSelectedRow();
                if (selectedRow >= 0) {
                    String customerName = (String) Table.getValueAt(selectedRow, 0);
                    int orderNumber = Integer.parseInt(Table.getValueAt(selectedRow, 1).toString());
        
                    // Finding the corresponding customer in the list
                    Customer selectedCustomer = customers.stream()
                        .filter(c -> c.getName().equals(customerName) && c.getCustomerNumber() == orderNumber)
                        .findFirst()
                        .orElse(null);
        
                    if (selectedCustomer != null) {
                        // Pass name, order number, and order items to the new class
                        new OrderScreen(selectedCustomer);
                    }
                }
            }
        });

        JTableHeader header = Table.getTableHeader();
        header.setBackground(new Color(255, 192, 203));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Elephant", Font.BOLD, 18));

        JScrollPane scrollPane = new JScrollPane(Table);
        scrollPane.getViewport().setBackground(new Color(255, 182, 193));
        add(scrollPane, BorderLayout.CENTER);

        Color btnBg = new Color(255, 218, 185);

        cmdAddCustomer   = new JButton("Add Customer");
        cmdSortOrder  = new JButton("Sort by Order #");
        cmdNameSort = new JButton("Sort by Name");
        cmdClose   = new JButton("Close");

        cmdSortOrder.setBackground(btnBg);
        cmdClose.setBackground(btnBg);
        cmdAddCustomer.setBackground(btnBg);
        cmdNameSort.setBackground(btnBg);
        cmdAddCustomer.setForeground(Color.BLACK);
        cmdSortOrder.setForeground(Color.BLACK);
        cmdClose.setForeground(Color.BLACK);
        cmdNameSort.setForeground(Color.BLACK);

        cmdSortOrder.addActionListener(new SortOrderListener());
        cmdNameSort.addActionListener(new NameSortListener());
        cmdClose.addActionListener(new CloseListener());
        cmdAddCustomer.addActionListener(new AddCustomerListener());
        
        pnlCommand.add(cmdAddCustomer);
        pnlCommand.add(cmdNameSort);
        pnlCommand.add(cmdSortOrder);
        pnlCommand.add(cmdClose);
       
        add(pnlCommand, BorderLayout.SOUTH);
    }

        public static List<Customer> getCustomerList() {
        return customers;
    }
    
    private List<Customer> loadCustomers(String fileName) {
        List<Customer> list = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(" ");
                if (parts.length < 3) {
                    continue;
                }
                String name = parts[0] + " " + parts[1];
                int orderNum = Integer.parseInt(parts[2].replace("#", ""));
                List<String> orderItems = new ArrayList<>();
                if (parts.length > 3) {
                    String[] items = parts[3].split(";");
                    for (String item : items) {
                        if (!item.trim().isEmpty()) {
                            orderItems.add(item.trim());
                        }
                    }
                }
                list.add(new Customer(name, orderNum, orderItems));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(255, 105, 180, 100));
        int dotDiameter = 10;
        int gap = 20;
        for (int x = 0; x < getWidth(); x += gap) {
            for (int y = 0; y < getHeight(); y += gap) {
                g.fillOval(x, y, dotDiameter, dotDiameter);
            }
        }
    }
    
    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Cafe Orders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        OrderList contentPane = new OrderList();
        contentPane.setOpaque(true);
        frame.setContentPane(contentPane);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public static void resetTable() {
            tableModel.setRowCount(0);
            for (Customer c : customers) {
                tableModel.addRow(new Object[]{c.getName(), c.getCustomerNumber()});
            }
        }

    private class SortOrderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            customers.sort(Comparator.comparingInt(Customer::getCustomerNumber));
            tableModel.setRowCount(0);
            for (Customer c : customers) {
                tableModel.addRow(new Object[]{c.getName(), c.getCustomerNumber()});
            }
        }
    }
    
    private class NameSortListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Collections.sort(customers, new NameSort());
            tableModel.setRowCount(0);
            for (Customer c : customers) {
                tableModel.addRow(new Object[]{c.getName(), c.getCustomerNumber()});
            }
        }
    }

    private class CloseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
    
    private class AddCustomerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new AddCustomerScreen();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OrderList::createAndShowGUI);
    }
}