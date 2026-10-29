package dataaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        loadPropertiesFromResources();
    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    public static void deleteFromDatabase(String databaseCall, Object... args) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement(databaseCall, Statement.RETURN_GENERATED_KEYS)) {
                int i = 1;
                for (Object arg : args) {
                    if (arg instanceof String a){
                        preparedStatement.setString(i, a);
                    }
                    else if (arg instanceof Integer a) {
                        preparedStatement.setInt(i, a);
                    }
                    i++;
                }
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error While Connecting to the Database!", ex);
        }
    }

    public static ArrayList<ArrayList<Object>> getFromDatabase(String databaseCall, Object... args) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement(databaseCall)) {
                int i = 1;
                for (Object arg : args) {
                    if (arg instanceof String a){
                        preparedStatement.setString(i, a);
                    }
                    else if (arg instanceof Integer a) {
                        preparedStatement.setInt(i, a);
                    }
                    i++;
                }
                try (var result = preparedStatement.executeQuery()) {
                    return compileResults(result);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot Connect to Database!");
        }
    }

    private static ArrayList<ArrayList<Object>> compileResults(ResultSet result) throws SQLException {
        ArrayList<ArrayList<Object>> results = new ArrayList<>();
        int columnCount = result.getMetaData().getColumnCount();
        while (result.next()) {
            ArrayList<Object> rowValues = new ArrayList<>();
            for (int i = 0; i < columnCount; i++) {
                rowValues.add(result.getObject(i + 1));
            }
            results.add(rowValues);
        }
        return results;
    }

    public static int addToDatabase(String databaseCall, Object... args) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement(databaseCall, Statement.RETURN_GENERATED_KEYS)) {
                int i = 1;
                for (Object arg : args) {
                    if (arg instanceof String a){
                        preparedStatement.setString(i, a);
                    }
                    else if (arg instanceof Integer a) {
                        preparedStatement.setInt(i, a);
                    }
                    else if (arg == null) {
                        preparedStatement.setObject(i, null);
                    }
                    i++;
                }
                preparedStatement.executeUpdate();
                var resultSet = preparedStatement.getGeneratedKeys();
                int id = 0;
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
                return id;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error While Connecting to the Database!", ex);
        }
    }

    public static void configureDatabase(String[] createStatements) throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot Connect to Database!");
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }
}
