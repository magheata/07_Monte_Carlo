package Domain.Interfaces;

import Domain.FlagColors;

import java.beans.PropertyChangeListener;

public interface IController {
    void loadFlagsTable();
    FlagColors findColorPercentageOfImage(String path);
}
