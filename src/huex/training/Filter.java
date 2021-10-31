package huex.training;

import java.util.ArrayList;
import java.util.List;

public class Filter {
	
	public static List<NetflixData> filterByType(List<NetflixData> netflixData,int n,String type){
		List<NetflixData> results=new ArrayList<>();
		netflixData.stream().forEach(netflix-> {
	        if (netflix.getType().equals(type) && results.size()<n) {
	            results.add(netflix);
	        } 
	    });
		return results;
	}
	
	public static List<NetflixData> filterByListedIn(List<NetflixData> netflixData,int n,String listed){
		List<NetflixData> results=new ArrayList<>();
		netflixData.stream().forEach(netflix-> {
	        if (netflix.getListedIn()!=null  && results.size()<n) {
	        	if (netflix.getListedIn().stream().anyMatch(s -> s.trim().equals(listed.trim()))) {
		            results.add(netflix);
	        	}
	        } 
	    });
		return results;
	}
	
	public static List<NetflixData> filterByTypeAndCountry(List<NetflixData> netflixData,int n,String type,String country){
		List<NetflixData> results=new ArrayList<>();
		netflixData.stream().forEach(netflix-> {
	        if (netflix.getType().equals(type) && netflix.getCountries().contains(country) && results.size()<n) {
	            results.add(netflix);
	        } 
	    });
		return results;
	}
	
	public static List<NetflixData> filterByTypeAndDate(List<NetflixData> netflixData,int n,String type,long startDate,long endDate){
		List<NetflixData> results=new ArrayList<>();
		netflixData.stream().forEach(netflix-> {
	        if (netflix.getType().equals(type) && netflix.getDateAdded()!=0 && netflix.getDateAdded()>=startDate && netflix.getDateAdded()<=endDate && results.size()<n) {
	            results.add(netflix);
	        } 
	    });
		return results;
	}
	
	public static List<NetflixData> filterByListedInAndDate(List<NetflixData> netflixData,int n,String listed,long startDate,long endDate){
		List<NetflixData> results=new ArrayList<>();
		netflixData.stream().forEach(netflix-> {
	        if (netflix.getListedIn()!=null  && netflix.getDateAdded()>=startDate && netflix.getDateAdded()<=endDate && results.size()<n) {
	        	if (netflix.getListedIn().stream().anyMatch(s -> s.trim().equals(listed.trim()))) {
		            results.add(netflix);
	        	}
	        } 
	    });
		return results;
	}
	
	public static List<NetflixData> filterByTypeCountryAndDate(List<NetflixData> netflixData,int n,String type,String country,long startDate,long endDate){
		List<NetflixData> results=new ArrayList<>();
		netflixData.stream().forEach(netflix-> {
	        if (netflix.getType().equals(type) && netflix.getDateAdded()!=0 && netflix.getDateAdded()>=startDate && netflix.getDateAdded()<=endDate && netflix.getCountries().contains(country) && results.size()<n) {
	            results.add(netflix);
	        } 
	    });
		return results;
	}
}
