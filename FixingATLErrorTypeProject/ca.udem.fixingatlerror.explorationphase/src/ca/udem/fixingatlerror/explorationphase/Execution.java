package ca.udem.fixingatlerror.explorationphase;

import java.util.ArrayList;

import anatlyzer.examples.api.ReferencePoint;
import anatlyzer.examples.api.Sigma;




/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MWM
 */
public class Execution 
{
    //--- User parameters ---
    // these values represents the chosen reference point and sigma value
    
    static double aspiration_values[] = {0.5,0.5};
    static ReferencePoint ref = new ReferencePoint(aspiration_values) ;
    static Sigma s = new Sigma(1);
    
    static int generations = 10 ;
    
    static int population_size = 5 ;
    
    Execution()
    {
        
    }
    
    public static void main(String[] args) throws Exception 
    {
    //	Solution s=new Solution();
//s.create_solution();
//s.evaluate_solution();
//System.out.println("Merci pour les resultats");
    	
    //	Window w=new Window();
 //   w.getSMClick();
	//s.footprintscalcul("");
     
	// System.out.println("nb errors est "+s.op.analyser.getATLModel().getErrors().getNbErrors());

	 Population p = new Population(population_size,s,ref,generations); 
       ArrayList<CoSolution> parents = new ArrayList<CoSolution>();
        
        p.create_poplulation();
        
        for(int i=0; i<generations; i++)
        {
            p.update_sigma_value(i, generations);
            parents = p.random_selection();
            p.generate_next_popluation(parents,i);
            //p.print_popluation_metrics(i);
        }
        
        
    }
}