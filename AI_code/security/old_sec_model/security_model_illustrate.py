from Crypto.Random import get_random_bytes

from secure_server_side import decryptRawData, decryptAESKey
from secure_user_side import encryptRawData, encryptAESKey, generateTwoSymmetricRSAKeys

print("--------------------------------------------- user side --------------------------------------------------")
generateTwoSymmetricRSAKeys()
dummyTaskData = "1,0.1,78,14,66,17.773670230939274,-31.08917375907896,74.17352985627201,3.8333252049254725,6622.0,48.0,15.0,26.0,27.0,3362.0,1.11,36.4,2374.92,3,1556022080,192.168.1.30,49500,192.168.1.49,19315,tcp,-,0.0,0,0,S0,0,1,44,0,0,-,0,0,0,-,-,-,-,-,-,-,-,-,-,-,-,-,-,0,0,0,-,-,-,-,-,-,1,scanning"
aes_key = get_random_bytes(32)
encrypted_aes_key = encryptAESKey(aes_key)
print("encrypt AES-KEY with RSA Algprithm Done")
encrypted_data = encryptRawData(dummyTaskData, aes_key)
print("encrypt data with RSA-AES Algorithm Done")


print("--------------------------------------------- server side --------------------------------------------------")

decryptAESKey = decryptAESKey(encrypted_aes_key)
print("decrypt AES-KEY from RSA Algprithm Done")
decrypted_data = decryptRawData(encrypted_data, decryptAESKey)
print("decrypt data from RSA-AES Algorithm Done")


print("--------------------------------------------- evaluation Arrived Data part --------------------------------------------------")

key_match = aes_key == decryptAESKey
print("the decrypted AES key matches the original key")  if key_match else "the decrypted AES key does not matche the original key"

data_match = dummyTaskData == decrypted_data
print("Data Match: ", data_match)

print("--------------------------------------------- the PROCESS END SUCCESSFULLY --------------------------------------------------")
