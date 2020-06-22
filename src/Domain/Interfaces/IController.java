package Domain.Interfaces;

import Domain.FlagColors;

import java.util.concurrent.Future;

public interface IController {
    void closeConnection();
    FlagColors findColorPercentageOfImage(String path);
    Future<Object[]> getCountryForFlag(String flagPath, int iterations, int samples);
    String getCountryForFlag(String flagImage);
    void loadFlagsTable();
}
