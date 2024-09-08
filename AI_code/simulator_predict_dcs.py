import pandas as pd
import numpy as np
from joblib import load
import os
from keras.models import load_model
import warnings

from sklearn.preprocessing import LabelEncoder

parent_folder = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))




def predict_dc_forNewRequest(dataset_path,model_name, X_columns ):
    df = pd.read_csv(dataset_path)  
    """Predicts datacenter IDs using Array operations."""
    if (model_name != "ENSEMBLE"):
        ga_model = load_model(parent_folder + "/AI_code/models/ga_model_82.keras")
        snake_model = load_model(parent_folder + "/AI_code/models/snake_model_95.keras")

        features_array = df[X_columns].values  # Extract features as a NumPy array
        features_reshaped = features_array.reshape(-1, 1, features_array.shape[1])  # Reshape for prediction
        
        ga_predictions = ga_model.predict(features_reshaped)  # Predict in a single batch
        snake_prediction = snake_model.predict(features_reshaped)  # Predict in a single batch
        
        ga_predicted_ids = np.argmax(ga_predictions, axis=1) + 3  # Get predicted IDs and adjust
        snake_predicted_ids = np.argmax(snake_prediction, axis=1) + 3  # Get predicted IDs and adjust

        df['GA_predicted_DC'] = ga_predicted_ids  # Add predictions to the DataFrame
        df['SNAKE_predicted_DC'] = snake_predicted_ids  # Add predictions to the DataFrame
    elif model_name == "ENSEMBLE":
            ensemble_model = load(parent_folder + "/AI_code/models/ensemble_model_84.joblib")
            features_array = df[X_columns].values  # Extract features as a NumPy array
            ensemble_predicted_ids = ensemble_model.predict(features_array)+3
            df['ENSEMBLE_predicted_DC'] = ensemble_predicted_ids  # Add predictions to the DataFrame
            
    df.to_csv(dataset_path, index=False)
    return dataset_path

def main():
    dataset_path = parent_folder + "/AI_code/dataset/new_requests_dataset.csv"
    X_columns=['TaskFileSize', 'TaskOutputFileSize', 'TaskFileLength', 'CpuTime', 'TotalLength', 'UserLatitude', 'UserLongitude']
    
    # Run for SNAKE-GA
    result_dataset_path_snake_ga = predict_dc_forNewRequest(dataset_path=dataset_path, model_name='SNAKE-GA',X_columns=X_columns )
    print("result_dataset_path dcs (SNAKE-GA) -->> " + result_dataset_path_snake_ga)
   
    # Run for ENSEMBLE
    result_dataset_path_ensemble = predict_dc_forNewRequest(dataset_path=dataset_path, model_name='ENSEMBLE', X_columns=X_columns)
    print("result_dataset_path dcs (ENSEMBLE) -->> " + result_dataset_path_ensemble)
    
 
if __name__ == "__main__":
    main()
