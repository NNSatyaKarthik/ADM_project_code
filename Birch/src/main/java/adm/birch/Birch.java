package adm.birch;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by nagasaty on 4/20/17.
 */
//    B is for Branch Node
//    E is for Element Node
public class Birch  {
    final static Logger logger = Logger.getLogger(Birch.class); 
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
                ltemp.setParentPtr(n); // parent ptr of leaf node is set to cfnode in the internal ndoe
                this.root = itemp; // mark teh root as internal node
            }else{
                // not inserted.. split required here
                logger.error("root is null and split is required.. which desnt' make sense.. recheck the param MLT"+data);
            }
        }else{
// trickle down the root and then insert .. and then split the node if required
            Node minDistNode = null;
            Node node = root;
            Stack<InternalNode> path = new Stack<>();
            while(node.equals(root) || node.getCapacity() != L){ // iterate till we reach the node.
                CFNode nodeWithMinDis = getMinDistance((InternalNode)node, data);
                ((InternalNode)node).addVectorInfo(data); // updates nodes information
                nodeWithMinDis.addVectorInfoToEntry(data); // updates CFdata information
                path.push((InternalNode)node);// pushes node onto stack
                node = nodeWithMinDis.childPtr; // moves to the cfNode's child Poitner which is a new INternal Node
            }
            minDistNode = node; // you can remove the var minDistance node
            // we have reached the node with the min distance from root to the leaf
            if(minDistNode.getCapacity() == L){
//                  root is stilll leaf node.. as in first iteration of building nodes
//                check if there is space in the first and keep adding and get out.
//TODO for now just insert the element int ot he leaf node.
                isInserted = ((LeafNode)minDistNode).insert(data);
                if(isInserted){
//                    updateMetaInformationofThisNode((LeafNode) minDistNode);
//                    InternalNode pathNode = path.peek();
//                    pathNode.metaSync(minDistNode.getDelta(), path);
                }else{
                //minDistNode is full create a new InterNode and add this.root as a child and mark teh new node as root.
                    logger.info("Split required.. here" + data);
                    CFNode newCFNode= ((LeafNode)minDistNode).split(data);
                    CFEntry delta = ((LeafNode)minDistNode).getDelta();
                    
                    CFNode newCFNode1;
                    InternalNode pathNode = path.peek();
                    updateMetaInformationofThisNode((LeafNode) minDistNode, delta);
                    pathNode.metaSync(delta,path);
//                    path.pop();
                    // update minDistNode.parentPtr cf values.. after split
                    while(path.size() > 0 && !pathNode.insert(newCFNode)){
                        newCFNode1= pathNode.split(newCFNode);
                        delta = pathNode.getDelta();
                        if(pathNode.getParentPtr()!= null){
                            pathNode.getParentPtr().update(delta);
                        }else{
                            InternalNode root = new InternalNode(M, null , false);
                            root.insert(newCFNode1);
                            CFNode pathNodeCFNode = new CFNode(pathNode, new CFEntry(pathNode.getN(), pathNode.getLS(), pathNode.getSS()));
                            root.insert(pathNodeCFNode);
                            this.root = root;
                            break;
                        }
                        newCFNode = newCFNode1;
                        pathNode = path.peek();
                        pathNode.metaSync(delta, path);
                        path.pop();
                    }
                    delta = pathNode.getDelta();
                    if(pathNode.getParentPtr()!= null){
                        pathNode.getParentPtr().update(delta);
                    }
                    pathNode.metaSync(delta, path);
                }
            }else{
                System.err.println("While loop didn't reach the leaf node. in insert ");
            }
        }
    }

    //cf node
    private void updateMetaInformationofThisNode(LeafNode leafNode, CFEntry delta) {
        leafNode.getParentPtr().update(delta);
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
                            sb.append("level\n");
                        }else{
                            break;
                        }
                    }else{
                        sb.append("\n");
                        for(int i = 0 ; i < temp.size(); i++){
                            sb.append("Centroid: ");
                            sb.append(Distances.getCentroid(temp.get(i).getEntry().n, temp.get(i).getEntry().LS));
                            sb.append(" :: ");
                            sb.append(temp.get(i));
                            sb.append("\n");
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
            logger.error("getMinDistance.. not working as expected");
        }else{
            logger.debug("getMinDistance.. returned.. "+dist + data + "-->"+ minSofarRef);
        }
        return minSofarRef;
    }



}
