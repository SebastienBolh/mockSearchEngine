package finalproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry; // You may need it to implement fastSort

public class Sorting {

	/*
	 * input HashMap Comparable values 
	 * returns ArrayList with keys from map ordered 
	 * in descending order based on the values they mapped to
	 * 
	 * O(n*log(n)), n is # pairs on map
	 */
    public static <K, V extends Comparable> ArrayList<K> fastSort(HashMap<K, V> results) {
    	//get the arrayList of URLs
    	ArrayList<K> unsorted = new ArrayList<K>();
        unsorted.addAll(results.keySet());
   
        //make it an array to sort
        Object[] temp = unsorted.toArray();
        
        //mergeSort
        temp = sort(temp, 0, temp.length - 1, results);
        
        //turn it back to a list
        ArrayList<Object> sorted = new ArrayList<>(Arrays.asList(temp));
        
        return (ArrayList<K>) sorted;
        
    }
    
    //slightly overcomplicated but rather typical merge()
    private static <K, V extends Comparable> void merge(Object[] keys, int first, int mid, int last, HashMap<K, V> results) {
    	//find lengths of split arrays, n1 is first half n2 second half
    	int l1 = (mid - first) + 1;
    	int l2 = last - mid;
    	
    	//create arrays with correct size
    	Object half1[] = new Object[l1];
    	Object half2[] = new Object[l2];
    	
    	//fill respective arrays with correct values
    	for(int i = 0; i < l1; ++i) {
    		half1[i] = keys[first + i];
    	}
    	for(int c = 0; c < l2; ++c) {
    		half2[c] = keys[mid + 1 + c];
    	}
    	
    	//counters for first & second array halves
    	int i = 0, c = 0;
    	int k = first;
    	
    	//while they both have stuff
    	while(i < l1 && c < l2) {
    		//compare and set appropriately
    		if(results.get(half1[i]).compareTo(results.get(half2[c])) > 0) {
    			keys[k] = half1[i];
    			i++;
    		} else {
    			keys[k] = half2[c];
    			c++;
    		}
    		k++;
    	}
    	
    	//if either half has anything left at the end add it on
    	while(i < l1 || c < l2) {
    		if(i < l1) {
    			keys[k] = half1[i];
    			i++;
    			k++;
    		} else {
    			keys[k] = half2[c];
    			c++;
    			k++;
    		}
    	}
    }
    
	/*
	 * input HashMap Comparable values 
	 * returns ArrayList with keys from map ordered
	 * in descending order based on the values they mapped to 
	 * 
	 * O(n^2) bubble sort, n is # pairs on map
	 */
    public static <K, V extends Comparable> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());

        //bubble sort
        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) <0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    

    private static <K, V extends Comparable> Object[] sort(Object[] keys, int first, int last, HashMap<K, V> results) {
    	if (first < last) {
    		//find midpoint
    		int mid = (first + last) / 2;
    		
    		//recursively sort first then second half
    		sort(keys, first, mid, results);
    		sort(keys, mid + 1, last, results);
    		
    		//merge all the pieces back together
    		merge(keys, first, mid, last, results);
    	}
    	return keys;
    }
    

    

}