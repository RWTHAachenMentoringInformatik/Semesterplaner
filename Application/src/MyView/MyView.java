package MyView;

import Controller.Controller;
import HelperClasses.SommerWinterSemester;
import HelperClasses.Studium;
import MainPackage.Planner;
import sun.security.x509.SubjectInfoAccessExtension;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.ArrayList;

/**
 * Created by philippe on 11.11.14.
 */
public class MyView extends JFrame{


    private static final String QUIT_TEXT = " QUIT! ";
    private static final String SEND_TEXT = " SEND! ";
    private static final String TYPE_TEXT = " <<Type here>> ";
    private static String TYPE_TEXT_SEM = " <<Type here>> ";
    private static String TYPE_TEXT_CP = " <<Type here>> ";
    private static String TYPE_TEXT_H = " <<Type here>> ";
    int SIZE_X = 10;
    int SIZE_Y = 8;
    int SEMESTER = 0;

    ActionListener mainListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(!(actionEvent.getSource() instanceof JButton)){
                return;
            }else if(((JButton)actionEvent.getSource()).getBackground().equals(Color.LIGHT_GRAY)){
                return;
            }
            String name = ((JButton)actionEvent.getSource()).getText();
            updateButtonsByName(name);
        }
    };

    ArrayList<ArrayList<Boolean>> passed = new ArrayList<ArrayList<Boolean>>();

    ArrayList<ArrayList<JComponent>> bgroup = new ArrayList<ArrayList<JComponent>>();

    GridLayout experimentLayout;

    private ActionListener quitOrSendListener= new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String name = ((JButton)actionEvent.getSource()).getText();
            if(name.equals(QUIT_TEXT)){
                exit();
            }else if(name.equals(SEND_TEXT)){
                ArrayList<String> notAbsolved = new ArrayList<String>();
                for(int i = 0; i<passed.size(); i++){
                    for(int j = 0; j< passed.get(i).size(); j++){
                        if(!passed.get(i).get(j)){
                            notAbsolved.add(((JButton)bgroup.get(i).get(j)).getText());
                        }
                    }
                }
                int currSem = 1;
                int maxCp = Planner.getCpMax();
                int maxH = Planner.getHMax();
                if(!((JTextField)bgroup.get(1).get(SIZE_X-3)).getText().equals(TYPE_TEXT)){
                    currSem = Integer.parseInt(((JTextField)bgroup.get(1).get(SIZE_X-3)).getText());
                }
                if(!((JTextField)bgroup.get(1).get(SIZE_X-2)).getText().equals(TYPE_TEXT)){
                    maxH = Integer.parseInt(((JTextField)bgroup.get(1).get(SIZE_X-2)).getText());
                }
                if(!((JTextField)bgroup.get(1).get(SIZE_X-1)).getText().equals(TYPE_TEXT)){
                    maxCp = Integer.parseInt(((JTextField)bgroup.get(1).get(SIZE_X-1)).getText());
                }
                updatePlanner(notAbsolved, currSem, maxCp, maxH);
            }
        }
    };


    public MyView(ArrayList<ArrayList<Boolean>> passed, ArrayList<ArrayList<String>> names){
        this(passed,names,names.size(),1);
    }

    public MyView(ArrayList<ArrayList<Boolean>> passed, ArrayList<ArrayList<String>> names, int size, int currSem) {
        if(passed==null||names==null){
            return;
        }
        int max = 0;
        for(ArrayList<String> als: names) {
            if (als.size() > max) {
                max = als.size();
            }
        }
        SIZE_X = max +5;
        SEMESTER = currSem-1;
        SIZE_Y = Math.max(6, size);
        experimentLayout = new GridLayout(SIZE_X,SIZE_Y);
        TYPE_TEXT_SEM = ""+currSem;
        setTitle("Semester Planner");
        initbGroup();
        setPassedAndGiveNames(passed, names);
        placeButtonsAndGiveSizes();
        setSize(2000, 2000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(experimentLayout);
    }

    private void updateButtonsByName(String name){
        outer : for(int i = 0; i < passed.size(); i++){
            for(int j = 0; j < passed.get(i).size(); j++){
                if(bgroup.get(i).get(j) instanceof  JButton && ((JButton)bgroup.get(i).get(j)).getText().equals(name)){
                    passed.get(i).set(j, !(passed.get(i).get(j)));
                    if(passed.get(i).get(j)){
                        bgroup.get(i).get(j).setBackground(Color.GREEN);
                    }else{
                        bgroup.get(i).get(j).setBackground(Color.ORANGE);
                    }
                    break outer;
                }
            }
        }
    }

    private void placeButtonsAndGiveSizes() {

        for(int i = 0; i < SIZE_X; i++){
            for(int j = 0; j < SIZE_Y; j++){
                this.add(bgroup.get(j).get(i));
            }
        }

    }

    private void setPassedAndGiveNames(ArrayList<ArrayList<Boolean>> input, ArrayList<ArrayList<String>> names) {
        passed = (ArrayList<ArrayList<Boolean>>) input.clone();
        for(int i = 0; i < names.size(); i++){
            for(int j = 0; j < names.get(i).size(); j++){
                ((JButton)bgroup.get(i).get(j)).setText(names.get(i).get(j));
                bgroup.get(i).get(j).setVisible(true);
                if(input.get(i).get(j).equals(false)){
                    bgroup.get(i).get(j).setBackground(Color.RED);
                    passed.get(i).set(j,false);
                }
            }
        }

    }

    private void initbGroup() {
        bgroup.clear();
        passed.clear();
        for(int i = 0; i < SIZE_Y; i++){
            bgroup.add(new ArrayList<JComponent>());
            passed.add(new ArrayList<Boolean>());
            for (int j = 0; j < SIZE_X; j++){
                passed.get(i).add(new Boolean(true));
                if(i == 0 && j == SIZE_X-3) {
                    bgroup.get(i).add(new JTextField());
                    ((JTextField) bgroup.get(i).get(j)).setText("  Your Semester: ");
                    ((JTextField) bgroup.get(i).get(j)).setEditable(false);
                    bgroup.get(i).get(j).setFont(new Font("Serif",Font.CENTER_BASELINE|Font.BOLD,10));
                    bgroup.get(i).get(j).setBackground(Color.LIGHT_GRAY);
                    bgroup.get(i).get(j).setVisible(true);
                }else if(i == 1 && j == SIZE_X-3){
                    bgroup.get(i).add(new JTextField());
                    ((JTextField)bgroup.get(i).get(j)).setText(TYPE_TEXT_SEM);
                    bgroup.get(i).get(j).setFont(new Font("Serif",Font.CENTER_BASELINE|Font.BOLD,10));
                    bgroup.get(i).get(j).setBackground(Color.LIGHT_GRAY);
                    bgroup.get(i).get(j).setVisible(true);
                }else if(i == 0 && j == SIZE_X-1) {
                    bgroup.get(i).add(new JTextField());
                    ((JTextField) bgroup.get(i).get(j)).setText("  Maximal CP's per Semester: ");
                    ((JTextField) bgroup.get(i).get(j)).setEditable(false);
                    bgroup.get(i).get(j).setFont(new Font("Serif",Font.CENTER_BASELINE|Font.BOLD,9));
                    bgroup.get(i).get(j).setBackground(Color.LIGHT_GRAY);
                    bgroup.get(i).get(j).setVisible(true);
                }else if(i == 1 && j == SIZE_X-1){
                    bgroup.get(i).add(new JTextField());
                    ((JTextField)bgroup.get(i).get(j)).setText(TYPE_TEXT);
                    bgroup.get(i).get(j).setFont(new Font("Serif",Font.CENTER_BASELINE|Font.BOLD,10));
                    bgroup.get(i).get(j).setBackground(Color.LIGHT_GRAY);
                    bgroup.get(i).get(j).setVisible(true);
                }else if(i == 0 && j == SIZE_X-2) {
                    bgroup.get(i).add(new JTextField());
                    ((JTextField) bgroup.get(i).get(j)).setText("  Maximal Workload per Semester [houres]: ");
                    ((JTextField) bgroup.get(i).get(j)).setEditable(false);
                    bgroup.get(i).get(j).setFont(new Font("Serif",Font.CENTER_BASELINE|Font.BOLD,9));
                    bgroup.get(i).get(j).setBackground(Color.LIGHT_GRAY);
                    bgroup.get(i).get(j).setVisible(true);
                }else if(i == 1 && j == SIZE_X-2){
                    bgroup.get(i).add(new JTextField());
                    ((JTextField)bgroup.get(i).get(j)).setText(TYPE_TEXT);
                    bgroup.get(i).get(j).setFont(new Font("Serif",Font.CENTER_BASELINE|Font.BOLD,10));
                    bgroup.get(i).get(j).setBackground(Color.LIGHT_GRAY);
                    bgroup.get(i).get(j).setVisible(true);
                }else if(i == SIZE_Y-2 && j == SIZE_X-1){
                    bgroup.get(i).add(new JButton());
                    ((JButton)bgroup.get(i).get(j)).setText(SEND_TEXT);
                    ((JButton) bgroup.get(i).get(j)).addActionListener(quitOrSendListener);
                    bgroup.get(i).get(j).setFont(new Font("Serif",Font.CENTER_BASELINE,10));
                    bgroup.get(i).get(j).setBackground(Color.CYAN);
                    bgroup.get(i).get(j).setVisible(true);
                }else if(i == SIZE_Y-1 && j == SIZE_X-1){
                    bgroup.get(i).add(new JButton());
                    ((JButton)bgroup.get(i).get(j)).setText(QUIT_TEXT);
                    ((JButton) bgroup.get(i).get(j)).addActionListener(quitOrSendListener);
                    bgroup.get(i).get(j).setFont(new Font("Serif",Font.CENTER_BASELINE,10));
                    bgroup.get(i).get(j).setBackground(Color.ORANGE);
                    bgroup.get(i).get(j).setVisible(true);
                }else if(i>SEMESTER-1){
                    bgroup.get(i).add(new JButton());
                    bgroup.get(i).get(j).setBackground(Color.GREEN);
                    bgroup.get(i).get(j).setVisible(false);
                    ((JButton) bgroup.get(i).get(j)).addActionListener(mainListener);
                    bgroup.get(i).get(j).setFont(new Font("Serif",Font.CENTER_BASELINE|Font.ITALIC,9));
                }else{
                    bgroup.get(i).add(new JButton());
                    bgroup.get(i).get(j).setBackground(Color.LIGHT_GRAY);
                    ((JButton) bgroup.get(i).get(j)).addActionListener(mainListener);
                    bgroup.get(i).get(j).setVisible(false);
                    bgroup.get(i).get(j).setFont(new Font("Serif",Font.CENTER_BASELINE|Font.ITALIC,9));
                }
            }
        }
    }

    public void updatePlanner(ArrayList<String> notAbsolved, int currSem, int maxCp, int maxH){
        Planner.updateWithUserInput(notAbsolved,this,currSem,maxCp,maxH);
    }

    public void exit() {
        this.dispose();
    }
}
