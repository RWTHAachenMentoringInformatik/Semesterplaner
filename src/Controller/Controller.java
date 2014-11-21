package Controller;

import HelperClasses.Modul;
import HelperClasses.Studium;
import Parser.DataFileParser;
import Parser.DataParser;

/**
 * Created by philippe on 03.11.14.
 *
 * Controller ist eine Singelton-Klasse und verkörpert die Logik des Programms
 *
 */
public class Controller {

    DataParser dfp;

    public DataParser getParser(){
        return dfp;
    }

    public void setStudium(Studium studium) {
        this.studium = studium;
    }

    static boolean TEXTINPUT = true;

    private boolean modulesReorganizedOnce = false;

    private static Controller instance;

    private Studium studium;

    public Studium getStudium() {
        return studium;
    }

    short semesterWanted = 0; // get from view

    boolean startedInWinter = false; // get from view

    boolean[] absolvedModules;

    private Controller(String[] args, String datapath/*, MyView v*/){
        this.dfp = new DataFileParser(datapath);
        this.studium = dfp.parseStudium(datapath);
        System.out.println("Setting Studium!");
    }

    public void outputStudium(){
        System.out.println(this.studium.toString());
    }

    public static Controller getController(String[] args, String datapath){
        if(instance==null){
            instance = new Controller(args, datapath);
        }
        return instance;
    }

    public void reorganizeModules(){
        reorganizeModules(1, 30, 600);
    }

    public void reorganizeModules(int sem){
        reorganizeModules(sem, 30, 600);
    }

    public void reorganizeModules(int sem, int cpMax){
        reorganizeModules(sem, cpMax, 600);
    }

    public void reorganizeModules(int sem, int cpMax, int hMax){
        studium.setcPMax(cpMax);
        studium.sethMax(hMax);
        studium.reorganize(Math.max(sem,1), this.copyStudium());
        modulesReorganizedOnce = true;
    }

    public void rearrangeWithUserInput(){
        //Todo
    }

    public Studium copyStudium(){
        String source = this.getStudium().getSource();
        Studium news = (new DataFileParser(source)).parseStudium(source);
        news.setName(this.getStudium().getName());
        news.setSource(this.getStudium().getSource());
        for (Modul m :news.getModules()) m.setAbsolved(false);
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
        System.out.println(this.studium.toStringFromSemester(Math.max(currentSemester,1)));
    }
}
