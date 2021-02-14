import java.util.Comparator;

/**
 * The class implements a comparison between two Object(Tree Nodes).
 * The comparison is based on the distance argument.
 */
public class TreeComparator implements Comparator<Object> {
    /**
     * Compares the Tree nodes by alphabetical order of their first data.
     * @param o1
     * @param o2
     * @return the order number
     */
    @Override
    public int compare(Object o1, Object o2) {
        TreeNode<String> treeNode1 = (TreeNode<String>) o1;
        TreeNode<String> treeNode2 = (TreeNode<String>) o2;
        String value1 = treeNode1.data1;
        String value2 = treeNode2.data1;
        return value1.compareToIgnoreCase(value2);
    }
}

