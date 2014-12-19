package View;

import Main.Planner;
import Studium.Studium;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by philippe on 11.11.14.
 */
public class FirstView extends View {


    private static String TYPE_TEXT_SEM = "<<Type here>>";
    private static String TYPE_TEXT_CP = "<<Type here>>";
    private static String TYPE_TEXT_H = "<<Type here>>";

    int SIZE_X = 4;
    int SIZE_Y = 2;


    ArrayList<ArrayList<JComponent>> bgroup = null;

    GridLayout experimentLayout;
    private ActionListener controlListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int currSem = 1;
            int maxCp = Planner.getCpMax();
            int maxH = Planner.getHMax();
            if (!((JTextField) bgroup.get(1).get(0)).getText().equals(TYPE_TEXT_SEM)
                    && !((JTextField) bgroup.get(1).get(0)).getText().contains(" ")
                    && !((JTextField) bgroup.get(1).get(0)).getText().equals("")) {
                currSem = Integer.parseInt(((JTextField) bgroup.get(1).get(0)).getText());
            }
            if (!((JTextField) bgroup.get(1).get(1)).getText().equals(TYPE_TEXT_CP)
                    && !((JTextField) bgroup.get(1).get(1)).getText().contains(" ")
                    && !((JTextField) bgroup.get(1).get(1)).getText().equals("")) {
                maxCp = Integer.parseInt(((JTextField) bgroup.get(1).get(1)).getText());
            }
            if (!((JTextField) bgroup.get(1).get(2)).getText().equals(TYPE_TEXT_H)
                    && !((JTextField) bgroup.get(1).get(2)).getText().contains(" ")
                    && !((JTextField) bgroup.get(1).get(2)).getText().equals("")) {

                maxH = Integer.parseInt(((JTextField) bgroup.get(1).get(2)).getText());
            }
            updatePlanner(currSem, maxCp, maxH);
        }
    };

    public FirstView() {
        initbGroup();
        placeButtonsAndGiveSizes();
        setTitle("Semester Main.Planner");
        this.getContentPane().setBackground(Color.BLACK);
        setSize(350, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void placeButtonsAndGiveSizes() {
        experimentLayout = new GridLayout(SIZE_X, SIZE_Y, 5, 5);
        this.setLayout(experimentLayout);
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                this.add(bgroup.get(j).get(i));
            }
        }
    }

    private void initbGroup() {
        bgroup = new ArrayList<ArrayList<JComponent>>();
        for (int i = 0; i < SIZE_Y; i++) {
            bgroup.add(new ArrayList<JComponent>());
            for (int j = 0; j < SIZE_X; j++) {

                if (i == 0 && j == 0) {
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField) tmp).setText("Current Semester: ");
                    ((JTextField) tmp).setEditable(false);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 9));
                    tmp.setBackground(Color.WHITE);
                    tmp.setVisible(true);
                } else if (i == 1 && j == 0) {
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField) tmp).setText(TYPE_TEXT_SEM);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 10));
                    tmp.setBackground(Color.WHITE);
                    tmp.setVisible(true);
                } else if (i == 0 && j == 1) {
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField) tmp).setText("Maximal CP's per Semester: ");
                    ((JTextField) tmp).setEditable(false);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 9));
                    tmp.setBackground(Color.WHITE);
                    tmp.setVisible(true);
                } else if (i == 1 && j == 1) {
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField) tmp).setText(TYPE_TEXT_CP);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 10));
                    tmp.setBackground(Color.WHITE);
                    tmp.setVisible(true);
                } else if (i == 0 && j == 2) {
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField) tmp).setText("Workload per Semester [h's]: ");
                    ((JTextField) tmp).setEditable(false);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 9));
                    tmp.setBackground(Color.WHITE);
                    tmp.setVisible(true);
                } else if (i == 1 && j == 2) {
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField) tmp).setText(TYPE_TEXT_H);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 10));
                    tmp.setBackground(Color.WHITE);
                    tmp.setVisible(true);
                } else if (i == 0 && j == 3) {
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton) tmp).setText("Ok!");
                    ((JButton) tmp).addActionListener(controlListener);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 10));
                    tmp.setBackground(new Color(75, 195, 235));
                    tmp.setVisible(true);
                } else {
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton) tmp).setText("QUIT");
                    ((JButton) tmp).addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            exit();
                        }
                    });
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE, 10));
                    tmp.setBackground(new Color(235, 21, 21));
                    tmp.setForeground(Color.WHITE);
                    tmp.setVisible(true);
                }
            }
        }
    }

    public void updatePlanner(int currSem, int maxCp, int maxH) {
        Planner.addInitialData(currSem, maxCp, maxH);
        this.dispose();
    }

    @Override
    public void refresh(Studium studium) {
        return;
    }

    @Override
    public void exit() {
        Planner.stop();
    }

}
