package Parser;


import HelperClasses.Modul;
import HelperClasses.SommerWinterSemester;
import HelperClasses.Studium;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by philippe on 10.11.14.
 */
public class DataFileParser implements DataParser {

     FileReader fr;
     BufferedReader br;

    private  String readFileLine(){
        String s = null;
        try{
            s = br.readLine();
        }catch(Exception e){
            System.out.print("Could not read Line!");
        }
        return s;
    }
    private  Modul parseNextModule(){
        String line;
        do{
            line = readFileLine();
            if(line == null) return null;
        }while(!isModuleName(line));
        String name = "";
        boolean wintersem = false;
        boolean sose = false;
        short cp = 0;
        int houres = 0;
        short semester = 0;
        String tmps = "";
        char[] tmp = line.toCharArray();
        /*
        this.name,
                this.semesterNumber,
                this.voraussetzungen,
                this.empfehlungen,
                this.verschiebungen,
                this.anmerkungen,
                this.stunden_die_woche_h,
                this.credits,
                this.sommerWinter
         */
        int i = 0;
        while(isSpecialSign(tmp[i])) i++;
        while(!isSpecialSignWithoutSpace(tmp[i])){
            name += tmp[i];
            i++;
        }

        while(isSpecialSign(tmp[i])) i++;
        while(!isSpecialSign(tmp[i])){
            tmps += tmp[i];
            i++;
        }
        semester = Short.parseShort(tmps);
        tmps = "";

        while(isSpecialSign(tmp[i])) i++;
        while(!isSpecialSign(tmp[i])){
            tmps += tmp[i];
            i++;
        }
        if(tmps.equals("1")) wintersem = true;
        tmps = "";

        while(isSpecialSign(tmp[i])) i++;
        while(!isSpecialSign(tmp[i])){
            tmps += tmp[i];
            i++;
        }
        if(tmps.equals("1")) sose = true;
        tmps = "";

        while(isSpecialSign(tmp[i])) i++;
        while(!isSpecialSign(tmp[i])){
            tmps += tmp[i];
            i++;
        }
        cp = Short.parseShort(tmps);
        tmps = "";

        while(isSpecialSign(tmp[i])) i++;
        while(!isSpecialSign(tmp[i])){
            tmps += tmp[i];
            i++;
        }
        houres = Integer.parseInt(tmps);
        tmps = "";

        SommerWinterSemester sw = SommerWinterSemester.WINTER;
        if(sose&&wintersem) sw = SommerWinterSemester.BEIDE;
        else if(wintersem) sw = SommerWinterSemester.SOMMER;
        else sw = SommerWinterSemester.WINTER;
        return new Modul(name, semester,null,null,null,null,houres,cp,sw);
    }
    private  ArrayList<Modul> parseModules(){
        ArrayList<Modul> ret = new ArrayList<Modul>();
        Modul mtmp = null;
        do{
            mtmp = parseNextModule();
            if(mtmp!=null)ret.add(mtmp.copy());
        }while(mtmp != null);
        return ret;
    }
    private  String parseStudiumName() {
        String name = readFileLine();
        while(name!=null&&!isName(name)){
            name = readFileLine();
        }
        String ret = "";
        if(name!=null){
            char[] namec = name.toCharArray();
            int i = 0;
            while(isSpecialSign(namec[i])) i++;
            while(!isEndSign(namec[i])){
                ret += namec[i];
                i++;
            }
        }
        return ret;
    }
    public DataFileParser(){
        this("file.txt");
    }
    public DataFileParser(String pathToFile){
        try {
            fr = new FileReader(pathToFile);
            br = new BufferedReader(fr);
        } catch (Exception e) {
            System.out.print("Failed to open studium-data-file!\n");
            e.printStackTrace();
        }
    }

    @Override
    public Studium parseStudium(String pathToFile){
            Studium s = new Studium(parseStudiumName(), parseModules(), pathToFile);
            try {
                br.close();
                fr.close();
            } catch (Exception e) {
                System.out.print("Failed to close studium-data-file!\n");

                e.printStackTrace();
            }
            createDependencies(s, pathToFile);
            return s;

    }

    //#################### ONLY HELPER DOWN HERE ####################

    private  boolean isSpecialSign(char c) {
        return c=='#'||c==','||c==' '||c=='%'||c=='-'||c=='='||c=='~'||c=='>'||c=='('||c==')';
    }
    private  boolean isSpecialSignWithoutSpace(char c) {
        return c=='#'||c==','||c=='%'||c=='-'||c=='='||c=='~'||c=='>'||c=='('||c==')';
    }
    private  boolean isEndSign(char c) {
        return c=='\n'||c==';';
    }
    private  boolean isCommentOrEmtpy(String s) {
        if(s!=null && s.length()>1){
            return s.toCharArray()[0]=='%' || s.toCharArray()[0]==' ';
        }
        return false;
    }
    private  boolean isName(String s) {
        if(s!=null && s.length()>1){
            return s.toCharArray()[0]=='#' && s.toCharArray()[1]=='#';
        }
        return false;
    }
    private  boolean isModuleName(String s) {
        if(s!=null && s.length()>1){
            return s.toCharArray()[0]=='#' && s.toCharArray()[1]!='#';
        }
        return false;
    }


    private  boolean isEmpfehlung(String s) {
        if(s!=null && s.length()>1){
            return s.toCharArray()[0]=='-' && s.toCharArray()[1]=='>';
        }
        return false;
    }
    private  boolean isVerdraengung(String s) {
        if(s!=null && s.length()>1){
            return s.toCharArray()[0]=='=' && s.toCharArray()[1]=='>';
        }
        return false;
    }
    private  boolean isVoraussetzung(String s) {
        if(s!=null && s.length()>1){
            return s.toCharArray()[0]=='~' && s.toCharArray()[1]=='>';
        }
        return false;
    }

    private  void createDependencies(Studium s, String pathToFile){
        if(s==null) return;
        createDependenciesSoft(s, pathToFile);
        createDependenciesMiddle(s, pathToFile);
        createDependenciesHard(s, pathToFile);
    }

    private  void reOpen(String s){
        try {
            fr = new FileReader(s);
            br = new BufferedReader(fr);
        }catch(Exception e){
            System.out.print("Failed to open studium-data-file! [2]\n");
            e.printStackTrace();
        }
    }
    private  void close(){
        try{
            br.close(); fr.close();
        }catch(Exception e){
            System.out.print("Failed to close studium-data-file!\n");

            e.printStackTrace();
        }
    }
    private  void closeThenReOpen(String s){
        reOpen(s);
        close();
    }

    private  void createDependenciesHard(Studium s, String p) {
         for(int i = 0; i < s.getSemester().size(); i++) {
             big: for(int j = 0 ; j < s.getSemester().get(i).getModules().size(); j++) {
                reOpen(p);
                Modul m = s.getSemester().get(i).getModules().get(j);
                String name = m.getName();
                int count = 0;
                String name_tmp = "";
                String line = "";
                do {
                    count = 0;
                    name_tmp = "";
                    do {
                        line = readFileLine();
                        if (line == null) return;
                    } while (!isModuleName(line));
                    char[] tmp = line.toCharArray();
                    while (isSpecialSign(tmp[count])) {
                        count++;
                    }
                    while (!isSpecialSignWithoutSpace(tmp[count])) {
                        name_tmp += tmp[count];
                        count++;
                    }
                }while(!name.equals(name_tmp));

                count = 0;
                ArrayList<String> modules = new ArrayList<String>();
                do {
                    line = readFileLine();
                    if (line == null) return;
                    if (isModuleName(line)) continue big;
                } while (!isVoraussetzung(line));
                char[] tmp = line.toCharArray();
                name_tmp = "";
                do {
                    while (isSpecialSign(tmp[count])) count++;
                    while (!isSpecialSignWithoutSpace(tmp[count])&&!isEndSign(tmp[count])) {
                        name_tmp += tmp[count];
                        count++;
                    }
                    modules.add(name_tmp);
                    name_tmp = "";
                }while(!isEndSign(tmp[count]));
                m.setVoraussetzungen(s, modules);
                close();
            }
        }
    }

    private  void createDependenciesMiddle(Studium s, String p) {
        for(int i = 0; i < s.getSemester().size(); i++) {
            big: for(int j = 0 ; j < s.getSemester().get(i).getModules().size(); j++) {
                reOpen(p);
                Modul m = s.getSemester().get(i).getModules().get(j);
                String name = m.getName();
                int count = 0;
                String name_tmp = "";
                String line;
                do {
                    count = 0;
                    name_tmp = "";
                    do {
                        line = readFileLine();
                        if (line == null) return;
                    } while (!isModuleName(line));
                    char[] tmp = line.toCharArray();
                    while (isSpecialSign(tmp[count])) {
                        count++;
                    }
                    while (!isSpecialSignWithoutSpace(tmp[count])) {
                        name_tmp += tmp[count];
                        count++;
                    }
                }while(!name.equals(name_tmp));
                name_tmp = "";
                count = 0;
                ArrayList<String> modules = new ArrayList<String>();
                do {
                    line = readFileLine();
                    if (line == null) return;
                    if (isModuleName(line)) continue big;
                } while (!isVerdraengung(line));
                char[] tmp = line.toCharArray();
                do {
                    while (isSpecialSign(tmp[count])) count++;
                    while (!isSpecialSignWithoutSpace(tmp[count])&&!isEndSign(tmp[count])) {
                        name_tmp += tmp[count];
                        count++;
                    }
                    modules.add(name_tmp);
                    name_tmp = "";
                }while(!isEndSign(tmp[count]));
                m.setVerschiebungen(s, modules);

                close();
            }
        }
    }
    private  void createDependenciesSoft(Studium s, String p) {
        for(int i = 0; i < s.getSemester().size(); i++) {
            big:
            for (int j = 0; j < s.getSemester().get(i).getModules().size(); j++) {
                reOpen(p);
                Modul m = s.getSemester().get(i).getModules().get(j);
                String name = m.getName();
                int count = 0;
                String name_tmp = "";
                String line = "";
                do {
                    count = 0;
                    name_tmp = "";
                    do {
                        line = readFileLine();
                        if (line == null) return;
                    } while (!isModuleName(line));
                    char[] tmp = line.toCharArray();
                    while (isSpecialSign(tmp[count])) {
                        count++;
                    }
                    while (!isSpecialSignWithoutSpace(tmp[count])) {
                        name_tmp += tmp[count];
                        count++;
                    }
                } while (!name.equals(name_tmp));

                count = 0;
                ArrayList<String> modules = new ArrayList<String>();
                do {
                    line = readFileLine();
                    if (line == null) return;
                    if (isModuleName(line)) continue big;
                } while (!isEmpfehlung(line));
                char[] tmp = line.toCharArray();
                name_tmp = "";
                do {
                    while (isSpecialSign(tmp[count])) count++;
                    while (!isSpecialSignWithoutSpace(tmp[count]) && !isEndSign(tmp[count])) {
                        name_tmp += tmp[count];
                        count++;
                    }
                    modules.add(name_tmp);
                    name_tmp = "";
                } while (!isEndSign(tmp[count]));
                m.setEmpfehlungen(s, modules);
                close();
            }
        }
    }

}
