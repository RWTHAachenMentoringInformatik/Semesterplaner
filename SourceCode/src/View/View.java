package View;

import Studium.Studium;

import javax.swing.*;

/**
 * Created by philippe on 16.12.14.
 * Your View has to extend from this Class.
 * This abstract Class is a template that builds upon JFrame with
 * minimal restrictions (only two methods to implement). Your View should be able to visualize everything you
 * need in your view by searching in the structure 'Studium'.
 * */
public abstract class View extends JFrame {

    /**
     * Your view has to implement the function refresh to enable the Planner
     * to update your view every time something happens.
     * The passed studium is the current studium and contains all information (up-to-date)
     * */
    public abstract void refresh(Studium studium);

    /**
     *The default function that is called when your view should be closed
     * */
    public abstract void exit();

}
