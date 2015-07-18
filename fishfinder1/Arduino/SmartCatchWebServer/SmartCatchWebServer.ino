/*
  SmartCatchWebServer
  Mike Gittelsohn
  4/6/2015
 */

#include <SPI.h>
#include <Ethernet.h>
#include "Servo.h"
#include <dht11.h>
#include <stdlib.h>

// Enter a MAC address and IP address for your controller below.
// The IP address will be dependent on your local network:
byte mac[] = { 
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
IPAddress ip(10,0,0,240);

// Initialize the Ethernet server library
// with the IP address and port you want to use 
// (port 80 is default for HTTP):
EthernetServer server(80);

// servo handler
#define NUMSERVOS 8		// bump this for more per http://www.arduino.cc/en/Reference/Servo
byte servo_install_count;	// number of servos that have been initialized
byte servo_pin[NUMSERVOS];	// the pins of those servos
Servo myservos[NUMSERVOS];	// the Servo object for those servos

// used for printing floats and ints
static char floatstr[15];
static char intstr[15];

int servoHandler(int pin, int val) {
  byte slot = 0;
	// is this pin already allocated a servo slot?
	while (slot < servo_install_count) {
		if (servo_pin[slot] == pin) break;
		slot++;
	}
	if (slot >= servo_install_count) {	// not found

		// do we have a free slot to allocate?
		if (servo_install_count < NUMSERVOS) {
			slot = servo_install_count++;
			servo_pin[slot] = pin;
			myservos[slot].attach(pin);
		}
		else {
			Serial.println("servohandler: oops!");	// please increase NUMSERVOS above
			return -1;
		}
	}
	myservos[slot].write(val);
	return 0;
}


// humidityHandler:
// returns temp in degrees C or
// -300 = DHTLIB_ERROR_CHECKSUM or
// -301 = DHTLIB_ERROR_TIMEOUT or
// -302 = unknown error


dht11 DHT11;

float humidityHandler(int pin) {
  int dhtpin = pin;
  int chk = DHT11.read(dhtpin);
  float humidity;
  switch (chk)
  {
    case DHTLIB_OK:
      humidity = (float)DHT11.humidity;
      break;
    case DHTLIB_ERROR_CHECKSUM:
      humidity = -300;
      break;
    case DHTLIB_ERROR_TIMEOUT:
      humidity = -301;
      break;
    default:
      humidity = -302;
      break;
  }
  delay (2000);
  return humidity;
}


float tempHandler(int pin) {
  int dhtpin = pin;
  int chk = DHT11.read(dhtpin);
  float temp;
  switch (chk)
  {
    case DHTLIB_OK:
      temp = (float)DHT11.temperature;
      // Serial.println((float)DHT11.temperature, 2);
      break;
    case DHTLIB_ERROR_CHECKSUM:
      temp = -300;
      break;
    case DHTLIB_ERROR_TIMEOUT:
      temp = -301;
      break;
    default:
      temp = -302;
      break;
  }
  delay (2000);
  return temp;
}

void setup() {
 // Open serial communications and wait for port to open:
  Serial.begin(9600);
  pinMode(20, 1);
  pinMode(21, 1);

  // start the Ethernet connection and the server:
  Ethernet.begin(mac, ip);
  server.begin();
  Serial.print("server is at ");
  Serial.println(Ethernet.localIP());
}

String req;

void reply (EthernetClient client, String cmd, String lport, String val) {
  if (client) {
    client.print(cmd);
    client.print("("); 
    client.print(lport); 
    client.print(","); 
    client.print(val);
    client.println(")");
  }
  Serial.print(cmd);
  Serial.print("("); 
  Serial.print(lport); 
  Serial.print(","); 
  Serial.print(val);
  Serial.println(")");
}

void handleRequest(EthernetClient client) {
    // GET /dw(ppp,vvv) HTTP/1.1
    String cmd = req.substring(5,7);
    String port = req.substring(8,11);
    String val = req.substring(12,15);
    long lval = val.toInt();
    long lport = port.toInt();
    long ret;
    
    // send a standard http response header
    if (client) {
      client.println("HTTP/1.1 200 OK");
      client.println("Content-Type: text/html");
      client.println("Connection: close");  // the connection will be closed after completion of the response
      client.println("Refresh: 5");  // refresh the page automatically every 5 sec
      client.println();
      client.println("<!DOCTYPE HTML>");
      client.println("<html>");
    }
    // output the value of each analog input pin
    /*
    for (int analogChannel = 0; analogChannel < 6; analogChannel++) {
      int sensorReading = analogRead(analogChannel);
      client.print("analog input ");
      client.print(analogChannel);
      client.print(" is ");
      client.print(sensorReading);
      client.println("<br />");       
    }
    */
    
    if (cmd.equals("dr")) { // digitalRead
      ret = digitalRead(lport);
      itoa(ret,intstr,10); 
      reply(client, cmd, port, intstr);
    }
    else if (cmd.equals("dw")) { // digitalWrite
        digitalWrite(lport, lval);
        reply (client, cmd, port, val);
    }
    else if (cmd.equals("ar")) { // analogRead
        ret = analogRead(lport);
        itoa(ret, intstr, 10);
        reply (client, cmd, port, intstr);
    }
    else if (cmd.equals("aw")) { //analogWrite
        analogWrite(lport, lval);
        reply (client, cmd, port, val);
    }
    else if (cmd.equals("sh")) { // servoHandler
        servoHandler(lport, lval);
        reply(client, cmd, port, val);
    }
    else if (cmd.equals("th")) { // tempHandler
        float temp = tempHandler(lport);
        dtostrf(temp,7, 3, floatstr);
        reply(client, cmd, port, floatstr);
    }
     else if (cmd.equals("hh")) { // humidityHandler
        float humidity = humidityHandler(lport);
        dtostrf(humidity, 7, 3, floatstr);
        reply(client, cmd, port, floatstr);
    }
    else if (cmd.equals("pm")) { // pinMode
        pinMode(lport, lval);
        reply (client, cmd, port, val);
    }
    
    if (client)
      client.println("</html>");
    req = "";            // finished with request, empty string
}

void loop() {
  while (Serial.available() > 0)
  {
      char received = Serial.read();
      Serial.write(&received, 1);
      req += received; 

      // Process message when new line character is received
      if (received == '\r' || received == '\n') {
          req = "GET /" + req;
          handleRequest(0);
          req = ""; // Clear received buffer
      }
  }
  
  // listen for incoming clients
  EthernetClient client = server.available();
  if (client) {
    Serial.println("new client");
    // an http request ends with a blank line
    boolean currentLineIsBlank = true;
    while (client.connected()) {
      if (client.available()) {
        char c = client.read();
        req += c;
        Serial.write(c);
        // if you've gotten to the end of the line (received a newline
        // character) and the line is blank, the http request has ended,
        // so you can send a reply
        if (c == '\n' && currentLineIsBlank) {
          handleRequest(client);
          break;
        }
        if (c == '\n') {
          // you're starting a new line
          currentLineIsBlank = true;
        } 
        else if (c != '\r') {
          // you've gotten a character on the current line
          currentLineIsBlank = false;
        }
      }
    }
    // give the web browser time to receive the data
    delay(1);
    // close the connection:
    client.stop();
    Serial.println("client disonnected");
  }
}

