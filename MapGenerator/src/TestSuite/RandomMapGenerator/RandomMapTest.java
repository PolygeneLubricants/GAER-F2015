package TestSuite.RandomMapGenerator;

import RandomMapGenerator.RandomMap;
import SupportVectorMachine.Trainers.KernelTrainer;
import javafx.util.Pair;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Andreas on 7/4/2015.
 */
public class RandomMapTest {
    @Test
    public void testBlur() {
        // TODO: Skriv unit tests, for at verificere at blur virker korrekt.
    }

    @Test
    public void testRandomPredictIteration() {
        KernelTrainer t = new KernelTrainer();
        try {
            t.loadModel("N52E007.model", "N52E007.bounds");
        } catch (IOException e) {
            e.printStackTrace();
        }

        short[][] randMap = RandomMap.CreateRandomMap(100, 100, t.GetAltitudeBoundPair().getMin(), t.GetAltitudeBoundPair().getMax());
        Pair<Integer, Integer>[] remainingPairs = RandomMap.toIndexPairs(randMap);
        int oldTotal = remainingPairs.length;
        while(remainingPairs.length == oldTotal)
        {
            oldTotal = remainingPairs.length;
            randMap = RandomMap.CreateRandomMap(100, 100, t.GetAltitudeBoundPair().getMin(), t.GetAltitudeBoundPair().getMax());
            randMap = RandomMap.blurMap(randMap, 1);
            remainingPairs = RandomMap.toIndexPairs(randMap);
            remainingPairs = t.predict(randMap, remainingPairs, 3, 3);
        }

    }
}
