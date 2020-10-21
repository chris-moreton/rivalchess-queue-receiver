package com.netsensia.rivalchess.service

import com.google.gson.Gson
import com.netsensia.rivalchess.vie.model.MatchResult
import org.apache.activemq.ActiveMQConnectionFactory
import javax.jms.Destination
import javax.jms.JMSException
import javax.jms.Session

object JmsSender {
    //URL of the JMS server. DEFAULT_BROKER_URL will just mean that JMS server is on localhost
    private val url = System.getenv("ACTIVE_MQ_URL")
    private val user = System.getenv("ACTIVE_MQ_USER")
    private val pass = System.getenv("ACTIVE_MQ_PASSWORD")

    // default broker URL is : tcp://localhost:61616"
    private const val subject = "RivalResultQueue" // Queue Name.You can create any/many queue names as per your requirement.
    @Throws(JMSException::class)
    @JvmStatic
    fun send(message: MatchResult) {
        // Getting JMS connection from the server and starting it
        val connectionFactory = ActiveMQConnectionFactory(url)
        connectionFactory.userName = user
        connectionFactory.password = pass
        val connection = connectionFactory.createConnection()
        connection.start()

        //Creating a non transactional session to send/receive JMS message.
        val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)

        //Destination represents here our queue 'JCG_QUEUE' on the JMS server.
        //The queue will be created automatically on the server.
        val destination: Destination = session.createQueue(subject)

        // MessageProducer is used for sending messages to the queue.
        val producer = session.createProducer(destination)

        // We will send a small text message saying 'Hello World!!!'
        val gson = Gson()
        val sessionMessage = session.createTextMessage(gson.toJson(message))

        // Here we are sending our message!
        producer.send(sessionMessage)
        println("JCG printing@@ '" + sessionMessage.text + "'")
        connection.close()
    }
}