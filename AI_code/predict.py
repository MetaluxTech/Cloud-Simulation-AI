import pandas as pd
import numpy as np
import tensorflow as tf
from keras.models import load_model

df_path                 ="AI_code/dataset/distance_dataset.csv"
predicted_df_path       =("AI_code/dataset/predictedDataBase.csv")
ga_model           =load_model("AI_code/models/ga_model_86.keras")
snake_model        =load_model("AI_code/models/snake_model_91.keras")




df = pd.read_csv(df_path)  

def predict_datacenter_id(task_info,modelName):
    features = np.array([[task_info]]).reshape(1, 1, -1)
    return np.argmax(modelName.predict(features))+3

def SavePredictedDataBase(df, features_cols, ):
    """Predicts datacenter IDs using Array operations."""
    features_array = df[features_cols].values  # Extract features as a NumPy array
    features_reshaped = features_array.reshape(-1, 1, features_array.shape[1])  # Reshape for prediction
    
    ga_predictions = ga_model.predict(features_reshaped)  # Predict in a single batch
    snake_prediction = snake_model.predict(features_reshaped)  # Predict in a single batch
    
    ga_predicted_ids = np.argmax(ga_predictions, axis=1) + 3  # Get predicted IDs and adjust
    snake_predicted_ids = np.argmax(snake_prediction, axis=1) + 3  # Get predicted IDs and adjust
    
    df['GA_predicted_DC'] = ga_predicted_ids  # Add predictions to the DataFrame
    df['SNAKE_predicted_DC'] = snake_predicted_ids  # Add predictions to the DataFrame
    df.to_csv(predicted_df_path, index=False)
    return predicted_df_path

newdataset=SavePredictedDataBase(df, features_cols=['TaskFileSize', 'TaskOutputFileSize', 'TaskFileLength', 'UserLatitude', 'UserLongitude'])
print(newdataset)
