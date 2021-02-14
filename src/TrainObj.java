/**
 * The class represents a train row object.
 * Containing the row of train data, the distance from the row in the test being computed and the number of creation.
 */
public class TrainObj {
    private String[] trainRow;
    private int distance;
    private int createNum;

    /**
     * Constructor. Initializes the data.
     * @param trainRow
     * @param distance
     * @param createNum
     */
    public TrainObj(String[] trainRow, int distance, int createNum){
        this.trainRow = trainRow;
        this.distance = distance;
        this.createNum = createNum;
    }

    /**
     * Getter
     * @return the train row
     */
    public String[] getTrainRow() {
        return trainRow;
    }

    /**
     * Getter
     * @return the distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Getter
     * @return the create number
     */
    public int getCreateNum() { return createNum; }

}
