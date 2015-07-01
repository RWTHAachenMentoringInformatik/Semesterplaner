package Manager;

import Studium.Modul;
import Studium.Studium;
import Parser.DataFileParser;
import Parser.DataParser;

/**
 * Created by philippe on 03.11.14.
 * <p/>
 * Manager ist eine Singelton-Klasse und verkörpert die Logik des Programms
 */
public class StudiumManager {

    static boolean TEXTINPUT = true;
    private static String datapath = "";
    private static StudiumManager instance;
    DataParser dfp;
    short semesterWanted = 0; // get from view
    boolean startedInWinter = false; // get from view
    boolean[] absolvedModules;
    private String moduleToShift = "";
    private boolean modulesReorganizedOnce = false;
    private Studium studium;

    private StudiumManager(String datapath) {
        StudiumManager.datapath = datapath;
        this.dfp = new DataFileParser(datapath);
        this.studium = dfp.parseStudium(datapath);
    }

    public static StudiumManager getController(String datapath) {
        return new StudiumManager(datapath);
    }

    public static StudiumManager getController() {
        if (instance == null) {
            instance = new StudiumManager(datapath);
        }
        return instance;
    }

    public static void resetController() {
        instance = new StudiumManager(datapath);
    }

    public String getModuleToShift() {
        return moduleToShift;
    }

    public void setModuleToShift(String moduleToShift) {
        this.moduleToShift = moduleToShift;
    }

    public DataParser getParser() {
        return dfp;
    }

    public Studium getStudium() {
        return studium;
    }

    public void setStudium(Studium studium) {
        this.studium = studium;
    }

    public void outputStudium() {
        System.out.println(this.studium.toString());
    }

    public void reorganizeModules() {
        reorganizeModules(1, 30, 600);
    }

    public void reorganizeModules(int sem) {
        reorganizeModules(sem, 30, 600);
    }

    public void reorganizeModules(int sem, int cpMax) {
        reorganizeModules(sem, cpMax, 600);
    }

    public void reorganizeModules(int sem, int cpMax, int hMax) {
        studium.setcPMax(cpMax);
        studium.sethMax(hMax);
        studium.reorganize(Math.max(sem, 1), this.copyStudium());
        modulesReorganizedOnce = true;
    }

    public Studium copyStudium() {
        String source = this.getStudium().getSource();
        Studium news = (new DataFileParser(source)).parseStudium(source);
        news.setName(this.getStudium().getName());
        news.setSource(this.getStudium().getSource());
        for (Modul m : news.getModules()) m.setAbsolved(false);
        for (String mod : this.getStudium().getAbsolved()) news.setAbsolved(mod);
        return news;
    }

    public short getSemesterWanted() {
        return semesterWanted;
    }

    public void setSemesterWanted(short semesterWanted) {
        this.semesterWanted = semesterWanted;
    }

    public boolean isStartedInWinter() {
        return startedInWinter;
    }

    public void setStartedInWinter(boolean startedInWinter) {
        this.startedInWinter = startedInWinter;
    }

    public boolean[] getAbsolvedModules() {
        return absolvedModules;
    }

    public void setAbsolvedModules(boolean[] absolvedModules) {
        this.studium.setAbsolved(absolvedModules);
    }

    public boolean reorganizedModulesOnce() {
        return modulesReorganizedOnce;
    }

    public void outputStudiumFrom(int currentSemester) {
        System.out.println(this.studium.toStringFromSemester(Math.max(currentSemester, 1)));
    }

    public void shiftLeft() {
        if (!moduleToShift.equals("")) {
            studium.shiftLeft(moduleToShift);
        }
    }

    public void shiftRight() {
        if (!moduleToShift.equals("")) {
            studium.shiftRight(moduleToShift);
        }
    }

}
