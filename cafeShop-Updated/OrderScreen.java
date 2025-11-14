import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OrderScreen extends JFrame {
    private Customer customer;
    private DefaultListModel<String> listModel;
    private JList<String> itemList;
    private JTextField txtNewItem;
    private JButton btnAddItem;
    private JButton btnDeleteItem;
    private JButton btnSave;
    private List<String> availableProducts = Arrays.asList(
            "Grande", "Hot", "Expresso", "Muffin", "Bagel",
            "Coffee", "Cookie", "Latte", "Tea", "Scone"
    );

    public OrderScreen(Customer customer) {
        this.customer = customer;
        setTitle("Good day " + customer.getName() + " - This is your order details");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.PINK); // Set background to pink

        JLabel lblTitle = new JLabel("Order Details for " + customer.getName(), SwingConstants.CENTER);
        lblTitle.setFont(new Font("Elephant", Font.BOLD, 14));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        for (String item : customer.getOrderItems()) {
            listModel.addElement(item);
        }

        itemList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(itemList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Items"));

        // Edit panel with instruction, text field, and buttons
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        editPanel.setBorder(BorderFactory.createTitledBorder("Add/Delete Items"));
        editPanel.setBackground(Color.PINK);

        JLabel instructions = new JLabel("<html>Type an option from the available products then press Add,<br>or select an item from your items and press Delete to remove</html>");
        instructions.setFont(new Font("SansSerif", Font.PLAIN, 12));

        txtNewItem = new JTextField(10);

        btnAddItem = new JButton("Add");
        btnDeleteItem = new JButton("Delete");
        btnSave = new JButton("Save");

        // Style buttons
        Color buttonPink = new Color(255, 105, 180);
        btnAddItem.setBackground(buttonPink);
        btnAddItem.setForeground(Color.WHITE);
        btnDeleteItem.setBackground(buttonPink);
        btnDeleteItem.setForeground(Color.WHITE);
        btnSave.setBackground(buttonPink);
        btnSave.setForeground(Color.WHITE);

        btnAddItem.addActionListener(new AddItemListener());
        btnDeleteItem.addActionListener(new DeleteItemListener());
        btnSave.addActionListener(new SaveListener());
        
        editPanel.add(instructions);
        editPanel.add(Box.createVerticalStrut(5));
        editPanel.add(txtNewItem);
        editPanel.add(Box.createVerticalStrut(5));
        editPanel.add(btnAddItem);
        editPanel.add(Box.createVerticalStrut(5));
        editPanel.add(btnDeleteItem);
        editPanel.add(Box.createVerticalStrut(5));
        editPanel.add(btnSave);

        // Available products panel
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBorder(BorderFactory.createTitledBorder("Available Products"));
        productPanel.setBackground(Color.PINK);

        DefaultListModel<String> productListModel = new DefaultListModel<>();
        for (String product : availableProducts) {
            productListModel.addElement(product);
        }
        JList<String> productList = new JList<>(productListModel);
        productPanel.add(new JScrollPane(productList), BorderLayout.CENTER);

        // Combine edit and available list side-by-side
        JPanel sidePanel = new JPanel(new GridLayout(1, 2));
        sidePanel.setBackground(Color.PINK);
        sidePanel.add(editPanel);
        sidePanel.add(productPanel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(sidePanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    public void saveOrders(String fileName) {
        List<Customer> list = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
           FileWriter writer = null;
           boolean inList = false;
           String orderLines = "";
           writer = new FileWriter(fileName);         
           String joinedOrderList;

           for (Customer cus : OrderList.getCustomerList()){
               joinedOrderList = String.join(";", cus.getOrderItems());
               orderLines = orderLines + cus.getName() + " #" + cus.getCustomerNumber() + " " + joinedOrderList + System.lineSeparator();
           }
            
           writer.write(orderLines);
           writer.close();
           
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

    }    
    // Add item if valid
    private class AddItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String newItem = txtNewItem.getText().trim();
            if (!newItem.isEmpty()) {
                boolean valid = availableProducts.stream()
                        .anyMatch(p -> p.equalsIgnoreCase(newItem));
                if (valid) {
                    for (String product : availableProducts) {
                        if (product.equalsIgnoreCase(newItem)) {
                            listModel.addElement(product);
                            customer.getOrderItems().add(product);
                            break;
                        }
                    }
                    txtNewItem.setText("");
                } else {
                    JOptionPane.showMessageDialog(OrderScreen.this,
                            "Item not available. Please choose from the available products.",
                            "Invalid Item",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Remove selected item
    private class DeleteItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = itemList.getSelectedIndex();
            if (selectedIndex != -1) {
                listModel.remove(selectedIndex);
                customer.getOrderItems().remove(selectedIndex);
            }
        }
    }
    
    private class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (customer.getOrderItems().size() == 0) {
                OrderList.getCustomerList().remove(customer);
            }
            OrderList.resetTable();
            saveOrders("customers.txt");
            setVisible(false);
        }
    }

    // Test run
    //public static void main(String[] args) {
    //    SwingUtilities.invokeLater(() -> new OrderScreen("Shequan Osbourne", 1, Arrays.asList("Espresso", "Muffin")));
    //}
}
