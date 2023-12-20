
def caculate_Results(task_info,data_center_info):
    print("  ---------task info  ",task_info)
    print("---------- dc info",data_center_info)
    taskCpuCost= task_info['TaskFileSize'] * data_center_info['DataCenterCpuCost'] 
    taskBWCost= task_info['TaskFileSize'] *  data_center_info['DataCenterBwCost']  
    taskStorageCost=  task_info['TaskFileSize'] *  data_center_info['DataCenterStorageCost']  
    taskRamCost=  task_info['TaskFileSize'] *  data_center_info['DataCenterRamCost'] 
    taskTotalCost=taskCpuCost+taskBWCost+taskStorageCost+taskRamCost
  
    Costs_dict={
            "taskCpuCost":taskCpuCost,
            "taskBWCost":taskBWCost,
            "taskRamCost":taskRamCost,
            "taskStorageCost":taskStorageCost,
            "taskTotalCost":taskTotalCost
    }
    network_delay = data_center_info['NetworkDelay']
    processing_time = task_info['TaskOutputFileSize'] /   network_delay



    return {
        'Task-processing-Cost': Costs_dict,
        'Task-processing-Delay': processing_time
    }

