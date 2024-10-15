import java.util.Random;

public class NeuralNetwork {
    // Constants
    private static final int NUM_INPUT = 10;
    private static final int NUM_HIDDEN = 20;
    private static final int NUM_OUTPUT = 1;
    private static final int MAX_EPOCHS = 1000;
    private static final double ERROR_THRESHOLD = 0.01;

    // Neural Network Structure
    private double[][] weightsInputHidden;
    private double[][] weightsHiddenOutput;
    private double[] hiddenLayer;
    private double[] outputLayer;
    private double learningRate;
    private Random random;

    public NeuralNetwork(double learningRate) {
        this.learningRate = learningRate;
        this.random = new Random();
        this.weightsInputHidden = new double[NUM_INPUT][NUM_HIDDEN];
        this.weightsHiddenOutput = new double[NUM_HIDDEN][NUM_OUTPUT];
        this.hiddenLayer = new double[NUM_HIDDEN];
        this.outputLayer = new double[NUM_OUTPUT];
        initializeNetwork();
    }

    // Sigmoid Function
    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    // Derivative of Sigmoid
    private double sigmoidDerivative(double x) {
        return x * (1.0 - x);
    }

    // Initialize Weights Randomly
    private void initializeNetwork() {
        for (int i = 0; i < NUM_INPUT; i++) {
            for (int j = 0; j < NUM_HIDDEN; j++) {
                weightsInputHidden[i][j] = random.nextDouble() * 2 - 1; // Random values between -1 and 1
            }
        }
        for (int j = 0; j < NUM_HIDDEN; j++) {
            for (int k = 0; k < NUM_OUTPUT; k++) {
                weightsHiddenOutput[j][k] = random.nextDouble() * 2 - 1; // Random values between -1 and 1
            }
        }
    }

    // Training Function
    public void train(double[][] input, double[][] output, int numSamples) {
        for (int epoch = 0; epoch < MAX_EPOCHS; epoch++) {
            double totalError = 0.0;
            for (int s = 0; s < numSamples; s++) {
                // Feedforward
                for (int j = 0; j < NUM_HIDDEN; j++) {
                    double activation = 0.0;
                    for (int i = 0; i < NUM_INPUT; i++) {
                        activation += input[s][i] * weightsInputHidden[i][j];
                    }
                    hiddenLayer[j] = sigmoid(activation);
                }

                for (int k = 0; k < NUM_OUTPUT; k++) {
                    double activation = 0.0;
                    for (int j = 0; j < NUM_HIDDEN; j++) {
                        activation += hiddenLayer[j] * weightsHiddenOutput[j][k];
                    }
                    outputLayer[k] = sigmoid(activation);
                }

                // Calculate Error
                double error = output[s][0] - outputLayer[0];
                totalError += error * error;

                // Backpropagation
                double outputDelta = error * sigmoidDerivative(outputLayer[0]);
                for (int j = 0; j < NUM_HIDDEN; j++) {
                    weightsHiddenOutput[j][0] += learningRate * outputDelta * hiddenLayer[j];
                }

                for (int j = 0; j < NUM_HIDDEN; j++) {
                    double hiddenDelta = outputDelta * weightsHiddenOutput[j][0] * sigmoidDerivative(hiddenLayer[j]);
                    for (int i = 0; i < NUM_INPUT; i++) {
                        weightsInputHidden[i][j] += learningRate * hiddenDelta * input[s][i];
                    }
                }
            }

            totalError /= numSamples;
            if (totalError < ERROR_THRESHOLD) {
                System.out.println("Training stopped early at epoch " + epoch);
                break;
            }
        }
    }

    // Prediction Function
    public void predict(double[] input) {
        for (int j = 0; j < NUM_HIDDEN; j++) {
            double activation = 0.0;
            for (int i = 0; i < NUM_INPUT; i++) {
                activation += input[i] * weightsInputHidden[i][j];
            }
            hiddenLayer[j] = sigmoid(activation);
        }

        for (int k = 0; k < NUM_OUTPUT; k++) {
            double activation = 0.0;
            for (int j = 0; j < NUM_HIDDEN; j++) {
                activation += hiddenLayer[j] * weightsHiddenOutput[j][k];
            }
            outputLayer[k] = sigmoid(activation);
        }

        System.out.println("Prediction: " + outputLayer[0]);
    }

    // Example Usage
    public static void main(String[] args) {
        NeuralNetwork nn = new NeuralNetwork(0.01);

        // Sample Training Data
        double[][] inputData = {
            {1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
        };
        double[][] outputData = {
            {1.0},
            {1.0}
        };

        // Training the Neural Network
        nn.train(inputData, outputData, 2);

        // Making a Prediction
        double[] newInput = {1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        nn.predict(newInput);
    }
}
