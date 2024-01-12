
import pandas as pd


def caculate_Results(task_info,data_center_info):
    print("  ---------task info  ",task_info)
    print("---------- dc info",data_center_info)
    taskCpuCost= task_info[1] * data_center_info['DataCenterCpuCost'] 
    taskBWCost= task_info[1] *  data_center_info['DataCenterBwCost']  
    taskStorageCost=  task_info[1] *  data_center_info['DataCenterStorageCost']  
    taskRamCost=  task_info[1] *  data_center_info['DataCenterRamCost'] 
    taskTotalCost=taskCpuCost+taskBWCost+taskStorageCost+taskRamCost
  
    Costs_dict={
            "taskCpuCost":taskCpuCost,
            "taskBWCost":taskBWCost,
            "taskRamCost":taskRamCost,
            "taskStorageCost":taskStorageCost,
            "taskTotalCost":taskTotalCost
    }
    network_delay = data_center_info['NetworkDelay']
    processing_time = task_info[2] /   network_delay



    return {
        'Task-processing-Cost': Costs_dict,
        'Task-processing-Delay': processing_time
    }


def GetDatacenterCharacterestic(datacenter_id):
    # Assuming your CSV file is in the same directory as your script
    try:
        df = pd.read_csv('C:/Users/mohsal/Desktop/app/metalux/cloudsim/ga_lstm/AI_code/dataset/dataset_2.csv')
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
