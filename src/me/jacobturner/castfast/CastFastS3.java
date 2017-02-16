package me.jacobturner.castfast;

import java.io.File;
import java.net.URL;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class CastFastS3 {
	public static URL uploadFile(String show, String uploadFileName) throws AmazonServiceException, AmazonClientException {
		CastFastS3Credentials s3Credentials = new CastFastS3Credentials();
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(s3Credentials).build();
		File file = new File(uploadFileName);
		String keyString = show.replace(" ", "-") + "/" + file.toString().split("/")[3];
		s3Client.putObject(new PutObjectRequest(s3Credentials.getBucket(), keyString, file));
		s3Client.setObjectAcl(s3Credentials.getBucket(), keyString, CannedAccessControlList.PublicRead);
		return s3Client.getUrl(s3Credentials.getBucket(), keyString);
	}
}
