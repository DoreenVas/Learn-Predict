import java.util.*;

/**
 * The class implements the Naive Based algorithm.
 * The algorithm predicts a test data classification based on a train data set.
 */
public class NaiveBase {
    /**
     * Implements the naive based algorithm.
     * The algorithm is based on probabilities we compute.
     * @param trainData
     * @param testData
     * @return return a string of classifications, one for each test row.
     */
    public String[] naiveBase(List<String[]> trainData, List<String[]>testData){
        String[] naiveBaseAnswer = new String [testData.size()];
        naiveBaseAnswer[0]="naiveBase";
        Map<String, Integer> countMap = createCountMap(trainData);
        String[] firstLine = trainData.get(0);
        Set<String> classes = getAttSet(trainData, firstLine.length-1);
        int[] numOfAtt = new int[trainData.get(0).length];
        for (int pos=0;pos<trainData.get(0).length;pos++) {
            numOfAtt[pos] = getAttSet(trainData, pos).size();
        }

        //go throw each test row and perform the algorithm
        for (int i=1;i<testData.size();i++) {
            String[] testRow = testData.get(i);
            float maxProbability=0;
            String finalClass="";
            for (String classification:classes){
                float probability = 1;
                //go throw attributes without classification
                for(int j=0;j<testRow.length-1;j++){
                    String attName = firstLine[j];
                    String attValue = testRow[j];
                    String attDependClass = attName+attValue+classification;
                    int countAttDependClass = 0;
                    if(countMap.get(attDependClass) != null)
                        countAttDependClass = countMap.get(attDependClass);
                    probability *= ((float)countAttDependClass+1)/((float)countMap.get(classification)+numOfAtt[j]);
                }
                probability *= (float)countMap.get(classification)/(float)(trainData.size()-1);
                if(probability > maxProbability){
                    maxProbability = probability;
                    finalClass = classification;
                }
            }
            if(maxProbability == 0.5)
                finalClass = defaultClass(countMap, classes);
            naiveBaseAnswer[i] = finalClass;
        }
        return naiveBaseAnswer;
    }

    /**
     * Return computes the majority class
     * @param countMap
     * @param classes
     * @return the class with the most appearances
     */
    private String defaultClass(Map<String, Integer> countMap, Set<String> classes) {
        String finalClass="";
        int max = 0;
        for (String cls:classes) {
            if(countMap.get(cls)>max) {
                max = countMap.get(cls);
                finalClass = cls;
            }
        }
        return finalClass;
    }

    /**
     * Gets an attribute position and data and returns all the possible attributes in the correct position in the data.
     * @param trainData
     * @param position
     * @return set of attributes
     */
    private Set<String> getAttSet(List<String[]> trainData, int position) {
        Set<String> attSet = new HashSet<>();
        for(int i=1;i<trainData.size();i++){
            String[] trainRow = trainData.get(i);
            String value = trainRow[position];
            attSet.add(value);
        }
        return attSet;
    }

    /**
     * Counts appearances of each attribute with each class and also classes in the given data.
     * Then stores in a map by key:name, value:count.
     * @param trainData
     * @return the computed map
     */
    private Map<String, Integer> createCountMap (List<String[]> trainData) {
        Map<String,Integer> map = new HashMap<>();
        //go throw train data
        String[] firstLine =trainData.get(0);
        for (int j=1;j<trainData.size();j++) {
            String[] trainRow = trainData.get(j);
            //save the value of the classification attribute
            String classAtt = trainRow[trainRow.length-1];
            //add count of the classification to the map
            addToMap(map, classAtt);
            //go throw all attributes except the classification
            for (int k=0;k<trainRow.length-1;k++) {
                String attName = firstLine[k];
                String attValue = trainRow[k];
                String attDependClass = attName+attValue+classAtt;
                addToMap(map,attDependClass);
            }
        }
        return map;
    }

    /**
     * If the string given exists in the map we increment its value by one, otherwise we add it to the map with value 1.
     * @param map
     * @param str
     */
    private void addToMap(Map<String, Integer> map, String str) {
        if(map.get(str)==null){
            map.put(str,1);
        }
        else{
            map.put(str,map.get(str)+1);
        }
    }


}
