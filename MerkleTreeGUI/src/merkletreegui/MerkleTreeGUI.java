/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package merkletreegui;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danielgraca
 */
public class MerkleTreeGUI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MerkleTree mt = new MerkleTree();
        mt.addElement("1");
        mt.addElement("3");
        mt.addElement("xpto");
        System.out.println(mt);
        System.out.println("--------------");
        System.out.println("Root = " + mt.getRoot());
        mt.addElement("rodume");
        System.out.println(mt);
        System.out.println("--------------");
        mt.removeElement(2);
        System.out.println(mt);
        System.out.println("--------------");
        System.out.println("Root = " + mt.getRoot());
        System.out.println("Proof of rodume: " + mt.getProof(2));
    }

}
