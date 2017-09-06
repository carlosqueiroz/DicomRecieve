/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;
import java.util.*;
import java.io.*;
/**
 *
 * @author rt
 */
public class Diretorios {
     public void listar(File directory) {
        if(directory.isDirectory()) {
            System.out.println(directory.getPath());
            String[] subDirectory = directory.list();
            if(subDirectory != null) {
                for(String dir : subDirectory){
                    listar(new File(directory + File.separator  + dir));
                }
            }
        }
    }
}
