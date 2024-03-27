import os
import logging
import warnings

os.environ['TF_ENABLE_ONEDNN_OPTS'] = '0'
warnings.filterwarnings("ignore", category=DeprecationWarning)  
logging.getLogger('tensorflow').setLevel(logging.ERROR)  # Only log errors


import numpy as np
import sys
from keras.models import load_model

def predict_datacenter_id(args):

    if args==None:
        print("NONE")
        return
    path=args[0]
    model_name=args[1]
    task_size=float(args[2])
    task_output_size=float(args[3])
    task_length=float(args[4])
    task_latitude=float(args[5])
    task_longitude=float(args[6])
    model =load_model(f"C:\\Users\\mohsal\\Desktop\\app\\metalux\\cloudsim\\ga_lstm\\AI_code\\models\\{model_name}")

    task_info=[task_size,task_output_size,task_length,task_latitude,task_longitude]
    task_info_reshaped=np.array([[task_info]]).reshape(1, 1, -1)
    datacenter_id = np.argmax(model.predict(task_info_reshaped, verbose=0))+3
    print(datacenter_id)

    return datacenter_id

if __name__ == "__main__":
    if len(sys.argv) >= 2:
        predict_datacenter_id(sys.argv)  # Use command-line arguments
    else:
        predict_datacenter_id(None)
    sys.exit(1)
