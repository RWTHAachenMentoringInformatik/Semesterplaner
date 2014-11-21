package MyView;

import Controller.Controller;
import HelperClasses.SommerWinterSemester;

/**
 * Created by philippe on 11.11.14.
 */
public class MyView /*extends View*/{

    Controller c;

    public void setC(Controller c) {
        this.c = c;
    }

    // DO NOT TOUCH!
    private static MyView instance;

    // This one should instanciate your View at first call (Do not set the Controller.. will be set by 'Planner')!
    private MyView(){
        //TODO
    }

    //This one should update your view with all data you can get from 'Controller c' (Look at the class for details!)
    public void update() {
        //Todo
    }

    //returns true if user started in winterterm
    public boolean getUserInputStartSemester() {
        //TODO
        return true;
    }

    //returns the wanted semester number
    public short getSemesterWanted() {
        //TODO
        return (short) 42;
    }

    //returns all absolved modules by now
    public boolean[] getAbsolvedModules() {
        //Todo
        return null;
    }

    // DO NOT TOUCH!
    public static MyView getView(){
        if(instance==null){
            instance = new MyView();
        }
        return instance;
    }


    public boolean hasNewInput() {
        //Todo
        return true;
    }

    public boolean userWantsToExit() {
        //Todo
        return true;
    }

    //FOR LATER: SECOND ALGORITHM-> FOR ALEX
    public Object getUserInputRearange() {
        return null;
    }

    public boolean userWantsReorganize() {
        return false;
    }



}
