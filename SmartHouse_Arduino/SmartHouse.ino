#include <dht11.h>
#include <Versalino.h>

#include <DHT.h>
#include <DHT_U.h>



//Bluetooht haberleşme pinleri -----------------------------------------------------------
#include <SoftwareSerial.h> //Bluetooth haberleşme kütüphanesini ekliyoruz.
SoftwareSerial BTSerial(0,1); // Rx,Tx

// Arduino İle Isı ve Nem Sensörü Bağlantı Kodları ---------------------------------------
#define DHTPIN 2  //DHT11X data pini

DHT dht(DHTPIN, DHT11); 

//Led pins -------------------------------------------------------------------------------
int Led1 = 3;
int Led2 = 4;
int Led3 = 5;
int Led4 = 6;
int Led5 = 7;

// Hareket sensörü (PIR sensörü) -----------------------------------------------------------
int PirPin = 8; // Hareket sensörü out pini
int ValuePir = 0;  // Pirpin değerini kaydetmek için

//Buzzer ------------------------------------------------------------------------------------
int BuzzerPin = 9;

//MQ-4 gaz sensörü --------------------------------------------------------------------------
const int AOUTpin = A0;
//#define AOUTpin A0  //A0 pinini tanımlıyoruz.
const int DOUTpin = 10;


int valueMQ = 0;

//Mesafe Sensörü ----------------------------------------------------------------------------
int EchoPin = 12; // Mesafe sensörü alıcı pini
int TrigPin = 13; // Mesafe sensörü verici pini
unsigned long Time;  //Hareket sönseründen çıkan sinyalin geri geliş süresini tutmak için
double TotalDistance; // sinyalin gidip gelidği taplam mesafe
int BetweenDistance;  // Sensör ile cisim arasındaki mesafe
int DistanceLimit = 10; // Mesafe ölçeri sınırını belirlemek için (cm cinsinden)

//-------------------------------------------------------------------------------------------

//Gerekli değişkenler
bool AlarmFlag = false; // Alarmın aktif olup olmadığını anlamak için
char data = 0;
bool StartFlag = true; // Açılış animasyonunu takip etmek için kulanıcağımız işaret
int ValueAlarm = 0;
bool FireFlag = true;
#define threshold 150








//--------------------------------------------------------------------------------------------

void setup() {
    // put your setup code here, to run once:
    Serial.begin(9600); //Serial Porttan veri göndermek için baundrate ayarlanıyor.
    //Serial.println("System Starting");
    
  BTSerial.begin(9600);

  dht.begin(); 
  
  pinMode(DOUTpin, INPUT);  //MQ-4 Sensörümüzün data pinini input (alıcı) konumuna getiriyoruz.
  
  pinMode(PirPin, INPUT); // Hareket sensörü alıcı konumuna gecer
 
  pinMode(TrigPin, OUTPUT); // Trig pinin verici
  pinMode(EchoPin, INPUT);  // Echo pinini alıcı konumuna getiriyoruz.

//Led pinlerine akım göndermek için output olarak belirliyoruz.
  pinMode(Led1, OUTPUT);
  pinMode(Led2, OUTPUT);
  pinMode(Led3, OUTPUT);
  pinMode(Led4, OUTPUT);
  pinMode(Led5, OUTPUT);

//Buzzer pinlerine akım göndermek için output olarak belirliyoruz.
  pinMode(BuzzerPin,OUTPUT);
  digitalWrite(BuzzerPin, HIGH);
  
}

// -----------------------------------------------------------------------------------------------------------------------------------------------------------------

void loop() {
    // put your main code here, to run repeatedly:


    if(BTSerial.available())
        Serial.write(BTSerial.read());
    if(Serial.available())
        BTSerial.write(Serial.read());

        

        
   /*
        if(StartFlag == true){
            Leds(); // açılışta sırasıyla bütün ledleri yakıp söndürmek içi
            StartFlag = false;
        }

    */


  HeatHumidity();
  MetanDetector();
  DistanceDetector();

  AndroidLeds();

  //Sensör methodlarını çağırıyoruz.
    if(AlarmFlag == false){
       MoveDetector();
    }
    else{
       BuzzerModule();
    }
}










//------------------------------------------------------------------------------------------------------------------------------------------------

void MoveDetector(){
  ValuePir = digitalRead(PirPin); // Dijital pin okunarak değer değişkenine yazılıyor.

  //Hareket algılandığında ledleri yakıyoruz hareket yoksa söndürüyoruz.
    //digitalWrite(Led1,ValuePir);
    digitalWrite(Led2,ValuePir);
    digitalWrite(Led3,ValuePir);
    //digitalWrite(Led4,HIGH);
    //digitalWrite(Led5,HIGH);
}

//------------------------------------------------------------------------------------------------------------------------------------------------

void DistanceDetector(){

  /* Başlangıçta LOW durumda olan trigger bacağına gerilim uygulayıp ardından gerilimi keserek bir ses dalgası
  oluşturmuş oluyoruz. Bu işlem arasında 10 mikro saniye beklenmenin sebebi HC-SR04'ün en az 10 mikro saniyelik 
  dalgalar ile çalışmasıdır. */
  digitalWrite(TrigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(TrigPin, LOW);
 
  // Dalga üretildikten sonra geri yansıyıp Echo bacağının HIGH duruma geçireceği süreyi pulseIn fonksiyonu ile 
  // kaydediyoruz. 
  Time = pulseIn(EchoPin, HIGH);
 
  TotalDistance = (double)Time*0.034; // Toplam süreyi ses hızına bölerek sinyalin toplam mesafesini buluyoruz.
  BetweenDistance = TotalDistance / 2;// Toplam mesafeyi 2 ye bölerek cisim ile sensör arasındaki mesafeyi buluyoruz.

    if(BetweenDistance <= DistanceLimit)  //Değer, limit değerinden küçük veya eşit ise değeri yazdırıyoruz.
    { 
        Serial.print(BetweenDistance);
        Serial.print("-");
        digitalWrite(Led1,HIGH);
        delay(1000);
    }
     else
      {
      digitalWrite(Led1,LOW);
      }
   
}

//-----------------------------------------------------------------------------------------------------------------------------------------------

void HeatHumidity(){
 
  float nem = dht.readHumidity();          //Nem bilgisi alınır
  float sicaklik = dht.readTemperature();  //Sıcaklık bilgisi alınır

 if(nem != 0 || sicaklik != 0){
  digitalWrite(Led5, HIGH);
  Serial.print(nem  );
  Serial.print("-");
  Serial.print(sicaklik);
  Serial.print("-");
 }
else{
  digitalWrite(Led5, LOW);
  digitalWrite(BuzzerPin, HIGH);
 }

 if(sicaklik >= 21.00){

      if(FireFlag == true){
      digitalWrite(BuzzerPin, LOW);
      digitalWrite(Led5, HIGH); //Limit değerine ulaşıldığınde, LED4 dü yak
      delay(500);
      digitalWrite(Led5, LOW); //Limit değerine ulaşıldığınde, LED4 dü yak
      delay(500);
      digitalWrite(Led5, HIGH); //Limit değerine ulaşıldığınde, LED4 dü yak
      delay(500);
      digitalWrite(Led5, LOW); //Limit değerine ulaşıldığınde, LED4 dü yak
      delay(500);
      digitalWrite(Led5, HIGH); //Limit değerine ulaşıldığınde, LED4 dü yak
      delay(500);
      digitalWrite(Led5, LOW); //Limit değerine ulaşıldığınde, LED4 dü yak
      FireFlag = false;
      }    
}  
else
{
   digitalWrite(BuzzerPin, HIGH);
   FireFlag = false;
}
}

//------------------------------------------------------------------------------------------------------------------------------------------------

void MetanDetector(){
  valueMQ = analogRead(AOUTpin); //Metan sensörünün Aout analog pininden okunan değeri value değişkenine atıyoruz.
  
  
  Serial.print(valueMQ); //Metan değerini yazdırıyoruz
  Serial.print("-");
  
    if(valueMQ >= threshold){
    
      digitalWrite(Led4, HIGH); //Limit değerine ulaşıldığınde, LED4 dü yak
      digitalWrite(BuzzerPin, LOW);
      delay(100); // 10 milisaniye bekleme
      digitalWrite(BuzzerPin, HIGH);
      delay(100);
      
    }
  
  else{
    digitalWrite(Led4, LOW); //Limit aşılmadığında, Led4 dü kapat
    digitalWrite(BuzzerPin, HIGH);
   
  }
  
}

//------------------------------------------------------------------------------------------------------------------------------------------------

void BuzzerModule(){

ValueAlarm = digitalRead(PirPin); // Dijital pin okunarak değer değişkenine yazılıyor.

//Hareket algılandığında ledleri yakıyoruz hareket yoksa söndürüyoruz.
  if(ValueAlarm == HIGH){
    if(AlarmFlag == true ){
      
      digitalWrite(Led5,HIGH);
      digitalWrite(BuzzerPin, LOW);
      delay(1000);
      digitalWrite(BuzzerPin, HIGH);
      delay(1000);
     }
     else{
        digitalWrite(BuzzerPin, HIGH);
     }
  }else{
    digitalWrite(BuzzerPin, HIGH);
  }
}

//------------------------------------------------------------------------------------------------------------------------------------------------

void Leds(){
  // Ledleri ilk önce aktif edip(High duruma getirip) 1 saniye bekledikten sonra söndürüyoruz.
    digitalWrite(Led1, HIGH);
        delay(1000);
    digitalWrite(Led2, HIGH);
        delay(1000);
    digitalWrite(Led3, HIGH);
        delay(1000);
    digitalWrite(Led4, HIGH);
        delay(1000);
    digitalWrite(Led5, HIGH);
        delay(1000);
        
    digitalWrite(Led1, LOW);
    digitalWrite(Led2, LOW);
    digitalWrite(Led3, LOW);
    digitalWrite(Led4, LOW);
    digitalWrite(Led5, LOW);
}

//------------------------------------------------------------------------------------------------------------------------------------------------


void AndroidLeds(){

    data = Serial.read(); // Bluetooth tarafından gönderilen veriyi okuyoruz ve data değişkenine atıyoruz
    //data değişkeninin değerine göre ledlerimizi yakıp söndürüyoruz.
    
    if(data == '2')
      digitalWrite(Led1, HIGH);
    else if(data == '1')
      digitalWrite(Led1, LOW);
    else if(data == '4')
      digitalWrite(Led2, HIGH);
    else if(data == '3')
      digitalWrite(Led2, LOW);
    else if(data == '6')
      digitalWrite(Led3, HIGH);
    else if(data == '5')
      digitalWrite(Led3, LOW);
    else if(data == '9')
      AlarmFlag = true;
    else if(data == '8')
      AlarmFlag = false;
    else
    {
      digitalWrite(Led1, LOW);
      digitalWrite(Led2, LOW);
      digitalWrite(Led3, LOW);
    }
}

