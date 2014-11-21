package HelperClasses;

import java.util.LinkedList;

/**
 * Created by philippe on 03.11.14.
 */
public class TwoMuchNotPassedException extends Exception {

    LinkedList<Modul> not_passed;

    String error_message = "Bitte suche dein Mentoringteam auf, deine Nicht-Bestehens-Liste ist zu umfangreich!!";

}
