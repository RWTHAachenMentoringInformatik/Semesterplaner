package Parser;

import Studium.Studium;

/**
 * Created by philippe on 10.11.14.
 * The DataParser-Interface is the interface to implement if you want to exchange the default Parser
 * */
public interface DataParser {

    /*
    * This method gets the path to the File you want to read from
    * It has to deliver a read-in studium with all its dependencies and Modules
    * */
    Studium parseStudium(String pathToFile);

}
