

from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Util.Padding import pad
from Crypto.Random import get_random_bytes
from Crypto.Cipher import PKCS1_OAEP
from Crypto.PublicKey import RSA
    
public_key_path = "public_key.pem"

def encryptAESKey(aes_key):
    rsa_public_key = RSA.import_key(open(public_key_path).read())
    cipher_rsa = PKCS1_OAEP.new(rsa_public_key)
    encrypted_aes_key = cipher_rsa.encrypt(aes_key)
    return encrypted_aes_key

def encryptRawData(plaintext, key):
    iv = get_random_bytes(16)
    cipher = AES.new(key, AES.MODE_CBC, iv)
    ciphertext = cipher.encrypt(pad(plaintext.encode('utf-8'), AES.block_size))
    return iv + ciphertext


def generateTwoSymmetricRSAKeys():
    private_key_path = "security/private_key.pem"
    public_key_path = "security/public_key.pem"
    rsa_key = RSA.generate(2048)
    private_key = rsa_key.export_key()
    public_key = rsa_key.publickey().export_key()
    with open(private_key_path, 'wb') as file:
        file.write(private_key)
    with open(public_key_path, 'wb') as file:
        file.write(public_key)

