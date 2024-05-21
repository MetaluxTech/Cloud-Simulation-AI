import os
import logging
import warnings

os.environ['TF_ENABLE_ONEDNN_OPTS'] = '0'
warnings.filterwarnings("ignore", category=DeprecationWarning)  
logging.getLogger('tensorflow').setLevel(logging.ERROR)  # Only log errors


import numpy as np
import sys
from keras.models import load_model
from joblib import load

def predict_datacenter_id(args):
    if args==None:
        print("NONE")
        return   
    model_name=args[1]
    model =load_model(f"C:\\Users\\mohsal\\Desktop\\app\\metalux\\cloudsim\\ga_lstm\\AI_code\\models\\{model_name}")
    path=args[0]
    task_size=float(args[2])
    task_output_size=float(args[3])
    task_length=float(args[4])
    cpu_time=float(args[5])
    total_length=float(args[6])
    task_latitude=float(args[7])
    task_longitude=float(args[8])
    
    if "ensemble" in model_name:
        ensemble_model=load(f"C:\\Users\\mohsal\\Desktop\\app\\metalux\\cloudsim\\ga_lstm\\AI_code\\models\\{model_name}")
        new_array=np.array([features_array])
        data_center_id = ensemble_model.predict(new_array)+3
        print(data_center_id)
        return datacenter_id
    
    features_array=[task_size,task_output_size,task_length,cpu_time,total_length,task_latitude,task_longitude]
    features_reshaped=np.array([[features_array]]).reshape(1, 1, -1)
    datacenter_id = np.argmax(model.predict(features_reshaped, verbose=0))+3
    print(datacenter_id)
    return datacenter_id

if __name__ == "__main__":
    if len(sys.argv) >= 2:
        try:
            predict_datacenter_id(sys.argv)  # Use command-line arguments
        except Exception as e:
            print("error :-> ",str(e))
    else:
        predict_datacenter_id(None)
    sys.exit(1)
