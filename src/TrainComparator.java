import java.util.Comparator;

/**
 * The class implements a comparison between two train objects.
 * The comparison is based on the distance argument.
 */
public class TrainComparator implements Comparator<TrainObj> {
    /**
     * Compares two train objects by distance, if the distance is equal then compares by insertion time.
     * @param o1
     * @param o2
     * @return the order number
     */
    @Override
    public int compare(TrainObj o1, TrainObj o2) {
        if(o1.getDistance()<o2.getDistance())
            return -1;
        else if(o1.getDistance()>o2.getDistance())
            return 1;
        else{//distance is equal, go by order of entry
            if(o1.getCreateNum()<o2.getCreateNum())
                return -1;
            else return 1;
        }
    }
}
