### Cipher encrypt library

You can use this library to encrypt and decrypt messages.

### Usage:

##### Creating com.knoldus.cipher.CipherService object 
```
    val client = new com.knoldus.client.InternalClient(new com.knoldus.client.FakeCiphers())
    val cipher = new com.knoldus.cipher.CipherService(client)
```
##### Use method .create to encrypt text message. Type String
```
    val encryptedCipher = cipher.create("Test string for encrypt", Map.empty)
```
##### This method will return model com.knoldus.models.EncryptedCipher
```
    case class com.knoldus.models.EncryptedCipher(
        id: String,
        password: String
    )
```    
##### Use method .get to decrypt. Type com.knoldus.models.EncryptedCipher
```
    decryptedCipher = cipher.get(encryptedCipher)
```
##### This method will return model com.knoldus.models.EncryptedCipher
```
    case class com.knoldus.models.DecryptedCipher(
        id: String,
        cleartext: String,
        attributes: Map[String, String]
    )
```

### Example :
```
id - icc3AINJrw1GsGyDUioMwLcOEQriZZnMkw1S9DQI2BgxQmyTjuV9SRSm0HZgrJso : password - DaIptksNZuIWNMPsmAMMnxjIWYzfASR8rtD69B6nbtjziHATF5Qy7RRhc3IhP2R8
id - icc3AINJrw1GsGyDUioMwLcOEQriZZnMkw1S9DQI2BgxQmyTjuV9SRSm0HZgrJso : cleartext - Test string for encrypt
```