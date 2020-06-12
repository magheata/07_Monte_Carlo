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

    public void insertValuesIntoCountryTable(){
        try {
            readCountryFiles();
            Iterator countryIterator = countries.iterator();
            Iterator codesIterator = codes.iterator();
            Statement stmt = con.createStatement();

            while (countryIterator.hasNext() && codesIterator.hasNext()){
                stmt.executeUpdate(insertIntoCountryTableQuery((String) countryIterator.next(), (String) codesIterator.next()));
            }

            selectAllFromCountryTable();
            stmt.close();
            con.close();
        }
        catch( SQLException e ) {
            if (e.getErrorCode() == Constants.ERROR_CODE_TABLE_NOT_FOUND){
                System.out.println("Creating Country table...");
                createCountryTable();
                insertValuesIntoCountryTable();
            }
        }
    }

    private void readCountryFiles(){
        if (codes.isEmpty()){
            codes = reader.readFile(Constants.USER_PATH + "/" + Constants.FILE_COUNTRY_CODES);
        }

        if (countries.isEmpty()){
            countries = reader.readFile(Constants.USER_PATH + "/" + Constants.FILE_COUNTRY_NAMES);
        }
    }

    private void createCountryTable(){
        Statement stmt;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate( "CREATE TABLE " + Constants.TABLE_COUNTRY + " ( country varchar (100), code varchar (2), flag varchar(6) )" );
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
}
