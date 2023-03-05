package com.knoldus.client

import com.knoldus.models._
import org.apache.commons.lang3.RandomStringUtils

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

object Factories {
  def makeCipher(): CipherModel = CipherModel(
    id = Factories.randomString(24),
    attributes = Map()
  )

  def randomString(length: Int = 24): String =
    _root_.scala.util.Random.alphanumeric.take(length).mkString
}

class FakeCiphers() {
  val ciphers: mutable.Map[String, DecryptedCipherWithPassword] = scala.collection.mutable.Map[String, DecryptedCipherWithPassword]()

  def add(cipher: CipherModel, decryptedWithPassword: DecryptedCipherWithPassword): Unit = {
    ciphers += (cipher.id -> decryptedWithPassword)
    ()
  }

  def get(cipherId: String): DecryptedCipherWithPassword =
    ciphers(cipherId)
}

class InternalClient(fakeCiphers: FakeCiphers) {
  def post(
      cipherForm: CipherForm
  )(implicit ec: ExecutionContext): Future[CipherModel] = {
    val cipher = Factories.makeCipher().copy(id = RandomStringUtils.randomAlphanumeric(64))
    val decryptedWithPassword = DecryptedCipherWithPassword(
      DecryptedCipher(cipher.id, cipherForm.text, Map.empty),
      cipherForm.password
    )
    fakeCiphers.add(cipher, decryptedWithPassword)
    Future.successful(cipher)
  }

  def postGet(
      decryptCipherForm: DecryptCipherForm
  )(implicit ec: ExecutionContext): Future[DecryptedCipher] = {
    val decryptedWithPassword = fakeCiphers.get(decryptCipherForm.id)
    Future.successful(decryptedWithPassword.decryptedCipher)
  }
}
