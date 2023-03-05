import akka.actor.ActorSystem
import com.knoldus.cipher.CipherService
import com.knoldus.client.{FakeCiphers, InternalClient}
import com.knoldus.models.EncryptedCipher

import scala.concurrent.{ExecutionContext, Future}

object CipherExamples extends App {

  implicit lazy val system: ActorSystem = ActorSystem()
  implicit private val ec: ExecutionContext = system.dispatcher

  val client = new InternalClient(new FakeCiphers())

  val cipher = new CipherService(client)

  val encryptedCipher: Future[Either[String, EncryptedCipher]] = cipher.create("Test string for encrypt", Map.empty)

  encryptedCipher.map(x =>
    x.map { y =>
      println(s"id - ${y.id} : password - ${y.password}")
      val decryptedCipher = cipher.get(y)
      decryptedCipher.map(x => x.map(y => println(s"id - ${y.id} : cleartext - ${y.cleartext} ")))
    }
  )
}
