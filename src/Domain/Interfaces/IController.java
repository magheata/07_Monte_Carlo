package Domain.Interfaces;

import Domain.FlagColors;

public interface IController {
    void loadFlagsTable();
    FlagColors findColorPercentageOfImage(String path);
    String getCountryForFlag(String flagPath, int iterations, int samples);
    String getCountryForFlag(String flagImage);
    void closeConnection();
}
