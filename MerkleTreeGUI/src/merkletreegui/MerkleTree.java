/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package merkletreegui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author danielgraca
 */
public class MerkleTree implements Serializable{
    // elements of the tree
    private ArrayList<String> data;
    // tree structure (list of lists)
    private ArrayList<ArrayList<String>> tree;

    /**
     * Constructor
     * Creates an empty data list and empty tree
     */
    public MerkleTree() {
        this.data = new ArrayList<>();
        this.tree = new ArrayList<>();
    }

   /**
    * 
    * @return the elements list
    */
    public ArrayList<String> getData() {
        return data;
    }

    /**
     *
     * @return the list of lists (the tree itself)
     */
    public ArrayList<ArrayList<String>> getTree() {
        return tree;
    }

    /**
     * Initializes the tree with a list of the raw elements (no hashes)
     * @param elements elements to be hashed into the tree
     */
    public void initializeTree(List<String> elements) {
        tree = new ArrayList<ArrayList<String>>();
        data = (ArrayList<String>) elements;
        // loop through the data list and hash it into another list
        ArrayList<String> hashedData = new ArrayList<>();
        for (String elem : elements) {
            // hash in hexadecimal
            hashedData.add(Integer.toString(Math.abs(elem.hashCode()), 16));
        }
        // add the hashed data to the tree
        this.tree.add(hashedData);
        this.buildTree(hashedData);
    }

    /**
     * Builds the tree with the given data
     * @param data
     */
    public void buildTree(List<String> data) {

        // if tree has only one element (root) then it is finished
        if (data.size() == 1) {
            return;
        }

        // create a new array list
        ArrayList<String> hashedData = new ArrayList<>();
        String hash;
        // hash the elements in pairs
        for (int i = 0; i < data.size(); i+=2) {
            // in case it isn't possible to pair up elements
            if (i+2 > data.size()) {
                // hash in hexadecimal
                hashedData.add(Integer.toString(Math.abs(data.get(i).hashCode()), 16));
            } else {
                // hash in hexadecimal
                hash = Integer.toString(Math.abs(data.get(i).hashCode()), 16) + "" + Integer.toString(Math.abs(data.get(i+1).hashCode()), 16);
                hashedData.add(hash);
            }
        }
        this.tree.add(hashedData);
        buildTree(hashedData);
    }

    /**
     *
     * @return the tree's root as a string
     */
    public String getRoot() {
        return tree.get(tree.size()-1).get(0);
    }

    /**
     * Adds a new element into the data's list
     * @param elem element to be inserted
     */
    public void addElement(String elem) {
        // add element into data's list
        data.add(elem);
        // re-initialize the tree (and re-build's it)
        initializeTree(data);
    }

    /**
     * Removes an element from the data's list with a given index
     * @param idx index of the element to be removed
     */
    public void removeElement(int idx) {
        // remove element from data's list
        data.remove(idx);
        
        // if there are no elements, then the tree must be empty
        if (data.isEmpty()) {
            tree.clear();
            return;
        }
        
        // if there are elements, then we must
        // re-initialize the tree (and re-build it)
        initializeTree(data);
    }
    
    /**
     * Changes one element from the data
     * Re-initializes tree
     * @param idx index of the element to be changed
     * @param elem new element to be added
     */
    public void changeElement(int idx, String elem) {
        // change the raw element in data list
        data.set(idx, elem);
        // change the first list (of hashes) element
        tree.get(0).set(idx, Integer.toString(Math.abs(elem.hashCode()), 16));
        // changes everything else recursively
        changeElement(getParent(idx), 1);
    }
    
    /**
     * 
     * @param idx index of the element to change
     * @param lvl list in which the value will be changed
     */
    public void changeElement(int idx, int lvl) {
        // stopping condition
        if (lvl >= tree.size()) return;
        
        // get children
        String leftChild = tree.get(lvl-1).get(getLeftChild(idx));
        
        // check if there is right child
        try {
            String rightChild = tree.get(lvl-1).get(getRightChild(idx));
            String newHash = Integer.toString(Math.abs(leftChild.hashCode()), 16) + "" + Integer.toString(Math.abs(rightChild.hashCode()), 16);
            tree.get(lvl).set(idx, newHash);
        } catch (Exception e) {
            String newHash = Integer.toString(Math.abs(leftChild.hashCode()), 16);
            tree.get(lvl).set(idx, newHash);
        }
        
        changeElement(getParent(idx), lvl+1);
    }
    
    /**
     * 
     * @param idx index of the element
     * @return the index of one element parent
     */
    public int getParent(int idx) {
        return idx / 2;
    }
    
    /**
     * 
     * @param idx index of the parent
     * @return the index of one element children (left children)
     */
    public int getLeftChild(int idx) {
        return idx * 2;
    }
    
    /**
     * 
     * @param idx index of the parent
     * @return index of one element childrem (right children)
     */
    public int getRightChild(int idx) {
        return (idx * 2) + 1;
    }
    
    /**
     * get the proof of a leaf (given its index) as a list
     *
     * @param idx index of the element (leaft) we want to prove
     * @return list of elements that prove a leaf
     */
    public List<String> getProof(int idx) {
        // list of proofs
        List<String> proof = new ArrayList<>();
        int lvl = 0;
        int rightChild;
        int leftChild;
        
        if (idx % 2 == 0) { // leaf is left node, add right node
            try {
                proof.add(tree.get(0).get(idx+1));
            } catch (Exception e) {
                proof.add(tree.get(0).get(idx));
            }
        } else { // leaf is right node, add left node
            try {
                proof.add(tree.get(0).get(idx-1));
            } catch (Exception e) {
                proof.add(tree.get(0).get(idx));
            }
        }
        
        // loop through all tree
        while (true) {
            lvl++;
            
            // reduce idx
            if (idx != 0) {
                idx /= 2;
            } else if (lvl < tree.get(lvl).size() - 1) {
                idx++;
            }
            
            // gets parent 
            int parent = getParent(idx);
            // gets parent children
            rightChild = getRightChild(parent);
            leftChild = getLeftChild(parent);
            
            if (idx % 2 == 0) { // if node is left positioned
                try {
                    proof.add(tree.get(lvl).get(rightChild));
                } catch (Exception e) {
                    proof.add(tree.get(lvl).get(leftChild));
                }
            } else { // if node is right positioned
                try {
                    proof.add(tree.get(lvl).get(leftChild));
                } catch (Exception e) {
                    proof.add(tree.get(lvl).get(leftChild));
                }
            }
            
            
            // if the element is root, proof was "found"
            if (tree.get(lvl).get(idx).equals(getRoot())) {
                return proof;
            }
        }
    }

    /**
     *
     * @return the merkle tree in text format (String)
     */
    @Override
    public String toString() {
        String treeTxt = "";
        for (int i = tree.size()-1; i >= 0; i--) {
            treeTxt += tree.get(i) + "\n";
        }
        treeTxt += data;
        return treeTxt;
    }
}
