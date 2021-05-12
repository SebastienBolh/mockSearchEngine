package finalproject;

import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	// this will contain a set of pairs (String, LinkedList of Strings)
	public HashMap<String, ArrayList<String> > wordIndex;   	
	public MyWebGraph internet;
	public XmlParser parser;

	//constructor
	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}
	
	/* 
	 * crawlAndIndex does a graph traversal of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 */
	public void crawlAndIndex(String url) throws Exception {
		//Add this URL vertex and mark Visited
		internet.addVertex(url);
		internet.setVisited(url, true);
		//Get content from this URL
		ArrayList<String> mots = parser.getContent(url);
		//for all its words
		for(String mot: mots) {
			//index them properly
			ArrayList<String> urlList = wordIndex.get(mot);
			if(urlList == null) {
				urlList = new ArrayList<String>();
				urlList.add(url);
				wordIndex.put(mot, urlList);
			} else {
				if(!urlList.contains(url)) {
					urlList.add(url);
				}
			}
		}
		//now we will analyze all sites it links to
		ArrayList<String> links = parser.getLinks(url);
		for(String link: links) {
			//for each word in each linked page
			ArrayList<String> words = parser.getContent(link);
			for(String word: words) {
				//index them properly
				ArrayList<String> urlList = wordIndex.get(word);
				if(urlList == null) {
					urlList = new ArrayList<String>();
					urlList.add(link);
					wordIndex.put(word, urlList);
				} else {
					if(!urlList.contains(link)) {
						urlList.add(link);
					}
				}
			}
			//for each linked page, vertex / visited / edge
			if (!internet.getVisited(link)) {
				internet.addVertex(link);
				//dank recursion
				crawlAndIndex(link);
			}
			internet.addEdge(url, link);
			internet.setVisited(link, true);
			
			
		}
		
	}
	
	/* 
	 * assignPageRanks computes pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex(). Implementation created based on an algorithm that was 
	 * described in the assignment. 
	 */
	public void assignPageRanks(double epsilon) {
		// start by setting all pageRanks to 1
		for(String url: internet.getVertices()) {
			if(internet.getPageRank(url) == 0) {
				internet.setPageRank(url, 1);
			}
		}
		//loop through pages
		for(String url: internet.getVertices()) {
			//determine old rank, compute new rank, compare
			double prevRank = internet.getPageRank(url);
			computeRanks(internet.getVertices());
			double newRank = internet.getPageRank(url);
			//continue assigning rank until convergence is reached for difference between old and new rank
			while(!(Math.abs((prevRank) - (newRank)) < epsilon)) {
				prevRank = internet.getPageRank(url);
				computeRanks(internet.getVertices());
				newRank = internet.getPageRank(url);
			}
		}
	}

	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph 
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls. 
	 * Note that the double in the output list is matched to the url in the input list using 
	 * their position in the list.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		double dampingFactor = 0.5;
		double pr = 0;
		double total = 0;
		ArrayList<Double> ranks = new ArrayList<Double>();
		//set all the ranks to one
		for(String url: vertices) {
			if(internet.getPageRank(url) == 0) {
				internet.setPageRank(url, 1);
			}
		}
		
		//for each url
		for(String url: vertices) {
			//for each of the pages linking into it
			for(String linkedPage: internet.getEdgesInto(url)) {
				//add their rank/outdeg to this ones rank function
				pr += (internet.getPageRank(linkedPage) / internet.getOutDegree(linkedPage));
			}
			//finalize calucaltion step
			pr = (1 - dampingFactor) + (dampingFactor * pr);
			//set this page's rank
			internet.setPageRank(url, pr);
			//add it to the list
			ranks.add(pr);
			//reset rank and go to next url
			pr = 0;
		}
		
		return ranks;
	}

	
	/* 
	 * Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 */
	public ArrayList<String> getResults(String query) {
		// TODO: Add code here
		ArrayList<String> urls = new ArrayList<String>();
	
		//if the word exists on the Internet
		if(wordIndex.keySet().contains(query)) {
			//for all the URLs in the index list for this word
			for(String url: wordIndex.get(query)) {
				//if the list is empty
				if(urls.size() == 0) {
					urls.add(url);
				} else {
					int index = 0;
					while(!(index >= urls.size()) && internet.getPageRank(url) <= internet.getPageRank(urls.get(index))){
						index += 1;
					}
					urls.add(index, url);
				}
			}
		}
		return urls;
	}
	
	
}
