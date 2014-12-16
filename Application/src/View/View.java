package View;

import Studium.Studium;

import javax.swing.*;

/**
 * Created by philippe on 16.12.14.
 */
public abstract class View extends JFrame {
    public abstract void refresh(Studium studium);

    public abstract void exit();
}
