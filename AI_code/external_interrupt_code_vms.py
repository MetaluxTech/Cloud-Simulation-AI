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

def predict_vm_id(args):
    vm_id = -99
    if args==None:
        print("NO  External Argumnts Provided for The Function ")
        return   

    # [ D:\projects\Cloud-Simulation-AI\AI_code\external_interrupt_code_dcs.py, snake_vms_scheduling_99.keras, 18, 11, 44, -1.0, 44]
    venv_python_path=args[0]
    model_name='snake_vms_scheduling_99.keras'
    task_size=float(args[2])
    task_output_size=float(args[3])
    task_length=float(args[4])
    cpu_time=float(args[5])
    total_length=float(args[6])
    
    features_array=[task_size,task_output_size,task_length,cpu_time,total_length]
    features_reshaped=np.array([[features_array]]).reshape(1, 1, -1)
    if "ensemble" in model_name:
        ensemble_model=load(os.path.join(parent_folder, "AI_code", "models", model_name))
        new_array=np.array([features_array])

        vm_id = (ensemble_model.predict(new_array)+3)[0]
        print(vm_id)
        return vm_id
    
    model = load_model(os.path.join(parent_folder, "AI_code", "models", model_name)) 

    vm_id = np.argmax(model.predict(features_reshaped, verbose=0))+3
    print(vm_id)
    return vm_id

if __name__ == "__main__":
    if len(sys.argv) >= 2:
        try:
            predict_vm_id(sys.argv)  # Use command-line arguments
        except Exception as e:
            print(f" (( error in Vms python code ))  args (({sys.argv})) :-> ",str(e))
    else:
        args=['D:\projects\Cloud-Simulation-AI\AI_code\external_interrupt_code_vms.py', 'ensemble_vms_scheduling_84.joblib', 13, 17, 80, -1.0, 80]
        predict_vm_id(args)
    sys.exit(1)
