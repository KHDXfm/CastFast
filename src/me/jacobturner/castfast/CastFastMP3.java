package me.jacobturner.castfast;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
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
 		ID3v2 id3v2Tag;
 		if (mp3File.hasId3v1Tag()) {
 			mp3File.removeId3v1Tag();
 		}
 		if (mp3File.hasId3v2Tag()) {
 			mp3File.removeId3v2Tag();
 		}
 		if (mp3File.hasCustomTag()) {
 			mp3File.removeCustomTag();
 		}
 		id3v2Tag = new ID3v24Tag();
 		mp3File.setId3v2Tag(id3v2Tag);
    	id3v2Tag.setArtist(showData.get(1));
    	id3v2Tag.setComment(showData.get(0) + ", broadcast " + showData.get(3) + " on KHDX-FM 93.1, Conway, AR - khdx.fm");
		id3v2Tag.setTitle(date);
		id3v2Tag.setAlbum(showData.get(0));
		id3v2Tag.setYear(date.substring(0, 4));
		id3v2Tag.setUrl("http://khdx.fm");
		File f = new File("./cover.jpg");
		if (f.exists() && !f.isDirectory()) { 
			try {
				RandomAccessFile file = new RandomAccessFile("./cover.jpg", "r");
				byte[] bytes = new byte[(int) file.length()];
				file.read(bytes);
				file.close();
				id3v2Tag.setAlbumImage(bytes, "image/jpeg");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			mp3File.save("./Podcasts/" + showData.get(0) + "/" + date + "-" + showData.get(0) + ".mp3");
		} catch (NotSupportedException | IOException e) {
			e.printStackTrace();
		}
		return "./Podcasts/" + showData.get(0) + "/" + date + "-" + showData.get(0) + ".mp3";
	}
}
