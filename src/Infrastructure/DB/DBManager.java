/* Created by andreea on 11/06/2020 */
package Infrastructure.DB;

import Config.Constants;
import Infrastructure.ReaderService;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DBManager {

    private Connection con;
    private ReaderService reader;
    private ArrayList<String> countries = new ArrayList<>();
    private ArrayList<String> codes = new ArrayList<>();

    public DBManager() {
        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:"+ Constants.USER_PATH + "/db/ColorimetryFlags", "", "" );
            reader = new ReaderService();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean tableExists(String tableName){
        Statement stmt;
        try {
            stmt = con.createStatement();
            stmt.executeQuery("SELECT * FROM "+ tableName);
        } catch (SQLException throwables) {
            if (throwables.getErrorCode() == Constants.ERROR_CODE_TABLE_NOT_FOUND){
                return false;
            }
        }
        return true;
    }

    /**
     * 0 - RED
     * 30 - ORANGE
     * 60 - YELLOW
     * 90 - GREEN 1
     * 120 - GREEN 2
     * 150 - GREEN 3
     * 180 - BLUE 1
     * 210 - BLUE 2
     * 240 - BLUE 3
     * 270 - INDIGO
     * 300 - PINK
     * 330 - MAGENTA
     */

    public void createFlagsTable(){
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate( "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_FLAGS + " ( flag varchar (6), " +
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
                    " black float)" );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void insertValuesIntroFlafTable(String flag, float red, float orange, float yellow, float green_1, float green_2, float green_3, float blue_1,
                                           float blue_2, float blue_3, float indigo, float pink, float magenta, float black, float white){
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO " + Constants.TABLE_FLAGS + "(flag, red, orange, yellow, green_1, green_2, green_3, blue_1, blue_2, blue_3, indigo, pink, magenta, black, white) " +
                    "VALUES ('"+ flag +"', " +
                    "" + red +", " +
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
    public void insertValuesIntoCountryTable(){
        try {
            readCountryFiles();
            Iterator countryIterator = countries.iterator();
            Iterator codesIterator = codes.iterator();
            Statement stmt = con.createStatement();
            stmt.executeUpdate( "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_COUNTRY + " ( country varchar (100), code varchar (2), flag varchar(6) )" );

            while (countryIterator.hasNext() && codesIterator.hasNext()){
                stmt.executeUpdate(insertIntoCountryTableQuery((String) countryIterator.next(), (String) codesIterator.next()));
            }
            stmt.close();
        }
        catch( SQLException e ) { }
    }

    private void readCountryFiles(){
        if (codes.isEmpty()){
            codes = reader.readFile(Constants.USER_PATH + "/" + Constants.FILE_COUNTRY_CODES);
        }

        if (countries.isEmpty()){
            countries = reader.readFile(Constants.USER_PATH + "/" + Constants.FILE_COUNTRY_NAMES);
        }
    }

    private String insertIntoCountryTableQuery(String country, String code){
        return "INSERT INTO "+ Constants.TABLE_COUNTRY +" ( country, code, flag) VALUES ( '" + country + "', '" + code + "' , '" + code.toLowerCase() + ".png')";
    }

    private void selectAllFromCountryTable(){
        Statement stmt;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM "+ Constants.TABLE_COUNTRY);
            while( rs.next() )
            {
                String name = rs.getString("country");
                String code = rs.getString("code");
                String flag = rs.getString("flag");

                System.out.println( name + " " + code + " " + flag);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void selectAllFromFlagsTable(){
        Statement stmt;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM "+ Constants.TABLE_FLAGS);
            while( rs.next() )
            {
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

                System.out.println( flag + " RED: " + red + " ORANGE: " + orange +
                        " YELLOW: " + yellow +
                        " GREEN 1: " + green1 +
                        " GREEN 2: " + green2 +
                        " GREEN 3: " + green3 +
                        " BLUE 1: " + blue1 +
                        " BLUE 2: " + blue2 +
                        " BlUE 3: " + blue3 +
                        " INDIGO: " + indigo +
                        " PINK: " + pink +
                        " MAGENTA: " + magenta +
                        " BLACK: " + black +
                        " WHITE: " + white
                );
                System.out.println();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ArrayList<String> getAllValuesForColumn(String table, String column){
        Statement stmt;
        ArrayList<String> values = new ArrayList<>();
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM "+ table);
            while( rs.next() )
            {
                values.add(rs.getString(column));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return values;
    }
}
