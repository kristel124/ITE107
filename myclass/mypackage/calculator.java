package myPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Calculator extends JFrame implements ActionListener {

    private JTextField display;
    private double num1, num2, result;
    private String operator;

    public Calculator() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 350);
        setLayout(new BorderLayout());

        getContentPane().setBackground(Color.BLACK);

        display = new JTextField();
        display.setEditable(false);
        display.setBackground(Color.BLACK);
        display.setForeground(Color.WHITE);
        add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4));

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "Backspace", "C"
        };

        for (String button : buttons) {
            JButton b = new JButton(button);
            b.addActionListener(this);
            buttonPanel.add(b);
        }

        add(buttonPanel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenuItem computeMenuItem = new JMenuItem("Compute");
        computeMenuItem.addActionListener(e -> display.setText(""));

        JMenuItem historyMenuItem = new JMenuItem("View History");
        historyMenuItem.addActionListener(e -> displayHistory());

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));

        menu.add(computeMenuItem);
        menu.add(historyMenuItem);
        menu.addSeparator();
        menu.add(exitMenuItem);
        menuBar.add(menu);

        setJMenuBar(menuBar);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (Character.isDigit(command.charAt(0)) || command.equals(".")) {
            display.setText(display.getText() + command);
        } else if (command.equals("=")) {
            try {
                num2 = Double.parseDouble(display.getText());
                calculate();
                display.setText(String.valueOf(result));
                appendCalculationToFile(num1, operator, num2, result);
            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
        } else if (command.equals("Backspace")) {
            String text = display.getText();
            if (text.length() > 0) {
                display.setText(text.substring(0, text.length() - 1)); 
            }
        } else if (command.equals("C")) {
            display.setText(""); 
        } else {
            try {
                operator = command;
                num1 = Double.parseDouble(display.getText());
                display.setText("");
            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
        }
    }

    private void calculate() {
        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                result = num1 / num2;
                break;
        }
    }

    private void appendCalculationToFile(double num1, String operator, double num2, double result) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("calculator_history.txt", true))) {
            writer.write(num1 + " " + operator + " " + num2 + " = " + result + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayHistory() {
        StringBuilder history = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("calculator_history.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                history.append(line).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return; 
        }

        JTextArea historyArea = new JTextArea(history.toString());
        historyArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyArea); 
        scrollPane.setPreferredSize(new Dimension(300, 400)); 
        JOptionPane.showMessageDialog(this, scrollPane, "Calculation History", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
