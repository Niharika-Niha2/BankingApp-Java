import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class BankAppGUI extends JFrame {
    private Bank bank;
    private JPanel mainPanel;

    public BankAppGUI() {
        bank = new Bank();
        setTitle("Bank Application");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new CardLayout());

        // Create all pages/panels
        JPanel menuPanel = createMenuPanel();
        JPanel createAccountPanel = createCreateAccountPanel();
        JPanel depositPanel = createDepositPanel();
        JPanel withdrawPanel = createWithdrawPanel();
        JPanel displayPanel = createDisplayPanel();

        // Add panels to mainPanel with identifiers
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(createAccountPanel, "CreateAccount");
        mainPanel.add(depositPanel, "Deposit");
        mainPanel.add(withdrawPanel, "Withdraw");
        mainPanel.add(displayPanel, "Display");

        add(mainPanel);
        showPanel("Menu");
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(5,1,10,10));

        JButton createBtn = new JButton("Create Account");
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton displayBtn = new JButton("Display All Accounts");
        JButton exitBtn = new JButton("Exit");

        createBtn.addActionListener(e -> showPanel("CreateAccount"));
        depositBtn.addActionListener(e -> showPanel("Deposit"));
        withdrawBtn.addActionListener(e -> showPanel("Withdraw"));
        displayBtn.addActionListener(e -> {
            updateDisplayPanel();
            showPanel("Display");
        });
        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(createBtn);
        panel.add(depositBtn);
        panel.add(withdrawBtn);
        panel.add(displayBtn);
        panel.add(exitBtn);

        panel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        return panel;
    }

    private JPanel createCreateAccountPanel() {
        JPanel panel = new JPanel(new GridLayout(5,2,10,10));

        JLabel accNoLabel = new JLabel("Account Number:");
        JTextField accNoField = new JTextField();

        JLabel nameLabel = new JLabel("Account Holder Name:");
        JTextField nameField = new JTextField();

        JLabel initBalLabel = new JLabel("Initial Deposit:");
        JTextField initBalField = new JTextField();

        JButton submitBtn = new JButton("Create");
        JButton backBtn = new JButton("Back");

        submitBtn.addActionListener(e -> {
            try {
                int accNo = Integer.parseInt(accNoField.getText());
                String name = nameField.getText();
                double initBal = Double.parseDouble(initBalField.getText());

                Account newAcc = new Account(accNo, name, initBal);
                bank.addAccount(newAcc);
                JOptionPane.showMessageDialog(this, "Account created successfully!");
                accNoField.setText("");
                nameField.setText("");
                initBalField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid input.");
            }
        });

        backBtn.addActionListener(e -> showPanel("Menu"));

        panel.add(accNoLabel);
        panel.add(accNoField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(initBalLabel);
        panel.add(initBalField);
        panel.add(submitBtn);
        panel.add(backBtn);

        panel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        return panel;
    }

    private JPanel createDepositPanel() {
        JPanel panel = new JPanel(new GridLayout(4,2,10,10));

        JLabel accNoLabel = new JLabel("Account Number:");
        JTextField accNoField = new JTextField();

        JLabel amountLabel = new JLabel("Deposit Amount:");
        JTextField amountField = new JTextField();

        JButton submitBtn = new JButton("Deposit");
        JButton backBtn = new JButton("Back");

        submitBtn.addActionListener(e -> {
            try {
                int accNo = Integer.parseInt(accNoField.getText());
                double amount = Double.parseDouble(amountField.getText());
                bank.depositToAccount(accNo, amount);
                JOptionPane.showMessageDialog(this, "Deposit successful!");
                accNoField.setText("");
                amountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid input.");
            }
        });

        backBtn.addActionListener(e -> showPanel("Menu"));

        panel.add(accNoLabel);
        panel.add(accNoField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(submitBtn);
        panel.add(backBtn);

        panel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        return panel;
    }

    private JPanel createWithdrawPanel() {
        JPanel panel = new JPanel(new GridLayout(4,2,10,10));

        JLabel accNoLabel = new JLabel("Account Number:");
        JTextField accNoField = new JTextField();

        JLabel amountLabel = new JLabel("Withdraw Amount:");
        JTextField amountField = new JTextField();

        JButton submitBtn = new JButton("Withdraw");
        JButton backBtn = new JButton("Back");

        submitBtn.addActionListener(e -> {
            try {
                int accNo = Integer.parseInt(accNoField.getText());
                double amount = Double.parseDouble(amountField.getText());
                bank.withdrawFromAccount(accNo, amount);
                JOptionPane.showMessageDialog(this, "Withdrawal processed!");
                accNoField.setText("");
                amountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid input.");
            }
        });

        backBtn.addActionListener(e -> showPanel("Menu"));

        panel.add(accNoLabel);
        panel.add(accNoField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(submitBtn);
        panel.add(backBtn);

        panel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        return panel;
    }

    private JTextArea displayArea;

    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> showPanel("Menu"));

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backBtn, BorderLayout.SOUTH);

        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        return panel;
    }

    private void updateDisplayPanel() {
        List<Account> accounts = bank.getAllAccounts();
        StringBuilder sb = new StringBuilder();
        for (Account acc : accounts) {
            sb.append(acc.getDetails()).append("\n");
        }
        displayArea.setText(sb.toString());
    }

    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout)(mainPanel.getLayout());
        cl.show(mainPanel, panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankAppGUI app = new BankAppGUI();
            app.setVisible(true);
        });
    }
}
