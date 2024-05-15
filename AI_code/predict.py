import pandas as pd
import numpy as np
from joblib import load

from keras.models import load_model
import warnings

from sklearn.preprocessing import LabelEncoder


warnings.filterwarnings("ignore", category=DeprecationWarning, module="tensorflow")
warnings.resetwarnings()
df_path                                 ="dataset/predictedDataBase.csv"
predicted_df_path                       =df_path
vms_scheduling_df_path                  ="dataset/vms_predicted_dataset.csv"
predicted_VMs_Scheduling_df_path        =vms_scheduling_df_path

ga_model           =load_model("models/ga_model_86.keras")
snake_model        =load_model("models/snake_model_91.keras")
new_model        =load_model("models/model2.keras")
snake_vms_scheduling_model=load_model("models/snake_vms_scheduling_86.keras")
ensemble_model    =load("models/ensemble_model_85.joblib")
ensemble_vms_model    =load("models/enesemble_vms_scheduling_85.joblib")

df = pd.read_csv(df_path)  
vms_df=pd.read_csv(vms_scheduling_df_path)

def predict_datacenter_id(task_info,modelName):
    features = np.array([[task_info]]).reshape(1, 1, -1)
    return np.argmax(modelName.predict(features))+3

def predict_VM_id(task_info,modelName):
    features = np.array([[task_info]]).reshape(1, 1, -1)
    return np.argmax(modelName.predict(features))

def SavePredictedDataBase(model_name,df, features_cols ):
    """Predicts datacenter IDs using Array operations."""
    if (model_name != "ensemble"):
        features_array = df[features_cols].values  # Extract features as a NumPy array
        features_reshaped = features_array.reshape(-1, 1, features_array.shape[1])  # Reshape for prediction
        
        ga_predictions = ga_model.predict(features_reshaped)  # Predict in a single batch
        snake_prediction = snake_model.predict(features_reshaped)  # Predict in a single batch
        new_model_prediction = new_model.predict(features_reshaped)  # Predict in a single batch
        
        ga_predicted_ids = np.argmax(ga_predictions, axis=1) + 3  # Get predicted IDs and adjust
        snake_predicted_ids = np.argmax(snake_prediction, axis=1) + 3  # Get predicted IDs and adjust
        new_model_predicted_ids = np.argmax(new_model_prediction, axis=1) + 3  # Get predicted IDs and adjust

        df['GA_predicted_DC'] = ga_predicted_ids  # Add predictions to the DataFrame
        df['SNAKE_predicted_DC'] = snake_predicted_ids  # Add predictions to the DataFrame
        df['new_model_predicted_DC'] = new_model_predicted_ids  # Add predictions to the DataFrame
    elif model_name == "ensemble":
            features_array = df[features_cols].values  # Extract features as a NumPy array
            ensemble_predicted_ids = ensemble_model.predict(features_array)+3
            df['ENSEMBLE_predicted_DC'] = ensemble_predicted_ids  # Add predictions to the DataFrame
            
    df.to_csv(predicted_df_path, index=False)
    return predicted_df_path


def savePredictedVMsScedulingDataBase(df,model_name, features_cols):
    
    y=df['VmID']
    label_encoder = LabelEncoder()
    y = label_encoder.fit_transform(y)
    if (model_name=="snake"):
        features_array = df[features_cols].values  # Extract features as a NumPy array
        features_reshaped = features_array.reshape(-1, 1, features_array.shape[1])  # Reshape for prediction
        snake_prediction = snake_vms_scheduling_model.predict(features_reshaped)  # Predict in a single batch
        snake_predicted_ids = np.argmax(snake_prediction, axis=1) # Get predicted IDs and adjust
        new_y=label_encoder.inverse_transform(snake_predicted_ids)
        df['SNAKE_predicted_VMID'] = new_y  # Add predictions to the DataFrame
    elif (model_name=="ensemble"):
        features_array = df[features_cols].values
        ensemble_predicted_ids = ensemble_vms_model.predict(features_array)
        new_y = label_encoder.inverse_transform(ensemble_predicted_ids)
        df['ENSEMBLE_predicted_VMID'] = new_y  # Add predictions to the DataFrame
    else:
        print("model name not found")
        
    
    df.to_csv(predicted_VMs_Scheduling_df_path, index=False)
    return predicted_VMs_Scheduling_df_path  
# newdataset=SavePredictedDataBase(df, features_cols=['TaskID', 'TaskFileSize', 'TaskOutputFileSize', 'TaskFileLength','CpuTime', 'TotalLength'])
newdataset=SavePredictedDataBase('ensemble',df, features_cols=['TaskFileSize', 'TaskOutputFileSize', 'TaskFileLength', 'UserLatitude', 'UserLongitude'])

print(newdataset)
