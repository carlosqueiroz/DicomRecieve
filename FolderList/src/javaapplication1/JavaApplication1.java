/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
http://respostas.guj.com.br/28821-como-fazer-para-percorrer-pastas-e-subpastas-com-java\

http://www.guj.com.br/t/listando-arquivos-de-um-diretorio-java/44086/6 arquivos
 */
package javaapplication1;
import javaapplication1.Diretorios;
/**
 *
 * @author rt
 */
import java.io.File;
public class JavaApplication1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Diretorios dir = new Diretorios();
        dir.listar(new File("\\\\servidor\\ftproot\\imagem"));
        System.out.println("Hello, World");
    }
    
}
