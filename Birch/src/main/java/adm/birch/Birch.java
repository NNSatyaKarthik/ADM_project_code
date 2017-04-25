package adm.birch;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by nagasaty on 4/20/17.
 */
//    B is for Branch Node
//    E is for Element Node
public class Birch{
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
        logger.debug(data+"--->"+this.root);
        LeafNode ltemp;
        InternalNode itemp;
        boolean isInserted;
        boolean internalNodeState = false;
        if(this.root == null){// create new Node.. and then insert
            itemp = new InternalNode(M, null, false); // creates a internal node
            ltemp = new LeafNode(L, itemp, true); // creates a new leaf node
            isInserted = ltemp.insert(data); // inserts data into the leaf node
            if(isInserted){
                CFNode n = new CFNode(ltemp);
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
            while(node.equals(root) || !(node instanceof LeafNode)){ // iterate till we reach the node.
                CFNode nodeWithMinDis = getMinDistance((InternalNode)node, data);
                ((InternalNode)node).addVectorInfo(data); // updates nodes information
//                nodeWithMinDis.addVectorInfoToEntry(data); // updates CFdata information
                path.push((InternalNode)node);// pushes node onto stack
                node = nodeWithMinDis.childPtr; // moves to the cfNode's child Poitner which is a new INternal Node
            }
            minDistNode = node; // you can remove the var minDistance node
            // we have reached the node with the min distance from root to the leaf
            if(minDistNode instanceof LeafNode){
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
                    logger.debug("Split required.. here" + data);
                    CFNode newCFNode= ((LeafNode)minDistNode).split(data);
                    CFEntry delta = ((LeafNode)minDistNode).getDelta();
                    logger.debug("Delta for leaf split: "+delta + " new cf node is : "+newCFNode);
                    logger.debug("Tree state after split(update not done): "+root);
                    CFNode newCFNode1;
                    InternalNode pathNode = path.peek();
//                    updateMetaInformationofThisNode((LeafNode) minDistNode, delta);
                    pathNode.metaSync(delta,path);
                    logger.debug("Leaf split done: state of the tree: "+root);
//                    path.pop();
                    logger.debug("pathNode: "+pathNode + "\t cf node: "+newCFNode);
                    // update minDistNode.parentPtr cf values.. after split
                    while(path.size() > 0 && !pathNode.insert(newCFNode)){
                        logger.debug("Insert failed for Internal cfnode: "+newCFNode + " at: "+pathNode);
                        logger.debug("State of tree before split: "+this.root);
//                        path.pop();
                        
                        newCFNode1= pathNode.split(newCFNode);
                        logger.debug("State of tree after split: "+this.root);
                        if(pathNode.getParentPtr()!= null){
                            internalNodeState = true;
                            delta = pathNode.getDelta();
                            logger.debug("Delta after split of internal node is : "+delta);
                            logger.debug(pathNode+"-- paret pointer is not null, updating cfinfo and parento to root info till the root with ");
//                            pathNode.getParentPtr().update(delta);
                            newCFNode = newCFNode1;
                            pathNode = path.peek();
                            logger.debug("state of tree before update: "+ this.root);
                            pathNode.metaSync(delta, path, 1);
                            logger.debug("state of tree after update: "+ this.root);
                            path.pop();
                            pathNode = path.peek();
                        }else{
                            InternalNode root = new InternalNode(M, null , false);
                            root.add(newCFNode1);
                            logger.debug("newCFNode1 (new split cfnode):"+newCFNode1);
                            logger.debug("added to root: "+root);

                            logger.debug("PathNode: " + pathNode);
                            CFNode pathNodeCFNode = new CFNode(pathNode);
                            logger.debug("pathNode: "+pathNode);
                            logger.debug("pathnode new CF:: "+pathNodeCFNode);
                            root.add(pathNodeCFNode);
                            
                            logger.debug("added to root: "+root);
                            logger.debug(pathNode.parentPtr);
                            pathNode.setParentPtr(pathNodeCFNode);
                            logger.debug(pathNode.parentPtr);
                            this.root = root;
                            pathNode = (InternalNode) this.root;
                            logger.debug("Treestate after creating new root):"+this.root);
                            break;
                        }
                    }
                    logger.debug("Tree state before returing from insert of "+data +" is : "+this.root);
                    delta = pathNode.getDelta();
//                    if(pathNode.getParentPtr()!= null){
//                        pathNode.getParentPtr().update(delta);
//                    }
                    if(pathNode.getParentPtr()!=null) {
                        if(internalNodeState){
                            pathNode.metaSync(delta, path, 1);
                        }
                        else pathNode.metaSync(delta, path);
                    }
                    logger.debug("Tree state after insert of "+data +" is : "+this.root);
                }
            }else{
                logger.error("While loop didn't reach the leaf node. in insert ");
            }
        }
    }

    //cf node
//    private void updateMetaInformationofThisNode(LeafNode leafNode, CFEntry delta) {
//        leafNode.getParentPtr().update(delta);
//    }

    public String levelOrderTraversal(){
        StringBuilder sb = new StringBuilder();
        if(this.root != null){
            Queue<InternalNode> queue = new LinkedList<>();
            int count = 0 ;
            int vcount = 0 ;
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
                            sb.append(Distances.getCentroid(temp.get(i).n.value, temp.get(i).LS));
                            sb.append(" :: ");
                            sb.append(temp.get(i));
                            sb.append("\n");
                            if(temp.get(i).getChildPtr() instanceof InternalNode){
                                queue.add((InternalNode) temp.get(i).getChildPtr());
                            }else{
                                count++;
                                vcount += temp.get(i).getChildPtr().getN().value;
                                for(Vector v : ((LeafNode)temp.get(i).getChildPtr()).points){
                                    System.out.println(v);
                                }
                            }
                        }
                    }
                }
                System.out.format("%d, %d", count, vcount);
            }
        }
        return sb.toString();
    }

    private CFNode getMinDistance(InternalNode inode, Vector data) {
        CFNode minSofarRef = null;
        CFNode temp;
        Vector centroid;
        double minSofar = -1, dist = -1;
        for(int i = 0 ; i < inode.size();i++){
            temp = inode.get(i);
            centroid = Distances.getCentroid(temp.n.value, temp.LS);
            dist = Distances.getD0(centroid, data);
            if(minSofar == -1 || Distances.getD0(centroid, data) < minSofar){
                minSofar = dist;
                minSofarRef = inode.get(i);
            }
        }
        if(minSofarRef == null) {
            logger.error("getMinDistance.. not working as expected");
        }else{
//            logger.debug("getMinDistance.. returned.. "+dist + data + "-->"+ minSofarRef);
        }
        return minSofarRef;
    }

    public Map<Integer, List<Vector>> labelData(int numberOfClusters){
        int levels = (int)Math.floor(Math.log(numberOfClusters)/Math.log(this.M));
        Queue<InternalNode> inodes = new LinkedList<>();
        inodes.add((InternalNode) this.root);
        inodes.add(null);
        InternalNode temp;
        int label = 0;
        int clusters = 0;
        Map<InternalNode, Integer> cfNodesMap = new HashMap<>();
        while(inodes.size() > 0){
            temp = inodes.poll();
            if(temp == null){
                if(inodes.size() >= numberOfClusters){
                    break;
                }
                clusters = 0;
                if(inodes.size() > 0 ){
                    inodes.offer(null);
                }else{
                    break;
                }
            }else{
                clusters++;
//                if(levels > 0){
                    for(int i = 0 ; i < temp.points.size(); i++){
                        if(temp.points.get(i).getChildPtr() instanceof InternalNode){
                            inodes.offer((InternalNode) temp.points.get(i).childPtr);
                        }
                    }
//                }else {
//                    for(int i = 0 ; i < temp.points.size(); i++){
//                        if(temp.points.get(i).getChildPtr() instanceof InternalNode){
//                            cfNodesMap.put((InternalNode) temp.points.get(i).childPtr, label++);
//                        }
//                    }
                    
//                }
            }
            levels--;
        }
        System.out.println(inodes.size());
        while(inodes.size() > 0) {
            temp = inodes.poll();
            cfNodesMap.put((InternalNode)temp, label++);
//            for (int i = 0; i < temp.points.size(); i++) {
//                if (temp.points.get(i).getChildPtr() instanceof InternalNode) {
//                    cfNodesMap.put((InternalNode) temp.points.get(i).childPtr, label++);
//                }
//            }
        }


        Map<Integer, List<Vector>> res = new HashMap<>();
        for(InternalNode node: cfNodesMap.keySet()){
            this.inorderTraversal(node, cfNodesMap.get(node), res);
        }
        return res;
    }

    private void inorderTraversal(InternalNode node, Integer label, Map<Integer, List<Vector>> map) {
        
        for(int i = 0 ; i < node.points.size(); i++){
            if(node.get(i).getChildPtr().getCapacity() == L){
                List<Vector> list ;
                if(!map.containsKey(label)) list = new ArrayList<Vector>();
                else list = map.get(label);
                list.addAll(((LeafNode)node.get(i).getChildPtr()).points);
                map.put(label, list);
            }else{
                inorderTraversal((InternalNode) node.get(i).getChildPtr(), label, map);
            }
        }
        return;
    }


    public Node getRoot() {
        return this.root;
    }
}
