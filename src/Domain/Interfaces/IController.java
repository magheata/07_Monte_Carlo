package Domain.Interfaces;

import Domain.FlagColors;

public interface IController {
    void loadFlagsTable();
    FlagColors findColorPercentageOfImage(String path);
}
