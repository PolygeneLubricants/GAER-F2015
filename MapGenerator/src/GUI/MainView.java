package GUI;

import Preprocessor.Parser;
import RandomMapGenerator.RandomMap;
import SupportVectorMachine.Model.SupportVector;
import SupportVectorMachine.Trainers.KernelTrainer;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * User: AndreasRydingLund
 * Date: 07-04-2015
 */
public class MainView {
    private JFrame _frame;
    PixelMap _randomMap;
    PixelMap _realMap;
    KernelTrainer _trainer;
    Label _predicted;
    Pair<Integer, Integer>[] _remainingPairs;

    Panel mapPanel;
    Panel controlPanel;

    public MainView() {
        _trainer = new KernelTrainer();
        loadModel();
        _frame = new JFrame("MapGenerator");
        _frame.setLayout(new BorderLayout());
        mapPanel = new Panel();
        mapPanel.setLayout(new BorderLayout());
        controlPanel = new Panel();
        controlPanel.setLayout(new FlowLayout());

        setupRandomGroup();
        setupRealGroup();
        setupPredictions();
        setupControls();

        _frame.add(mapPanel, BorderLayout.WEST);
        _frame.add(controlPanel, BorderLayout.EAST);
        _frame.pack();
        _frame.setVisible(true);
        _frame.setResizable(true);
        _frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void loadModel() {
        try {
            _trainer.loadModel("N52E007.model", "N52E007.bounds");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void classify() {
        _predicted.setText("");

        if(_remainingPairs == null) {
            _remainingPairs = RandomMap.toIndexPairs(_randomMap.getMap());
        }

        _remainingPairs = _trainer.predict(_randomMap.getMap(), _remainingPairs, 3, 3);
        int total = _randomMap.getMap().length * _randomMap.getMap()[0].length;
        _predicted.setText(total - _remainingPairs.length + " out of " + total + " predicted.");
    }

    private void setupControls() {
        Label title = new Label("Controls");
        // PREDICT
        Button predictButton = new Button("Predict");
        predictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classify();
            }
        });

        // STEP
        Button stepButton = new Button("Step");
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // STEP
                short[][] blurredRandomMap = RandomMap.blurMap(_randomMap.getMap(), 2);
                setRandomMap(blurredRandomMap); // TODO: Change to actual step
            }
        });


        // RUN
        Button runButton = new Button("Run");
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // RUN
            }
        });

        controlPanel.add(title);
        controlPanel.add(predictButton);
        controlPanel.add(stepButton);
        controlPanel.add(runButton);
    }

    private void setupPredictions() {
        Panel predictionGroup = new Panel();
        predictionGroup.setLayout(new BorderLayout());
        Label title = new Label("Classification");
        _predicted = new Label("");
        predictionGroup.add(title, BorderLayout.NORTH);
        predictionGroup.add(_predicted, BorderLayout.SOUTH);
        _frame.add(predictionGroup, BorderLayout.SOUTH);
    }

    private void setupRandomGroup() {
        generateRandomMap(100, 100, _trainer.GetAltitudeBoundPair().getMin(), _trainer.GetAltitudeBoundPair().getMax());
        Panel randomGroup = new Panel();
        randomGroup.setLayout(new BorderLayout());
        Label name = new Label("Random map");
        randomGroup.add(name, BorderLayout.NORTH);
        randomGroup.add(_randomMap, BorderLayout.CENTER);

        mapPanel.add(randomGroup, BorderLayout.WEST);
    }

    private void setupRealGroup() {
        setRealMap("./data/raw/N32/N52E007.hgt", 0, 0, 100, 100);
        Panel realGroup = new Panel();
        realGroup.setLayout(new BorderLayout());
        Label name = new Label("Real map");
        realGroup.add(name, BorderLayout.NORTH);
        realGroup.add(_realMap, BorderLayout.CENTER);

        mapPanel.add(realGroup, BorderLayout.EAST);
    }

    public void generateRandomMap(int width, int height, short min, short max) {
        setRandomMap(RandomMap.CreateRandomMap(width, height, min, max));
    }

    public void setRandomMap(short[][] randomMap) {
        if(_randomMap == null) {
            _randomMap = createMap(randomMap);
        }
        else {
            _randomMap.fillCanvas(randomMap);
        }
    }

    public void setRealMap(String filePath, int fromX, int fromY, int width, int height) {
        Parser p = new Parser();
        short[][] realMap = null;

        try {
            realMap = p.cut(p.read(filePath), fromX, fromY, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(_realMap == null) {
            _realMap = createMap(realMap);
        }
        else {
            _realMap.fillCanvas(realMap);
        }
    }

    private PixelMap createMap(short[][] map) {
        PixelMap panel = new PixelMap(map);
        return panel;
    }
}
