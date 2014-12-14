package MyView;

import HelperClasses.Studium;
import MainPackage.Planner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by philippe on 11.11.14.
 */
public class MyView extends JFrame{


    private static final String QUIT_TEXT = " QUIT! ";
    private static final String SEND_TEXT = " SEND! ";
    private static final String TYPE_TEXT = " <<Type here>> ";
    private static final String DRAG_TEXT_ON = "DRAGGING ON";
    private static final String DRAG_TEXT_OFF = "DRAGGING OFF";
    private static final String RESET_TEXT = "RESET!";
    public static final String RIGHT = ">>>";
    public static final String LEFT = "<<<";
    private static String TYPE_TEXT_SEM = " <<Type here>> ";
    private static String TYPE_TEXT_CP = " <<Type here>> ";
    private static String TYPE_TEXT_H = " <<Type here>> ";
    int SIZE_X = 10;
    int SIZE_Y = 8;
    int SEMESTER = 0;


    JButton currPressedButton = null;
    boolean dragging = false;
    String sourceModuleForDragging = "";
    boolean exchanging =false;

    private boolean isNotSpecial(String text){
        boolean ret = true;
        if(
                text.equals(QUIT_TEXT)||text.equals(SEND_TEXT)||text.equals(TYPE_TEXT)
                ||text.equals(DRAG_TEXT_OFF)||text.equals(DRAG_TEXT_ON)||text.equals(RIGHT)
                ||text.equals(LEFT)||text.equals("")
                ){
            ret = false;
        }
        return ret;
    }

    MouseAdapter mainadapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            int currSem = 1;
            if(!((JTextField)bgroup.get(1).get(SIZE_X-3)).getText().equals(TYPE_TEXT)){
                currSem = Integer.parseInt(((JTextField)bgroup.get(1).get(SIZE_X-3)).getText());
            }
            if(!(e.getSource() instanceof JButton)){
                return;
            }else if(((JButton)e.getSource()).getBackground().equals(Color.LIGHT_GRAY)){
                return;
            }
            currPressedButton = (JButton) e.getSource();
            if(!dragging) {
                Planner.updateAbsolved(currPressedButton.getText(), currSem);
            }else{
                if (!exchanging) {
                    if(isNotSpecial(currPressedButton.getText())) {
                        sourceModuleForDragging = currPressedButton.getText();
                        currPressedButton.setBackground(Color.LIGHT_GRAY);
                        exchanging = true;
                    }
                } else {
                    if(currPressedButton.getText().equals(LEFT)){
                        Planner.shiftModule(LEFT, sourceModuleForDragging, currSem);
                        exchanging = false;
                    }else if(currPressedButton.getText().equals(RIGHT)){
                        Planner.shiftModule(RIGHT, sourceModuleForDragging, currSem);
                        exchanging = false;
                    }else{
                        if(isNotSpecial(currPressedButton.getText())) {
                            findButtonByName(sourceModuleForDragging).setBackground(Color.WHITE);
                            sourceModuleForDragging = currPressedButton.getText();
                            currPressedButton.setBackground(Color.LIGHT_GRAY);
                            exchanging = true;
                        }
                    }
                }
            }
        }
    };

    private int findNextEmpty(int i) {
        int ret = 0;
        for(int j = 0; j< bgroup.get(i).size(); j++){
            if((bgroup.get(i).get(j) instanceof JButton)&&((JButton)bgroup.get(i).get(j)).getText().equals("")){
                ret = j;
                break;
            }
        }
        return ret;
    }

    private JButton findButtonByName(String text) {
        outer: for(int i = 0; i< bgroup.size(); i++){
            for(int j = 0; j< bgroup.get(i).size(); j++){
                if((bgroup.get(i).get(j) instanceof JButton)&&((JButton)bgroup.get(i).get(j)).getText().equals(text)){
                    return (JButton)bgroup.get(i).get(j);
                }
            }
        }
        return null;
    }

    ArrayList<ArrayList<JComponent>> bgroup = null;

    GridLayout experimentLayout;


    private ActionListener controlListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String name = ((JButton)actionEvent.getSource()).getText();
            if(name.equals(QUIT_TEXT)){
                exit();
            }else if(name.equals(SEND_TEXT)){
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
                updatePlanner(currSem, maxCp, maxH);
            }else if(name.equals(DRAG_TEXT_OFF)||name.equals(DRAG_TEXT_ON)){
                if(dragging){
                    ((JButton)actionEvent.getSource()).setText(DRAG_TEXT_OFF);
                    findButtonByName(sourceModuleForDragging).setBackground(Color.WHITE);
                    sourceModuleForDragging = "";
                }else{
                    ((JButton)actionEvent.getSource()).setText(DRAG_TEXT_ON);
                }
                dragging = !dragging;
                setNullTextVisible(dragging);
            }else if(name.equals(RESET_TEXT)){
                Planner.resetStudium();
            }
        }
    };


    public MyView(Studium studium, int currSem) {
        TYPE_TEXT_SEM = ""+currSem;
        setTitle("Semester Planner");
        initbGroup(studium, currSem);
        setPassedAndGiveNames(studium);
        placeButtonsAndGiveSizes();
        setSize(1500, 1500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void placeButtonsAndGiveSizes() {
        experimentLayout = new GridLayout(SIZE_X,SIZE_Y,5,5);
        this.setLayout(experimentLayout);
        for(int i = 0; i < SIZE_X; i++){
            for(int j = 0; j < SIZE_Y; j++){
                this.add(bgroup.get(j).get(i));
            }
        }
    }

    private void setPassedAndGiveNames(Studium studium) {
        for(int i = 0; i < studium.getSemester().size(); i++){
            for(int j = 0; j < studium.getSemester().get(i).getModules().size(); j++){
                ((JButton)bgroup.get(i+1).get(j+1)).setText(studium.getSemester().get(i).getModules().get(j).getName());
                if(!studium.getSemester().get(i).getModules().get(j).isAbsolved()){
                    bgroup.get(i+1).get(j + 1).setBackground(Color.ORANGE);
                }else{
                    bgroup.get(i+1).get(j + 1).setBackground(Color.WHITE);
                }
                bgroup.get(i+1).get(j+1).setVisible(true);
            }
        }
    }

    public void refresh(Studium studium, int currSem){
        for(int i = 0; i < SIZE_X; i++){
            for(int j = 0; j < SIZE_Y; j++){
                this.remove(bgroup.get(j).get(i));
            }
        }
        TYPE_TEXT_SEM = ""+currSem;
        SEMESTER = currSem-1;
        setTitle("Semester Planner");
        initbGroup(studium, currSem);
        setPassedAndGiveNames(studium);
        placeButtonsAndGiveSizes();
        if(dragging) {
            findButtonByName(DRAG_TEXT_OFF).setText(DRAG_TEXT_ON);
            setNullTextVisible(dragging);
        }
        this.revalidate();
        this.repaint();
    }

    private void initbGroup(Studium studium, int currSem) {
        int max = 0;
        for(int i = 0; i < studium.getSemester().size(); i++) {
            if (studium.getSemester().get(i).getModules().size() > max) {
                max = studium.getSemester().get(i).getModules().size();
            }
        }
        SIZE_X = max +6;
        SEMESTER = currSem-1;
        SIZE_Y = Math.max(7, studium.getSemester().size()+1);
        bgroup = new ArrayList<ArrayList<JComponent>>();
        for(int i = 0; i < SIZE_Y; i++){
            bgroup.add(new ArrayList<JComponent>());
            for (int j = 0; j < SIZE_X; j++){
                if(i == 0 && j == SIZE_X-3) {
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField) tmp).setText("  Your Semester: ");
                    ((JTextField) tmp).setEditable(false);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE|Font.BOLD,10));
                    tmp.setBackground(Color.LIGHT_GRAY);
                    tmp.setVisible(true);
                }else if(i == 0 && j < SIZE_X-5 && j !=0){
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField)tmp).setText("Modul #" + (j));
                    ((JTextField) tmp).setEditable(false);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 10));
                    tmp.setBackground(Color.LIGHT_GRAY);
                    tmp.setVisible(true);
                }else if(i < SIZE_Y && j ==0 && i!=0){
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField)tmp).setText("Semester #" + (i));
                    ((JTextField) tmp).setEditable(false);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE|Font.BOLD,10));
                    tmp.setBackground(Color.LIGHT_GRAY);
                    tmp.setVisible(true);
                }else if(j==0 && i ==0){
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField)tmp).setText("Your Plan: ");
                    ((JTextField) tmp).setEditable(false);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 10));
                    tmp.setBackground(Color.LIGHT_GRAY);
                    tmp.setVisible(true);
                }else if(i == 1 && j == SIZE_X-3){
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField)tmp).setText(TYPE_TEXT_SEM);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE|Font.BOLD,10));
                    tmp.setBackground(Color.LIGHT_GRAY);
                    tmp.setVisible(true);
                }else if(i == 0 && j == SIZE_X-1) {
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField) tmp).setText("  Maximal CP's per Semester: ");
                    ((JTextField) tmp).setEditable(false);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE|Font.BOLD,9));
                    tmp.setBackground(Color.LIGHT_GRAY);
                    tmp.setVisible(true);
                }else if(i == 1 && j == SIZE_X-1){
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField)tmp).setText(TYPE_TEXT);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE|Font.BOLD,10));
                    tmp.setBackground(Color.LIGHT_GRAY);
                    tmp.setVisible(true);
                }else if(i == 0 && j == SIZE_X-2) {
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField) tmp).setText("  Maximal Workload per Semester [houres]: ");
                    ((JTextField) tmp).setEditable(false);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE|Font.BOLD,9));
                    tmp.setBackground(Color.LIGHT_GRAY);
                    tmp.setVisible(true);
                }else if(i == 1 && j == SIZE_X-2){
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField)tmp).setText(TYPE_TEXT);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE|Font.BOLD,10));
                    tmp.setBackground(Color.LIGHT_GRAY);
                    tmp.setVisible(true);
                }else if(i == SIZE_Y-3 && j == SIZE_X-1){
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton)tmp).setText(RESET_TEXT);
                    ((JButton) tmp).addActionListener(controlListener);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE,10));
                    tmp.setBackground(Color.GRAY);
                    tmp.setVisible(true);
                }else if(i == SIZE_Y-2 && j == SIZE_X-1){
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton)tmp).setText(SEND_TEXT);
                    ((JButton) tmp).addActionListener(controlListener);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE,10));
                    tmp.setBackground(Color.CYAN);
                    tmp.setVisible(true);
                }else if(i == SIZE_Y-1 && j == SIZE_X-1){
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton)tmp).setText(QUIT_TEXT);
                    ((JButton) tmp).addActionListener(controlListener);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE, 10));
                    tmp.setBackground(Color.ORANGE);
                    tmp.setVisible(true);
                }else if(i == SIZE_Y-1 && j == SIZE_X-3){
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton)tmp).setText(DRAG_TEXT_OFF);
                    ((JButton) tmp).addActionListener(controlListener);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE,10));
                    tmp.setBackground(Color.CYAN);
                    tmp.setVisible(true);
                }else if(i == SIZE_Y-2 && j == SIZE_X-3){
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton)tmp).setText(RIGHT);
                    tmp.addMouseListener(mainadapter);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE, 10));
                    tmp.setBackground(Color.CYAN);
                    tmp.setVisible(true);
                }else if(i == SIZE_Y-3 && j == SIZE_X-3){
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton)tmp).setText(LEFT);
                    tmp.addMouseListener(mainadapter);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE, 10));
                    tmp.setBackground(Color.CYAN);
                    tmp.setVisible(true);
                }else if(i>SEMESTER){
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    tmp.setBackground(Color.WHITE);
                    tmp.setVisible(false);
                    ((JButton) tmp).setText("");
                    tmp.addMouseListener(mainadapter);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE|Font.ITALIC,9));
                }else{
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    tmp.setBackground(Color.LIGHT_GRAY);
                    tmp.setVisible(false);
                    tmp.setEnabled(false);
                    ((JButton) tmp).setText("");
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE|Font.ITALIC,9));
                }
            }
        }
    }

    public void setNullTextVisible(boolean b){
        for(int i = 0; i< bgroup.size(); i++){
            for(int j = 0; j< bgroup.get(i).size()-4; j++){
                if((bgroup.get(i).get(j) instanceof JButton)&&((JButton)bgroup.get(i).get(j)).getText().equals("")){
                    ((JButton)bgroup.get(i).get(j)).setVisible(b);
                }
            }
        }
    }

    public void updatePlanner(int currSem, int maxCp, int maxH){
        Planner.doReorganize(currSem, maxCp, maxH);
    }

    public void exit() {
        this.dispose();
    }
}
