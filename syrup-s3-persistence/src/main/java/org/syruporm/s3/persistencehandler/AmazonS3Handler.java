package org.syruporm.s3.persistencehandler;

import java.io.InputStream;
import java.io.OutputStream;

import org.syruporm.core.PersistenceHandler;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

/**
 * Leverages the power of Amazon Web Services S3 to provide a cloud-based,
 * scalable persistence contrivance
 * 
 * @since 1.1.0
 */
public class AmazonS3Handler implements PersistenceHandler {

	private AmazonS3 s3Client;
	private String bucketName;
	private String objectPrefix;

	/**
	 * Instantiates an instance of S3OutputStream to write data to AWS S3
	 * 
	 * @param myClass Class to be persisted with Amazon S3
	 * @return The OutputStream for underlying Properties object to write data
	 * @since 1.1.0
	 */
	@Override
	public synchronized OutputStream save(Class<? extends Object> myClass) {
		String objectPath = (objectPrefix != null && objectPrefix.length() > 0) ? objectPrefix + myClass.getName()
				: myClass.getName();

		OutputStream outputStream = new S3OutputStream(s3Client, bucketName, objectPath);

		return outputStream;
	}

	/**
	 * Provides an InputStream that points to the correct object in AWS S3 to read
	 * persisted data
	 * 
	 * @param myClass Class that has been persisted with Amazon S3
	 * @return InputStream for underlying Properties object to read data
	 * @since 1.1.0
	 */
	@Override
	public InputStream load(Class<? extends Object> myClass) {
		String objectPath = (objectPrefix != null && objectPrefix.length() > 0) ? objectPrefix + myClass.getName()
				: myClass.getName();
		S3Object s3Object = null;

		try {
			s3Object = s3Client.getObject(bucketName, objectPath);
		} catch (Exception e) {
			return new EmptyS3InputStream();
		}

		if (s3Object == null || !(s3Object instanceof S3Object))
			return new EmptyS3InputStream();

		InputStream inputStream = s3Object.getObjectContent();

		return inputStream;
	}

	/**
	 * Deletes persisted data according to the naming convention of Syrup managed
	 * object
	 * 
	 * @param myClass Class to be deleted from Amazon S3
	 * @return True or false in relation to the success of the operation
	 * @since 1.1.0
	 */
	@Override
	public Boolean delete(Class<? extends Object> myClass) {
		String objectPath = (objectPrefix != null && objectPrefix.length() > 0) ? objectPrefix + myClass.getName()
				: myClass.getName();

		try {
			s3Client.deleteObject(bucketName, objectPath);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Provides an instance of AmazonS3Handler with specified bucket name and
	 * default Amazon S3 client
	 * 
	 * @param bucketName Name of the bucket in S3 to use
	 * @since 1.1.0
	 */
	public AmazonS3Handler(String bucketName) {
		this.bucketName = bucketName;

		s3Client = AmazonS3ClientBuilder.defaultClient();
	}

	/**
	 * Provides an instance of AmazonS3Handler with specified bucket name, object
	 * prefix (such as folder paths in S3) and a default Amazon S3 client
	 * 
	 * @param bucketName   Name of the bucket in S3 to use
	 * @param objectPrefix Any sort of prefix to the object storage path in S3,
	 *                     including folder prefixes and excluding bucket name
	 * @since 1.1.0
	 */
	public AmazonS3Handler(String bucketName, String objectPrefix) {
		this.bucketName = bucketName;
		this.objectPrefix = objectPrefix;

		s3Client = AmazonS3ClientBuilder.defaultClient();
	}

	/**
	 * Provides an instance of AmazonS3Handler leveraging provided AmazonS3 client
	 * and specified bucket name
	 * 
	 * @param s3Client   Specific AmazonS3 instance
	 * @param bucketName Name of the bucket in S3 to use
	 * @since 1.1.0
	 */
	public AmazonS3Handler(AmazonS3 s3Client, String bucketName) {
		this.s3Client = s3Client;
		this.bucketName = bucketName;
	}

	/**
	 * Provides an instance of AmazonS3Handler leveraging provided AmazonS3 client,
	 * specified bucket name and object prefix (such as folder paths in S3)
	 * 
	 * @param s3Client     Specific AmazonS3 instance
	 * @param bucketName   Name of the bucket in S3 to use
	 * @param objectPrefix Any sort of prefix to the object storage path in S3,
	 *                     including folder prefixes and excluding the bucket name
	 * @since 1.1.0
	 */
	public AmazonS3Handler(AmazonS3 s3Client, String bucketName, String objectPrefix) {
		this.s3Client = s3Client;
		this.bucketName = bucketName;
		this.objectPrefix = objectPrefix;
	}

	/**
	 * Specify the instance of AmazonS3 to use
	 * 
	 * @param s3Client Specific AmazonS3 instance
	 * @return Instance of AmazonS3Handler with specified options
	 * @since 1.1.0
	 */
	public AmazonS3Handler withS3Client(AmazonS3 s3Client) {
		this.s3Client = s3Client;

		return this;
	}

	/**
	 * Specify the instance of AmazonS3 to use
	 * 
	 * @param bucketName Specific bucket name to use
	 * @return Instance of AmazonS3Handler with specified options
	 * @since 1.1.0
	 */
	public AmazonS3Handler withBucketName(String bucketName) {
		this.bucketName = bucketName;

		return this;
	}

	/**
	 * Specify an object prefix
	 * 
	 * @param objectPrefix Specific object prefix to use
	 * @return Instance of AmazonS3Handler with specified options
	 * @since 1.1.0
	 */
	public AmazonS3Handler withObjectPrefix(String objectPrefix) {
		this.objectPrefix = objectPrefix;

		return this;
	}

	/**
	 * @return the s3Client
	 */
	public AmazonS3 getS3Client() {
		return s3Client;
	}

	/**
	 * @param s3Client the s3Client to set
	 */
	public void setS3Client(AmazonS3 s3Client) {
		this.s3Client = s3Client;
	}

	/**
	 * @return the bucketName
	 */
	public String getBucketName() {
		return bucketName;
	}

	/**
	 * @param bucketName the bucketName to set
	 */
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	/**
	 * @return the objectPrefix
	 */
	public String getObjectPrefix() {
		return objectPrefix;
	}

	/**
	 * @param objectPrefix the objectPrefix to set
	 */
	public void setObjectPrefix(String objectPrefix) {
		this.objectPrefix = objectPrefix;
	}
}