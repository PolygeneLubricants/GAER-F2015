package GUI;

import Preprocessor.Parser;
import RandomMapGenerator.RandomMap;
import SupportVectorMachine.Model.SupportVector;
import SupportVectorMachine.Trainers.KernelTrainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
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

    Panel mapPanel;
    Panel controlPanel;

    public MainView() {
        _trainer = new KernelTrainer();
        _frame = new JFrame("MapGenerator");
        _frame.setLayout(new BorderLayout());
        mapPanel = new Panel();
        mapPanel.setLayout(new BorderLayout());
        controlPanel = new Panel();
        controlPanel.setLayout(new BorderLayout());

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
            _trainer.loadModel("N52E007.model");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void classify() {
        loadModel();
        Parser p = new Parser();

        SupportVector[] randomVectors = p.parse(_randomMap.getMap(), 3, 3);
        double[] predictions = _trainer.predict(_trainer.toSvmNodeMatrix(randomVectors));
        int correct = 0;
        for(int i = 0; i < predictions.length; i++) {
            if(predictions[i] == 1) {
                correct++;
            }
        }

        _predicted.setText(correct + " out of " + predictions.length + " predicted.");
    }

    private void setupControls() {
        Label title = new Label("Controls");
        Button predictButton = new Button("Predict");
        predictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classify();
            }
        });
        controlPanel.add(title, BorderLayout.NORTH);
        controlPanel.add(predictButton, BorderLayout.CENTER);
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
        setRandomMap(100, 100);
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

    public void setRandomMap(int width, int height) {
        short[][] randomMap = RandomMap.CreateRandomMap(width, height);
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
