import io.flow.util.Random

import scala.concurrent.{ExecutionContext, Future}

trait CipherServiceT {
  def create(
    cleartext: String,
    attributes: Map[String, String] = Map.empty
  )(implicit ec: ExecutionContext): Future[Either[String, EncryptedCipher]]

  def get(
    id: String,
    password: String
  )(implicit ec: ExecutionContext): Future[Either[String, DecryptedCipher]] = get(EncryptedCipher(id, password))

  def get(cipher: EncryptedCipher)(implicit ec: ExecutionContext): Future[Either[String, DecryptedCipher]]
}

class CipherService(
  client: PaymentInternalClient
) extends CipherServiceT {

  private[this] val random = Random()

  private[this] def generatePassword: String = random.alphaNumeric(64)

  override def create(cleartext: String, attributes: Map[String, String])(implicit ec: ExecutionContext): Future[Either[String, EncryptedCipher]] = {
    val password = generatePassword
    val form = CipherForm(cleartext, Some(attributes), Some(password))

    client.post(
      cipherForm = form
    ).map { cipher =>
      Right(EncryptedCipher(cipher.id, password))
    }.recover {
      case ex: Throwable => {
        Left(s"Error encrypting cipher: ${ex.getMessage}")
      }
    }
  }

  override def get(cipher: EncryptedCipher)(implicit ec: ExecutionContext): Future[Either[String, DecryptedCipher]] = {
    val form = DecryptCipherForm(id = cipher.id, password = cipher.password)
    client.postGet(
      decryptCipherForm = form
    ).map { decryptedCipher =>
      Right(
        DecryptedCipher(
          id = decryptedCipher.id,
          cleartext = decryptedCipher.cleartext,
          attributes = decryptedCipher.attributes
        )
      )
    }.recover {
      case ex: Throwable => {
        Left(s"Error decrypting cipher: ${ex.getMessage}")
      }
    }
  }
}
