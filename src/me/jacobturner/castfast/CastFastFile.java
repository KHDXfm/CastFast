package me.jacobturner.castfast;

import java.io.File;
import java.util.ArrayList;

public class CastFastFile {
	public static void checkDir(ArrayList<String> showList) {
		File mainDir = new File("./Podcasts");
		if (!mainDir.isDirectory()) {
		   mainDir.mkdir();
		}
		for (int i = 0; i < showList.size(); i++) {
			File pcDir = new File("./Podcasts/" + showList.get(i));
			if (!pcDir.isDirectory()) {
			   pcDir.mkdir();
			}
		}
	}
}
