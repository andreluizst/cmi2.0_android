/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.android.cmi.model;

import java.io.Serializable;

/**
 *
 * @author Vitor
 */
public class Cor implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 44L;
	
	
	
	private int r,g,b;

    public Cor(){
        
    }
    
    public Cor(int r, int g, int b){
        
        this.r = r;
        this.g = g;
        this.b = b;
        
    }
    
    /**
     * @return the r
     */
    public int getR() {
        return r;
    }

    /**
     * @param r the r to set
     */
    public void setR(int r) {
        this.r = r;
    }

    /**
     * @return the g
     */
    public int getG() {
        return g;
    }

    /**
     * @param g the g to set
     */
    public void setG(int g) {
        this.g = g;
    }

    /**
     * @return the b
     */
    public int getB() {
        return b;
    }

    /**
     * @param b the b to set
     */
    public void setB(int b) {
        this.b = b;
    }
    
}
