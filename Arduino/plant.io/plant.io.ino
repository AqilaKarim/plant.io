#include <dht.h>
#include <SoftwareSerial.h>

dht DHT;
SoftwareSerial esp8266(0, 1);      // RX, TX for ESP8266

bool DEBUG = true;   //show more logs
int responseTime = 10; //communication timeout

#define DHT11_PIN 7 //input for temperature
#define WATERPIN 2 //output for water pump
#define READSOILPIN A0 //input for soil moisture
#define MAXDRYNESS 700 //define upper limit of soil moisture
#define WATERDELAY 750 // define delay for pre water pump 
#define WATERPOSTDELAY 3000 //define delay for post water pump
#define SensorPin A1 //input for light 

void setup()
{
  Serial.begin(9600);
  pinMode(READSOILPIN, INPUT);
  pinMode(DHT11_PIN, INPUT);
  pinMode(SensorPin, INPUT);
  pinMode(WATERPIN, OUTPUT);
  esp8266.begin(9600);
  sendCommand("AT+RST\r\n",2000,DEBUG); // reset module
  sendCommand("AT+CWMODE=3\r\n",1000,DEBUG);// configure as access point
  sendCommand("AT+CWSAP=\"Plant.io\",\"\",1,0\r\n",3000,DEBUG); //set SSID as Plantio and password as Rplgroup9 
  delay(10000);
  //sendCommand("AT+CWLIF=\r\n",1000,DEBUG);
  sendCommand("AT+CIPMUX=1\r\n",1000,DEBUG); // configure for multiple connections
  sendCommand("AT+CIPSERVER=1,80\r\n",1000,DEBUG); // turn on server on port 80 (http)
  Serial.println("Server Ready");
}

void loop()
{
  int chk = DHT.read11(DHT11_PIN);
  Serial.print("\nTemperature\t= ");
  Serial.println(DHT.temperature);
  delay(1000);

  int SensorValueSoil = analogRead(READSOILPIN);
  Serial.print("Soil Moisture\t= ");
  Serial.println(SensorValueSoil);
  if(SensorValueSoil >= MAXDRYNESS)
  {
    Serial.print("Soil Dry, Start Watering");
    digitalWrite(WATERPIN, HIGH);
    delay(WATERDELAY);
    digitalWrite(WATERPIN, LOW);
    delay(WATERPOSTDELAY);
  }
  delay(50);

  int SensorValueLDR;
  SensorValueLDR = analogRead(SensorPin);
  Serial.print("Light Intensity\t= ");
  Serial.println(SensorValueLDR);
  Serial.print("%");
  delay(100);

  if(esp8266.available())
  {
     // get the connection id so that we can then disconnect
     int connectionId = esp8266.read()-48; 
     // subtract 48 because the read() function returns 
     // the ASCII decimal value and 0 (the first decimal number) starts at 48
      
     String content;
     content = ("Soil Moisture:");
     content += SensorValueSoil;
     //sendHTTPResponse(connectionId,content);
     //delay(1000);
     
     content += ("Temperature:");
     content += DHT.temperature;
     //sendHTTPResponse(connectionId,content);
     //delay(1000);
     
     content += ("Light intensity:");
     content += SensorValueLDR;
     //sendHTTPResponse(connectionId,content);
     //delay(1000);

     sendHTTPResponse(connectionId,content);
     
     // make close command
     String closeCommand = "AT+CIPCLOSE="; 
     closeCommand+=connectionId; // append connection id
     closeCommand+="\r\n";

     
     sendCommand(closeCommand,1000,DEBUG); // close connection
  }
}

/*
* Name: sendData
* Description: Function used to send data to ESP8266.
* Params: command - the data/command to send; timeout - the time to wait for a response; debug - print to Serial window?(true = yes, false = no)
* Returns: The response from the esp8266 (if there is a reponse)
*/
String sendData(String command, const int timeout, boolean debug)
{
    String response = "";
    
    int dataSize = command.length();
    char data[dataSize];
    command.toCharArray(data,dataSize);
           
    esp8266.write(data,dataSize); // send the read character to the esp8266
    if(debug)
    {
      Serial.println("\r\n====== HTTP Response From Arduino ======");
      Serial.write(data,dataSize);
      Serial.println("\r\n========================================");
    }
    
    long int time = millis();
    
    while( (time+timeout) > millis())
    {
      while(esp8266.available())
      {
        
        // The esp has data so display its output to the serial window 
        char c = esp8266.read(); // read the next character.
        response+=c;
      }  
    }
    
    if(debug)
    {
      Serial.print(response);
    }
    
    return response;
}
 
/*
* Name: sendHTTPResponse
* Description: Function that sends HTTP 200, HTML UTF-8 response
*/
void sendHTTPResponse(int connectionId, String content)
{
     
     // build HTTP response
     String httpResponse;
     String httpHeader;
     // HTTP Header
     httpHeader = "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\n"; 
     httpHeader += "Content-Length: ";
     httpHeader += content.length();
     httpHeader += "\r\n";
     httpHeader +="Connection: close\r\n\r\n";
     httpResponse = httpHeader + content + " "; // There is a bug in this code: the last character of "content" is not sent, I cheated by adding this extra space
     sendCIPData(connectionId,httpResponse);
}
 
/*
* Name: sendCIPDATA
* Description: sends a CIPSEND=<connectionId>,<data> command
*
*/
void sendCIPData(int connectionId, String data)
{
   String cipSend = "AT+CIPSEND=";
   cipSend += connectionId;
   cipSend += ",";
   cipSend +=data.length();
   cipSend +="\r\n";
   sendCommand(cipSend,1000,DEBUG);
   sendData(data,1000,DEBUG);
}
 
/*
* Name: sendCommand
* Description: Function used to send data to ESP8266.
* Params: command - the data/command to send; timeout - the time to wait for a response; debug - print to Serial window?(true = yes, false = no)
* Returns: The response from the esp8266 (if there is a reponse)
*/
String sendCommand(String command, const int timeout, boolean debug)
{
    String response = "";
           
    esp8266.print(command); // send the read character to the esp8266
    
    long int time = millis();
    
    while( (time + timeout) > millis())
    {
      while(esp8266.available())
      {
        
        // The esp has data so display its output to the serial window 
        char c = esp8266.read(); // read the next character.
        response+=c;
      }  
    }
    
    if(debug)
    {
      Serial.print(response);
    }
    
    return response;
}
