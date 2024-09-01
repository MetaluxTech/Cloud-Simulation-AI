import os
import logging
import warnings

os.environ['TF_ENABLE_ONEDNN_OPTS'] = '0'
warnings.filterwarnings("ignore", category=DeprecationWarning)  
logging.getLogger('tensorflow').setLevel(logging.ERROR)  # Only log errors
parent_folder = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

import numpy as np
import sys
from keras.models import load_model
from joblib import load

def predict_datacenter_id(args):
    dc_id = -99
    if args==None:
        print("NONE")
        return   
    model_name=args[1]
    path = args[0]    
    task_size=float(args[2])
    task_output_size=float(args[3])
    task_length=float(args[4])
    cpu_time=float(args[5])
    total_length=float(args[6])
    task_latitude=float(args[7])
    task_longitude=float(args[8])
    features_array=[task_size,task_output_size,task_length,cpu_time,total_length,task_latitude,task_longitude]    
    features_reshaped=np.array([[features_array]]).reshape(1, 1, -1)

    if "ensemble" in model_name:
        ensemble_model=load(os.path.join(parent_folder, "AI_code", "models", model_name))
        new_array=np.array([features_array])
        datacenter_id = (ensemble_model.predict(new_array)+3)[0]
        print(datacenter_id)
        return datacenter_id
    

    model = load_model(os.path.join(parent_folder, "AI_code", "models", model_name))    
    datacenter_id = np.argmax(model.predict(features_reshaped, verbose=0))+3
    print(datacenter_id)
    return datacenter_id

if __name__ == "__main__":
    if len(sys.argv) >= 2:
        try:
            predict_datacenter_id(sys.argv)  # Use command-line arguments
        except Exception as e:
            print(f" (( error in Dcs python code code )) (({sys.argv})) :-> ",str(e))
    else:
        args=["D:\projects\Cloud-Simulation-AI\AI_code\simulation_predict.py","ensemble_model_84.joblib", 73, 20, 24, 1.8461538461538463, 24, 54.18336812, 34.69151705]
        predict_datacenter_id(args)
    sys.exit(1)


