import io.flow.test.utils.FlowPlaySpec
import scala.concurrent.ExecutionContext

class CipherServiceSpec extends FlowPlaySpec {

  implicit private val ec: ExecutionContext = system.dispatcher

  val client = new PaymentInternalClient(new FakeCiphers())

  val cipherService = new CipherService(client)

  "create and get a cipher" in {
   val text = createTestId()
   val cipher = await(cipherService.create(text)).rightValue
   cipher.id must not be (text)
   val decryptedCipher = await(cipherService.get(cipher.id, cipher.password)).rightValue
   decryptedCipher.cleartext must be (text)
  }

}