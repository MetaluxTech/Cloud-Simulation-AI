import pandas as pd
import matplotlib.pyplot as plt


# #EDA
print("--------------------------------------------- import data start --------------------------------------------------")
df = pd.read_csv("security/TON_IoT.csv")
unique_values = df['type'].unique()
plt.scatter(df.index.values,df.src_port,)

plt.show(block=True)
print("--------------------------------------------- import libraries start --------------------------------------------------")

from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from base64 import b64encode
from Crypto.Util.Padding import pad, unpad
from Crypto.Random import get_random_bytes
from Crypto.Cipher import PKCS1_OAEP
from Crypto.PublicKey import RSA
from base64 import b64decode

print("--------------------------------------------- start generate aes-key Start --------------------------------------------------")

aes_key = get_random_bytes(32)  # AES-256
base64_encoded_aes_key = b64encode(aes_key).decode('utf-8')

print("--------------------------------------------- start encrypt dataset with the aes-key --------------------------------------------------")

input_file_path = "security/TON_IoT.csv"
encrypted_file_path = "security/encrypted_TON_IoT.csv"
decrypted_file_path = "security/decrypted_TON_IoT.csv"
def encrypt_file(input_file_path, output_file_path, key):
    iv = get_random_bytes(16)
    cipher = AES.new(key, AES.MODE_CBC, iv)
    with open(input_file_path, 'rb') as file:
        plaintext = file.read()
        ciphertext = cipher.encrypt(pad(plaintext, AES.block_size))
    with open(output_file_path, 'wb') as file:
        file.write(iv + ciphertext)
encrypt_file(input_file_path, encrypted_file_path, aes_key)




print("--------------------------------------------- start generation RSA-KEY --------------------------------------------------")

rsa_key = RSA.generate(2048)
private_key = rsa_key.export_key()
public_key = rsa_key.publickey().export_key()

print("--------------------------------------------- start saving rsa keys  --------------------------------------------------")

private_key_path = "private_key.pem"
public_key_path = "public_key.pem"
with open(private_key_path, 'wb') as file:
    file.write(private_key)
with open(public_key_path, 'wb') as file:
    file.write(public_key)


print("--------------------------------------------- start encrypt AES-KEY with RSA-Algorithm --------------------------------------------------")

rsa_public_key = RSA.import_key(open(public_key_path).read())
cipher_rsa = PKCS1_OAEP.new(rsa_public_key)
encrypted_aes_key = cipher_rsa.encrypt(aes_key)
base64_encoded_encrypted_aes_key = b64encode(encrypted_aes_key).decode('utf-8')



print("--------------------------------------------- start decrypt AES-KEY with RSA-Algorithm --------------------------------------------------")

rsa_private_key = RSA.import_key(open(private_key_path).read())
cipher_rsa_dec = PKCS1_OAEP.new(rsa_private_key)
decrypted_aes_key = cipher_rsa_dec.decrypt(encrypted_aes_key)
base64_encoded_decrypted_aes_key = b64encode(decrypted_aes_key).decode('utf-8')
base64_encoded_decrypted_aes_key

print("--------------------------------------------- start decrypt the data with AES-KEY --------------------------------------------------")

def decrypt_file(input_file_path, output_file_path, key):
    with open(input_file_path, 'rb') as file:
        iv = file.read(16)
        ciphertext = file.read()
        
    cipher = AES.new(key, AES.MODE_CBC, iv)

    plaintext = unpad(cipher.decrypt(ciphertext), AES.block_size)
    with open(output_file_path, 'wb') as file:
        file.write(plaintext)

decrypt_file(encrypted_file_path, decrypted_file_path, decrypted_aes_key)

print("--------------------------------------------- start matching the AES-original-key with AES-decrypted-key --------------------------------------------------")


key_match = decrypted_aes_key == aes_key
print('the decrypted AES key matches the original key') if key_match else 'the decrypted AES key does not matche the original key'



print("--------------------------------------------- start compare both datasets  --------------------------------------------------")

# Load the original and decrypted CSV files
original_df = pd.read_csv(input_file_path)
decrypted_df = pd.read_csv(decrypted_file_path)

# Get the shapes of both dataframes
original_shape = original_df.shape
decrypted_shape = decrypted_df.shape

print('shapes:', original_shape, decrypted_shape)

# Compare rows of both dataframes
matching_rows = (original_df == decrypted_df).all(axis=1).sum()

# Calculate the percentage of matching rows
percentage_matching = (matching_rows / len(original_df)) * 100

print('matching rows:')
print(matching_rows , '/', len(original_df))
print(percentage_matching, '% match')

print("--------------------------------------------- the PROCESS END SUCCESSFULLY --------------------------------------------------")



