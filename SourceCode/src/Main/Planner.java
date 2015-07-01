package Main; /**
 * Created by philippe on 03.11.14.
 */

import Manager.StudiumManager;
import Studium.Studium;
import View.FirstView_2;
import View.SaveView;
import View.MyView;
import View.View;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.windows.WindowsLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel;


import javax.swing.*;
import java.io.*;


/**
 * The Planner runs the main program. It instantiates the Controller and the View, then
 * coordinates the communication betwenn the two of them!
 * */
public class Planner {

    final private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static View view = null;
    private static JFrame frame = null;
    private static StudiumManager c = null;
    private static int semester = 1;
    private static int credits = 30;
    private static int houres = 600;

    static String filepath1 = "";
    static String filepath2 = "";

    private static String read() {
        if (br != null && view == null) {
            String input = "";
            try {
                input = br.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return input;
        }
        return null;
    }

    /**
     * This function refreshes your view.
     * */
    private static void refreshView() {
        view.refresh(c.getStudium());
    }

    /**
     * Call this method to start the program.
     * */
    public static void main(String[] args) {
        try {
            //UIManager.setLookAndFeel(new PlasticLookAndFeel());
            UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
            //UIManager.setLookAndFeel(new WindowsLookAndFeel());
            //UIManager.setLookAndFeel(new WindowsClassicLookAndFeel());
        } catch (Exception e) {}

        String datapath = "";
        if(args.length>=1){
            filepath1 = args[0];
        }
        if(args.length>=2){
            filepath2 = args[1];
        }
        view = new FirstView_2();
        view.setVisible(true);


    }

    /**
     * Reorganizes the studium with the user-input
     * */
    public static void doReorganize() {
        c.reorganizeModules();
        refreshView();
    }

    /**Helper-Function for my version of the view*/
    public static int getCpMax() {
        return credits;
    }

    /**Helper-Function for my version of the view*/
    public static int getHMax() {
        return houres;
    }

    /**
     * Pushes the selected Modul to the next ("Right") free Semester
     * (or to the last free Semester "Left")
     * */
    public static void shiftModule(String dir) {
        if (dir.equals(MyView.RIGHT)) {
            c.shiftRight();
        } else {
            c.shiftLeft();
        }
        refreshView();
    }


    public static String getModulToShift() {
        return c.getModuleToShift();
    }

    /**
     * Sets the Modul that can be shifted afterwards with the Method "shiftModule"
     * */
    public static void setModulToShift(String text) {
        c.setModuleToShift(text);
        refreshView();
    }

    /**
     * The Modul that has the same name as "text" will be set Absolved/Not Absolved
     * */
    public static void updateAbsolved(String text) {
        if (c.getStudium().findModulbyName(text).isAbsolved()) {
            c.getStudium().setNotAbsolved(text);
        } else {
            c.getStudium().setAbsolved(text);
        }
        System.out.println(c.getStudium().toString());
        refreshView();
    }

    /**
     * Resets everything except for the initial choices of maximal credits, houres, semester, etc.
     * */
    public static void resetStudium() {
        StudiumManager.resetController();
        c = StudiumManager.getController();
        c.getStudium().setcPMax(credits);
        c.getStudium().setCurrentSemester(semester);
        c.getStudium().sethMax(houres);
        refreshView();
    }

    /**
     * Your View can (and should) deliver the initial Data with this Method!
     * */
    public static void addInitialData(int currSem, int maxCp, int maxH, boolean startInSummer) {
        if(startInSummer){
            if(!filepath2.equals("")){
                c = StudiumManager.getController(filepath2);
            }else if(!filepath1.equals("")){
                c = StudiumManager.getController(filepath1);
            }else{
                c = StudiumManager.getController("file2.txt");
            }
        }else{
            if(!filepath1.equals("")){
                c = StudiumManager.getController(filepath1);
            }else {
                c = StudiumManager.getController("file.txt");
            }
        }
        c.getStudium().setCurrentSemester(currSem);
        credits = maxCp;
        houres = maxH;
        semester = currSem;
        c.getStudium().setcPMax(maxCp);
        c.getStudium().sethMax(maxH);
        view = new MyView(c.getStudium());
        view.setVisible(true);
    }

    /**
     * Helper Function if you want to visualize Tipps the mentoringTeam gives you!
     * */
    public static String requestPopup(String module) {
        String ret = "";
        String s;
        for (int i = 0; i < c.getStudium().findModulbyName(module).getAnmerkungen().size(); i++) {
            s = c.getStudium().findModulbyName(module).getAnmerkungen().get(i);
            if (i != c.getStudium().findModulbyName(module).getAnmerkungen().size() - 1) {
                ret += s + "\n";
            } else {
                ret += s;
            }
        }
        return ret;
    }

    /**
     * Kills your current View!
     * */
    public static void stop() {
        view.dispose();
    }

    /**
     * Kills the current view and Exchanges it with the initial View!
     * */
    public static void exchangeViews() {
        semester = 1;
        credits = 30;
        houres = 600;
        view.dispose();
        view = new FirstView_2();
        view.setVisible(true);
    }

    public static void saveStudiumAsFile() {
        View v = new SaveView();
        v.setVisible(true);
    }

    public static void saveStudiumAsFile(String s) {
        System.out.print(s);
        File file = new File(s);
        String toFile = c.getStudium().toDataString();
        if(file.exists()){
            file.delete();
        }else{
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(toFile);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setNewStudium(String text) {
        c = StudiumManager.getController(text);
        c.getStudium().setCurrentSemester(semester);
        c.getStudium().setcPMax(credits);
        c.getStudium().sethMax(houres);
        view.dispose();
        view = new MyView(c.getStudium());
        view.setVisible(true);
    }
}
