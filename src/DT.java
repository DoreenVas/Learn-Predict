import java.io.IOException;
import java.util.*;

/**
 * The class implements the Decision tree algorithm.
 * The algorithm builds a tree based on the given train data.
 * Then goes over the tree with each test row to get a classification.
 */
public class DT {
    private Map<String, Set<String>> valuesForEachAtt = new HashMap<>();

    /**
     * The function calls the dtl function that creates the tree, then printTreeToFile function that prints him.
     * Then goes over each test row and calls the findClassification function that finds the classification.
     * @param trainData
     * @param testData
     * @return The list of each test row classification.
     * @throws IOException
     */
    public String[] dt(List<String[]> trainData, List<String[]>testData) throws IOException {
        String[] dtAnswer = new String [testData.size()];
        dtAnswer[0]="DT";
        //make attributes name set
        Set<String> attNameSet = new HashSet<>();
        String[] firstRow = trainData.get(0);
        for(int i=0;i<firstRow.length-1;i++){
            attNameSet.add(firstRow[i]);
        }
        //make a map, key : attribute , value : set of values of the attribute
        for (String attribute: attNameSet) {
            Set<String> values = getAttSet(trainData, findAttLocation(trainData, attribute));
            valuesForEachAtt.put(attribute,values);
        }

        TreeNode<String> tree = dtl(trainData, attNameSet , majorityClass(trainData));
        tree.printTreeToFile("output_tree.txt");
        for (int j=1;j<testData.size();j++) {
            String[] row = testData.get(j);
            dtAnswer[j] = findClassification(trainData, tree, row);
        }
        return dtAnswer;
    }

    /**
     * Goes over the given tree and chooses a branch to go to by the test row data.
     * @param trainData
     * @param tree
     * @param row
     * @return the classification in the end leaf.
     */
    private String findClassification(List<String[]> trainData, TreeNode<String> tree, String[] row) {
        int classLocation = findAttLocation(trainData, tree.data2);
        if(tree.children.size() == 0)
            return tree.data2;
        for (TreeNode<String> child:tree.children) {
            if(child.data1.equals(row[classLocation])){
                String answer = findClassification(trainData, child, row);
                return answer;
            }
        }
        return "ERROR";
    }

    /**
     * The DTL algorithm.
     * @param trainData
     * @param attributes
     * @param defaultAtt
     * @return the constructed tree.
     */
    private TreeNode<String> dtl(List<String[]> trainData, Set<String> attributes, String defaultAtt){
        String className = trainData.get(0)[trainData.get(0).length-1];
        //No more examples, only the names line
        if(trainData.size() == 1)
            return new TreeNode<>(className,defaultAtt);
        //All examples has the same classification
        else if(getAttSet(trainData,trainData.get(0).length-1 ).size() == 1){
            String classification = trainData.get(1)[trainData.get(1).length-1];
            return new TreeNode<>(className, classification);
        }
        // Attributes is empty
        else if(attributes.isEmpty()){
            return new TreeNode<>(className, majorityClass(trainData));
        }
        else {
            String best = chooseAttribute(attributes, trainData);
            TreeNode<String> tree = new TreeNode<>(best, best);
            //Set<String> values = getAttSet(trainData, findAttLocation(trainData, best));
            for (String value : valuesForEachAtt.get(best)) {
                List<String[]> examples = examplesByValue(trainData, best, value);
                attributes.remove(best);
                Set<String> attSet = makeSetCopy(attributes);
                TreeNode<String> subTree = dtl(examples, attSet, majorityClass(trainData));
                subTree.data1 = value;
                tree.addChild(subTree);
            }
            return tree;
        }
    }

    /**
     * Makes a copy to a given set.
     * @param attributes
     * @return the copy set generated.
     */
    private Set<String> makeSetCopy(Set<String> attributes) {
        Set<String> copy = new HashSet<>();
        for (String value: attributes) {
            copy.add(value);
        }
        return copy;
    }

    /**
     *  Filters the train data to the rows in which the best attribute has the given value.
     * @param trainData
     * @param best
     * @param value
     * @return the filtered list of examples.
     */
    private List<String[]> examplesByValue(List<String[]> trainData, String best, String value) {
        List<String[]> examples = new ArrayList<>();
        examples.add(trainData.get(0));
        int location = findAttLocation(trainData, best);
        for(int i=1; i<trainData.size();i++){
            String[] row = trainData.get(i);
            if (row[location].equals(value))
                examples.add(row);
        }
        return examples;
    }

    /**
     * Goes throw the first row of attribute names in the train data and finds the location of the given attribute.
     * @param trainData
     * @param att
     * @return the position or -1 if not found.
     */
    private int findAttLocation(List<String[]> trainData, String att) {
        String[] firstRow = trainData.get(0);
        for(int i=0;i<firstRow.length;i++){
            if(firstRow[i].equals(att))
                return i;
        }
        return -1;
    }

    /**
     * Chooses the attribute with the highest gain, if equals then takes the first one to appear.
     * @param attributes
     * @param trainData
     * @return the chosen attribute.
     */
    private String chooseAttribute(Set<String> attributes, List<String[]> trainData) {
        //there is only one attribute in the set, we choose it.
        if(attributes.size() == 1)
            return (String)attributes.toArray()[0];
        Map<String,Integer> countMap = createCountMap(trainData);
        String[] firstRow = trainData.get(0);
        Set<String> classesSet = getAttSet(trainData, firstRow.length-1);
        Object[] classes = classesSet.toArray();
        int numPos = countMap.get(classes[0]);
        int numNeg = 0;
        if(classes.length == 2)
            numNeg = countMap.get(classes[1]);
        float entropy = entropy(numPos, numNeg);
        float maxGain = 0;
        String best = "";
        String[] attList = attSetToList(attributes, trainData);
        for (String attribute:attList) {
            float gain = gain(entropy, attribute, countMap, trainData, classes);
            if(gain > maxGain){
                maxGain = gain;
                best = attribute;
            }
        }
        return best;
    }

    /**
     * converts the set of attributes to list by the train data's first row order.
     * @param attributes
     * @param trainData
     * @return the list
     */
    private String[] attSetToList(Set<String> attributes, List<String[]> trainData) {
        String[] attList = new String[attributes.size()];
        int location = 0;
        for (int i=0;i<trainData.get(0).length-1;i++) {
            if(attributes.contains(trainData.get(0)[i])){
                attList[location] = trainData.get(0)[i];
                location++;
            }
        }
        return attList;
    }

    /**
     * Computes the gain of an attribute based on a formula.
     * @param entropy
     * @param attribute
     * @param map
     * @param trainData
     * @param classes
     * @return the computed gain.
     */
    private float gain(float entropy, String attribute, Map<String, Integer> map, List<String[]> trainData, Object[] classes) {
        Set<String> attSet = getAttSet(trainData, findAttLocation(trainData, attribute));
        float gain = entropy;
        float temp = 0;
        int total = trainData.size()-1;
        //go throw the given attribute optional values
        for (String att: attSet) {
            int posForAtt = 0;
            int negForAtt = 0;
            if(map.containsKey(attribute+att+classes[0])) {
                posForAtt = map.get(attribute+att+classes[0]);
            }
            if(classes.length == 2 && map.containsKey(attribute+att+classes[1]))
                negForAtt = map.get(attribute+att+classes[1]);
            temp -= (float)((float)(posForAtt+negForAtt)/total)*entropy(posForAtt, negForAtt);
        }
        return gain + temp;
    }

    /**
     * Computes the entropy based on a formula.
     * @param numPos
     * @param numNeg
     * @return the computed entropy.
     */
    private float entropy(int numPos, int numNeg) {
        int total = numNeg+numPos;
        float posProb = (float)numPos/(float)total;
        float negProb = (float)numNeg/(float)total;
        if(posProb == 0.0 || posProb == 1.0)
            return 0;
        float entropy = -posProb*log2(posProb)-negProb*log2(negProb);
        return entropy;
    }

    /**
     * Computes log with base of 2.
     * @param x
     * @return the computed log.
     */
    private float log2(float x){
        float answer = (float)(Math.log(x)/Math.log(2));
        return answer;
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

    private String majorityClass (List<String[]> trainData) {
        Map<String,Integer> map = new HashMap<>();
        //go throw train data
        for (int j=1;j<trainData.size();j++) {
            String[] trainRow = trainData.get(j);
            //save the value of the classification attribute
            String classAtt = trainRow[trainRow.length-1];
            //add count of the classification to the map
            addToMap(map, classAtt);
        }
        List<String> list = new ArrayList<>(map.keySet());
        if(list.size() == 1)
            return list.get(0);
        else if(map.get(list.get(0)) > map.get(list.get(1)))
            return list.get(0);
        else if(map.get(list.get(0)) < map.get(list.get(1)))
            return list.get(1);
        else{ //Same amount of each classification
            if (list.get(0).equals("1") || list.get(0).toLowerCase().equals("true") ||
                    list.get(0).toLowerCase().equals("yes") || list.get(0).toLowerCase().equals("t"))
                return list.get(0);
            else return list.get(1);
        }

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
