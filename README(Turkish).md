**Dil :** [English](https://github.com/BatuhanGunes/SmartHouse) / Turkish

# Akıllı Ev

Bu proje, akıllı ev sistemlerinin küçük bütçeli bir türü şeklinde tasarlanmıştır. Çeşitli sensörlerin eve yerleştirilmesinden sonra bu sensörlerden okunan değerleri bluetooht yardımıyla android işletim sistemine sahip cihaza aktarmasından oluşmaktadır. Aynı zamanda android cihazdan bu sistemi yönetebilmekte ve çeşitli ayarlamalar yapılabilmektedir. Bu sistem sayesinde ev içerisinde bulunurken rahatlıkla sistemi kontrol edebilmektesiniz. Sistem içerisinde mesafe sensörü ile garaj park sistemi, Hareket sensörü ile alarm sistemi, MQ-4 sensörü ile doğal gaz alarm sistemi, uzaktan kumanda edilebilir ışık sistemi ve DHT11 ile ısı nem ölçüm sistemi bulunmaktadır. Alarm sistemleri devrede iken bir durum oluştuğunda hem android cihazdan hemde sistemin kendisinden buzzer ile uyarı verilmektedir.

```
Projenin oluşturulma tarihi : Ekim 2018
```

## Ekran Görüntüleri

<img align="center" width="425" height="250" src="https://github.com/BatuhanGunes/SmartHouse/blob/master/Screenshots/1.jpeg"> <img align="center" width="425" height="250" src="https://github.com/BatuhanGunes/SmartHouse/blob/master/Screenshots/2.jpeg">

<img align="center" width="285" height="450" src="https://github.com/BatuhanGunes/SmartHouse/blob/master/Screenshots/Giris.jpg"> <img align="center" width="285" height="450" src="https://github.com/BatuhanGunes/SmartHouse/blob/master/Screenshots/BtControl.jpg"> <img align="center" width="285" height="450" src="https://github.com/BatuhanGunes/SmartHouse/blob/master/Screenshots/Main.jpg"> 

## Başlangıç

Projeyi çalıştırabilmek için proje dosyalarının bir kopyasını yerel makinenize indirin. Gerekli ortamları ve elektronik malzemeleri edinin. Edindiğiniz malzemeler ile gerekli devreyi kurun. Herhangi bir android cihaza yazılımı yükleyin. Yazılım üzerinden devre ile iletişimi sağlayın ve kullanmaya başlayın.

### Gereklilikler

- Devre için gerekli sensörler ve malzemeler
- Android yazılım sistemine sahip bir cihaz
- Android Studio
- Arduino IDE

Projeyi çalıştırabilmek için ilk olarak [Android Studio](https://developer.android.com/studio) adresinden sisteminize uygun IDE yazılımının herhangi bir sürümünü edinerek yerel makinenize kurun. Daha sonra yerel makinenize indirdiğiniz android için olan dosyaları bu ortamda açın. Gerekli ayarlamarı yaptıktan sonra uygulamanın apk uzantısını elde edin. Bu dosyayı android yazılıma sahip bir cihaza yükleyin.

Arduino için gerekli malzemeleri edinin ve devreyi oluşturun. Arduino için [Arduino IDE](https://www.arduino.cc/en/main/software) adresinden sisteminize uygun IDE yazılımının herhangi bir sürümünü edinerek yerel makinenize kurun. Arduino-pc bağlantısını gerçekleştirerek işlemciye arduino yazılımını arduino IDE ile yükleyin. Çalışır durumda olan devre ile uygulama arasında bluetooth bağlantısını oluşturun. Sistemi kullanmadan önce test etmeyi unutmayın. Bağlnatıların iyi yapıldığından emin olun

## Yazarlar

* **Batuhan Güneş**  - [BatuhanGunes](https://github.com/BatuhanGunes)

Ayrıca, bu projeye katılan ve katkıda bulunanlara [contributors](https://github.com/BatuhanGunes/SmartHouse/graphs/contributors) listesinden ulaşabilirsiniz.

## Lisans

Bu proje Apache lisansı altında lisanslanmıştır - ayrıntılar için [LICENSE.md](https://github.com/BatuhanGunes/SmartHouse/blob/master/LICENSE) dosyasına bakabilirsiniz.

