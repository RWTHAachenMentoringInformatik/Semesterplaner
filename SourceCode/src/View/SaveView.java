package View;

import Main.Planner;
import Studium.Studium;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by philippe on 09.02.15.
 */
public class SaveView extends View{

    int SIZE_X = 2;
    int SIZE_Y = 2;


    ArrayList<ArrayList<JComponent>> bgroup = null;

    GridLayout experimentLayout;
    private ActionListener controlListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Planner.saveStudiumAsFile(((JTextField) bgroup.get(0).get(0)).getText());
            exit();
        }
    };

    private ActionListener controlListener2 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Planner.setNewStudium(((JTextField) bgroup.get(0).get(1)).getText());
            exit();
        }
    };

    public SaveView() {
        initbGroup();
        placeButtonsAndGiveSizes();
        setTitle("Save File");
        this.getContentPane().setBackground(Color.BLACK);
        setSize(550, 100);
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
                    ((JTextField) tmp).setText("<< Path here >>");
                    ((JTextField) tmp).setEditable(true);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 9));
                    tmp.setBackground(Color.WHITE);
                    tmp.setVisible(true);
                } else if (i == 1 && j == 0) {
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton) tmp).setText("SAVE FILE");
                    ((JButton) tmp).addActionListener(controlListener);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 10));
                    tmp.setBackground(Color.GREEN);
                    tmp.setVisible(true);
                }else if (i == 0 && j == 1) {
                    bgroup.get(i).add(new JTextField());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextField) tmp).setText("<< Path here >>");
                    ((JTextField) tmp).setEditable(true);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 9));
                    tmp.setBackground(Color.WHITE);
                    tmp.setVisible(true);
                } else if (i == 1 && j == 1) {
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton) tmp).setText("LOAD FILE");
                    ((JButton) tmp).addActionListener(controlListener2);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 10));
                    tmp.setBackground(Color.YELLOW);
                    tmp.setVisible(true);
                }
            }
        }
    }

    @Override
    public void refresh(Studium studium) {
        return;
    }

    @Override
    public void exit() {
        this.dispose();
    }

}
