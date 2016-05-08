package me.jacobturner.castfast;

import java.io.File;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class CastFastS3 {
	public static void uploadFile(ArrayList<String> showData, String uploadFileName, String show) {
		CastFastS3Credentials s3Credentials = new CastFastS3Credentials();
		AmazonS3Client s3client = new AmazonS3Client(s3Credentials);
		try {
			File file = new File(uploadFileName);
			s3client.putObject(new PutObjectRequest(s3Credentials.getBucket(), show.replace(" ", "-"), file));
			String fileString = file.toString().split("/")[2];
			CastFastEmail.sendEmail(showData.get(2), fileString + "successfully uploaded", s3client.getUrl(s3Credentials.getBucket(), show.replace(" ", "-") + "/" + fileString).toString());
		} catch (AmazonServiceException ase) {
			System.out.println("AmazonServiceException:");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("AmazonClientException: ");
			System.out.println("Error Message: " + ace.getMessage());
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
