#include <SoftwareSerial.h>

SoftwareSerial bluetooth(12, 13); // RX, TX
const int COFFEEMAKER = 8; 
char recvChar;

void setup() {
  Serial.begin(9600);
  bluetooth.begin(9600);
  pinMode(COFFEEMAKER, OUTPUT);
  digitalWrite(COFFEEMAKER, HIGH);
}

void loop() {
  if (bluetooth.available()) {
    recvChar = bluetooth.read();
    if (recvChar == '0') {
      digitalWrite(COFFEEMAKER, HIGH);
    } else if(recvChar == '1') {
      digitalWrite(COFFEEMAKER, LOW);
    }
    Serial.print(recvChar);
  }
}
