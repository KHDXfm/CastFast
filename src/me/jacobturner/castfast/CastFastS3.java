package me.jacobturner.castfast;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class CastFastS3 {
	public static URL uploadFile(ArrayList<String> showData, String uploadFileName, String show) {
		CastFastS3Credentials s3Credentials = new CastFastS3Credentials();
		AmazonS3Client s3client = new AmazonS3Client(s3Credentials);
		try {
			File file = new File(uploadFileName);
			s3client.putObject(new PutObjectRequest(s3Credentials.getBucket(), show.replace(" ", "-"), file));
			String fileString = file.toString().split("/")[2];
			return s3client.getUrl(s3Credentials.getBucket(), show.replace(" ", "-") + "/" + fileString);
		} catch (AmazonServiceException ase) {
			System.out.println("AmazonServiceException:");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
			return null;
		} catch (AmazonClientException ace) {
			System.out.println("AmazonClientException: ");
			System.out.println("Error Message: " + ace.getMessage());
			return null;
		}
	}
}
