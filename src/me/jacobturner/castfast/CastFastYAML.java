package me.jacobturner.castfast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;

public class CastFastYAML {
	public static CastFastShow readShow(String name) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		CastFastShow show = mapper.readValue(new File("./Shows/" + name + ".yml"), CastFastShow.class);
		return show;
	}
	
	public static void editShow(String origName, String name, String[] DJ, String[] email, String airs) throws IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory().enable(Feature.MINIMIZE_QUOTES));
		if (!origName.equals(name)) {
			CastFastFile.deleteShow(origName);
		}
		CastFastShow show = new CastFastShow(name, DJ, email, airs);
		FileOutputStream fos = new FileOutputStream(new File("./Shows/" + name + ".yml"));
		SequenceWriter sw = mapper.writerWithDefaultPrettyPrinter().writeValues(fos);
		sw.write(show);
	}
	
}
