/**
 * Created by philippe on 03.11.14.
 */

import HelperClasses.Modul;
import MyView.*;
import Controller.*;
import Parser.DataFileParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IllegalFormatException;

public class Planner {

    static boolean EXIT = false;

    private static String read(){
        String input = "";
        try{
            input = br.readLine();
        }catch(Exception e){
            e.printStackTrace();
        }
        return input;
    }
    static BufferedReader br = null;
    public static void main(String[] args) {
        String datapath = args != null ? args[0] : "";
        if(datapath==null){
            System.out.print("Switching to default Studium!\n");
            datapath = "file.txt";
        }
        MyView v = MyView.getView();
        Controller c = Controller.getController(args, datapath);
        //int anzahlModule = c.getStudium().getAnzahlModule();

        try{
            br = new BufferedReader(new InputStreamReader(System.in));
        }catch(Exception e){
            e.printStackTrace();
        }
        boolean EXIT = false;

        String input;
        int maxCP = -1;
        int maxH = -1;
        ArrayList<Modul> modules = c.getStudium().getModules();
        int wanted = 1;
        short round = 0;
        ArrayList<Integer> list = new ArrayList<Integer>();
        while(!EXIT){
            switch(round){
                case 0:
                    list = new ArrayList<Integer>();
                    System.out.print("Please enter your current Semester: ");
                    input = read();
                    try{
                        wanted = Integer.parseInt(input);
                        round = 1;
                        System.out.print("\n");
                    }
                    catch(Exception e){
                        round = -1;
                    }
                    break;
                case -1:
                    System.out.print("Please re-enter your current Semester (Last was not a number!): ");
                    input = read();
                    try{
                        wanted = Integer.parseInt(input);
                        round = 1;
                        System.out.print("\n");
                    }
                    catch(Exception e){
                        round = -1;
                    }
                    break;
                case 1:
                    System.out.println("Which Modules did you NOT pass? Type in the numbers shown..");
                    System.out.println("Separate all the numbers with a ',', type ';' at the end and ENTER");
                    for(Modul m : modules){
                        System.out.println(m.getName() + " --> " + m.getID());
                    }
                    input = read();
                    String cur_number = "";
                    char[] ic = input.toCharArray();
                    for(int i = 0; i<ic.length; i++){
                        if(ic[i]==','||ic[i]==' '||ic[i]=='\n'||ic[i]==';') {
                            try{
                                list.add(Integer.parseInt(cur_number));
                                cur_number = "";
                            }catch(Exception e){
                                continue;
                            }
                            continue;
                        }
                        else cur_number += ic[i];
                    }
                    round = 2;
                    break;
                case 2:
                    System.out.println("Do you have a maximal CP-Number per Semester you would like to avoid:");
                    try{maxCP=Integer.parseInt(read());}catch(Exception e){}
                    System.out.println("Do you have a maximal Number of Houres Workload per Semester you would like to avoid:");
                    try{maxH=Integer.parseInt(read());}catch(Exception e){}
                    round = 3;
                    break;
                case 3:
                    Collections.sort(list);
                    for(Integer i : list){
                        c.getStudium().setNotAbsolved(i);
                    }
                    if(maxCP!=-1&&maxH!=-1){
                        c.reorganizeModules(wanted, maxCP, maxH);
                    }else if(maxCP!=-1&&maxH==-1){
                        c.reorganizeModules(wanted, maxCP);
                    }else if(maxCP==-1&&maxH==-1){
                        c.reorganizeModules(wanted);
                    }
                    c.outputStudiumFrom(wanted);
                    round = 4;
                    break;
                case 4:
                    System.out.println("\n\n Look Above for the studium I would suggest..");
                    System.out.println("Again? [No/yes]");
                    String tmp = read();
                    if(!(tmp.equals("yes")||tmp.equals("y")||tmp.equals("Y"))){
                        EXIT = true;
                    }
                    c.setStudium(new DataFileParser(args[0]).parseStudium(args[0]));
                    round = 0;
                    break;
            }

        }

    }

}
