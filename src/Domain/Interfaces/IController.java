package Domain.Interfaces;

import Domain.FlagColors;

import java.util.concurrent.Future;

public interface IController {
    void loadFlagsTable();
    FlagColors findColorPercentageOfImage(String path);
    Future<Object[]> getCountryForFlag(String flagPath, int iterations, int samples);
    String getCountryForFlag(String flagImage);
    void closeConnection();
}
