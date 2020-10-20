package com.netsensia.rivalchess.service

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.S3ClientOptions
import com.amazonaws.services.s3.model.GetObjectRequest
import java.io.File
import java.io.InputStream

val accessKey = System.getenv("AWS_ACCESS_KEY_ID")
val secretKey = System.getenv("AWS_SECRET_ACCESS_KEY")
val endpoint = "s3-eu-west-2.amazonaws.com"

fun getS3Client(): AmazonS3Client {
    val credentials = BasicAWSCredentials(accessKey, secretKey)

    return AmazonS3Client(credentials).apply {
        setEndpoint(endpoint).apply {
            println("S3 endpoint is ${endpoint}")
        }
        setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).build())
    }
}

fun getEngine(
        s3Client: AmazonS3Client,
        engineVersion: String
) {
    val s3Name = "rivalchess-${engineVersion}-1.jar"
    val filePath = "/tmp/$s3Name"

    if (File(filePath).exists()) {
        println("$s3Name already exists")
    } else {
        println("Getting engine $engineVersion from S3")
        val o = s3Client.getObject(GetObjectRequest("rivalchess-jars", s3Name))
        val objectData: InputStream = o.objectContent
        File(filePath).writeBytes(objectData.readBytes())
        objectData.close()
    }
}

fun getEngines(engine1: String, engine2: String) {
    val client = getS3Client()
    getEngine(client, engine1)
    getEngine(client, engine2)
}
