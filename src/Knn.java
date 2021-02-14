import java.util.*;

/**
 * The class implements the knn algorithm.
 * The algorithm predicts a test data classification based on a train data set.
 */
public class Knn {
    final static int k=5;

    /**
     *  The function implements the knn algorithm for k=5 neighbors.
     * @param trainData
     * @param testData
     * @return a string of classifications, one for each test row.
     */
    public String[] knn(List<String[]> trainData, List<String[]>testData){
        String[] knnAnswer = new String [testData.size()];
        knnAnswer[0]="KNN";
        //go throw each test row and perform the algorithm
        for (int i=1;i<testData.size();i++) {
            String[] testRow = testData.get(i);
            int count = 0;
            PriorityQueue<TrainObj> pqTrain=new PriorityQueue<>(1,new TrainComparator());
            for (int j=1;j<trainData.size();j++) {
                String[] trainRow = trainData.get(j);
                int distance=0;
                //go throw row attributes, without the classification (last one)
                for (int k=0;k<trainRow.length-1;k++) {
                    // if the attributes value is different, add 1 to the hamming distance
                    if(!testRow[k].equals(trainRow[k])){
                        distance++;
                    }
                }
                TrainObj trainObj = new TrainObj(trainRow,distance,count);
                pqTrain.add(trainObj);
                count++;
            }
            Map<String, Integer> kMap = createNeighborsMap(pqTrain);
            String finalClass = getClassification(kMap);
            knnAnswer[i]=finalClass;
        }
        return knnAnswer;
    }

    /**
     * Sums up the k nearest neighbors most appeared classification.
     * @param kMap
     * @return the computed classification.
     */
    private String getClassification(Map<String, Integer> kMap) {
        String finalClass="";
        int sum=0;
        for (String classification:kMap.keySet()) {
            if(kMap.get(classification)>sum) {
                finalClass = classification;
                sum = kMap.get(classification);
            }
        }
        return finalClass;
    }

    /**
     * Takes the k nearest neighbors from the given queue and puts their classifications in a map.
     * @param pqTrain
     * @return the map created.
     */
    private Map<String, Integer> createNeighborsMap(PriorityQueue<TrainObj> pqTrain) {
        Map<String,Integer> kMap = new HashMap<>();
        for(int count=0;count<k;count++){
            if(pqTrain.isEmpty())
                break;
            else {
                TrainObj train = pqTrain.poll();
                String[] trainRow = train.getTrainRow();
                String classification = trainRow[trainRow.length-1];
                if(kMap.get(classification)== null){
                    kMap.put(classification,1);
                }
                else{
                    kMap.put(classification,kMap.get(classification)+1);
                }
            }
        }
        return kMap;
    }
}
