

#include <SPI.h>
#include <WiFi.h>

char ssid[] = "HUAWEI_TIT-L01_1270"; //  your network SSID (name)
char pass[] = "8c724f99";    // your network password (use for WPA, or use as key for WEP)

// example.com

IPAddress server(93,184,216,34);
//char server[] = "www.example.com";
int port = 80;

// aws service

IPAddress awsserver(3,19,66,128);
//char server[] = "www.example.com";
int awsport = 8081;

int status = WL_IDLE_STATUS;

WiFiClient client;

const int DISCONNECTED = 0;
const int CONNECTED = 1;
int state = DISCONNECTED;

void printWifiStatus() {

  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  long rssi = WiFi.RSSI();
  Serial.print("signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}

void setup() {

  Serial.begin(9600);

  // check for the presence of the shield:
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    while (true);
  }

  String fv = WiFi.firmwareVersion();
  if (fv != "1.1.0") {
    Serial.println("Upgrade firmware");
  }

  int i = 0;
  while ((status != WL_CONNECTED) && (i < 3)) {
    
    Serial.print("Connecting to SSID [");
    Serial.print(i);
    Serial.print("] ");
    Serial.println(ssid);

    status = WiFi.begin(ssid, pass);

    delay(10000); // wait 10 seconds for connection:
    i++;
  }

  if (status == WL_CONNECTED) {
    Serial.println("Connected to wifi");
    printWifiStatus();
    state = CONNECTED;
  } else {
    Serial.println("Not connected to wifi");
  }
}


int c = 0;

void loop() {

    if ((c < 2) && (state == CONNECTED)) {
      doGetAws();
      delay(5000);
      c++;
    }

     // if there are incoming bytes available
  // from the server, read them and print them:
  while (client.available()) {
    char c = client.read();
    Serial.write(c);
  }

  // if the server's disconnected, stop the client:
  // if (!client.connected()) {
  //  Serial.println();
  //  Serial.println("disconnecting from server.");
  //  client.stop();
}

void doGetAws() {

  client.stop();
  
  Serial.println("\ndoGet - Connecting to server...");
  if (client.connect(awsserver, awsport)) {
    Serial.println("connected to server");
    // Make a HTTP request:
    client.println("GET /counters HTTP/1.1");
    client.println("Accept: application/json");
    client.println("Host: ec2-3-19-66-128.us-east-2.compute.amazonaws.com");
    //client.println("Host: localhost");
    client.println("Connection: close");
    client.println();
  } else {
    Serial.println("Unable to connect to server");
  }
}

void doGet() {

  client.stop();
  
  Serial.println("\ndoGet - Connecting to server...");
  if (client.connect(server, port)) {
    Serial.println("connected to server");
    // Make a HTTP request:
    client.println("GET /index.html HTTP/1.1");
    client.println("Host: www.example.com");
    client.println("Connection: close");
    client.println();
  } else {
    Serial.println("Unable to connect to server");
  }
}
