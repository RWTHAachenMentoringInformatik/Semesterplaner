package Studium;

import java.util.ArrayList;

/**
 * Created by philippe on 10.11.14.
 */
public class Semester implements Comparable {

    ArrayList<Modul> modules = new ArrayList<Modul>();
    private int cp;
    private int maxCp = 99;
    private int houresMax = 9999;
    private int houres;
    private int number = -1;

    public Semester() {
        this(-1);
    }

    public Semester(int i) {
        this.number = i;
        this.cp = 0;
    }

    public Semester(int i, int cp) {
        this.number = i;
        this.cp = cp;
    }

    public Semester(int i, int cp, int h) {
        this.number = i;
        this.cp = cp;
        this.houres = h;
    }


    public int getMaxCp() {
        return maxCp;
    }

    public void setMaxCp(int maxCp) {
        this.maxCp = maxCp;
    }

    public int getHouresMax() {
        return houresMax;
    }

    public void setHouresMax(int houresMax) {
        this.houresMax = houresMax;
    }

    public int getHoures() {
        return houres;
    }

    public void setHoures(int houres) {
        this.houres = houres;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public void add(Modul m) {
        modules.add(m);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Semester) {
            return ((Integer) this.getNumber()).compareTo(((Semester) o).getNumber());
        }
        return 0; //should never happen!
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<Modul> getModules() {
        return modules;
    }

    public void setModules(ArrayList<Modul> modules) {
        this.modules = modules;
    }

    public boolean isEmpty() {
        return modules.size() == 0;
    }
}
