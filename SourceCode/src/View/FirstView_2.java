package View;

import Main.Planner;
import Studium.Studium;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Thiemo on 18.06.2015.
 */
public class FirstView_2 extends View {
    private JComboBox cbox_semester;
    private JFormattedTextField text_cp;
    private JFormattedTextField text_workload;
    private JComboBox startinsummer;
    private JButton okButton;
    private JPanel panel;
    ;

    private ActionListener controlListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int currSem = 1;
            int maxCp = Planner.getCpMax();
            int maxH = Planner.getHMax();
            boolean start = false;

            currSem = cbox_semester.getSelectedIndex()+1;

            maxCp = Integer.parseInt(text_cp.getText());
            maxH = Integer.parseInt(text_workload.getText());
            start = (startinsummer.getSelectedIndex() != 0);

            updatePlanner(currSem, maxCp, maxH, start);
        }
    };

    public FirstView_2() throws HeadlessException {

        this.okButton.addActionListener(controlListener);


        //setUndecorated(true);
        //setTitle("Semester Planer Auswahl");
        //this.getContentPane().setBackground(Color.BLACK);
        //setLocationRelativeTo(null);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2-100, dim.height/2-this.getSize().height/2-100);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.pack();
    }

    public void updatePlanner(int currSem, int maxCp, int maxH, boolean startInSummer) {
        Planner.addInitialData(currSem, maxCp, maxH, startInSummer);
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
