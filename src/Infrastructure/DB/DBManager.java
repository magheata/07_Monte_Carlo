/* Created by andreea on 11/06/2020 */
package Infrastructure.DB;

import Config.Constants;
import Domain.FlagColors;
import Domain.Interfaces.IDBManager;
import Infrastructure.ReaderService;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DBManager implements IDBManager {

    private Connection con;
    private ReaderService reader;
    private ArrayList<String> countries = new ArrayList<>();
    private ArrayList<String> codes = new ArrayList<>();

    public DBManager() {
        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:" + Constants.USER_PATH + "/db/ColorimetryFlags", "", "");
            reader = new ReaderService();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean tableExists(String tableName) {
        Statement stmt;
        try {
            stmt = con.createStatement();
            stmt.executeQuery("SELECT * FROM " + tableName);
        } catch (SQLException throwables) {
            if (throwables.getErrorCode() == Constants.ERROR_CODE_TABLE_NOT_FOUND) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void createFlagsTable() {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + Constants.TABLE_FLAGS + " ( flag varchar (6), " +
                    " red float, " +
                    " orange float," +
                    " yellow float," +
                    " green_1 float," +
                    " green_2 float," +
                    " green_3 float," +
                    " blue_1 float," +
                    " blue_2 float," +
                    " blue_3 float," +
                    " indigo float," +
                    " pink float," +
                    " magenta float," +
                    " white float," +
                    " black float)");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void insertValuesIntoFlagTable(String flag, float red, float orange, float yellow, float green_1, float green_2, float green_3, float blue_1,
                                          float blue_2, float blue_3, float indigo, float pink, float magenta, float black, float white) {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO " + Constants.TABLE_FLAGS + "(flag, red, orange, yellow, green_1, green_2, green_3, blue_1, blue_2, blue_3, indigo, pink, magenta, black, white) " +
                    "VALUES ('" + flag + "', " +
                    "" + red + ", " +
                    orange + ", " +
                    yellow + ", " +
                    green_1 + ", " +
                    green_2 + ", " +
                    green_3 + ", " +
                    blue_1 + "," +
                    blue_2 + ", " +
                    blue_3 + "," +
                    indigo + ", " +
                    pink + ", " +
                    magenta + ", " +
                    black + ", " +
                    white + ")");
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void insertValuesIntoCountryTable() {
        try {
            readCountryFiles();
            Iterator countryIterator = countries.iterator();
            Iterator codesIterator = codes.iterator();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + Constants.TABLE_COUNTRY + " ( country varchar (100), code varchar (2), flag varchar(6) )");

            while (countryIterator.hasNext() && codesIterator.hasNext()) {
                stmt.executeUpdate(insertIntoCountryTableQuery((String) countryIterator.next(), (String) codesIterator.next()));
            }
            selectAllFromCountryTable();
            stmt.close();
        } catch (SQLException e) {
        }
    }

    private void readCountryFiles() {
        if (codes.isEmpty()) {
            codes = reader.readFile(Constants.USER_PATH + "/" + Constants.FILE_COUNTRY_CODES);
        }

        if (countries.isEmpty()) {
            countries = reader.readFile(Constants.USER_PATH + "/" + Constants.FILE_COUNTRY_NAMES);
        }
    }

    private String insertIntoCountryTableQuery(String country, String code) {
        return "INSERT INTO " + Constants.TABLE_COUNTRY + " ( country, code, flag) VALUES ( '" + country + "', '" + code + "' , '" + code.toLowerCase() + ".png')";
    }

    private void selectAllFromCountryTable() {
        Statement stmt;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + Constants.TABLE_COUNTRY);
            while (rs.next()) {
                String name = rs.getString("country");
                String code = rs.getString("code");
                String flag = rs.getString("flag");
                System.out.println(name + " " + code + " " + flag);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void selectAllFromFlagsTable() {
        Statement stmt;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + Constants.TABLE_FLAGS);
            while (rs.next()) {
                String flag = rs.getString("flag");
                String red = rs.getString("red");
                String orange = rs.getString("orange");
                String yellow = rs.getString("yellow");
                String green1 = rs.getString("green_1");
                String green2 = rs.getString("green_2");
                String green3 = rs.getString("green_3");
                String blue1 = rs.getString("blue_1");
                String blue2 = rs.getString("blue_2");
                String blue3 = rs.getString("blue_3");
                String indigo = rs.getString("indigo");
                String pink = rs.getString("pink");
                String magenta = rs.getString("magenta");
                String black = rs.getString("black");
                String white = rs.getString("white");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> getAllValuesForColumn(String table, String column) {
        Statement stmt;
        ArrayList<String> values = new ArrayList<>();
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + table);
            while (rs.next()) {
                values.add(rs.getString(column));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return values;
    }

    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public ArrayList<FlagColors> getFlagsWithinRange(float margin, float red, float orange, float yellow, float green_1, float green_2, float green_3, float blue_1, float blue_2,
                                                     float blue_3, float indigo, float pink, float magenta, float black, float white) {
        Statement stmt;
        ArrayList<FlagColors> flags = new ArrayList<>();
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + Constants.TABLE_FLAGS + " " +
                    "WHERE (red >= " + (red - margin) + " AND red <= " + (red + margin) + ")" +
                    "AND (orange >= " + (orange - margin) + " AND orange <= " + (orange + margin) + ") " +
                    "AND (yellow >= " + (yellow - margin) + " AND yellow <= " + (yellow + margin) + ") " +
                    "AND (green_1 >= " + (green_1 - margin) + " AND green_1 <= " + (green_1 + margin) + ") " +
                    "AND (green_2 >= " + (green_2 - margin) + " AND green_2 <= " + (green_2 + margin) + ") " +
                    "AND (green_3 >= " + (green_3 - margin) + " AND green_3 <= " + (green_3 + margin) + ") " +
                    "AND (blue_1 >= " + (blue_1 - margin) + " AND blue_1 <= " + (blue_1 + margin) + ") " +
                    "AND (blue_2 >= " + (blue_2 - margin) + " AND blue_2 <= " + (blue_2 + margin) + ") " +
                    "AND (blue_3 >= " + (blue_3 - margin) + " AND blue_3 <= " + (blue_3 + margin) + ") " +
                    "AND (indigo >= " + (indigo - margin) + " AND indigo <= " + (indigo + margin) + ") " +
                    "AND (pink >= " + (pink - margin) + " AND pink <= " + (pink + margin) + ") " +
                    "AND (magenta >= " + (magenta - margin) + " AND magenta <= " + (magenta + margin) + ") " +
                    "AND (black >= " + (black - margin) + " AND black <= " + (black + margin) + ") " +
                    "AND (white >= " + (white - margin) + " AND white <= " + (white + margin) + ") ;");
            while (rs.next()) {
                flags.add(new FlagColors(rs.getString("flag"),
                        rs.getFloat("red"),
                        rs.getFloat("orange"),
                        rs.getFloat("yellow"),
                        rs.getFloat("green_1"),
                        rs.getFloat("green_2"),
                        rs.getFloat("green_3"),
                        rs.getFloat("blue_1"),
                        rs.getFloat("blue_2"),
                        rs.getFloat("blue_3"),
                        rs.getFloat("indigo"),
                        rs.getFloat("pink"),
                        rs.getFloat("magenta"),
                        rs.getFloat("black"),
                        rs.getFloat("white")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flags;
    }

    @Override
    public String getNameOfCountryFlag(String flag){
        Statement stmt;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + Constants.TABLE_COUNTRY +
                    " WHERE flag = '" + flag + "';");
            while (rs.next()) {
                return rs.getString("country");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
