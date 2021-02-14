import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * java_ex2 is the main class.
 * In this class we read the train and test files and store the data in a compatible structure.
 * Then we activate each algorithm on the data to obtain classification for each row in the test file.
 * Lastly we compute accuracy for each algorithm and then print to "output.txt" file the results classifications and
 * accuracy for each algorithm.
 */
public class java_ex2 {
    /**
     * In this function we read the train and test files and store the data in a compatible structure.
     * Then we activate each algorithm on the data to obtain classification for each row in the test file.
     * Lastly we compute accuracy for each algorithm and then print to "output.txt" file the results classifications and
     * accuracy for each algorithm.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)throws Exception {
        //read from train file
        File file= new File("train.txt");
        BufferedReader reader= new BufferedReader(new FileReader(file));
        List<String[]> trainData = new ArrayList<String[]>();

        //storing the train data in an array of string lists
        for(String line=reader.readLine();line!=null;line=reader.readLine()){
            String[] currentLine = line.split("\\t");
            trainData.add(currentLine);
        }
        reader.close();

        //read from test file
        File file2= new File("test.txt");
        BufferedReader reader2= new BufferedReader(new FileReader(file2));
        List<String[]> testData = new ArrayList<String[]>();

        //storing the test data in an array of string lists
        for(String line=reader2.readLine();line!=null;line=reader2.readLine()){
            String[] currentLine = line.split("\\t");
            testData.add(currentLine);
        }
        reader2.close();

        //DT
        String[] dtAnswer = new DT().dt(trainData, testData);
        float accuracyDT = getAccuracy(testData, dtAnswer);

        //KNN
        String[] knnAnswer = new Knn().knn(trainData, testData);
        float accuracyKnn = getAccuracy(testData, knnAnswer);

        //Naive Base
        String[] naiveBaseAnswer = new NaiveBase().naiveBase(trainData, testData);
        float accuracyNaive = getAccuracy(testData, naiveBaseAnswer);

        //write to file
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        for (int count=0;count<testData.size();count++) {
            if(count==0)
                writer.write("num"+"\t"+dtAnswer[0]+"\t"+knnAnswer[0]+"\t"+naiveBaseAnswer[0]+"\r\n");
            else{
                writer.write(count+"\t"+dtAnswer[count]+"\t"+knnAnswer[count]+"\t"+naiveBaseAnswer[count]+"\r\n");
            }
        }
        writer.write("\t"+round(accuracyDT)+"\t"+round(accuracyKnn)+"\t"+round(accuracyNaive));
        writer.close();
    }

    /**
     * rounds the number to obtain 2 numbers after the dot
     * @param x
     * @return rounded x
     */
    private static double round(float x){
        double roundOff = Math.ceil(x * 100.0) / 100.0;
        return roundOff;
    }

    /**
     * Goes throw each row in test data and compares the given classification to the one we computed.
     * then we return the probability of returning a correct answer.
     * @param testData
     * @param answer
     * @return probability to get correct answer
     */
    private static float getAccuracy(List<String[]> testData, String[] answer) {
        int correctCount = 0;
        for (int i=1;i<testData.size();i++) {
            String[] testRow = testData.get(i);
            if(answer[i].equals(testRow[testRow.length-1]))
                correctCount++;
        }
        return (float)correctCount/(float)(testData.size()-1);
    }

}
