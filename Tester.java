package finalproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* This tester tests the 
 * # of vertices added during crawling,
 * # of edges added during crawling,
 * if all vertices are correctly marked visited after crawling
 * if assignPageRanks in the SearchEngine class produces plausible output
 * if the search results are in correct order based on what I know they should be */

public class Tester {
	public static void main(String[] args) {
		SearchEngine searchEngine;
		try {
			searchEngine = new SearchEngine("test.xml");
			searchEngine.crawlAndIndex("www.cs.mcgill.ca");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		testNumVertices(0);
		testNumEdges(1);
		testVisited(2);
		testAssignRanks(3);
		testGetResults(4);
		
	}
	
	private static void testNumVertices(int testIdx)
	 {
	  System.out.println("[" + testIdx + "]: Test the number of vertices added during crawling.");
	  try {
	   SearchEngine searchEngine = new SearchEngine("test.xml");
	   searchEngine.crawlAndIndex("www.cs.mcgill.ca");

	   if (searchEngine.internet.vertexList.size() == 6) {
		System.out.println("Success. The number of vertices added during crawling was correct.\n");
	   } else {
	    System.out.println("Error: Number of vertices added during crawling is incorrect.\n");
	    
	   }

	    }
	  catch (Exception e) {
	   System.out.println("There was a problem.");
	   e.printStackTrace();
	  }

	 }
	
	private static void testNumEdges(int testIdx) 
	{
	  System.out.println("[" + testIdx + "]: Test the number of edges added during crawling.");
	  try {
	   SearchEngine searchEngine = new SearchEngine("test.xml");
	   searchEngine.crawlAndIndex("www.cs.mcgill.ca");
	   ArrayList<String> v = searchEngine.internet.getVertices();
	   int numEdges = 0;
	   for(String url: v){
	    int outDeg = searchEngine.internet.getOutDegree(url);
	    numEdges+=outDeg;
	   }
	   if(numEdges == 21)
		 System.out.println("Success. Correct Number of edges created.\n");
	   else {
	     System.out.println("Error: Number of edges added during crawling is incorrect.\n");
	   }
	  }
	  catch (Exception e) {
		System.out.println("There was a problem.");
		e.printStackTrace();
	  }
	 }
	
	 private static void testVisited(int testIdx)
	 {
	  System.out.print("[" + testIdx + "]: Test if vertices are marked visited during crawling.\n");
	  
	  try {
	   SearchEngine searchEngine = new SearchEngine("test.xml");
	   searchEngine.crawlAndIndex("www.cs.mcgill.ca");

	   Iterator it = searchEngine.internet.vertexList.entrySet().iterator();
	      while (it.hasNext()) {
	          Map.Entry pair = (Map.Entry)it.next();
	          if (!searchEngine.internet.getVisited((String) pair.getKey())) {
	           System.out.println("Error: Vertices are not marked visited during crawling.\n");
	           break;
	          }
	      }

	   System.out.println("Success. Vertices marked visited during crawling.\n");
	  }
	  catch (Exception e) {
	   System.out.println("There was a problem.");
	   e.printStackTrace();
	  }

	 }
	 
	 private static void testAssignRanks(int testIdx)
	 {
	  System.out.println("[" + testIdx + "]: Test whether AssignPageRanks produces plausible output. Passing this test does not gurantee your output is correct.");
	  String comment = "";
	 try {
	   boolean result = true;
	   SearchEngine searchEngine = new SearchEngine("test.xml");
	   searchEngine.crawlAndIndex("www.cs.mcgill.ca");

	   ArrayList<String> vertices = searchEngine.internet.getVertices();
	   ArrayList<Double> rankAfterOneIteration = searchEngine.computeRanks(vertices);

	   // set initial values for the ranks
	   for (String v : vertices)
	    searchEngine.internet.setPageRank(v, 1.0);

	   for (int i = 0; i < vertices.size(); i++)  {
	    if (Double.isInfinite(rankAfterOneIteration.get(i))) {
	     System.out.println(" Page rank for vertex " + vertices.get(i) + " is infinite. Check for divide by zero errors." + "\n");
	     result = false;
	     break;
	    }
	   }
	   
	   for (int i = 0; i < vertices.size(); i++) {
	    if (Double.isNaN(rankAfterOneIteration.get(i))) {
	     System.out.println(" Page rank for vertex " + vertices.get(i) + " is NaN. Check for divide by zero errors." + "\n");
	     result = false;
	     break;
	    }
	   }
	   
	   searchEngine.assignPageRanks(1000);
	   for (String v : vertices) {
	    if (searchEngine.internet.getPageRank(v) <= 0) {
	     System.out.println(" Page rank for vertex " + v + " is negetive. Page rank should be positive at each iteration." + "\n");
	     result = false;
	     break;
	    }
	   }
	   for (String v: vertices) {
		 System.out.printf("%.3f  ", searchEngine.internet.getPageRank(v));
	     System.out.println(v);
	   }
	  }
	
	  catch (Exception e) {
		System.out.println("There was a problem.");
		e.printStackTrace();
	  }
	 
	  System.out.println("Success. There were no impossible results. Do they look like good numbers to you?\n");
	 }
	 
	 private static void testGetResults(int testIdx)  
	 {
	  System.out.println("[" + testIdx + "]: Test whether the search results are correct.");
	  try {
	   boolean succeeded = true;
	   SearchEngine searchEngine = new SearchEngine("test.xml");
	   searchEngine.crawlAndIndex("www.cs.mcgill.ca");

	   searchEngine.assignPageRanks(0.01);
	   //try a word in the graph
	   ArrayList<String> results = searchEngine.getResults("and"); 

	   if(results != null && (results.get(0).equals("www.ea.com")==true || results.get(0).equals("www.unity.com")==true)){ 
	    System.out.println("Success. The #1 ranked result was correct.");
	   } else {
		   System.out.println("Failiure. The #1 ranked result was incorrect.");
		   succeeded = false;
	   }
	   if(results.get(1).equals("www.unity.com")==true || results.get(1).equals("www.ea.com")==true) {
		System.out.println("Success. The #2 ranked result was correct.");
	   } else {
		   System.out.println("Failiure. The #2 ranked result was incorrect.");
		   succeeded = false;
	   }
	   if(results.get(2).equals("www.cs.mcgill.ca")==true) {
			System.out.println("Success. The #3 ranked result was correct.");
	   } else {
		    System.out.println("Failiure. The #3 ranked result was incorrect.");
			succeeded = false;
	   }
	   if(results.get(3).equals("www.ubisoft.com")==true) {
			System.out.println("Success. The #4 ranked result was correct.");
	   } else {
		    System.out.println("Failiure. The #4 ranked result was incorrect.");
			succeeded = false;
	   }
	   if(results.get(4).equals("www.eidos.com")==true) {
			System.out.println("Success. The #5 ranked result was correct.");
	   } else {
		    System.out.println("Failiure. The #5 ranked result was incorrect.");
			succeeded = false;
	   }
	   if(results.get(5).equals("www.unreal.com")==true) {
			System.out.println("Success. The #6 ranked result was correct.");
	   } else {
		    System.out.println("Failiure. The #6 ranked result was incorrect.");
			succeeded = false;
	   }
	   if(!succeeded){
	    System.out.println("OVERALL FAILIRUE. At least one result was ranked incorrectly.\n");
	   } else {
		   System.out.println("GREAT SUCCESS!");
	   }
	  }
	  catch (Exception e) {
		System.out.println("There was a problem.");
	    e.printStackTrace();
	  }
	 }
	 
	 
	

}
