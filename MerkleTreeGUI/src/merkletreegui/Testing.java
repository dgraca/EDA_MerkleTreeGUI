/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package merkletreegui;

import java.util.List;

/**
 *
 * @author danielgraca
 */
public class Testing {
    public static void main(String[] args) {
        MerkleTree tree = new MerkleTree();
        for (int i = 0; i < 6; i++) {
            tree.addElement(i + "");
        }
        
        System.out.println(tree.toString());
        
        int test = 10;
        List<String> proof = tree.getProof(test);
        System.out.println("[" + tree.getTree().get(0).get(test) + "]: " + proof);
    }
}
