import pandas as pd
import numpy as np
import tensorflow as tf
from tensorflow.keras.models import load_model

def predict_datacenter_id(task_info, model_name="models/lstm87.keras"):
  
    expected_feature_count = 4

    if len(task_info) != expected_feature_count:
        raise ValueError(f"Expected {expected_feature_count} features, but received {len(task_info)}.")

    # Build the data dictionary and reshape the input data
    data_dict = {
        "TaskID": task_info[0],
        "TaskFileSize": task_info[1],
        "TaskOutputFileSize": task_info[2],
        "TaskFileLength": task_info[3],
    }
    input_data = np.array([[data_dict["TaskID"], data_dict["TaskFileSize"], data_dict["TaskOutputFileSize"], data_dict["TaskFileLength"]]])
    input_data_lstm = input_data.reshape(input_data.shape[0], 1, input_data.shape[1])
    
    
    loaded_model = load_model(model_name)
    # Make predictions using the loaded model
    predicted_probabilities = loaded_model.predict(input_data_lstm)
    predicted_class = np.argmax(predicted_probabilities, axis=1)
    predicted_datacenter_id = int(predicted_class[0])
    return predicted_datacenter_id
import pandas as pd

def GetDatacenterCharacterestic(taskInfo,datacenter_id):
    # Assuming your CSV file is in the same directory as your script

    try:
        df = pd.read_csv('dataset/resourcesxxx.csv')
        row = df[df['DataCenterID'] == datacenter_id].iloc[0]
        
        datacenter_characteristic = {
            'DataCenterCpuCost': float(row['DataCenterCpuCost']),
            'DataCenterRamCost': float(row['DataCenterRamCost']),
            'DataCenterStorageCost': float(row['DataCenterStorageCost']),
            'DataCenterBwCost': float(row['DataCenterBwCost']),
            'DataCenterTotalLoad': float(row['DataCenterTotalLoad']),
            'NetworkDelay': float(row['NetworkDelay'])
        }
        
        return datacenter_characteristic

    except Exception as e:
        raise Exception(f"Error reading CSV file: {str(e)}")

    # If the given datacenter_id is not found in the CSV file
    raise Exception(f"DataCenterID {datacenter_id} not found in the dataset")
