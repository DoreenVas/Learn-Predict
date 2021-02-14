import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tree Node class.
 * Each node represents a tree with data, parent and list of children.
 * @param <T>
 */
public class TreeNode<T>  {

    T data1;
    T data2;
    TreeNode<T> parent;
    List<TreeNode<T>> children;

    /**
     * Constructor. Initializes the data.
     * @param data1
     * @param data2
     */
    public TreeNode(T data1, T data2) {
        this.data1 = data1;
        this.data2 = data2;
        this.children = new ArrayList<>();
    }

    /**
     * Adds the given child to the trees list of children.
     * @param child
     * @return the node itself
     */
    public TreeNode<T> addChild(TreeNode<T> child) {
        TreeNode<T> childNode = child;
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    /**
     * print the tree to the given file name using the printToTree recursive print.
     * @param fileName
     * @throws IOException
     */
    public void printTreeToFile(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        this.children.sort(new TreeComparator());
        for (TreeNode<T> child:this.children) {
            writer.write(this.data2+"="+child.data1);
            printToTree(writer, child,0);
        }
        writer.close();
    }

    /**
     * Sorts the children by alphabetical order and writes to the given writer in the correct order.
     * @param writer
     * @param tree
     * @param iteration
     * @throws IOException
     */
    private void printToTree(BufferedWriter writer, TreeNode<T> tree, int iteration) throws IOException {
        if(tree.children.size() == 0){
            writer.write(":"+tree.data2+"\r\n");
        }
        else {
            if(iteration == 0)
                writer.write( "\r\n");
            iteration++;
            tree.children.sort(new TreeComparator());
            for (TreeNode<T> child : tree.children) {
                for(int i=0;i<iteration;i++)
                    writer.write("\t");
                writer.write( "|" + tree.data2 + "=" + child.data1);
                if(child.children.size() != 0) {
                    writer.write("\r\n");
                }
                printToTree(writer, child, iteration);
            }
        }
    }
}