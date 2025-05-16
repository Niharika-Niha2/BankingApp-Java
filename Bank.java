import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private Connection conn;

    public Bank() {
        try {
            // Connect to SQLite database (creates file if not exists)
            conn = DriverManager.getConnection("jdbc:sqlite:bankapp.db");
            createTableIfNotExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create account table if it doesn't exist
    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS accounts (" +
                     "accountNumber INTEGER PRIMARY KEY," +
                     "accountHolderName TEXT NOT NULL," +
                     "balance REAL NOT NULL" +
                     ");";
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        stmt.close();
    }

    // Add new account
    public void addAccount(Account acc) {
        try {
            String sql = "INSERT INTO accounts(accountNumber, accountHolderName, balance) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, acc.getAccountNumber());
            pstmt.setString(2, acc.getAccountHolderName());
            pstmt.setDouble(3, acc.getBalance());
            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Account created successfully.");
        } catch (SQLException e) {
            System.out.println("Error: Could not add account. " + e.getMessage());
        }
    }

    // Deposit amount to account
    public void depositToAccount(int accNo, double amount) {
        try {
            Account acc = findAccount(accNo);
            if (acc != null) {
                acc.setBalance(acc.getBalance() + amount);
                updateAccount(acc);
                System.out.println("Deposit successful.");
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error during deposit: " + e.getMessage());
        }
    }

    // Withdraw amount from account
    public void withdrawFromAccount(int accNo, double amount) {
        try {
            Account acc = findAccount(accNo);
            if (acc != null) {
                if (acc.getBalance() >= amount) {
                    acc.setBalance(acc.getBalance() - amount);
                    updateAccount(acc);
                    System.out.println("Withdrawal successful.");
                } else {
                    System.out.println("Insufficient balance.");
                }
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error during withdrawal: " + e.getMessage());
        }
    }

    // Find account by account number
    private Account findAccount(int accNo) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE accountNumber = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, accNo);
        ResultSet rs = pstmt.executeQuery();
        Account acc = null;
        if (rs.next()) {
            acc = new Account(
                rs.getInt("accountNumber"),
                rs.getString("accountHolderName"),
                rs.getDouble("balance")
            );
        }
        rs.close();
        pstmt.close();
        return acc;
    }

    // Update account details in DB
    private void updateAccount(Account acc) throws SQLException {
        String sql = "UPDATE accounts SET accountHolderName = ?, balance = ? WHERE accountNumber = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, acc.getAccountHolderName());
        pstmt.setDouble(2, acc.getBalance());
        pstmt.setInt(3, acc.getAccountNumber());
        pstmt.executeUpdate();
        pstmt.close();
    }

    // Get all accounts from DB
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM accounts";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Account acc = new Account(
                    rs.getInt("accountNumber"),
                    rs.getString("accountHolderName"),
                    rs.getDouble("balance")
                );
                accounts.add(acc);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error fetching accounts: " + e.getMessage());
        }
        return accounts;
    }
}
