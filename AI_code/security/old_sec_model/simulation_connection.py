import base64
import sys
from Crypto.Cipher import PKCS1_OAEP
from Crypto.PublicKey import RSA


from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Util.Padding import pad, unpad
from Crypto.Random import get_random_bytes
from Crypto.Cipher import PKCS1_OAEP
from Crypto.PublicKey import RSA

public_key_path = "C:/Users/mohsal/Desktop/app/metalux/cloudsim/ga_lstm/AI_code/security/public_key.pem"
private_key_path = "C:/Users/mohsal/Desktop/app/metalux/cloudsim/ga_lstm/AI_code/security/private_key.pem"

def encryptData(original_data,key):
    try:
        iv = get_random_bytes(16)
        cipher = AES.new(key, AES.MODE_CBC, iv)
        ciphertext = cipher.encrypt(pad(original_data.encode('utf-8'), AES.block_size))
        return iv + ciphertext

    except Exception as e:
        print("Error in encryption data:", e)
        return None


def encryptAESKey(aes_key)->bytes:
    try:
        rsa_public_key = RSA.import_key(open(public_key_path).read())
        cipher_rsa = PKCS1_OAEP.new(rsa_public_key)
        encrypted_aes_key = cipher_rsa.encrypt(aes_key)
        return encrypted_aes_key
    except Exception as e:
        print("Error in encryption key:", e)
        return None
   
def decryptAESKey(encrypted_aes_key):
    try:
        rsa_private_key = RSA.import_key(open(private_key_path).read())
        cipher_rsa_dec = PKCS1_OAEP.new(rsa_private_key)
        print()
        decrypted_aes_key = cipher_rsa_dec.decrypt(encrypted_aes_key)
        return decrypted_aes_key
    except Exception as e:
        print("Error in decryption aes-key:", e)
        return None


def decryptData(ciphertext, key):
    try:
        iv = ciphertext[:16]
        ciphertext = ciphertext[16:]
        cipher = AES.new(key, AES.MODE_CBC, iv)
        decrepted_data = unpad(cipher.decrypt(ciphertext), AES.block_size)
        return decrepted_data.decode('utf-8')
    except Exception as e:

        print("Error in decryption data:", e)
        return None


if __name__ == '__main__':
       
        if len(sys.argv) >= 2:
            method = sys.argv[1]    
            if method=="Encrypt-AES":
                key=(sys.argv[3]).encode()
                encrypted_aes_key=encryptAESKey(aes_key=key)         
                encrypted_aes_key_base64 = base64.b64encode(encrypted_aes_key).decode('utf-8')  # Decode to string
                print(encrypted_aes_key_base64)
              
            elif method == "Encrypt-Task":
                task_data = sys.argv[2]
                key=(sys.argv[3]).encode()
                encrypted_data=encryptData(original_data=task_data,key=key)        
                encrypted_data_base64 = base64.b64encode(encrypted_data).decode('utf-8')  # Decode to string
                print(encrypted_data_base64)
            
            elif method == "Decrypt-AES":
                encrypted_aes_key=(sys.argv[2]).encode()
                aes_basebytes=base64.b64decode(encrypted_aes_key)
                decrypted_key=decryptAESKey(encrypted_aes_key=aes_basebytes)
                
                print(decrypted_key) 
            elif method== "Decrypt-Task":
                try:
                    cipher_data=sys.argv[2]
                    key=(sys.argv[3]).encode()
                    cipherdata_basebytes=base64.b64decode(cipher_data)
                    decrypted_data=decryptData(ciphertext=cipherdata_basebytes,key=key) 
                    print(decrypted_data)
                except Exception as e:
                    print(f"exxexpt: {e}")
                
            
        