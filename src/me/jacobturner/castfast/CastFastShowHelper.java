package me.jacobturner.castfast;

public class CastFastShowHelper {
	public static String arrayToString(String[] array) {
		StringBuilder stringArray = new StringBuilder();
		for(String i : array) {
			stringArray.append(i + ", ");
		}
		return stringArray.toString().substring(0, stringArray.toString().length() - 2);
	}
}
