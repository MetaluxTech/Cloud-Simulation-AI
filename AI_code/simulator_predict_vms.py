import pandas as pd
import numpy as np
from joblib import load
import os
from keras.models import load_model
import warnings

from sklearn.preprocessing import LabelEncoder


warnings.filterwarnings("ignore", category=DeprecationWarning, module="tensorflow")
warnings.resetwarnings()
parent_folder = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))





def predict_vms_forNewRequests(dataset_path,model_name, X_columns):
    df=df = pd.read_csv(dataset_path)  
    if (model_name!="ENSEMBLE"):
        snake_vms_scheduling_model = load_model(parent_folder + "\AI_code\models\snake_vms_scheduling_99.keras")
        features_array = df[X_columns].values  # Extract features as a NumPy array
        features_reshaped = features_array.reshape(-1, 1, features_array.shape[1])  # Reshape for prediction
        snake_prediction = snake_vms_scheduling_model.predict(features_reshaped)  # Predict in a single batch
        snake_predicted_ids = np.argmax(snake_prediction, axis=1)  # Get predicted IDs and adjust
        df['SNAKE_predicted_VM']=np.where(snake_predicted_ids == 0, 1, snake_predicted_ids)
        
    elif (model_name=="ENSEMBLE"):
        ensemble_vms_model = load(parent_folder + "\AI_code\models\ensemble_vms_scheduling_84.joblib")
        features_array = df[X_columns].values
        ensemble_predicted_ids = ensemble_vms_model.predict(features_array)
        df['ENSEMBLE_predicted_VM'] = np.where(ensemble_predicted_ids == 0, 1, ensemble_predicted_ids)
        
    else:
        print("model name not found")
        
    
    df.to_csv(dataset_path, index=False)
    return dataset_path  

def main():
    dataset_path = parent_folder + "/AI_code/dataset/new_requests_dataset.csv"
    X_columns=['TaskFileSize', 'TaskOutputFileSize', 'TaskFileLength', 'CpuTime', 'TotalLength']
    
    # Run for ENSEMBLE
    result_dataset_path_ensemble=predict_vms_forNewRequests(dataset_path=dataset_path,model_name='ENSEMBLE', X_columns=X_columns)
    print("result_dataset_path vms (ENSEMBLE) -->> " + result_dataset_path_ensemble)
    
    # Run for SNAKE-GA
    result_dataset_path_snake_ga = predict_vms_forNewRequests(dataset_path=dataset_path, model_name='SNAKE-GA', X_columns=X_columns)
    print("result_dataset_path vms (SNAKE-GA) -->> " + result_dataset_path_snake_ga)
if __name__ == "__main__":
    main()

