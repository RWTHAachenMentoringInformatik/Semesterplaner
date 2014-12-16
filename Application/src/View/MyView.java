package View;

import Studium.Modul;
import Studium.Semester;
import Studium.Studium;
import Main.Planner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by philippe on 11.11.14.
 */
public class MyView extends View {

    public static final String RIGHT = ">>>";
    public static final String LEFT = "<<<";
    private static final String QUIT_TEXT = " RESTART! ";
    private static final String SEND_TEXT = " SEND! ";
    private static final String TYPE_TEXT = " <<Type here>> ";
    private static final String DRAG_TEXT_ON = "Dragging ON";
    private static final String DRAG_TEXT_OFF = "Dragging OFF";
    private static final String RESET_TEXT = "RESET!";
    private static final String INFOTEXT =
            "                                                           \n" +
                    "   Welcome!                                                \n" +
                    "                                                           \n" +
                    "   You wonder how to plan your studium?                    \n" +
                    "   Step 1: Click on the Modules you did NOT pass           \n" +
                    "   Step 2: Click on the <<SEND!>> Button                   \n" +
                    "   Step 3: Now you may drag around the modules with        \n" +
                    "      help of the three Buttons in the lower left corner!  \n" +
                    "                                                           \n";

    private static String TYPE_TEXT_SEM = " <<Type here>> ";

    int SIZE_X = 10;
    int SIZE_Y = 8;
    int SEMESTER = 0;

    Studium studium;

    Color buttonColor = Color.WHITE;
    Color semesterColor = Color.LIGHT_GRAY;
    Color tooMuchCredits = new Color(208, 191, 97);
    Color warning = new Color(209, 98, 65);
    Color notPassed = new Color(235, 21, 21);
    Color passed = Color.white;
    Color notEditable = new Color(187, 237, 225);
    Color chosen = new Color(187, 237, 123);


    JFrame popup = null;

    JButton currPressedButton = null;
    boolean dragging = false;
    String sourceModuleForDragging = Planner.getModulToShift();
    boolean exchanging = false;
    ArrayList<ArrayList<JComponent>> bgroup = null;
    GridLayout experimentLayout;
    MouseAdapter infoadapter = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            closePopup();
            popup = new JFrame();
            popup.setUndecorated(true);
            popup.setLayout(new GridLayout(1, 1));
            JTextArea jta = new JTextArea(INFOTEXT);
            jta.setFont(new Font("Serif", Font.CENTER_BASELINE, 13));
            jta.setEditable(false);
            popup.add(jta);
            popup.setSize((jta.getText().length() / jta.getLineCount()) * 8, jta.getLineCount() * 18);
            popup.setLocationRelativeTo(null);
            popup.setVisible(true);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            closePopup();
        }
    };
    MouseAdapter mainadapter = new MouseAdapter() {

        @Override
        public void mouseEntered(MouseEvent e) {
            closePopup();
            if ((((Component) e.getSource()).getBackground().equals(warning)
                    || ((Component) e.getSource()).getBackground().equals(tooMuchCredits))) {
                popup = new JFrame();
                popup.setUndecorated(true);
                popup.setLayout(new GridLayout(1, 1));
                JTextArea jta = new JTextArea();
                if (((Component) e.getSource()).getBackground().equals(warning)) {
                    jta.setText(requestPopupText(((JButton) e.getSource()).getText()));
                } else if (((Component) e.getSource()).getBackground().equals(tooMuchCredits)) {
                    jta.setText("Too much credits in that Semester!!");
                }
                jta.setFont(new Font("Serif", Font.CENTER_BASELINE, 13));
                jta.setEditable(false);
                popup.add(jta);
                popup.setSize((jta.getText().length() / jta.getLineCount()) * 8, jta.getLineCount() * 18);
                popup.setLocationRelativeTo(((Component) e.getSource()));
                popup.setVisible(true);
            }

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (!(e.getSource() instanceof JButton)) {
                return;
            }
            currPressedButton = (JButton) e.getSource();
            if (!dragging) {
                Planner.updateAbsolved(currPressedButton.getText());
            } else {
                if (Planner.getModulToShift().equals("")) {
                    if (isNotSpecial(currPressedButton.getText())) {
                        sourceModuleForDragging = currPressedButton.getText();
                        currPressedButton.setBackground(Color.LIGHT_GRAY);
                        exchanging = true;
                        Planner.setModulToShift(currPressedButton.getText());
                    }
                } else {
                    if (isNotSpecial(currPressedButton.getText())) {
                        findButtonByName(Planner.getModulToShift()).setBackground(Color.WHITE);
                        if (currPressedButton.getText().equals(sourceModuleForDragging)) {
                            sourceModuleForDragging = "";
                            exchanging = false;
                            Planner.setModulToShift("");
                        } else {
                            sourceModuleForDragging = currPressedButton.getText();
                            currPressedButton.setBackground(Color.LIGHT_GRAY);
                            exchanging = true;
                            Planner.setModulToShift(currPressedButton.getText());
                        }
                    } else if (currPressedButton.getText().equals(LEFT)) {
                        Planner.shiftModule(LEFT);
                        exchanging = false;
                    } else if (currPressedButton.getText().equals(RIGHT)) {
                        Planner.shiftModule(RIGHT);
                        exchanging = false;
                    }
                }
            }
        }

    };
    private ActionListener controlListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String name = ((JButton) actionEvent.getSource()).getText();
            if (name.equals(QUIT_TEXT)) {
                exit();
            } else if (name.equals(SEND_TEXT)) {
                updatePlanner();
            } else if (name.equals(DRAG_TEXT_OFF) || name.equals(DRAG_TEXT_ON)) {
                if (dragging) {
                    sourceModuleForDragging = "";
                    exchanging = false;
                    Planner.setModulToShift("");
                }
                dragging = !dragging;
                setTextDragging();
            } else if (name.equals(RESET_TEXT)) {
                Planner.resetStudium();
            }
        }
    };

    public MyView(Studium studium) {
        this.studium = studium;
        SEMESTER = studium.getCurrentSemester() - 1;
        setTitle("Semester Main.Planner");
        initbGroup();
        this.getContentPane().setBackground(Color.BLACK);
        setPassedAndGiveNames();
        placeButtonsAndGiveSizes();
        setSize(1500, 1500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private boolean isNotSpecial(String text) {
        boolean ret = true;
        if (
                text.equals(QUIT_TEXT) || text.equals(SEND_TEXT) || text.equals(TYPE_TEXT)
                        || text.equals(DRAG_TEXT_OFF) || text.equals(DRAG_TEXT_ON) || text.equals(RIGHT)
                        || text.equals(LEFT) || text.equals("")
                ) {
            ret = false;
        }
        return ret;
    }

    private JButton findButtonByName(String text) {
        outer:
        for (int i = 0; i < bgroup.size(); i++) {
            for (int j = 0; j < bgroup.get(i).size(); j++) {
                if ((bgroup.get(i).get(j) instanceof JButton) && ((JButton) bgroup.get(i).get(j)).getText().equals(text)) {
                    return (JButton) bgroup.get(i).get(j);
                }
            }
        }
        return null;
    }

    private void decideColor(Semester s) {
        if (s.getCp() > Planner.getCpMax()) {
            semesterColor = tooMuchCredits;
        } else {
            semesterColor = Color.LIGHT_GRAY;
        }
    }

    private void decideColor(Modul m) {
        if (m.getAnmerkungen().size() != 0) {
            buttonColor = warning;
        } else {
            buttonColor = passed;
        }
    }

    private void closePopup() {
        if (popup != null) popup.dispose();
        popup = null;
    }

    private void placeButtonsAndGiveSizes() {
        experimentLayout = new GridLayout(SIZE_X, SIZE_Y, 6, 3);
        this.setLayout(experimentLayout);
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                this.add(bgroup.get(j).get(i));
            }
        }
    }

    private void setPassedAndGiveNames() {
        for (int i = 0; i < studium.getSemester().size(); i++) {
            for (int j = 0; j < studium.getSemester().get(i).getModules().size(); j++) {
                decideColor(studium.getSemester().get(i).getModules().get(j));
                ((JButton) bgroup.get(i + 1).get(j + 1)).setText(studium.getSemester().get(i).getModules().get(j).getName());
                if (sourceModuleForDragging.equals(studium.getSemester().get(i).getModules().get(j).getName())) {
                    bgroup.get(i + 1).get(j + 1).setBackground(chosen);
                } else if (!studium.getSemester().get(i).getModules().get(j).isAbsolved()) {
                    bgroup.get(i + 1).get(j + 1).setBackground(notPassed);
                    bgroup.get(i + 1).get(j + 1).setForeground(Color.WHITE);
                } else {
                    bgroup.get(i + 1).get(j + 1).setBackground(buttonColor);
                    if (buttonColor.equals(warning)) {
                        bgroup.get(i + 1).get(j + 1).setForeground(Color.WHITE);
                    }
                }
                bgroup.get(i + 1).get(j + 1).setVisible(true);
            }
        }
    }

    @Override
    public void refresh(Studium studium) {
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                this.remove(bgroup.get(j).get(i));
            }
        }
        this.studium = studium;
        setTitle("Semester Main.Planner");
        initbGroup();
        setPassedAndGiveNames();
        placeButtonsAndGiveSizes();
        if (dragging) {
            setTextDragging();
            if (!sourceModuleForDragging.equals("")) {
                exchanging = true;
            }
        }
        this.revalidate();
        this.repaint();
    }

    private String requestPopupText(String module) {
        return Planner.requestPopup(module);
    }

    private void initbGroup() {
        int max = 0;
        for (int i = 0; i < studium.getSemester().size(); i++) {
            if (studium.getSemester().get(i).getModules().size() > max) {
                max = studium.getSemester().get(i).getModules().size();
            }
        }
        SIZE_X = max + 2;
        SIZE_Y = Math.max(7, studium.getSemester().size() + 1);
        bgroup = new ArrayList<ArrayList<JComponent>>();
        for (int i = 0; i < SIZE_Y; i++) {
            if (i >= 1) {
                decideColor(studium.getSemester().get(i - 1));
            }
            bgroup.add(new ArrayList<JComponent>());
            for (int j = 0; j < SIZE_X; j++) {
                if (i == 0 && j < SIZE_X - 1 && j != 0) {
                    bgroup.get(i).add(new JTextArea());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextArea) tmp).setText("Modul #" + (j));
                    ((JTextArea) tmp).setEditable(false);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 13));
                    tmp.setBackground(Color.LIGHT_GRAY);
                    tmp.setVisible(true);
                } else if (i < SIZE_Y && j == 0 && i != 0) {
                    bgroup.get(i).add(new JTextArea());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextArea) tmp).setText("Semester #" + (i) + "\n" +
                            studium.getSemester().get(i - 1).getCp() + " Cp's" + " / " +
                            studium.getSemester().get(i - 1).getHoures() + " h's");
                    ((JTextArea) tmp).setEditable(false);
                    tmp.addMouseListener(mainadapter);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 13));
                    tmp.setBackground(semesterColor);
                    tmp.setVisible(true);
                } else if (j == 0 && i == 0) {
                    bgroup.get(i).add(new JTextArea());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JTextArea) tmp).setText("Your Plan: ");
                    ((JTextArea) tmp).setEditable(false);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.BOLD, 15));
                    tmp.setBackground(Color.LIGHT_GRAY);
                    tmp.setVisible(true);
                } else if (i == 5 && j == SIZE_X - 1) {
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton) tmp).setText(RESET_TEXT);
                    ((JButton) tmp).addActionListener(controlListener);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE, 10));
                    tmp.setBackground(Color.ORANGE);
                    tmp.setVisible(true);
                } else if (i == 1 && j == SIZE_X - 1) {
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton) tmp).setText(SEND_TEXT);
                    ((JButton) tmp).addActionListener(controlListener);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE, 10));
                    tmp.setBackground(new Color(75, 195, 235));
                    tmp.setVisible(true);
                } else if (i == 0 && j == SIZE_X - 1) {
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton) tmp).setText("Info?");
                    ((JButton) tmp).setBorder(null);
                    tmp.addMouseListener(infoadapter);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.ITALIC, 10));
                    tmp.setBackground(new Color(136, 235, 75));
                    tmp.setVisible(true);
                } else if (i == 6 && j == SIZE_X - 1) {
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton) tmp).setText(QUIT_TEXT);
                    ((JButton) tmp).addActionListener(controlListener);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE, 10));
                    tmp.setBackground(notPassed);
                    tmp.setForeground(Color.WHITE);
                    tmp.setVisible(true);
                } else if (i == 2 && j == SIZE_X - 1) {
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton) tmp).setText(DRAG_TEXT_OFF);
                    ((JButton) tmp).addActionListener(controlListener);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE, 10));
                    tmp.setBackground(new Color(177, 118, 222));
                    tmp.setVisible(true);
                } else if (i == 4 && j == SIZE_X - 1) {
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton) tmp).setText(RIGHT);
                    tmp.addMouseListener(mainadapter);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE, 10));
                    tmp.setBackground(new Color(177, 118, 222));
                    tmp.setVisible(true);
                } else if (i == 3 && j == SIZE_X - 1) {
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    ((JButton) tmp).setText(LEFT);
                    tmp.addMouseListener(mainadapter);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE, 10));
                    tmp.setBackground(new Color(177, 118, 222));
                    tmp.setVisible(true);
                } else if (j == SIZE_X - 1) {
                    bgroup.get(i).add(new JTextArea());
                    JComponent tmp = bgroup.get(i).get(j);
                    tmp.setBackground(Color.LIGHT_GRAY);
                    ((JTextArea) tmp).setEditable(false);
                    tmp.setVisible(true);
                } else if (i > SEMESTER) {
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    tmp.setBackground(passed);
                    tmp.setVisible(false);
                    ((JButton) tmp).setText("");
                    tmp.addMouseListener(mainadapter);
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.ITALIC, 9));
                } else {
                    bgroup.get(i).add(new JButton());
                    JComponent tmp = bgroup.get(i).get(j);
                    tmp.setBackground(notEditable);
                    tmp.setVisible(false);
                    tmp.addMouseListener(mainadapter);
                    tmp.setEnabled(false);
                    ((JButton) tmp).setText("");
                    tmp.setFont(new Font("Serif", Font.CENTER_BASELINE | Font.ITALIC, 9));
                }
            }
        }
    }

    public void setTextDragging() {
        JButton button;
        if (findButtonByName(DRAG_TEXT_OFF) == null) {
            button = findButtonByName(DRAG_TEXT_ON);
            button.setText(DRAG_TEXT_OFF);
            button.setBackground(new Color(177, 118, 222));
        } else {
            button = findButtonByName(DRAG_TEXT_OFF);
            button.setText(DRAG_TEXT_ON);
            button.setBackground(Color.ORANGE);
        }
    }

    public void updatePlanner() {
        Planner.doReorganize();
    }

    @Override
    public void exit() {
        Planner.exchangeViews();
    }

}
