


from Crypto.Cipher import AES
from Crypto.Util.Padding import unpad
from Crypto.Cipher import PKCS1_OAEP
from Crypto.PublicKey import RSA

private_key_path = "private_key.pem"
def decryptAESKey(encrypted_aes_key):
    rsa_private_key = RSA.import_key(open(private_key_path).read())
    cipher_rsa_dec = PKCS1_OAEP.new(rsa_private_key)
    decrypted_aes_key = cipher_rsa_dec.decrypt(encrypted_aes_key)
    return decrypted_aes_key

def decryptRawData(ciphertext, key):
    iv = ciphertext[:16]
    ciphertext = ciphertext[16:]
    cipher = AES.new(key, AES.MODE_CBC, iv)
    plaintext = unpad(cipher.decrypt(ciphertext), AES.block_size)
    return plaintext.decode('utf-8')



def generateTwoRawSemmetricKey():
    private_key_path = "private_key.pem"
    public_key_path = "public_key.pem"
    rsa_key = RSA.generate(2048)
    private_key = rsa_key.export_key()
    public_key = rsa_key.publickey().export_key()
    with open(private_key_path, 'wb') as file:
        file.write(private_key)
    with open(public_key_path, 'wb') as file:
        file.write(public_key)
    
    return private_key, public_key

