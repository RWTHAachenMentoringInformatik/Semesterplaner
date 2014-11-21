package HelperClasses; /**
 * Created by philippe on 03.11.14.
 */


import java.util.ArrayList;

public class Modul implements Comparable {

    public static int COUNT = 0;

    public Modul() {
        this("", (short) 0, null, null, null, null, 0, (short) 0, SommerWinterSemester.BEIDE);
    }

    public Modul(String name, short semesterNumber, ArrayList<Modul> voraussetzungen, ArrayList<Modul> empf, ArrayList<Modul> versch,
                 ArrayList<String> anmerkungen, int stunden_die_woche_h, short credits, SommerWinterSemester sommer_winter) {
        super();
        this.ID = COUNT++;
        this.name = name;
        this.semesterNumber = semesterNumber;
        if (voraussetzungen != null) this.voraussetzungen = (ArrayList<Modul>) voraussetzungen.clone();
        if (versch != null) this.verschiebungen = (ArrayList<Modul>) versch.clone();
        if (empf != null) this.empfehlungen = (ArrayList<Modul>) empf.clone();
        if (anmerkungen != null) this.anmerkungen = (ArrayList<String>) anmerkungen.clone();
        this.stunden_die_woche_h = stunden_die_woche_h;
        this.credits = credits;
        this.sommerWinter = sommer_winter;
    }

    private Modul(int ID, String name, short semesterNumber, ArrayList<Modul> voraussetzungen, ArrayList<Modul> empf, ArrayList<Modul> versch,
                  ArrayList<String> anmerkungen, int stunden_die_woche_h, short credits, SommerWinterSemester sommer_winter) {
        super();
        this.ID = ID;
        this.name = name;
        this.semesterNumber = semesterNumber;
        if (voraussetzungen != null) this.voraussetzungen = (ArrayList<Modul>) voraussetzungen.clone();
        if (versch != null) this.verschiebungen = (ArrayList<Modul>) versch.clone();
        if (empf != null) this.empfehlungen = (ArrayList<Modul>) empf.clone();
        if (anmerkungen != null) this.anmerkungen = (ArrayList<String>) anmerkungen.clone();
        this.stunden_die_woche_h = stunden_die_woche_h;
        this.credits = credits;
        this.sommerWinter = sommer_winter;
    }

    private int ID;

    private String name;

    private boolean absolved = false;

    public boolean isAbsolved() {
        return absolved;
    }

    public void setAbsolved(boolean bestanden) {
        this.absolved = bestanden;
    }

    public ArrayList<Modul> getEmpfehlungen() {
        return empfehlungen;
    }

    public void setEmpfehlungen(Studium st, ArrayList<String> empfehlungen) {
        //Todo
        for (String s : empfehlungen) {
            this.getEmpfehlungen().add(st.findModulbyName(s));
        }
    }

    public ArrayList<Modul> getVerschiebungen() {
        return verschiebungen;
    }

    public void setVerschiebungen(Studium st, ArrayList<String> verschiebungen) {
        //Todo
        for (String s : verschiebungen) {
            this.getVerschiebungen().add(st.findModulbyName(s));
        }
    }

    public ArrayList<Modul> getVoraussetzungen() {
        return voraussetzungen;
    }

    public void setVoraussetzungen(Studium st, ArrayList<String> voraussetzungen) {
        for (String s : voraussetzungen) {
            this.getVoraussetzungen().add(st.findModulbyName(s));
        }
    }

    private short semesterNumber;

    private ArrayList<Modul> voraussetzungen = new ArrayList<Modul>();

    private ArrayList<Modul> empfehlungen = new ArrayList<Modul>();

    private ArrayList<Modul> verschiebungen = new ArrayList<Modul>();

    private ArrayList<String> anmerkungen = new ArrayList<String>();

    private int stunden_die_woche_h;

    private short credits;

    private SommerWinterSemester sommerWinter;

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public short getSemesterNumber() {
        return semesterNumber;
    }

    public ArrayList<String> getAnmerkungen() {
        return anmerkungen;
    }

    public int getStunden_die_woche_h() {
        return stunden_die_woche_h;
    }

    public short getCredits() {
        return credits;
    }

    public SommerWinterSemester getSommerWinter() {
        return sommerWinter;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setSemesterNumber(short semesterNumber) {
        this.semesterNumber = semesterNumber;
    }

    public Modul copy() {
        Modul ret = new Modul(this.ID,
                this.name,
                this.semesterNumber,
                this.voraussetzungen,
                this.empfehlungen,
                this.verschiebungen,
                this.anmerkungen,
                this.stunden_die_woche_h,
                this.credits,
                this.sommerWinter);
        return ret;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Modul) {
            return this.getName().compareTo(((Modul) o).getName());
        }
        return 0;
    }

    public void notifyRecomm(String name) {
        this.anmerkungen.add("Achtung: du hast " + name + " nicht absolved, das könnte schwer werden!!");
    }

}
