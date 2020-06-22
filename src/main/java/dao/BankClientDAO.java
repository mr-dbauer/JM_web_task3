package dao;

//import com.sun.deploy.util.SessionState;

import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        List<BankClient> list = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from bank_client");
        while (resultSet.next()) {
            long id = resultSet.getLong(1);
            String name = resultSet.getString(2);
            String password = resultSet.getString(3);
            long money = resultSet.getLong(4);
            list.add(new BankClient(id, name, password, money));
        }
        resultSet.close();
        stmt.close();
        return list;
    }

    public boolean validateClient(String name, String password) {
        try (PreparedStatement stmt = connection.prepareStatement(" select * from bank_client where name = ?")) {
            stmt.setString(1, name);
            stmt.execute();
            ResultSet resultSet = stmt.getResultSet();
            return resultSet.next();
        } catch (SQLException q) {
            q.printStackTrace();
            return false;
        }
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(" select * from bank_client where name = ? and password = ?");
        stmt.setString(1, name);
        stmt.setString(2, password);
        stmt.execute();
        ResultSet resultSet = stmt.getResultSet();
        if (resultSet.next()) {
            long id = resultSet.getLong(1);
            String passwordClientDB = resultSet.getString(3);
            long money = resultSet.getLong(4);
            if (passwordClientDB.equals(password)) {
                money = money + transactValue;
                PreparedStatement preparedStatement = connection.prepareStatement(" update bank_client set money = ? where id = ?");
                preparedStatement.setLong(1, money);
                preparedStatement.setLong(2, id);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
        }
        resultSet.close();
        stmt.close();

    }

    public BankClient getClientById(long id) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from bank_client where id ='" + id + "'");
        //resultSet.next();
        String name = resultSet.getString(2);
        String password = resultSet.getString(3);
        long money = resultSet.getLong(4);
        stmt.close();
        resultSet.close();
        return new BankClient(id, name, password, money);
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(" select * from bank_client where name = ?");
        stmt.setString(1, name);
        stmt.execute();
        ResultSet resultSet = stmt.getResultSet();
        if (resultSet.next()) {
            long money = resultSet.getLong(4);
            stmt.close();
            resultSet.close();
            if (money >= expectedSum) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public BankClient getClientByName(String name) {
        try {
            PreparedStatement stmt = connection.prepareStatement(" select * from bank_client where name = ?");
            stmt.setString(1, name);
            stmt.execute();
            ResultSet resultSet = stmt.getResultSet();
            if (resultSet.next()) {
                long id = resultSet.getLong(1);
                String password = resultSet.getString(3);
                long money = resultSet.getLong(4);
                stmt.close();
                resultSet.close();
                return new BankClient(id, name, password, money);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public void addClient(BankClient client) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(" insert into bank_client (name, password, money) values (?, ?, ?)");
        stmt.setString(1, client.getName());
        stmt.setString(2, client.getPassword());
        stmt.setLong(3, client.getMoney());
        stmt.execute();
        stmt.close();
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }
}
