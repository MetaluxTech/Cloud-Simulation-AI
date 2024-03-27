import time
tmr_beforstartapp=time.time()


import numpy as np
import sys
from keras.models import load_model


tmr_afterstartapp=time.time()

def predict_datacenter_id(args):
    tmr_enterfunction=time.time()
    if args==None:
        print("NONE")
        return
    
    path=args[0]
    model_name=args[1]
    task_size=args[2]
    task_output_size=args[3]
    task_length=args[4]
    task_latitude=args[5]
    task_longitude=args[6]
    print("args: ",args)
    tmr_beforloadmodel=time.time()
    model =load_model("models/ga_model_86.keras")
    tmr_afterloadmodel=time.time()
    
    task_info=np.array([[task_info]]).reshape(1, 1, -1)
    tmr_beforpredictusingmodel=time.time()
    datacenter_id = np.argmax(model.predict(task_info))
    tmr_afterpredictionusingmodel=time.time()
    print(f"Diff  after app start: ",tmr_afterstartapp-tmr_beforstartapp)
    print(f"Diff  load model: ",tmr_afterloadmodel-tmr_beforloadmodel)
    print(f"Diff  predict model: ",tmr_afterpredictionusingmodel-tmr_beforpredictusingmodel)
    
    print("datacenterID: ",datacenter_id)
    tmr_finishthefunction=time.time()

    return datacenter_id+3

if __name__ == "__main__":
    tmr_beformainstart=time.time()
    print(dict)
    if len(sys.argv) >= 2:
        predict_datacenter_id(sys.argv)  # Use command-line arguments
    else:
        predict_datacenter_id(None)
    tmr_aftermainfinished=time.time()
    print(f"Diff  total time: ",tmr_aftermainfinished-tmr_beforstartapp)

    sys.exit(1)
