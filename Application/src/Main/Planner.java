package Main; /**
 * Created by philippe on 03.11.14.
 */

import Manager.StudiumManager;
import View.FirstView;
import View.MyView;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Planner {

    final private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static JFrame view = null;
    private static StudiumManager c = null;
    private static int semester = 1;
    private static int credits = 1;
    private static int houres = 1;

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

    private static void refreshView() {
        ((MyView) view).refresh(c.getStudium());
    }

    public static void main(String[] args) {
        String datapath = "";
        if (args.length != 0) {
            datapath = args[0];
        }
        if (args == null) {
            System.out.print("Switching to default Studium!\n");
            datapath = "file.txt";
        }
        c = StudiumManager.getController(datapath);
        view = new FirstView();
        view.setVisible(true);
    }

    public static void doReorganize() {
        c.reorganizeModules();
        refreshView();
    }

    public static int getCpMax() {
        return c.getStudium().getcPMax();
    }

    public static int getHMax() {
        return c.getStudium().gethMax();
    }

    public static void shiftModule(String dir) {
        if (dir.equals(((MyView) view).RIGHT)) {
            c.shiftRight();
        } else {
            c.shiftLeft();
        }
        refreshView();
    }

    public static String getModulToShift() {
        return c.getModuleToShift();
    }

    public static void setModulToShift(String text) {
        c.setModuleToShift(text);
        refreshView();
    }

    public static void updateAbsolved(String text) {
        if (c.getStudium().findModulbyName(text).isAbsolved()) {
            c.getStudium().setNotAbsolved(text);
        } else {
            c.getStudium().setAbsolved(text);
        }
        System.out.println(c.getStudium().toString());
        refreshView();
    }

    public static void resetStudium() {
        StudiumManager.resetController();
        c = StudiumManager.getController();
        c.getStudium().setcPMax(credits);
        c.getStudium().setCurrentSemester(semester);
        c.getStudium().sethMax(houres);
        refreshView();
    }

    public static void addInitialData(int currSem, int maxCp, int maxH) {
        c.getStudium().setCurrentSemester(currSem);
        credits = maxCp;
        houres = maxH;
        semester = currSem;
        c.getStudium().setcPMax(maxCp);
        c.getStudium().sethMax(maxH);
        view = new MyView(c.getStudium());
        view.setVisible(true);
    }

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

    public static void stop() {
        view.dispose();
    }

    public static void exchangeViews() {
        view.dispose();
        view = new FirstView();
        view.setVisible(true);
    }
}
