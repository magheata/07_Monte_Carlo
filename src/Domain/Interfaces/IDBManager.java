package Domain.Interfaces;

import Domain.FlagColors;

import java.util.ArrayList;

public interface  IDBManager {
    boolean tableExists(String tableName);

    void createFlagsTable();

    void insertValuesIntoFlagTable(String flag, float red, float orange, float yellow, float green_1, float green_2, float green_3, float blue_1,
                                   float blue_2, float blue_3, float indigo, float pink, float magenta, float black, float white);

    void insertValuesIntoCountryTable();

    ArrayList<String> getAllValuesForColumn(String table, String column);

    ArrayList<FlagColors> getFlagsWithinRange(float margin, float red, float orange, float yellow, float green_1, float green_2, float green_3, float blue_1, float blue_2,
                                              float blue_3, float indigo, float pink, float magenta, float black, float white);

    String getNameOfCountryFlag(String flag);
}
