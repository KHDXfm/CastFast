package me.jacobturner.castfast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class CastFastFile {
	public static void checkPodcastDir(ArrayList<String> showList) {
		File mainDir = new File("./Podcasts");
		if (!mainDir.isDirectory()) {
		   mainDir.mkdir();
		}
		for (int i = 0; i < showList.size(); i++) {
			File podcastDir = new File("./Podcasts/" + showList.get(i));
			if (!podcastDir.isDirectory()) {
			   podcastDir.mkdir();
			}
		}
	}
	
	public static void checkShowsDir() {
		File mainDir = new File("./Shows");
		if (!mainDir.isDirectory()) {
		   mainDir.mkdir();
		}
	}
	
	public static ArrayList<String> getShowNames() {
		ArrayList<String> shows = new ArrayList<String>();
		File mainDir = new File("./Shows");
		File[] listOfShows = mainDir.listFiles(new FilenameFilter() {
			@Override
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".yml");
		    }
		});
	    for (int i = 0; i < listOfShows.length; i++) {
	    	if (listOfShows[i].isFile()) {
	    		shows.add(listOfShows[i].getName().split("\\.")[0]);
	    	}
	    }
		return shows;
	}
	
	public static void deleteShow(String show) {
		File oldShow = new File("./Shows/" + show + ".yml");
		oldShow.delete();
	}
}
