import pandas as pd
import numpy as np
from joblib import load

from keras.models import load_model
import warnings

from sklearn.preprocessing import LabelEncoder


warnings.filterwarnings("ignore", category=DeprecationWarning, module="tensorflow")
warnings.resetwarnings()
df_path                                 ="dataset/global_dataset.csv"


ga_model           =load_model("models/ga_model_82.keras")
snake_model        =load_model("models/snake_model_95.keras")
new_model        =load_model("models/model2.keras")
snake_vms_scheduling_model=load_model("models/snake_vms_scheduling_99.keras")
ensemble_model    =load("models/ensemble_model_84.joblib")
ensemble_vms_model    =load("models/enesemble_vms_scheduling_84.joblib")

df = pd.read_csv(df_path)  

def predict_datacenter_id(task_info,modelName):
    features = np.array([[task_info]]).reshape(1, 1, -1)
    return np.argmax(modelName.predict(features))+3

def predict_VM_id(task_info,modelName):
    features = np.array([[task_info]]).reshape(1, 1, -1)
    return np.argmax(modelName.predict(features))

def SavePredictedDataBase(model_name,df, features_cols ):
    """Predicts datacenter IDs using Array operations."""
    if (model_name != "ENSEMBLE"):
        features_array = df[features_cols].values  # Extract features as a NumPy array
        features_reshaped = features_array.reshape(-1, 1, features_array.shape[1])  # Reshape for prediction
        
        ga_predictions = ga_model.predict(features_reshaped)  # Predict in a single batch
        snake_prediction = snake_model.predict(features_reshaped)  # Predict in a single batch
        
        ga_predicted_ids = np.argmax(ga_predictions, axis=1) + 3  # Get predicted IDs and adjust
        snake_predicted_ids = np.argmax(snake_prediction, axis=1) + 3  # Get predicted IDs and adjust

        df['GA_predicted_DC'] = ga_predicted_ids  # Add predictions to the DataFrame
        df['SNAKE_predicted_DC'] = snake_predicted_ids  # Add predictions to the DataFrame
    elif model_name == "ENSEMBLE":
            features_array = df[features_cols].values  # Extract features as a NumPy array
            ensemble_predicted_ids = ensemble_model.predict(features_array)+3
            df['ENSEMBLE_predicted_DC'] = ensemble_predicted_ids  # Add predictions to the DataFrame
            
    df.to_csv(df_path, index=False)
    return df_path


def savePredictedVMsScedulingDataBase(df,model_name, features_cols):

    y = df['VmID']
    label_encoder = LabelEncoder()
    y = label_encoder.fit_transform(y)
    if (model_name=="SNAKE"):
        features_array = df[features_cols].values  # Extract features as a NumPy array
        features_reshaped = features_array.reshape(-1, 1, features_array.shape[1])  # Reshape for prediction
        snake_prediction = snake_vms_scheduling_model.predict(features_reshaped)  # Predict in a single batch
        snake_predicted_ids = np.argmax(snake_prediction, axis=1) # Get predicted IDs and adjust
        new_y=label_encoder.inverse_transform(snake_predicted_ids)
        df['SNAKE_predicted_VM'] = new_y  # Add predictions to the DataFrame
    elif (model_name=="ENSEMBLE"):
        features_array = df[features_cols].values
        ensemble_predicted_ids = ensemble_vms_model.predict(features_array)
        new_y = label_encoder.inverse_transform(ensemble_predicted_ids)
        df['ENSEMBLE_predicted_VM'] = new_y  # Add predictions to the DataFrame
    else:
        print("model name not found")
        
    
    df.to_csv(df_path, index=False)
    return df_path  
# predicted_dataset=SavePredictedDataBase('ENSEMBLE',df, features_cols=['TaskFileSize', 'TaskOutputFileSize', 'TaskFileLength', 'CpuTime','TotalLength','UserLatitude', 'UserLongitude'])
predicted_dataset=savePredictedVMsScedulingDataBase(df,'SNAKE', features_cols=['TaskFileSize', 'TaskOutputFileSize', 'TaskFileLength', 'CpuTime','TotalLength'])

print(predicted_dataset)
