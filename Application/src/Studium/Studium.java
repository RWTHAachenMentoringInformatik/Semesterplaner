package Studium;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by philippe on 10.11.14.
 */
public class Studium {

    String name = "";
    int cPMax = 35;
    int hMax = 600;
    int anzahlModule = 0;
    String source = "";
    int currentSemester = 1;
    ArrayList<Semester> semester = new ArrayList<Semester>();
    int oldwhitesize = 0;
    private String white = "";

    /*Gibt ein Studium mit geordneten Semestern und Modulen zurück
    * Geordnet heißt: ID des Moduls = SemesterZahl-1 + Alphabetischer Platz in diesem Semester!
    * Bsp: Afi-> Semester 1, Alphabet 0 -> Afi.ID = 0
    * Bsp2: Malo -> Semester 4, Alphabet 3 -> Malo.ID = 6
    */
    public Studium() {
        //Do Not Use!
    }

    public Studium(String name, ArrayList<Modul> module, String source) {
        super();
        this.name = name;
        this.source = source;
        this.anzahlModule = module.size();
        short maxSemester = 0;
        int id = 0;
        int credits = 0;
        int houres = 0;
        for (Modul m : module) {
            maxSemester = m.getSemesterNumber() > maxSemester ? m.getSemesterNumber() : maxSemester;
        }
        for (int i = 0; i < maxSemester; i++) {
            semester.add(new Semester(i + 1));
        }
        for (Modul m : module) {
            semester.get(m.getSemesterNumber() - 1).add(m.copy());
        }
        for (int i = 0; i < maxSemester; i++) {
            for (Modul m : semester.get(i).getModules()) {
                credits += m.getCredits();
                houres += m.getStunden_die_woche_h();
            }
            semester.get(i).setCp(credits);
            semester.get(i).setHoures(houres);
            credits = 0;
        }
        if (semester != null) {
            Collections.sort(semester);
            for (int i = 0; i < semester.size(); i++) {
                Collections.sort(semester.get(i).getModules());
            }
            for (int j = 0; j < semester.size(); j++) {
                for (int i = 0; i < semester.get(j).getModules().size(); i++) {
                    semester.get(j).getModules().get(i).setID(id);
                    semester.get(j).getModules().get(i).setAbsolved(true);
                    id++;
                }
            }
        }
    }

    public int getcPMax() {
        return cPMax;
    }

    public void setcPMax(int cPMax) {
        this.cPMax = cPMax;
    }

    public int gethMax() {
        return hMax;
    }

    public void sethMax(int hMax) {
        this.hMax = hMax;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }

    public ArrayList<String> getAbsolved() {
        ArrayList<String> ret = new ArrayList<String>();
        for (Semester s : this.semester) {
            for (Modul m : s.getModules()) {
                if (m.isAbsolved()) ret.add(m.getName());
            }
        }
        return ret;
    }

    public void setAbsolved(int module) {
        Modul m = findModulbyID(module);
        if (m != null) m.setAbsolved(true);
    }

    public void setAbsolved(String module) {
        Modul m = findModulbyName(module);
        if (m != null) m.setAbsolved(true);
    }

    public int getAnzahlModule() {
        return anzahlModule;
    }

    public String toString() {
        if (semester == null) return "Studium empty!";
        String out = "\n" + this.getName() + ": \n";
        for (int j = 0; j < semester.size(); j++) {
            out += "--- Semester " + semester.get(j).getNumber() + ": -----------------[" + semester.get(j).getCp() + "] \n\n";
            for (int i = 0; i < semester.get(j).getModules().size(); i++) {
                Modul t = semester.get(j).getModules().get(i);
                out += "     > " + t.getName() +
                        "(" + t.getID() +
                        ", " + t.getSommerWinter().toString() +
                        ", " + t.getCredits() +
                        ", " + t.getStunden_die_woche_h() + ")\n";
               /* out += "           ->";
                ArrayList<Modul> tmp = t.getEmpfehlungen();
                if (tmp != null) for (int z = 0; z < tmp.size(); z++) {
                    if (tmp.get(z) != null) out += tmp.get(z).getName() + ", ";
                }

                out += "\n           =>";
                tmp = t.getVerschiebungen();
                if (tmp != null) for (int z = 0; z < tmp.size(); z++) {
                    if (tmp.get(z) != null) out += tmp.get(z).getName() + ", ";
                }
                out += "\n           ~>";
                tmp = t.getVoraussetzungen();
                if (tmp != null) for (int z = 0; z < tmp.size(); z++) {
                    if (tmp.get(z) != null) out += tmp.get(z).getName() + ", ";
                }*/

                ArrayList<String> tmp2 = t.getAnmerkungen();
                if (tmp2 != null && tmp2.size() != 0) {
                    out += "\n           !!";
                    for (int z = 0; z < tmp2.size(); z++) {
                        if (z != 0) out += ", ";
                        if (tmp2.get(z) != null) out += tmp2.get(z).toString();
                    }
                }
                out += "\n\n";
            }
        }
        return out;
    }

    public String toDataString() {
        if (semester == null) return "";
        String out = "\n##" + this.getName() + "; \n";
        for (int j = 0; j < semester.size(); j++) {
            for (int i = 0; i < semester.get(j).getModules().size(); i++) {
                Modul t = semester.get(j).getModules().get(i);
                int sommer =
                        t.getSommerWinter() == SommerWinterSemester.BEIDE || t.getSommerWinter() == SommerWinterSemester.SOMMER ? 1 : 0;
                int winter =
                        t.getSommerWinter() == SommerWinterSemester.BEIDE || t.getSommerWinter() == SommerWinterSemester.WINTER ? 1 : 0;
                out += "#" + t.getName() +
                        "(" + t.getSemesterNumber() +
                        ", " + sommer +
                        ", " + winter +
                        ", " + t.getCredits() +
                        ", " + t.getStunden_die_woche_h() + ")\n";

                ArrayList<Modul> tmp = t.getEmpfehlungen();
                if (tmp != null) for (int z = 0; z < tmp.size(); z++) {
                    if (z == 0) out += "->";
                    if (tmp.get(z) != null) out += tmp.get(z).getName();
                    if (z == tmp.size() - 1) out += ";\n";
                    else out += ", ";
                }


                tmp = t.getVerschiebungen();
                if (tmp != null) for (int z = 0; z < tmp.size(); z++) {
                    if (z == 0) out += "=>";
                    if (tmp.get(z) != null) out += tmp.get(z).getName();
                    if (z == tmp.size() - 1) out += ";\n";
                    else out += ", ";
                }

                tmp = t.getVoraussetzungen();
                if (tmp != null) for (int z = 0; z < tmp.size(); z++) {
                    if (z == 0) out += "~>";
                    if (tmp.get(z) != null) out += tmp.get(z).getName();
                    if (z == tmp.size() - 1) out += ";\n";
                    else out += ", ";
                }

                out += "\n\n";
            }
        }
        return out;
    }

    /*
    * Setzt die absolvierten Module im Studium.
    * Diese werden dann speziell behandelt (zB ignoriert beim Verschieben) in den Algorithmen des Controllers
    * */
    public void setAbsolved(boolean[] absolved) {
        int count = 0;
        for (int j = 0; j < semester.size(); j++) {
            for (int i = 0; i < semester.get(j).getModules().size(); i++) {
                if (absolved[(count++)]) semester.get(j).getModules().get(i).setAbsolved(true);
            }
        }
    }

    public void setNotAbsolved(String module) {
        Modul m = findModulbyName(module);
        if (m != null) m.setAbsolved(false);
    }

    public void setNotAbsolved(int module) {
        Modul m = findModulbyID(module);
        if (m != null) m.setAbsolved(false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Semester> getSemester() {
        return semester;
    }

    private void removeEmptySemesters() {
        for (int i = semester.size() - 1; i >= 0; i--) {
            if (semester.get(i).getModules().size() == 0) {
                semester.remove(i);
            } else {
                break;
            }
        }
    }

    public Modul findModulbyName(String s) {
        for (int j = 0; j < semester.size(); j++) {
            for (int i = 0; i < semester.get(j).getModules().size(); i++) {
                if (semester.get(j).getModules().get(i).getName().equals(s)) {
                    return semester.get(j).getModules().get(i);

                }
            }
        }
        return null;
    }

    public Modul findModulbyID(int s) {
        for (int j = 0; j < semester.size(); j++) {
            for (int i = 0; i < semester.get(j).getModules().size(); i++) {
                if (semester.get(j).getModules().get(i).getID() == s) {
                    return semester.get(j).getModules().get(i);

                }
            }
        }
        return null;
    }

    private String recUpWhite() {
        oldwhitesize = oldwhitesize + 2;
        return white += "  ";
    }

    private String recDownWhite() {
        String whitenew = "";
        for (int i = 0; i < oldwhitesize - 2; i++) {
            whitenew += " ";
        }
        return whitenew;
    }

    //DO NOT TOUCH
    private void pushToNextFreeSlot(Modul modul, int currentUserSemester) {

        recUpWhite();

        short oldSemNumber = modul.getSemesterNumber();

        Semester oldSemester = this.getSemester().get(oldSemNumber - 1);

        short size = (short) this.getSemester().size();

        short nextSemesterNumber = (short) (oldSemNumber + 2);

        if (modul.getSommerWinter() == SommerWinterSemester.BEIDE) {
            nextSemesterNumber--;
        }

        if ((currentUserSemester % 2 == 1 && modul.getSommerWinter().equals(SommerWinterSemester.SOMMER))
                || (currentUserSemester % 2 == 0 && modul.getSommerWinter().equals(SommerWinterSemester.WINTER))) {
            nextSemesterNumber = (short) Math.max(nextSemesterNumber, currentUserSemester + 1);
        } else {
            nextSemesterNumber = (short) Math.max(nextSemesterNumber, currentUserSemester);
        }


        modul.setSemesterNumber(nextSemesterNumber);

        for (int i = nextSemesterNumber - size; i > 0; i--) {
            this.semester.add(new Semester(nextSemesterNumber - (i - 1)));
        }

        this.deleteByName(modul.getName());
        oldSemester.setCp(oldSemester.getCp() - modul.getCredits());
        oldSemester.setHoures(oldSemester.getHoures() - modul.getStunden_die_woche_h());

        Semester tmpSem = this.semester.get(nextSemesterNumber - 1);

        ArrayList<Modul> modules = tmpSem.getModules();

        ArrayList<Modul> allModulesTillThen = new ArrayList<Modul>();

        for (int i = 0; i < nextSemesterNumber - 1; i++) {
            allModulesTillThen.addAll(this.semester.get(i).getModules());
        }

        modules.add(modul);
        tmpSem.setCp(tmpSem.getCp() + modul.getCredits());
        tmpSem.setHoures(tmpSem.getHoures() + modul.getStunden_die_woche_h());

        Modul zuversch;

        boolean nothingPushed = true;

        System.out.print(white + modul.getName() + ": " + oldSemester.getNumber() + " to " + tmpSem.getNumber() + " {\n");

        for (int i = 0; i < modul.getVoraussetzungen().size(); i++) {
            zuversch = modul.getVoraussetzungen().get(i);
            if (zuversch == null) {
                continue;
            } else if (allModulesTillThen.contains(this.findModulbyName(zuversch.getName()))) {
                System.out.print(white + "  " + modul.getName() + " ~> " + zuversch.getName() + "\n");
                pushToNextFreeSlot(zuversch, currentUserSemester);
                nothingPushed = false;
            }
        }

        if (tmpSem.getCp() >= this.cPMax || tmpSem.getCp() >= tmpSem.getMaxCp()
                || tmpSem.getHoures() >= this.hMax || tmpSem.getHoures() >= tmpSem.getHouresMax()) {
            if (nothingPushed) for (int i = 0; i < modul.getVerschiebungen().size(); i++) {
                zuversch = modul.getVerschiebungen().get(i);
                if (zuversch == null) {
                    continue;
                } else if (nothingPushed && modules.contains(this.findModulbyName(zuversch.getName()))) {
                    System.out.print(white + "  " + modul.getName() + " => " + zuversch.getName() + "\n");
                    pushToNextFreeSlot(zuversch, currentUserSemester);
                    nothingPushed = false;
                }
            }

            if (nothingPushed) for (int i = 0; i < modules.size(); i++) {
                zuversch = modules.get(i);
                if (zuversch == null) {
                    continue;
                } else if (nothingPushed && modules.contains(zuversch)) {
                    System.out.print(white + "  " + modul.getName() + " -> " + zuversch.getName() + "\n");
                    pushToNextFreeSlot(zuversch, currentUserSemester);
                    nothingPushed = false;
                }
            }
        }

        for (int i = 0; i < modul.getEmpfehlungen().size(); i++) {
            zuversch = modul.getEmpfehlungen().get(i);
            if (zuversch == null) {
                continue;
            } else if (zuversch != null && allModulesTillThen.contains(this.findModulbyName(zuversch.getName()))) {
                System.out.print(white + "  " + modul.getName() + " n-> " + zuversch.getName() + "\n");
                zuversch.notifyRecomm(modul.getName());
            }
        }


        System.out.println(white + "}");
        white = recDownWhite();

    }

    public void reorganize(int wanted, Studium old) {
        Semester tmpS;
        Modul tmpM;

        //System.out.println("\n##REORGANIZE##\n{");
        for (int i = 0; i < old.getSemester().size(); i++) {
            tmpS = old.getSemester().get(i);
            for (int j = 0; j < tmpS.getModules().size(); j++) {
                tmpM = tmpS.getModules().get(j);
                if (!tmpM.isAbsolved()) {
                    this.pushToNextFreeSlot(this.findModulbyName(tmpM.getName()), wanted);
                    white = "";
                    oldwhitesize = 0;
                }
            }
        }
        //System.out.println("}\n##DONE##\n");

        for (int i = 0; i < getSemester().size(); i++) {
            Semester tmp = getSemester().get(i);
            for (int j = 0; j < tmp.getModules().size(); j++) {
                tmp.getModules().get(j).setAbsolved(true);
            }
        }
        /*
        for(int i = 0; i < getSemester().size(); i++){
            Semester tmp = getSemester().get(i);
            while(tmp.getCp()>cPMax){
                this.pushToNextFreeSlot(tmp.getModules().get(0),wanted);
            }
        }*/
    }

    private void deleteByName(String s) {
        for (int j = 0; j < semester.size(); j++) {
            for (int i = 0; i < semester.get(j).getModules().size(); i++) {
                if (semester.get(j).getModules().get(i).getName().equals(s)) {
                    this.semester.get(j).getModules().remove(i);
                }
            }
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ArrayList<Modul> getModules() {
        ArrayList<Modul> all = new ArrayList<Modul>();
        for (int i = 0; i < semester.size(); i++) {
            all.addAll(semester.get(i).getModules());
        }
        Collections.sort(all);
        return all;
    }

    public String toStringFromSemester(int currentSemester) {
        if (semester == null) return "Studium empty!";
        String out = "\n" + this.getName() + ": \n";
        for (int j = currentSemester - 1; j < semester.size(); j++) {
            out += "--- Semester " + semester.get(j).getNumber() + ": -----------------[" + semester.get(j).getCp() + "] \n\n";
            for (int i = 0; i < semester.get(j).getModules().size(); i++) {
                Modul t = semester.get(j).getModules().get(i);
                out += "     > " + t.getName() +
                        "(" + t.getID() +
                        ", " + t.getSommerWinter().toString() +
                        ", " + t.getCredits() +
                        ", " + t.getStunden_die_woche_h() +
                        ", " + t.isAbsolved() + ")\n";
                ArrayList<String> tmp2 = t.getAnmerkungen();
                if (tmp2 != null && tmp2.size() != 0) {
                    out += "\n           !!";
                    for (int z = 0; z < tmp2.size(); z++) {
                        if (z != 0) out += ", ";
                        if (tmp2.get(z) != null) out += tmp2.get(z).toString();
                    }
                }
                out += "\n\n";
            }
        }
        return out;
    }

    public void shiftLeft(String text) {

        Modul mod = findModulbyName(text);

        short oldSemNumber = mod.getSemesterNumber();

        Semester oldSemester = this.getSemester().get(oldSemNumber - 1);

        short nextSemesterNumber = (short) (oldSemNumber - 2);

        if (mod.getSommerWinter() == SommerWinterSemester.BEIDE) {
            nextSemesterNumber++;
        }

        if ((currentSemester % 2 == 1 && mod.getSommerWinter().equals(SommerWinterSemester.SOMMER))
                || (currentSemester % 2 == 0 && mod.getSommerWinter().equals(SommerWinterSemester.WINTER))) {
            nextSemesterNumber = (short) Math.max(nextSemesterNumber, currentSemester + 1);
        } else {
            nextSemesterNumber = (short) Math.max(nextSemesterNumber, currentSemester);
        }

        mod.setSemesterNumber(nextSemesterNumber);

        this.deleteByName(mod.getName());
        oldSemester.setCp(oldSemester.getCp() - mod.getCredits());
        oldSemester.setHoures(oldSemester.getHoures() - mod.getStunden_die_woche_h());

        Semester tmpSem = this.semester.get(nextSemesterNumber - 1);

        ArrayList<Modul> modules = tmpSem.getModules();

        modules.add(mod);
        tmpSem.setCp(tmpSem.getCp() + mod.getCredits());
        tmpSem.setHoures(tmpSem.getHoures() + mod.getStunden_die_woche_h());

        removeEmptySemesters();
    }

    public void shiftRight(String text) {

        Modul mod = findModulbyName(text);

        short oldSemNumber = mod.getSemesterNumber();

        Semester oldSemester = this.getSemester().get(oldSemNumber - 1);

        short size = (short) this.getSemester().size();

        short nextSemesterNumber = (short) (oldSemNumber + 2);

        if (mod.getSommerWinter() == SommerWinterSemester.BEIDE) {
            nextSemesterNumber--;
        }

        if ((currentSemester % 2 == 1 && mod.getSommerWinter().equals(SommerWinterSemester.SOMMER))
                || (currentSemester % 2 == 0 && mod.getSommerWinter().equals(SommerWinterSemester.WINTER))) {
            nextSemesterNumber = (short) Math.max(nextSemesterNumber, currentSemester + 1);
        } else {
            nextSemesterNumber = (short) Math.max(nextSemesterNumber, currentSemester);
        }

        mod.setSemesterNumber(nextSemesterNumber);

        for (int i = nextSemesterNumber - size; i > 0; i--) {
            this.semester.add(new Semester(nextSemesterNumber - (i - 1)));
        }

        this.deleteByName(mod.getName());
        oldSemester.setCp(oldSemester.getCp() - mod.getCredits());
        oldSemester.setHoures(oldSemester.getHoures() - mod.getStunden_die_woche_h());

        Semester tmpSem = this.semester.get(nextSemesterNumber - 1);

        ArrayList<Modul> modules = tmpSem.getModules();

        modules.add(mod);
        tmpSem.setCp(tmpSem.getCp() + mod.getCredits());
        tmpSem.setHoures(tmpSem.getHoures() + mod.getStunden_die_woche_h());
    }
}