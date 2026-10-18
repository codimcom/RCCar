#include "SoftwareSerial.h"
SoftwareSerial mySerial(2, 3); // ports TX, RX

#define DRIVE 11
#define SIDE 10
#define IN1 4
#define IN2 5
#define IN3 6
#define IN4 7

int val;
int turn;
int speed;
int temp;

void setup() {
  mySerial.begin(9600);
  Serial.begin(9600);
  Serial.println("Connected");
  pinMode(DRIVE, OUTPUT);
  pinMode(SIDE, OUTPUT);
}

void loop() {
  
  if (mySerial.available()) {
    val = mySerial.read();

    turn = val/11 - 5;
    speed = val%11 - 5;
    Serial.print(turn*51);
    Serial.print(" ");
    Serial.println(speed*51);

    if (turn > 0){
      analogWrite(SIDE, fabs(turn*51));
      digitalWrite(IN1, HIGH);
      digitalWrite(IN2, LOW);
    } else if (turn < 0){
      analogWrite(SIDE, -turn*51);
      digitalWrite(IN2, HIGH);
      digitalWrite(IN1, LOW);
    } else if (turn == 0) {
      analogWrite(SIDE, 0);
      digitalWrite(IN1, LOW);
      digitalWrite(IN2, LOW);
    }

    if(speed > 0){
      analogWrite(DRIVE, speed*51);
      digitalWrite(IN3, HIGH);
      digitalWrite(IN4, LOW);
    } else if(speed < 0) {
      analogWrite(DRIVE, -speed*51);
      digitalWrite(IN4, HIGH);
      digitalWrite(IN3, LOW);
    } else if (speed == 0) {
      analogWrite(DRIVE, 0);
      digitalWrite(IN4, LOW);
      digitalWrite(IN3, LOW);
    }
  }
}