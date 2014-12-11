package MainPackage; /**
 * Created by philippe on 03.11.14.
 */

import MyView.*;
import Controller.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Planner {

    static boolean EXIT = false;
    private static MyView view = null;
    final private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static Controller c = null;

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

    public static void main(String[] args) {
        String datapath = args != null ? args[0] : "";
        if (datapath == null) {
            System.out.print("Switching to default Studium!\n");
            datapath = "file.txt";
        }
        c = Controller.getController(args, datapath);
        ArrayList<ArrayList<Boolean>> arb = new ArrayList<ArrayList<Boolean>>();
        ArrayList<ArrayList<String>> names = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < c.getStudium().getSemester().size(); i++) {
            arb.add(new ArrayList<Boolean>());
            names.add(new ArrayList<String>());
            for (int j = 0; j < c.getStudium().getSemester().get(i).getModules().size(); j++) {
                arb.get(i).add(c.getStudium().getSemester().get(i).getModules().get(j).isAbsolved());
                names.get(i).add(c.getStudium().getSemester().get(i).getModules().get(j).getName());
            }
        }
        view = new MyView(arb, names);
        view.setVisible(true);
    }

    public static void updateWithUserInput(ArrayList<String> notAbsolvedNames, MyView currView, int sem, int cp, int hs){
        currView.exit();
        for(String s : notAbsolvedNames){
            c.getStudium().setNotAbsolved(s);
        }
        c.reorganizeModules(sem, cp, hs);
        ArrayList<ArrayList<Boolean>> arb = new ArrayList<ArrayList<Boolean>>();
        ArrayList<ArrayList<String>> names = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < c.getStudium().getSemester().size(); i++) {
            arb.add(new ArrayList<Boolean>());
            names.add(new ArrayList<String>());
            for (int j = 0; j < c.getStudium().getSemester().get(i).getModules().size(); j++) {
                arb.get(i).add(c.getStudium().getSemester().get(i).getModules().get(j).isAbsolved());
                names.get(i).add(c.getStudium().getSemester().get(i).getModules().get(j).getName());
            }
        }
        MyView newView = new MyView(arb, names, c.getStudium().getSemester().size(), sem);
        view.exit();
        newView.setVisible(true);
        view = newView;
    }

    public static int getCpMax(){
        return c.getStudium().getcPMax();
    }

    public static int getHMax(){
        return c.getStudium().gethMax();
    }

}
