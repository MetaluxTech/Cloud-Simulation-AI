import pandas as pd
import numpy as np
import tensorflow as tf
from keras.models import load_model

model = load_model("C:/Users/mohsal/Desktop/app/metalux/cloudsim/ga_lstm/AI_code/models/ga_snake88.keras")

def predict_datacenter_id(task_info,modelName=model):
    features = np.array([[task_info]]).reshape(1, 1, -1)
    return np.argmax(modelName.predict(features))+3

