import time
from Crypto.Cipher import AES
from Crypto.Util.Padding import unpad, pad
from Crypto.Random import get_random_bytes
from matplotlib import pyplot as plt

dummyTaskData = "1,0.1,78,14,66,17.773670230939274,-31.08917375907896,74.17352985627201,3.8333252049254725,6622.0,48.0,15.0,26.0,27.0,3362.0,1.11,36.4,2374.92,3,1556022080,192.168.1.30,49500,192.168.1.49,19315,tcp,-,0.0,0,0,S0,0,1,44,0,0,-,0,0,0,-,-,-,-,-,-,-,-,-,-,-,-,-,-,0,0,0,-,-,-,-,-,-,1,scanning"
tasklength=dummyTaskData.split(',')[2]
aes_key = get_random_bytes(32)

def encrypt_data_with_time(plaintext, key):
    start_time = time.time()  # Start timing for encryption
    iv = get_random_bytes(16)
    cipher = AES.new(key, AES.MODE_CBC, iv)
    ciphertext = cipher.encrypt(pad(plaintext.encode('utf-8'), AES.block_size))
    end_time = time.time()  # End timing for encryption
    return iv + ciphertext, end_time - start_time  # Return ciphertext and encryption time 2.985 ms

def decrypt_data_with_time(ciphertext, key):
    start_time = time.time()  # Start timing for decryption
    iv = ciphertext[:16]
    ciphertext = ciphertext[16:]
    cipher = AES.new(key, AES.MODE_CBC, iv)
    plaintext = unpad(cipher.decrypt(ciphertext), AES.block_size)
    end_time = time.time()  # End timing for decryption
    return plaintext.decode('utf-8'), end_time - start_time  # Return plaintext and decryption time 0.985 ms



encrypted_data, encryption_time = encrypt_data_with_time(dummyTaskData, aes_key)
decrypted_data, decryption_time = decrypt_data_with_time(encrypted_data, aes_key)

print("Encryption time:", encryption_time*1000, " ms",int(tasklength)/15)  #  3 ms
print("Decryption time:", decryption_time*1000, " ms",int(tasklength)/15)  #  1 ms
print()