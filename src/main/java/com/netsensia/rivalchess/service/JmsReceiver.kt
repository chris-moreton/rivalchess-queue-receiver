package com.netsensia.rivalchess.service

import org.apache.activemq.ActiveMQConnectionFactory
import javax.jms.*


object JmsReceiver {
    // URL of the JMS server
    private val url = System.getenv("ACTIVE_MQ_URL")
    private val user = System.getenv("ACTIVE_MQ_USER")
    private val pass = System.getenv("ACTIVE_MQ_PASSWORD")

    // default broker URL is : tcp://localhost:61616"
    // Name of the queue we will receive messages from
    private const val subject = "RivalMatchQueue"
    @Throws(JMSException::class)
    @JvmStatic
    fun receive(): String {
        // Getting JMS connection from the server
        val connectionFactory = ActiveMQConnectionFactory(url)
        connectionFactory.userName = user
        connectionFactory.password = pass
        val connection = connectionFactory.createConnection()

        connection.start()

        // Creating session for seding messages
        val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)

        // Getting the queue 'JCG_QUEUE'
        val destination: Destination = session.createQueue(subject)

        // MessageConsumer is used for receiving (consuming) messages
        val consumer = session.createConsumer(destination)

        // Here we receive the message.
        val message = consumer.receive()

        // We will be using TestMessage in our example. MessageProducer sent us a TextMessage
        // so we must cast to it to get access to its .getText() method.
        connection.close()

        if (message is TextMessage) {
            return message.text
        }
        return "ERROR"
    }
}