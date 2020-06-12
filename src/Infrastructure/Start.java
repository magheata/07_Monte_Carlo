/* Created by andreea on 11/06/2020 */
package Infrastructure;

import Infrastructure.DB.DBManager;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Start {
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            DBManager dbManager = new DBManager();
            dbManager.insertValuesIntoCountryTable();
        }
        catch( Exception e )
        {
            System.out.println( e.getMessage() );
        }
    }
}