package me.jacobturner.castfast;

import java.io.IOException;
import java.util.ArrayList;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class CastFastMP3 {
	public static String updateFile(String path, String date, ArrayList<String> showData) {
		Mp3File mp3File = null;
 		try {
			mp3File = new Mp3File(path);
		} catch (UnsupportedTagException | InvalidDataException | IOException e) {
			e.printStackTrace();
		}
		ID3v1 id3v1Tag;
		if (mp3File.hasId3v1Tag()) {
			mp3File.removeId3v1Tag();
		}
		id3v1Tag = new ID3v1Tag();
		mp3File.setId3v1Tag(id3v1Tag);
    	id3v1Tag.setArtist(showData.get(1));
    	id3v1Tag.setComment(showData.get(0) + ", broadcast " + showData.get(3) + " on KHDX-FM 93.1, Conway, AR - khdx.fm");
		id3v1Tag.setTitle(date);
		id3v1Tag.setAlbum(showData.get(0));
		id3v1Tag.setYear(date.substring(0, 4));
		try {
			mp3File.save("./Podcasts/" + showData.get(0) + "/" + date + "-" + showData.get(0) + ".mp3");
		} catch (NotSupportedException | IOException e) {
			e.printStackTrace();
		}
		return "./Podcasts/" + showData.get(0) + "/" + date + "-" + showData.get(0) + ".mp3";
	}
}
