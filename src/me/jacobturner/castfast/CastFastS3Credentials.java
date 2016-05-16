package me.jacobturner.castfast;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import me.jacobturner.castfast.CastFastOptions;

public class CastFastS3Credentials implements AWSCredentialsProvider {
	@Override
	public AWSCredentials getCredentials() {
		CastFastOptions options = new CastFastOptions();
		String accessKey = options.getValue("access_key");
		String secretKey = options.getValue("secret_key");
		options.close();
		return new BasicAWSCredentials(accessKey, secretKey);
	}

	@Override
	public void refresh() {}
	
	public String getBucket() {
		CastFastOptions options = new CastFastOptions();
		String bucketName = options.getValue("bucket_name");
		options.close();
		return bucketName;
	}

}
