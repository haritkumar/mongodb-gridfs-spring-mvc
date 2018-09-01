package com.gridfs.app;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {
	
	private int maxUploadSizeInMb = 150 * 1024 * 1024; // 5 MB
	
	@Bean
	public GridFsTemplate gridFsTemplate() throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
	}

	@Override
	protected String getDatabaseName() {
		return "gridfs_db";
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		MongoCredential mongoCredential =  MongoCredential.createCredential("root", "gridfs_db", "root".toCharArray());
		ServerAddress serverAddress = new ServerAddress("127.0.0.1",27017);
		List<MongoCredential> cred = new ArrayList<MongoCredential>();
		cred.add(mongoCredential);
		return new MongoClient(serverAddress, cred);
	}
	
	@Bean
    public CommonsMultipartResolver multipartResolver() {

        CommonsMultipartResolver cmr = new CommonsMultipartResolver();
        cmr.setMaxUploadSize(maxUploadSizeInMb * 2);
        cmr.setMaxUploadSizePerFile(maxUploadSizeInMb); //bytes
        return cmr;

    }

}
