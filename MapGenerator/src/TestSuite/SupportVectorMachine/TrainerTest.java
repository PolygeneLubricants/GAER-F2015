package TestSuite.SupportVectorMachine;

import Preprocessor.Parser;
import SupportVectorMachine.Trainer;

import java.io.IOException;

/**
 * Created by Andreas on 24/3/2015.
 */
public class TrainerTest {
    public void testRun() {
        Parser p = new Parser();
        short[][] altitudeMap = new short[0][];
        try {
            altitudeMap = p.read("./data/raw/N32/N52E007.hgt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Trainer t = new Trainer();
        try {
            t.run(p.parse(altitudeMap, 3, 3), "testRun.model");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testCrossValidate() {

    }
}
