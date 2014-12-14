package MainPackage; /**
 * Created by philippe on 03.11.14.
 */

import MyView.*;
import Controller.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Planner {

    static boolean EXIT = false;
    private static MyView view = null;
    final private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static Controller c = null;
    private static int semester = 1;
    private static String read(){
        if(br!=null&&view==null) {
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

    private static void refreshView(){
        view.refresh(c.getStudium(), semester);
    }

    public static void main(String[] args) {
        String datapath = "";
        if (args.length!=0) {
            datapath = args[0];
        }
        if (args == null) {
            System.out.print("Switching to default Studium!\n");
            datapath = "file.txt";
        }
        c = Controller.getController(datapath);
        view = new MyView(c.getStudium(), 1);
        view.setVisible(true);
    }

    public static void doReorganize(int sem, int cp, int hs){
        c.reorganizeModules(sem, cp, hs);
        semester = sem;
        refreshView();
    }

    public static int getCpMax(){
        return c.getStudium().getcPMax();
    }

    public static int getHMax(){
        return c.getStudium().gethMax();
    }

    public static void shiftModule(String dir, String text, int sem) {
        semester = sem;
        if(dir.equals(view.RIGHT)){
            c.shiftRight(text,  sem);
        }else{
            c.shiftLeft(text,  sem);
        }
        refreshView();
    }

    public static void updateAbsolved(String text, int sem) {
        semester = sem;
        if(c.getStudium().findModulbyName(text).isAbsolved()){
            c.getStudium().setNotAbsolved(text);
        }else{
            c.getStudium().setAbsolved(text);
        }
        refreshView();
    }

    public static void resetStudium() {
        Controller.resetController();
        c = Controller.getController();
        refreshView();
    }
}
