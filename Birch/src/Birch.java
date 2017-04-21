import java.util.*;

/**
 * Created by nagasaty on 4/20/17.
 */
//    B is for Branch Node
//    E is for Element Node
public class Birch  {
    private int M; 
    private int L;
    private int T;
    private Node root = null;
    public Birch(int threshold, int branchingFactor, int noOfLeaves) {
        this.M  = branchingFactor;
        this.L = noOfLeaves;
        this.T = threshold;
    }

    public Birch(int threshold, int branchingFactor) {
        this.T = threshold;
        this.M  = branchingFactor;
        this.L = branchingFactor;
    }
    
    public void insert(Vector data){
        LeafNode ltemp;
        InternalNode itemp;
        boolean isInserted;
        if(this.root == null){// create new Node.. and then insert
            itemp = new InternalNode(M, null, false); // creates a internal node
            ltemp = new LeafNode(L, itemp, true); // creates a new leaf node
            isInserted = ltemp.insert(data); // inserts data into the leaf node
            if(isInserted){
                CFNode n = new CFNode(ltemp, new CFEntry(ltemp.getN(), ltemp.getLS(), ltemp.getSS()));
                isInserted = itemp.insert(n); // inserts data of the leaf node ot hte parent node
                ltemp.setParentPtr(n);
                this.root = itemp; // mark teh root as internal node
            }else{
                // not inserted.. split required here
                System.err.println("Split required.. here"+data);
            }
        }else{
// trickle down the root and then insert .. and then split the node if required
            Node minDistNode = null;
            Node node = root;
            Stack<InternalNode> path = new Stack<>();
            while(node.equals(root) || node.getCapacity() != L){ // iterate till we reach the node.
                CFNode nodeWithMinDis = getMinDistance((InternalNode)node, data);
                path.push((InternalNode)node);
                node = nodeWithMinDis.childPtr;
            }
            minDistNode = node; // you can remove the var minDistance node
            // we have reached the node with the min distance from root to the leaf
            if(minDistNode.getCapacity() == L){
//                  root is stilll leaf node.. as in first iteration of building nodes
//                check if there is space in the first and keep adding and get out.
//TODO for now just insert the element int ot he leaf node.
                isInserted = ((LeafNode)minDistNode).insert(data);
                if(isInserted){
                    ((LeafNode)minDistNode).getParentPtr().update(((LeafNode) minDistNode).getN(), ((LeafNode) minDistNode).getLS(), ((LeafNode) minDistNode).getSS());
                }else{
                //minDistNode is full create a new InterNode and add this.root as a child and mark teh new node as root.
                    System.err.println("Split required.. here" + data);
                    CFNode newCFNode= ((LeafNode)minDistNode).split(data);
                    CFNode newCFNode1;
                    InternalNode pathNode = path.pop();
                    // update minDistNode.parentPtr cf values.. after split
                    ((LeafNode)minDistNode).getParentPtr().update(((LeafNode) minDistNode).getN(), ((LeafNode) minDistNode).getLS(), ((LeafNode) minDistNode).getSS());
                    while(path.size() > 0 && pathNode.insert(newCFNode)){
                        newCFNode1= pathNode.split(newCFNode);
                        pathNode.getParentPtr().update(pathNode.getN(), pathNode.getLS(), pathNode.getSS());
                        newCFNode = newCFNode1;
                        pathNode = path.pop();
                    }
                    if(path.size() == 0){
                        // reached root 
                        InternalNode root = new InternalNode(M, null , false);
                        root.insert(newCFNode);
                        CFNode pathNodeCFNode = new CFNode(pathNode, new CFEntry(pathNode.getN(), pathNode.getLS(), pathNode.getSS())); 
                        root.insert(pathNodeCFNode);
                        this.root = root;
                    }// else.. data is consumed some where consumed
                    else{
                        // update till teh root the CF pointers
                    }
                    
                    
                }
            }else{
                System.err.println("While loop didn't reach the leaf node. in insert ");
            }
        }
    }
    
    public String levelOrderTraversal(){
        StringBuilder sb = new StringBuilder();
        if(this.root != null){
            Queue<InternalNode> queue = new LinkedList<>();
            if(this.root.getCapacity() == M){
                queue.add((InternalNode) this.root);
                queue.add(null);
                InternalNode temp; 
                while(queue.size() > 0){
                    temp = queue.poll();
                    if(temp == null){
                        if(queue.size() > 0){
                            queue.add(null);
                            sb.append("\n");
                        }else{
                            break;
                        }
                    }else{
                        for(int i = 0 ; i < temp.size(); i++){
                            sb.append(temp.get(i));
                            sb.append("\t");
                            if(temp.get(i).getChildPtr().getCapacity() == M){
                                queue.add((InternalNode) temp.get(i).getChildPtr());
                            }
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    private CFNode getMinDistance(InternalNode inode, Vector data) {
        CFNode minSofarRef = null;
        CFEntry temp;
        Vector centroid;
        double minSofar = -1, dist = -1;
        for(int i = 0 ; i < inode.size();i++){
            temp = inode.get(i).getEntry();
            centroid = Distances.getCentroid(temp.n, temp.LS);
            dist = Distances.getD0(centroid, data);
            if(minSofar == -1 || Distances.getD0(centroid, data) < minSofar){
                minSofar = dist;
                minSofarRef = inode.get(i);
            }
        }
        if(minSofarRef == null) {
            System.err.println("getMinDistance.. not working as expected");
        }else{
            System.out.println("getMinDistance.. returned.. "+dist + data + "-->"+ minSofarRef);
        }
        return minSofarRef;
    }

    // splits the nodes dataPoints and new dataPoint into 2parts.
//    private Node<X> split(Node<X> node, X data) {
//        return  null;
//    }

}
