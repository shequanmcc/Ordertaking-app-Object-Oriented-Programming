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
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.table.*;
import java.util.Comparator;
import java.util.Collections;
import javax.swing.*;


public class AddCustomerScreen extends JFrame
{
    private JButton cmdAddCustomer;
    private JButton cmdClose;
    private JTextField txtCustomerName;
    private JLabel addNametxt;
    

    public AddCustomerScreen()
    {
        setTitle("Add Customer");
        setSize(400, 100);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.PINK);
        
        addNametxt = new JLabel("Enter new customer name: ");
        txtCustomerName = new JTextField(10);
        
        Color btnBg = new Color(255, 218, 185);
        
        cmdAddCustomer = new JButton("Add Customer");
        cmdAddCustomer.setBackground(btnBg);
        cmdAddCustomer.setForeground(Color.BLACK);
        cmdClose = new JButton("Close");
        cmdClose.setBackground(btnBg);
        cmdClose.setForeground(Color.BLACK);

        mainPanel.add(addNametxt);
        mainPanel.add(txtCustomerName);
        mainPanel.add(cmdAddCustomer);
        mainPanel.add(cmdClose);
        
        cmdClose.addActionListener(new CloseListener());
        cmdAddCustomer.addActionListener(new AddCustomerListener());
        
        add(mainPanel);
        setVisible(true);
    }

    private class CloseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }
    
    private class AddCustomerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String customerName = txtCustomerName.getText().trim();
            Customer newCustomer = new Customer(customerName);
            new OrderScreen(newCustomer);
            OrderList.getCustomerList().add(newCustomer);
            OrderList.resetTable();
            setVisible(false);
        }
    }
}
