package SupportVectorMachine.Trainers;

import Preprocessor.Parser;
import SupportVectorMachine.Model.AltitudeBoundPair;
import SupportVectorMachine.Model.SupportVector;
import SupportVectorMachine.Model.SvmNodeMatrix;
import javafx.util.Pair;
import libsvm.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Andreas on 29/3/2015.
 */
public class KernelTrainer extends BaseTrainer {
    private svm_parameter _param;		// set by setParameters

    public svm_problem getProblem() {
        return _prob;
    }

    private svm_problem _prob;		// set by read
    private svm_model _model;
    private int _nrFold; // Fold number for cross validation

    private AltitudeBoundPair _altitudeBoundPair;

    public AltitudeBoundPair GetAltitudeBoundPair() { return _altitudeBoundPair; }

    public KernelTrainer() {
        setParameters();
    }

    public KernelTrainer(String modelFilePath, String boundsFilePath) throws IOException {
        setParameters();
        loadModel(modelFilePath, boundsFilePath);
    }

    public void loadModel(String modelFilePath, String boundsFilePath) throws IOException {
        svm_model model = svm.svm_load_model(modelFilePath);
        _model = model;

        FileInputStream fileIn = new FileInputStream(boundsFilePath);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        try {
            _altitudeBoundPair = (AltitudeBoundPair) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        in.close();
        fileIn.close();
    }

    public double[] predict(SvmNodeMatrix matrix) {
        double[] predictions = new double[matrix.get_length()];
        int index = 0;
        for(svm_node[] feature : matrix.get_matrix()) {
            predictions[index] = predict(feature);
            index++;
        }

        return predictions;
    }

    /**
     * Predicts a list of support vectors, to determine whether they fit in the model or not.
     * @param matrix map to be parsed.
     * @return a list of pairs containing the indices of the matrix which still does *not* fit.
     */
    public Pair<Integer, Integer>[] predict(short[][] matrix, Pair<Integer, Integer>[] unsolved, int width, int height) {
        ArrayList<Pair<Integer, Integer>> remainingUnsolved = new ArrayList<>();
        Parser p = new Parser();
        for(Pair<Integer, Integer> unsolvedStartIndex : unsolved) {
            SupportVector vector = p.parseSingle(matrix, unsolvedStartIndex.getKey(), unsolvedStartIndex.getValue(), width, height);
            svm_node[] nodeVector = toSvmNodeArray(vector);
            if(predict(nodeVector) != 1.0)
                remainingUnsolved.add(unsolvedStartIndex);
        }

        Pair[] arr = remainingUnsolved.toArray(new Pair[remainingUnsolved.size()]);
        return arr;
    }

    public double predict(svm_node[] vector) {
        return svm.svm_predict(_model, vector);
    }

    public void crossValidate()
    {
        int i;
        int total_correct = 0;

        double[] target = new double[_prob.l];
        double total_error = 0;
        double sumV = 0, sumY = 0, sumVV = 0, sumYY = 0, sumVY = 0;

        svm.svm_cross_validation(_prob, _param, _nrFold, target);
        if(_param.svm_type == svm_parameter.EPSILON_SVR ||
                _param.svm_type == svm_parameter.NU_SVR)
        {
            for(i=0;i< _prob.l;i++)
            {
                double y = _prob.y[i];
                double v = target[i];
                total_error += (v-y)*(v-y);
                sumV += v;
                sumY += y;
                sumVV += v*v;
                sumYY += y*y;
                sumVY += v*y;
            }
            System.out.print("Cross Validation Mean squared error = "+total_error/ _prob.l+"\n");
            System.out.print("Cross Validation Squared correlation coefficient = "+
                            ((_prob.l*sumVY-sumV*sumY)*(_prob.l*sumVY-sumV*sumY))/
                                    ((_prob.l*sumVV-sumV*sumV)*(_prob.l*sumYY-sumY*sumY))+"\n"
            );
        }
        else
        {
            for(i=0;i< _prob.l;i++)
                if(target[i] == _prob.y[i])
                    ++total_correct;
            System.out.print("Cross Validation Accuracy = "+100.0*total_correct/ _prob.l+"%\n");
        }
    }

    public void run(SupportVector[] vectors, String fileName, String boundsFileName) throws IOException
    {
        read(toSvmNodeMatrix(vectors));
        _model = svm.svm_train(_prob, _param);
        svm.svm_save_model(fileName, _model);
        try
        {
            FileOutputStream fileOut =
                    new FileOutputStream(boundsFileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(_altitudeBoundPair);
            out.close();
            fileOut.close();
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }

    public void run(SupportVector[] vectors)
    {
        read(toSvmNodeMatrix(vectors));
        _model = svm.svm_train(_prob, _param);
    }

    public void run(SvmNodeMatrix matrix) {
        read(matrix);
        _model = svm.svm_train(_prob, _param);
    }

    private void setParameters()
    {
        _param = new svm_parameter();
        // default values
        _param.svm_type = svm_parameter.ONE_CLASS;
        _param.kernel_type = svm_parameter.RBF;
        _param.degree = 3;
        _param.gamma = 0.000000001;	// 1/num_features
        _param.coef0 = 0;
        _param.nu = 0.08;
        _param.cache_size = 10;
        _param.C = 1;
        _param.eps = 0.0001;
        _param.p = 0.1;
        _param.shrinking = 1;
        _param.probability = 0;
        _param.nr_weight = 0;
        _param.weight_label = new int[0];
        _param.weight = new double[0];
        _nrFold = 1;
    }

    public SvmNodeMatrix toSvmNodeMatrix(SupportVector[] vectors) {
        int length = vectors.length; // Length of training data
        double[] classification = new double[length]; // This is redundant for our one-class SVM.
        svm_node[][] trainingSet = new svm_node[length][]; // The training set.
        AltitudeBoundPair globalBounds = new AltitudeBoundPair();
        for(int i = 0; i < length; i++)
        {
            classification[i] = 1; // Since classifications are redundant in our setup, they all belong to the same class, 1.

            trainingSet[i] = toSvmNodeArray(vectors[i]);
            AltitudeBoundPair localBounds = vectors[i].GetAltitudeBounds();
            if(localBounds.getMax() > globalBounds.getMax())
                globalBounds.setMax(localBounds.getMax());
            else if(localBounds.getMin() < globalBounds.getMin())
                globalBounds.setMin(localBounds.getMin());
        }

        return new SvmNodeMatrix(trainingSet, classification, length, globalBounds);
    }

    public svm_node[] toSvmNodeArray(SupportVector supportVector) {
        svm_node[] vector = new svm_node[supportVector.getLength()];

        double[] doubles = supportVector.toDouble(); // The SVM runs on doubles.
        for(int j = 0; j < doubles.length; j++) {
            svm_node node = new svm_node();
            node.index = j;
            node.value = doubles[j];
            vector[j] = node;
        }

        return vector;
    }

    // read in a problem (in svmlight format)
    private void read(SvmNodeMatrix nodeMatrix)
    {
        _prob = new svm_problem();
        _prob.l = nodeMatrix.get_length();
        _prob.y = nodeMatrix.get_classification();
        _prob.x = nodeMatrix.get_matrix();
        _altitudeBoundPair = nodeMatrix.getAltitudeBoundPair();
    }

    public static void main(String[] args) {
        String inputDataPath = "./data/raw/N32/N52E007.hgt";
        String outputModelName = "N52E007.model";
        String boundsModelName = "N52E007.bounds";
        int width = 100;
        int height = 100;

        if(args.length != 0) {
            inputDataPath = args[0];
            outputModelName = args[1];
            width = Integer.parseInt(args[2]);
            height = Integer.parseInt(args[3]);
        }

        Parser p = new Parser();
        short[][] altitudeMap = new short[0][];
        try {
            altitudeMap = p.read(inputDataPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        altitudeMap = p.cut(altitudeMap, 0, 0, width, height);
        SupportVector[] vectors = p.parse(altitudeMap, 3, 3);
        KernelTrainer t = new KernelTrainer();
        try {
            t.run(vectors, outputModelName, boundsModelName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}