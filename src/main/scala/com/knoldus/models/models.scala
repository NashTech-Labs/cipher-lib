package com.knoldus.models

case class CipherModel(
    id: String,
    attributes: Map[String, String]
)

case class DecryptCipherForm(
    id: String,
    password: String
)

case class EncryptedCipher(
    id: String,
    password: String
)

case class DecryptedCipher(
    id: String,
    cleartext: String,
    attributes: Map[String, String]
)

case class CipherForm(
    text: String,
    attributes: _root_.scala.Option[Map[String, String]] = None,
    password: _root_.scala.Option[String] = None
)

case class DecryptedCipherWithPassword(
    decryptedCipher: DecryptedCipher,
    password: Option[String]
)
