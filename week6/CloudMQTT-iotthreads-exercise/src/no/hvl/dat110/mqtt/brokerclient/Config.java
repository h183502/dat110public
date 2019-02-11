package no.hvl.dat110.mqtt.brokerclient;

public class Config {
	
	public static String topic = "Temp";							// You can change it to any topic of choice
    public static int qos = 1;
    public static String broker = "tcp://<hostname:port>"; 			// to be copied from CloudMQTT
    public static String pub_clientId = "MQTT_Temperature_PUB";
    public static String sub_clientId = "MQTT_Temperature_SUB";
    public static String username = "";								// to be copied from CloudMQTT
    public static String password = "";								// to be copied from CloudMQTT
    
}
